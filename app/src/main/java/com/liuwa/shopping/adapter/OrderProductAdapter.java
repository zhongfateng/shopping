package com.liuwa.shopping.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.model.OrderProductItem;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderProductAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<OrderProductItem> orderProductItems;
	//public int foodpoition;
	OnCartClick  onCartClick;

	public OrderProductAdapter(Context context, ArrayList<OrderProductItem> orderProductItems) {
		this.context = context;
		this.orderProductItems = orderProductItems;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setOnCartClick(OnCartClick onCartClick) {
		this.onCartClick = onCartClick;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderProductItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orderProductItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		final int location=position;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_confrim_order_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.img_show=(ImageView)convertView.findViewById(R.id.img_show);
			viewHolder.tv_num = (TextView) convertView
					.findViewById(R.id.tv_num);
			viewHolder.tv_p_num = (TextView) convertView
					.findViewById(R.id.tv_p_num);
			viewHolder.tv_status=(TextView)convertView.findViewById(R.id.tv_status);
			viewHolder.tv_pay=(TextView)convertView.findViewById(R.id.tv_pay);
			viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tv_connect=(TextView)convertView.findViewById(R.id.tv_connect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final OrderProductItem  productModel= orderProductItems.get(position);
		viewHolder.tv_name.setText(productModel.proName);
		viewHolder.tv_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(productModel.buyPrice)));
		viewHolder.tv_num.setText("x"+productModel.buyNum);
		viewHolder.tv_pay.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(productModel.total)));
		viewHolder.tv_status.setText(productModel.typename);
		if(productModel.type.equals("4"))
		{
			viewHolder.tv_connect.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tv_connect.setVisibility(View.GONE);
		}
		viewHolder.tv_connect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.pjClick(productModel);
			}
		});
		ImageShowUtil.showImage(productModel.fristimg,viewHolder.img_show);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_num;
		public TextView tv_price;
		public ImageView img_show;
		public TextView tv_name,tv_pay,tv_p_num;
		public TextView tv_status,tv_connect;
	}
	public interface OnCartClick{
		public void pjClick(OrderProductItem model);
	}
}
