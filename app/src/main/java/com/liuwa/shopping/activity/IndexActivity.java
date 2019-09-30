package com.liuwa.shopping.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.github.jaiimageio.impl.common.ImageUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.IndexProductAdapter;
import com.liuwa.shopping.adapter.IndexTuanGouProductAdapter;
import com.liuwa.shopping.adapter.jakewharton.salvage.RecyclingPagerAdapter;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.model.SheQuModel;
import com.liuwa.shopping.model.SpecialModel;
import com.liuwa.shopping.model.TuanModel;
import com.liuwa.shopping.model.TuanProductModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.permission.requestresult.SetPermissions;
import com.liuwa.shopping.util.DatasKey;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.ListUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.SPUtils;
import com.liuwa.shopping.util.TimeUtil;
import com.liuwa.shopping.util.VersionUpdataHelper;
import com.liuwa.shopping.view.AutoScrollViewPager;
import com.liuwa.shopping.view.CircleImageView;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.MyListView;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;



public class IndexActivity extends BaseActivity implements IndexProductAdapter.OnCartClick{
	private Context context;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private AutoScrollViewPager     index_auto_scroll_view;
	private CirclePageIndicator     cpi_indicator;
	private  ImagePagerAdapter      imageAdatper;
	private MyGridView              index_category_type;
	private LinearLayout                tv_go_search;
	private TabLayout               tb_time;
	private MyGridView              mgw_guangou;
	private TextView tv_title;
	private IndexTuanGouProductAdapter indexTuanGouProductAdapter;
	private ArrayList<TuanProductModel> tuanItemList=new ArrayList<TuanProductModel>();
	private ArrayList<CategoryModel> cateList =new ArrayList<CategoryModel>();
	private ArrayList<ImageItemModel> imageUrlList=new ArrayList<ImageItemModel>();
	private ArrayList<TuanModel<TuanProductModel>> tuanList=new ArrayList<TuanModel<TuanProductModel>>();
	private ArrayList<ProductModel> productList=new ArrayList<ProductModel>();
	private MyGridAdapter  myGridAdapter;
	private MyListView lv_show_list;
	private TextView tv_dingwei;
	IndexProductAdapter indexProductAdapter;
	public BaseDataModel<ProductModel>  baseModel;
	private LinearLayout ll_left,ll_down,ll_content;
	public static final int ReqCode = 3;
	private int page=1;
	private int pageSize=10;
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = new AMapLocationClientOption();
	private AMapLocation location;
	private AMapLocationListener mAMapLocationListener;
	private Gson gson;
	private static final int PERMISSION_REQUEST_CODE = 1; //权限请求码
	private String lat="";
	private String lon="";
	private ImageView img_xihua,tv_ce,img_show_left;
	public LinearLayout go_shequ;
	public static  final  int ReqShequ= 67;
	public TextView show_more;
	public String tuanId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_layout);
		this.context=this;
		initViews();
		initEvent();
		doGetDatas();
		getTuangou();
		getProduct();
		startLocation();
	}
	public void initViews()
	{
		ImageView img_back=(ImageView)findViewById(R.id.img_back);
		img_back.setVisibility(View.GONE);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("首页");
		tv_go_search				= (LinearLayout)findViewById(R.id.tv_go_search);
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToScrollView);
		index_auto_scroll_view  = (AutoScrollViewPager)findViewById(R.id.index_auto_scroll_view);
		cpi_indicator				= (CirclePageIndicator)findViewById(R.id.cpi_indicator);
		index_category_type      = (MyGridView)findViewById(R.id.index_category_type);
		//修改了为了16：9的比例
