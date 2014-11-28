package com.svnitsai.gm;

import java.io.Serializable;

public class CustomerBankBean implements Serializable
{
	private int bankId;
	private String bankName;
	private String branchName;
	private String accountNumber;
	private String accountType;
	
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int supplierBankId) {
		this.bankId = supplierBankId;
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
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	
}
