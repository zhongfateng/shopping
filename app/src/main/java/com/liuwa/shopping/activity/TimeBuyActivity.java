package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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
import com.liuwa.shopping.adapter.TimeBuyAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;


public class TimeBuyActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private MyGridView gv_favoriate_list;
	private TimeBuyAdapter timeBuyAdapter;
	private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();
	public int page=0;
	public int pageSize=10;
	private TextView tv_day,tv_hour,tv_min,tv_second,tv_tag;
	private long day,hour,min,mSecond;
	private LinearLayout ll_right;
	public String classesid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_buy_show_layout);
		this.context=this;
		classesid=getIntent().getStringExtra("classesid");
		initViews();
		initEvent();
		doGetDatas();

	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("限时秒杀");
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToScrollView);
		gv_favoriate_list        = (MyGridView)findViewById(R.id.gv_favoriate_list);
		timeBuyAdapter =  new TimeBuyAdapter(this, proList);
		gv_favoriate_list.setAdapter(timeBuyAdapter);
		gv_favoriate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel)parent.getAdapter().getItem(position);
				Intent intent=new Intent(context,TimeProductActivity.class);
				intent.putExtra("miaoinfoid",model.miaoInfoId);
				startActivity(intent);
			}
		});
		tv_tag=(TextView)findViewById(R.id.tv_tag);
		tv_day=(TextView)findViewById(R.id.tv_day);
		tv_hour=(TextView)findViewById(R.id.tv_hour);
		tv_min=(TextView)findViewById(R.id.tv_min);
		tv_second=(TextView)findViewById(R.id.tv_second);
		ll_right=(LinearLayout)findViewById(R.id.ll_right);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				doGetDatas();
				pullToRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				page++;
				doGetDatas();
				pullToRefreshScrollView.onRefreshComplete();
			}
		});
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				TimeBuyActivity.this.finish();
				break;
			}
		}
	};
	public  void doShowTime(long startTime,long endTime){
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long timeDQ = new Date().getTime();
			if(timeDQ<startTime){
				tv_tag.setText("活动尚未开启");
				ll_right.setVisibility(View.GONE);
			}else if(timeDQ>startTime&&timeDQ<endTime) {
				long validTimes = endTime;
				long date = validTimes - timeDQ;
				day = date / (1000 * 60 * 60 * 24);
				hour = (date / (1000 * 60 * 60) - day * 24);
				min = ((date / (60 * 1000)) - day * 24 * 60 - hour * 60);
				mSecond = (date / 1000) - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
				startRun();
			}else if(timeDQ>endTime){
				tv_tag.setText("活动已结束");
				ll_right.setVisibility(View.GONE);
			}
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
						ll_right.setVisibility(View.GONE);
						tv_tag.setText("已关闭");
					}
			ll_right.setVisibility(View.VISIBLE);
					tv_tag.setText("距结束：");
					tv_day.setText(day + "");
					tv_hour.setText(hour + "");
					tv_min.setText(min + "");
					tv_second.setText(mSecond + "");
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

	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.MiaoSha);
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
						JSONObject jsonObject = job.getJSONObject("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						proList.clear();
						proList.addAll(localGson.fromJson(jsonObject.getJSONArray("miaoinfolist").toString(),
								new TypeToken<ArrayList<ProductModel>>() {
								}.getType()));
						long startTime=jsonObject.getJSONObject("miao").getJSONObject("beginTime").getLong("time");
						long endTime=jsonObject.getJSONObject("miao").getJSONObject("endTime").getLong("time");
						timeBuyAdapter.notifyDataSetChanged();
						doShowTime(startTime,endTime);
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
