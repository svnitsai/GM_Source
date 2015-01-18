package com.svnitsai.gm.SMS.VendorSpecific;

import com.svnitsai.gm.SMS.DataPost;
import com.svnitsai.gm.SMS.URLParamEncoder;
import com.svnitsai.gm.util.date.DateUtil;

public class TestApexDirectPost {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//datapost.inlinePost();
		
        //String inStringURL = "http://sms.apexinfo.co.in/sendsms.jsp?user=username&password=demo1234&message=";
        //inStringURL = inStringURL+URLParamEncoder.encode("test message");
		
		DataPost datapost = new DataPost();
		//String inStringURL = ApexConfig.getSMS_Send_URL();
		String inStringURL = "http://sms.apexinfo.co.in/sendsms.jsp";
		String inStringRequestParm = "?user=username&password=demo1234&mobiles=91xxxxxxxxxx&senderid=TEST&sms=" + URLParamEncoder.encode("test message");;
		StringBuffer outStringBufferResponse = new StringBuffer();
		int returnCode = datapost.inlineHttpClientPost(inStringURL, inStringRequestParm, outStringBufferResponse, DateUtil.getCurrentTimestamp().toString());
		
		

	}

}
