package com.liuwa.shopping.client;

import java.util.HashMap;

public class TransferRequestTag {
	
	public static final int GETLOGININFOSERVICE						= 1 ;
	public static final int GETMYDEALFILESLIST						= 2 ;
	public static final int ISMAINBODYSERVICE						= 3 ;
	public static final int GETFILETITLE							= 4 ;
	public static final int GETWORDCENTER							= 5 ;
	public static final int GETMARKINGLIST							= 6 ;
	public static final int IOS_SUBMITFILE							= 7 ;
	public static final int AZ_SUBMITFILE							= 71 ;
	public static final int IOS_MOREPOINTINFO						= 8 ;
	public static final int GETBINDTRANSACTORSSERVICE				= 9 ;
	public static final int GETDEPTLISTBYTRANRANG					= 10 ;
	public static final int GETDPETUSERLISTBYTRANRANG				= 11 ;
	public static final int GETWORKGROUPLISTBYTRANRANG				= 12 ;
	public static final int GETWORKGROUPTRANUSER					= 13 ;
	public static final int IOS_MORESUBMIT							= 14 ;
	public static final int GETFLOWLIST								= 15 ;
	public static final int GETFLOWDETAIL							= 16 ;
	public static final int DOFLOWUPDATE							= 17 ;
	public static final int GETTOAUDITBILLLISTBYUSER				= 18 ;
	public static final int GETBILLINFO								= 19 ;
	public static final int AUDITBILL								= 20 ;
	public static final int GETXXZZLISTSERVICE						= 21 ;
	public static final int GETTZLISTSERVICE						= 22 ;
	public static final int GETINFORMSERVICE						= 23 ;
	public static final int IOS_GETNOTIFYINFORMSERVICE				= 24 ;
	public static final int GETFORMATTINFO							= 25 ;
	
	public static final int GETMYDEALDOCUMENTLIST					= 26;
	public static final int GETTZCOUNT 								= 27;
	public static final int GETFORMURL								= 28;
	public static final int ISBACKFLOW								= 29;
	public static final int BACKFLOW								= 30;
	
	public static final int UPDATE									= 31;
	public static final int GETUNREADERFILEPASSLIST					= 32;
	public static final int GETFILEPASSBODY							= 33;
	public static final int GETHISFLOWLIST							= 34;
	public static final int	GETREADERFILEPASSLIST					= 35;
	public static final int GETZJGLHISTORYLIST						= 36;
	
	public static final int GETMYDEALFILESLIST_LENGTH				= 91;
	public static final int GETFLOWLIST_LENGTH						= 92;
	public static final int GETTOAUDITBILLLISTBYUSER_LENGTH			= 93;
	
	public static final int SETTINGBADIDUPUSH						= 100;
	
	
	private static HashMap<Integer, String> requestTagMap 	= null;
	
	public static HashMap<Integer, String> getRequestTagMap(){
		if (null == requestTagMap) {
			requestTagMap = new HashMap<Integer, String>();
			
			requestTagMap.put(GETLOGININFOSERVICE, "getLoginInfoService");
			requestTagMap.put(GETMYDEALFILESLIST, "getMyDealFilesList");
			requestTagMap.put(GETUNREADERFILEPASSLIST, "getUnReaderFilePassList");
			requestTagMap.put(GETREADERFILEPASSLIST, "getReaderFilePassList");
			requestTagMap.put(GETZJGLHISTORYLIST, "getToAuditedBillListByUser");
			requestTagMap.put(GETFILEPASSBODY, "getFilePassBody");
			requestTagMap.put(GETHISFLOWLIST, "getHisFlowlist");
			requestTagMap.put(ISMAINBODYSERVICE, "IsMainBodyService");
			requestTagMap.put(GETFILETITLE, "getFileTitle");
			requestTagMap.put(GETWORDCENTER, "getWordCenter");
			requestTagMap.put(GETMARKINGLIST, "getMarkingList");
			requestTagMap.put(IOS_SUBMITFILE, "IOS_submitFile");
			requestTagMap.put(AZ_SUBMITFILE, "AZ_submitFile");
			requestTagMap.put(IOS_MOREPOINTINFO, "IOS_MorePointInfo");
			requestTagMap.put(GETBINDTRANSACTORSSERVICE, "GetBindTransactorsService");
			requestTagMap.put(GETDEPTLISTBYTRANRANG, "getDeptListByTranRang");
			requestTagMap.put(GETDPETUSERLISTBYTRANRANG, "getDpetUserListByTranRang");
			requestTagMap.put(GETWORKGROUPLISTBYTRANRANG, "getWorkGroupListByTranRang");
			requestTagMap.put(GETWORKGROUPTRANUSER, "getWorkGroupTranUser");
			requestTagMap.put(IOS_MORESUBMIT, "IOS_MoreSubMit");
			requestTagMap.put(GETFLOWLIST, "getFlowList");
			requestTagMap.put(GETFLOWDETAIL, "getFlowdetail");
			requestTagMap.put(DOFLOWUPDATE, "doFlowUpdate");
			requestTagMap.put(GETTOAUDITBILLLISTBYUSER, "getToAuditBillListByUser");
			requestTagMap.put(GETBILLINFO, "getBillInfo");
			requestTagMap.put(AUDITBILL, "auditBill");
			requestTagMap.put(GETXXZZLISTSERVICE, "getxxzzListService");
			requestTagMap.put(GETTZLISTSERVICE, "gettzListService");
			requestTagMap.put(GETINFORMSERVICE, "GetInformService");
			requestTagMap.put(IOS_GETNOTIFYINFORMSERVICE, "IOS_GetNotifyInformService");
			requestTagMap.put(GETFORMATTINFO, "getFormAttInfo");
			
			requestTagMap.put(GETMYDEALFILESLIST_LENGTH, "getMyDealFilesList");
			requestTagMap.put(GETFLOWLIST_LENGTH, "getFlowList");
			requestTagMap.put(GETTOAUDITBILLLISTBYUSER_LENGTH, "getToAuditBillListByUser");
			
			requestTagMap.put(SETTINGBADIDUPUSH, "SavePushNumber");
			
			requestTagMap.put(GETMYDEALDOCUMENTLIST, "getMyDealDocumentList");
			requestTagMap.put(GETTZCOUNT, "getTZCount");
			requestTagMap.put(GETFORMURL, "getFromUrl");
			requestTagMap.put(ISBACKFLOW, "IsBackFlow");
			requestTagMap.put(BACKFLOW, "BackFlow");
			requestTagMap.put(UPDATE, "AN_Upgrade");
			
		}
		
		return requestTagMap;
	}

}
