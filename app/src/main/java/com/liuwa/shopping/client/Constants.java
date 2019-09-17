package com.liuwa.shopping.client;

public class Constants {
	
	// 要相应的更新string.xml中的 versionstr 字段
	// 当前系统的版本号
	public static final String VERSION					= "1.3.6";
	//微信key
	public static final String APP_ID                   = "wxdcd80da641faa3ae";
	public static final String DESCRIPTOR              = "com.liuwa.shopping";
	public static final String DEFAULTHOST             = "http://lw.xingyf.com/";
	public static final String USER_ID					= "user_id";
	public static final String USER					    = "user";
	public static final String kHOSTNAME 				= "hostName";
	public static final String kREALHOST				= "RealHost"; // 存储真正的连网地址
	public static final String kMETHODNAME 				= "methodName";
	public static final String kPARAMNAME 				= "paramName";
	public static final String TOKEN               		= "token";
	//用户反馈--xx
	public static final String content                  = "content";
	//用户手机号
	public static final String Phone                  	= "phone";
	//联系人
	public static final String CONNECTNUM               = "40089191919";
	//签名key值
	public static final String keySecret               	= "keySecret";
	//签名value值
	public static final String VALUE                    = "jklw";
	//信息返回CODE值 100表示正确
	public static final int CODE                        = 100;
	//信息返回CODE值 token生路失效表示正确
	public static final int TOKENCODE                  = 413 ;
	public static class Config {
		public static final boolean DEVELOPER_MODE    = false;
	}
	//接口 获取分类
	public static final String GETCATEGORY              = "proclassesjson";
	//接口 获取特别分类
	public static final String GETSPECIALCATEGORY      = "proclassestjson";
	//接口 首页团购数据
	public static final String GETTUANGOU               = "tuanlist";
	//接口 查询公告接口
	public static final String GETNOTICES               = "newsjson";
	//点击获取验证码
	public static final String GETVDCODE                = "zhucedx";
	//校验输入验证码接口地址
	public static final String VALIDATEMOBILECODE      = "regist/ValiateMobileCode.do";
	//特殊分类商品列表接口
	public static final String SPECIALPRODUCTLIST      = "productlisttjson";
	//分类商品列表
	public static final String PRODUCTLIST      		 = "productlistjson";
	//分类商品列表
	public static final String PRODUCTDETAIL      		 = "productinfojson";
	//登录接口
	public static final String LOGIN      				 =  "logindo";
	//注册接口
	public static final String REGISTER      			 =  "zhucedo";
	//3.5.2支付订单/api/stu/order/pay
	public static final String PayOrder                  = "api/stu2/order/pay";
	//邻居购买
	public static final String NEIBORBUY                 = "linjvbuyjson";
	//加入购物车
	public static final String ADDCART                   = "addcartjson";
	//购物车列表
	public static final String CARTLIST                  = "memcartjson";
	//立即购买
	public static final String BUY                        = "addlijiorder";
	//订单信息
	public static final String ORDERDETAIL               = "orderdetil";
	//获取收货地址
	public static final String GETADDRESS                = "getmemaddressjson";
	//市区域
	public static final String City                       = "shijson";
	//区区域
	public static final String QU                         = "qujson";
	//用户中心
	public static final String USERCENTER                = "memcenterjson";
	//订单数量
	public static final String ORDERCOUNT                = "memordercount";
	//地区
	public static final String Area                       = "indexsqjson";
	//添加地址
	public static final String ADDADDRESS                = "addmemaddressjson";
	//删除地址
	public static final String DELETEADDRESS             = "deladdressjson";
	//默认地址
	public static final String ISADDRESS                  = "mraddressjson";






}
