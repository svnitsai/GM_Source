package com.svnitsai.gm;

import java.io.Serializable;
import java.util.Date;

public class CollectionDetailBean implements Serializable
{
	private long collectionDetailId;
	private long companyCode;
	private long supplierCode;
	private long supplierBankId;
	private String companyName;
	private String customerBankName;
	private String supplierName;
	private String supplierBankName;
	private String supplierBankBranch;
	private String supplierAccountNumber;
	private int ledgerNumber;
	private double paidAmount;
	private Date collectionDate;
	private String collectionDateStr;
	private String updatedBy;
	private Date updatedDate;

	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDateStr() {
		return CollectionBean.getFormattedDate(updatedDate);
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getSupplierName() {
		return (supplierName != null ) ? supplierName : "";
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
	public long getCollectionDetailId() {
		return collectionDetailId;
	}
	public void setCollectionDetailId(long collectionDetailId) {
		this.collectionDetailId = collectionDetailId;
	}
	public int getLedgerNumber() {
		return ledgerNumber;
	}
	public void setLedgerNumber(int ledgerNumber) {
		this.ledgerNumber = ledgerNumber;
	}
	public long getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(long supplierCode) {
		this.supplierCode = supplierCode;
	}
	public long getSupplierBankId() {
		return supplierBankId;
	}
	public void setSupplierBankId(long supplierBankId) {
		this.supplierBankId = supplierBankId;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getCollectionDateStr() {
		return (this.collectionDateStr != null )
				? this.collectionDateStr
				: CollectionBean.getFormattedDate(collectionDate);
	}
	public Date getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
	public void setCollectionDateStr(String collectionDateStr) {
		this.collectionDateStr = collectionDateStr;
	}
	public long getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(long companyCode) {
		this.companyCode = companyCode;
	}
	public String getCustomerBankName() {
		return (customerBankName != null ? customerBankName : "");
	}
	public void setCustomerBankName(String bankName)
	{
		this.customerBankName = bankName;
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
}
