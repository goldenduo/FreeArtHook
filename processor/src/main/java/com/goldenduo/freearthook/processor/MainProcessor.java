package com.goldenduo.freearthook.processor;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class MainProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Messager mMessager;
    private Elements mElementUtils;
    private Constructor mConstructor;
    private ProcessingEnvironment mProcessingEnvironment;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mProcessingEnvironment=processingEnvironment;
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        mConstructor = new Constructor(processingEnvironment);

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(HookMethod.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> hookMethodElements = roundEnvironment.getElementsAnnotatedWith(HookMethod.class);
        Map<HookClass, List<HookRecord>> hooksMap=new HashMap<>();
        for (Element methodElement:hookMethodElements){

            HookRecord record=new HookRecord((ExecutableElement)methodElement,processingEnv);

            List<HookRecord> records=hooksMap.get(record.classHook);
            if (records==null){
                records=new ArrayList<>();
                hooksMap.put(record.classHook,records);
            }
            records.add(record);
        }
        for (HookClass hookClass:hooksMap.keySet()) {
            mConstructor.buildGen(hooksMap.get(hookClass));
            mConstructor.buildHelper(hooksMap.get(hookClass));

        }



        return true;
    }



    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void note(String format, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }
}