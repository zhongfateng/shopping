package com.liuwa.shopping.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.liuwa.shopping.activity.fragment.ProductShowByCategoryFragment;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class ProductShowByCategroyActivity extends BaseActivity implements ProductShowByCategoryFragment.OnFragmentInteractionListener {
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private android.support.v4.view.ViewPager vp_category;
	private TabLayout tl_tabs;
	private int position;
	private ArrayList<CategoryModel> cateList=new ArrayList<>();
	private ArrayList<Fragment> fragments=new ArrayList<>();
	public MyPagerAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_show_by_category_layout);
		this.context=this;
		//init();
		initViews();
		initEvent();
		doGetData();
	}
	public void init(){
		position=getParent().getIntent().getIntExtra("position",0);
		cateList=(ArrayList<CategoryModel>)getParent().getIntent().getSerializableExtra("cateList");
	}
	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		img_back.setVisibility(View.GONE);
		tv_title =(TextView)findViewById(R.id.tv_title);
		tl_tabs   =(TabLayout)findViewById(R.id.tl_tabs);
		vp_category =(ViewPager)findViewById(R.id.vp_category);
		adapter=new MyPagerAdapter(getSupportFragmentManager(),context,fragments,cateList);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);
		tl_tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				tv_title.setText(tab.getText());;
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

	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_favoriate_back:
				ProductShowByCategroyActivity.this.finish();
				break;
			}
		}
	};

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
	public void doGetData(){
		TreeMap<String, Object> categorymap1 = new TreeMap<String, Object>();
		categorymap1.put("timespan", System.currentTimeMillis()+"");
		categorymap1.put("sign",Md5SecurityUtil.getSignature(categorymap1));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.GETCATEGORY);
		requestCategoryMap.put(Constants.kPARAMNAME, categorymap1);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getCagegoryHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}
	private LKAsyncHttpResponseHandler getCagegoryHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
						cateList.clear();
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						cateList.addAll((Collection<? extends CategoryModel>) localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<CategoryModel>>() {
								}.getType()));
						for(CategoryModel model :cateList){
							fragments.add(ProductShowByCategoryFragment.newInstance(model));
						}
						adapter.notifyDataSetChanged();
//						tl_tabs.getTabAt(position).select();
//						vp_category.setCurrentItem(position);
					} else {
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
		private List<CategoryModel> list_Title;

		public MyPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<CategoryModel> list_Title) {
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
			return list_Title.get(position).getProClassesName();
		}
	}
}
