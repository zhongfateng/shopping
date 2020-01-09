package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
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
import com.liuwa.shopping.activity.fragment.DialogFragmentFromBottom;
import com.liuwa.shopping.activity.fragment.IntegralDialogFragment;
import com.liuwa.shopping.activity.fragment.IntegralFragment;
import com.liuwa.shopping.activity.fragment.WebFragment;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.adapter.ImagePagerAdapter;
import com.liuwa.shopping.adapter.MyPagerAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.ProductChildModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.SPUtils;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.view.AutoScrollViewPager;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class IntegralProductDetailActivity extends BaseActivity implements DialogFragmentFromBottom.OnFragmentInteractionListener,WebFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private AutoScrollViewPager     index_auto_scroll_view;
	private CirclePageIndicator     cpi_indicator;
	private ImagePagerAdapter imageAdatper;
	private ArrayList<ImageItemModel> imgs=new ArrayList<>();
	private TextView tv_duihuan;
	private ProductModel model;
	public TextView tv_name,tv_kucun;
	public String peiSong="";
	private ArrayList<ProductChildModel> productChildModels=new ArrayList<>();
	private ArrayList fragmentList = new ArrayList<>();
	private ArrayList list_Title = new ArrayList<>();
	public TabLayout tl_tabs;
	public ViewPager vp_category;
	public MyPagerAdapter adapter;
	public TextView tv_jifen;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_product_detail_layout);
		this.context=this;
		model=(ProductModel) getIntent().getSerializableExtra("model");
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
		list_Title.add("服务");
	}
	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("商品详情");
		index_auto_scroll_view  = (AutoScrollViewPager)findViewById(R.id.index_auto_scroll_view);
		cpi_indicator				= (CirclePageIndicator)findViewById(R.id.cpi_indicator);
		//修改了为了1：1的比例
		double height = getScreenPixel().widthPixels / (100 / 100.0);
		ViewGroup.LayoutParams params = index_auto_scroll_view.getLayoutParams();
		params.height = (int) (height);
		index_auto_scroll_view.setLayoutParams(params);
		imageAdatper=new ImagePagerAdapter(context, imgs);
		index_auto_scroll_view.setAdapter(imageAdatper);
		cpi_indicator.setViewPager(index_auto_scroll_view);
		index_auto_scroll_view.startAutoScroll();
		index_auto_scroll_view.setInterval(4000);
		index_auto_scroll_view.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		tv_duihuan=(TextView)findViewById(R.id.tv_duihuan);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_kucun=(TextView)findViewById(R.id.tv_kucun);
		tv_jifen=(TextView)findViewById(R.id.tv_jifen);
		tl_tabs = (TabLayout) findViewById(R.id.tb_top);
		//设置TabLayout的模式
		tl_tabs.setTabMode(TabLayout.MODE_FIXED);

		//设置分割线
		LinearLayout linearLayout = (LinearLayout) tl_tabs.getChildAt(0);
		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
		linearLayout.setDividerDrawable(ContextCompat.getDrawable(context,
				R.drawable.divider)); //设置分割线的样式
		linearLayout.setDividerPadding(ScreenUtil.dip2px(context,2)); //设置分割线间隔
		vp_category = (ViewPager) findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);//此方法就是让tablayout和ViewPager联动
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_duihuan.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				IntegralProductDetailActivity.this.finish();
				break;
				case R.id.tv_duihuan:
					DialogFragmentFromBottom();
					break;
			}
		}
	};

	private void DialogFragmentFromBottom() {
		showDialog();
	}
	void showDialog() {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragmentFromBottom newFragment = DialogFragmentFromBottom.newInstance(model.proName,productChildModels,model.fristimg);
		newFragment.show(ft, "dialog");
	}

	private void doBuy(String prochildid,int num){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("leaderid", SPUtils.getShequMode(context,Constants.AREA).leaderId);
		baseParam.put("buynum",num);
		baseParam.put("buynum",prochildid);
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put(Constants.kMETHODNAME,Constants.JiFenADD);
		requestMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest cartReq = new LKHttpRequest(requestMap, buyHandler());

		new LKHttpRequestQueue().addHttpRequest(cartReq)
				.executeQueue("请稍候", new LKHttpRequestQueueDone(){

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
					else if(code==102){
						Toast.makeText(context,"当前抢购人数较多请稍后再试",Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("proheadid",model.proHeadId);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTDETAIL);
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
	private void showDialog(String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		IntegralDialogFragment newFragment = IntegralDialogFragment.newInstance(tag);
		newFragment.show(ft, "dialog");
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
						JSONObject oo=jsonObject.getJSONObject("product_head");
						ProductModel model = localGson.fromJson(oo.toString(),
								ProductModel.class);
						peiSong=model.peiSong;
						tv_name.setText(model.proName+"");
						tv_kucun.setText("剩余"+model.allKuCun+"件");
						tv_jifen.setText((int)model.showprice+"积分");
						init(model);
						adapter.notifyDataSetChanged();
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

//	@Override
//	public void onFragmentInteraction(String tag) {
//		if(tag.equals("0")){
//			//去充值
//			Intent intent =new Intent(context,IntegralActivity.class);
//			startActivity(intent);
//		}else if(tag.equals("1")){
//			//成功返回积分商城
//			Intent intent =new Intent(context,IntegralActivity.class);
//			startActivity(intent);
//			IntegralProductDetailActivity.this.finish();
//		}
//	}

	@Override
	public void onFragmentInteraction(String prochildid, int num) {
		Intent intent =new Intent(context,IntegralConfirmActivity.class);
		intent.putExtra("model",model);
		intent.putExtra("peiSong",peiSong);
		intent.putExtra("prochild",prochildid);
		intent.putExtra("num",num);
		startActivity(intent);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
