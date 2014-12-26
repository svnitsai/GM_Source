package com.svnitsai.gm;

import java.io.Serializable;

public class CustomerBankBean implements Serializable
{
	private long bankId;
	private String bankName;
	private String branchName;
	private String accountNumber;
	private String accountType;
	private String accountName;
	
	public long getBankId() {
		return bankId;
	}
	public void setBankId(long supplierBankId) {
		this.bankId = supplierBankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return (branchName != null) ? branchName : "";
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAccountNumber() {
		return (accountNumber != null) ? accountNumber : "";
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
	public String getAccountName() {
		return (accountName != null) ? accountName : "";
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getBankInfoString()
	{
		String supplierBankInfo = getBankName();
		if(getBranchName().trim().length() > 0)
		{
			supplierBankInfo += ", " + getBranchName();
		}
		if(getAccountNumber().trim().length() > 0)
		{
			supplierBankInfo += ", A/c # " + getAccountNumber();
		}
		if(getAccountName().trim().length() > 0)
		{
			supplierBankInfo += ", A/c Name: " + getAccountName();
		}
		return supplierBankInfo;
	}
	
}
