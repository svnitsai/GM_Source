package com.svnitsai.gm;

import java.io.Serializable;
import java.util.Date;

public class CollectionDetailBean implements Serializable
{
	private int collectionDetailId;
	private int companyId;
	private int	partyBankId;
	private int	supplierId;
	private int supplierBankId;
	private String companyName;
	private String partyBankName;
	private String partyBankBranch;
	private String supplierName;
	private String supplierBankName;
	private String supplierBankBranch;
	private String supplierAccountNumber;
	private int ledgerNumber;
	private double paidAmount;
	private Date collectionDate;
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierBankName() {
		return supplierBankName;
	}
	public void setSupplierBankName(String supplierBankName) {
		this.supplierBankName = supplierBankName;
	}
	public String getSupplierBankBranch() {
		return supplierBankBranch;
	}
	public void setSupplierBankBranch(String supplierBankBranch) {
		this.supplierBankBranch = supplierBankBranch;
	}
	public String getSupplierAccountNumber() {
		return supplierAccountNumber;
	}
	public void setSupplierAccountNumber(String supplierAccountNumber) {
		this.supplierAccountNumber = supplierAccountNumber;
	}
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
	public int getPartyBankId() {
		return partyBankId;
	}
	public void setPartyBankId(int partyBankId) {
		this.partyBankId = partyBankId;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public int getSupplierBankId() {
		return supplierBankId;
	}
	public void setSupplierBankId(int supplierBankId) {
		this.supplierBankId = supplierBankId;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
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
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getPartyBankInfo() {
		return partyBankName + ", " + partyBankBranch;
	}
	public void setPartyBankInfo(String bankName, String branch)
	{
		setPartyBankName(bankName);
		setPartyBankBranch(branch);
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSupplierAccountInfo()
	{
		String info = 	getSupplierName() + 
						"<br>" + 
						getSupplierBankName();
		return info;
	}
	public String getPartyBankName() {
		return partyBankName;
	}
	public void setPartyBankName(String partyBankName) {
		this.partyBankName = partyBankName;
	}
	public String getPartyBankBranch() {
		return partyBankBranch;
	}
	public void setPartyBankBranch(String partyBankBranch) {
		this.partyBankBranch = partyBankBranch;
	}
}
