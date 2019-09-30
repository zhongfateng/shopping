package com.liuwa.shopping.activity;

/**
 * Created by ZFT on 2019/9/27.
 */

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.liuwa.shopping.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_layout);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainTabActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000 * 3);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
