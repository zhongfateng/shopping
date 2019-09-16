package com.liuwa.shopping.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.fragment.DialogFragmentFromBottom;
import com.liuwa.shopping.activity.fragment.DialogFragmentShowArea;
import com.liuwa.shopping.client.ApplicationEnvironment;
import com.liuwa.shopping.client.Constants;
import com.liuwa.shopping.client.LKAsyncHttpResponseHandler;
import com.liuwa.shopping.client.LKHttpRequest;
import com.liuwa.shopping.client.LKHttpRequestQueue;
import com.liuwa.shopping.client.LKHttpRequestQueueDone;
import com.liuwa.shopping.permission.PermissionUtils;
import com.liuwa.shopping.permission.request.IRequestPermissions;
import com.liuwa.shopping.permission.request.RequestPermissions;
import com.liuwa.shopping.permission.requestresult.IRequestPermissionsResult;
import com.liuwa.shopping.permission.requestresult.RequestPermissionsResultSetApp;
import com.liuwa.shopping.util.Md5SecurityUtil;
import com.liuwa.shopping.util.uri.SystemProgramUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;


public class HeadApplyActivity extends BaseActivity implements DialogFragmentShowArea.OnFragmentInteractionListener{
	private Context context;
	private ImageView img_back;
	private TextView tv_title;
	private TextView tv_get_code;
	private TextView tv_tel_num;
	private Button btn_commit;
	private String address="";
	private String detail="";
	private String code="";
	private String name="";
	private TimeCount time;
	private TextView tv_address;
	private EditText et_detail;
	private EditText tv_yzm;
	private EditText et_name;
	private String phone;
	private TextView tv_area,tv_area_item;
	private String param="";
	private String childparam="";
	private String tag="0";
	IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
	IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
	public static final int REQUEST_CODE_DINGWEI = 3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_applay_layout);
		this.context=this;
		initViews();
		initEvent();
	}

	public void initViews() {
		time = new TimeCount(9000, 1000);//构造CountDownTimer对象
		img_back=(ImageView)findViewById(R.id.img_back);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_title.setText("团长申请");
		btn_commit=(Button)findViewById(R.id.btn_commit);
		tv_tel_num=(TextView)findViewById(R.id.tv_tel_num);
		tv_address=(TextView)findViewById(R.id.tv_address);
		et_detail=(EditText)findViewById(R.id.et_detail);
		tv_yzm=(EditText)findViewById(R.id.et_code);
		tv_get_code=(TextView) findViewById(R.id.tv_get_code);
		et_name=(EditText)findViewById(R.id.et_name);
		tv_area=(TextView)findViewById(R.id.tv_area);
		tv_area_item=(TextView)findViewById(R.id.tv_area_item);
		phone= ApplicationEnvironment.getInstance().getPreferences().getString(Constants.Phone, "");
		tv_tel_num.setText(phone);
	}
	
	public void initEvent(){
		img_back.setOnClickListener(onClickListener);
		tv_address.setOnClickListener(onClickListener);
		tv_get_code.setOnClickListener(onClickListener);
		btn_commit.setOnClickListener(onClickListener);
		tv_area.setOnClickListener(onClickListener);
		tv_area_item.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		Intent intent;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				HeadApplyActivity.this.finish();
				break;
			case R.id.tv_get_code:
				getCode();
				break;
			case R.id.tv_address:
				if(!requestPermissions()){
					return;
				}
				intent =new Intent(context,NeiborActivity.class);
				startActivityForResult(intent,REQUEST_CODE_DINGWEI);
				break;
			case R.id.tv_area:
				tag="0";
				param="";
				tv_area.setText("");
				tv_area_item.setText("");
				childparam="";
				DialogFragmentFromBottom();
				break;
			case R.id.tv_area_item:
				if(param.length()==0){
					Toast.makeText(context,"请选择城市",Toast.LENGTH_SHORT).show();
					return;
				}
				tag="1";
				DialogFragmentFromBottom();
				break;
			case R.id.btn_commit:
				address=tv_address.getText().toString();
				detail=et_detail.getText().toString();
				code=tv_yzm.getText().toString();
				name=et_name.getText().toString();
				if(!checkValue(address,detail,code,name)){
					return;
				}
				commit();
				break;
			}
		}
	};
	//请求权限
	private boolean requestPermissions(){
		//需要请求的权限
		/**
		 * Manifest.permission.ACCESS_COARSE_LOCATION,
		 Manifest.permission.ACCESS_FINE_LOCATION,
		 Manifest.permission.WRITE_EXTERNAL_STORAGE,
		 Manifest.permission.READ_EXTERNAL_STORAGE,
		 Manifest.permission.READ_PHONE_STATE
		 * **/
		String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.READ_PHONE_STATE};
		//开始请求权限
		return requestPermissions.requestPermissions(
				this,
				permissions,
				PermissionUtils.ResultCode1);
	}

	//用户授权操作结果（可能授权了，也可能未授权）
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		//用户给APP授权的结果
		//判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
		if(requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)){
			//请求的权限全部授权成功，此处可以做自己想做的事了
			//输出授权结果
			Toast.makeText(context,"授权成功，请重新点击刚才的操作！",Toast.LENGTH_LONG).show();
		}else{
			//输出授权结果
			Toast.makeText(context,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
		}
	}
	private void DialogFragmentFromBottom() {
		showDialog();
	}
	void showDialog() {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragmentShowArea	newFragment = DialogFragmentShowArea.newInstance(param);
		newFragment.show(ft, "dialog");
	}

	private void commit(){
//		TreeMap<String, Object> productParam = new TreeMap<String, Object>();
//		productParam.put("start",page);
//		productParam.put("rows",pageSize);
//		productParam.put("classesid","1");
//		productParam.put("type",1);
//		productParam.put("timespan", System.currentTimeMillis()+"");
//		productParam.put("sign", Md5SecurityUtil.getSignature(productParam));
//		HashMap<String, Object> requestCategoryMap = new HashMap<String, Object>();
//		requestCategoryMap.put(Constants.kMETHODNAME,Constants.PRODUCTLIST);
//		requestCategoryMap.put(Constants.kPARAMNAME, productParam);
//		LKHttpRequest categoryReq = new LKHttpRequest(requestCategoryMap, getProductHandler());
//		new LKHttpRequestQueue().addHttpRequest(categoryReq)
//				.executeQueue(null, new LKHttpRequestQueueDone(){
//
//					@Override
//					public void onComplete() {
//						super.onComplete();
//					}
//
//				});
	}
	private LKAsyncHttpResponseHandler getProductHandler(){
		return new LKAsyncHttpResponseHandler(){

			@Override
			public void successAction(Object obj) {
				String json=(String)obj;
				try {
					JSONObject  job= new JSONObject(json);
					int code =	job.getInt("code");
					if(code==Constants.CODE)
					{
//						JSONObject jsonObject = job.getJSONObject("data");
//						Gson localGson = new GsonBuilder().disableHtmlEscaping()
//								.create();
//						baseModel = localGson.fromJson(jsonObject.toString(),
//								new TypeToken<BaseDataModel<ProductModel>>() {
//								}.getType());
//						proList.addAll(baseModel.list);
//						fpAdapter.notifyDataSetChanged();

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
	private void  getCode(){
			TreeMap<String, Object> map = new TreeMap<String, Object>();
			map.put("tel", phone);
			map.put("timespan",System.currentTimeMillis()+"");
			map.put("sign", Md5SecurityUtil.getSignature(map));
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
	//校验输入的值
	private Boolean checkValue(String addres,String detail,String code,String name){
		if(addres.length()==0){
			this.showToast("请选择地区");
			return false;
		}else if(detail.length()==0){
			this.showToast("请填写小区");
			return false;
		}else if(code.length()==0){
			this.showToast("请输入验证码");
			return false;
		}else if(name.length()==0){
			this.showToast("请输入姓名");
			return false;
		}
		return true;
	}

	@Override
	public void onFragmentInteraction(String id, String areaname) {
		if(tag.equals("0")){
			tv_area.setText(areaname);
			param=id;
			childparam="";
			tv_area_item.setText("");
		}else if(tag.equals("1")){
			tv_area_item.setText(areaname);
			childparam=id;
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			tv_get_code.setText("重新验证");
			tv_get_code.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示

			tv_get_code.setClickable(false);
			tv_get_code.setText(millisUntilFinished /1000+"秒");
		}
	}
}
