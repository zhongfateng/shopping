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
import com.liuwa.shopping.model.AddressModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddressAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<AddressModel> addressList;
	//public int foodpoition;
	OnClick onClick;

	public AddressAdapter(Context context, ArrayList<AddressModel> addressList) {
		this.context = context;
		this.addressList = addressList;
		layoutInflater = LayoutInflater.from(context);
	}

	public void setOnClick(OnClick onClick) {
		this.onClick = onClick;
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
			convertView = layoutInflater.inflate(R.layout.activity_my_address_list_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_addresss_name=(TextView)convertView.findViewById(R.id.tv_addresss_name);
			viewHolder.tv_link_num=(TextView) convertView.findViewById(R.id.tv_link_num);
			viewHolder.tv_detail_address=(TextView)convertView.findViewById(R.id.tv_detail_address);
			viewHolder.tv_edit=(TextView)convertView.findViewById(R.id.tv_edit);
			viewHolder.tv_delete_address=(TextView)convertView.findViewById(R.id.tv_delete_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final AddressModel  productModel= addressList.get(position);
		viewHolder.tv_addresss_name.setText(productModel.address_name);
		viewHolder.tv_delete_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClick.deleteClick(productModel);
			}
		});
		viewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClick.editClick(productModel);
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_addresss_name,tv_link_num,tv_detail_address;
		public TextView tv_edit,tv_delete_address;
	}
	public interface OnClick{
		public void 	editClick(AddressModel model);
		public void  deleteClick(AddressModel model);
	}
}
