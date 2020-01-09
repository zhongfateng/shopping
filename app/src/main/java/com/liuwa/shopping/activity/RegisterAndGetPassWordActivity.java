package com.liuwa.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liuwa.shopping.R;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.UserModel;
import com.liuwa.shopping.util.MD5;
import com.liuwa.shopping.util.Md5SecurityUtil;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterAndGetPassWordActivity extends BaseActivity {
	private Context context;
	private TimeCount time;
	private EditText  et_register_input_phone,et_register_input_code,et_register_input_pass;
	private TextView tv_title,tv_register_getcode;
	private Button btn_register_commit;
	//输入的手机号码
	private String intput_phone_num="";
	private String input_code_num="";
	private String input_password="";
	private int tag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_layout);
		this.context=this;
		tag=getIntent().getIntExtra("tag",LoginActivity.CodeRegister);
		initViews();
		initEvent();
	}

	public void initViews()
	{
		time = new TimeCount(90000, 1000);//构造CountDownTimer对象
		tv_title=(TextView)findViewById(R.id.tv_title);
		et_register_input_phone=(EditText) findViewById(R.id.et_register_input_phone);
		et_register_input_code=(EditText) findViewById(R.id.et_register_input_code);
		et_register_input_pass=(EditText)findViewById(R.id.et_register_input_pass);
		tv_register_getcode=(TextView) findViewById(R.id.tv_register_getcode);
		btn_register_commit=(Button) findViewById(R.id.btn_register_commit);
		if(tag==LoginActivity.CodeRegister){
			tv_title.setText("手机号注册");
			btn_register_commit.setText("注册");
		}else if(tag==LoginActivity.CodeForget){
			tv_title.setText("找回密码");
			btn_register_commit.setText("确定");
		}
	}
	
	public void initEvent(){
		tv_register_getcode.setOnClickListener(onClickListener);
		btn_register_commit.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_register_commit:
				onRegister();
				break;
			/**获取验证码**/
			case R.id.tv_register_getcode:
				getCode();
				break;
			}
		}
	};
	
	
	
	
	//校验输入的手机号和验证码以及密码
	private Boolean checkValue(String phone,String code,String pass){
		 if(phone.length() == 0||!isMobileNum(phone)){
			this.showToast("请输入正确手机号码");
			return false;
		}
		else if(code.length()==0)
		{
			this.showToast("请输入验证码");
			return false;
		}else if(pass.length()==0){
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

	 	private void  getCode(){
	 			intput_phone_num = et_register_input_phone.getText().toString();
				if(intput_phone_num.length() == 0||!isMobileNum(intput_phone_num)){
					this.showToast("请输入正确手机号码");
					return;
				}
				else
				{
				if(ApplicationEnvironment.getInstance().checkNetworkAvailable())
					{
						TreeMap<String, Object> map = new TreeMap<String, Object>();
						map.put("tel", intput_phone_num);
						map.put("timespan",System.currentTimeMillis()+"");
						map.put("sign",Md5SecurityUtil.getSignature(map));
						HashMap<String, Object> mapend2 = new HashMap<String, Object>();
						mapend2.put(Constants.kMETHODNAME,Constants.GETVDCODE);
						mapend2.put(Constants.kPARAMNAME, map);
						LKHttpRequest req1 = new LKHttpRequest(mapend2, getCodeHandler());
						new LKHttpRequestQueue().addHttpRequest(req1)
						.executeQueue("请稍后", new LKHttpRequestQueueDone(){
							@Override
							public void onComplete() {
								super.onComplete();
							}
						});	
						time.start();
					}

				}
		}
		private LKAsyncHttpResponseHandler getCodeHandler(){
			 return new LKAsyncHttpResponseHandler(){
				@Override
				public void successAction(Object obj) {
				String str=(String)obj;
				try {
					JSONObject object=new JSONObject(str);
					int  code=  object.getInt("code");
					if(code==Constants.CODE) {
						Toast.makeText(context, "验证码已发送！注意查收", Toast.LENGTH_SHORT).show();
					}
					else if(code==200){
						Toast.makeText(context,object.getString("msg"),Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			};
		}
	private void onRegister(){
		intput_phone_num =  et_register_input_phone.getText().toString();
		input_code_num   =  et_register_input_code.getText().toString();
		input_password   =  et_register_input_pass.getText().toString();
			if(checkValue(intput_phone_num,input_code_num,input_password))
			{
				TreeMap<String, Object> map = new TreeMap<String, Object>();
				map.put("tel", intput_phone_num);
				map.put("code", input_code_num);
				map.put("password", MD5.GetMD5Code(input_password));
				map.put("timespan",System.currentTimeMillis()+"");
				map.put("sign",Md5SecurityUtil.getSignature(map));
				HashMap<String, Object> mapend2 = new HashMap<String, Object>();
				if(tag==LoginActivity.CodeRegister) {
					mapend2.put(Constants.kMETHODNAME, Constants.REGISTER);
				}else if(tag==LoginActivity.CodeForget){
					mapend2.put(Constants.kMETHODNAME, Constants.XGCode);
				}
				mapend2.put(Constants.kPARAMNAME, map);
				LKHttpRequest req1 = new LKHttpRequest(mapend2, registerHandler());
				new LKHttpRequestQueue().addHttpRequest(req1)
				.executeQueue("正在注册请稍候...", new LKHttpRequestQueueDone(){

					@Override
					public void onComplete() {
						super.onComplete();
					}
				});	
			}
		
	}
	private LKAsyncHttpResponseHandler registerHandler(){
		return new LKAsyncHttpResponseHandler(){
			
			@Override
			public void successAction(Object obj) {
				String str=(String)obj;
				try {
					JSONObject object=new JSONObject(str);
					int  code=  object.getInt("code");
					if(code==Constants.CODE) {
						JSONObject oo=object.getJSONObject("data");
						String token = oo.getString("token");
						Gson localGson = new GsonBuilder().disableHtmlEscaping()
								.create();
						UserModel userModel = localGson.fromJson(oo.getJSONObject("member").toString(),UserModel.class);
						SharedPreferences.Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
						editor.putString(Constants.TOKEN, token);
						editor.putString(Constants.USER,localGson.toJson(userModel));
						editor.putString(Constants.flag,"");
						editor.putString(Constants.USER_PHONE,intput_phone_num);
						editor.putString(Constants.USER_PASS,input_password);
						boolean flag =editor.commit();
						if(flag==true) {
							if(tag==LoginActivity.CodeRegister){
								Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
							}else if(tag==LoginActivity.CodeForget){
								Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
							}
						}
						RegisterAndGetPassWordActivity.this.finish();
					}
					else if(code==200){
						Toast.makeText(context,object.getString("msg"),Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			tv_register_getcode.setText("重新验证");
			tv_register_getcode.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示

			tv_register_getcode.setClickable(false);
			tv_register_getcode.setText(millisUntilFinished /1000+"秒");
		}
	}
}
