package com.goldenduo.freearthook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;
import com.goldenduo.freearthook.demo.DemoHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"I am MainActivity"+this);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"get = "+get());
    }
    public int onFFF(Integer[][] ff){
        return 2;
    }
    public String get(){
        Log.d(TAG,"before get");
        return "MainActivity";
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}