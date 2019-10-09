package com.liuwa.shopping.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
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
import com.liuwa.shopping.activity.fragment.WebFragment;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.adapter.ImageAdapter;
import com.liuwa.shopping.adapter.ImagePagerAdapter;
import com.liuwa.shopping.adapter.MyPagerAdapter;
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
import com.liuwa.shopping.util.SPUtils;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.view.AutoScrollViewPager;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.ReferenceQueue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class ProductDetailActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick,DialogFragmentFromBottom.OnFragmentInteractionListener,WebFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private String leaderId;
	private LinearLayout ll_left,rl_cart;
	private TextView tv_add_cart,tv_buy;
	private ArrayList<ProductModel> neiborList=new ArrayList<>();
	private ArrayList<ProductModel> tuiJianList=new ArrayList<>();
	private MyGridView gw_list;
	private MyGridView gw_tuijian;
	private NeiborBuyAdapter neiborBuyAdapter;
	private FavoriateProductAdapter adapter;
	private MyGridView gw_toumai;
	private ArrayList<String> urls;
	private ImageAdapter imageAdapter;
	private TabLayout tl_tabs;
	private ViewPager vp_category;
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
	private TextView tv_kucun,tv_xiaoliang,tv_ship,tv_peisong;
	private ArrayList<ImageItemModel> imgs=new ArrayList<>();
	private ArrayList<ProductChildModel> productChildModels=new ArrayList<>();
	private ProductModel model;
	private ArrayList fragmentList= new ArrayList<>();;
	private ArrayList list_Title = new ArrayList<>();;
	private MyPagerAdapter pageradapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail_layout);
		this.context = this;
		model=(ProductModel)getIntent().getSerializableExtra("model");
		//init();
		initViews();
		initEvent();
		doGetDatas();
	}

	public void init(ProductModel model) {
		fragmentList.add(WebFragment.newInstance("dsaf",model.content));
		fragmentList.add(WebFragment.newInstance("dsaf",model.pjcontent));
		fragmentList.add(WebFragment.newInstance("dsaf",model.shcontent));
		list_Title.add("详情");
		list_Title.add("评价");
		list_Title.add("售后");
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

		//商品详情hmtl
		tl_tabs = (TabLayout) findViewById(R.id.tb_top);
		//设置分割线
		LinearLayout linearLayout = (LinearLayout) tl_tabs.getChildAt(0);
		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
		linearLayout.setDividerDrawable(ContextCompat.getDrawable(context,
				R.drawable.divider)); //设置分割线的样式
		linearLayout.setDividerPadding(ScreenUtil.dip2px(context,2)); //设置分割线间隔
		vp_category = (ViewPager) findViewById(R.id.vp_category);
		pageradapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(pageradapter);
		tl_tabs.setupWithViewPager(vp_category);//此方法就是让tablayout和ViewPager联动


		//多少人购买
		gw_toumai=(MyGridView)findViewById(R.id.gw_toumai);
		imageAdapter=new ImageAdapter(context,DatasUtils.strings);
		gw_toumai.setAdapter(imageAdapter);
		imageAdapter.notifyDataSetChanged();
		//邻居都在买
		neiborBuyAdapter=new NeiborBuyAdapter(context, neiborList);
		gw_list=(MyGridView)findViewById(R.id.gw_list);
		gw_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel) parent.getAdapter().getItem(position);
				Intent intent =new Intent(context,ProductDetailActivity.class);
				intent.putExtra("model",model);
				startActivity(intent);
				ProductDetailActivity.this.finish();
			}
		});
		gw_list.setAdapter(neiborBuyAdapter);
		//推荐购买
		adapter=new FavoriateProductAdapter(context,tuiJianList);
		adapter.setOnCartClick(this);
		gw_tuijian=(MyGridView)findViewById(R.id.gw_tuijian);
		gw_tuijian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel) parent.getAdapter().getItem(position);
				Intent intent =new Intent(context,ProductDetailActivity.class);
				intent.putExtra("model",model);
				startActivity(intent);
				ProductDetailActivity.this.finish();
			}
		});
		gw_tuijian.setAdapter(adapter);

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
		tv_peisong=findViewById(R.id.tv_peisong);

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
					if(token==null||token.length()==0){
						intent =new Intent(context,LoginActivity.class);
						startActivity(intent);
						return;
					}
					tag="1";
					DialogFragmentFromBottom();
					break;
				case R.id.tv_buy:
					//立即购买
					if(token==null||token.length()==0){
						intent =new Intent(context,LoginActivity.class);
						startActivity(intent);
						return;
					}
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
		DialogFragmentFromBottom	newFragment = DialogFragmentFromBottom.newInstance(model.proName,productChildModels,model.fristimg);
		newFragment.show(ft, "dialog");
	}
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("proheadid",model.proHeadId);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTDETAIL);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());

		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("leaderId", SPUtils.getShequMode(context,Constants.AREA));
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> requestNeiborMap = new HashMap<String, Object>();
		requestNeiborMap.put(Constants.kMETHODNAME,Constants.NEIBORBUY);
		requestNeiborMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest neiborReq = new LKHttpRequest(requestNeiborMap, getNeiborHandler());

		TreeMap<String, Object> Param = new TreeMap<String, Object>();
		Param.put("clssesid", model.classes);
		Param.put("timespan", System.currentTimeMillis()+"");
		Param.put("sign", Md5SecurityUtil.getSignature(Param));
		HashMap<String, Object> Map = new HashMap<String, Object>();
		Map.put(Constants.kMETHODNAME,Constants.TUIJIAN);
		Map.put(Constants.kPARAMNAME,Param);
		LKHttpRequest Req = new LKHttpRequest(Map, getTuiJianHandler());

		new LKHttpRequestQueue().addHttpRequest(categoryReq,neiborReq, Req)
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
						ProductModel model = localGson.fromJson(oo.toString(),
							ProductModel.class);
						tv_name.setText(model.proName+"");
						tv_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.showprice)));
						tv_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
						tv_market_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.price)));

						tv_kucun.setText("库存："+model.allKuCun);
						tv_xiaoliang.setText("销量："+model.allSaleNum);
						tv_ship.setText("运费：包邮");
						tv_peisong.setText(model.peiSong);
						imgs.clear();
						imgs.addAll((Collection<? extends ImageItemModel>)localGson.fromJson(jsonObject.getJSONArray("proimglist").toString(),
								new TypeToken<ArrayList<ImageItemModel>>() {
								}.getType()));
						imageAdatper.notifyDataSetChanged();
						productChildModels.clear();
						productChildModels.addAll((Collection<? extends ProductChildModel>)localGson.fromJson(jsonObject.getJSONArray("prochildlist").toString(),
								new TypeToken<ArrayList<ProductChildModel>>() {
								}.getType()));
						init(model);
						pageradapter.notifyDataSetChanged();
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
		baseParam.put("leaderid",SPUtils.getShequMode(context,Constants.AREA).leaderId);
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
					else  if(code==402)
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
	private void doAddCart(String prochildid,int num){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("proheadid",model.proHeadId);
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
					else if(code==402)
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
						neiborBuyAdapter.notifyDataSetChanged();

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

	@Override
	public void cartOnClick(ProductModel model) {

	}
	public void onResume(){
		super.onResume();
		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		token=pre.getString(Constants.TOKEN,"");
	}

	@Override
	public void onFragmentInteraction(String prochildid,int num) {
		if(tag.equals("0")){
			//立即购买
			doBuy(prochildid,num);
		}else if(tag.equals("1")){
			//加入购物车
			doAddCart(prochildid,num);
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}


