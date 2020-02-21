package com.liuwa.shopping.client;

public class Constants {
	//1 团购 3秒杀 5积分 猜你喜欢 7 热销 9
	// 要相应的更新string.xml中的 versionstr 字段
	// 当前系统的版本号
	public static final String VERSION					= "1.8";
	public static final String FavoriteType				= "7";
	public static final String IntegralType				= "5";
	public static final String RXType				    = "9";
	public static final String TGType				    = "1";
	public static final String MXType				    = "3";
	//微信key
	public static final String APP_ID                   = "wx32fce07e42cb9a50";
	public static final String DESCRIPTOR              = "com.liuwa.shopping";
//	public static final String IMAGEHOSTPRE             = "http://ht.xingyf.com/upload/";
//	public static final String DEFAULTHOST             = "http://lw.xingyf.com/";
	public static final String DEFAULTHOST             = "http://mslx6ap.xinliushangmao.com/";
	public static final String IMAGEHOSTPRE             = "http://ygwl9ht.xinliushangmao.com/upload/";
	public static final String USER_ID					= "user_id";
	public static final String USER					    = "user";
	public static final String USER_PHONE					= "user_phone";
	public static final String USER_PASS					= "user_pass";
	public static final String kHOSTNAME 				= "hostName";
	public static final String kREALHOST				= "RealHost"; // 存储真正的连网地址
	public static final String kMETHODNAME 				= "methodName";
	public static final String kPARAMNAME 				= "paramName";
	public static final String TOKEN               		= "token";
	public static final String LeadId               		= "leadid";
	public static final String AREA               		= "area";
	//用户反馈--xx
	public static final String content                  = "content";
	public static final String flag                     = "flag";
	//用户手机号
	public static final String Phone                  	= "phone";
	public static final int  Padding                  	= 200;
	//联系人
	public static final String CONNECTNUM               = "40089191919";
	//签名key值
	public static final String keySecret               	= "keySecret";
	//签名value值
	public static final String VALUE                    = "jklwxlgy66";
	//public static final String VALUE                    = "jklw";
	//信息返回CODE值 100表示正确
	public static final int CODE                        = 100;
	//信息返回CODE值 token生路失效表示正确
	public static final int TOKENCODE                  = 413 ;
	public static final int TOKENNULL                  = 412 ;

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
	//配送时间
	public static final String orderpslist      		 = "orderpslist";
	//分类商品列表
	public static final String PRODUCTDETAIL      		 = "productinfojson";
	//登录接口
	public static final String LOGIN      				 =  "logindo";

	//注销接口
	public static final String ZHUXIAO      				 =  "zhuxiaodo";

	//注册接口
	public static final String REGISTER      			 =  "zhucedo";
	//修改密码
	public static final String XGCode      			     =  "xgmimado";
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
	//修改地址
	public static final String UPDATEADDADDRESS                = "updatememaddressjson";

	//删除地址
	public static final String DELETEADDRESS             = "deladdressjson";
	//默认地址
	public static final String ISADDRESS                  = "mraddressjson";
	//购物车结算
	public static final String JIESUAN                     = "addorder";
	//订单列表
	public static final String ORDERLIST                   = "orderlist";
	//团长订单列表
	public static final String LEADERORDERLIST            = "leaderorderlist";
	//用户积分获得记录
	public static final String GETINTEGRALORDER           = "memscorehdlistjson";
	//用户积分获得记录
	public static final String XIAOFEIORDER                = "memscorexflistjson";
	//8.3用户余额充值记录
	public static final String MONEYORDER                  = "memmoneyczlistjson";
	//用户消费记录
	public static final String XFMONEYORDER                = "memmoneyxflistjson";
	//社区列表
	public static final String SheQuList                    = "indexsqlistjson";
	//推荐购买
	public static final String TUIJIAN                      = "tuijianbuyjson";
	//团长中心
	public static final String HEADERCETER                 = "leadercenterjson";
	//订单数量
	public static final String CountOrder                  = "memordercount";
	//申请成为副团长页面
	public static final String ShenQing                    = "leaderfulistjson";
	//团长订单数量
	public static final String LeaderOrderCount           = "leaderordercount";
	//用户待提货
	public static final String LeaderTihuo                 = "leadertihuo";
	//用户收货确认核销
	public static final String LeaderTihuoDo                 = "leadertihuodo";
	//扫码收货
	public static final String LeaderShouHuo                 = "leadershouhuo";
	//确认收货地址
	public static final String ComitOrderAddr                 = "orderdaddressqr";
	//订单支付
	public static final String PAYORDER                      = "payorder";
	//充值
	public static final String PAYMoney                      = "memchongzhi";
	//秒杀列表
	public static final String MiaoSha                       = "miaolist";
	//秒杀商品详情
	public static final String MiaoShaOrder                  = "productinfomiaojson";
	//秒杀商品下单
	public static final String MiaoShaOrderADD                 = "addmiaoorder";
	//团购列表
	public static final String TUANGOULIST                     = "tuaninfo";
	//团购商品详情
	public static final String TUANGOULISTDetail              = "productinfotuanjson";
	//团购下单
	public static final String TUANGOUAdd                      = "addtuanorder";
	//积分商品下单
	public static final String JiFenADD                         = "addscoreorder";
	//退款
	public static final String Orderdaddressqr                 = "ordertui";
	//统一成为副团长
	public static final String TONGYI                            = "leaderfutongyijson";
	public static final String QUXIAO                            = "leaderfuquxiaojson";
	//删除副团长
	//
	public static final String ADDLEADERFU                      = "addleaderfujson";
	//佣金记录
	public static final String leaderyjhdlistjson               = "leaderyjhdlistjson";
	//佣金提现使用
	public static final String leaderyjtxlistjson               = "leaderyjtxlistjson";
	//积分下单
	public static final String ScoreOrder                        = "addscoreorder";
	//版本更新
	public static final String GetVersion                         = "updateContr";
	//购物车删除
	public static final String DeleteCart                         = "delcartjson";
	//团长申请
	public static final String HeadApply                         = "addleaderjson";
	//确认收货
	public static final String MEMBERSHDO                         = "membershdo";
	//设置
	public static final String SETTING                           = "settingjson";
	//充值item获取
	public static final String MONEYITEM                           = "memberczpage";
	//代提可提
	public static final String MONEYDT                           = "leaderyjtijson";
	//提现申请
	public static final String TXAPPLY                           = "leaderyjtxsqjson";
	//退出
	public static final String LOGINOUT                           = "memberquit";
	//评价
	public static final String COmment                           = "addevaulation";
	//修改昵称
	public static final String UPDATENAME                         = "updatememnick";
	//修改昵称
	public static final String NOMOREDATA                         = "无更多数据";




}
