package com.liuwa.shopping.util;


import com.liuwa.shopping.client.Constants;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import static java.net.URLEncoder.encode;


public class Md5SecurityUtil {

	
	/** 签名参数名 */
	public final static String OAUTH_SIGNATURE = "sign";
	/** 时间戳参数名 */
	public final static String OAUTH_TIMESTAMP = "timespan";
	
	/**
	 * 生成md5 签名
	 * @param nameValuePairs
	 * @return
	 * @throws Exception
	 */
	public static String getSignatureValue(List<NameValuePair> nameValuePairs) throws Exception{
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		for (NameValuePair pair : nameValuePairs) {
			parameterMap.put(pair.name, pair.value.toString());
		}
		
		ArrayList<NameValuePair> currentPairs = new ArrayList<NameValuePair>(nameValuePairs);
		
		String timestamp = System.currentTimeMillis() + "";
		
		if (!parameterMap.containsKey(OAUTH_TIMESTAMP)){
			currentPairs.add(new NameValuePair(OAUTH_TIMESTAMP, timestamp));
		}
		String signatureBaseString = getSignatureBaseString(nameValuePairs);
	    String signatureValue = getSignatureValue(signatureBaseString);
	    
		return signatureValue;
	}
	
	public static String getSignatureValueMap(TreeMap<String, Object> map){
		
		List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		
		
		 for (String key : map.keySet()) {
			 
			 NameValuePair pair=new NameValuePair(key, map.get(key));
			 nameValuePairs.add(pair);
			 
			  }
		
		
//		Map<String, String> parameterMap = new HashMap<String, String>();
//		for (NameValuePair pair : nameValuePairs) {
//			parameterMap.put(pair.name, pair.value.toString());
//		}
//		
//		ArrayList<NameValuePair> currentPairs = new ArrayList<NameValuePair>(nameValuePairs);
//		
//		String timestamp = System.currentTimeMillis() + "";
//		
//		if (!parameterMap.containsKey(OAUTH_TIMESTAMP)){
//			currentPairs.add(new NameValuePair(OAUTH_TIMESTAMP, timestamp));
//		}
		String signatureBaseString;
		String signatureValue = null;
		try {
			signatureBaseString = getSignatureBaseString(nameValuePairs);
			signatureValue = getSignatureValue(signatureBaseString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return signatureValue;
	}

	public static String getSignature(TreeMap<String, Object> map){

		List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		for (String key : map.keySet()) {
				NameValuePair pair = new NameValuePair(key, map.get(key));
				nameValuePairs.add(pair);
		}
		String signatureValue=null;
		String signatureBaseValue;
		try {
			signatureBaseValue = buildUrl(nameValuePairs);
			StringBuffer sb=new StringBuffer(signatureBaseValue);
			sb.append("&");
			sb.append(Constants.keySecret);
			sb.append("=");
			sb.append(Constants.VALUE);
			signatureBaseValue=sb.toString();
			signatureValue=MD5.GetMD5Code(signatureBaseValue,false);
		}catch (Exception e){
			e.printStackTrace();
		}
		return  signatureValue;
	}
	
	
	
	/**
	 * 组装请求的url(多用于测试)
	 * @param nameValuePairs 除oauth_signature 外的参数值对集合
	 * @return
	 * @throws Exception
	 */
	public static String buildUrl(List<NameValuePair> nameValuePairs) throws Exception {

		//将除oauth_signature 外的参数集合按照字母升序排列
		Collections.sort(nameValuePairs, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair o1, NameValuePair o2) {
				return o1.name.compareTo(o2.name);
			}
		});


		StringBuilder sb = new StringBuilder(40);
		for (int i=0;i< nameValuePairs.size();i++) {
			NameValuePair pair = nameValuePairs.get(i);
			if(i >0)
				sb.append("&");
			sb.append(pair.name).append("=").append(pair.value);
		}
		String signatureBaseString = sb.toString();

		return signatureBaseString;
	}
	/**
	 * 获取签名
	 * 获取签名基础串 -->> urlencode(a=x&b=y&...)
	 * @param nameValuePairs 除oauth_signature 外的参数值对集合
	 * @return
	 * @throws Exception
	 */
	private static String getSignatureBaseString(List<NameValuePair> nameValuePairs) throws Exception {
		
		//将除oauth_signature 外的参数集合按照字母升序排列
		Collections.sort(nameValuePairs, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair o1, NameValuePair o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		
		
		StringBuilder sb = new StringBuilder(40);
		for (int i=0;i< nameValuePairs.size();i++) {
			NameValuePair pair = nameValuePairs.get(i);
			if(i >0)
				sb.append("%26");
			sb.append(encode(pair.name, "utf-8")).append("%3D").append(encode(pair.value.toString(), "utf-8"));
		}
		String signatureBaseString = sb.toString();

		return signatureBaseString;
	}
	
private static String getSignatureBaseStringMap(HashMap<String, Object> map) throws Exception {
		
	
	  Map<String, Object> treeMap = new TreeMap<String, Object>(map);

	
	
		//将除oauth_signature 外的参数集合按照字母升序排列
//		Collections.sort(nameValuePairs, new Comparator<NameValuePair>() {
//			@Override
//			public int compare(NameValuePair o1, NameValuePair o2) {
//				return o1.name.compareTo(o2.name);
//			}
//		});
//		
//		
//		StringBuilder sb = new StringBuilder(40);
//		for (int i=0;i< nameValuePairs.size();i++) {
//			NameValuePair pair = nameValuePairs.get(i);
//			if(i >0)
//				sb.append("%26");
//			sb.append(encode(pair.name, "utf-8")).append("%3D").append(encode(pair.value, "utf-8"));
//		}
//		String signatureBaseString = sb.toString();
//
//		return signatureBaseString;
	  
	  StringBuilder sb = new StringBuilder(40);
	  
	  Set<Entry<String, Object>> entrySet = treeMap.entrySet();
	  int i=0;
	  for (Entry<String, Object> entry : entrySet) {
		 
		 if(i>0)
			 sb.append("%26");
		 
		 sb.append(encode(entry.getKey(), "utf-8")).append("%3D").append(encode((String)entry.getValue(), "utf-8"));
		 i++;
	  }
		String signatureBaseString = sb.toString();

		return signatureBaseString;
	  
	}
	
	
	/**
	 * 
	 * @param signatureBaseString 签名基础串
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private static String getSignatureValue(String signatureBaseString)
			throws NoSuchAlgorithmException, InvalidKeyException {
		
		String signatureValue = MD5.GetMD5Code(signatureBaseString, true);
		
		return signatureValue;
	}
	
	private static String getSignatureValueMap(String signatureBaseString)
			throws NoSuchAlgorithmException, InvalidKeyException {
		
		String signatureValue = MD5.GetMD5Code(signatureBaseString, true);
		
		return signatureValue;
	}
	
	/**
	 * 签名验证
	 * @param request
	 * @return
	 */
//	public static boolean verifyOauthSignature(HttpServletRequest request) {
//		boolean ret = true;
//		
//		List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
//		
//	    
//	    Enumeration<String> names = request.getParameterNames();
//		while (names.hasMoreElements()) {
//			String name = names.nextElement();
//			requestParameters.add(new NameValuePair(name, request.getParameter(name)));
//		}
//		
//		List<NameValuePair> executeParameter = new ArrayList<NameValuePair>(requestParameters.size());
//		String signatureVlaue = "";
//		for (NameValuePair pair : requestParameters) {
//			if (OAUTH_SIGNATURE.equals(pair.name)) {
//				//提取sign
//				signatureVlaue = pair.value;
//			}
//			else {
//				executeParameter.add(pair);
//			}
//		}
//		
//		if (StringUtils.isBlank(signatureVlaue)) {
//			logger.info("签名验证为空");
//			return false;
//		}
//		
//		try{
//			String signatureBaseString = getSignatureBaseString( executeParameter);
//			String computationalSignatureValue = getSignatureValue(signatureBaseString);
//			if (!signatureVlaue.equals(computationalSignatureValue)) {
//				logger.debug("签名错误, 请求url:{}, 客户端签名{}, 验证签名{}", request.getRequestURL(), signatureVlaue, computationalSignatureValue);
//				return false;
//			}
//		}	catch(Exception e){
//			logger.error("签名验证异常", e);
//			ret = false;
//		}
//		return ret;
//	}
	
	
	public static void main(String[] args) throws Exception {
		List<NameValuePair> l = new ArrayList<NameValuePair>();
		l.add(new NameValuePair("uid", "123"));
		l.add(new NameValuePair("token", "fdsafdasfdsafd"));
		l.add(new NameValuePair("timestamp", "1459949515"));
		l.add(new NameValuePair("id", "1"));

		System.out.println(buildUrl(l));
		String s = getSignatureBaseString(l);
		System.out.println(s);
		System.out.println(getSignatureValue(l));

	}
	
	
}