package com.goldenduo.freearthook.processor;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class HookRecord {
    //gen file, hook to target class
    String genPackageName;
    String genClassName;
    String genMethodName;
    String genBackupName;
    String genMethodParams;

    //hooked implementation
    TypeElement classElement;
    ExecutableElement methodElement;

    //backup
    String helperClassName;
    String helperMethodParams;

    //backup and real hook
    String packageName;
    String methodSign;

    HookClass classHook;
    HookMethod methodHook;
    static int sProguardIndex;
    public HookRecord(ExecutableElement methodElement,ProcessingEnvironment processingEnvironment) {
        this.methodElement = methodElement;
        fill(processingEnvironment);
    }

    private void fill(ProcessingEnvironment processingEnvironment) {
        Elements elements=processingEnvironment.getElementUtils();
        classElement = (TypeElement) methodElement.getEnclosingElement();


        classHook = classElement.getAnnotation(HookClass.class);
        methodHook = methodElement.getAnnotation(HookMethod.class);

        packageName = elements.getPackageOf(classElement).getQualifiedName().toString();
        List<? extends VariableElement> variableElements= methodElement.getParameters();
        for (int i=0;i<variableElements.size();i++){
            VariableElement element=variableElements.get(i);
            TypeMirror typeMirror=element.asType();
            String type=typeMirror.toString();
            if (type.contains("<")){
               type= type.substring(0,type.indexOf("<"));
            }
            if ("thiz".equals(element.getSimpleName())&&classHook.value().equals(type)){

            }
        }


        this.helperClassName = classElement.getSimpleName() + "Helper";
        this.genClassName = "Proguard" + sProguardIndex++;
    }
    private static String getMethodSig(){

    }

    public String getGenMethodParams() {
        return genMethodParams;
    }


    public String getMethodSign() {
        return methodSign;
    }


    public String getHelperMethodParams() {
        return helperMethodParams;
    }


    public String getGenPackageName() {
        return genPackageName;
    }


    public String getHelperClassName() {
        return helperClassName;
    }


    public String getPackageName() {
        return packageName;
    }


    public TypeElement getClassElement() {
        return classElement;
    }


    public ExecutableElement getMethodElement() {
        return methodElement;
    }


    public String getGenClassName() {
        return genClassName;
    }


    public String getGenMethodName() {
        return genMethodName;
    }


    public String getGenBackupName() {
        return genBackupName;
    }


}
