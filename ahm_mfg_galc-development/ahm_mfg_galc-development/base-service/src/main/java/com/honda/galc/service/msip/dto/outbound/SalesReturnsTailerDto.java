package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class SalesReturnsTailerDto{
	private String tRecordId="T";
	private String tSpace1;
	private String tTotInvoice;
	private String tSpace3;
	private String tTotAmount;
	private String tSpace4;
	private String tCurrency;
	private String tSpace5;
	private String tLineCount;
	private String tSpace6;
	public String gettRecordId() {
		return tRecordId;
	}
	public void settRecordId(String tRecordId) {
		this.tRecordId = tRecordId;
	}
	public String gettSpace1() {
		return tSpace1;
	}
	public void settSpace1(String tSpace1) {
		this.tSpace1 = tSpace1;
	}
	public String gettTotInvoice() {
		return tTotInvoice;
	}
	public void settTotInvoice(String tTotInvoice) {
		this.tTotInvoice = tTotInvoice;
	}
	public String gettSpace3() {
		return tSpace3;
	}
	public void settSpace3(String tSpace3) {
		this.tSpace3 = tSpace3;
	}
	public String gettTotAmount() {
		return tTotAmount;
	}
	public void settTotAmount(String tTotAmount) {
		this.tTotAmount = tTotAmount;
	}
	public String gettSpace4() {
		return tSpace4;
	}
	public void settSpace4(String tSpace4) {
		this.tSpace4 = tSpace4;
	}
	public String gettCurrency() {
		return tCurrency;
	}
	public void settCurrency(String tCurrency) {
		this.tCurrency = tCurrency;
	}
	public String gettSpace5() {
		return tSpace5;
	}
	public void settSpace5(String tSpace5) {
		this.tSpace5 = tSpace5;
	}
	public String gettLineCount() {
		return tLineCount;
	}
	public void settLineCount(String tLineCount) {
		this.tLineCount = tLineCount;
	}
	public String gettSpace6() {
		return tSpace6;
	}
	public void settSpace6(String tSpace6) {
		this.tSpace6 = tSpace6;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tRecordId == null) ? 0 : tRecordId.hashCode());
		result = prime * result + ((tSpace1 == null) ? 0 : tSpace1.hashCode());
		result = prime * result + ((tTotInvoice == null) ? 0 : tTotInvoice.hashCode());
		result = prime * result + ((tSpace3 == null) ? 0 : tSpace3.hashCode());
		result = prime * result + ((tTotAmount == null) ? 0 : tTotAmount.hashCode());
		result = prime * result + ((tSpace4 == null) ? 0 : tSpace4.hashCode());
		result = prime * result + ((tCurrency == null) ? 0 : tCurrency.hashCode());
		result = prime * result + ((tSpace5 == null) ? 0 : tSpace5.hashCode());
		result = prime * result + ((tLineCount == null) ? 0 : tLineCount.hashCode());
		result = prime * result + ((tSpace6 == null) ? 0 : tSpace6.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesReturnsTailerDto other = (SalesReturnsTailerDto) obj;
		if (tRecordId == null) {
			if (other.tRecordId != null)
				return false;
		} else if (!tRecordId.equals(other.tRecordId))
			return false;
		if (tSpace1 == null) {
			if (other.tSpace1 != null)
				return false;
		} else if (!tSpace1.equals(other.tSpace1))
			return false;
		if (tTotInvoice == null) {
			if (other.tTotInvoice != null)
				return false;
		} else if (!tTotInvoice.equals(other.tTotInvoice))
			return false;
		if (tSpace3 == null) {
			if (other.tSpace3 != null)
				return false;
		} else if (!tSpace3.equals(other.tSpace3))
			return false;
		if (tTotAmount == null) {
			if (other.tTotAmount != null)
				return false;
		} else if (!tTotAmount.equals(other.tTotAmount))
			return false;
		if (tSpace4 == null) {
			if (other.tSpace4 != null)
				return false;
		} else if (!tSpace4.equals(other.tSpace4))
			return false;
		if (tCurrency == null) {
			if (other.tCurrency != null)
				return false;
		} else if (!tCurrency.equals(other.tCurrency))
			return false;
		if (tSpace5 == null) {
			if (other.tSpace5 != null)
				return false;
		} else if (!tSpace5.equals(other.tSpace5))
			return false;
		if (tLineCount == null) {
			if (other.tLineCount != null)
				return false;
		} else if (!tLineCount.equals(other.tLineCount))
			return false;
		if (tSpace6 == null) {
			if (other.tSpace6 != null)
				return false;
		} else if (!tSpace6.equals(other.tSpace6))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
