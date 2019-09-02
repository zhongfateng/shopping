package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class MySelfActivity extends BaseActivity{
	private Context context;
	private RelativeLayout rl_address,rl_applay_head;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		rl_address=(RelativeLayout)findViewById(R.id.rl_address);
		rl_applay_head=(RelativeLayout)findViewById(R.id.rl_applay_head);
	}
	
	public void initEvent(){
		rl_address.setOnClickListener(onClickListener);
		rl_applay_head.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			    case R.id.rl_address:
			    	intent=new Intent(context,MyAddressActivity.class);
			    	startActivity(intent);
				break;
				case R.id.rl_applay_head:
					intent=new Intent(context,IntegralActivity.class);
					startActivity(intent);
					break;
				case R.id.tv_go_to_order:
					intent=new Intent(context,LoginActivity.class);
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
