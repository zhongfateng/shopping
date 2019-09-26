package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.IntegralFragment;
import com.liuwa.shopping.activity.fragment.ProductShowByCategoryFragment;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.UserModel;
import com.liuwa.shopping.util.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.liuwa.shopping.util.ScreenUtil.dip2px;


public class IntegralActivity extends BaseActivity implements IntegralFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ViewPager vp_category;
	private TabLayout tl_tabs;
	private TextView tv_keyong, tv_jifen, tv_duihuan;
	private ArrayList fragmentList;
	private ArrayList list_Title;
	private MyPagerAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_detail_layout);
		this.context = this;
		init();
		initViews();
		initEvent();
	}

	public void init() {
		fragmentList = new ArrayList<>();
		list_Title = new ArrayList<>();
		fragmentList.add(IntegralFragment.newInstance(Constants.GETINTEGRALORDER));
		fragmentList.add(IntegralFragment.newInstance(Constants.XIAOFEIORDER));
		list_Title.add("获取明细");
		list_Title.add("使用记录");
	}

	public void initViews() {
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("积分明细");
		tv_duihuan = (TextView) findViewById(R.id.tv_duihuan);
		tv_keyong = (TextView) findViewById(R.id.tv_keyong);
		tv_jifen = (TextView) findViewById(R.id.tv_jifen);
		tl_tabs = (TabLayout) findViewById(R.id.tb_top);
		vp_category = (ViewPager) findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);//此方法就是让tablayout和ViewPager联动
		Util.reflex(tl_tabs);
		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String userStr=pre.getString(Constants.USER,"");
		if(userStr!=null){
			UserModel model =new Gson().fromJson(userStr, UserModel.class);
			tv_keyong.setText(model.score+"");
			tv_jifen.setText("累计积分"+model.score);
		}


	}

	public void initEvent() {
		img_back.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.img_back:
					IntegralActivity.this.finish();
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


