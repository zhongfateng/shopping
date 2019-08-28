package com.liuwa.shopping.client;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.liuwa.shopping.activity.IndexActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class LKHttpRequest {
	
	private int tag;
	private HashMap<String, Object> requestDataMap;
	private LKAsyncHttpResponseHandler responseHandler;
	private AsyncHttpClient client;
	private LKHttpRequestQueue queue;
	
	public LKHttpRequest(HashMap<String, Object> requestMap, LKAsyncHttpResponseHandler handler){
		this.requestDataMap = requestMap;
		this.responseHandler = handler;
		client = new AsyncHttpClient();
		
		if (null != this.responseHandler){
			this.responseHandler.setRequest(this);
		}
	}
	
	public String getRequestURL(){
		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String hostStr = pre.getString(Constants.kREALHOST, null);
		if (null == hostStr || hostStr.trim().equals("")){
			hostStr = pre.getString(Constants.kHOSTNAME, Constants.DEFAULTHOST);
		}
		
		System.out.println("URL:"+hostStr + requestDataMap.get(Constants.kMETHODNAME));
		return hostStr + requestDataMap.get(Constants.kMETHODNAME);
	}
	
	public int getTag(){
		return tag;
	}
	
	public void setTag(int tag){
		this.tag = tag;
	}
	
	public LKHttpRequestQueue getRequestQueue(){
		return this.queue;
	}
	
	public void setRequestQueue(LKHttpRequestQueue queue){
		this.queue = queue;
	}
	
	public HashMap<String, Object> getRequestDataMap() {
		return requestDataMap;
	}
	
	public LKAsyncHttpResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public AsyncHttpClient getClient() {
		return client;
	}
	
	
	/****************************************/
	
	public void post(){
		this.client.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
		//this.client.addHeader("charset", "UTF-8");

		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String token=pre.getString(Constants.TOKEN,"");
//		if(token.length()==0) {
//			Application app = ApplicationEnvironment.getInstance().getApplication();
//			Intent intent = new Intent(app, IndexActivity.class);
//			app.startActivity(intent);
//		}
//		else{
			this.client.addHeader(Constants.TOKEN, token);
			this.client.post(ApplicationEnvironment.getInstance().getApplication(), this.getRequestURL(), this.getRequestParams(this), this.responseHandler);
//		}
	}
	
	@SuppressWarnings("unchecked")
	private HttpEntity getHttpEntity(LKHttpRequest request){
		HashMap<String, Object> reqMap = request.getRequestDataMap();
		HashMap<String, Object> maps = (HashMap<String, Object>) reqMap.get(Constants.kPARAMNAME);
		
		HttpEntity entity = null;
		try {
			entity = new StringEntity(param2String(maps));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return entity;
	}
	
	private RequestParams getRequestParams(LKHttpRequest request){
		HashMap<String, Object> reqMap = request.getRequestDataMap();
		TreeMap<String, Object> maps = (TreeMap<String, Object>) reqMap.get(Constants.kPARAMNAME);
		
		RequestParams requestParams = new RequestParams();
		if(maps!=null)
		{
		for(Entry<String, Object> entry : maps.entrySet())
		{
			Object object=	entry.getValue();
			if(object  instanceof String)
			{
				
				String	sb =(String) object;
				requestParams.put(entry.getKey(), sb);
			}
			if(object instanceof Integer)
			{
				Integer ind=(Integer)object;
			String	sb =ind.toString();
				requestParams.put(entry.getKey(), sb);
				
			}
			if(object instanceof Float)
			{
				Float flo=(Float)object;
				String	sb=flo.toString();
				requestParams.put(entry.getKey(), sb);
				
			}
			if(object instanceof File)
			{
				
				File file=(File)object;
				
				try {
					requestParams.put(entry.getKey(), file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		}
		}
		return requestParams;
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	private String param2String(HashMap<String, Object> paramMap){
		
				/*for(Entry<String, Object> ss:paramMap.entrySet()){
					System.out.println(ss.getValue());
				}*/
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private String hashMap2XML(HashMap<String, Object> paramMap){
		StringBuffer sb = new StringBuffer();
		for	(String key : paramMap.keySet()){
			Object obj = paramMap.get(key);
			if (obj instanceof String){
				sb.append("<").append(key).append(">").append(obj).append("</").append(key).append(">");
			} else {
				sb.append("<").append(key).append(">").append(this.hashMap2XML((HashMap<String, Object>)obj)).append("</").append(key).append(">");
			}
		}
		return sb.toString();
	}
	
}
