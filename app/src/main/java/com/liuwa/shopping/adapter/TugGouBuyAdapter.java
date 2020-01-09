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
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.ScreenUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TugGouBuyAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<ProductModel> productList;
	//public int foodpoition;

	public TugGouBuyAdapter(Context context, ArrayList<ProductModel> productList) {
		this.context = context;
		this.productList = productList;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView = layoutInflater.inflate(R.layout.activity_tuangou_buy_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.img_show=(ImageView)convertView.findViewById(R.id.img_show);
			viewHolder.tv_show_price = (TextView) convertView
					.findViewById(R.id.tv_show_price);
			viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.img_buy =(TextView)convertView.findViewById(R.id.img_buy);
			viewHolder.tv_tuan=(TextView)convertView.findViewById(R.id.tv_tuan);
			viewHolder.tv_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ScreenUtil.resizeImage(context,viewHolder.img_show);
		final ProductModel  productModel=productList.get(position);
		viewHolder.tv_name.setText(productModel.proName);
		viewHolder.tv_show_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(productModel.priceA)));
		viewHolder.tv_price.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(productModel.price)));
		ImageShowUtil.showImage(productModel.fristimg,viewHolder.img_show);
		viewHolder.tv_tuan.setText("已团"+productModel.saleNum);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_show_price,tv_name;
		public TextView tv_price,img_buy;
		public ImageView img_show;
		public TextView tv_tuan;
	}
}
