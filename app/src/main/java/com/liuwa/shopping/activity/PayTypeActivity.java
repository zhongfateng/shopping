package com.liuwa.shopping.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.liuwa.shopping.R;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.UserModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.PayResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class PayTypeActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private CheckBox ck_yue_pay,ck_wx_pay,ck_alipay;
	public String order_id;
	public String payment;
	public TextView tv_pay;
	private static final int SDK_PAY_FLAG = 1;
	public TextView tv_yue;
	public TextView tv_chongzhi;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_type_layout);
		this.context=this;
		order_id=getIntent().getStringExtra("order_id");
		initViews();
		initEvent();
		//doGetDatas();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("支付方式");
		ck_yue_pay=(CheckBox)findViewById(R.id.ck_yue_pay);
		ck_wx_pay=(CheckBox)findViewById(R.id.ck_wx_pay);
		ck_alipay=(CheckBox)findViewById(R.id.ck_alipay);
		tv_pay=(TextView)findViewById(R.id.tv_pay);
		tv_yue=(TextView)findViewById(R.id.tv_yue);
		tv_chongzhi=(TextView)findViewById(R.id.tv_chongzhi);

	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		ck_yue_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						ck_wx_pay.setChecked(false);
						ck_alipay.setChecked(false);
						payment="3";
					}
			}
		});
		tv_chongzhi.setOnClickListener(onClickListener);
		ck_wx_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					ck_yue_pay.setChecked(false);
					ck_alipay.setChecked(false);
					payment="2";
				}
			}
		});
		ck_alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					ck_wx_pay.setChecked(false);
					ck_yue_pay.setChecked(false);
					payment="1";
				}
			}
		});
		tv_pay.setOnClickListener(onClickListener);

	}
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				PayTypeActivity.this.finish();
				break;
				case R.id.tv_chongzhi:
					Intent intent =new Intent(context,MyMoneyActivity.class);
					startActivity(intent);
					break;
				case R.id.tv_pay:
					if(payment==null||payment.length()==0){
						Toast.makeText(context,"请选择支付方式",Toast.LENGTH_SHORT).show();
						return;
					}
					if(payment.equals("1")) {
						doAlipayDatas();
					}else if(payment.equals("3"))
					{
						doMoneyDatas();
					}else if(payment.equals("2"))
					{

					}
					break;
			}
		}
	};

	//加载特殊分类商品 例如猜你喜欢！
	private void doAlipayDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
//		productParam.put("start",page);
//		productParam.put("rows",pageSize);
		productParam.put("orderid",order_id);
		productParam.put("payment","1");
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PAYORDER);
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
						final String orderInfo=job.getString("data");
						final Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								PayTask alipay = new PayTask(PayTypeActivity.this);
								Map<String, String> result = alipay.payV2(orderInfo, true);
								Log.i("msp", result.toString());

								Message msg = new Message();
								msg.what = SDK_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};

						// 必须异步调用
						Thread payThread = new Thread(payRunnable);
						payThread.start();
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
	public void onResume()
	{
		super.onResume();
		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String userStr=pre.getString(Constants.USER,"");
		if(userStr!=null){
			UserModel model =new Gson().fromJson(userStr, UserModel.class);
			tv_yue.setText("￥"+model.yuE+"");
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						//showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
						Intent intent=new Intent(context,PaySuccessActivity.class);
						intent.putExtra("order_id",order_id);
						startActivity(intent);
						PayTypeActivity.this.finish();
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						//showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
					}
					break;
				}
				default:
					break;
			}
		};
	};

	//加载特殊分类商品 例如猜你喜欢！
	private void doMoneyDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
//		productParam.put("start",page);
//		productParam.put("rows",pageSize);
		productParam.put("orderid",order_id);
		productParam.put("payment","3");
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PAYORDER);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getMoneyHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}

	private LKAsyncHttpResponseHandler getMoneyHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						Intent intent=new Intent(context,PaySuccessActivity.class);
						intent.putExtra("order_id",order_id);
						startActivity(intent);
						PayTypeActivity.this.finish();
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

	//加载特殊分类商品 例如猜你喜欢！
	private void doWxDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
//		productParam.put("start",page);
//		productParam.put("rows",pageSize);
		productParam.put("orderid",order_id);
		productParam.put("payment","2");
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PAYORDER);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getWxHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}

	private LKAsyncHttpResponseHandler getWxHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						Intent intent=new Intent(context,PaySuccessActivity.class);
						intent.putExtra("order_id",order_id);
						startActivity(intent);
						PayTypeActivity.this.finish();
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
}
