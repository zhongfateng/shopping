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
import com.liuwa.shopping.model.SheQuModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SheQuAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<SheQuModel> productList;
	//public int foodpoition;

	public SheQuAdapter(Context context, ArrayList<SheQuModel> productList) {
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
			convertView = layoutInflater.inflate(R.layout.activity_shequ_list_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.img_show=(ImageView)convertView.findViewById(R.id.img_show);
			viewHolder.tv_regison = (TextView) convertView
					.findViewById(R.id.tv_regison);
			viewHolder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tv_detail =(TextView)convertView.findViewById(R.id.tv_detail);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SheQuModel  productModel=productList.get(position);
		viewHolder.tv_regison.setText(productModel.region);
		viewHolder.tv_detail.setText(productModel.shiname+" "+productModel.areaname+" "+productModel.region);
		viewHolder.tv_name.setText(productModel.tname);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_regison;
		public TextView tv_name;
		public ImageView img_show;
		public TextView tv_detail;
	}
	public interface OnCartClick{
		public void cartOnClick(ProductModel model);
	}
}
