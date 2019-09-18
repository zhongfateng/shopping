package com.liuwa.shopping.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.OrderShowByCategoryFragment;
import com.liuwa.shopping.activity.fragment.ProductShowByCategoryFragment;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.OrderTitleModel;
import com.liuwa.shopping.util.DatasUtils;

import java.util.ArrayList;
import java.util.List;


public class OrderShowByCategroyActivity extends BaseActivity implements OrderShowByCategoryFragment.OnFragmentInteractionListener {
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ViewPager vp_category;
	private TabLayout tl_tabs;
	private int position;
	private ArrayList<OrderTitleModel> cateList= DatasUtils.orderTitleModels;
	private ArrayList fragmentList;
	private ArrayList list_Title;
	private MyPagerAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_show_by_category_layout);
		this.context=this;
		init();
		initViews();
		initEvent();
	}
	public void init(){
		position=getIntent().getIntExtra("position",0);
		fragmentList = new ArrayList<>();
		list_Title = new ArrayList<>();
		fragmentList.add(OrderShowByCategoryFragment.newInstance("0"));
		fragmentList.add(OrderShowByCategoryFragment.newInstance("1"));
		fragmentList.add(OrderShowByCategoryFragment.newInstance("2"));
		fragmentList.add(OrderShowByCategoryFragment.newInstance("3"));
		list_Title.add("获取明细");
		list_Title.add("使用记录");
		list_Title.add("获取明细");
		list_Title.add("获取明细");
	}
	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title =(TextView)findViewById(R.id.tv_title);
		tv_title.setText("订单列表");
		tl_tabs   =(TabLayout)findViewById(R.id.tl_tabs);
		vp_category =(ViewPager)findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
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
		tl_tabs.getTabAt(position).select();
		vp_category.setCurrentItem(position);

	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				OrderShowByCategroyActivity.this.finish();
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
}
