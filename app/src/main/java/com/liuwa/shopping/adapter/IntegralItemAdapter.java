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
import com.liuwa.shopping.model.IntegralModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class IntegralItemAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<IntegralModel> integralList;
	//public int foodpoition;

	public IntegralItemAdapter(Context context, ArrayList<IntegralModel> integralList) {
		this.context = context;
		this.integralList = integralList;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return integralList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return integralList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.fragment_integral_list_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_detail = (TextView) convertView
					.findViewById(R.id.tv_detail);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		IntegralModel  model= integralList.get(position);
		viewHolder.tv_detail.setText(model.description+"");
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_detail;
	}
}
