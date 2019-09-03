package com.liuwa.shopping.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.uri.FileProviderUtils;
import com.liuwa.shopping.util.uri.SystemProgramUtils;

import java.io.File;



public class MainActivity extends AppCompatActivity {
    Button btnPaizhao;
    Button btnXiangce;
    ImageView ivTupian;

    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    //初始化控件
    private void initView(){
        btnPaizhao = (Button) findViewById(R.id.paizhao);
        btnXiangce = (Button) findViewById(R.id.xiangce);
        ivTupian = (ImageView) findViewById(R.id.tupian);
    }

    //初始化事件
    private void initEvent(){
        //拍照
        btnPaizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!requestPermissions()){
                    return;
                }
                //SystemProgramUtils.paizhao(MainActivity.this, new File("/mnt/sdcard/tupian.jpg"));
                SystemProgramUtils.call(MainActivity.this);
            }
        });

        //相册
        btnXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!requestPermissions()){
                    return;
                }
                SystemProgramUtils.zhaopian(MainActivity.this);
            }
        });
    }

    //请求权限
    private boolean requestPermissions(){
        //需要请求的权限
        String[] permissions = {Manifest.permission.CALL_PHONE};
        //开始请求权限
        return requestPermissions.requestPermissions(
                this,
                permissions,
                PermissionUtils.ResultCode1);
    }

    //用户授权操作结果（可能授权了，也可能未授权）
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if(requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)){
            //请求的权限全部授权成功，此处可以做自己想做的事了
            //输出授权结果
            Toast.makeText(MainActivity.this,"授权成功，请重新点击刚才的操作！",Toast.LENGTH_LONG).show();
        }else{
            //输出授权结果
            Toast.makeText(MainActivity.this,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
        }
    }

    //拍照、相册、图片裁切结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
           return;
        }
        Uri filtUri;
        File outputFile = new File("/mnt/sdcard/tupian_out.jpg");//裁切后输出的图片
        switch (requestCode) {
            case SystemProgramUtils.REQUEST_CODE_PAIZHAO:
                //拍照完成，进行图片裁切
                File file = new File("/mnt/sdcard/tupian.jpg");
                filtUri = FileProviderUtils.uriFromFile(MainActivity.this, file);
                SystemProgramUtils.Caiqie(MainActivity.this, filtUri, outputFile);
                break;
            case SystemProgramUtils.REQUEST_CODE_ZHAOPIAN:
                //相册选择图片完毕，进行图片裁切
                if (data == null ||  data.getData()==null) {
                    return;
                }
                filtUri = data.getData();
                SystemProgramUtils.Caiqie(MainActivity.this, filtUri, outputFile);
                break;
            case SystemProgramUtils.REQUEST_CODE_CAIQIE:
                //图片裁切完成，显示裁切后的图片
                try {
                    Uri uri = Uri.fromFile(outputFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivTupian.setImageBitmap(bitmap);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
        }
    }
}
