package com.liuwa.shopping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.MoneyModel;

import java.util.ArrayList;

public class DepositAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<MoneyModel> moneyModels;
	int last_item;
	private int selectedPosition = -1;
	public DepositAdapter(Context context, ArrayList<MoneyModel> moneyModels){
		this.context = context;
		this.moneyModels = moneyModels;
		inflater= LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return moneyModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return moneyModels.get(position);
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
	    convertView = inflater.inflate(R.layout.activity_deposit_item_layout, null);
	    holder = new ViewHolder();
	    holder.ll=(LinearLayout)convertView.findViewById(R.id.ll_item);
        holder.tv_money =(TextView)convertView.findViewById(R.id.tv_money);
        holder.tv_jifen=(TextView)convertView.findViewById(R.id.tv_jifen);
        convertView.setTag(holder);
	    }
	    else{
	    holder=(ViewHolder)convertView.getTag();
	    }

		MoneyModel model=moneyModels.get(position);
	    holder.tv_money.setText("充 "+model.money+" 元");
		holder.tv_jifen.setText("送 "+model.jifen+" 积分");
		
	    
	    
	    // 设置选中效果    
	     if(selectedPosition == position)   
	    {   
			holder.ll.setBackgroundResource(R.drawable.shaper_corner_bg);
			holder.tv_money.setTextColor(context.getResources().getColor(R.color.tab_line));
			holder.tv_jifen.setTextColor(context.getResources().getColor(R.color.tab_line));
	   } else {
			 holder.ll.setBackgroundResource(R.drawable.shaper_corner_unselect_bg);
			 holder.tv_money.setTextColor(context.getResources().getColor(R.color.tab_color));
			 holder.tv_jifen.setTextColor(context.getResources().getColor(R.color.tab_color));
	    }   
		return convertView;
	}

	public static class ViewHolder{
		public TextView tv_money,tv_jifen;
		public LinearLayout ll;
	}

	public void setSelectedPosition(int position) {   
	   selectedPosition = position;   
	}   

}