//		double height = getScreenPixel().widthPixels / (480 / 270.0);
//		ViewGroup.LayoutParams params = index_auto_scroll_view.getLayoutParams();
//		params.height = (int) (height);
//		index_auto_scroll_view.setLayoutParams(params);
		imageAdatper=new ImagePagerAdapter(context, DatasUtils.imageList);
		index_auto_scroll_view.setAdapter(imageAdatper);
		cpi_indicator.setViewPager(index_auto_scroll_view);
		index_auto_scroll_view.startAutoScroll();
		index_auto_scroll_view.setInterval(4000);
		index_auto_scroll_view.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		imageAdatper.notifyDataSetChanged();
		myGridAdapter=new MyGridAdapter(this);
		index_category_type.setAdapter(myGridAdapter);
		//特殊分类实现
		ll_left=(LinearLayout)findViewById(R.id.ll_left);
		ll_down=(LinearLayout)findViewById(R.id.ll_down);
		ll_content=(LinearLayout)findViewById(R.id.ll_content);
		//定位
		tv_dingwei=(TextView)findViewById(R.id.tv_dingwei);


		//团购tab实现
		tb_time=(TabLayout)findViewById(R.id.tb_time);
		mgw_guangou=(MyGridView)findViewById(R.id.mgw_guangou);
		indexTuanGouProductAdapter	= new IndexTuanGouProductAdapter(context,tuanItemList);
		mgw_guangou.setAdapter(indexTuanGouProductAdapter);
		mgw_guangou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TuanProductModel model=(TuanProductModel) parent.getAdapter().getItem(position);
				Intent intent=new Intent(context,BuyTogetherProductActivity.class);
				intent.putExtra("tuanInfoId",model.tuanInfoId);
				startActivity(intent);
			}
		});
		tb_time.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int position=tab.getPosition();
				tuanItemList.clear();
				tuanItemList.addAll(tuanList.get(position).tuaninfolist);
				tuanId=tuanList.get(position).tuan.tuanId;
				indexTuanGouProductAdapter.notifyDataSetChanged();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		//产品列表
		lv_show_list=(MyListView)findViewById(R.id.lv_show_list);
		indexProductAdapter=new IndexProductAdapter(context,productList);
		indexProductAdapter.setOnCartClick(this);
		lv_show_list.setAdapter(indexProductAdapter);
		lv_show_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel) parent.getAdapter().getItem(position);
				Intent intent =new Intent(context,ProductDetailActivity.class);
				intent.putExtra("model",model);
				startActivity(intent);
			}
		});
		img_xihua=(ImageView)findViewById(R.id.img_xihua);
		tv_ce=(ImageView)findViewById(R.id.tv_ce);
		img_show_left=(ImageView)findViewById(R.id.img_show_left);
		go_shequ=(LinearLayout)findViewById(R.id.go_shequ);
		show_more=(TextView)findViewById(R.id.show_more);
	}
	public void initEvent(){
		pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				page++;
				getProduct();
				refreshView.onRefreshComplete();
			}
		});
		index_category_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				CategoryModel model=(CategoryModel) parent.getAdapter().getItem(position);
