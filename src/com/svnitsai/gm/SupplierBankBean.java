package com.svnitsai.gm;

import java.io.Serializable;

public class SupplierBankBean implements Serializable
{
	private int supplierBankId;
	private String bankName;
	private String branchName;
	private String accountNumber;
	private int accountType;
	
	public int getSupplierBankId() {
		return supplierBankId;
	}
	public void setSupplierBankId(int supplierBankId) {
		this.supplierBankId = supplierBankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	
	
}
