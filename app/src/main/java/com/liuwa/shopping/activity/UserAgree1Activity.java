package com.liuwa.shopping.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;


public class UserAgree1Activity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private WebView webView;
	public String url;
	public String title;
	public TextView tv_title;
	public TextView tv_show;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_agress_1layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("用户隐私");
		tv_show=(TextView)findViewById(R.id.tv_show);
	}

	//Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);

	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				UserAgree1Activity.this.finish();
				break;
			}
		}
	};
}
