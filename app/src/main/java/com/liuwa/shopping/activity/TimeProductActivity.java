package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.BlankFragment;
import com.liuwa.shopping.activity.fragment.IntegralFragment;
import com.liuwa.shopping.activity.fragment.WebFragment;
import com.liuwa.shopping.adapter.ImagePagerAdapter;
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
import com.liuwa.shopping.view.LoadingDialog;
import com.liuwa.shopping.view.LoadingTimeDialog;
import com.liuwa.shopping.view.MyViewPager;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class TimeProductActivity extends BaseActivity implements WebFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ViewPager vp_category;
	private TabLayout tl_tabs;

	private MyPagerAdapter adapter;
	private AutoScrollViewPager     index_auto_scroll_view;
	private CirclePageIndicator     cpi_indicator;
	private ImagePagerAdapter imageAdatper;
	public String miaoinfoid;
	private ArrayList<ImageItemModel> imgs=new ArrayList<>();
	public TextView tv_name;
	public TextView tv_price;
	public TextView tv_show_Price;
	public TextView tv_botoom;
	public TimeCount time;
	private ArrayList fragmentList = new ArrayList<>();
	private ArrayList list_Title = new ArrayList<>();
	public int num;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_product_layout);
		this.context = this;
		miaoinfoid=getIntent().getStringExtra("miaoinfoid");
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
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("miaoinfoid",miaoinfoid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.MiaoShaOrder);
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
						tv_name.setText(model.proName);
						tv_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.showprice)));
						tv_show_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
						tv_show_Price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.price)));
						init(model);
						adapter.notifyDataSetChanged();
						imgs.clear();
						imgs.addAll((Collection<? extends ImageItemModel>)localGson.fromJson(jsonObject.getJSONArray("proimglist").toString(),
								new TypeToken<ArrayList<ImageItemModel>>() {
								}.getType()));
						imageAdatper.notifyDataSetChanged();
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

	public void initViews() {
		time = new TimeCount(3000, 1000);//构造CountDownTimer对象
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("商品详情");
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

		index_auto_scroll_view  = (AutoScrollViewPager)findViewById(R.id.index_auto_scroll_view);
		cpi_indicator				= (CirclePageIndicator)findViewById(R.id.cpi_indicator);
		//修改了为了16：9的比例
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
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_price=(TextView)findViewById(R.id.tv_price);
		tv_show_Price=(TextView)findViewById(R.id.tv_show_Price);
		tv_botoom=(TextView)findViewById(R.id.tv_botoom);
	}

	public void initEvent() {
		img_back.setOnClickListener(onClickListener);
		tv_botoom.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.img_back:
					TimeProductActivity.this.finish();
					break;
				case R.id.tv_botoom:
					num=0;
					doBuy(1);
					break;
			}
		}
	};

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
	private void doBuy(int num){
		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("leaderid",SPUtils.getShequMode(context,Constants.AREA).leaderId);
		baseParam.put("buynum",num);
		baseParam.put("miaoinfoid",miaoinfoid);
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put(Constants.kMETHODNAME,Constants.MiaoShaOrderADD);
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
					}else if(code==406){
							//time.start();
							if(num==0) {
								LoadingTimeDialog loadingDialog = new LoadingTimeDialog(context);
								loadingDialog.setCanceledOnTouchOutside(false);
								loadingDialog.show();
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										//操作内容
										loadingDialog.dismiss();
										num++;
										doBuy(1);
									}
								}, 3000);
							}else {
								Toast.makeText(context, "库存紧张，请稍后再试", Toast.LENGTH_SHORT).show();
							}
					}
					else if(code==102){
						Toast.makeText(context,"当前抢购人数较多请稍后再试",Toast.LENGTH_SHORT).show();
					}else if(code==200)
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


	public class MyPagerAdapter extends FragmentPagerAdapter {
		private Context context;
		private List<Fragment> fragmentList;
		private List<String> list_Title;

		public MyPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> list_Title) {
			super(fm);
			this.context = context;
			this.fragmentList = fragmentList;
			this.list_Title = list_Title;
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return list_Title.size();
		}

		/**
		 * //此方法用来显示tab上的名字
		 *
		 * @param position
		 * @return
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return list_Title.get(position);
		}
	}
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			tv_botoom.setText("抢购中");
			tv_botoom.setClickable(true);
			doBuy(1);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示

			tv_botoom.setClickable(false);
			tv_botoom.setText("三秒后自动刷新重试");
		}
	}
}


