package com.honda.galc.dto.lcvinbom;

import java.util.Date;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class BeamPartDto implements IDto {
	private static final long serialVersionUID = 1L;

	private int prototypeOrMassProd;
	private String modelYearCode;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String bpn;
	private String dcpn;
	private Date effectiveBeginDate;
	private Date effectiveEndDate;
	private String matchingDigits;
	private String swClassification;
	private String rxWin;
	private String systemName;
	private String partName;

	public BeamPartDto() {
	}

	public int getPrototypeOrMassProd() {
		return this.prototypeOrMassProd;
	}

	public void setPrototypeOrMassProd(int prototypeOrMassProd) {
		this.prototypeOrMassProd = prototypeOrMassProd;
	}

	public String getModelYearCode() {
		return this.modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return this.modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return this.modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getBpn() {
		return this.bpn;
	}

	public void setBpn(String bpn) {
		this.bpn = bpn;
	}

	public String getDcpn() {
		return this.dcpn;
	}

	public void setDcpn(String dcpn) {
		this.dcpn = dcpn;
	}

	public Date getEffectiveBeginDate() {
		return this.effectiveBeginDate;
	}

	public void setEffectiveBeginDate(Date effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}

	public Date getEffectiveEndDate() {
		return this.effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getMatchingDigits() {
		return this.matchingDigits;
	}

	public void setMatchingDigits(String matchingDigits) {
		this.matchingDigits = matchingDigits;
	}

	public String getSwClassification() {
		return this.swClassification;
	}

	public void setSwClassification(String swClassification) {
		this.swClassification = swClassification;
	}

	public String getRxWin() {
		return this.rxWin;
	}

	public void setRxWin(String rxWin) {
		this.rxWin = rxWin;
	}

	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getPartName() {
		return this.partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bpn == null) ? 0 : bpn.hashCode());
		result = prime * result + ((dcpn == null) ? 0 : dcpn.hashCode());
		result = prime * result + ((effectiveBeginDate == null) ? 0 : effectiveBeginDate.hashCode());
		result = prime * result + ((effectiveEndDate == null) ? 0 : effectiveEndDate.hashCode());
		result = prime * result + ((matchingDigits == null) ? 0 : matchingDigits.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		result = prime * result + prototypeOrMassProd;
		result = prime * result + ((rxWin == null) ? 0 : rxWin.hashCode());
		result = prime * result + ((swClassification == null) ? 0 : swClassification.hashCode());
		result = prime * result + ((systemName == null) ? 0 : systemName.hashCode());
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
		BeamPartDto other = (BeamPartDto) obj;
		if (bpn == null) {
			if (other.bpn != null)
				return false;
		} else if (!bpn.equals(other.bpn))
			return false;
		if (dcpn == null) {
			if (other.dcpn != null)
				return false;
		} else if (!dcpn.equals(other.dcpn))
			return false;
		if (effectiveBeginDate == null) {
			if (other.effectiveBeginDate != null)
				return false;
		} else if (!effectiveBeginDate.equals(other.effectiveBeginDate))
			return false;
		if (effectiveEndDate == null) {
			if (other.effectiveEndDate != null)
				return false;
		} else if (!effectiveEndDate.equals(other.effectiveEndDate))
			return false;
		if (matchingDigits == null) {
			if (other.matchingDigits != null)
				return false;
		} else if (!matchingDigits.equals(other.matchingDigits))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (prototypeOrMassProd != other.prototypeOrMassProd)
			return false;
		if (rxWin == null) {
			if (other.rxWin != null)
				return false;
		} else if (!rxWin.equals(other.rxWin))
			return false;
		if (swClassification == null) {
			if (other.swClassification != null)
				return false;
		} else if (!swClassification.equals(other.swClassification))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		return true;
	}

	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(),
				getPrototypeOrMassProd(), getModelYearCode(), 
				getModelCode(), getModelTypeCode(), getModelOptionCode(), 
				getBpn(), getDcpn(), getEffectiveBeginDate(), 
				getEffectiveEndDate(), getMatchingDigits(), 
				getSwClassification(), getRxWin(), getSystemName(), 
				getPartName());
	}
}
