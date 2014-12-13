package com.svnitsai.gm;

import java.util.Date;

public class DailyPayableBean 
{
	private long 	payableId;
	private Date 	payableDate;
	private String  payableDateStr;
	private double 	payableAmount;
	private double  paidAmount;
	private String 	status;
	private long	supplierCode;
	private long	supplierBankCode;
	private String	instructions;
	
	public long getPayableId() {
		return payableId;
	}
	public void setPayableId(long payableId) {
		this.payableId = payableId;
	}
	public Date getPayableDate() {
		return payableDate;
	}
	public void setPayableDate(Date payableDate) {
		this.payableDate = payableDate;
	}
	public String getPayableDateStr() {
		return (payableDateStr != null) 
					? payableDateStr
					: Util.getFormattedDate(getPayableDate());
	}
	public void setPayableDateStr(String payableDateStr) {
		this.payableDateStr = payableDateStr;
	}
	public double getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(long supplierCode) {
		this.supplierCode = supplierCode;
	}
	public long getSupplierBankCode() {
		return supplierBankCode;
	}
	public void setSupplierBankCode(long supplierBankCode) {
		this.supplierBankCode = supplierBankCode;
	}
	public String getInstructions() {
		return (instructions != null) ? instructions : "";
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	
}
