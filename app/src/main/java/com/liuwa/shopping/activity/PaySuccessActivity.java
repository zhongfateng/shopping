package com.liuwa.shopping.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class PaySuccessActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private TextView tv_title,tv_go_to_order,tv_go_to_index;
	private String order_id;
	private TextView tv_commit;
	private TextView tv_success;
	private int key;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success_layout);
		this.context=this;
		order_id=getIntent().getStringExtra("order_id");
		key=getIntent().getIntExtra("key",0);
		initViews();
		initEvent();
	}

	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("支付成功");
		tv_commit=(TextView)findViewById(R.id.tv_commit);
		tv_success=(TextView)findViewById(R.id.tv_success);
		if(key!=0){
			tv_success.setText("兑换成功");
		}
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_commit.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			    case R.id.img_back:
				 PaySuccessActivity.this.finish();
				break;
				case R.id.tv_commit:
					intent=new Intent(context,OrderDetailActivity.class);
					intent.putExtra("order_id",order_id);
					startActivity(intent);
					PaySuccessActivity.this.finish();
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
}
