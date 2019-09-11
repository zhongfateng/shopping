package com.liuwa.shopping.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.adapter.OrderProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.AddressModel;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class ConfirmOrderActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private ListView gv_favoriate_list;
	private OrderProductAdapter fpAdapter;
	private TextView tv_title;
	private RelativeLayout rl_add;
	private LinearLayout rl_address;
	MyListView lv_show_list;
	private ArrayList<OrderProductItem> orderProductItems =new ArrayList<OrderProductItem>();
	private String order_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order_layout);
		this.context=this;
		order_id=getIntent().getStringExtra("order_id");
		initViews();
		initEvent();
		doGetDatas();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("订单确认");
		rl_add=(RelativeLayout)findViewById(R.id.rl_add);
		rl_address=(LinearLayout)findViewById(R.id.rl_address);
		lv_show_list=(MyListView)findViewById(R.id.lv_show_list);
		fpAdapter=new OrderProductAdapter(context,orderProductItems);
		lv_show_list.setAdapter(fpAdapter);
		fpAdapter.notifyDataSetChanged();

	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);

	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				ConfirmOrderActivity.this.finish();
				break;
			}
		}
	};

	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("order_id",order_id);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.ORDERDETAIL);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getOrderHandler());

		TreeMap<String, Object> param = new TreeMap<String, Object>();
		param.put("isused","1");
		param.put("timespan", System.currentTimeMillis()+"");
		param.put("sign", Md5SecurityUtil.getSignature(param));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.kMETHODNAME,Constants.GETADDRESS);
		map.put(Constants.kPARAMNAME, param);
		LKHttpRequest areaReq = new LKHttpRequest(map, getAddressHandler());

		new LKHttpRequestQueue().addHttpRequest(categoryReq,areaReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}

	private LKAsyncHttpResponseHandler getOrderHandler(){
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

	private LKAsyncHttpResponseHandler getAddressHandler(){
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
						ArrayList<AddressModel> addressModels=localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<AddressModel>>() {
								}.getType());
						if(addressModels.size()==0){

						}else {

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
}
