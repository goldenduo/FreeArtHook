package com.goldenduo.freearthook.demo;

import android.app.Activity;
import android.os.Bundle;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;

@HookClass("android.app.Activity")
public class Demo {
    @HookMethod
    public static void onCreate(Activity thiz, Bundle savedInstanceState) {
        DemoHelper.onCreate(thiz, savedInstanceState);
    }
}
