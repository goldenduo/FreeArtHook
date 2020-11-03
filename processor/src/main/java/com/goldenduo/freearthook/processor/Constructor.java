package com.goldenduo.freearthook.processor;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class Constructor {
    private Filer mFiler;
    private Elements mElementUtils;
    private int mProguardIndex;
    private Messager mMessager;
    public Constructor(ProcessingEnvironment processingEnvironment){
        mFiler=processingEnvironment.getFiler();
        mElementUtils=processingEnvironment.getElementUtils();
        mMessager=processingEnvironment.getMessager();
    }



    public void buildHelper(HookRecord record){
        TypeElement classElement=record.getClassElement();
        ExecutableElement methodElement=record.getMethodElement();
        HookClass classHook=classElement.getAnnotation(HookClass.class);
        HookMethod methodHook=methodElement.getAnnotation(HookMethod.class);
        String packageName = mElementUtils.getPackageOf(classElement).getQualifiedName().toString();
        String helperClassName=classElement.getSimpleName()+"Helper";
        String genClassName="Proguard"+mProguardIndex;


        //build
        StringBuilder builder=new StringBuilder();
        builder.append("package "+packageName+";\n");
        builder.append("import android.os.Build;\n");

//
//        try {
//            JavaFileObject jfo = mFiler.createSourceFile(packageName+"."+helperClassName, classElement,methodElement);
//            Writer writer = jfo.openWriter();
////            writer.write(brewCode(pkName, bindViewFiledClassType, bindViewFiledName, id));
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void note(String format, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }
}
