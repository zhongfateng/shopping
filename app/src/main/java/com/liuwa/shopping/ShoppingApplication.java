package com.liuwa.shopping;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.x;

public class ShoppingApplication extends Application {
	
	
    private static ShoppingApplication mInstance = null;
    public static final String strKey = "KGbf7XtlNsbSjkBZE4kVsXKG";
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		x.Ext.init(this);
		x.Ext.setDebug(true);
		initImageLoader(getApplicationContext());
	}


	 /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
	public static ShoppingApplication getInstance() {
		if(mInstance==null)
		{
			mInstance=new ShoppingApplication();
		}
		return mInstance;
	}


	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}