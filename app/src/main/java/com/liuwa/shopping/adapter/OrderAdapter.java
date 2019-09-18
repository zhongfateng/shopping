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
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<OrderModel> productList;
	OnCartClick  onCartClick;
	//public int foodpoition;

	public OrderAdapter(Context context, ArrayList<OrderModel> productList) {
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
			convertView = layoutInflater.inflate(R.layout.activity_oder_list_item_layout, null);
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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final OrderModel  productModel=productList.get(position);
		viewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.cartOnClick(productModel);
			}
		});
		viewHolder.tv_name.setText(productModel.childlist.get(0).proName);
		viewHolder.tv_price.setText(MoneyUtils.formatAmountAsString(new BigDecimal(productModel.childlist.get(0).buyPrice)));
		viewHolder.tv_num.setText("x"+productModel.childlist.get(0).buyNum+"");
		ImageShowUtil.showImage(productModel.childlist.get(0).fristimg,viewHolder.img_show);
		viewHolder.tv_total.setText(MoneyUtils.formatAmountAsString(new BigDecimal(productModel.total)));
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
	}
	public interface OnCartClick{
		public void cartOnClick(OrderModel model);
	}
}
