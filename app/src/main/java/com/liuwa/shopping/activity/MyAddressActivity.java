package com.liuwa.shopping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.AddressAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.AddressModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.util.Md5SecurityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class MyAddressActivity extends BaseActivity implements AddressAdapter.OnClick{
	private Context context;
	private ImageView imageView;
	private TextView  tv_title;
	private AddressAdapter addressAdapter;
	private ListView listView;
	private ArrayList<AddressModel> addressModels=new ArrayList<AddressModel>();
	private TextView tv_add;
	private static  final  int REQADD = 67;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_address_list_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("收货地址");
		imageView=(ImageView)findViewById(R.id.img_back);
		listView=(ListView) findViewById(R.id.ptr_listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AddressModel model=(AddressModel)parent.getAdapter().getItem(position);
				Intent intent =new Intent();
				intent.putExtra("addressid",model.addressId);
				setResult(Activity.RESULT_OK,intent);
				MyAddressActivity.this.finish();
			}
		});
		addressAdapter=new AddressAdapter(context,addressModels);
		addressAdapter.setOnClick(this);
		listView.setAdapter(addressAdapter);
		tv_add=(TextView)findViewById(R.id.tv_add);
	}
	
	public void initEvent(){
		imageView.setOnClickListener(onClickListener);
		tv_add.setOnClickListener(onClickListener);

	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			    case R.id.img_back:
				 MyAddressActivity.this.finish();
				break;
				case R.id.tv_add:
					intent=new Intent(context,AddAddressActivity.class);
					startActivityForResult(intent,REQADD);
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
					MyAddressActivity.this.finish();
					break;
			}
		}
	};

	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.GETADDRESS);
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
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						JSONArray jsonObject = job.getJSONArray("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						addressModels.clear();
						addressModels.addAll((Collection<? extends  AddressModel>)localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<AddressModel>>() {
								}.getType()));
						addressAdapter.notifyDataSetChanged();
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
	private void isDefault(String addressid){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("addressId", addressid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.ISADDRESS);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, areaHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}
	private LKAsyncHttpResponseHandler areaHandler(){
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
	public void editClick(AddressModel model) {
		//编辑
	}

	@Override
	public void deleteClick(AddressModel model) {
		//删除
		doDelete(model.addressId);

	}

	@Override
	public void isdefault(AddressModel model) {
		isDefault(model.addressId);
	}

	@Override
	public void onResume() {
		super.onResume();
		doGetDatas();
	}
	private void doDelete(String addressid){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("addressId", addressid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.DELETEADDRESS);
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
	private LKAsyncHttpResponseHandler getHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
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
}
