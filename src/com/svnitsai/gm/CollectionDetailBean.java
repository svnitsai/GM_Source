package com.svnitsai.gm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CollectionDetailBean implements Serializable
{
	private int collectionDetailId;
	private int ledgerNumber;
	private double collectionAmount;
	private String partyBank;
	private String partyBankBranch;
	private String supplierName;
	private String supplierBank;
	private String supplierAccountNum;
	private Date collectionDate;
	private Date deferedDate;
	
	public int getCollectionDetailId() {
		return collectionDetailId;
	}
	public void setCollectionDetailId(int collectionDetailId) {
		this.collectionDetailId = collectionDetailId;
	}
	public int getLedgerNumber() {
		return ledgerNumber;
	}
	public void setLedgerNumber(int ledgerNumber) {
		this.ledgerNumber = ledgerNumber;
	}
	public double getCollectionAmount() {
		return collectionAmount;
	}
	public void setCollectionAmount(double collectionAmount) {
		this.collectionAmount = collectionAmount;
	}
	public String getPartyBank() {
		return (partyBank != null) ? partyBank : "";
	}
	public void setPartyBank(String partyBank) {
		this.partyBank = partyBank;
	}
	public String getPartyBankBranch() {
		return (partyBankBranch != null) ? partyBankBranch : "";
	}
	public void setPartyBankBranch(String partyBankBranch) {
		this.partyBankBranch = partyBankBranch;
	}
	public String getSupplierName() {
		return (supplierName != null)? supplierName : "";
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierBank() {
		return (supplierBank != null) ? supplierBank : "";
	}
	public void setSupplierBank(String supplierBank) {
		this.supplierBank = supplierBank;
	}
	public String getSupplierAccountNum() {
		return (supplierAccountNum != null) ? supplierAccountNum : "";
	}
	public void setSupplierAccountNum(String supplierAccountNum) {
		this.supplierAccountNum = supplierAccountNum;
	}
	public String getCollectionDateStr() {
		return CollectionBean.getFormattedDate(collectionDate);
	}
	public Date getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
	public String getDeferedDateStr() {
		return CollectionBean.getFormattedDate(deferedDate);
	}
	public Date getDeferedDate() {
		return deferedDate;
	}
	public void setDeferedDate(Date deferedDate) {
		this.deferedDate = deferedDate;
	}
	
	public String getPartyAccountInfo()
	{
		if(getPartyBank().length() > 0)
		{
			return (this.getPartyBank() + "<br/>" + this.getPartyBankBranch());
		}
		else
		{
			return "";
		}
	}
	
	public String getSupplierAccountInfo()
	{
		if(getSupplierName().length() > 0)
		{
			return (this.getSupplierName() + "<br/>" + 
					this.getSupplierBank() + "<br/>" + 
					"A/C " + this.getSupplierAccountNum());
		}
		else
		{
			return "";
		}
	}
	
}
