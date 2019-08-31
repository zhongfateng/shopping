package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.adapter.AddressAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.model.AddressModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyAddressActivity extends BaseActivity implements AddressAdapter.OnClick{
	private Context context;
	private ImageView imageView;
	private TextView  tv_title;
	private PullToRefreshListView pullToRefreshListView;
	private AddressAdapter addressAdapter;
	private ListView listView;
	private ArrayList<AddressModel> addressModels=new ArrayList<AddressModel>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_address_list_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		for (int i=0;i<4;i++){
			AddressModel model=new AddressModel();
			model.address_name="asdfasd"+i;
			addressModels.add(model);
		}
		imageView=(ImageView)findViewById(R.id.img_back);
		pullToRefreshListView=(PullToRefreshListView)findViewById(R.id.ptr_listView);
		listView=pullToRefreshListView.getRefreshableView();
		addressAdapter=new AddressAdapter(context,addressModels);
		addressAdapter.setOnClick(this);
		listView.setAdapter(addressAdapter);
		addressAdapter.notifyDataSetChanged();

	}
	
	public void initEvent(){
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("收货地址");
		imageView.setOnClickListener(onClickListener);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

			}
		});
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			    case R.id.img_back:
				 MyAddressActivity.this.finish();
				break;
				case R.id.tv_go_to_order:
					intent=new Intent(context,LoginActivity.class);
					startActivity(intent);
				break;
				case R.id.tv_go_to_index:
					intent=new Intent();
					intent.setAction(MainTabActivity.ACTION_TAB_INDEX);
					intent.putExtra(MainTabActivity.TAB_INDEX_KEY,3);
					sendBroadcast(intent);//发送标准广播
					MyAddressActivity.this.finish();
					break;
			}
		}
	};

	private void doGetDatas(){
//		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
//		productParam.put("start",page);
//		productParam.put("rows",pageSize);
//		productParam.put("classesid","1");
//		productParam.put("type",1);
//		productParam.put("timespan", System.currentTimeMillis()+"");
//		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
//		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
//		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
//		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
//		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
//		new LKHttpRequestQueue().addHttpRequest(categoryReq)
//				.executeQueue(null, new LKHttpRequestQueueDone(){
//
//					@Override
//					public void onComplete() {
//						super.onComplete();
//					}
//
//				});
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
//						JSONObject jsonObject = job.getJSONObject("data");
//						Gson localGson = new GsonBuilder().disableHtmlEscaping()
//								.create();
//						baseModel = localGson.fromJson(jsonObject.toString(),
//								new TypeToken<BaseDataModel<ProductModel>>() {
//								}.getType());
//						proList.addAll(baseModel.list);
//						fpAdapter.notifyDataSetChanged();

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
	public void editClick(AddressModel model) {
		//编辑
	}

	@Override
	public void deleteClick(AddressModel model) {
		//删除
	}
}
