package com.liuwa.shopping.activity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TabHost;
import android.widget.Toast;
import com.liuwa.shopping.R;

import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.TabsUtil;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

	public static int maxTabIndex = 3;
	public static String TAB_INDEX_KEY = "TAB_INDEX_KEY";
	public static String ACTION_TAB_INDEX = "ACTION_TAB_INDEX";
	private TabHost mTabHost;
	private long exitTime = 0;

	private TabReceiver mTabReceiver;
	private String token;
	public static  Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main_tab_activity);
			this.context=this;
			init();
			//initTabHost();
			mTabReceiver = new TabReceiver();
			IntentFilter filter = new IntentFilter(ACTION_TAB_INDEX);
			registerReceiver(mTabReceiver, filter);
			requestPermissions();
			initTabHost();
		//	checkVersionUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
	IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
	//请求权限
	private boolean requestPermissions(){
		//需要请求的权限
		/**
		 * Manifest.permission.ACCESS_COARSE_LOCATION,
		 Manifest.permission.ACCESS_FINE_LOCATION,
		 Manifest.permission.WRITE_EXTERNAL_STORAGE,
		 Manifest.permission.READ_EXTERNAL_STORAGE,
		 Manifest.permission.READ_PHONE_STATE
		 * **/
		String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION};
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
		}else{
			//输出授权结果
			Toast.makeText(context,"请给APP授权，否则部分功能无法正常使用！",Toast.LENGTH_LONG).show();
		}
		Intent intb=new Intent();
		intb.setAction(IndexActivity.ACTION_LOCATION);
		sendBroadcast(intb);//发送标准广播
	}
	
	public void init(){
		 token=ApplicationEnvironment.getInstance().getPreferences().getString(Constants.TOKEN, "");
	}
	
	private void initTabHost() {
		mTabHost = (TabHost) getTabHost();
		TabsUtil.addTab(mTabHost, "首页", R.drawable.tab_nav_index_selector, 0, new Intent(this, IndexActivity.class));
		TabsUtil.addTab(mTabHost, "分类", R.drawable.tab_nav_cate_selector, 1, new Intent(this, ProductShowByCategroyActivity.class));
		TabsUtil.addTab(mTabHost, "购物车", R.drawable.tab_nav_cart_selector, 2, new Intent(this, CartShopActivity.class));
		TabsUtil.addTab(mTabHost, "我的", R.drawable.tab_nav_myself_selector, 3, new Intent(this, MySelfActivity.class));

		for (int i = 0; i < 4; i++) {
			final int id = i;
			mTabHost.getTabWidget().getChildAt(id).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(id==0) {
						mTabHost.setCurrentTab(0);
					}
					if(id==1) {
						mTabHost.setCurrentTab(1);
					}
					if(id==2) {
						if(token==null||token.length()==0){
							Intent intent =new Intent(context,LoginActivity.class);
							startActivity(intent);
							return;
						}
						mTabHost.setCurrentTab(2);
					}
					if(id==3) {
						if(token==null||token.length()==0){
							Intent intent =new Intent(context,LoginActivity.class);
							startActivity(intent);
						}else {
							mTabHost.setCurrentTab(3);
						}
					}
					
				}
			});
			mTabHost.getTabWidget().getChildAt(id).setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (id == 3) {
					}
				}
			});
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	//	Logger.logError("=======MainTabActivity onNewIntent===========");
		// if (intent != null) {
		// int tabNum = intent.getIntExtra(Settings.TABHOST, 0);
		// mTabHost.setCurrentTab(tabNum);
		// currentView = tabNum;
		// }
//		if (intent != null) {
//			int tabNum = intent.getIntExtra("flag", 0);
//			mTabHost.setCurrentTab(tabNum);
//			
//		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTabReceiver != null) {
			unregisterReceiver(mTabReceiver);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		 token=ApplicationEnvironment.getInstance().getPreferences().getString(Constants.TOKEN, "");
	}
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.clear();
		onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				onKeyDown(KeyEvent.KEYCODE_BACK, event);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@SuppressLint("WrongConstant")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
//			if (mTabHost.getCurrentTab()==3) {
//				mTabHost.setCurrentTab(3);
//			}else{
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序", 0).show();
					exitTime = System.currentTimeMillis();
				} else {
					// String str = JsonUtils.toJsonStr(GetString.userInfo);
					// if (!TextUtils.isEmpty(str))
					// SharedPreferencesOperate.updateUserInfo(this,
					// SharedPreferencesOperate.SAVE_USER_INFO, str);
					
					logout();
					finish();
				}
		//	}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 注销
	 */
	public void logout() {
		// GetString.isLogin = false;
		// GetString.userInfo = null;
		// MessageCenterDetails2Activity.Count = 0;
		// if (mTabHost.getCurrentTab() == Settings.USERHOME) {
		// mTabHost.setCurrentTab(0);
		// }
	}

	class TabReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			int index = intent.getIntExtra(TAB_INDEX_KEY, 0);
			if (index <= maxTabIndex) {
				if(index==1){
//					int position=intent.getIntExtra("position",0);
//					Intent intb=new Intent();
//					intb.setAction(ProductShowByCategroyActivity.ACTION_POSATION);
//					intb.putExtra("position",position);
//					sendBroadcast(intb);//发送标准广播
					mTabHost.setCurrentTab(index);
				}else{
					mTabHost.setCurrentTab(index);
				}

			}
		}

	}

}
