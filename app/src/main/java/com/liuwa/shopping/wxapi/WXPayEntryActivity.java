package com.liuwa.shopping.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.MyMoneyActivity;
import com.liuwa.shopping.activity.PaySuccessActivity;
import com.liuwa.shopping.activity.PayTypeActivity;
import com.liuwa.shopping.client.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private static String orderid;
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			PayResp rep = (PayResp) resp;
			String jsonStr = rep.extData;
			try {
				JSONObject oo=new JSONObject(jsonStr);
				String key=oo.getString("key");
				switch (key){
					case MyMoneyActivity.CHONGZHI:
						if(resp.errCode==0)
						{
							Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
							WXPayEntryActivity.this.finish();
						}
						else
						{
							Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
							WXPayEntryActivity.this.finish();
						}
						break;
					case PayTypeActivity.PAYORDER:
						if(resp.errCode==0)
						{
							Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(WXPayEntryActivity.this,PaySuccessActivity.class);
							intent.putExtra("order_id", oo.getString("order_id"));
							startActivity(intent);
							WXPayEntryActivity.this.finish();
						}
						else
						{
							Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
							WXPayEntryActivity.this.finish();
						}
						break;
				}
			}catch (Exception e){

			}
		}
	}
}