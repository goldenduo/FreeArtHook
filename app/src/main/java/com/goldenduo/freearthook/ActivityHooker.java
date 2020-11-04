package com.goldenduo.freearthook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;

@HookClass("com.goldenduo.freearthook.MainActivity")
public class ActivityHooker {
    private static final String TAG="ActivityHooker";

    public static void onCreate(Activity thiz, Bundle savedInstanceState){
//        Log.d(TAG,"before onCreate"+thiz);
//        ActivityHookerHelper.onCreate(thiz,savedInstanceState);
//        Log.d(TAG,"after onCreate"+thiz);
    }
    @HookMethod
    public static String get(MainActivity thiz){
        Log.d(TAG,"before get"+thiz);
        return "ActivityHooker"+ActivityHookerHelper.get(thiz);
    }
}
