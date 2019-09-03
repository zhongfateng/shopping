package com.liuwa.shopping.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.uri.FileProviderUtils;
import com.liuwa.shopping.util.uri.SystemProgramUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MySelfActivity extends BaseActivity {
	private Context context;
	private RelativeLayout rl_address,rl_applay_head,rl_integral_shop,rl_money,rl_connect;
	private TextView tv_my_integral,tv_my_money;
	private RelativeLayout rl_my_order;
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
		rl_integral_shop=(RelativeLayout)findViewById(R.id.rl_integral_shop);
		rl_money=(RelativeLayout)findViewById(R.id.rl_money);
		rl_connect=(RelativeLayout)findViewById(R.id.rl_connect);
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
					intent=new Intent(context,MoneyActivity.class);
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
//					intent =  new Intent(context,);
//					startActivity(intent);
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
