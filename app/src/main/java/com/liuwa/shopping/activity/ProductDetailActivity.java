package com.liuwa.shopping.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.DialogFragmentFromBottom;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.adapter.ImageAdapter;
import com.liuwa.shopping.adapter.ImagePagerAdapter;
import com.liuwa.shopping.adapter.NeiborBuyAdapter;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.ProductChildModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.view.AutoScrollViewPager;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class ProductDetailActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick,DialogFragmentFromBottom.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private String proheadid,leaderId;
	private LinearLayout ll_left,rl_cart;
	private TextView tv_add_cart,tv_buy;
	private ArrayList<ProductModel> neiborList=new ArrayList<>();
	private MyGridView gw_list;
	private MyGridView gw_tuijian;
	private NeiborBuyAdapter neiborBuyAdapter;
	private FavoriateProductAdapter adapter;
	private MyGridView gw_toumai;
	private ArrayList<String> urls;
	private ImageAdapter imageAdapter;
	private int num=1;
	private String token;
	//区分立即购买还是加入购物车
	private String tag;
	private ArrayList<String> list=new ArrayList<>();
	DialogFragmentFromBottom newFragment;
	private AutoScrollViewPager     index_auto_scroll_view;
	private CirclePageIndicator     cpi_indicator;
	private  ImagePagerAdapter      imageAdatper;
	private TextView tv_name,tv_price,tv_market_price;
	private TextView tv_kucun,tv_xiaoliang,tv_ship;
	private ArrayList<ImageItemModel> imgs=new ArrayList<>();
	private ArrayList<ProductChildModel> productChildModels=new ArrayList<>();
	private ProductModel model;
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
		token= ApplicationEnvironment.getInstance().getPreferences().getString(Constants.TOKEN, "");
	}

	public void initViews() {
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("商品详情");
		index_auto_scroll_view  = (AutoScrollViewPager)findViewById(R.id.index_auto_scroll_view);
		cpi_indicator				= (CirclePageIndicator)findViewById(R.id.cpi_indicator);
		imageAdatper=new ImagePagerAdapter(context, imgs);
		index_auto_scroll_view.setAdapter(imageAdatper);
		cpi_indicator.setViewPager(index_auto_scroll_view);
		index_auto_scroll_view.startAutoScroll();
		index_auto_scroll_view.setInterval(4000);
		index_auto_scroll_view.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);

		//多少人购买
		gw_toumai=(MyGridView)findViewById(R.id.gw_toumai);
		imageAdapter=new ImageAdapter(context,DatasUtils.strings);
		gw_toumai.setAdapter(imageAdapter);
		imageAdapter.notifyDataSetChanged();
		//邻居都在买
		neiborBuyAdapter=new NeiborBuyAdapter(context, DatasUtils.productModels);
		gw_list=(MyGridView)findViewById(R.id.gw_list);
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

		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_price=(TextView)findViewById(R.id.tv_price);
		tv_market_price=findViewById(R.id.tv_market_price);

		tv_kucun=(TextView)findViewById(R.id.tv_kucun);
		tv_xiaoliang=(TextView)findViewById(R.id.tv_xiaoliang);
		tv_ship=(TextView)findViewById(R.id.tv_ship);

	}
	public PopupWindow.OnDismissListener  dissmiss=new PopupWindow.OnDismissListener() {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub

		}
	};
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
					tag="1";
					DialogFragmentFromBottom();
					break;
				case R.id.tv_buy:
					//立即购买
					tag="0";
					DialogFragmentFromBottom();
					break;

			}
		}
	};
	private void DialogFragmentFromBottom() {
		showDialog();
	}
	void showDialog() {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragmentFromBottom	newFragment = DialogFragmentFromBottom.newInstance(model.proName,productChildModels);
		newFragment.show(ft, "dialog");
	}
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
						JSONObject jsonObject = job.getJSONObject("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						JSONObject oo=jsonObject.getJSONObject("product_head");
						model = localGson.fromJson(oo.toString(),
							ProductModel.class);
						tv_name.setText(model.proName+"");
						tv_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.showprice)));
						tv_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
						tv_market_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.price)));

						tv_kucun.setText("库存："+model.allKuCun);
						tv_xiaoliang.setText("销量："+model.allSaleNum);
						if(model.peiSong.equals("1")){
						tv_ship.setText("运费：包邮");
						}
						imgs.clear();
						imgs.addAll((Collection<? extends ImageItemModel>)localGson.fromJson(jsonObject.getJSONArray("proimglist").toString(),
								new TypeToken<ArrayList<ImageItemModel>>() {
								}.getType()));
						imageAdatper.notifyDataSetChanged();
						productChildModels.clear();
						productChildModels.addAll((Collection<? extends ProductChildModel>)localGson.fromJson(jsonObject.getJSONArray("prochildlist").toString(),
								new TypeToken<ArrayList<ProductChildModel>>() {
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
	private void doBuy(String prochildid,int num){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("leaderid","1");
		baseParam.put("prochildid",prochildid);
		baseParam.put("buynum",num);
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put(Constants.kMETHODNAME,Constants.BUY);
		requestMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest cartReq = new LKHttpRequest(requestMap, buyHandler());

		new LKHttpRequestQueue().addHttpRequest(cartReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}
	private LKAsyncHttpResponseHandler buyHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code== Constants.CODE) {
						String order_id=job.getString("data");
						Intent intent =new Intent(context,ConfirmOrderActivity.class);
						intent.putExtra("order_id",order_id);
						startActivity(intent);
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
	private void doAddCart(String prochildid,int num){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("proheadid",proheadid);
		baseParam.put("prochildid",prochildid);
		baseParam.put("num",num);
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put(Constants.kMETHODNAME,Constants.ADDCART);
		requestMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest cartReq = new LKHttpRequest(requestMap, addHandler());

		new LKHttpRequestQueue().addHttpRequest(cartReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}
	private LKAsyncHttpResponseHandler addHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code== Constants.CODE) {
						Toast.makeText(context,"添加购物车成功!!!",Toast.LENGTH_SHORT).show();
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

	@Override
	public void onFragmentInteraction(String prochildid,int num) {
		this.proheadid=proheadid;
		this.num=num;
		if(tag.equals("0")){
			//立即购买
			doBuy(prochildid,num);
		}else if(tag.equals("1")){
			//加入购物车
			doAddCart(prochildid,num);
		}
	}
}


