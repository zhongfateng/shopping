package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.SPUtils;
import com.liuwa.shopping.util.ScreenUtil;
import com.liuwa.shopping.view.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;


public class FavoriateActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick{
	private Context context;
	private ImageView img_back;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private MyGridView gv_favoriate_list;
	private FavoriateProductAdapter fpAdapter;
	private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();
	public int page=1;
	public int pageSize=10;
	private TextView tv_title;
	public BaseDataModel<ProductModel>  baseModel;
	private String classesid;
	private ImageView img_top;
	private String name;
	public int key;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favoriate_show_layout);
		this.context=this;
		classesid=getIntent().getStringExtra("classesid");
		name=getIntent().getStringExtra("name");
		key=getIntent().getIntExtra("key",0);
		initViews();
		initEvent();
		doGetDatas();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText(name);
		img_top=(ImageView)findViewById(R.id.img_top);
		double height = ScreenUtil.resize(this);
		final ViewGroup.LayoutParams params = img_top.getLayoutParams();
		params.height = (int) (height);
		img_top.setLayoutParams(params);
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToScrollView);
		gv_favoriate_list        = (MyGridView)findViewById(R.id.gv_favoriate_list);
		fpAdapter                 =  new FavoriateProductAdapter(this,proList);
		fpAdapter.setOnCartClick(this);
		gv_favoriate_list.setAdapter(fpAdapter);
		gv_favoriate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel)parent.getAdapter().getItem(position);
				Intent intent=new Intent(context, ProductDetailActivity.class);
				intent.putExtra("model",model);
				startActivity(intent);
			}
		});
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				page=1;
				proList.clear();
				doGetDatas();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(baseModel.nowpage<baseModel.totalpage){
					page++;
					doGetDatas();
				}else{
					Toast.makeText(context,Constants.NOMOREDATA,Toast.LENGTH_SHORT).show();
				}
				pullToRefreshScrollView.onRefreshComplete();
			}
		});
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				FavoriateActivity.this.finish();
				break;
			}
		}
	};

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("page",page);
		productParam.put("rows",pageSize);
		productParam.put("classesid",classesid);
		productParam.put("type",1);
		productParam.put("area", SPUtils.getShequMode(context,Constants.AREA).area);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());



		TreeMap<String, Object> Param = new TreeMap<String, Object>();
		if(key==0) {
			Param.put("type", Constants.RXType);
		}else if(key==1)
		{
			Param.put("type", Constants.FavoriteType);
		}

		Param.put("timespan", System.currentTimeMillis()+"");
		Param.put("sign", Md5SecurityUtil.getSignature(Param));
		HashMap<String, Object> Map = new HashMap<String, Object>();
		Map.put(Constants.kMETHODNAME,Constants.SETTING);
		Map.put(Constants.kPARAMNAME, Param);
		LKHttpRequest Req = new LKHttpRequest(Map, getImageHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq,Req)
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
						pullToRefreshScrollView.onRefreshComplete();
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
	private LKAsyncHttpResponseHandler getImageHandler(){
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
						String imagepaht=jsonObject.getString("imgpath");
						ImageShowUtil.showImageByType(imagepaht,img_top);

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
