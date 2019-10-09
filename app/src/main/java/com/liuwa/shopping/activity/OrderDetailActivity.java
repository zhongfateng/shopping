package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.liuwa.shopping.util.TimeUtil;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
	private ArrayList<ProductModel> tuiJianList=new ArrayList<>();
	private FavoriateProductAdapter adapter;
	private TextView tv_cancel,tv_pay;
	private MyListView lv_show_list;
	private ArrayList<OrderProductItem> orderProductItems =new ArrayList<OrderProductItem>();
	public String order_id;
	public MyGridView gw_tuijian;
	public TextView tv_hudong,tv_youhui;
	public LinearLayout ll_bottom;
	private long day,hour,min,mSecond;
	public TextView tv_min,tv_seconds;
	public TextView tv_tip,tv_shouhuoren,tv_detail_leader,tv_order_num,tv_total,tv_p_num,tv_order_id,tv_time,tv_shouhuo_dz;
	public LinearLayout ll_top;
	public TextView tv_leader_name_tel;
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
		ll_top=(LinearLayout)findViewById(R.id.ll_top);
		lv_show_list=(MyListView)findViewById(R.id.lv_show_list);
		fpAdapter=new OrderProductAdapter(context,orderProductItems);
		lv_show_list.setAdapter(fpAdapter);
		lv_show_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		//推荐购买
		adapter=new FavoriateProductAdapter(context,tuiJianList);
		adapter.setOnCartClick(this);
		gw_tuijian=(MyGridView)findViewById(R.id.gv_favoriate_list);
		gw_tuijian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel) parent.getAdapter().getItem(position);
				Intent intent =new Intent(context,ProductDetailActivity.class);
				intent.putExtra("model",model);
				startActivity(intent);
			}
		});
		gw_tuijian.setAdapter(adapter);

		tv_cancel=(TextView)findViewById(R.id.tv_cancel);
		tv_pay=(TextView)findViewById(R.id.tv_pay_order);
		tv_tip=(TextView)findViewById(R.id.tv_tip);
		tv_shouhuoren=(TextView)findViewById(R.id.tv_tihuoren);
		tv_shouhuo_dz=(TextView)findViewById(R.id.tv_shouhuo_dz);
		tv_detail_leader=(TextView)findViewById(R.id.tv_detail_leader);
		tv_leader_name_tel=(TextView)findViewById(R.id.tv_leader_name_tel);
		tv_order_num=(TextView)findViewById(R.id.tv_order_num);
		tv_total=(TextView)findViewById(R.id.tv_total);
		tv_p_num=(TextView)findViewById(R.id.tv_p_num);
		tv_order_id=(TextView)findViewById(R.id.tv_order_id);
		tv_time=(TextView)findViewById(R.id.tv_time);
		tv_hudong=(TextView)findViewById(R.id.tv_hudong);
		tv_youhui=(TextView)findViewById(R.id.tv_youhui);
		ll_bottom=(LinearLayout)findViewById(R.id.ll_bottom);
		tv_min=(TextView)findViewById(R.id.tv_min);
		tv_seconds=(TextView)findViewById(R.id.tv_seconds);

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
				case R.id.tv_connect:
