package com.liuwa.shopping.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;

import com.liuwa.shopping.client.ApplicationEnvironment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.view.LKAlertDialog;
import com.liuwa.shopping.view.LKProgressDialog;
import com.liuwa.shopping.view.LoadingDialog;

import java.util.ArrayList;
import java.util.Stack;

public class BaseActivity extends FragmentActivity {
	private static Stack<BaseActivity> stack = new Stack<BaseActivity>();
	public static final int ALL_DIALOG			= 3;
	public static final int Loading_DIALOG		= 4; //不可干预打转的对话框  样式
	private LoadingDialog  loadingDialog=null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private String message = null;

	 private Context mContext;
	 private final String mPageName = "BaseActivity";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext=this;
		stack.push(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		//stack.push(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK){
			this.setResult(Activity.RESULT_OK);
			this.finish();
		}
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		// 然后会调用 startActivityForResult();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void finish() {
		if (!stack.empty()){
			stack.pop();
		}
		super.finish();
	}
	
	public static BaseActivity getTopActivity(){
		try{
			return stack.peek();
			
		} catch(Exception e){
			// 重启系统
			ApplicationEnvironment.getInstance().restartApp();
			return new BaseActivity();
		}
		
	}
	
	public static ArrayList<BaseActivity> getAllActiveActivity() {
		if (null == stack || stack.isEmpty()){
			return null;
		}
		ArrayList<BaseActivity> list = new ArrayList<BaseActivity>();
		for (int i=0; i<stack.size(); i++){
			list.add(stack.get(i));
		}
		
		return list;
	}
	
	public static void popActivity(){
		try{
			stack.pop();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void showDialog(final int type, String message){
		this.message = message;
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				showDialog(type);
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case Loading_DIALOG:
			this.showLoadingDialog();
			break;
		}
		
		return super.onCreateDialog(id);
	}

	private void showLoadingDialog(){
		try{
			// 这里应该关闭其它提示型的对话框
			this.hideDialog(ALL_DIALOG);
			loadingDialog=new LoadingDialog(this);
			loadingDialog.setCanceledOnTouchOutside(false);  
			loadingDialog.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void hideDialog(int type){
		switch(type){
		case Loading_DIALOG:
			if(null!=loadingDialog&&loadingDialog.isShowing())
			{
				loadingDialog.dismiss();
			}
			break;

		default:
			if(null!=loadingDialog&&loadingDialog.isShowing())
			{
				loadingDialog.dismiss();
			}
			break;
		}
		
	}

	public void showToast(String message){
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	@SuppressWarnings("deprecation")
	protected DisplayMetrics getScreenPixel() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

//		Logger.logError("分辨率：" + dm.widthPixels + "x" + dm.heightPixels + ",精度：" + dm.density + ",densityDpi="
//				+ dm.densityDpi);
//		Logger.logError("Product Model: " + android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK + ","
//				+ android.os.Build.VERSION.RELEASE);
		return dm;
	}
	
}
