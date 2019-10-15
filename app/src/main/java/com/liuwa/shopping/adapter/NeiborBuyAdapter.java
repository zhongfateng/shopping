package com.liuwa.shopping.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.MoneyModel;
import com.liuwa.shopping.model.ProductModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NeiborBuyAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<ProductModel> productModelArrayList;
	public NeiborBuyAdapter(Context context, ArrayList<ProductModel> productModelArrayList){
		this.context = context;
		this.productModelArrayList = productModelArrayList;
		inflater= LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productModelArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productModelArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder  holder = null;
	    if(convertView==null){
	    convertView = inflater.inflate(R.layout.activity_neibor_buy_list_item_layout, null);
	    holder = new ViewHolder();
	    holder.imageView=(ImageView)convertView.findViewById(R.id.img_show);
        holder.tv_pro_name =(TextView)convertView.findViewById(R.id.tv_pro_name);
        holder.tv_left=(TextView)convertView.findViewById(R.id.tv_left);
        holder.tv_right=(TextView)convertView.findViewById(R.id.tv_right);
        holder.tv_right.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        convertView.setTag(holder);
	    }
	    else{
	    holder=(ViewHolder)convertView.getTag();
	    }

		ProductModel model= productModelArrayList.get(position);
	    holder.tv_pro_name.setText(model.proName);
		holder.tv_left.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.showprice)));
		holder.tv_right.setText("￥"+MoneyUtils.formatAmountAsString(new BigDecimal(model.showprice)));
		ImageShowUtil.showImage(model.fristimg,holder.imageView);
		return convertView;
	}

	public static class ViewHolder{
		public TextView tv_left,tv_pro_name,tv_right;
		public ImageView imageView;
	}

}
