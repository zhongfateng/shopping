package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.HeaderApplayModel;
import com.liuwa.shopping.model.OrderModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;
import com.liuwa.shopping.util.TimeUtil;
import com.liuwa.shopping.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddHeaderAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<HeaderApplayModel> productList;
	OnCartClick  onCartClick;
	public String mparam;
	//public int foodpoition;

	public AddHeaderAdapter(Context context, ArrayList<HeaderApplayModel> productList,String mparam) {
		this.context = context;
		this.productList = productList;
		this.mparam=mparam;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setOnCartClick(OnCartClick onCartClick) {
		this.onCartClick = onCartClick;
	}
	public void setList(ArrayList<HeaderApplayModel> productList,String mparam) {
		this.mparam=mparam;
		this.productList = productList;
		notifyDataSetChanged();
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
			convertView = layoutInflater.inflate(R.layout.fragment_add_header_list_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.circle_left=(CircleImageView)convertView.findViewById(R.id.circle_left);
			viewHolder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tv_agress=(TextView)convertView.findViewById(R.id.tv_agress);
			viewHolder.tv_delete=(TextView)convertView.findViewById(R.id.tv_delete);
			viewHolder.tv_sc=(TextView)convertView.findViewById(R.id.tv_sc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final HeaderApplayModel  productModel=productList.get(position);
		viewHolder.tv_time.setText(TimeUtil.getFormatTimeFromTimestamp(productModel.createDate.time,null)+"申请");
		if(mparam.equals("1")){
			viewHolder.tv_agress.setVisibility(View.GONE);
			viewHolder.tv_delete.setVisibility(View.GONE);
			viewHolder.tv_sc.setVisibility(View.VISIBLE);
		}else if(mparam.equals("2")){
			viewHolder.tv_agress.setVisibility(View.VISIBLE);
			viewHolder.tv_delete.setVisibility(View.VISIBLE);
			viewHolder.tv_sc.setVisibility(View.GONE);
		}

		viewHolder.tv_agress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.agressClick(productModel);
			}
		});
		viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.deleteClick(productModel);
			}
		});
		viewHolder.tv_sc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCartClick.scClick(productModel);
			}
		});
		viewHolder.tv_name.setText(productModel.nickname);
		ImageLoader.getInstance().displayImage(productModel.headImg,viewHolder.circle_left);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_delete;
		public TextView tv_time;
		public TextView tv_name;
		public TextView tv_agress;
		public TextView tv_sc;
		public CircleImageView circle_left;
 	}
	public interface OnCartClick{
		public void agressClick(HeaderApplayModel model);
		public void deleteClick(HeaderApplayModel model);
		public void scClick(HeaderApplayModel model);
	}
}
