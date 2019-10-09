package com.liuwa.shopping.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.HeaderOrderByCategoryFragment;
import com.liuwa.shopping.activity.fragment.OrderShowByCategoryFragment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.OrderTitleModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class HeaderOrderByCategroyActivity extends BaseActivity implements HeaderOrderByCategoryFragment.OnFragmentInteractionListener {
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ViewPager vp_category;
	private TabLayout tl_tabs;
	private int position;
	private ArrayList<OrderTitleModel> cateList= DatasUtils.orderTitleModels;
	private ArrayList fragmentList=new ArrayList<>();;
	private ArrayList list_Title= new ArrayList<>();;
	private MyPagerAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_show_by_category_layout);
		this.context=this;
		init();
		initViews();
		initEvent();
		doGetDatas();
	}
	public void init(){
		position=getIntent().getIntExtra("position",0);
	}
	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title =(TextView)findViewById(R.id.tv_title);
		tv_title.setText("我的团单");
		tl_tabs   =(TabLayout)findViewById(R.id.tl_tabs);
		vp_category =(ViewPager)findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);
		tl_tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});


	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				HeaderOrderByCategroyActivity.this.finish();
				break;
			}
		}
	};

	@Override
	public void onFragmentInteraction(int position,int num) {

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
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.LeaderOrderCount);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue("请稍候", new LKHttpRequestQueueDone(){

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
						list_Title.add("待收货"+"("+jsonObject.getString("count1")+")");
						list_Title.add("待核销"+"("+jsonObject.getString("count2")+")");
						list_Title.add("已核销"+"("+jsonObject.getString("count3")+")");
						fragmentList.add(HeaderOrderByCategoryFragment.newInstance("0"));
						fragmentList.add(HeaderOrderByCategoryFragment.newInstance("1"));
						fragmentList.add(HeaderOrderByCategoryFragment.newInstance("2"));
						adapter.notifyDataSetChanged();
						tl_tabs.getTabAt(position).select();
						vp_category.setCurrentItem(position);
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
