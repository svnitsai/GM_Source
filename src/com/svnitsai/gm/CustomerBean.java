package com.svnitsai.gm;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerBean implements Serializable
{
	private long id;
	private String type;
	private String name;
	private String address1;
	private String address2;
	private String location;
	private int phoneNumber;
	private ArrayList<CustomerBankBean> bankAccountList;
	
	public CustomerBean()
	{
		bankAccountList = new ArrayList<CustomerBankBean>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public ArrayList<CustomerBankBean> getBankAccountList() {
		return bankAccountList;
	}
	public void setBankAccountList(ArrayList<CustomerBankBean> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isMerchant()
	{
		return ("merchant".equalsIgnoreCase(getType()));
	}
	
	public boolean isSupplier()
	{
		return ("supplier".equalsIgnoreCase(getType()));
	}
	
	public boolean isCompany()
	{
		return ("company".equalsIgnoreCase(getType()));
	}
	
	public boolean isAgent()
	{
		return ("Agent".equalsIgnoreCase(getType()));
	}
}
