package com.liuwa.shopping.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.DialogFragmentSelectBottom;
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


public class RefundActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick,DialogFragmentSelectBottom.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private MyGridView gv_favoriate_list;
	private FavoriateProductAdapter fpAdapter;
	private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();
	public int page=1;
	public int pageSize=10;
	private TextView tv_title;
	public BaseDataModel<ProductModel>  baseModel;
	private String classesid;
	private ImageView img_top;
	private TextView tv_commit;
	private RelativeLayout rl_select;
	private TextView tv_reason;
	public String selectStr;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refund_layout);
		this.context=this;
		initViews();
		initEvent();
		//doGetDatas();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText("申请退款");
//		gv_favoriate_list        = (MyGridView)findViewById(R.id.gv_favoriate_list);
//		fpAdapter                 =  new FavoriateProductAdapter(this,proList);
//		fpAdapter.setOnCartClick(this);
//		gv_favoriate_list.setAdapter(fpAdapter);
//		gv_favoriate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				ProductModel model=(ProductModel)parent.getAdapter().getItem(position);
//				Toast.makeText(context,"item"+model.proName,Toast.LENGTH_SHORT).show();
//			}
//		});
		tv_commit=(TextView)findViewById(R.id.tv_commit);
		rl_select=(RelativeLayout)findViewById(R.id.rl_select);
		tv_reason=(TextView)findViewById(R.id.tv_reason);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_commit.setOnClickListener(onClickListener);
		rl_select.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				RefundActivity.this.finish();
				break;
			case R.id.tv_commit:
				RefundActivity.this.finish();
				break;
			case R.id.rl_select:
				DialogFragmentFromBottom();
				break;
			}
		}
	};
	private void DialogFragmentFromBottom() {
		showDialog();
	}
	void showDialog() {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragmentSelectBottom newFragment = DialogFragmentSelectBottom.newInstance();
		newFragment.show(ft, "dialog");
	}

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("page",page);
		productParam.put("rows",pageSize);
		productParam.put("classesid",classesid);
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

	@Override
	public void onFragmentInteraction(String selectStr) {
		this.selectStr=selectStr;
		tv_reason.setText(selectStr);
	}
}
