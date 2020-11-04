package com.goldenduo.freearthook.processor;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public class HookRecord {
    //gen file, hook to target class
    String genPackageName;
    String genClassName;
    String genMethodName;
    String genBackupName;
    String genMethodParams;
    String genMethodParamsWithoutType;
    //hooked implementation
    TypeElement classElement;
    ExecutableElement methodElement;

    //backup
    String helperClassName;
    String helperMethodParams;
    String helperMethodParamsWithoutType;
    String helperMethodParamsOnlyType;
    //backup and real hook
    String packageName;
    String methodSig;
    //eg. return 1
    String returnResult;
    //eg. void
    String methodReturn;
    String methodName;
    HookClass classHook;
    HookMethod methodHook;
    static int sProguardIndex;
    public HookRecord(ExecutableElement methodElement,ProcessingEnvironment processingEnvironment) {
        this.methodElement = methodElement;
        fill(processingEnvironment);
    }

    private void fill(ProcessingEnvironment processingEnvironment) {
        int index=0;
        Elements elements=processingEnvironment.getElementUtils();
        classElement = (TypeElement) methodElement.getEnclosingElement();
        checkFormat();
        classHook = classElement.getAnnotation(HookClass.class);
        methodHook = methodElement.getAnnotation(HookMethod.class);
        packageName = elements.getPackageOf(classElement).getQualifiedName().toString();
        methodName = methodElement.getSimpleName().toString();

        List<? extends VariableElement> variableElements= methodElement.getParameters();
        StringBuilder methodSigBuilder=new StringBuilder();
        StringBuilder helperMethodParamsBuilder=new StringBuilder();
        StringBuilder genMethodParamsBuilder=new StringBuilder();
        StringBuilder genMethodParamsWithoutTypeBuilder=new StringBuilder();
        StringBuilder helperMethodParamsWithoutTypeBuilder=new StringBuilder();
        StringBuilder helperMethodParamsOnlyTypeBuilder=new StringBuilder();
        if (variableElements.isEmpty()){
            methodSigBuilder.append("()");
        }
        //params
        for (int i=0;i<variableElements.size();i++){
            VariableElement element=variableElements.get(i);
            TypeMirror typeMirror=element.asType();
            String type=typeMirror.toString();
            if (type.contains("<")){
               type= type.substring(0,type.indexOf("<"));
            }
            String name=element.getSimpleName().toString();
            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE,type+" "+name);
            String proguardName="p"+index++;
            genMethodParamsBuilder.append(type);
            genMethodParamsBuilder.append(" ");
            genMethodParamsBuilder.append(proguardName);
            if (i!=variableElements.size()-1){
                genMethodParamsBuilder.append(',');
            }

            genMethodParamsWithoutTypeBuilder.append(proguardName);
            if (i!=variableElements.size()-1){
                genMethodParamsWithoutTypeBuilder.append(',');
            }

            helperMethodParamsBuilder.append(type);
            helperMethodParamsBuilder.append(" ");
            helperMethodParamsBuilder.append(name);
            if (i!=variableElements.size()-1){
                helperMethodParamsBuilder.append(',');
            }

            helperMethodParamsWithoutTypeBuilder.append(name);
            if (i!=variableElements.size()-1){
                helperMethodParamsWithoutTypeBuilder.append(',');
            }
            helperMethodParamsOnlyTypeBuilder.append(',');
            helperMethodParamsOnlyTypeBuilder.append(type);
            helperMethodParamsOnlyTypeBuilder.append(".class");

            if (i==0&&"thiz".equals(name)){
                if (variableElements.size()==1){
                    methodSigBuilder.append("()");
                }else{
                    methodSigBuilder.append("(");
                }
                continue;
            }else if (i==0){
                methodSigBuilder.append("(");
            }
            methodSigBuilder.append(getTypeSign(type));
            if (i==variableElements.size()-1){
                methodSigBuilder.append(")");

            }

        }
        //return
        if (methodElement.getReturnType().getKind()== TypeKind.VOID){
            methodSigBuilder.append("V");
            methodReturn="void";
            returnResult="";
        }else{
            TypeMirror returnType=methodElement.getReturnType();
            methodSigBuilder.append(getTypeSign(returnType.toString()));
            methodReturn=returnType.toString();
            if (returnType.getKind().isPrimitive()) {
                returnResult="return 0;";
            }else {
                returnResult="return null;";
            }
        }
        methodSig=methodSigBuilder.toString();
        genMethodParams=genMethodParamsBuilder.toString();
        genMethodParamsWithoutType=genMethodParamsWithoutTypeBuilder.toString();
        helperMethodParams=helperMethodParamsBuilder.toString();
        helperMethodParamsWithoutType=helperMethodParamsWithoutTypeBuilder.toString();
        helperMethodParamsOnlyType=helperMethodParamsOnlyTypeBuilder.toString();



        helperClassName = classElement.getSimpleName() + "Helper";
        genClassName = getGenClassName(classHook);
        genPackageName ="com.goldenduo.freearthook.gen";
        genMethodName ="p"+ sProguardIndex++;
        genBackupName ="p"+ sProguardIndex++;
    }

    private void checkFormat() {
        StringBuilder errorInfoBuilder=new StringBuilder();
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)){
            errorInfoBuilder.append(classElement+" should be public");
        }
        if (!methodElement.getModifiers().contains(Modifier.STATIC)){
            errorInfoBuilder.append(classElement+"."+methodElement+" should be static");
        }
        if (!methodElement.getModifiers().contains(Modifier.PUBLIC)){
            errorInfoBuilder.append(classElement+"."+methodElement+" should be public");
        }
        String errorInfo=errorInfoBuilder.toString();
        if (!errorInfo.isEmpty()){
            throw new RuntimeException(errorInfo);
        }
    }

    // real class name -> gen class name
    private static Map<String,String> sGenClassNameMap=new HashMap<>();
    private static String getGenClassName(HookClass classHook){
        if (!sGenClassNameMap.containsKey(classHook.value())){
            sGenClassNameMap.put(classHook.value(),"Proguard" + sProguardIndex++);
        }
        return sGenClassNameMap.get(classHook.value());
    }

    private static String getTypeSign(String type){
        int arrayCount=0;
        if (type.contains("[")){
            int startIndex=type.indexOf('[');
            arrayCount=(type.length()-startIndex)/2;
            type=type.substring(0,startIndex);
        }
        if (type.equals("boolean")){
            type="Z";
        }else if (type.equals("byte")){
            type="B";
        }else if (type.equals("char")){
            type="C";
        }else if (type.equals("short")){
            type="S";
        }else if (type.equals("int")){
            type="I";
        }else if (type.equals("long")){
            type="J";
        }else if (type.equals("float")){
            type="F";
        }else if (type.equals("double")){
            type="D";
        }else{
            type=type.replace('.','/');
            type="L"+type+";";
        }
        for (int i=0;i<arrayCount;i++){
            type="["+type;
        }
        return type;
    }


    public TypeElement getClassElement() {
        return classElement;
    }


    public ExecutableElement getMethodElement() {
        return methodElement;
    }




}
