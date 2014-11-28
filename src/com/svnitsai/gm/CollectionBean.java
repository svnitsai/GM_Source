package com.svnitsai.gm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectionBean implements Serializable 
{
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	
	private int custCode;
	private int collectionId;
	private long invoiceNumber;
	private double invoiceAmount;
	private String partyName;
	private String partyInfo;
	private String status;
	private Date dueDate;
	private Date deferredDate;
	private String updatedBy;
	private Date updatedDate;
	

	private ArrayList<CollectionDetailBean> detailsList;
	
	public CollectionBean()
	{
		detailsList = new ArrayList<CollectionDetailBean>();
	}
	
	public int getCustCode() {
		return custCode;
	}
	public void setCustCode(int partyId) {
		this.custCode = partyId;
	}
	public int getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(int collectionId) {
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
	public double getTotalCollectionAmount() {
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
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getPartyInfo() {
		return partyInfo;
	}
	public void setPartyInfo(String info) {
		this.partyInfo = info;
	}
	public void setPartyInfo(String city, String contactNumber)
	{
		String info = city;
		if(info == null)
		{
			info = "";
		}
		if(contactNumber == null)
		{
			contactNumber = "";
		}
		if(info.length() > 0 && contactNumber.length() > 0)
		{
			info += "<br/>Phone: ";
		}
		info += contactNumber;
		setPartyInfo(info);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDueDateStr() {
		return getFormattedDate(dueDate);
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getDeferredDateStr() {
		return CollectionBean.getFormattedDate(deferredDate);
	}
	public Date getDeferredDate() {
		return deferredDate;
	}
	public void setDeferredDate(Date deferredDate) {
		this.deferredDate = deferredDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDateStr() {
		return getFormattedDate(updatedDate);
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
			str += "<br> Deferred To " + defStr;
		}
		return str;
	}
	public static String getFormattedDate(Date d)
	{
		if(d == null)
		{
			return "";
		}
		else
		{
			return dateFormatter.format(d);
		}
	}
}
