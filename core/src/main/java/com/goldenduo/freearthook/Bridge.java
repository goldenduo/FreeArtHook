package com.goldenduo.freearthook;

import android.util.Log;

import java.lang.reflect.Method;

import lab.galaxy.yahfa.HookMain;

public class Bridge {
    private static final String TAG="Bridge";
    public static void hook(Class<?> targetClass,Class<?> hookClass,Class<?> backupClass,String methodName,String methodSig,Class<?>... parameterTypes){
        try {
            Method hook = hookClass.getDeclaredMethod(methodName, parameterTypes);
            Method backup = backupClass.getDeclaredMethod(methodName, parameterTypes);
            HookMain.findAndBackupAndHook(targetClass, methodName, methodSig, hook, backup);
        }catch (NoSuchMethodException e){
            Log.e(TAG,"hook fail: no such method:\n"+e.toString());
        }
    }
}
