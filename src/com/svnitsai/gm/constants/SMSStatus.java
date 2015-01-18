package com.svnitsai.gm.constants;
/*
 * SMSStatus.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Constants used in SMSContactHistory table status
 * 
 */
public enum SMSStatus {
	INITIAL("Initial"), SENT("Sent"), FAILED("Failed");
 
	private String statusCode;
 
	private SMSStatus(String s) {
		statusCode = s;
	}
 
	public String getStatusCode() {
		return statusCode;
	}
 
}