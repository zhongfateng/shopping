package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.IntegralFragment;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.adapter.ImageAdapter;
import com.liuwa.shopping.adapter.NeiborBuyAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class ProductDetailActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private String proheadid,leaderId;
	private LinearLayout ll_left,rl_cart;
	private TextView tv_add_cart,tv_buy;
	private ArrayList<ProductModel> neiborList=new ArrayList<>();
	private GridView gw_list;
	private MyGridView gw_tuijian;
	private NeiborBuyAdapter neiborBuyAdapter;
	private FavoriateProductAdapter adapter;
	private GridView gw_toumai;
	private ArrayList<String> urls;
	private ImageAdapter imageAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail_layout);
		this.context = this;
		proheadid=getIntent().getStringExtra("proheadid");
		leaderId=getIntent().getStringExtra("leaderId");
		init();
		initViews();
		initEvent();
		doGetDatas();
	}

	public void init() {

	}

	public void initViews() {
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("商品详情");
		//多少人购买
		gw_toumai=(GridView)findViewById(R.id.gw_toumai);
		imageAdapter=new ImageAdapter(context,DatasUtils.strings);
		gw_toumai.setAdapter(imageAdapter);
		imageAdapter.notifyDataSetChanged();
		//邻居都在买
		neiborBuyAdapter=new NeiborBuyAdapter(context, DatasUtils.productModels);
		gw_list=(GridView)findViewById(R.id.gw_list);
		gw_list.setAdapter(neiborBuyAdapter);
		neiborBuyAdapter.notifyDataSetChanged();
		//推荐购买
		adapter=new FavoriateProductAdapter(context,DatasUtils.productModels);
		adapter.setOnCartClick(this);
		gw_tuijian=(MyGridView)findViewById(R.id.gw_tuijian);
		gw_tuijian.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		ll_left=(LinearLayout)findViewById(R.id.ll_left);
		rl_cart=(LinearLayout)findViewById(R.id.rl_cart);
		tv_add_cart=(TextView)findViewById(R.id.tv_add_cart);
		tv_buy=(TextView)findViewById(R.id.tv_buy);


	}

	public void initEvent() {
		img_back.setOnClickListener(onClickListener);
		ll_left.setOnClickListener(onClickListener);
		rl_cart.setOnClickListener(onClickListener);
		tv_add_cart.setOnClickListener(onClickListener);
		tv_buy.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.img_back:
					ProductDetailActivity.this.finish();
					break;
				case R.id.ll_left:
					//返回首页
					intent=new Intent();
					intent.setAction(MainTabActivity.ACTION_TAB_INDEX);
					intent.putExtra(MainTabActivity.TAB_INDEX_KEY,0);
					sendBroadcast(intent);//发送标准广播
					ProductDetailActivity.this.finish();
					break;
				case R.id.rl_cart:
					intent=new Intent();
					intent.setAction(MainTabActivity.ACTION_TAB_INDEX);
					intent.putExtra(MainTabActivity.TAB_INDEX_KEY,2);
					sendBroadcast(intent);//发送标准广播
					ProductDetailActivity.this.finish();
					break;
				case R.id.tv_add_cart:
					//加入购物车
					break;
				case R.id.tv_buy:
					//立即购买
					break;

			}
		}
	};
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("proheadid",proheadid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTDETAIL);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());

		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("leaderId",leaderId);
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> requestNeiborMap = new HashMap<String, Object>();
		requestNeiborMap.put(Constants.kMETHODNAME,Constants.NEIBORBUY);
		requestNeiborMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest neiborReq = new LKHttpRequest(requestNeiborMap, getNeiborHandler());

		new LKHttpRequestQueue().addHttpRequest(categoryReq,neiborReq)
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
					JSONObject job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code== Constants.CODE)
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
	private LKAsyncHttpResponseHandler getNeiborHandler(){
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
						neiborList.clear();
						localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<ProductModel>>() {
								}.getType());
						neiborList.addAll((Collection<? extends ProductModel>)localGson.fromJson(jsonObject.toString(),
							new TypeToken<ArrayList<ProductModel>>() {
							}.getType()));

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

	}
}


