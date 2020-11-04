package com.goldenduo.freearthook;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityHookerHelper.freeArtHook();
    }
}
