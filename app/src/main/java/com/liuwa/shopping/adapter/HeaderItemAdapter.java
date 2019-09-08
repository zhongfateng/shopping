package com.liuwa.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.model.HeaderModel;
import com.liuwa.shopping.view.CircleImageView;

import java.util.ArrayList;

public class HeaderItemAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<HeaderModel> headerModels;
	//public int foodpoition;
	OnClickListener  onClickListener;

	public HeaderItemAdapter(Context context, ArrayList<HeaderModel> headerModels) {
		this.context = context;
		this.headerModels = headerModels;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return headerModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return headerModels.get(position);
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
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_agress = (TextView) convertView
					.findViewById(R.id.tv_agress);
			viewHolder.tv_delete = (TextView) convertView
					.findViewById(R.id.tv_delete);
			viewHolder.circle_left=(CircleImageView)convertView.findViewById(R.id.circle_left);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		HeaderModel  model= headerModels.get(position);
		viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.onDelete();
			}
		});
		viewHolder.tv_agress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.onAgress();
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public TextView tv_name,tv_time,tv_agress,tv_delete;
		public CircleImageView circle_left;
	}
	public interface  OnClickListener{
		public void onDelete();
		public void onAgress();
	}
}
