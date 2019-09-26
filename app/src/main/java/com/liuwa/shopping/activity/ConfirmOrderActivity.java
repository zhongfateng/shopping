package com.liuwa.shopping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
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
	private TextView tv_pay;
	private TextView tv_shouhuoren,tv_tel,tv_detail,tv_head_name,tv_didian;
	public static  final  int REQCODE=89;
	public String addressid="0";
	public TextView tv_tip,tv_p_num;
	public LinearLayout ll_goto_address;
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
		tv_pay=(TextView)findViewById(R.id.tv_pay);

		tv_shouhuoren=(TextView)findViewById(R.id.tv_shouhuoren);
		tv_tel=(TextView)findViewById(R.id.tv_tel);
		tv_detail=(TextView)findViewById(R.id.tv_detail);
		tv_head_name=(TextView)findViewById(R.id.tv_head_name);
		tv_didian=(TextView)findViewById(R.id.tv_didian);

		tv_tip=(TextView)findViewById(R.id.tv_tip);
		tv_p_num=(TextView)findViewById(R.id.tv_p_num);
		ll_goto_address=(LinearLayout)findViewById(R.id.ll_goto_address);

	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_pay.setOnClickListener(onClickListener);
		rl_add.setOnClickListener(onClickListener);
		ll_goto_address.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		Intent intent;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				ConfirmOrderActivity.this.finish();
				break;
			case R.id.ll_goto_address:
				intent =new Intent(context,MyAddressActivity.class);
				startActivityForResult(intent,REQCODE);
				break;
			case R.id.tv_pay:
//				Intent intent =new Intent(context,PayTypeActivity.class);
//				startActivity(intent);
				if(addressid.equals("0")){
					Toast.makeText(context,"请添加收货地址",Toast.LENGTH_SHORT).show();
					return;
				}
				comitAddress();
				break;
			case R.id.rl_add:
				intent =new Intent(context,MyAddressActivity.class);
				startActivityForResult(intent,REQCODE);
				break;
			}
		}
	};

	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("orderid",order_id);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.ORDERDETAIL);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getOrderHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
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
						JSONObject jsonObject=job.getJSONObject("data");
						tv_tip.setText(jsonObject.getString("yjps"));
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						OrderModel orderModel=localGson.fromJson(jsonObject.getJSONObject("order_head").toString(), OrderModel.class);
						if(orderModel.address.equals("0")){
							rl_add.setVisibility(View.VISIBLE);
							addressid="0";
							rl_address.setVisibility(View.GONE);
						}else
						{
							addressid=orderModel.address;
							JSONObject add=jsonObject.getJSONObject("addressmap");
							rl_address.setVisibility(View.VISIBLE);
							rl_add.setVisibility(View.GONE);
							tv_shouhuoren.setText(add.getString("lxRen"));
							tv_tel.setText(add.getString("lxTel"));
							tv_detail.setText(add.getString("detail"));
						}
						{
							JSONObject leader = jsonObject.getJSONObject("leadermap");
							tv_head_name.setText(leader.getString("tname"));
							tv_didian.setText(leader.getString("taddress"));

						}
						{

						}
						orderProductItems.clear();
						orderProductItems.addAll((Collection<? extends OrderProductItem>)localGson.fromJson(jsonObject.getJSONArray("order_childlist").toString(),
								new TypeToken<ArrayList<OrderProductItem>>() {
								}.getType()));
						tv_p_num.setText("共"+orderModel.allbuynum+"件商品");
						fpAdapter.notifyDataSetChanged();
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

	public void comitAddress(){
		TreeMap<String, Object> param = new TreeMap<String, Object>();
		param.put("addressid",addressid);
		param.put("orderid",order_id);
		param.put("timespan", System.currentTimeMillis()+"");
		param.put("sign", Md5SecurityUtil.getSignature(param));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.kMETHODNAME,Constants.ComitOrderAddr);
		map.put(Constants.kPARAMNAME, param);
		LKHttpRequest areaReq = new LKHttpRequest(map, getAddressHandler());

		new LKHttpRequestQueue().addHttpRequest(areaReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
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
						Intent intent=new Intent(context,PayTypeActivity.class);
						intent.putExtra("order_id",order_id);
						startActivity(intent);
						ConfirmOrderActivity.this.finish();
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
	public void onActivityResult(int requestCode, int resultCode, Intent i){
		if(requestCode == REQCODE){ // 对应启动时那个代号4
			if(resultCode == Activity.RESULT_OK){ // 对应B里面的标志为成功
				 addressid = i.getStringExtra("addressid");
				 rl_add.setVisibility(View.GONE);
				 rl_address.setVisibility(View.INVISIBLE);
			}
		}
	}

}
