package com.liuwa.shopping.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import java.util.HashMap;
import java.util.TreeMap;


public class ProductShowByCategroyActivity extends BaseActivity implements ProductShowByCategoryFragment.OnFragmentInteractionListener {
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private android.support.v4.view.ViewPager vp_category;
	private TabLayout tl_tabs;
	private int position;
	private ArrayList<CategoryModel> cateList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_show_by_category_layout);
		this.context=this;
		init();
		initViews();
		initEvent();
	}
	public void init(){
		position=getIntent().getIntExtra("position",0);
		cateList=(ArrayList<CategoryModel>)getIntent().getSerializableExtra("cateList");
	}
	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title =(TextView)findViewById(R.id.tv_title);
		tl_tabs   =(TabLayout)findViewById(R.id.tl_tabs);
		vp_category =(ViewPager)findViewById(R.id.vp_category);
		vp_category.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return ProductShowByCategoryFragment.newInstance(cateList.get(position));
			}

			@Override
			public int getCount() {
				return cateList.size();
			}
			@Nullable
			@Override
			public CharSequence getPageTitle(int position) {
				String title=cateList.get(position).getProClassesName();
				return title;
			}
		});
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
		tl_tabs.getTabAt(position).select();
		vp_category.setCurrentItem(position);

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
}
