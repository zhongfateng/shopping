package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.util.MD5;
import com.liuwa.shopping.util.Md5SecurityUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends BaseActivity {
	private Context context;
	private EditText  et_login_input_phone,et_login_input_pass;
	private Button btn_login_commit;
	private TextView tv_btn_register,tv_btn_forget;
	//输入的手机号码
	private String intput_phone_num="";
	private String input_password="";
	//0表示注册，1,表示忘记密码
	public static  final  int CodeRegister = 0;
	public static  final  int CodeForget   = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		et_login_input_phone=(EditText) findViewById(R.id.et_login_input_phone);
		et_login_input_pass=(EditText) findViewById(R.id.et_login_input_pass);
		btn_login_commit=(Button) findViewById(R.id.btn_login_commit);
		tv_btn_register=(TextView)findViewById(R.id.tv_btn_register);
		tv_btn_forget=(TextView)findViewById(R.id.tv_btn_forget);
	}
	
	public void initEvent(){
		btn_login_commit.setOnClickListener(onClickListener);
		tv_btn_register.setOnClickListener(onClickListener);
		tv_btn_forget.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.btn_login_commit:
					onLogin();
					break;
				case R.id.tv_btn_register:
					intent=new Intent(context,RegisterAndGetPassWordActivity.class);
					intent.putExtra("tag",CodeRegister);
					startActivity(intent);
				break;
					case  R.id.tv_btn_forget:
					intent=new Intent(context,RegisterAndGetPassWordActivity.class);
					intent.putExtra("tag",CodeForget);
					startActivity(intent);
				break;
			}
		}
	};
	

	//校验输入的手机号和验证码以及密码
	private Boolean checkValue(String phone,String pass){
		 if(phone.length() == 0||!isMobileNum(phone)){
			this.showToast("请输入正确手机号码");
			return false;
		}
		else if(pass.length()==0){
			 this.showToast("请填写密码");
			 return false;
		 }
		return true;
	}
	
	
	public boolean isMobileNum(String num){
		 String str = "^((1[0-9][0-9]))\\d{8}$";
		 Pattern p = Pattern.compile(str);
		 Matcher m = p.matcher(num);
		 return m.matches();
	}

	private LKAsyncHttpResponseHandler getCodeHandler(){
		 return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
			String str=(String)obj;
			try {
				JSONObject object=new JSONObject(str);
				String flag=object.getString("code");
				Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		};
	}
	private void onLogin(){
		intput_phone_num =  et_login_input_phone.getText().toString();
		input_password   =  et_login_input_pass.getText().toString();
			if(checkValue(intput_phone_num,input_password)) {
				TreeMap<String, Object> map = new TreeMap<String, Object>();
				map.put("tel", intput_phone_num);
				map.put("password", MD5.GetMD5Code(input_password));
				map.put("timespan",System.currentTimeMillis()+"");
				map.put("sign", Md5SecurityUtil.getSignature(map));
				HashMap<String, Object> mapend2 = new HashMap<String, Object>();
				mapend2.put(Constants.kMETHODNAME,Constants.LOGIN);
				mapend2.put(Constants.kPARAMNAME, map);
				LKHttpRequest req1 = new LKHttpRequest(mapend2, loginHandler());
				new LKHttpRequestQueue().addHttpRequest(req1)
				.executeQueue("正在注册请稍候...", new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}
				});	
			}
		
	}
	private LKAsyncHttpResponseHandler loginHandler(){
		return new LKAsyncHttpResponseHandler(){
			
			@Override
			public void successAction(Object obj) {
				String str=(String)obj;
				try {
					JSONObject object=new JSONObject(str);
					int  code=  object.getInt("code");
					if(code==Constants.CODE) {

					}
					else {
						Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

}
