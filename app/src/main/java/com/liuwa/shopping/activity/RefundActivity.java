package com.liuwa.shopping.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.liuwa.shopping.adapter.RefundAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.model.ShoppingCartModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.TimeUtil;
import com.liuwa.shopping.view.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class RefundActivity extends BaseActivity implements FavoriateProductAdapter.OnCartClick,DialogFragmentSelectBottom.OnFragmentInteractionListener,RefundAdapter.CheckInterface{
	private Context context;
	private ImageView img_back;
	private MyGridView gv_favoriate_list;
	private FavoriateProductAdapter fpAdapter;
	private ArrayList<ProductModel> proList = new ArrayList<ProductModel>();
	public int page=1;
	public int pageSize=10;
	private TextView tv_title;
	public BaseDataModel<ProductModel>  baseModel;
	private ArrayList<OrderProductItem> orderProductItems =new ArrayList<OrderProductItem>();
	private String classesid;
	private ImageView img_top;
	private TextView tv_commit;
	private RelativeLayout rl_select;
	private TextView tv_reason;
	public String selectStr;
	public RefundAdapter refundAdapter;
	public ListView list_shopping_cart;
	public String 		order_id;
	public TextView tv_total;
	public double totalPrice;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refund_layout);
		this.context=this;
		order_id=(String) getIntent().getStringExtra("order_id");
		initViews();
		initEvent();
		doGetDatas();
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
		list_shopping_cart=(ListView)findViewById(R.id.list_shopping_cart);
		//装配数据
		refundAdapter = new RefundAdapter(this);
		refundAdapter.setCheckInterface(this);
		list_shopping_cart.setAdapter(refundAdapter);

		tv_commit=(TextView)findViewById(R.id.tv_commit);
		rl_select=(RelativeLayout)findViewById(R.id.rl_select);
		tv_reason=(TextView)findViewById(R.id.tv_reason);

		tv_total=(TextView)findViewById(R.id.tv_total);
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
				lementOnder();
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
		productParam.put("orderid",order_id);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.ORDERDETAIL);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getOrderHandler());
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
						Toast.makeText(context,"退款申请已经提交!",Toast.LENGTH_SHORT).show();

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

	private LKAsyncHttpResponseHandler getOrderHandler(){
		return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
						JSONObject jsonObject=job.getJSONObject("data");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						OrderModel orderModel=localGson.fromJson(jsonObject.getJSONObject("order_head").toString(), OrderModel.class);
						JSONObject add=jsonObject.getJSONObject("addressmap");
						orderProductItems.clear();
						orderProductItems.addAll((Collection<? extends OrderProductItem>)localGson.fromJson(jsonObject.getJSONArray("order_childlist").toString(),
								new TypeToken<ArrayList<OrderProductItem>>() {
								}.getType()));
						refundAdapter.setShoppingCartModelList(orderProductItems);
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

	@Override
	public void checkGroup(int position, boolean isChecked) {
		orderProductItems.get(position).setChoosed(isChecked);
		refundAdapter.notifyDataSetChanged();
		statistics();
	}
	private void lementOnder() {
		//选中的需要提交的商品清单
		StringBuffer proids = new StringBuffer();
		int i=0;
		for (OrderProductItem bean:orderProductItems ){
			boolean choosed = bean.isChoosed();
			if (choosed){
				proids.append(bean.orderChildId);
				proids.append(",");
				i++;
			}
		}
		if(i==0) {
			Toast.makeText(context,"请勾选退款订单",Toast.LENGTH_SHORT).show();
			return;

		}
		String finalproids = proids.deleteCharAt(proids.length() - 1).toString();
		tuikuanDatas(finalproids);
		//提交申请退款
	}
	/**
	 * 统计操作
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
	 * 3.给底部的textView进行数据填充
	 */
	public void statistics() {
		totalPrice = 0.00;
		for (int i = 0; i < orderProductItems.size(); i++) {
			OrderProductItem shoppingCartBean = orderProductItems.get(i);
			if (shoppingCartBean.isChoosed()) {
				totalPrice += shoppingCartBean.total;
			}
		}
		tv_total.setText("￥" + MoneyUtils.formatAmountAsString(new BigDecimal(totalPrice)));
	}
	private void tuikuanDatas(String orderchildids){
		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
		productParam.put("orderchildids",orderchildids);
		productParam.put("timespan", System.currentTimeMillis()+"");
		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
		requestCategoryMap.put(Constants.kMETHODNAME,Constants.Orderdaddressqr);
		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getNoticeHandler());
		new LKHttpRequestQueue().addHttpRequest(categoryReq)
				.executeQueue(null, new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}

				});
	}
}