//				Intent intent =new Intent(context,ProductShowByCategroyActivity.class);
//				intent.putExtra("position",position);
//				intent.putExtra("cateList",cateList);
//				startActivity(intent);
				Intent intent=new Intent();
				intent.setAction(MainTabActivity.ACTION_TAB_INDEX);
				intent.putExtra(MainTabActivity.TAB_INDEX_KEY,1);
				intent.putExtra("position",position);
				intent.putExtra("cateList",cateList);
				sendBroadcast(intent);//发送标准广播
			}
		});
		tv_go_search.setOnClickListener(onClickListener);
		ll_left.setOnClickListener(onClickListener);
		ll_content.setOnClickListener(onClickListener);
		ll_down.setOnClickListener(onClickListener);
		show_more.setOnClickListener(onClickListener);
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
							//SPUtils.putString(context, DatasKey.LOCATION_INFO, gson.toJson(location));
							updateDatas(lon,lat);
						} else {
							//定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
							Log.e("AmapError", "location Error, ErrCode:"
									+ loc.getErrorCode() + ", errInfo:"
									+ loc.getErrorInfo());
							updateDatas(lon,lat);
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		go_shequ.setOnClickListener(onClickListener);

	}
	@Override
	protected void onResume() {
		super.onResume();
		startLocation();
		getVersionDatas();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent;
			switch(v.getId()){
			case R.id.tv_go_search:
				intent=new Intent(context,SearchHistoryActivity.class);
				intent.putExtra("requestCode",IndexActivity.ReqCode);
				startActivity(intent);
				break;
			case R.id.ll_left:
				intent=new Intent(context,TimeBuyActivity.class);
				intent.putExtra("classesid",(String)v.getTag());
				startActivity(intent);
				break;
			case R.id.show_more:
				intent=new Intent(context,BuyTogetherActivity.class);
				intent.putExtra("tuanId",tuanId);
				startActivity(intent);
				break;
			case R.id.go_shequ:
				intent=new Intent(context,SheQuListActivity.class);
				startActivityForResult(intent,ReqShequ);
				break;
			case R.id.ll_content:
				intent=new Intent(context,SearchHistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_down:
				intent=new Intent(context,FavoriateActivity.class);
				intent.putExtra("classesid",(String)v.getTag());
				startActivity(intent);
				break;
			case R.id.tv_dingwei:
//				if(!requestPermissions()){
//					return;
//				}
//				startLocation();
//
			if(checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)){
					startLocation();
			}else {
				SetPermissions.openAppDetails(IndexActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
			}
				break;
				
			}
		}
	};


	private void showRequestPermissionDialog(final String[] permissions, final int requestCode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("使用该功能需要使用定位权限\n是否再次开启权限");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ActivityCompat.requestPermissions(IndexActivity.this,permissions,requestCode);
			}
		});
		builder.setNegativeButton("否",null);
		builder.setCancelable(true);
		builder.show();
	}

	/**
	 * 检测权限是否授权
	 * @return
	 */
	private boolean checkPermission(Context context, String permission) {
		return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context,permission);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode){
			case PERMISSION_REQUEST_CODE:
				if (grantResults.length >0 &&
						grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//得到了授权
					Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
					startLocation();
				}else {
					//未授权
					Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}
	//
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
						tv_dingwei.setText(sheQuModel.region);
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
	private void getVersionDatas(){
		TreeMap<String, Object> categorymap1 = new TreeMap<String, Object>();
		categorymap1.put("currentVersion", Constants.VERSION);
		categorymap1.put("timespan", System.currentTimeMillis()+"");
		categorymap1.put("sign",Md5SecurityUtil.getSignature(categorymap1));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.GetVersion);
		requestCategoryMap.put(Constants.kPARAMNAME, categorymap1);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, versionHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	private LKAsyncHttpResponseHandler versionHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  jobObject= new JSONObject(json);
					int code =	jobObject.getInt("code");
					if(code==Constants.CODE) {
						JSONObject job=jobObject.getJSONObject("data");
					int  newVersion=	job.getInt("newVersion");
					String isUpdate  =  job.getString("isUpdate");
					String 	forceUpdate=job.getString("forceUpdate");
					String apkurl=job.getString("apkurl");
					//	new VersionUpdataHelper(context, apkurl,true);
						BaseAndroid.checkUpdate(IndexActivity.this, 2, "http://gdown.baidu.com/data/wisegame/ecd186afa44f0325/youkushipin_205.apk","更新了XXX\n修复OOO", false);
					} else {
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}
	//加载分类 公告
	private void doGetDatas(){
		TreeMap<String, Object> categorymap1 = new TreeMap<String, Object>();
		categorymap1.put("page",1);
		categorymap1.put("rows",10);
		categorymap1.put("timespan", System.currentTimeMillis()+"");
		categorymap1.put("sign",Md5SecurityUtil.getSignature(categorymap1));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.GETCATEGORY);
		requestCategoryMap.put(Constants.kPARAMNAME, categorymap1);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getCagegoryHandler());

		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("page", 1);
		map.put("rows", 2);
		map.put("mdtype", "1");
		HashMap<String, Object> noticeMap = new HashMap<String, Object>();
		noticeMap.put(Constants.kMETHODNAME,Constants.GETNOTICES);
		noticeMap.put(Constants.kPARAMNAME, map);
		LKHttpRequest noticeReq = new LKHttpRequest(noticeMap, getNoticeHandler());

		TreeMap<String, Object> specilCategory = new TreeMap<String, Object>();
		specilCategory.put("page", 1);
		specilCategory.put("rows", 3);
		specilCategory.put("timespan", System.currentTimeMillis()+"");
		specilCategory.put("sign",Md5SecurityUtil.getSignature(specilCategory));
		HashMap<String, Object> specialMap = new HashMap<String, Object>();
		specialMap.put(Constants.kMETHODNAME,Constants.GETSPECIALCATEGORY);
		specialMap.put(Constants.kPARAMNAME, specilCategory);
		LKHttpRequest specialCategoryReq = new LKHttpRequest(specialMap, getSpeicalCagegoryHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq,specialCategoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	public void getProduct(){
		TreeMap<String, Object> baseProductParam = new TreeMap<String, Object>();
		baseProductParam.put("page",page);
		baseProductParam.put("rows",pageSize);
		baseProductParam.put("type","1");
		baseProductParam.put("timespan", System.currentTimeMillis()+"");
		baseProductParam.put("sign", Md5SecurityUtil.getSignature(baseProductParam));
		HashMap<String, Object> productMap = new HashMap<String, Object>();
		productMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
		productMap.put(Constants.kPARAMNAME, baseProductParam);
		LKHttpRequest productReq = new LKHttpRequest(productMap, getProductHandler());
		new LKHttpRequestQueue().addHttpRequest(productReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	public void getTuangou(){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> tuangouMap = new HashMap<String, Object>();
		tuangouMap.put(Constants.kMETHODNAME,Constants.GETTUANGOU);
		tuangouMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest tuangouReq = new LKHttpRequest(tuangouMap, getTuanGouHandler());
		new LKHttpRequestQueue().addHttpRequest(tuangouReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
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
								new TypeToken<BaseDataModel<ProductModel>>() {
								}.getType());
						productList.addAll(baseModel.list);
						indexProductAdapter.notifyDataSetChanged();

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

	private LKAsyncHttpResponseHandler getNoticeHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						JSONArray array=job.getJSONArray("rs");
						imageUrlList.clear();
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						imageUrlList.addAll((Collection<? extends ImageItemModel>) localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<ImageItemModel>>() {
								}.getType()));
						imageAdatper.notifyDataSetChanged();
					}
					else {
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	private LKAsyncHttpResponseHandler getTuanGouHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
						tuanList.clear();
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						tuanList.addAll((Collection<? extends TuanModel<TuanProductModel>>)(localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<TuanModel<TuanProductModel>>>() {
								}.getType())));
						for(TuanModel<TuanProductModel> model:tuanList){
							String tuancode=model.tuan.tuanCode;
							tb_time.addTab(tb_time.newTab().setText(TimeUtil.getFormatimestamp(model.tuan.beginTime.time,model.tuan.endTime.time,"HH:mm")+""));
						}
						tb_time.getTabAt(0).select();
                        tuanItemList.clear();
						tuanItemList.addAll(tuanList.get(0).tuaninfolist);
						indexTuanGouProductAdapter.notifyDataSetChanged();
					}
					else {
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	private LKAsyncHttpResponseHandler getCagegoryHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
						cateList.clear();
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						cateList.addAll((Collection<? extends CategoryModel>) localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<CategoryModel>>() {
								}.getType()));
						myGridAdapter.notifyDataSetChanged();
					} else {
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	private LKAsyncHttpResponseHandler getSpeicalCagegoryHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						ArrayList<SpecialModel> list=localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<SpecialModel>>() {
								}.getType());
