package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.AddressModel;
import com.liuwa.shopping.model.AreaModel;

import java.util.ArrayList;

public class AddressItemAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<AreaModel> addressList;

	public AddressItemAdapter(Context context, ArrayList<AreaModel> addressList) {
		this.context = context;
		this.addressList = addressList;
		layoutInflater = LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return addressList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return addressList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.fragment_area_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_addresss_name=(TextView)convertView.findViewById(R.id.tv_addresss_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final AreaModel  productModel= addressList.get(position);
		viewHolder.tv_addresss_name.setText(productModel.areaname);

		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_addresss_name,tv_link_num,tv_detail_address;
		public TextView tv_edit,tv_delete_address;
	}
}
