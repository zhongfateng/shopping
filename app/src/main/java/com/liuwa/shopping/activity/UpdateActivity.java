package com.liuwa.shopping.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.ShoppingApplication;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.model.VersionModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DecimalFormat;


public class UpdateActivity extends BaseActivity{
	private Context context;
	IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
	IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
	public String apkurl;
	public ProgressBar pg_bar;
	public TextView tv_show;
	public String fileURL;
	public String fileName;
	public DownloadManager downloadManager;
	public long id;
	public TextView tv_title;
	public TextView tv_confirm,tv_cancel;
	public VersionModel versionModel;
	public LinearLayout ll_cancel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_show_layout);
		this.context=this;
		versionModel=(VersionModel) getIntent().getSerializableExtra("versionModel");
		initViews();
		initEvent();
//		if(!requestPermissions()){
//			return;
//		}
//		downloadAndOpen(context,apkurl,"update.apk");

	}

	public void initViews()
	{
		ll_cancel=(LinearLayout)findViewById(R.id.ll_cancel);
		pg_bar=(ProgressBar)findViewById(R.id.pg_bar);
		tv_show=(TextView)findViewById(R.id.tv_show);
		tv_confirm=(TextView)findViewById(R.id.tv_confirm);
		tv_cancel=(TextView)findViewById(R.id.tv_cancel);
		if(versionModel.forceUpdate.equals("1")){
			ll_cancel.setVisibility(View.GONE);
		}else{
			ll_cancel.setVisibility(View.VISIBLE);
		}

	}
	//请求权限
	private boolean requestPermissions(){
		//需要请求的权限
		/**
		 * Manifest.permission.ACCESS_COARSE_LOCATION,
		 Manifest.permission.ACCESS_FINE_LOCATION,
		 Manifest.permission.WRITE_EXTERNAL_STORAGE,
		 Manifest.permission.READ_EXTERNAL_STORAGE,
		 Manifest.permission.READ_PHONE_STATE

		 Manifest.permission.READ_EXTERNAL_STORAGE,
		 Manifest.permission.WRITE_EXTERNAL_STORAGE,
		 Manifest.permission.REQUEST_INSTALL_PACKAGES
		 ————————————————
		 版权声明：本文为CSDN博主「安卓开发架构」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
		 原文链接：https://blog.csdn.net/qq_43580100/article/details/90146053
		 * **/
		String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE};
		//开始请求权限
		return requestPermissions.requestPermissions(
				this,
				permissions,
				PermissionUtils.ResultCode1);
	}
	public void initEvent(){
		tv_confirm.setOnClickListener(onClickListener);
		tv_cancel.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				UpdateActivity.this.finish();
				break;
				case R.id.tv_cancel:
					ShoppingApplication app=(ShoppingApplication)getApplication();
					app.setTag(1);
					UpdateActivity.this.finish();
					break;
				case R.id.tv_confirm:
					if(!requestPermissions()){
						return;
					}
					downloadAndOpen(context,versionModel.apkurl,"update.apk");
					break;

			}
		}
	};
	//用户授权操作结果（可能授权了，也可能未授权）
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		//用户给APP授权的结果
		//判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
		if(requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)){
			//请求的权限全部授权成功，此处可以做自己想做的事了
			//输出授权结果
			//downLoadApk();
			Toast.makeText(context,"授权成功，请重新点击刚才的操作！",Toast.LENGTH_LONG).show();
		}else{
			//输出授权结果
			Toast.makeText(context,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
		}
	}

	// 下载任务
	class DownloadFileTask extends AsyncTask<Object, Object, Object> {
		InputStream inStream = null;
		FileOutputStream outStream = null;
		HttpURLConnection conn = null;


		int totalSize = 0;
		float totalMB = 0.0f;

		public DownloadFileTask(){

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Object doInBackground(Object... params) {

			try {
				URL url = new URL(fileURL);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(10000);
//				conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//				conn.setRequestMethod("POST");
//				conn.setDoOutput(true);
				conn.setUseCaches(false);
				HttpURLConnection.setFollowRedirects(true);

				conn.connect();

				inStream = conn.getInputStream();

				totalSize = conn.getContentLength(); // 获取响应文件的总大小

				// TODO exception totalSize = -1; stream == null, android UnknownlengthInputstream
				if (null == inStream || totalSize == -1){
					throw new ConnectException();
				}

				if (totalSize == 0){
					throw new IOException();
				}

				totalMB = format(totalSize);
				if (null != inStream){
					File file = new File(FileUtil.getDownloadPath(), fileName);
					outStream = new FileOutputStream(file);
					byte[] buffer = new byte[1024 * 20]; // 这个值设置太小，会导致频繁更新而卡死界面
					int downloadedSize = 0;

					int refreshCount = 0;
					int count = -1;
					while((count = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, count);
						downloadedSize += count;
						// 更新下载进度
						refreshCount++;
						if (refreshCount % 20 == 0){ // 避免频繁更新，每读取30次才刷新一次进度。
							this.publishProgress(downloadedSize);
						}
					}

					buffer = null;
				}

				return null;

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "文件下载失败";

			} catch(ConnectException e){
				e.printStackTrace();
				return "文件下载失败，有可能是网络异常或文件不存在。";

			} catch(SocketTimeoutException e){
				e.printStackTrace();
				return "连接服务器超时，请检查您的网络环境是否正常或稍候再试。";

			}  catch (IOException e) {
				e.printStackTrace();
				return "文件下载失败。";

			} catch (Exception e) {
				e.printStackTrace();
				return "下载失败，请稍候再试";
			} finally {
				try {
					conn.disconnect();
					inStream.close();
					outStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			int downloadedSize = (Integer) values[0];
			int schedule = downloadedSize * 100 / totalSize;
			StringBuffer sb = new StringBuffer();
			sb.append(format(downloadedSize)).append("KB/").append(totalMB).append("KB");

			tv_show.setText(sb.toString());
			pg_bar.setProgress(schedule);
		}

		@Override
		protected void onPostExecute(Object result) {

			if (null == result){
				Log.e("download", "下载完成，打开文件");

				FileUtil.openFile(fileName);

			} else {
				// 下载失败要删除已创建的缓存文件
				FileUtil.deleteFile(fileName);
			}
		}

		private float format(int l){
			return Float.valueOf(new DecimalFormat("#.00").format(l*1.0f/1024));
		}

	}

	public void downloadAndOpen(Context context, String fileURL, String fileName){
		this.context = context;
		this.fileURL = fileURL;
		this.fileName = fileName;

//		if (FileUtil.fileExists(fileName)){
//			FileUtil.openFile(fileName);
//		} else {

			if (!ApplicationEnvironment.getInstance().checkNetworkAvailable()){
				BaseActivity.getTopActivity().showToast("网络连接不可用，请稍候重试");
			} else {
				new DownloadFileTask().execute();
			}

		//}

	}
	private void downLoadApk() {
		//创建request对象
		DownloadManager.Request request=new DownloadManager.Request(Uri.parse(apkurl));
		//设置什么网络情况下可以下载
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		//设置通知栏的标题
		request.setTitle("下载");
		//设置通知栏的message
		request.setDescription("正在下载.....");
		//设置漫游状态下是否可以下载
		request.setAllowedOverRoaming(false);
		//设置文件存放目录
		request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,"update.apk");
		//获取系统服务
		downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		//进行下载
		id = downloadManager.enqueue(request);
	}

}
