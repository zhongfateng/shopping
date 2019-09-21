package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class OrderDetailActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick{
	private Context context;
	private ImageView img_back;
	private MyGridView gv_favoriate_list;
	private OrderProductAdapter fpAdapter;
	private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();
	private TextView tv_title;
	public BaseDataModel<ProductModel>  baseModel;
	private TextView tv_cancel,tv_pay;
	private MyListView lv_show_list;
	private ArrayList<OrderProductItem> orderProductItems =new ArrayList<OrderProductItem>();
	public String order_id;
	public TextView tv_tip,tv_shouhuoren,tv_detail_leader,tv_order_num,tv_total,tv_p_num,tv_order_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail_layout);
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
		tv_title.setText("订单详情");
		lv_show_list=(MyListView)findViewById(R.id.lv_show_list);
		fpAdapter=new OrderProductAdapter(context,orderProductItems);
		lv_show_list.setAdapter(fpAdapter);
		lv_show_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel)parent.getAdapter().getItem(position);
				Toast.makeText(context,"item"+model.proName,Toast.LENGTH_SHORT).show();
			}
		});
		tv_cancel=(TextView)findViewById(R.id.tv_cancel);
		tv_pay=(TextView)findViewById(R.id.tv_pay_order);
		tv_tip=(TextView)findViewById(R.id.tv_tip);
		tv_shouhuoren=(TextView)findViewById(R.id.tv_tihuoren);
		tv_detail_leader=(TextView)findViewById(R.id.tv_detail_leader);
		tv_order_num=(TextView)findViewById(R.id.tv_order_num);
		tv_total=(TextView)findViewById(R.id.tv_total);
		tv_p_num=(TextView)findViewById(R.id.tv_p_num);
		tv_order_id=(TextView)findViewById(R.id.tv_order_id);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_cancel.setOnClickListener(onClickListener);
		tv_pay.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		Intent intent;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				OrderDetailActivity.this.finish();
				break;
				case R.id.tv_cancel:
					intent=new Intent(context,RefundActivity.class);
					startActivity(intent);
					break;
				case R.id.tv_pay_order:
					intent=new Intent(context,PayTypeActivity.class);
					startActivity(intent);
					break;
			}
		}
	};

	//加载特殊分类商品 例如猜你喜欢！
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
				.executeQueue("请稍候", new LKHttpRequestQueueDone(){

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
							JSONObject add=jsonObject.getJSONObject("addressmap");
							tv_shouhuoren.setText(add.getString("lxRen")+"  "+add.getString("lxTel"));
						{
							JSONObject leader = jsonObject.getJSONObject("leadermap");
							tv_detail_leader.setText(leader.getString("taddress"));
						}
						{

						}
						orderProductItems.clear();
						orderProductItems.addAll((Collection<? extends OrderProductItem>)localGson.fromJson(jsonObject.getJSONArray("order_childlist").toString(),
								new TypeToken<ArrayList<OrderProductItem>>() {
								}.getType()));
						tv_order_num.setText("共"+orderModel.allbuynum+"件商品");
						tv_p_num.setText(orderModel.allbuynum+"件商品");
						tv_total.setText("￥"+ MoneyUtils.formatAmountAsString(new BigDecimal(orderModel.total)));
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


	@Override
	public void cartOnClick(ProductModel model) {
		Toast.makeText(this,"购物车点击"+model.proName,Toast.LENGTH_SHORT).show();
	}
}