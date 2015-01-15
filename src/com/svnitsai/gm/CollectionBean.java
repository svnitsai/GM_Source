package com.svnitsai.gm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectionBean implements Serializable 
{
	private long custCode;
	private long agentCode;
	private long collectionId;
	private long invoiceNumber;
	private double invoiceAmount;
	private Date invoiceDate;
	private String custName;
	private String custPhoneNumber;
	private String custCity;
	private String status;
	private Date dueDate;
	private String agentName;
	private Date deferredDate;
	private String deferredDateStr;
	private String updatedBy;
	private Date updatedDate;
	

	private ArrayList<CollectionDetailBean> detailsList;
	
	public CollectionBean()
	{
		detailsList = new ArrayList<CollectionDetailBean>();
	}
	
	public long getCustCode() {
		return custCode;
	}
	public void setCustCode(long partyId) {
		this.custCode = partyId;
	}
	public long getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(long agentId) {
		this.agentCode = agentId;
	}
	public long getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(long collectionId) {
		this.collectionId = collectionId;
	}
	public long getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceDateStr() {
		return Util.getFormattedDate(invoiceDate);
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public double getTotalPaidAmount() {
		double totalCollectionAmount = 0;
		for(CollectionDetailBean detailBean : getDetailsList())
		{
			if(detailBean.getCollectionDate() != null)
			{
				totalCollectionAmount += detailBean.getPaidAmount();
			}
		}
		
		return totalCollectionAmount;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getCustPhoneNumber() {
		return custPhoneNumber;
	}
	public void setCustPhoneNumber(String custPhoneNumber) {
		this.custPhoneNumber = custPhoneNumber;
	}
	public String getCustCity() {
		return custCity;
	}
	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}
	public String getStatus() {
		return (status != null) ? status : "Open";
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDueDateStr() {
		return Util.getFormattedDate(dueDate);
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getDeferredDateStr() {
		return (this.deferredDateStr != null) 
				? this.deferredDateStr
				: Util.getFormattedDate(deferredDate);
	}
	public Date getDeferredDate() {
		return deferredDate;
	}
	public void setDeferredDate(Date deferredDate) {
		this.deferredDate = deferredDate;
	}
	public void setDeferredDateStr(String dateStr) {
		this.deferredDateStr = dateStr;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDateStr() {
		return Util.getFormattedDate(updatedDate);
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public ArrayList<CollectionDetailBean> getDetailsList() {
		return detailsList;
	}
	public void setDetailsList(ArrayList<CollectionDetailBean> detailsList) {
		this.detailsList = detailsList;
	}
	public String getDueDateForDisplay()
	{
		String str = getDueDateStr();
		String defStr = getDeferredDateStr();
		if(defStr.length() > 0)
		{
			str += "<br> DEFERRED To <br>" + defStr;
		}
		else
		{
			str += "<br>" + getStatus();
		}
		return str;
	}
	
}
