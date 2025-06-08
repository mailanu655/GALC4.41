package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class SalesReturnsAmtDto{
	private String amtRecordId="S";
	private String amtSpace1;
	private String amtConditionType;
	private String amtSpace2;
	private String amtAmountSign;
	private String amtAmount;
	private String amtSpace3;
	private String amtPriceCurr;
	private String amtSpace4;
	private String amtArGlAcct;
	private String amtSpace5;
	private String amtCostCenter;
	private String amtSpace6;
	private String amtCompCode;
	private String amtSpace7;
	private String amtWbsElement;
	private String amtSpace8;
	private String amtPlant;
	private String amtSpace9;
	public String getAmtRecordId() {
		return amtRecordId;
	}
	public void setAmtRecordId(String amtRecordId) {
		this.amtRecordId = amtRecordId;
	}
	public String getAmtSpace1() {
		return amtSpace1;
	}
	public void setAmtSpace1(String amtSpace1) {
		this.amtSpace1 = amtSpace1;
	}
	public String getAmtConditionType() {
		return amtConditionType;
	}
	public void setAmtConditionType(String amtConditionType) {
		this.amtConditionType = amtConditionType;
	}
	public String getAmtSpace2() {
		return amtSpace2;
	}
	public void setAmtSpace2(String amtSpace2) {
		this.amtSpace2 = amtSpace2;
	}
	public String getAmtAmountSign() {
		return amtAmountSign;
	}
	public void setAmtAmountSign(String amtAmountSign) {
		this.amtAmountSign = amtAmountSign;
	}
	public String getAmtAmount() {
		return amtAmount;
	}
	public void setAmtAmount(String amtAmount) {
		this.amtAmount = amtAmount;
	}
	public String getAmtSpace3() {
		return amtSpace3;
	}
	public void setAmtSpace3(String amtSpace3) {
		this.amtSpace3 = amtSpace3;
	}
	public String getAmtPriceCurr() {
		return amtPriceCurr;
	}
	public void setAmtPriceCurr(String amtPriceCurr) {
		this.amtPriceCurr = amtPriceCurr;
	}
	public String getAmtSpace4() {
		return amtSpace4;
	}
	public void setAmtSpace4(String amtSpace4) {
		this.amtSpace4 = amtSpace4;
	}
	public String getAmtArGlAcct() {
		return amtArGlAcct;
	}
	public void setAmtArGlAcct(String amtArGlAcct) {
		this.amtArGlAcct = amtArGlAcct;
	}
	public String getAmtSpace5() {
		return amtSpace5;
	}
	public void setAmtSpace5(String amtSpace5) {
		this.amtSpace5 = amtSpace5;
	}
	public String getAmtCostCenter() {
		return amtCostCenter;
	}
	public void setAmtCostCenter(String amtCostCenter) {
		this.amtCostCenter = amtCostCenter;
	}
	public String getAmtSpace6() {
		return amtSpace6;
	}
	public void setAmtSpace6(String amtSpace6) {
		this.amtSpace6 = amtSpace6;
	}
	public String getAmtCompCode() {
		return amtCompCode;
	}
	public void setAmtCompCode(String amtCompCode) {
		this.amtCompCode = amtCompCode;
	}
	public String getAmtSpace7() {
		return amtSpace7;
	}
	public void setAmtSpace7(String amtSpace7) {
		this.amtSpace7 = amtSpace7;
	}
	public String getAmtWbsElement() {
		return amtWbsElement;
	}
	public void setAmtWbsElement(String amtWbsElement) {
		this.amtWbsElement = amtWbsElement;
	}
	public String getAmtSpace8() {
		return amtSpace8;
	}
	public void setAmtSpace8(String amtSpace8) {
		this.amtSpace8 = amtSpace8;
	}
	public String getAmtPlant() {
		return amtPlant;
	}
	public void setAmtPlant(String amtPlant) {
		this.amtPlant = amtPlant;
	}
	public String getAmtSpace9() {
		return amtSpace9;
	}
	public void setAmtSpace9(String amtSpace9) {
		this.amtSpace9 = amtSpace9;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amtRecordId == null) ? 0 : amtRecordId.hashCode());
		result = prime * result + ((amtSpace1 == null) ? 0 : amtSpace1.hashCode());
		result = prime * result + ((amtConditionType == null) ? 0 : amtConditionType.hashCode());
		result = prime * result + ((amtSpace2 == null) ? 0 : amtSpace2.hashCode());
		result = prime * result + ((amtAmountSign == null) ? 0 : amtAmountSign.hashCode());
		result = prime * result + ((amtAmount == null) ? 0 : amtAmount.hashCode());
		result = prime * result + ((amtSpace3 == null) ? 0 : amtSpace3.hashCode());
		result = prime * result + ((amtPriceCurr == null) ? 0 : amtPriceCurr.hashCode());
		result = prime * result + ((amtSpace4 == null) ? 0 : amtSpace4.hashCode());
		result = prime * result + ((amtArGlAcct == null) ? 0 : amtArGlAcct.hashCode());
		result = prime * result + ((amtSpace5 == null) ? 0 : amtSpace5.hashCode());
		result = prime * result + ((amtCostCenter == null) ? 0 : amtCostCenter.hashCode());
		result = prime * result + ((amtSpace6 == null) ? 0 : amtSpace6.hashCode());
		result = prime * result + ((amtCompCode == null) ? 0 : amtCompCode.hashCode());
		result = prime * result + ((amtSpace7 == null) ? 0 : amtSpace7.hashCode());
		result = prime * result + ((amtWbsElement == null) ? 0 : amtWbsElement.hashCode());
		result = prime * result + ((amtSpace8 == null) ? 0 : amtSpace8.hashCode());
		result = prime * result + ((amtPlant == null) ? 0 : amtPlant.hashCode());		
		result = prime * result + ((amtSpace9 == null) ? 0 : amtSpace9.hashCode());
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
		SalesReturnsAmtDto other = (SalesReturnsAmtDto) obj;
		if (amtRecordId == null) {
			if (other.amtRecordId != null)
				return false;
		} else if (!amtRecordId.equals(other.amtRecordId))
			return false;
		if (amtSpace1 == null) {
			if (other.amtSpace1 != null)
				return false;
		} else if (!amtSpace1.equals(other.amtSpace1))
			return false;
		if (amtConditionType == null) {
			if (other.amtConditionType != null)
				return false;
		} else if (!amtConditionType.equals(other.amtConditionType))
			return false;
		if (amtSpace2 == null) {
			if (other.amtSpace2 != null)
				return false;
		} else if (!amtSpace2.equals(other.amtSpace2))
			return false;
		if (amtAmountSign == null) {
			if (other.amtAmountSign != null)
				return false;
		} else if (!amtAmountSign.equals(other.amtAmountSign))
			return false;
		if (amtAmount == null) {
			if (other.amtAmount != null)
				return false;
		} else if (!amtAmount.equals(other.amtAmount))
			return false;
		if (amtSpace3 == null) {
			if (other.amtSpace3 != null)
				return false;
		} else if (!amtSpace3.equals(other.amtSpace3))
			return false;
		if (amtPriceCurr == null) {
			if (other.amtPriceCurr != null)
				return false;
		} else if (!amtPriceCurr.equals(other.amtPriceCurr))
			return false;
		if (amtSpace4 == null) {
			if (other.amtSpace4 != null)
				return false;
		} else if (!amtSpace4.equals(other.amtSpace4))
			return false;
		if (amtArGlAcct == null) {
			if (other.amtArGlAcct != null)
				return false;
		} else if (!amtArGlAcct.equals(other.amtArGlAcct))
			return false;
		if (amtSpace5 == null) {
			if (other.amtSpace5 != null)
				return false;
		} else if (!amtSpace5.equals(other.amtSpace5))
			return false;
		if (amtCostCenter == null) {
			if (other.amtCostCenter != null)
				return false;
		} else if (!amtCostCenter.equals(other.amtCostCenter))
			return false;
		if (amtSpace6 == null) {
			if (other.amtSpace6 != null)
				return false;
		} else if (!amtSpace6.equals(other.amtSpace6))
			return false;
		if (amtCompCode == null) {
			if (other.amtCompCode != null)
				return false;
		} else if (!amtCompCode.equals(other.amtCompCode))
			return false;
		if (amtSpace7 == null) {
			if (other.amtSpace7 != null)
				return false;
		} else if (!amtSpace7.equals(other.amtSpace7))
			return false;
		if (amtWbsElement == null) {
			if (other.amtWbsElement != null)
				return false;
		} else if (!amtWbsElement.equals(other.amtWbsElement))
			return false;
		if (amtSpace8 == null) {
			if (other.amtSpace8 != null)
				return false;
		} else if (!amtSpace8.equals(other.amtSpace8))
			return false;
		if (amtPlant == null) {
			if (other.amtPlant != null)
				return false;
		} else if (!amtPlant.equals(other.amtPlant))
			return false;
		if (amtSpace9 == null) {
			if (other.amtSpace9 != null)
				return false;
		} else if (!amtSpace9.equals(other.amtSpace9))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
