package com.goldenduo.freearthook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.goldenduo.freearthook.anotation.HookClass;
import com.goldenduo.freearthook.anotation.HookMethod;
import com.goldenduo.freearthook.demo.DemoHelper;

import java.util.List;

@HookClass("fff3f22")
public class MainActivity extends AppCompatActivity {
    static{
        DemoHelper.freeArtHook();
    }
    @HookMethod("fff3f22")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @HookMethod("fff3f22ff")
    void onFFF(List<? extends Integer> ff){

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}