package com.svnitsai.gm.database.generated;

// Generated Dec 13, 2014 2:45:34 PM by Hibernate Tools 4.0.0

/**
 * CustomerBanksId generated by hbm2java
 */
public class CustomerBanksId implements java.io.Serializable {

	private int custCode;
	private int custBankId;

	public CustomerBanksId() {
	}

	public CustomerBanksId(int custCode, int custBankId) {
		this.custCode = custCode;
		this.custBankId = custBankId;
	}

	public int getCustCode() {
		return this.custCode;
	}

	public void setCustCode(int custCode) {
		this.custCode = custCode;
	}

	public int getCustBankId() {
		return this.custBankId;
	}

	public void setCustBankId(int custBankId) {
		this.custBankId = custBankId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CustomerBanksId))
			return false;
		CustomerBanksId castOther = (CustomerBanksId) other;

		return (this.getCustCode() == castOther.getCustCode())
				&& (this.getCustBankId() == castOther.getCustBankId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCustCode();
		result = 37 * result + this.getCustBankId();
		return result;
	}

}
