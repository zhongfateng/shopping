package com.liuwa.shopping.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.liuwa.shopping.util.TabsUtil;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

	public static int maxTabIndex = 3;
	public static String TAB_INDEX_KEY = "TAB_INDEX_KEY";
	public static String ACTION_TAB_INDEX = "ACTION_TAB_INDEX";
	private TabHost mTabHost;
	private long exitTime = 0;

	private TabReceiver mTabReceiver;
	private String uid;
	private String token;
	public static  Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main_tab_activity);
			this.context=this;
			init();
			initTabHost();
			mTabReceiver = new TabReceiver();
			IntentFilter filter = new IntentFilter(ACTION_TAB_INDEX);
			registerReceiver(mTabReceiver, filter);
		//	checkVersionUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void init(){
		 uid= ApplicationEnvironment.getInstance().getPreferences().getString(Constants.USER_ID, "");
		 token=ApplicationEnvironment.getInstance().getPreferences().getString(Constants.TOKEN, "");
	}
	
	private void initTabHost() {
		mTabHost = (TabHost) getTabHost();
		TabsUtil.addTab(mTabHost, "首页", R.drawable.tab_nav_index_selector, 0, new Intent(this, IndexActivity.class));
		TabsUtil.addTab(mTabHost, "分类", R.drawable.tab_nav_cate_selector, 1, new Intent(this, RegisterAndGetPassWordActivity.class));
		TabsUtil.addTab(mTabHost, "购物车", R.drawable.tab_nav_cart_selector, 2, new Intent(this, FavoriateActivity.class));
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
						mTabHost.setCurrentTab(2);
					}
					if(id==3) {
						mTabHost.setCurrentTab(3);
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
		 uid= ApplicationEnvironment.getInstance().getPreferences().getString(Constants.USER_ID, "");
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
				mTabHost.setCurrentTab(index);
			}
		}

	}

}
