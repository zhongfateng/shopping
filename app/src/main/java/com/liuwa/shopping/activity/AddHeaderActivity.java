package com.liuwa.shopping.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.activity.CaptureActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.HeaderFragment;
import com.liuwa.shopping.activity.fragment.TianJiaLeaderFragment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.HeaderApplayModel;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.ProductChildModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.QrCodeUtil;
import com.liuwa.shopping.util.SPUtils;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static com.google.zxing.activity.CaptureActivity.RESULT_CODE_QR_SCAN;


public class AddHeaderActivity extends BaseActivity implements TianJiaLeaderFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ViewPager vp_category;
	private TabLayout tl_tabs;
	private ArrayList fragmentList=new ArrayList<>();;
	private ArrayList list_Title= new ArrayList<>();;
	private MyPagerAdapter adapter;
	public ArrayList<HeaderApplayModel> leaderApplyModels=new ArrayList<>();
	public ArrayList<HeaderApplayModel> leaderApplyModels2=new ArrayList<>();
	public PullToRefreshScrollView pullToScrollView;
	public ImageView leader_img;
	public String leaderId;
	IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
	IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
	//打开扫描界面请求码
	private int REQUEST_CODE = 0x01;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_header_layout);
		this.context = this;
		init();
		leaderId=getIntent().getStringExtra("leaderId");
		initViews();
		initEvent();
		getTuangou();
	}

	public void init() {

	}

	public void initViews() {
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("添加副团长");
		leader_img=(ImageView)findViewById(R.id.leader_img);
		pullToScrollView=(PullToRefreshScrollView)findViewById(R.id.pullToScrollView);
		pullToScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				getTuangou();
				refreshView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}
		});
		tl_tabs = (TabLayout) findViewById(R.id.tb_top);
		vp_category = (ViewPager) findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);//此方法就是让tablayout和ViewPager联动
		Util.reflex(tl_tabs);
	}

	public void initEvent() {
		img_back.setOnClickListener(onClickListener);
		leader_img.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.img_back:
					AddHeaderActivity.this.finish();
					break;
				case R.id.leader_img:
					if(!requestPermissions()){
						return;
					}
					startToScan();
					break;
			}
		}
	};
	public void startToScan(){
		Intent intent = new Intent(context, CaptureActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
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
		String[] permissions = {Manifest.permission.CAMERA,
				Manifest.permission.VIBRATE};
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
		}else{
			//输出授权结果
			Toast.makeText(context,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void agress(HeaderApplayModel model) {
		doGetDatas(model);
	}

	@Override
	public void delete(HeaderApplayModel model) {

	}

	@Override
	public void sc(HeaderApplayModel model) {

	}

	private void doGetDatas(HeaderApplayModel model){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("memberid",model.memberId);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.TONGYI);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());

		new LKHttpRequestQueue().addHttpRequest(categoryReq)
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
					JSONObject job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code== Constants.CODE) {
						getTuangou();
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

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private Context context;
		private List<Fragment> fragmentList=new ArrayList<>();
		private List<String> list_Title=new ArrayList<>();

		public MyPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> list_Title) {
			super(fm);
			this.context = context;
			this.fragmentList = fragmentList;
			this.list_Title = list_Title;
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return list_Title.size();
		}

		/**
		 * //此方法用来显示tab上的名字
		 *
		 * @param position
		 * @return
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return list_Title.get(position);
		}
	}
	public void getTuangou(){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> tuangouMap = new HashMap<String, Object>();
		tuangouMap.put(Constants.kMETHODNAME,Constants.ShenQing);
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
	private LKAsyncHttpResponseHandler getTuanGouHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONObject object=job.getJSONObject("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						leaderApplyModels.clear();
						leaderApplyModels.addAll((Collection<? extends HeaderApplayModel>)(localGson.fromJson(object.getJSONArray("memlistty").toString(),
								new TypeToken<ArrayList<HeaderApplayModel>>() {
								}.getType())));
						leaderApplyModels2.clear();
						leaderApplyModels2.addAll((Collection<? extends HeaderApplayModel>)(localGson.fromJson(object.getJSONArray("memlistsq").toString(),
								new TypeToken<ArrayList<HeaderApplayModel>>() {
								}.getType())));
						list_Title.clear();
						list_Title.add("已添加");
						list_Title.add("待审核");
						fragmentList.add(TianJiaLeaderFragment.newInstance(leaderApplyModels,"1"));
						fragmentList.add(TianJiaLeaderFragment.newInstance(leaderApplyModels2,"2"));
						adapter.notifyDataSetChanged();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//扫描结果回调
		if (resultCode == RESULT_CODE_QR_SCAN) { //RESULT_OK = -1
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
			doDatas(scanResult);
			//将扫描出的信息显示出来
		}
	}
	private void doDatas(String memberid){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("memberid",memberid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.ADDLEADERFU);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getNoticeHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

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
						getTuangou();
					}
					else if(code==200)
					{
						Toast.makeText(context,job.getString("msg"),Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
}


