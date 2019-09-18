package com.liuwa.shopping.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.DepositAdapter;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.MoneyModel;
import com.liuwa.shopping.model.UserModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.PayResult;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;


public class MyMoneyActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private GridView mg_list;
	private DepositAdapter depositAdapter;
	private RadioGroup pay_type;
	private RadioButton zhifubao;
	private RadioButton weixin;
	private String zhifu_type="";
	private static final int SDK_PAY_FLAG = 1;
	private String uid;
	private String token;
	private IWXAPI api;
	private String order_id;
	private TextView tv_commit;
	private MoneyModel selectModel;
	private TextView tv_money_detail;
	private TextView tv_money;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_money_layout);
		this.context=this;
		order_id	=getIntent().getStringExtra("order_id");
		init();
		initViews();
		initEvent();
	}
	public void init(){
		uid= ApplicationEnvironment.getInstance().getPreferences().getString(Constants.USER_ID, "");
		token=ApplicationEnvironment.getInstance().getPreferences().getString(Constants.TOKEN, "");
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
	}
	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("我的余额");
		tv_commit=(TextView)findViewById(R.id.tv_commit);
		mg_list=(MyGridView)findViewById(R.id.mg_list);
		depositAdapter=new DepositAdapter(context, DatasUtils.moneyList);
		mg_list.setAdapter(depositAdapter);
		mg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectModel=(MoneyModel) parent.getAdapter().getItem(position);
				depositAdapter.setSelectedPosition(position);
				depositAdapter.notifyDataSetChanged();
			}
		});
		tv_money=(TextView)findViewById(R.id.tv_money);
		pay_type=(RadioGroup)findViewById(R.id.rg_pay_type);
		zhifubao=(RadioButton)findViewById(R.id.rb_aplipay);
		weixin=(RadioButton)findViewById(R.id.rb_wechatpay);
		pay_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==zhifubao.getId()) {
					zhifu_type="1";
				}else if(checkedId==weixin.getId()) {
					zhifu_type="0";
				}
			}
		});
		tv_money_detail=(TextView)findViewById(R.id.tv_money_detail);

		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String userStr=pre.getString(Constants.USER,"");
		if(userStr!=null) {
			UserModel model = new Gson().fromJson(userStr, UserModel.class);
			tv_money.setText("￥" + model.yuE + "");
		}
	}
	//微信支付详情
	private void payWEIXINOrder(){

		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("timestamp",System.currentTimeMillis()/1000+"");
		map.put("uid", uid);
		map.put("pay_type", zhifu_type);
		map.put("order_id", order_id);
		map.put("token", token);
		map.put("sign", Md5SecurityUtil.getSignatureValueMap(map));

		HashMap<String, Object> mapend = new HashMap<String, Object>();
		mapend.put(Constants.kMETHODNAME,Constants.PayOrder);
		mapend.put(Constants.kPARAMNAME, map);

		LKHttpRequest req1 = new LKHttpRequest(mapend, getWeixinPayHandler());
		new LKHttpRequestQueue().addHttpRequest(req1)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}


	private LKAsyncHttpResponseHandler getWeixinPayHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {



				String json=(String)obj;
				JSONObject jobj;
				try {
					jobj = new JSONObject(json);
					String code =	jobj.getString("code");
					String msg  =	jobj.getString("msg");
					if(code.equals("0"))
					{
						Bundle bundle = new Bundle();
						bundle.putString("orderid",order_id);
						Message message = WXPayEntryActivity.handler2.obtainMessage();
						message.what = 1;
						message.setData(bundle);
						message.sendToTarget();
						String pay_str=	jobj.getString("rs");

						JSONObject ob=new JSONObject(pay_str);

						String prepayId=ob.getString("prepayid");
						String nonceStr=ob.getString("noncestr");
						String timeStamp=ob.getString("timestamp");
						String packageValue=ob.getString("package");
						String sign        =ob.getString("sign");
						String partnerId	=ob.getString("partnerid");


						PayReq req = new PayReq();
						req.appId = Constants.APP_ID;
						req.partnerId = partnerId;
						req.prepayId = prepayId;
						req.nonceStr = nonceStr;
						req.timeStamp = String.valueOf(timeStamp);
						req.packageValue = packageValue;


						req.sign = sign;

						// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
						boolean flag=	api.sendReq(req);
						if(flag) {
							MyMoneyActivity.this.finish();
						}
					}
					else {
						Toast.makeText(MyMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_commit.setOnClickListener(onClickListener);
		tv_money_detail.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.img_back:
				 MyMoneyActivity.this.finish();
				break;
				case R.id.tv_money_detail:
					intent =new Intent(context,MoneyActivity.class);
					startActivity(intent);
					break;
				case R.id.tv_commit:
					if(selectModel==null){
						Toast.makeText(context,"请选择充值金额",Toast.LENGTH_SHORT).show();
						return;
					}
					if(zhifu_type==null||zhifu_type.length()==0){
						Toast.makeText(context,"请选择支付方式",Toast.LENGTH_SHORT).show();
						return;
					}
					if(zhifu_type.equals("1")){
						payZhiFuBaoOrder();
					}else if(zhifu_type.equals("0")){
						if(isWXAppInstalledAndSupported(context,api)) {
							payWEIXINOrder();
						}
					}
					break;
			}
		}
	};
	//支付宝支付详情
	private void payZhiFuBaoOrder(){
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("timestamp",System.currentTimeMillis()/1000+"");
		map.put("uid", uid);
		map.put("pay_type", zhifu_type);
		map.put("order_id", order_id);
		map.put("token", token);
		map.put("timespan", System.currentTimeMillis()+"");
		map.put("sign", Md5SecurityUtil.getSignature(map));
		HashMap<String, Object> mapend = new HashMap<String, Object>();
		mapend.put(Constants.kMETHODNAME,Constants.PayOrder);
		mapend.put(Constants.kPARAMNAME, map);
		LKHttpRequest req1 = new LKHttpRequest(mapend, getPayHandler());
		new LKHttpRequestQueue().addHttpRequest(req1)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}


	private LKAsyncHttpResponseHandler getPayHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				JSONObject jobj;
				try {
					jobj = new JSONObject(json);
					String code =	jobj.getString("code");
					String msg  =	jobj.getString("msg");
					if(code.equals("0"))
					{
						final String orderInfo=jobj.getString("rs");
						Runnable payRunnable = new Runnable() {
							@Override
							public void run() {
								// 构造PayTask 对象
								PayTask alipay = new PayTask(MyMoneyActivity.this);
								// 调用支付接口，获取支付结果
								String result = alipay.pay(orderInfo, true);

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
						Toast.makeText(MyMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}


	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
					 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
					 * docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息

					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(MyMoneyActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//								Intent intent =new Intent(PayActivity.this,StuOrderDetailActivity.class);
//								intent.putExtra("id", order_id);
//								startActivity(intent);
//								PayActivity.this.finish();

						Intent intent6 =new Intent(context, PaySuccessActivity.class);
						intent6.putExtra("position", 1);
						startActivity(intent6);
						MyMoneyActivity.this.finish();


					} else {
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(MyMoneyActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(MyMoneyActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

//									Intent intent =new Intent(PayActivity.this,StuOrderDetailActivity.class);
//									intent.putExtra("id", order_id);
//									startActivity(intent);
//									PayActivity.this.finish();

//							Intent intent6 =new Intent(context, MyOrderActivity.class);
//							intent6.putExtra("position", 2);
//							startActivity(intent6);
//							MyMoneyActivity.this.finish();

						}
					}
					break;
				}
				default:
					break;
			}
		};
	};
	private  boolean isWXAppInstalledAndSupported(Context context,
												  IWXAPI api) {
		// LogOutput.d(TAG, "isWXAppInstalledAndSupported");
		boolean	sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
				&& api.isWXAppSupportAPI();
		if (!sIsWXAppInstalledAndSupported) {
			Toast.makeText(context, "尚未安装微信客户端或者微信版本不支持", Toast.LENGTH_SHORT).show();
		}

		return sIsWXAppInstalledAndSupported;
	}
	private void doGetDatas(){
//		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
//		productParam.put("start",page);
//		productParam.put("rows",pageSize);
//		productParam.put("classesid","1");
//		productParam.put("type",1);
//		productParam.put("timespan", System.currentTimeMillis()+"");
//		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
//		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
//		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
//		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
//		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
//		new LKHttpRequestQueue().addHttpRequest(categoryReq)
//				.executeQueue(null, new LKHttpRequestQueueDone(){
//
//					@Override
//					public void onComplete() {
//						super.onComplete();
//					}
//
//				});
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
//						JSONObject jsonObject = job.getJSONObject("data");
//						Gson localGson = new GsonBuilder().disableHtmlEscaping()
//								.create();
//						baseModel = localGson.fromJson(jsonObject.toString(),
//								new TypeToken<BaseDataModel<ProductModel>>() {
//								}.getType());
//						proList.addAll(baseModel.list);
//						fpAdapter.notifyDataSetChanged();

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
