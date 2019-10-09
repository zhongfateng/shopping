package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.liuwa.shopping.adapter.HeaderOrderDtAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class UserDTOrderActivity extends BaseActivity implements HeaderOrderDtAdapter.OnCartClick{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ListView gv_favoriate_list;
	private HeaderOrderDtAdapter fpAdapter;
	private ArrayList<OrderModel> productList = new ArrayList<OrderModel>();
	public  String memberid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dt_order_layout);
		this.context=this;
		memberid=getIntent().getStringExtra("memberid");
		initViews();
		initEvent();
		doGetDatas();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("用户待提货");
		gv_favoriate_list        = (ListView)findViewById(R.id.lv_show);
		fpAdapter                 =  new HeaderOrderDtAdapter(this,productList);
		fpAdapter.setOnCartClick(this);
		gv_favoriate_list.setAdapter(fpAdapter);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				UserDTOrderActivity.this.finish();
				break;
			}
		}
	};

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("memberid",memberid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.LeaderTihuo);
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
						JSONArray jsonObject = job.getJSONArray("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						productList.clear();
						productList.addAll(localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<OrderModel>>() {
								}.getType()));
						fpAdapter.notifyDataSetChanged();
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
	private LKAsyncHttpResponseHandler getHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						Toast.makeText(context,"已经收货",Toast.LENGTH_SHORT).show();
						doGetDatas();
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
	@Override
	public void cartOnClick(OrderModel model) {
		Intent intent =new Intent(context, OrderDetailActivity.class);
		intent.putExtra("order_id",model.orderId);
		startActivity(intent);
	}

	@Override
	public void hxClick(OrderModel model) {
		doDatas(model.orderId);
	}
	private void doDatas(String orderid){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("orderid",orderid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.LeaderTihuoDo);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}
}
