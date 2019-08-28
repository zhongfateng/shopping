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
import com.liuwa.shopping.model.CategoryModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FavoriateProductAdapter extends BaseAdapter {
	
	Context context;
	LayoutInflater layoutInflater;
	ArrayList<ProductModel> productList;
	OnCartClick  onCartClick;
	//public int foodpoition;

	public FavoriateProductAdapter(Context context, ArrayList<ProductModel> productList) {
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
			convertView = layoutInflater.inflate(R.layout.activity_favoriate_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_show_price = (TextView) convertView
					.findViewById(R.id.tv_show_price);
			viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.img_buy =(ImageView)convertView.findViewById(R.id.img_buy);
			viewHolder.tv_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ProductModel  productModel=productList.get(position);
		viewHolder.tv_show_price.setText(MoneyUtils.formatAmountAsString(new BigDecimal(productModel.showprice)));
		viewHolder.tv_price.setText(MoneyUtils.formatAmountAsString(new BigDecimal(productModel.Price)));
		viewHolder.img_buy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.cartOnClick(productModel);
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_show_price;
		public TextView tv_price;
		public ImageView img_buy;
	}
	public interface OnCartClick{
		public void cartOnClick(ProductModel model);
	}
}
