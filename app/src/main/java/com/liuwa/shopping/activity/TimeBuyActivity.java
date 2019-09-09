package com.liuwa.shopping.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class TimeBuyActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick{
	private Context context;
	private ImageView img_favoriate_back;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private MyGridView gv_favoriate_list;
	private FavoriateProductAdapter fpAdapter;
	private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();
	public int page=0;
	public int pageSize=10;
	public BaseDataModel<ProductModel>  baseModel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_buy_show_layout);
		this.context=this;
		initViews();
		initEvent();
		doGetDatas();
	}

	public void initViews()
	{
		img_favoriate_back=(ImageView)findViewById(R.id.img_favoriate_back);
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToScrollView);
		gv_favoriate_list        = (MyGridView)findViewById(R.id.gv_favoriate_list);
		fpAdapter                 =  new FavoriateProductAdapter(this,proList);
		fpAdapter.setOnCartClick(this);
		gv_favoriate_list.setAdapter(fpAdapter);
		gv_favoriate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel)parent.getAdapter().getItem(position);
				Toast.makeText(context,"item"+model.proName,Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void initEvent(){
		img_favoriate_back.setOnClickListener(onClickListener);
		pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				pullToRefreshScrollView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				pullToRefreshScrollView.onRefreshComplete();
			}
		});
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_favoriate_back:
				TimeBuyActivity.this.finish();
				break;
			}
		}
	};

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("start",page);
		productParam.put("rows",pageSize);
		productParam.put("classesid","1");
		productParam.put("type",1);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
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
						baseModel = localGson.fromJson(jsonObject.toString(),
								new TypeToken<BaseDataModel<ProductModel>>() {
								}.getType());
						proList.addAll(baseModel.list);
						fpAdapter.notifyDataSetChanged();

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


	@Override
	public void cartOnClick(ProductModel model) {
		Toast.makeText(this,"购物车点击"+model.proName,Toast.LENGTH_SHORT).show();
	}
}
