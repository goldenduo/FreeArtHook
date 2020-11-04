package com.goldenduo.freearthook.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class Constructor {
    private Filer mFiler;
    private Elements mElementUtils;
    private int mProguardIndex;
    private Messager mMessager;

    public Constructor(ProcessingEnvironment processingEnvironment) {
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
    }

    public void buildGen(List<HookRecord> records) {
        if (records.isEmpty()) {
            return;
        }
        HookRecord record = records.get(0);
        StringBuilder builder = new StringBuilder();
        builder.append(
                "package " + record.genPackageName + ";\n" +
                "import " + record.packageName + "." + record.classElement.getSimpleName().toString() + ";\n" +
                "public class " + record.genClassName + " {\n");
        for (HookRecord item:records) {
            boolean canReturn = !item.methodReturn.equals("void");
            builder.append(
                    "    public static "+item.methodReturn+" "+item.genMethodName+"("+item.genMethodParams+") {\n" +
                            "        "+ (canReturn ? "return " : "")+item.classElement.getSimpleName().toString()+"."+item.methodName+"("+item.genMethodParamsWithoutType+");\n" +
                            "    }\n");
            builder.append(
                    "    public static "+item.methodReturn+" "+item.genBackupName+"("+item.genMethodParams+") {\n" +
                            item.returnResult +
                            "    }\n");
        }
        builder.append("}");
        writeJavaFile(record.genPackageName + "." + record.genClassName, builder.toString(), records);
    }

    public void buildHelper(List<HookRecord> records) {

        if (records.isEmpty()) {
            return;
        }
        HookRecord record = records.get(0);

        //build
        StringBuilder builder = new StringBuilder();
        builder.append("package " + record.packageName + ";\n");
        builder.append(
                "import android.os.Build;\n" +
                        "import com.goldenduo.freearthook.Bridge;\n" +
                        "import " + record.genPackageName + "." + record.genClassName + ";\n");
        builder.append(
                "public class " + record.helperClassName + " {\n");
        for (HookRecord item : records) {
            boolean canReturn = !item.methodReturn.equals("void");
            builder.append(
                    "    public static " + item.methodReturn + " " + item.methodName + "(" + item.helperMethodParams + ") {\n" +
                            "        " + (canReturn ? "return " : "") + item.genClassName + "." + item.genBackupName + "(" + item.helperMethodParamsWithoutType + ");\n" +
                            "    }\n");
        }
        builder.append(
                "    public static void freeArtHook() {\n" +
                        "        int sdkInt = Build.VERSION.SDK_INT;\n" +
                        "        if (sdkInt >= " + record.classHook.minApi() + " && sdkInt <= " + record.classHook.maxApi() + ") {\n");
        for (HookRecord item : records) {
            builder.append(
                    "            if (sdkInt >= " + item.methodHook.minApi() + " && sdkInt <= " + item.methodHook.maxApi() + ") {\n" +
                            "                Bridge.hook(" + item.classHook.value() + ".class, " + item.genPackageName + "." + item.genClassName + ".class, " + item.genPackageName + "." + item.genClassName + ".class, \"" + item.methodName + "\", \"" + item.genMethodName + "\", \"" + item.genBackupName + "\", \"" + item.methodSig + "\"" + item.helperMethodParamsOnlyType + ");\n" +
                            "            }\n");
        }
        builder.append(
                "        }\n" +
                        "    }\n" +
                        "}");


        writeJavaFile(record.packageName + "." + record.helperClassName, builder.toString(), records);
    }

    private void writeJavaFile(String fileFullName, String content, List<HookRecord> hookRecords) {
        Element[] affectElements = new Element[hookRecords.size() + 1];
        for (int i = 0; i < affectElements.length - 1; i++) {
            affectElements[i] = hookRecords.get(i).methodElement;
        }
        affectElements[affectElements.length - 1] = hookRecords.get(0).classElement;
        try {
            JavaFileObject jfo = mFiler.createSourceFile(fileFullName, (Element[]) affectElements);
            Writer writer = jfo.openWriter();
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void note(String format, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(format, args));
    }
}
