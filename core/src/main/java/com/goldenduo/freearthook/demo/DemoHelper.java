package com.goldenduo.freearthook.demo;

import android.os.Build;

import com.goldenduo.freearthook.Bridge;
import com.goldenduo.freearthook.demo.gen.Proguard;
import com.goldenduo.freearthook.demo.gen.Proguard2;

public class DemoHelper {
    public static void onCreate(android.app.Activity thiz, android.os.Bundle bundle) {
        Proguard2.onCreate(thiz,bundle);
    }
    public static void freeArtHook() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 0 && sdkInt <= Integer.MAX_VALUE) {
            if (sdkInt >= 0 && sdkInt <= Integer.MAX_VALUE) {
                Bridge.hook(android.app.Activity.class, Proguard.class, Proguard2.class, "onCreate","onCreate","onCreate", "(Landroid/os/Bundle;)V", android.app.Activity.class, android.os.Bundle.class);
            }
        }
    }
}
