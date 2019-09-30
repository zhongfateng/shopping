package com.liuwa.shopping.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.MoneyApplayFragment;
import com.liuwa.shopping.activity.fragment.MoneyFragment;
import com.liuwa.shopping.util.Util;

import java.util.ArrayList;
import java.util.List;


public class MoneyApplayActivity extends BaseActivity implements MoneyApplayFragment.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private ViewPager vp_category;
	private TabLayout tl_tabs;
	private ArrayList fragmentList;
	private ArrayList list_Title;
	private MyPagerAdapter adapter;
	private TextView tv_kt,tv_dt;
	private EditText et_money;
	private TextView tv_duihuan;
	private String et_money_str="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money_applay_layout);
		this.context = this;
		init();
		initViews();
		initEvent();
	}

	public void init() {
		fragmentList = new ArrayList<>();
		list_Title = new ArrayList<>();
		fragmentList.add(MoneyApplayFragment.newInstance());
		fragmentList.add(MoneyApplayFragment.newInstance());
		list_Title.add("佣金记录");
		list_Title.add("使用记录");
	}

	public void initViews() {
		img_back = (ImageView) findViewById(R.id.img_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("团长佣金");
		tl_tabs = (TabLayout) findViewById(R.id.tb_top);
		vp_category = (ViewPager) findViewById(R.id.vp_category);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), context, fragmentList, list_Title);
		vp_category.setAdapter(adapter);
		tl_tabs.setupWithViewPager(vp_category);//此方法就是让tablayout和ViewPager联动
		Util.reflex(tl_tabs);
		et_money=(EditText)findViewById(R.id.et_money);
		tv_dt=(TextView)findViewById(R.id.tv_dt);
		tv_kt=(TextView)findViewById(R.id.tv_kt);
		tv_duihuan=(TextView)findViewById(R.id.tv_duihuan);
	}

	public void initEvent() {
		img_back.setOnClickListener(onClickListener);
		tv_duihuan.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.img_back:
					MoneyApplayActivity.this.finish();
					break;
				case R.id.tv_duihuan:
					et_money_str=et_money.getText().toString();
					if(et_money_str==null||et_money_str.length()==0){
						Toast.makeText(context,"请输入正确金额",Toast.LENGTH_SHORT).show();
						return;
					}
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