//					intent=new Intent(context,PayTypeActivity.class);
//					startActivity(intent);
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


		TreeMap<String, Object> Param = new TreeMap<String, Object>();
		Param.put("clssesid", "63");
		Param.put("timespan", System.currentTimeMillis()+"");
		Param.put("sign", Md5SecurityUtil.getSignature(Param));
		HashMap<String, Object> Map = new HashMap<String, Object>();
		Map.put(Constants.kMETHODNAME,Constants.TUIJIAN);
		Map.put(Constants.kPARAMNAME,Param);
		LKHttpRequest Req = new LKHttpRequest(Map, getTuiJianHandler());

		new LKHttpRequestQueue().addHttpRequest(categoryReq,Req)
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
	private LKAsyncHttpResponseHandler getTuiJianHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code== Constants.CODE)
					{
						JSONArray jsonObject = job.getJSONArray("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						tuiJianList.clear();
						localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<ProductModel>>() {
								}.getType());
						tuiJianList.addAll((Collection<? extends ProductModel>)localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<ProductModel>>() {
								}.getType()));
						adapter.notifyDataSetChanged();

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
							tv_shouhuo_dz.setText(add.getString("detail"));
						{
							JSONObject leader = jsonObject.getJSONObject("leadermap");
							tv_detail_leader.setText(leader.getString("taddress"));
							tv_leader_name_tel.setText(leader.getString("tname")+"    "+leader.getString("tel"));
						}
						{

						}
						tv_order_id.setText(orderModel.orderCode+"");
						tv_time.setText(TimeUtil.getFormatTimeFromTimestamp(orderModel.createDate.time,null));
						tv_youhui.setText("-￥："+MoneyUtils.formatAmountAsString(new BigDecimal(orderModel.youhui)));
						orderProductItems.clear();
						orderProductItems.addAll((Collection<? extends OrderProductItem>)localGson.fromJson(jsonObject.getJSONArray("order_childlist").toString(),
								new TypeToken<ArrayList<OrderProductItem>>() {
								}.getType()));
						tv_order_num.setText("共"+jsonObject.getString("allbuynum")+"件商品");
						tv_p_num.setText(jsonObject.getString("allbuynum")+"件商品");
						tv_total.setText("￥"+ MoneyUtils.formatAmountAsString(new BigDecimal(orderModel.total)));
						fpAdapter.notifyDataSetChanged();
						if(orderModel.type.equals("0")){
							ll_bottom.setVisibility(View.VISIBLE);
							ll_top.setVisibility(View.VISIBLE);
							//当前时间都超过了下单后的15分钟
							long interval=System.currentTimeMillis()- (orderModel.createDate.time+15*60*1000);
							if(interval>0){
								tv_min.setText("00");
								tv_seconds.setText("00");
								ll_bottom.setVisibility(View.GONE);
							}else {
								doShowTime(orderModel.createDate.time+15*60*1000);
							}
						}else if(orderModel.type.equals("1")){
							ll_top.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.GONE);
						}else if(orderModel.type.equals("2"))
						{
							ll_top.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.GONE);
						}
						else if(orderModel.type.equals("2"))
						{
							ll_top.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.GONE);
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

	public  void doShowTime(long endTime){
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long validTimes = endTime;
			long timeDQ = new Date().getTime();
			long date = validTimes - timeDQ;
			day = date / (1000 * 60 * 60 * 24);
			hour = (date / (1000 * 60 * 60) - day * 24);
			min = ((date / (60 * 1000)) - day * 24 * 60 - hour * 60);
			mSecond = (date / 1000) - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
			startRun();
		}catch (Exception e){

		}
	}
	private boolean isRun = true;
	private void startRun() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isRun) {
					try {
						Thread.sleep(1000); // sleep 1000ms
						Message message = Message.obtain();
						message.what = 6;
						handler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 6:
					computeTime();
					if (day < 0 && hour < 0 && min < 0) {
//						ll_right.setVisibility(View.GONE);
//						tv_tag.setText("已关闭");
						ll_bottom.setVisibility(View.GONE);
					}
					tv_min.setText(min + "");
					if(mSecond<10){
						tv_seconds.setText("0"+mSecond + "");
					}else {
						tv_seconds.setText(mSecond + "");
					}

					break;
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * 倒计时计算
	 */
	private void computeTime() {
		mSecond--;
		if (mSecond < 0) {
			min--;
			mSecond = 59;
			if (min < 0) {
				min = 59;
				hour--;
				if (hour < 0) {
					// 倒计时结束
					hour = 23;
					day--;
				}
			}
		}

	}

	@Override
	public void cartOnClick(ProductModel model) {
		Toast.makeText(this,"购物车点击"+model.proName,Toast.LENGTH_SHORT).show();
	}
}
