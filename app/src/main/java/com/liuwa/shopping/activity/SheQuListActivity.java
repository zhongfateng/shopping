package com.liuwa.shopping.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.adapter.SheQuAdapter;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.model.SheQuModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class SheQuListActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private ListView gv_favoriate_list;
	private SheQuAdapter fpAdapter;
	private ArrayList<SheQuModel> proList = new ArrayList<SheQuModel>();
	public int page=1;
	public int pageSize=10;
	private TextView tv_title;
	public String lon;
	public String lat;
	public String region;
	public BaseDataModel<SheQuModel>  baseModel;
	public EditText et_search;
	IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
	IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = new AMapLocationClientOption();
	private AMapLocation location;
	private AMapLocationListener mAMapLocationListener;
	private TextView tv_name,tv_regison,tv_detail;
	public RelativeLayout current_shequ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shequ_layout);
		this.context=this;
		initViews();
		initEvent();
		requestPermissions();
		startLocation();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("社区");
		current_shequ=(RelativeLayout)findViewById(R.id.current_shequ);
		et_search=(EditText)findViewById(R.id.et_search);
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToScrollView);
		gv_favoriate_list        = (ListView)findViewById(R.id.lv_show_shequ);
		fpAdapter                 =  new SheQuAdapter(this,proList);
		gv_favoriate_list.setAdapter(fpAdapter);
		gv_favoriate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SheQuModel model=(SheQuModel)parent.getAdapter().getItem(position);
//				Intent intent =new Intent();
//				intent.putExtra(Constants.AREA,model);
//				setResult(Activity.RESULT_OK,intent);
//				SheQuListActivity.this.finish();
				Gson localGson = new GsonBuilder().disableHtmlEscaping()
						.create();
				SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
				editor.putString(Constants.AREA, localGson.toJson(model));
				boolean flag=editor.commit();
				if(flag){
					SheQuListActivity.this.finish();
				};
			}
		});
		gv_favoriate_list.setEmptyView(findViewById(android.R.id.empty));
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_regison=(TextView)findViewById(R.id.tv_regison);
		tv_detail=(TextView)findViewById(R.id.tv_detail);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				proList.clear();
				page=1;
				doGetDatas(region);
				pullToRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				page++;
				doGetDatas(region);
				pullToRefreshScrollView.onRefreshComplete();
			}
		});
		// 搜索框的键盘搜索键点击回调
		et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
					// 先隐藏键盘
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					// TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
					String value=et_search.getText().toString();
					page=1;
					proList.clear();
					doGetDatas(value);
				}
				return false;
			}
		});

		// 搜索框的文本变化实时监听
		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() == 0) {
				} else {
				}
				String tempName = et_search.getText().toString();
				// 根据tempName去模糊查询数据库中有没有数据
				//queryData(tempName);

			}
		});
		//定位监听
		//gps定位监听器
		mAMapLocationListener = new AMapLocationListener() {
			@Override
			public void onLocationChanged(AMapLocation loc) {
				try {
					if (null != loc) {
						stopLocation();
						if (loc.getErrorCode() == 0) {//可在其中解析amapLocation获取相应内容。
							location = loc;
							lat=location.getLatitude()+"";
							lon=location.getLongitude()+"";
							updateDatas(lon,lat);
							doGetDatas(region);

						} else {
							//定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
							Log.e("AmapError", "location Error, ErrCode:"
									+ loc.getErrorCode() + ", errInfo:"
									+ loc.getErrorInfo());
							Toast.makeText(context,"请打开定位，以免定位失败",Toast.LENGTH_SHORT).show();
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		current_shequ.setOnClickListener(onClickListener);
	}
	private void updateDatas(String lon,String lat){
		TreeMap<String, Object> categorymap1 = new TreeMap<String, Object>();
		if(lon.length()!=0) {
			categorymap1.put("x", lon);
		}
		if(lat.length()!=0) {
			categorymap1.put("y", lat);
		}
		categorymap1.put("timespan", System.currentTimeMillis()+"");
		categorymap1.put("sign",Md5SecurityUtil.getSignature(categorymap1));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.Area);
		requestCategoryMap.put(Constants.kPARAMNAME, categorymap1);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, areaHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	private LKAsyncHttpResponseHandler areaHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONObject array=job.getJSONObject("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						SheQuModel sheQuModel=localGson.fromJson(array.toString(), SheQuModel.class);
						tv_name.setText(sheQuModel.tname);
						tv_regison.setText(sheQuModel.region);
						tv_detail.setText(sheQuModel.shiname+" "+sheQuModel.areaname+" "+sheQuModel.region);
						SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
						editor.putString(Constants.AREA, localGson.toJson(sheQuModel));
						editor.commit();
					} else {
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				SheQuListActivity.this.finish();
				break;
				case R.id.current_shequ:
//					Intent intent =new Intent();
//					intent.putExtra(Constants.AREA,sheQuModel);
//					setResult(Activity.RESULT_OK,intent);
//					SheQuListActivity.this.finish();
					break;
			}
		}
	};

	private void doGetDatas(String region){
// 		String areaJson= ApplicationEnvironment.getInstance().getPreferences().getString(Constants.AREA,"");
//		SheQuModel model=null;
// 		if(areaJson!=null){
// 			 model=new Gson().fromJson(areaJson,SheQuModel.class);
//		}
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("page",page);
		productParam.put("rows",pageSize);
		productParam.put("x",lon);
		productParam.put("y",lat);
		if(region!=null&&region.length()!=0){
			productParam.put("region",region);
		}
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.SheQuList);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue("请稍后", new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}

	private LKAsyncHttpResponseHandler getNoticeHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
					}
					else
					{
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	private LKAsyncHttpResponseHandler getProductHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						JSONObject jsonObject = job.getJSONObject("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						baseModel = localGson.fromJson(jsonObject.toString(),
								new TypeToken<BaseDataModel<SheQuModel>>() {
								}.getType());
						proList.addAll(baseModel.list);
						fpAdapter.notifyDataSetChanged();

					}
					else
					{
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
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
			Toast.makeText(context,"授权成功，请重新点击刚才的操作！",Toast.LENGTH_LONG).show();
			startLocation();
		}else{
			//输出授权结果
			Toast.makeText(context,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 开始定位
	 */
	public void startLocation() {
		initLocation();
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
	}

	/**
	 * 停止定位
	 */
	private void stopLocation() {
		if (null != locationClient) {
			locationClient.stopLocation();
		}
	}
	/**
	 * 初始化定位
	 */
	private void initLocation() {
		if (null == locationClient) {
			//初始化client
			locationClient = new AMapLocationClient(this.getApplicationContext());
			//设置定位参数
			locationClient.setLocationOption(getDefaultOption());
			// 设置定位监听
			locationClient.setLocationListener(mAMapLocationListener);
		}
	}
	/**
	 * 默认的定位参数
	 */
	private AMapLocationClientOption getDefaultOption() {
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
		mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setMockEnable(true);//如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
		return mOption;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLocation();
		if (null != locationClient) {
			locationClient.onDestroy();
		}
	}

}
