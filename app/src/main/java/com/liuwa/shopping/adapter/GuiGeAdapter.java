package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.MoneyModel;
import com.liuwa.shopping.model.ProductChildModel;

import java.util.ArrayList;

public class GuiGeAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<ProductChildModel> productChildModels;
	int last_item;
	private int selectedPosition = -1;
	public GuiGeAdapter(Context context, ArrayList<ProductChildModel> productChildModels){
		this.context = context;
		this.productChildModels = productChildModels;
		inflater= LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productChildModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productChildModels.get(position);
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
	    convertView = inflater.inflate(R.layout.activity_item_layout, null);
	    holder = new ViewHolder();
        holder.tv_name =(TextView)convertView.findViewById(R.id.tv_name);
        convertView.setTag(holder);
	    }
	    else{
	    holder=(ViewHolder)convertView.getTag();
	    }

		ProductChildModel model= productChildModels.get(position);
	    holder.tv_name.setText(model.guiGe+"");

	    
	    
	    // 设置选中效果    
	     if(selectedPosition == position)   
	    {   
			holder.tv_name.setBackgroundResource(R.drawable.prochild_item_select_bg);
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.name_color));
	   } else {
			 holder.tv_name.setBackgroundResource(R.drawable.prochild_item_bg);
			 holder.tv_name.setTextColor(context.getResources().getColor(R.color.detail_address_color));
	    }   
		return convertView;
	}

	public static class ViewHolder{
		public TextView tv_name;
	}

	public void setSelectedPosition(int position) {   
	   selectedPosition = position;   
	}   

}
