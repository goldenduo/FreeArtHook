package com.goldenduo.freearthook;

import android.util.Log;

import java.lang.reflect.Method;

import lab.galaxy.yahfa.HookMain;

public class Bridge {
    private static final String TAG="Bridge";
    public static void hook(Class<?> targetClass,Class<?> hookClass,Class<?> backupClass,String methodName,String genMethodName,String genBackupName,String methodSig,Class<?>... parameterTypes){
        Log.e(TAG,"targetClass="+targetClass);
        Log.e(TAG,"methodName="+methodName);
        Log.e(TAG,"methodSig="+methodSig);
        try {
            Method hook = hookClass.getDeclaredMethod(genMethodName, parameterTypes);
            Log.e(TAG,"hook="+hook);
            Method backup = backupClass.getDeclaredMethod(genBackupName, parameterTypes);
            Log.e(TAG,"backup="+backup);
            HookMain.findAndBackupAndHook(targetClass, methodName, methodSig, hook, backup);
        }catch (NoSuchMethodException e){
            Log.e(TAG,"hook fail: no such method:\n"+e.toString());
        }
    }
}
