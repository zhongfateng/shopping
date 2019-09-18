package com.liuwa.shopping.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liuwa.shopping.R;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.UserModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.StatusBarUtils;
import com.liuwa.shopping.util.uri.FileProviderUtils;
import com.liuwa.shopping.util.uri.SystemProgramUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;


public class MySelfActivity extends BaseActivity {
	private Context context;
	private RelativeLayout rl_address,rl_applay_head,rl_integral_shop,rl_money,rl_connect;
	private TextView tv_my_integral,tv_my_money;
	private RelativeLayout rl_my_order;
	private UserModel userModel;
	private TextView tv_nickname,tv_id;
	private TextView tv_dfk_dot,tv_dfh_dot,tv_dth_dot,tv_pj;
	private TextView tv_show;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		rl_my_order=(RelativeLayout)findViewById(R.id.rl_my_order);
		tv_my_integral=(TextView)findViewById(R.id.tv_my_integral);
		tv_my_money=(TextView)findViewById(R.id.tv_my_money);
		rl_address=(RelativeLayout)findViewById(R.id.rl_address);
		rl_applay_head=(RelativeLayout)findViewById(R.id.rl_applay_head);
		tv_show=(TextView)findViewById(R.id.tv_show);
		rl_integral_shop=(RelativeLayout)findViewById(R.id.rl_integral_shop);
		rl_money=(RelativeLayout)findViewById(R.id.rl_money);
		rl_connect=(RelativeLayout)findViewById(R.id.rl_connect);
		tv_nickname=(TextView)findViewById(R.id.tv_nickname);
		tv_id=(TextView)findViewById(R.id.tv_id);
		tv_dfk_dot=(TextView)findViewById(R.id.tv_dfk_dot);
		tv_dfh_dot=(TextView)findViewById(R.id.tv_dfh_dot);
		tv_dth_dot=(TextView)findViewById(R.id.tv_dth_dot);
		tv_pj=(TextView)findViewById(R.id.tv_pj);
	}
	public void initEvent(){
		tv_my_integral.setOnClickListener(onClickListener);
		tv_my_money.setOnClickListener(onClickListener);
		rl_address.setOnClickListener(onClickListener);
		rl_applay_head.setOnClickListener(onClickListener);
		rl_integral_shop.setOnClickListener(onClickListener);
		rl_money.setOnClickListener(onClickListener);
		rl_connect.setOnClickListener(onClickListener);
		rl_my_order.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.tv_my_integral:
					intent=new Intent(context,IntegralActivity.class);
					startActivity(intent);
					break;
				case R.id.tv_my_money:
					intent=new Intent(context,MyMoneyActivity.class);
					startActivity(intent);
					break;
			    case R.id.rl_address:
			    	intent=new Intent(context,MyAddressActivity.class);
			    	startActivity(intent);
				break;
				case R.id.rl_applay_head:
					intent=new Intent(context,HeadApplyActivity.class);
					startActivity(intent);
					break;
				case R.id.rl_integral_shop:
					intent=new Intent(context,IntegralShopActivity.class);
					startActivity(intent);
				break;
				case R.id.rl_money:
					intent=new Intent(context,MyMoneyActivity.class);
					startActivity(intent);
					break;
				case R.id.rl_connect:
					intent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + Constants.CONNECTNUM));
					startActivity(intent);
					break;
				case R.id.rl_my_order:
					intent =  new Intent(context,OrderShowByCategroyActivity.class);
					startActivity(intent);
					break;
				case R.id.tv_go_to_index:
					intent=new Intent();
					intent.setAction(MainTabActivity.ACTION_TAB_INDEX);
					intent.putExtra(MainTabActivity.TAB_INDEX_KEY,3);
					sendBroadcast(intent);//发送标准广播
					MySelfActivity.this.finish();
					break;
			}
		}
	};

	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.USERCENTER);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());

		TreeMap<String, Object> Param = new TreeMap<String, Object>();
		Param.put("timespan", System.currentTimeMillis()+"");
		Param.put("sign", Md5SecurityUtil.getSignature(Param));
		HashMap<String, Object> Map = new HashMap<String, Object>();
		Map.put(Constants.kMETHODNAME,Constants.ORDERCOUNT);
		Map.put(Constants.kPARAMNAME, Param);
		LKHttpRequest Req = new LKHttpRequest(Map, getCountHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq,Req)
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
						userModel = localGson.fromJson(jsonObject.toString(),UserModel.class);
						SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
						editor.putString(Constants.Phone, userModel.tel);
						editor.putString(Constants.USER,localGson.toJson(userModel));
						editor.commit();
						tv_nickname.setText(userModel.nickname);
						tv_id.setText("用户 ID:"+userModel.memberId);
						tv_my_integral.setText("我的积分："+userModel.score+">");
						tv_my_money.setText("我的余额：￥："+ MoneyUtils.formatAmountAsString(new BigDecimal(userModel.yuE))+">");
						String flag=userModel.isLeader;
						if(flag!=null&&flag.equals("1")){
							tv_show.setText("团长中心");
							rl_applay_head.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent =new Intent(context,HeaderCenterActivity.class);
									startActivity(intent);
								}
							});
						}
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
	private LKAsyncHttpResponseHandler getCountHandler(){
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
						int dfk=jsonObject.getInt("count1");
						if(dfk!=0){
							tv_dfk_dot.setVisibility(View.VISIBLE);
							tv_dfk_dot.setText(dfk+"");
						}
						int dfh=jsonObject.getInt("count2");
						if(dfh!=0){
							tv_dfh_dot.setVisibility(View.VISIBLE);
							tv_dfh_dot.setText(dfh+"");
						}
						int dth=jsonObject.getInt("count3");
						if(dth!=0){
							tv_dth_dot.setVisibility(View.VISIBLE);
							tv_dth_dot.setText(dth+"");
						}
						int pj=jsonObject.getInt("count4");
						if(pj!=0){
							tv_pj.setVisibility(View.VISIBLE);
							tv_pj.setText(pj+"");
						}
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
	public void onResume(){
		super.onResume();
		doGetDatas();
	}
}
