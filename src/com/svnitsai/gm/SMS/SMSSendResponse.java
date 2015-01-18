package com.svnitsai.gm.SMS;

import com.svnitsai.gm.constants.SMSUIMessage;

/*
 * SMSSendResponse.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	SMS communicator object; Vendor SMS packages return this Object after processing the SMS
 * 
 */
public class SMSSendResponse {
	
	private int returnCode = 0;
	private String failureReason = "";
	private String successReferenceId = ""; //aka MessageId
	private SMSUIMessage smsUIMessage;
	
	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public String getSuccessReferenceId() {
		return successReferenceId;
	}
	public void setSuccessReferenceId(String successReferenceId) {
		this.successReferenceId = successReferenceId;
	}
	public SMSUIMessage getSmsUIMessage() {
		return smsUIMessage;
	}
	public void setSmsUIMessage(SMSUIMessage smsUIMessage) {
		this.smsUIMessage = smsUIMessage;
	}

}
