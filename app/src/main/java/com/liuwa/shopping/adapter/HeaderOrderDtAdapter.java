package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.TimeUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class HeaderOrderDtAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<OrderModel> productList;
	OnCartClick  onCartClick;
	//public int foodpoition;

	public HeaderOrderDtAdapter(Context context, ArrayList<OrderModel> productList) {
		this.context = context;
		this.productList = productList;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setOnCartClick(OnCartClick onCartClick) {
		this.onCartClick = onCartClick;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.activity_user_dt_oder_list_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.tv_tag=(TextView)convertView.findViewById(R.id.tv_tag);
			viewHolder.img_show=(ImageView)convertView.findViewById(R.id.img_left);
			viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tv_num=(TextView)convertView.findViewById(R.id.tv_num);
			viewHolder.tv_total=(TextView)convertView.findViewById(R.id.tv_total);
			viewHolder.tv_order_num=(TextView)convertView.findViewById(R.id.tv_order_num);
			viewHolder.tv_detail=(TextView)convertView.findViewById(R.id.tv_detail);
			viewHolder.tv_hx=(TextView)convertView.findViewById(R.id.tv_hx);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final OrderModel  productModel=productList.get(position);
		viewHolder.tv_time.setText("下单时间："+ TimeUtil.getFormatTimeFromTimestamp(productModel.createDate.time,null));
		viewHolder.tv_tag.setText(productModel.childlist.get(0).typename);
		viewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.cartOnClick(productModel);
			}
		});
		viewHolder.tv_hx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.hxClick(productModel);
			}
		});
		viewHolder.tv_name.setText(productModel.childlist.get(0).proName);
		viewHolder.tv_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(productModel.childlist.get(0).buyPrice)));
		viewHolder.tv_num.setText("x"+productModel.childlist.get(0).buyNum+"");
		ImageShowUtil.showImage(productModel.childlist.get(0).fristimg,viewHolder.img_show);
		viewHolder.tv_total.setText("实付：￥"+MoneyUtils.formatAmountAsString(new BigDecimal(productModel.total)));
		viewHolder.tv_order_num.setText("共"+productModel.allbuynum+"件商品");
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_num;
		public TextView tv_price;
		public ImageView img_buy,img_show;
		public TextView tv_time;
		public TextView tv_tag;
		public TextView tv_name;
		public TextView tv_total;
		public TextView tv_order_num;
		public TextView tv_detail;
		public TextView tv_hx;
 	}
	public interface OnCartClick{
		public void cartOnClick(OrderModel model);
		public void hxClick(OrderModel model);
	}
}
