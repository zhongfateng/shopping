package com.liuwa.shopping.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.WebFragment;


public class WebActivity extends BaseActivity{
	private Context context;
	private ImageView img_back;
	private WebView webView;
	public String url;
	public String title;
	public TextView tv_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_web_layout);
		this.context=this;
		url=getIntent().getStringExtra("url");
		title=getIntent().getStringExtra("title");
		initViews();
		initEvent();
	}

	public void initViews()
	{
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText(title);
		webView= (WebView)findViewById(R.id.web);
		WebSettings webSettings = webView.getSettings();
		//设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		//加载需要显示的网页
		//设置Web视图
		webView.setWebViewClient(new webViewClient());
		webView.loadUrl(url);
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
				WebActivity.this.finish();
				break;
			}
		}
	};
}
