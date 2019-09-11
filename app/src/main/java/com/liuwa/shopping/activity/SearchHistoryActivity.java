package com.liuwa.shopping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.util.RecordSQLiteOpenHelper;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.view.FlowLayout;

import java.util.ArrayList;


public class SearchHistoryActivity extends BaseActivity{
	private Context context;
	EditText et_search;
	ImageView tv_clear;
	ImageView back;
	TextView tv_tip;
	public int colnum;
	private FlowLayout ll_flow;
	private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);;
	private SQLiteDatabase db;
	private BaseAdapter adapter;
	private int requestCode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_history);
		this.context=this;
		Intent intent = getIntent();
		requestCode = intent.getIntExtra("requestCode", 0);
		initViews();
		initEvent();
	}

	public void initViews() {
		back=(ImageView)findViewById(R.id.back);
		et_search=findViewById(R.id.et_search);
		ll_flow=findViewById(R.id.ll_flow);
		tv_tip=findViewById(R.id.tv_tip);
		tv_clear=(ImageView) findViewById(R.id.img_delete);
		// 清空搜索历史
		tv_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteData();
				queryData("");
			}
		});
		// 搜索框的键盘搜索键点击回调
		et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
					// 先隐藏键盘
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					// 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
					boolean hasData = hasData(et_search.getText().toString().trim());
					if (!hasData) {
						insertData(et_search.getText().toString().trim());
						queryData("");
					}
					// TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
					String value=et_search.getText().toString();
					setBackValue(value);
				}
				return false;
			}
		});

		// 搜索框的文本变化实时监听
		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() == 0) {
					tv_tip.setText("搜索历史");
				} else {
					tv_tip.setText("搜索结果");
				}
				String tempName = et_search.getText().toString();
				// 根据tempName去模糊查询数据库中有没有数据
				queryData(tempName);

			}
		});
		// 第一次进入查询所有的历史记录
		queryData("");

	}
	public void setBackValue(String value){
		if(requestCode==IndexActivity.ReqCode){
			Intent intent=new Intent(context,LoginActivity.class);
			intent.putExtra("searchKey", value);
			startActivity(intent);
			SearchHistoryActivity.this.finish();
		}else if(requestCode== NeiborActivity.SEARCHREQUESTCODE){
			Intent resultIntent = new Intent();
			resultIntent.putExtra("searchKey", value);
			setResult(RESULT_OK, resultIntent);
			finish();
		}
	}
	public void initEvent(){
		back.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				SearchHistoryActivity.this.finish();
				break;
			}
		}
	};
	/**
	 * 插入数据
	 */
	private void insertData(String tempName) {
		db = helper.getWritableDatabase();
		db.execSQL("insert into records(name) values('" + tempName + "')");
		db.close();
	}

	/**
	 * 模糊查询数据
	 */
	private void queryData(String tempName) {
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
		// 创建adapter适配器对象
//		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
//				new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		ll_flow.removeAllViews();
		if (cursor.moveToFirst()) {
			do{
				//取出数据,调用cursor.getInt/getString等方法
				final TextView label=(TextView) View.inflate(context,R.layout.search_list_item_layout,null);
				label.setText(cursor.getString(1));
				label.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(SearchHistoryActivity.this,LoginActivity.class);
						intent.putExtra("searchKey", label.getText().toString()+"");
						startActivity(intent);
						SearchHistoryActivity.this.finish();
					}
				});
				ll_flow.addView(label);
			}while(cursor.moveToNext());
		}
	}
	/**
	 * 检查数据库中是否已经有该条记录
	 */
	private boolean hasData(String tempName) {
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select id as _id,name from records where name =?", new String[]{tempName});
		//判断是否有下一个
		return cursor.moveToNext();
	}
	/**
	 * 清空数据
	 */
	private void deleteData() {
		db = helper.getWritableDatabase();
		db.execSQL("delete from records");
		db.close();
	}
	public class SearchListAdapter extends BaseAdapter {

		Context context;
		LayoutInflater layoutInflater;
		ArrayList<String> strings;
		//public int foodpoition;

		public SearchListAdapter(Context context, ArrayList<String> strings) {
			this.context = context;
			this.strings = strings;
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return strings.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return strings.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			final int location=position;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.search_list_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_text = (TextView) convertView
						.findViewById(R.id.tv_text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String  str=strings.get(position);
			viewHolder.tv_text.setText(str);
			//int itemWidth=viewHolder.tv_text.getWidth();
			int itemWidth = (ScreenUtil.getWindowsWidth((Activity) context) -  colnum * 10)  / colnum;
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(
					itemWidth,
					AbsListView.LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(param);
			return convertView;
		}

		public  class ViewHolder {
			public TextView tv_text;
		}
	}



}
