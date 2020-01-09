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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.LogoutDialogFragment;
import com.liuwa.shopping.activity.fragment.UpdateNameFragment;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.UserModel;
import com.liuwa.shopping.model.VersionModel;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.QrCodeUtil;
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


public class MySelfActivity extends BaseActivity implements LogoutDialogFragment.OnFragmentInteractionListener,UpdateNameFragment.OnFragmentInteractionListener {
	private Context context;
	private RelativeLayout rl_address,rl_applay_head,rl_integral_shop,rl_money,rl_connect,rl_logout;
	private TextView tv_my_integral,tv_my_money;
	private RelativeLayout rl_my_order;
	//private UserModel userModel;
	private TextView tv_nickname,tv_id;
	private TextView tv_dfk_dot,tv_dfh_dot,tv_dth_dot,tv_pj;
	private TextView tv_show;
	private ImageView img_qr;
	public LinearLayout rl_dfk,rl_dfh,rl_dth,pj;
	public String questionurl;
	public String messageurl;
	public String kftel;
	public RelativeLayout rl_question;
	public RelativeLayout rl_sys_message;
	public RelativeLayout rl_update;
	public TextView tv_version;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		rl_dfk=(LinearLayout)findViewById(R.id.rl_dfk);
		rl_dfh=(LinearLayout)findViewById(R.id.rl_dfh);
		rl_dth=(LinearLayout)findViewById(R.id.rl_dth);
		pj=(LinearLayout)findViewById(R.id.pj);
		rl_my_order=(RelativeLayout)findViewById(R.id.rl_my_order);
		tv_my_integral=(TextView)findViewById(R.id.tv_my_integral);
		tv_my_money=(TextView)findViewById(R.id.tv_my_money);
		rl_address=(RelativeLayout)findViewById(R.id.rl_address);
		rl_applay_head=(RelativeLayout)findViewById(R.id.rl_applay_head);
		tv_show=(TextView)findViewById(R.id.tv_show);
		rl_integral_shop=(RelativeLayout)findViewById(R.id.rl_integral_shop);
		rl_money=(RelativeLayout)findViewById(R.id.rl_money);
		rl_connect=(RelativeLayout)findViewById(R.id.rl_connect);
		rl_logout=(RelativeLayout)findViewById(R.id.rl_logout);
		rl_sys_message=(RelativeLayout)findViewById(R.id.rl_sys_message);
		tv_nickname=(TextView)findViewById(R.id.tv_nickname);
		tv_id=(TextView)findViewById(R.id.tv_id);
		tv_dfk_dot=(TextView)findViewById(R.id.tv_dfk_dot);
		tv_dfh_dot=(TextView)findViewById(R.id.tv_dfh_dot);
		tv_dth_dot=(TextView)findViewById(R.id.tv_dth_dot);
		tv_pj=(TextView)findViewById(R.id.tv_pj);
		tv_version=(TextView)findViewById(R.id.tv_version);
		img_qr=(ImageView)findViewById(R.id.img_qr);
		rl_question=(RelativeLayout)findViewById(R.id.rl_question);
		rl_update=(RelativeLayout)findViewById(R.id.rl_update);
		tv_version.setText("当前版本:"+Constants.VERSION);

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
		rl_dfk.setOnClickListener(onClickListener);
		rl_dfh.setOnClickListener(onClickListener);
		rl_dth.setOnClickListener(onClickListener);
		pj.setOnClickListener(onClickListener);
		rl_logout.setOnClickListener(onClickListener);
		rl_question.setOnClickListener(onClickListener);
		rl_sys_message.setOnClickListener(onClickListener);
		tv_nickname.setOnClickListener(onClickListener);
		rl_update.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.tv_my_integral:
					intent=new Intent(context,IntegralActivity.class);
//					intent=new Intent(context,UpdateActivity.class);
//					VersionModel model=new VersionModel();
//					model.isUpdate="1";
//					model.forceUpdate="0";
//					model.apkurl="http://ygwl9ht.xinliushangmao.com/upload/app-zs1021.apk";
//					intent.putExtra("versionModel",model);
//					intent.putExtra("apkurl","http://ygwl9ht.xinliushangmao.com/upload/app-zs1021.apk");
					startActivity(intent);
					break;
				case R.id.rl_logout:
					DialogFragmentFromBottom();
					break;
				case R.id.tv_nickname:
					String name=tv_nickname.getText().toString();
					showNameDialog(name);
					break;
				case R.id.rl_sys_message:
					intent=new Intent(context,WebActivity.class);
					intent.putExtra("title","系统消息");
					intent.putExtra("url",messageurl);
					startActivity(intent);
					break;
				case R.id.rl_dfk:
					intent=new Intent(context,OrderShowByCategroyActivity.class);
					intent.putExtra("position",0);
					startActivity(intent);
					break;
				case R.id.rl_dfh:
					intent=new Intent(context,OrderShowByCategroyActivity.class);
					intent.putExtra("position",1);
					startActivity(intent);
					break;
				case R.id.rl_dth:
					intent=new Intent(context,OrderShowByCategroyActivity.class);
					intent.putExtra("position",2);
					startActivity(intent);
					break;
				case R.id.pj:
					intent=new Intent(context,OrderShowByCategroyActivity.class);
					intent.putExtra("position",3);
					startActivity(intent);
					break;
				case R.id.tv_my_money:
					intent=new Intent(context,MyMoneyActivity.class);
					startActivity(intent);
					break;
				case R.id.rl_update:
					getVersionDatas();
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
				case R.id.rl_question:
					intent=new Intent(context,WebActivity.class);
					intent.putExtra("title","常见问题");
					intent.putExtra("url",questionurl);
					startActivity(intent);
					break;
				case R.id.rl_connect:
					intent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + kftel));
					startActivity(intent);
					break;
				case R.id.rl_my_order:
					intent =  new Intent(context,OrderShowByCategroyActivity.class);
					startActivity(intent);
					break;

			}
		}
	};
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
				.executeQueue("请稍候", new LKHttpRequestQueueDone(){
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
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						VersionModel versionModel=localGson.fromJson(job.toString(), VersionModel.class);
						if(versionModel.isUpdate.equals("1")){
							Intent intent =new Intent(context,UpdateActivity.class);
							intent.putExtra("versionModel",versionModel);
							startActivity(intent);
						}else if(isUpdate.equals("0")) {
							Toast.makeText(context,"当前暂无版本更新",Toast.LENGTH_SHORT).show();
						}
					} else {
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}
	private void DialogFragmentFromBottom() {
		showDialog();
	}
	void showDialog() {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		LogoutDialogFragment newFragment = LogoutDialogFragment.newInstance();
		newFragment.show(ft, "dialog");
	}

	public void showNameDialog(String name) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		UpdateNameFragment newFragment = UpdateNameFragment.newInstance(name);
		newFragment.show(ft, "dialog");
	}
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
				.executeQueue("请稍候", new LKHttpRequestQueueDone(){
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
						UserModel userModel = localGson.fromJson(jsonObject.toString(),UserModel.class);
//						SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
//						editor.putString(Constants.USER,localGson.toJson(userModel));
//						editor.commit();
						tv_nickname.setText(userModel.nickname);
						tv_id.setText("用户 ID:"+userModel.memberId);
						tv_my_integral.setText("我的积分："+userModel.score+">");
						tv_my_money.setText("我的余额：￥"+ MoneyUtils.formatAmountAsString(new BigDecimal(userModel.yuE))+">");
						String flag=jsonObject.getString("isLeader");
						if(flag!=null&&flag.equals("1")){
							tv_show.setText("团长中心");
							rl_applay_head.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent =new Intent(context,HeaderCenterActivity.class);
									startActivity(intent);
								}
							});
						}else {
							tv_show.setText("申请成为团长");
							rl_applay_head.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent =new Intent(context,HeadApplyActivity.class);
									startActivity(intent);
								}
							});
						}
						questionurl=jsonObject.getString("questionurl");
						messageurl=jsonObject.getString("messageurl");
						kftel=jsonObject.getString("kftel");
						//生成的二维码图片
						Bitmap qr = QrCodeUtil.createQRImage(userModel.memberId,300,300,null);
						img_qr.setImageBitmap(qr);
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
						}else
						{
							tv_dfk_dot.setVisibility(View.GONE);
						}
						int dfh=jsonObject.getInt("count2");
						if(dfh!=0){
							tv_dfh_dot.setVisibility(View.VISIBLE);
							tv_dfh_dot.setText(dfh+"");
						}else
						{
							tv_dfh_dot.setVisibility(View.GONE);
						}
						int dth=jsonObject.getInt("count3");
						if(dth!=0){
							tv_dth_dot.setVisibility(View.VISIBLE);
							tv_dth_dot.setText(dth+"");
						}else
						{
							tv_dth_dot.setVisibility(View.GONE);
						}
						int pj=jsonObject.getInt("count4");
						if(pj!=0){
							tv_pj.setVisibility(View.VISIBLE);
							tv_pj.setText(pj+"");
						}else
						{
							tv_pj.setVisibility(View.GONE);
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

	@Override
	public void onFragmentInteraction() {
		logout();
	}
	private void logout(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.LOGINOUT);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, outHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	private LKAsyncHttpResponseHandler outHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
						editor.putString(Constants.TOKEN, "");
						editor.putString(Constants.USER,"");
						editor.putString(Constants.flag,"");
						editor.putString(Constants.USER_PHONE,"");
						editor.putString(Constants.USER_PASS,"");
						boolean flag =editor.commit();
						if(flag){
							Intent intent=new Intent();
							intent.setAction(MainTabActivity.ACTION_TAB_INDEX);
							intent.putExtra(MainTabActivity.TAB_INDEX_KEY,0);
							context.sendBroadcast(intent);//发送标准广播
						}
						Toast.makeText(context,"已经退出当前用户",Toast.LENGTH_SHORT).show();
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

	private void updateName(String name){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("nick", name);
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.UPDATENAME);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, nameHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	private LKAsyncHttpResponseHandler nameHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						doGetDatas();
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
	public void onOk(String name) {
		updateName(name);
	}

	@Override
	public void onCancle() {

	}
}
