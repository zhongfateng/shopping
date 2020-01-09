package com.liuwa.shopping.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.DialogFragmentShowArea;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.model.AddressModel;
import com.liuwa.shopping.util.Md5SecurityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateAddressActivity extends BaseActivity implements DialogFragmentShowArea.OnFragmentInteractionListener{
	private Context context;
	private CheckBox  ck_detail_address;
	//0表示未设置默认，1表示设置默认
	private int flag;
	private ImageView img_back;
	private TextView tv_title;
	private EditText et_input_name,et_phone;
	private LinearLayout ll_shi,rl_qu;
	private TextView tv_qu,tv_shi;
	private EditText et_detail;
	private TextView tv_ok,tv_cancel;
	private String input_name;
	private String input_phone;
	private String detail;
	private String param="";
	private String childparam="";
	private String tag="0";
	public AddressModel model;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address_layout);
		this.context=this;
		model=(AddressModel) getIntent().getSerializableExtra("areamodel");
		initViews();
		initEvent();
		initData();
	}

	public void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("修改地址");
		et_input_name=(EditText)findViewById(R.id.et_input_name);
		et_phone=(EditText)findViewById(R.id.et_phone);
		ll_shi=(LinearLayout)findViewById(R.id.ll_shi);
		rl_qu=(LinearLayout)findViewById(R.id.rl_qu);
		tv_shi=(TextView) findViewById(R.id.tv_shi);
		ck_detail_address=(CheckBox) findViewById(R.id.ck_detail_address);
		tv_qu=(TextView) findViewById(R.id.tv_qu);
		et_detail=(EditText)findViewById(R.id.et_detail);
		tv_ok=(TextView)findViewById(R.id.tv_ok);
		tv_cancel=(TextView)findViewById(R.id.tv_cancel);
	}
	public void initData()
	{
		et_input_name.setText(model.lxRen);
		et_phone.setText(model.lxTel);
		tv_shi.setText(model.shiname);
		tv_qu.setText(model.quname);
		et_detail.setText(model.detail);
		if(model.isUsed.equals("1")){
			ck_detail_address.setChecked(true);
		}else{
			ck_detail_address.setChecked(false);
		}
		param=model.city;
		childparam=model.area;
	}
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		ll_shi.setOnClickListener(onClickListener);
		rl_qu.setOnClickListener(onClickListener);
		tv_cancel.setOnClickListener(onClickListener);
		tv_ok.setOnClickListener(onClickListener);
		ck_detail_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					flag=1;
				}else {
					flag=0;
				}
			}
		});
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				UpdateAddressActivity.this.finish();
				break;
			case R.id.tv_ok:
				doGetDatas();
				break;
			case R.id.tv_cancel:
				UpdateAddressActivity.this.finish();
				break;
			case R.id.ll_shi:
				tag="0";
				param="";
				tv_shi.setText("");
				tv_qu.setText("");
				childparam="";
				DialogFragmentFromBottom();
				break;
			case R.id.rl_qu:
				if(param.length()==0){
					Toast.makeText(context,"请选择城市",Toast.LENGTH_SHORT).show();
					return;
				}
				tag="1";
				DialogFragmentFromBottom();
				break;

			}
		}
	};


	private void doGetDatas(){
		input_name =  et_input_name.getText().toString();
		input_phone   =  et_phone.getText().toString();
		detail=et_detail.getText().toString();
		if(checkValue(input_name,input_phone,param,childparam,detail)) {
			TreeMap<String, Object> map = new TreeMap<String, Object>();
			map.put("province", "0");
			map.put("city", param);
			map.put("addressId", model.addressId);
			map.put("area", childparam);
			map.put("detail", detail);
			map.put("lxRen", input_name);
			map.put("lxTel", input_phone);
			map.put("moren",tag);
			map.put("timespan",System.currentTimeMillis()+"");
			map.put("sign", Md5SecurityUtil.getSignature(map));
			HashMap<String, Object> mapend2 = new HashMap<String, Object>();
			mapend2.put(Constants.kMETHODNAME,Constants.UPDATEADDADDRESS);
			mapend2.put(Constants.kPARAMNAME, map);
			LKHttpRequest req1 = new LKHttpRequest(mapend2, getProductHandler());
			new LKHttpRequestQueue().addHttpRequest(req1)
					.executeQueue("正在请稍候...", new LKHttpRequestQueueDone(){

						@Override
						public void onComplete() {
							super.onComplete();
						}
					});
		}
	}
	private LKAsyncHttpResponseHandler getProductHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE) {
						UpdateAddressActivity.this.finish();
					}
					else
					{
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}
	//校验输入的手机号和验证码以及密码
	private Boolean checkValue(String input_name,String phone,String param,String childparam,String detail){
		if(phone.length() == 0||!isMobileNum(phone)){
			this.showToast("请输入正确手机号码");
			return false;
		}
		else if(input_name==null||input_name.length()==0){
			this.showToast("请填写收货人");
			return false;
		}else if(param.length()==0){
			this.showToast("请选择城市");
			return false;
		}else if(childparam.length()==0){
			this.showToast("请选择区域");
			return false;
		}else if(detail==null||detail.length()==0){
			this.showToast("请填写详情地址");
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
	private void DialogFragmentFromBottom() {
		showDialog();
	}
	void showDialog() {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragmentShowArea newFragment = DialogFragmentShowArea.newInstance(param);
		newFragment.show(ft, "dialog");
	}

	@Override
	public void onFragmentInteraction(String id, String areaname) {
		if(tag.equals("0")){
			tv_shi.setText(areaname);
			param=id;
			childparam="";
			tv_qu.setText("");
		}else if(tag.equals("1")){
			tv_qu.setText(areaname);
			childparam=id;
		}
	}
}
