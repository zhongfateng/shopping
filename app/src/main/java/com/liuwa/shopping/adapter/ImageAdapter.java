package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.MoneyModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.view.CircleImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<String> strings;
	public ImageAdapter(Context context, ArrayList<String> strings){
		this.context = context;
		this.strings = strings;
		inflater= LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strings.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return strings.get(position);
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
	    convertView = inflater.inflate(R.layout.circle_item_layout, null);
	    holder = new ViewHolder();
	    holder.image=(CircleImageView)convertView.findViewById(R.id.circle_image);
        convertView.setTag(holder);
	    }
	    else{
	    holder=(ViewHolder)convertView.getTag();
	    }

		String model= strings.get(position);
		ImageShowUtil.showImage(model,holder.image);
		return convertView;
	}

	public static class ViewHolder{
		public CircleImageView image;
	}

}
