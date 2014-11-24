package com.svnitsai.gm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectionBean implements Serializable 
{
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	
	private int partyId;
	private int collectionId;
	private long invoiceNumber;
	private double invoiceAmount;
	private String partyName;
	private String partyContact;
	private String status;
	private Date dueDate;
	private ArrayList<CollectionDetailBean> detailsList;
	
	public CollectionBean()
	{
		detailsList = new ArrayList<CollectionDetailBean>();
	}
	
	public int getPartyId() {
		return partyId;
	}
	public void setPartyId(int partyId) {
		this.partyId = partyId;
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
				totalCollectionAmount += detailBean.getCollectionAmount();
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
	public String getPartyContact() {
		return partyContact;
	}
	public void setPartyContact(String partyContact) {
		this.partyContact = partyContact;
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
	public ArrayList<CollectionDetailBean> getDetailsList() {
		return detailsList;
	}
	public void setDetailsList(ArrayList<CollectionDetailBean> detailsList) {
		this.detailsList = detailsList;
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
