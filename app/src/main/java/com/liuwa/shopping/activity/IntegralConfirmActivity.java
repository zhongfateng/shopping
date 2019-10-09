package com.liuwa.shopping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.liuwa.shopping.adapter.FavoriateProductAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.AddressModel;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.SPUtils;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class IntegralConfirmActivity extends BaseActivity {
	private Context context;
	private ImageView img_back;
	public RelativeLayout rl_add;
	public LinearLayout rl_address;
	public TextView tv_shouhuoren,tv_tel,tv_detail,tv_didian,tv_head_name,tv_tip,tv_p_num;
	public TextView tv_name,tv_count;
	public ImageView img_show;
	public TextView tv_confirm;
	public TextView tv_commit;
	public ProductModel model;
	public  String prochild;
	public int num=1;
	public String addressid;
	public static  final  int REQCODE=89;
	public TextView tv_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_confirm_layout);
		this.context=this;
		model=(ProductModel) getIntent().getSerializableExtra("model");
		prochild=getIntent().getStringExtra("prochild");
		num=getIntent().getIntExtra("num",1);
		initViews();
		initEvent();
		doGetDatas();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("订单确认");
		rl_add=(RelativeLayout)findViewById(R.id.rl_add);
		rl_address=(LinearLayout)findViewById(R.id.rl_address);
		tv_shouhuoren=(TextView)findViewById(R.id.tv_shouhuoren);
		tv_tel=(TextView)findViewById(R.id.tv_tel);
		tv_detail=(TextView)findViewById(R.id.tv_detail);
		tv_didian=(TextView)findViewById(R.id.tv_didian);
		tv_head_name=(TextView)findViewById(R.id.tv_head_name);


		tv_tip=(TextView)findViewById(R.id.tv_tip);
		tv_tip.setText(model.peiSong);

		tv_p_num=(TextView)findViewById(R.id.tv_p_num);
		tv_p_num.setText("1件商品");

		img_show=(ImageView)findViewById(R.id.img_show);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_name.setText(model.proName);
		tv_count=(TextView)findViewById(R.id.tv_count);
		tv_count.setText(Double.toString(model.showprice));
		tv_confirm=(TextView)findViewById(R.id.tv_confirm);
		tv_confirm.setText(Double.toString(model.showprice));
		tv_commit=(TextView)findViewById(R.id.tv_commit);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_commit.setOnClickListener(onClickListener);
		rl_add.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				IntegralConfirmActivity.this.finish();
				break;
			case R.id.rl_add:
				Intent intent =new Intent(context,MyAddressActivity.class);
				startActivityForResult(intent,REQCODE);
				break;

			case R.id.tv_commit:
				if(addressid==null||addressid.length()==0){
					Toast.makeText(context,"请添加收货地址",Toast.LENGTH_SHORT).show();
					return;
				}
				doCommitDatas();
				break;
			}
		}
	};
	//加载特殊分类商品 例如猜你喜欢！
	private void doCommitDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("leaderid",SPUtils.getShequMode(context,Constants.AREA).leaderId);
		productParam.put("prochildid",prochild);
		productParam.put("buynum",num);
		productParam.put("addressid",addressid);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.ScoreOrder);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getAreaHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}
	private LKAsyncHttpResponseHandler getAreaHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						String order_id=job.getString("data");
						Intent intent =new Intent(context,PaySuccessActivity.class);
						intent.putExtra("order_id",order_id);
						startActivity(intent);
						IntegralConfirmActivity.this.finish();
					} else
					{
						Toast.makeText(context,job.getString("msg"),Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	//加载特殊分类商品 例如猜你喜欢！
	private void doGetDatas(){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("isused",1);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.GETADDRESS);
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
						JSONArray jsonObject = job.getJSONArray("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						ArrayList<AddressModel> list=localGson.fromJson(jsonObject.toString(),
								new TypeToken<ArrayList<AddressModel>>() {
								}.getType());
						if(list.size()==0){
							rl_add.setVisibility(View.VISIBLE);
							rl_address.setVisibility(View.GONE);
						}else{
							rl_add.setVisibility(View.GONE);
							rl_address.setVisibility(View.VISIBLE);
							AddressModel model=list.get(0);
							setData(model);
						}
						tv_didian.setText(SPUtils.getShequMode(context,Constants.AREA).region);
						tv_head_name.setText(SPUtils.getShequMode(context,Constants.AREA).tname);

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
	public void setData(AddressModel model)
	{
		tv_shouhuoren.setText(model.lxRen);
		tv_tel.setText(model.lxTel);
		tv_detail.setText(model.shiname+model.quname+model.detail);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent i){
		if(requestCode == REQCODE){ // 对应启动时那个代号4
			if(resultCode == Activity.RESULT_OK){ // 对应B里面的标志为成功
				AddressModel model = (AddressModel) i.getSerializableExtra("address_model");
				addressid=model.addressId;
				rl_add.setVisibility(View.GONE);
				rl_address.setVisibility(View.VISIBLE);
				setData(model);
			}
		}
	}

}
