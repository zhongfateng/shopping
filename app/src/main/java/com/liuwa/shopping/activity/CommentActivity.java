package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.Md5SecurityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;


public class CommentActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private RatingBar rate_id,rate_sp_id,rate_wl_id,rate_fu_id;
	public OrderProductItem model;
	public ImageView img_left;
	public TextView tv_name;
	public TextView tv_price,tv_num,tv_guige;
	public EditText et_detail;
	public int num,num3,num2,num1;
	public TextView tv_comment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_layout);
		this.context=this;
		model=(OrderProductItem)getIntent().getSerializableExtra("model");
		initViews();
		initEvent();
	}

	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("评价");
		img_left=(ImageView)findViewById(R.id.img_left);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_price=(TextView)findViewById(R.id.tv_price);
		tv_num=(TextView)findViewById(R.id.tv_num);
		tv_guige=(TextView)findViewById(R.id.tv_guige);
		et_detail=(EditText)findViewById(R.id.et_detail);
		rate_id=(RatingBar)findViewById(R.id.rate_id);
		rate_sp_id=(RatingBar)findViewById(R.id.rate_sp_id);
		rate_wl_id=(RatingBar)findViewById(R.id.rate_wl_id);
		rate_fu_id=(RatingBar)findViewById(R.id.rate_fu_id);
		tv_comment=(TextView)findViewById(R.id.tv_comment);
		ImageShowUtil.showImage(model.fristimg,img_left);
		tv_name.setText(model.proName);
		tv_price.setText("￥"+model.buyPrice);
		tv_guige.setText(model.guiGe);
		tv_num.setText("x"+model.buyNum);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		rate_id.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				num=(int)rating;
			}
		});
		rate_sp_id.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				num1=(int)rating;
			}
		});
		rate_wl_id.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				num2=(int)rating;
			}
		});
		rate_fu_id.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				num3=(int)rating;
			}
		});
		tv_comment.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			    case R.id.img_back:
				 CommentActivity.this.finish();
				break;
				case R.id.tv_comment:
					String detail=et_detail.getText().toString();
					if(detail==null||detail.length()==0){
						Toast.makeText(context,"说点啥吧",Toast.LENGTH_SHORT).show();
						return;
					}
					doGetDatas(detail);
					break;
			}
		}
	};

	private void doGetDatas(String detail){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("content",detail);
		productParam.put("productheadid",model.proHeadId);
		productParam.put("score1",num);
		productParam.put("score2",num1);
		productParam.put("score3",num2);
		productParam.put("score4",num3);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.COmment);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue("请稍候", new LKHttpRequestQueueDone(){

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
						Toast.makeText(context,"感谢您的评价",Toast.LENGTH_SHORT).show();
						CommentActivity.this.finish();
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
