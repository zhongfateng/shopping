package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.IntegralModel;
import com.liuwa.shopping.model.Money;
import com.liuwa.shopping.util.TimeUtil;

import java.util.ArrayList;

public class MoneyItemAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<Money> moneyArrayList;
	//public int foodpoition;

	public MoneyItemAdapter(Context context, ArrayList<Money> moneyArrayList) {
		this.context = context;
		this.moneyArrayList = moneyArrayList;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return moneyArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return moneyArrayList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.fragment_money_list_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_detail = (TextView) convertView
					.findViewById(R.id.tv_detail);
			viewHolder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Money  model= moneyArrayList.get(position);
		if(model.equals("1")) {
			viewHolder.tv_detail.setText("充值" + model.money + "元");
			viewHolder.tv_time.setText(TimeUtil.getFormatTimeFromTimestamp(model.createDate.time, ""));
			viewHolder.tv_money.setText("+" + model.money);
		}else if(model.type.equals("2")){
			viewHolder.tv_detail.setText("消费" + model.money + "元");
			viewHolder.tv_time.setText(TimeUtil.getFormatTimeFromTimestamp(model.createDate.time, ""));
			viewHolder.tv_money.setText("-" + model.money);
		}else if(model.type.equals("3")){
			viewHolder.tv_detail.setText("退款" + model.money + "元");
			viewHolder.tv_time.setText(TimeUtil.getFormatTimeFromTimestamp(model.createDate.time, ""));
			viewHolder.tv_money.setText("+" + model.money);
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_detail;
		public TextView tv_time;
		public TextView tv_money;
	}
}
