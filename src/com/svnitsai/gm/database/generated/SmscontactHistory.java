package com.svnitsai.gm.database.generated;

// Generated Jan 17, 2015 10:18:43 AM by Hibernate Tools 4.0.0

import java.util.Date;

/**
 * SmscontactHistory generated by hbm2java
 */
public class SmscontactHistory implements java.io.Serializable {

	private int historyId;
	private String requestInitiatedTs;
	private String requestVendorReferrence;
	private long payCreferenceNumber;
	private long custId;
	private int custCode;
	private String custName;
	private long smsmobileNumber;
	private String smsmobileOwnerName;
	private String smsmessage;
	private String smsvendor;
	private Date sentTimeStamp;
	private String failedReason;
	private String status;
	private Date statusUpdatedTs;

	public SmscontactHistory() {
	}

	public SmscontactHistory(int historyId, String requestInitiatedTs,
			long payCreferenceNumber, long custId, int custCode,
			String custName, long smsmobileNumber, String smsmobileOwnerName,
			String smsmessage, String smsvendor, String status,
			Date statusUpdatedTs) {
		this.historyId = historyId;
		this.requestInitiatedTs = requestInitiatedTs;
		this.payCreferenceNumber = payCreferenceNumber;
		this.custId = custId;
		this.custCode = custCode;
		this.custName = custName;
		this.smsmobileNumber = smsmobileNumber;
		this.smsmobileOwnerName = smsmobileOwnerName;
		this.smsmessage = smsmessage;
		this.smsvendor = smsvendor;
		this.status = status;
		this.statusUpdatedTs = statusUpdatedTs;
	}

	public SmscontactHistory(int historyId, String requestInitiatedTs,
			String requestVendorReferrence, long payCreferenceNumber,
			long custId, int custCode, String custName, long smsmobileNumber,
			String smsmobileOwnerName, String smsmessage, String smsvendor,
			Date sentTimeStamp, String failedReason, String status,
			Date statusUpdatedTs) {
		this.historyId = historyId;
		this.requestInitiatedTs = requestInitiatedTs;
		this.requestVendorReferrence = requestVendorReferrence;
		this.payCreferenceNumber = payCreferenceNumber;
		this.custId = custId;
		this.custCode = custCode;
		this.custName = custName;
		this.smsmobileNumber = smsmobileNumber;
		this.smsmobileOwnerName = smsmobileOwnerName;
		this.smsmessage = smsmessage;
		this.smsvendor = smsvendor;
		this.sentTimeStamp = sentTimeStamp;
		this.failedReason = failedReason;
		this.status = status;
		this.statusUpdatedTs = statusUpdatedTs;
	}

	public int getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public String getRequestInitiatedTs() {
		return this.requestInitiatedTs;
	}

	public void setRequestInitiatedTs(String requestInitiatedTs) {
		this.requestInitiatedTs = requestInitiatedTs;
	}

	public String getRequestVendorReferrence() {
		return this.requestVendorReferrence;
	}

	public void setRequestVendorReferrence(String requestVendorReferrence) {
		this.requestVendorReferrence = requestVendorReferrence;
	}

	public long getPayCreferenceNumber() {
		return this.payCreferenceNumber;
	}

	public void setPayCreferenceNumber(long payCreferenceNumber) {
		this.payCreferenceNumber = payCreferenceNumber;
	}

	public long getCustId() {
		return this.custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public int getCustCode() {
		return this.custCode;
	}

	public void setCustCode(int custCode) {
		this.custCode = custCode;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public long getSmsmobileNumber() {
		return this.smsmobileNumber;
	}

	public void setSmsmobileNumber(long smsmobileNumber) {
		this.smsmobileNumber = smsmobileNumber;
	}

	public String getSmsmobileOwnerName() {
		return this.smsmobileOwnerName;
	}

	public void setSmsmobileOwnerName(String smsmobileOwnerName) {
		this.smsmobileOwnerName = smsmobileOwnerName;
	}

	public String getSmsmessage() {
		return this.smsmessage;
	}

	public void setSmsmessage(String smsmessage) {
		this.smsmessage = smsmessage;
	}

	public String getSmsvendor() {
		return this.smsvendor;
	}

	public void setSmsvendor(String smsvendor) {
		this.smsvendor = smsvendor;
	}

	public Date getSentTimeStamp() {
		return this.sentTimeStamp;
	}

	public void setSentTimeStamp(Date sentTimeStamp) {
		this.sentTimeStamp = sentTimeStamp;
	}

	public String getFailedReason() {
		return this.failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusUpdatedTs() {
		return this.statusUpdatedTs;
	}

	public void setStatusUpdatedTs(Date statusUpdatedTs) {
		this.statusUpdatedTs = statusUpdatedTs;
	}

}
