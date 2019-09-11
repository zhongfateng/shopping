package com.liuwa.shopping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.IndexProductAdapter;
import com.liuwa.shopping.adapter.IndexTuanGouProductAdapter;
import com.liuwa.shopping.adapter.jakewharton.salvage.RecyclingPagerAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.model.TuanModel;
import com.liuwa.shopping.model.TuanProductModel;
import com.liuwa.shopping.util.DatasUtils;
import com.liuwa.shopping.util.ListUtils;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.view.AutoScrollViewPager;
import com.liuwa.shopping.view.CircleImageView;
import com.liuwa.shopping.view.MyGridView;
import com.liuwa.shopping.view.indicator.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class IndexActivity extends BaseActivity implements IndexProductAdapter.OnCartClick{
	private Context context;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private AutoScrollViewPager     index_auto_scroll_view;
	private CirclePageIndicator     cpi_indicator;
	private  ImagePagerAdapter      imageAdatper;
	private MyGridView              index_category_type;
	private LinearLayout                tv_go_search;
	private TabLayout               tb_time;
	private MyGridView              mgw_guangou;
	private TextView tv_title;
	private IndexTuanGouProductAdapter indexTuanGouProductAdapter;
	private ArrayList<TuanProductModel> tuanItemList=new ArrayList<TuanProductModel>();
	private ArrayList<CategoryModel> cateList =new ArrayList<CategoryModel>();
	private ArrayList<ImageItemModel> imageUrlList=new ArrayList<ImageItemModel>();
	private ArrayList<TuanModel<TuanProductModel>> tuanList=new ArrayList<TuanModel<TuanProductModel>>();
	private ArrayList<ProductModel> productList=new ArrayList<ProductModel>();
	private MyGridAdapter  myGridAdapter;
	private ListView lv_show_list;
	IndexProductAdapter indexProductAdapter;
	public BaseDataModel<ProductModel>  baseModel;
	private LinearLayout ll_left,ll_down,ll_content;
	public static final int ReqCode = 3;
	private int page=1;
	private int pageSize=10;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_layout);
		this.context=this;
		initViews();
		initEvent();
		doGetDatas();
		}
	
	public void initViews()
	{
		ImageView img_back=(ImageView)findViewById(R.id.img_back);
		img_back.setVisibility(View.GONE);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("首页");
		tv_go_search				= (LinearLayout)findViewById(R.id.tv_go_search);
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToScrollView);
		index_auto_scroll_view  = (AutoScrollViewPager)findViewById(R.id.index_auto_scroll_view);
		cpi_indicator				= (CirclePageIndicator)findViewById(R.id.cpi_indicator);
		index_category_type      = (MyGridView)findViewById(R.id.index_category_type);
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
		myGridAdapter=new MyGridAdapter(this);
		index_category_type.setAdapter(myGridAdapter);
		//特殊分类实现
		ll_left=(LinearLayout)findViewById(R.id.ll_left);
		ll_down=(LinearLayout)findViewById(R.id.ll_down);
		ll_content=(LinearLayout)findViewById(R.id.ll_content);

		//团购tab实现
		tb_time=(TabLayout)findViewById(R.id.tb_time);
		mgw_guangou=(MyGridView)findViewById(R.id.mgw_guangou);
		indexTuanGouProductAdapter	= new IndexTuanGouProductAdapter(context,tuanItemList);
		mgw_guangou.setAdapter(indexTuanGouProductAdapter);
		tb_time.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int position=tab.getPosition();
				tuanItemList.clear();
				tuanItemList.addAll(tuanList.get(position).tuaninfolist);
				indexTuanGouProductAdapter.notifyDataSetChanged();

			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		//产品列表
		lv_show_list=(ListView)findViewById(R.id.lv_show_list);
		indexProductAdapter=new IndexProductAdapter(context,productList);
		indexProductAdapter.setOnCartClick(this);
		lv_show_list.setAdapter(indexProductAdapter);
		lv_show_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductModel model=(ProductModel) parent.getAdapter().getItem(position);
				Intent intent =new Intent(context,ProductDetailActivity.class);
				intent.putExtra("proheadid",model.proHeadId);
				startActivity(intent);
			}
		});

	}
	public void initEvent(){
		pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}
		});
		index_category_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CategoryModel model=(CategoryModel) parent.getAdapter().getItem(position);
				Intent intent =new Intent(context,ProductShowByCategroyActivity.class);
				intent.putExtra("position",position);
				intent.putExtra("cateList",cateList);
				startActivity(intent);
			}
		});
		tv_go_search.setOnClickListener(onClickListener);
		ll_left.setOnClickListener(onClickListener);
		ll_content.setOnClickListener(onClickListener);
		ll_down.setOnClickListener(onClickListener);

	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent;
			switch(v.getId()){
			case R.id.tv_go_search:
				intent=new Intent(context,SearchHistoryActivity.class);
				startActivity(intent);

//				intent = new Intent();
//				intent.setAction("android.intent.action.CALL");
//				intent.setData(Uri.parse("tel:" + "13181279291"));
//				startActivity(intent);
				break;
			case R.id.ll_left:
				intent=new Intent(context,TimeBuyActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_content:
				intent=new Intent(context,SearchHistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_down:
				intent=new Intent(context,FavoriateActivity.class);
				startActivity(intent);
				break;

				
			}
		}
	};
	//加载分类 公告
	private void doGetDatas(){
		TreeMap<String, Object> categorymap1 = new TreeMap<String, Object>();
		categorymap1.put("page",1);
		categorymap1.put("rows",10);
		categorymap1.put("timespan", System.currentTimeMillis()+"");
		categorymap1.put("sign",Md5SecurityUtil.getSignature(categorymap1));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.GETCATEGORY);
		requestCategoryMap.put(Constants.kPARAMNAME, categorymap1);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getCagegoryHandler());

		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("page", 1);
		map.put("rows", 2);
		map.put("mdtype", "1");
		HashMap<String, Object> noticeMap = new HashMap<String, Object>();
		noticeMap.put(Constants.kMETHODNAME,Constants.GETNOTICES);
		noticeMap.put(Constants.kPARAMNAME, map);
		LKHttpRequest noticeReq = new LKHttpRequest(noticeMap, getNoticeHandler());


		TreeMap<String, Object> baseParam = new TreeMap<String, Object>();
		baseParam.put("timespan", System.currentTimeMillis()+"");
		baseParam.put("sign", Md5SecurityUtil.getSignature(baseParam));
		HashMap<String, Object> tuangouMap = new HashMap<String, Object>();
		tuangouMap.put(Constants.kMETHODNAME,Constants.GETTUANGOU);
		tuangouMap.put(Constants.kPARAMNAME, baseParam);
		LKHttpRequest tuangouReq = new LKHttpRequest(tuangouMap, getTuanGouHandler());

		TreeMap<String, Object> baseProductParam = new TreeMap<String, Object>();
		baseProductParam.put("page",page);
		baseProductParam.put("rows",pageSize);
		baseProductParam.put("type","1");
		baseProductParam.put("timespan", System.currentTimeMillis()+"");
		baseProductParam.put("sign", Md5SecurityUtil.getSignature(baseProductParam));
		HashMap<String, Object> productMap = new HashMap<String, Object>();
		productMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
		productMap.put(Constants.kPARAMNAME, baseProductParam);
		LKHttpRequest productReq = new LKHttpRequest(productMap, getProductHandler());



		TreeMap<String, Object> specilCategory = new TreeMap<String, Object>();
		specilCategory.put("page", 1);
		specilCategory.put("rows", 3);
		specilCategory.put("timespan", System.currentTimeMillis()+"");
		specilCategory.put("sign",Md5SecurityUtil.getSignature(specilCategory));
		HashMap<String, Object> specialMap = new HashMap<String, Object>();
		specialMap.put(Constants.kMETHODNAME,Constants.GETSPECIALCATEGORY);
		specialMap.put(Constants.kPARAMNAME, specilCategory);
		LKHttpRequest specialCategoryReq = new LKHttpRequest(specialMap, getSpeicalCagegoryHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq,specialCategoryReq,productReq,tuangouReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){
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
						productList.addAll(baseModel.list);
						indexProductAdapter.notifyDataSetChanged();
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
						JSONArray array=job.getJSONArray("rs");
						imageUrlList.clear();
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						imageUrlList.addAll((Collection<? extends ImageItemModel>) localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<ImageItemModel>>() {
								}.getType()));
						imageAdatper.notifyDataSetChanged();
					}
					else {
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	private LKAsyncHttpResponseHandler getTuanGouHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
						tuanList.clear();
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						tuanList.addAll((Collection<? extends TuanModel<TuanProductModel>>)(localGson.fromJson(array.toString(),
								new TypeToken<ArrayList<TuanModel<TuanProductModel>>>() {
								}.getType())));
						for(TuanModel<TuanProductModel> model:tuanList){
							String tuancode=model.tuan.tuanCode;
							tb_time.addTab(tb_time.newTab().setText(tuancode+""));
						}
						tb_time.getTabAt(0).select();
                        tuanItemList.clear();
						tuanItemList.addAll(tuanList.get(0).tuaninfolist);
						indexTuanGouProductAdapter.notifyDataSetChanged();
					}
					else {
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
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
						myGridAdapter.notifyDataSetChanged();
					} else {
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	private LKAsyncHttpResponseHandler getSpeicalCagegoryHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						JSONArray array=job.getJSONArray("data");
					}
					else {
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
		Toast.makeText(context,model.fristimg,Toast.LENGTH_SHORT).show();
	}

	public class ImagePagerAdapter extends RecyclingPagerAdapter {

		private Context       context;
		private List<ImageItemModel> imageIdList;
		private LayoutInflater inflater;
		private int           size;
		private boolean       isInfiniteLoop;
		public ImagePagerAdapter(Context context, List<ImageItemModel> imageIdList) {
			this.context = context;
			this.imageIdList = imageIdList;
			this.size = ListUtils.getSize(imageIdList);
			isInfiniteLoop = false;
			inflater=LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// Infinite loop
			return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
		}
		/**
		 * get really position
		 *
		 * @param position
		 * @return
		 */
		private int getPosition(int position) {
			return isInfiniteLoop ? position % size : position;
		}

		@Override
		public View getView(int position, View view, ViewGroup container) {
			ViewHolder holder;
			if (view == null) {
				view=inflater.inflate(
						R.layout.index_image_list_item, null);
				holder = new ViewHolder();
				holder.imageView=(ImageView) view.findViewById(R.id.image_item);
				view.setTag(holder);
			} else {
				holder = (ViewHolder)view.getTag();
			}
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			final ImageItemModel model=imageIdList.get(position);
			ImageLoader.getInstance().displayImage(model.getImageUrl(), holder.imageView, new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.mipmap.ic_launcher)
					.showImageOnFail(R.mipmap.ic_launcher)
					.showImageOnLoading(R.mipmap.ic_launcher)
					.resetViewBeforeLoading(true)
					.cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(100))
					.build(), new SimpleImageLoadingListener());

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

			return view;
		}

		private  class ViewHolder {

			ImageView imageView;
		}

		/**
		 * @return the isInfiniteLoop
		 */
		public boolean isInfiniteLoop() {
			return isInfiniteLoop;
		}

		/**
		 * @param isInfiniteLoop the isInfiniteLoop to set
		 */
		public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
			this.isInfiniteLoop = isInfiniteLoop;
			return this;
		}
	}
	//分类适配器
	public class MyGridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Activity context;
		public MyGridAdapter(Context context) {
			this.context = (Activity) context;
			inflater = LayoutInflater.from(context);
		}
		public class AdapterViews {
			public TextView hot_name;
			public CircleImageView hot_image;
		}

		@Override
		public int getCount() {
			return cateList.size();
		}

		@Override
		public Object getItem(int location) {
			return cateList.get(location);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AdapterViews views;
			if (null == convertView) {
				convertView = inflater.inflate(
						R.layout.hot_type_item, null);
				views = new AdapterViews();
				views.hot_name=(TextView) convertView
						.findViewById(R.id.hot_type_name_id);
				views.hot_image=(CircleImageView)convertView.findViewById(R.id.hot_type_image);
				convertView.setTag(views);
			} else {
				views = (AdapterViews) convertView.getTag();
			}
			CategoryModel  Model=	cateList.get(position);
			views.hot_name.setText(Model.getProClassesName());
			ImageLoader.getInstance().displayImage("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1559814520,1200587741&fm=26&gp=0.jpg", views.hot_image, new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.mipmap.ic_launcher)
					.showImageOnFail(R.mipmap.ic_launcher)
					.showImageOnLoading(R.mipmap.ic_launcher)
					.resetViewBeforeLoading(true)
					.cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(100))
					.build(), new SimpleImageLoadingListener());
			return convertView;
		}
	}
}
