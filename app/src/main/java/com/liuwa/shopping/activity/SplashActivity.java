package com.liuwa.shopping.activity;

/**
 * Created by ZFT on 2019/9/27.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.ShoppingApplication;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.util.SPUtils;

public class SplashActivity extends AppCompatActivity {
    public CheckBox checkBox;

    public TextView tv_xy,tv_ys;

    public TextView go_to;
    public LinearLayout ll_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_layout);
        initView();

       String is_agress= ApplicationEnvironment.getInstance().getPreferences().getString("is_agress","0");
       if(is_agress.equals("0"))
       {
           ll_show.setVisibility(View.VISIBLE);
       }else
       {
           ll_show.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainTabActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000 * 3);
       }
    }

    public void initView()
    {
        ll_show=(LinearLayout)findViewById(R.id.ll_show);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        tv_xy=(TextView)findViewById(R.id.tv_xy);
        tv_ys=(TextView)findViewById(R.id.tv_ys);
        go_to=(TextView)findViewById(R.id.go_to);
        tv_xy.setOnClickListener(onClickListener);
        tv_ys.setOnClickListener(onClickListener);
        go_to.setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.tv_xy:
                    Intent intent2 =new Intent(SplashActivity.this,UserAgreeActivity.class);
                    startActivity(intent2);
                    break;

                case  R.id.tv_ys:
                    Intent intent3 =new Intent(SplashActivity.this,UserAgree1Activity.class);
                    startActivity(intent3);
                    break;
                case  R.id.go_to:
                    if(checkBox.isChecked())
                    {
                       SharedPreferences.Editor editor= ApplicationEnvironment.getInstance().getPreferences().edit();
                       editor.putString("is_agress","1");
                       editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, MainTabActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Toast.makeText(SplashActivity.this,"请勾选用户协议",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
