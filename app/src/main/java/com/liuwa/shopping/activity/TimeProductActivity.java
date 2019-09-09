package com.liuwa.shopping.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.BlankFragment;
import com.liuwa.shopping.activity.fragment.IntegralFragment;
import com.liuwa.shopping.adapter.ImagePagerAdapter;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.view.AutoScrollViewPager;
import com.liuwa.shopping.view.MyViewPager;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


public class TimeProductActivity extends BaseActivity implements BlankFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private MyViewPager vp_category;
	private TabLayout tl_tabs;
	private ArrayList fragmentList;
	private ArrayList list_Title;
	private MyPagerAdapter adapter;
	private AutoScrollViewPager     index_auto_scroll_view;
	private CirclePageIndicator     cpi_indicator;
	private ImagePagerAdapter imageAdatper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_product_layout);
		this.context = this;
		init();
		initViews();
		initEvent();
	}

	public void init() {
		fragmentList = new ArrayList<>();
		list_Title = new ArrayList<>();
		fragmentList.add(BlankFragment.newInstance("dsaf","BlankFragment"));
		fragmentList.add(BlankFragment.newInstance("dsaf","BlankFragment"));
		fragmentList.add(BlankFragment.newInstance("dsaf","BlankFragment"));
		list_Title.add("详情");
		list_Title.add("评价");
		list_Title.add("售后");
	}

	public void initViews() {
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("商品西区你高兴");
		tl_tabs = (TabLayout) findViewById(R.id.tb_top);
		//设置TabLayout的模式
		tl_tabs.setTabMode(TabLayout.MODE_FIXED);

		//设置分割线
		LinearLayout linearLayout = (LinearLayout) tl_tabs.getChildAt(0);
		linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
		linearLayout.setDividerDrawable(ContextCompat.getDrawable(context,
				R.drawable.divider)); //设置分割线的样式
		linearLayout.setDividerPadding(ScreenUtil.dip2px(context,2)); //设置分割线间隔
		vp_category = (MyViewPager) findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);//此方法就是让tablayout和ViewPager联动

		index_auto_scroll_view  = (AutoScrollViewPager)findViewById(R.id.index_auto_scroll_view);
		cpi_indicator				= (CirclePageIndicator)findViewById(R.id.cpi_indicator);
		//修改了为了16：9的比例
		double height = getScreenPixel().widthPixels / (480 / 270.0);
		ViewGroup.LayoutParams params = index_auto_scroll_view.getLayoutParams();
		params.height = (int) (height);
		index_auto_scroll_view.setLayoutParams(params);
		imageAdatper=new ImagePagerAdapter(context, DatasUtils.imageList);
		index_auto_scroll_view.setAdapter(imageAdatper);
		cpi_indicator.setViewPager(index_auto_scroll_view);
		index_auto_scroll_view.startAutoScroll();
		index_auto_scroll_view.setInterval(4000);
		index_auto_scroll_view.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		imageAdatper.notifyDataSetChanged();
	}

	public void initEvent() {
		img_back.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.img_back:
					TimeProductActivity.this.finish();
					break;
			}
		}
	};

	@Override
	public void onFragmentInteraction(Uri uri) {

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


