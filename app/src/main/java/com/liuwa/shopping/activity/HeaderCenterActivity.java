package com.liuwa.shopping.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.activity.CaptureActivity;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.AddHeaderAdapter;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.HeaderCenterModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.QrCodeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;

import static com.google.zxing.activity.CaptureActivity.RESULT_CODE_QR_SCAN;


public class HeaderCenterActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick{
	private Context context;
	private ImageView img_back;
	public TextView tv_name,tv_tel,tv_regison,tv_yongjin,tv_weidaozhang;
	public RelativeLayout rl_connect,rl_question,rl_back;
	public LinearLayout saomashouhuo,ll_saomatihuo;
	//打开扫描界面请求码
	private int REQUEST_CODE = 0x01;
	//扫描成功返回码
	private int RESULT_OK = 0xA1;
	IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
	IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
	public int tag;
	private LinearLayout ll_yongjin,tv_xiaoliang,ll_add_header;
	public HeaderCenterModel model;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_header_center_layout);
		this.context=this;
		initViews();
		initEvent();

	}

	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_tel=(TextView)findViewById(R.id.tv_tel);
		tv_regison=(TextView)findViewById(R.id.tv_regison);
		tv_yongjin=(TextView)findViewById(R.id.tv_yongjin);
		tv_weidaozhang=(TextView)findViewById(R.id.tv_weidaozhang);
		rl_connect=(RelativeLayout)findViewById(R.id.rl_connect);
		rl_question=(RelativeLayout)findViewById(R.id.rl_question);
		rl_back=(RelativeLayout)findViewById(R.id.rl_back);
		saomashouhuo=(LinearLayout)findViewById(R.id.saomashouhuo);
		ll_saomatihuo=(LinearLayout)findViewById(R.id.ll_saomatihuo);

		tv_xiaoliang=(LinearLayout)findViewById(R.id.tv_xiaoliang);
		ll_yongjin=(LinearLayout)findViewById(R.id.ll_yongjin);
		ll_add_header=(LinearLayout)findViewById(R.id.ll_add_header);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		rl_connect.setOnClickListener(onClickListener);
		rl_question.setOnClickListener(onClickListener);
		rl_back.setOnClickListener(onClickListener);
		saomashouhuo.setOnClickListener(onClickListener);
		ll_saomatihuo.setOnClickListener(onClickListener);

		tv_xiaoliang.setOnClickListener(onClickListener);
		ll_yongjin.setOnClickListener(onClickListener);
		ll_add_header.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		Intent intent;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				HeaderCenterActivity.this.finish();
				break;
				case R.id.rl_back:
					HeaderCenterActivity.this.finish();
					break;
				case R.id.rl_connect:
					intent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.CONNECTNUM));
					startActivity(intent);
					break;
				case R.id.ll_saomatihuo:
					if(!requestPermissions()){
						return;
					}
					tag=0;
					startToScan();
					break;
				case R.id.saomashouhuo:
					if(!requestPermissions()){
					return;
					}
					tag=1;
					startToScan();
					break;
				case R.id.tv_xiaoliang:

					break;
				case R.id.ll_yongjin:
					intent=new Intent(context, MoneyApplayActivity.class);
					startActivity(intent);
					break;
				case R.id.ll_add_header:
					intent=new Intent(context, AddHeaderActivity.class);
					intent.putExtra("leaderId",model.leaderId);
					startActivity(intent);
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

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.HEADERCETER);
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

	private void doDatas(String orderheadid){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("orderheadid",orderheadid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.LeaderShouHuo);
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
						Intent intent=new Intent(context,HeaderOrderByCategroyActivity.class);
						startActivity(intent);
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
						String isleader=jsonObject.getString("isleader");
						if(isleader.equals("1")) {
							Gson localGson = new GsonBuilder().disableHtmlEscaping()
									.create();
							model = localGson.fromJson(jsonObject.getJSONObject("leader").toString(), HeaderCenterModel.class);
							tv_name.setText(model.tname);
							tv_tel.setText(model.Tel + "");
							tv_regison.setText("绑定社区：" + model.region);
							tv_yongjin.setText("我的佣金：￥" + MoneyUtils.formatAmountAsString(new BigDecimal(model.allMoney)));
							tv_weidaozhang.setText("未到账佣金：￥" + MoneyUtils.formatAmountAsString(new BigDecimal(model.nowMoney)));

						}else if(isleader.equals("417")){
							Intent intent =new Intent(context,HeadApplyActivity.class);
							startActivity(intent);
							HeaderCenterActivity.this.finish();
						}

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


	@Override
	public void cartOnClick(ProductModel model) {
		Toast.makeText(this,"购物车点击"+model.proName,Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//扫描结果回调
		if (resultCode == RESULT_CODE_QR_SCAN) { //RESULT_OK = -1
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
			if(tag==0){
				Intent intent=new Intent(context,UserDTOrderActivity.class);
				intent.putExtra("memberid",scanResult);
				startActivity(intent);
			}else if(tag==1){
				doDatas(scanResult);
			}
			//将扫描出的信息显示出来
		}
	}
	public void onResume(){
		super.onResume();
		doGetDatas();
	}
}
