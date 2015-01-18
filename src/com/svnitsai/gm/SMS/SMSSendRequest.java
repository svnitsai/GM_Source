package com.svnitsai.gm.SMS;

import java.util.Date;
/*
 * SMSSendRequest.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	SMS communicator object; Vendor SMS packages consume this Object to send SMS
 * 
 */
public class SMSSendRequest {
	
	private String SMSMessage = null;
	private String MobileNumber = null;
	private String SMSMessageGrouping = null;

	//Following are options fields
	private String ClientSMSId = null; //pass unique sms serial no (ex. 311)
	private String cdmaSenderId = null; //cdma mobile number
	private Date SMSScheduleTime = null;
	private String SenderID = null; //sender id. If you pass sender id as a blank so it will take defaul sender id which is assigned in SMS account.
	private String AccountUsageTypeID = "0"; //0-Service, 1-promotional for Apex
	private boolean uniCode = false; //false is 0; 0 or 1. 0 mean normal message and 1 mean unicode message

	public String getSMSMessage() {
		return SMSMessage;
	}
	public void setSMSMessage(String sMSMessage) {
		SMSMessage = sMSMessage;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	public String getSMSMessageGrouping() {
		return SMSMessageGrouping;
	}
	public void setSMSMessageGrouping(String sMSBulkMessageGrouping) {
		SMSMessageGrouping = sMSBulkMessageGrouping;
	}
	public String getClientSMSId() {
		return ClientSMSId;
	}
	public void setClientSMSId(String clientSMSId) {
		ClientSMSId = clientSMSId;
	}
	public String getCdmaSenderId() {
		return cdmaSenderId;
	}
	public void setCdmaSenderId(String cdmaSenderId) {
		this.cdmaSenderId = cdmaSenderId;
	}
	public Date getSMSScheduleTime() {
		return SMSScheduleTime;
	}
	public void setSMSScheduleTime(Date sMSScheduleTime) {
		SMSScheduleTime = sMSScheduleTime;
	}
	public String getSenderID() {
		return SenderID;
	}
	public void setSenderID(String senderID) {
		SenderID = senderID;
	}
	public String getAccountUsageTypeID() {
		return AccountUsageTypeID;
	}
	public void setAccountUsageTypeID(String accountUsageTypeID) {
		AccountUsageTypeID = accountUsageTypeID;
	}
	public boolean isUniCode() {
		return uniCode;
	}
	public void setUniCode(boolean uniCode) {
		this.uniCode = uniCode;
	}
	
}
