package com.svnitsai.gm.database.generated;

// Generated Jan 9, 2015 6:40:55 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;

/**
 * DailyPayCdetails generated by hbm2java
 */
public class DailyPayCdetails implements java.io.Serializable {

	private DailyPayCdetailsId id;
	private Date payCdate;
	private String payCtype;
	private Integer supplierCode;
	private Long supplierBankId;
	private BigDecimal paidAmount;
	private Integer accountLocationCode;
	private Integer ledgerPageNumber;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private int deleted;

	public DailyPayCdetails() {
	}

	public DailyPayCdetails(DailyPayCdetailsId id, Date createdDate,
			String createdBy, int deleted) {
		this.id = id;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.deleted = deleted;
	}

	public DailyPayCdetails(DailyPayCdetailsId id, Date payCdate,
			String payCtype, Integer supplierCode, Long supplierBankId,
			BigDecimal paidAmount, Integer accountLocationCode,
			Integer ledgerPageNumber, Date createdDate, String createdBy,
			Date updatedDate, String updatedBy, int deleted) {
		this.id = id;
		this.payCdate = payCdate;
		this.payCtype = payCtype;
		this.supplierCode = supplierCode;
		this.supplierBankId = supplierBankId;
		this.paidAmount = paidAmount;
		this.accountLocationCode = accountLocationCode;
		this.ledgerPageNumber = ledgerPageNumber;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
		this.deleted = deleted;
	}

	public DailyPayCdetailsId getId() {
		return this.id;
	}

	public void setId(DailyPayCdetailsId id) {
		this.id = id;
	}

	public Date getPayCdate() {
		return this.payCdate;
	}

	public void setPayCdate(Date payCdate) {
		this.payCdate = payCdate;
	}

	public String getPayCtype() {
		return this.payCtype;
	}

	public void setPayCtype(String payCtype) {
		this.payCtype = payCtype;
	}

	public Integer getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(Integer supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Long getSupplierBankId() {
		return this.supplierBankId;
	}

	public void setSupplierBankId(Long supplierBankId) {
		this.supplierBankId = supplierBankId;
	}

	public BigDecimal getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Integer getAccountLocationCode() {
		return this.accountLocationCode;
	}

	public void setAccountLocationCode(Integer accountLocationCode) {
		this.accountLocationCode = accountLocationCode;
	}

	public Integer getLedgerPageNumber() {
		return this.ledgerPageNumber;
	}

	public void setLedgerPageNumber(Integer ledgerPageNumber) {
		this.ledgerPageNumber = ledgerPageNumber;
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

	public int getDeleted() {
		return this.deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

}