//						ImageShowUtil.showImage(list.get(2).imgPath,img_xihua);
//						ll_down.setTag(list.get(2).proClassesId);
//
//						ImageShowUtil.showImage(list.get(1).imgPath,tv_ce);
//						ll_content.setTag(list.get(1).proClassesId);
//
//						ImageShowUtil.showImage(list.get(0).imgPath,img_show_left);
//						ll_left.setTag(list.get(0).proClassesId);
					}
					else {
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	@Override
	public void cartOnClick(ProductModel model) {
		Toast.makeText(context,model.fristimg,Toast.LENGTH_SHORT).show();
	}

	public class ImagePagerAdapter extends RecyclingPagerAdapter {

		private Context       context;
		private List<ImageItemModel> imageIdList;
		private LayoutInflater inflater;
		private int           size;
		private boolean       isInfiniteLoop;
		public ImagePagerAdapter(Context context, List<ImageItemModel> imageIdList) {
			this.context = context;
			this.imageIdList = imageIdList;
			this.size = ListUtils.getSize(imageIdList);
			isInfiniteLoop = false;
			inflater=LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// Infinite loop
			return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
		}
		/**
		 * get really position
		 *
		 * @param position
		 * @return
		 */
		private int getPosition(int position) {
			return isInfiniteLoop ? position % size : position;
		}

		@Override
		public View getView(int position, View view, ViewGroup container) {
			ViewHolder holder;
			if (view == null) {
				view=inflater.inflate(
						R.layout.index_image_list_item, null);
				holder = new ViewHolder();
				holder.imageView=(ImageView) view.findViewById(R.id.image_item);
				view.setTag(holder);
			} else {
				holder = (ViewHolder)view.getTag();
			}
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			final ImageItemModel model=imageIdList.get(position);
			ImageShowUtil.showImage(model.getImageUrl(),holder.imageView);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

			return view;
		}

		private  class ViewHolder {

			ImageView imageView;
		}

		/**
		 * @return the isInfiniteLoop
		 */
		public boolean isInfiniteLoop() {
			return isInfiniteLoop;
		}

		/**
		 * @param isInfiniteLoop the isInfiniteLoop to set
		 */
		public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
			this.isInfiniteLoop = isInfiniteLoop;
			return this;
		}
	}
	//分类适配器
	public class MyGridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Activity context;
		public MyGridAdapter(Context context) {
			this.context = (Activity) context;
			inflater = LayoutInflater.from(context);
		}
		public class AdapterViews {
			public TextView hot_name;
			public CircleImageView hot_image;
		}

		@Override
		public int getCount() {
			return cateList.size();
		}

		@Override
		public Object getItem(int location) {
			return cateList.get(location);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AdapterViews views;
			if (null == convertView) {
				convertView = inflater.inflate(
						R.layout.hot_type_item, null);
				views = new AdapterViews();
				views.hot_name=(TextView) convertView
						.findViewById(R.id.hot_type_name_id);
				views.hot_image=(CircleImageView)convertView.findViewById(R.id.hot_type_image);
				convertView.setTag(views);
			} else {
				views = (AdapterViews) convertView.getTag();
			}
			CategoryModel  Model=	cateList.get(position);
			views.hot_name.setText(Model.getProClassesName());
			ImageShowUtil.showImage(Model.getImgPath(),views.hot_image);
			return convertView;
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
		if (null != gson) {
			gson = null;
		}
		if (null != locationClient) {
			locationClient.onDestroy();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==ReqShequ){
			if (resultCode == Activity.RESULT_OK){
				SheQuModel model = (SheQuModel)data.getSerializableExtra(Constants.AREA);
				tv_dingwei.setText(model.region);
				SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
				editor.putString(Constants.AREA, new Gson().toJson(model));
				editor.commit();
			}
		}
	}
}
