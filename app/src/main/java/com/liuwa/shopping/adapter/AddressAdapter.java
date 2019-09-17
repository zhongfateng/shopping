package com.liuwa.shopping.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
			viewHolder.ctv_flag=(TextView)convertView.findViewById(R.id.ctv_flag);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final AddressModel  productModel= addressList.get(position);
		viewHolder.tv_addresss_name.setText("收货人："+productModel.lxRen);
		viewHolder.tv_link_num.setText(""+productModel.lxTel);
		viewHolder.tv_detail_address.setText(""+productModel.detail);
		viewHolder.tv_delete_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClick.deleteClick(productModel);
			}
		});
		viewHolder.ctv_flag.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				onClick.isdefault(productModel);
			}
		});

		viewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClick.editClick(productModel);
			}
		});
		if(productModel.isUsed.equals("1")){
			viewHolder.ctv_flag.setTextColor(context.getResources().getColor(R.color.tab_line));
			Drawable weather = context.getResources().getDrawable(R.mipmap.check);
			weather.setBounds(0, 0, weather.getMinimumWidth(), weather.getMinimumWidth());
			viewHolder.ctv_flag.setCompoundDrawables(weather, null, null, null);
		}else{
			viewHolder.ctv_flag.setTextColor(context.getResources().getColor(R.color.delete_color));
			Drawable weather = context.getResources().getDrawable(R.mipmap.un_check);
			weather.setBounds(0, 0, weather.getMinimumWidth(), weather.getMinimumWidth());
			viewHolder.ctv_flag.setCompoundDrawables(weather, null, null, null);
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_addresss_name,tv_link_num,tv_detail_address;
		public TextView tv_edit,tv_delete_address,ctv_flag;
	}
	public interface OnClick{
		public void 	editClick(AddressModel model);
		public void  deleteClick(AddressModel model);
		public void   isdefault(AddressModel model);
	}
}
