package com.svnitsai.gm.database.generated;

// Generated Jan 9, 2015 6:40:55 PM by Hibernate Tools 4.0.0

import java.util.Date;

/**
 * Customer generated by hbm2java
 */
public class Customer implements java.io.Serializable {

	private int custId;
	private int custCode;
	private String custType;
	private String custName;
	private String custAddress1;
	private String custAddress2;
	private String custAddress3;
	private String custAddress4;
	private String custCity;
	private String custState;
	private String custCountry;
	private Long custContactNumber;
	private Integer custExtension;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private Long secondaryPhoneNumber;
	private Long residencePhoneNumber;
	private Long mobileNumber;
	private Long smsmobileNumber;
	private String smsmobileOwnerName;

	public Customer() {
	}

	public Customer(int custId, int custCode, String custType, String custName,
			Date createdDate, String createdBy, Date updatedDate,
			String updatedBy) {
		this.custId = custId;
		this.custCode = custCode;
		this.custType = custType;
		this.custName = custName;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}

	public Customer(int custId, int custCode, String custType, String custName,
			String custAddress1, String custAddress2, String custAddress3,
			String custAddress4, String custCity, String custState,
			String custCountry, Long custContactNumber, Integer custExtension,
			Date createdDate, String createdBy, Date updatedDate,
			String updatedBy, Long secondaryPhoneNumber,
			Long residencePhoneNumber, Long mobileNumber, Long smsmobileNumber,
			String smsmobileOwnerName) {
		this.custId = custId;
		this.custCode = custCode;
		this.custType = custType;
		this.custName = custName;
		this.custAddress1 = custAddress1;
		this.custAddress2 = custAddress2;
		this.custAddress3 = custAddress3;
		this.custAddress4 = custAddress4;
		this.custCity = custCity;
		this.custState = custState;
		this.custCountry = custCountry;
		this.custContactNumber = custContactNumber;
		this.custExtension = custExtension;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
		this.secondaryPhoneNumber = secondaryPhoneNumber;
		this.residencePhoneNumber = residencePhoneNumber;
		this.mobileNumber = mobileNumber;
		this.smsmobileNumber = smsmobileNumber;
		this.smsmobileOwnerName = smsmobileOwnerName;
	}

	public int getCustId() {
		return this.custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public int getCustCode() {
		return this.custCode;
	}

	public void setCustCode(int custCode) {
		this.custCode = custCode;
	}

	public String getCustType() {
		return this.custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAddress1() {
		return this.custAddress1;
	}

	public void setCustAddress1(String custAddress1) {
		this.custAddress1 = custAddress1;
	}

	public String getCustAddress2() {
		return this.custAddress2;
	}

	public void setCustAddress2(String custAddress2) {
		this.custAddress2 = custAddress2;
	}

	public String getCustAddress3() {
		return this.custAddress3;
	}

	public void setCustAddress3(String custAddress3) {
		this.custAddress3 = custAddress3;
	}

	public String getCustAddress4() {
		return this.custAddress4;
	}

	public void setCustAddress4(String custAddress4) {
		this.custAddress4 = custAddress4;
	}

	public String getCustCity() {
		return this.custCity;
	}

	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}

	public String getCustState() {
		return this.custState;
	}

	public void setCustState(String custState) {
		this.custState = custState;
	}

	public String getCustCountry() {
		return this.custCountry;
	}

	public void setCustCountry(String custCountry) {
		this.custCountry = custCountry;
	}

	public Long getCustContactNumber() {
		return this.custContactNumber;
	}

	public void setCustContactNumber(Long custContactNumber) {
		this.custContactNumber = custContactNumber;
	}

	public Integer getCustExtension() {
		return this.custExtension;
	}

	public void setCustExtension(Integer custExtension) {
		this.custExtension = custExtension;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getSecondaryPhoneNumber() {
		return this.secondaryPhoneNumber;
	}

	public void setSecondaryPhoneNumber(Long secondaryPhoneNumber) {
		this.secondaryPhoneNumber = secondaryPhoneNumber;
	}

	public Long getResidencePhoneNumber() {
		return this.residencePhoneNumber;
	}

	public void setResidencePhoneNumber(Long residencePhoneNumber) {
		this.residencePhoneNumber = residencePhoneNumber;
	}

	public Long getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getSmsmobileNumber() {
		return this.smsmobileNumber;
	}

	public void setSmsmobileNumber(Long smsmobileNumber) {
		this.smsmobileNumber = smsmobileNumber;
	}

	public String getSmsmobileOwnerName() {
		return this.smsmobileOwnerName;
	}

	public void setSmsmobileOwnerName(String smsmobileOwnerName) {
		this.smsmobileOwnerName = smsmobileOwnerName;
	}

}
