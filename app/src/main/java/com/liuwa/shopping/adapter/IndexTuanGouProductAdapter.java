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
import com.liuwa.shopping.model.TuanProductModel;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class IndexTuanGouProductAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<TuanProductModel> productList;
	//public int foodpoition;

	public IndexTuanGouProductAdapter(Context context,ArrayList<TuanProductModel> productList) {
		this.context = context;
		this.productList=productList;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return  productList == null ? 0 :productList.size();
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
			convertView = layoutInflater.inflate(R.layout.activity_index_tuangou_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_title=(TextView)convertView.findViewById(R.id.tv_title);
			viewHolder.tv_show_price = (TextView) convertView
					.findViewById(R.id.tv_show_price);
			viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.img_show =(ImageView)convertView.findViewById(R.id.img_show);
			viewHolder.tv_show_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final TuanProductModel  productModel=productList.get(position);
		viewHolder.tv_show_price.setText(MoneyUtils.formatAmountAsString(new BigDecimal(productModel.realPrice)));
		viewHolder.tv_price.setText(MoneyUtils.formatAmountAsString(new BigDecimal(productModel.realPrice)));
		viewHolder.tv_title.setText(productModel.proName);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_show_price;
		public TextView tv_price,tv_title;
		public ImageView img_show;
	}
}
