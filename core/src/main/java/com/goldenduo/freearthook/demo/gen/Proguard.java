package com.goldenduo.freearthook.demo.gen;

import android.app.Activity;
import android.os.Bundle;

import com.goldenduo.freearthook.demo.Demo;

public class Proguard {
    public static void onCreate(Activity thiz, Bundle savedInstanceState) {
        Demo.onCreate(thiz, savedInstanceState);
    }
}
