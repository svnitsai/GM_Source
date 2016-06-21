package com.svnitsai.gm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectionBean implements Serializable 
{
	public static enum DeferredReasonEnum
	{
		NO_REPLY(1, "No Reply"),
		NO_CONTACT(2, "Unable to contact"),
		NEXT_DAY(3, "Daily Next Day"),
		NOT_REACHABLE(4, "Not Reachable"),
		OTHER(5, "Other");
		
		private int id;
		private String reason;
		
		DeferredReasonEnum(int id, String reason)
		{
			this.id = id;
			this.reason = reason;
		}
		
		public static String getReason(int id)
		{
			if(id == NO_REPLY.id)
			{
				return NO_REPLY.reason;
			}
			else if(id == NO_CONTACT.id)
			{
				return NO_CONTACT.reason;
			}
			else if(id == NEXT_DAY.id)
			{
				return NEXT_DAY.reason;
			}
			else if(id == NOT_REACHABLE.id)
			{
				return NOT_REACHABLE.reason;
			}
			else 
			{
				return OTHER.reason;
			}
			
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getReason()
		{
			return reason;
		}
		
		public static int getId(String reason)
		{
			if(NO_REPLY.reason.equals(reason))
			{
				return NO_REPLY.id;
			}
			else if(NO_CONTACT.reason.equals(reason))
			{
				return NO_CONTACT.id;
			}
			else if(NEXT_DAY.reason.equals(reason))
			{
				return NEXT_DAY.id;
			}
			else if(NOT_REACHABLE.reason.equals(reason))
			{
				return NOT_REACHABLE.id;
			}
			else 
			{
				return OTHER.id;
			}
			
		}
	}
	
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
	private String formNumber;
	private Date deferredDate;
	private String deferredDateStr;
	private String deferredReason;
	private int deferredReasonChoice;
	/** Introducing the ledger number and the remarks field to the DailyPayC table,
	 * 	as Garment Mantra wants to edit directly on the listing page and remove the edit option.
	 * 	If we wanted to edit directly on the page and have the advanced edit button,
	 * 	then refactor this to obtain these two fields from DailyPayCDetails tables.
	 */
	private String ledgerNumber;
	private String remarks;
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
	public String getFormNumber() {
		return formNumber;
	}
	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
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
	public String getDeferredReason() {
		return deferredReason;
	}
	public void setDeferredReason(String deferredReason) {
		this.deferredReason = deferredReason;
	}
	public int getDeferredReasonChoice() {
		return deferredReasonChoice;
	}
	public void setDeferredReasonChoice(int deferredReasonChoice) {
		this.deferredReasonChoice = deferredReasonChoice;
	}
	public void setDeferredReasonChoiceStr(String deferredReasonChoice) {
		try
		{
			this.deferredReasonChoice = Integer.parseInt(deferredReasonChoice);
		}
		catch(Exception e){
			e.printStackTrace();
			this.deferredReasonChoice = 0;
		}
	}
	public void updateDeferredReason() 
	{
		// check if deferred choice is set. If yes, this bean was created from UI request
		if(getDeferredReasonChoice() > 0)
		{
			// if any of the standard reasons were chosen, set the predefined reason
			if(getDeferredReasonChoice() != DeferredReasonEnum.OTHER.id)
			{
				setDeferredReason(DeferredReasonEnum.getReason(getDeferredReasonChoice()));
			}
		}
		else if(getDeferredReason() != null)
		{
			//set the choice so that it can be displayed on the UI.
			setDeferredReasonChoice(DeferredReasonEnum.getId(getDeferredReason()));
		}
		
		
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
//		if(!isClosed() && defStr.length() > 0)
//		{
//			str += "<br> DEFERRED To <br>" + defStr;
//		}
//		else
//		{
//			str += "<br>" + getStatus();
//		}
		str += "<br>" + getStatus();
		return str;
	}
	public boolean isClosed()
	{
		return "closed".equalsIgnoreCase(getStatus());
	}

	public String getLedgerNumber() {
		return (ledgerNumber != null) ? ledgerNumber : "";
	}

	public void setLedgerNumber(String ledgerNumber) {
		this.ledgerNumber = ledgerNumber;
	}

	public String getRemarks() {
		return (remarks != null) ? remarks : "";
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
}
