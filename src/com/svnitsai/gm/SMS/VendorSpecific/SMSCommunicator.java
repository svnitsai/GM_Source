package com.svnitsai.gm.SMS.VendorSpecific;

import java.util.List;

import com.svnitsai.gm.SMS.SMSSendRequest;
import com.svnitsai.gm.SMS.SMSSendResponse;

/* 
* SMSCommunicator.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	All vendor specific communicators extend this.
* 
*/

public abstract class SMSCommunicator {
	
	public String vendorName;
	
	public String getName() {
		return vendorName;
	}
	
	//submit bulk SMS Send request to vendor
	public abstract int submitSendSMS(List <SMSSendRequest> sendRequestList, SMSSendResponse sendResponse);

	//submit single SMS Send request to vendor
	public abstract int submitSendSMS(SMSSendRequest sendRequest, SMSSendResponse sendResponse);
}
