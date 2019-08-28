package com.liuwa.shopping.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.liuwa.shopping.adapter.ShoppingCartAdapter;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.BaseDataModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.model.ShoppingCartModel;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.ToastUtil;
import com.liuwa.shopping.view.MyGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class CartShopActivity extends BaseActivity  implements ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface{
	private Context context;
	private ImageView img_back;
	private TextView tv_title,btnEdit,tvSettlement,tv_show_price;
	private CheckBox ckAll;
	private ShoppingCartAdapter shoppingCartAdapter;
	private boolean flag = false;
	private List<ShoppingCartModel> shoppingCartBeanList = new ArrayList<>();
	private ListView list_shopping_cart;
	private boolean mSelect;
	private double totalPrice = 0.00;// 购买的商品总价
	private int totalCount = 0;// 购买的商品总数量
	private static final String TAG = "CartShopActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart_list_layout);
		this.context=this;
		initViews();
		initEvent();
		doGetDatas();
	}

	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		img_back.setVisibility(View.INVISIBLE);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("购物车");
		btnEdit=(TextView)findViewById(R.id.tv_edit);
		btnEdit.setVisibility(View.VISIBLE);
		btnEdit.setText("编辑");
		list_shopping_cart=findViewById(R.id.list_shopping_cart);
		ckAll=(CheckBox) findViewById(R.id.ck_all);
		tvSettlement=(TextView)findViewById(R.id.tv_settlement);
		tv_show_price=(TextView) findViewById(R.id.tv_show_price);
		//装配数据
		shoppingCartAdapter = new ShoppingCartAdapter(this);
		shoppingCartAdapter.setCheckInterface(this);
		shoppingCartAdapter.setModifyCountInterface(this);
		list_shopping_cart.setAdapter(shoppingCartAdapter);
		shoppingCartAdapter.setShoppingCartModelList(shoppingCartBeanList);

	}
	
	public void initEvent(){
		btnEdit.setOnClickListener(onClickListener);
		ckAll.setOnClickListener(onClickListener);
		tvSettlement.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ck_all:
					if (shoppingCartBeanList.size() != 0) {
						if (ckAll.isChecked()) {
							for (int i = 0; i < shoppingCartBeanList.size(); i++) {
								shoppingCartBeanList.get(i).setChoosed(true);
							}
							shoppingCartAdapter.notifyDataSetChanged();
						} else {
							for (int i = 0; i < shoppingCartBeanList.size(); i++) {
								shoppingCartBeanList.get(i).setChoosed(false);
							}
							shoppingCartAdapter.notifyDataSetChanged();
						}
					}
					statistics();
					break;
				case R.id.tv_edit:
					flag = !flag;
					if (flag) {
						btnEdit.setText("完成");
						shoppingCartAdapter.isShow(false);
					} else {
						btnEdit.setText("编辑");
						shoppingCartAdapter.isShow(true);
					}
					break;
				case R.id.tv_settlement: //结算
					lementOnder();
					break;
			}
		}
	};

	//加载特殊分类商品 例如猜你喜欢！
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
	/**
	 * 结算订单、支付
	 */
	private void lementOnder() {
		//选中的需要提交的商品清单
		for (ShoppingCartModel bean:shoppingCartBeanList ){
			boolean choosed = bean.isChoosed();
			if (choosed){
				String shoppingName = bean.getShoppingName();
				int count = bean.getCount();
				double price = bean.getPrice();
				int size = bean.getDressSize();
				String attribute = bean.getAttribute();
				int id = bean.getId();
				Log.d(TAG,id+"----id---"+shoppingName+"---"+count+"---"+price+"--size----"+size+"--attr---"+attribute);
			}
		}

		//跳转到支付界面
	}
	/**
	 * 单选
	 * @param position  组元素位置
	 * @param isChecked 组元素选中与否
	 */
	@Override
	public void checkGroup(int position, boolean isChecked) {
		shoppingCartBeanList.get(position).setChoosed(isChecked);
		if (isAllCheck())
			ckAll.setChecked(true);
		else
			ckAll.setChecked(false);
		shoppingCartAdapter.notifyDataSetChanged();
		statistics();
	}
	/**
	 * 遍历list集合
	 * @return
	 */
	private boolean isAllCheck() {

		for (ShoppingCartModel group : shoppingCartBeanList) {
			if (!group.isChoosed())
				return false;
		}
		return true;
	}
	/**
	 * 统计操作
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
	 * 3.给底部的textView进行数据填充
	 */
	public void statistics() {
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < shoppingCartBeanList.size(); i++) {
			ShoppingCartModel shoppingCartBean = shoppingCartBeanList.get(i);
			if (shoppingCartBean.isChoosed()) {
				totalCount++;
				totalPrice += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
			}
		}
		tv_show_price.setText("合计:" + totalPrice);
		tvSettlement.setText("结算(" + totalCount + ")");
	}
	/**
	 * 增加
	 * @param position      组元素位置
	 * @param showCountView 用于展示变化后数量的View
	 * @param isChecked     子元素选中与否
	 */
	@Override
	public void doIncrease(int position, View showCountView, boolean isChecked) {
		ShoppingCartModel shoppingCartBean = shoppingCartBeanList.get(position);
		int currentCount = shoppingCartBean.getCount();
		currentCount++;
		shoppingCartBean.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");
		shoppingCartAdapter.notifyDataSetChanged();
		statistics();
	}
	/**
	 * 删减
	 *
	 * @param position      组元素位置
	 * @param showCountView 用于展示变化后数量的View
	 * @param isChecked     子元素选中与否
	 */
	@Override
	public void doDecrease(int position, View showCountView, boolean isChecked) {
		ShoppingCartModel shoppingCartBean = shoppingCartBeanList.get(position);
		int currentCount = shoppingCartBean.getCount();
		if (currentCount == 1) {
			return;
		}
		currentCount--;
		shoppingCartBean.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");
		shoppingCartAdapter.notifyDataSetChanged();
		statistics();
	}
	/**
	 * 删除
	 *
	 * @param position
	 */
	@Override
	public void childDelete(int position) {
		shoppingCartBeanList.remove(position);
		shoppingCartAdapter.notifyDataSetChanged();
		statistics();
	}

}
