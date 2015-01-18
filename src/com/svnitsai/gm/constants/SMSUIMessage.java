package com.svnitsai.gm.constants;
/*
 * SMSUIMessage.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	UI messages from SMS processing
 * 
 */
public enum SMSUIMessage {
		TRYLATER_INTERNET_ISSUE("CODE: TL_IE ; System encountered an error; Possibly internet connection issue.  Try after some time!  If this continues,"
				+ " please contact System Administrator!")
	, 	SENT_SUCCESSFULLY ("Requested SMS message(s)have been sent.  Please go to SMS >> History menu option to see the details.")
	, 	SMS_VENDOR_REJECTED("CODE: VR_ ; SMS vendor rejected the request.  Try again later. If this continues,"
			+ " please contact your System Administrator!")
	, 	USER_ACTIONABLE_ID_ISSUE("CODE: UA_ID ; SMS vendor side user name / password is incorrect! "
			+ "Contact your System Administrator to change the same!")
	,	SYSTEM_ERROR("CODE: SE_ ; System encountered an error.  Please try again!  If this continues,"
				+ " please contact System Administrator!")
	;
 
	private String message;
 
	private SMSUIMessage(String s) {
		message = s;
	}
 
	public String getMessage() {
		return message;
	}
 
}