package com.liuwa.shopping.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuwa.shopping.R;

public class LoadingTimeDialog extends Dialog {
	private TextView tv;

	public LoadingTimeDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}

	private LoadingTimeDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		tv = (TextView)this.findViewById(R.id.tv);
		tv.setText("正在抢购中...");
		LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
		linearLayout.getBackground().setAlpha(210);
	}
}
