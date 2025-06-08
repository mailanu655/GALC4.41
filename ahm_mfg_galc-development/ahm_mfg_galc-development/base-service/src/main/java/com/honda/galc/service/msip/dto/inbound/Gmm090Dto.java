package com.honda.galc.service.msip.dto.inbound;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */

public class Gmm090Dto implements IMsipInboundDto {
	private static final long serialVersionUID = 1L;
	private String plantCd;	
	private String modelYear;	
	private String modelCd;	
	private String devSeqCd;	
	private String baseType;	
	private String fifType;	
	private String groupCode;	
	private Date efctBegDt;		
	private String groupDesc;	
	private Date efctEndDt;	
	private String required;	
	private int fifLength;	
	private int fifOffset;

	public String getPlantCd() {
		return StringUtils.trim(plantCd);
	}

	public void setPlantCd(String plantCd) {
		this.plantCd = plantCd;
	}

	public String getModelYear() {
		return StringUtils.trim(modelYear);
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelCd() {
		return StringUtils.trim(modelCd);
	}

	public void setModelCd(String modelCd) {
		this.modelCd = modelCd;
	}

	public String getDevSeqCd() {
		return StringUtils.trim(devSeqCd);
	}

	public void setDevSeqCd(String devSeqCd) {
		this.devSeqCd = devSeqCd;
	}

	public String getBaseType() {
		return StringUtils.trim(baseType);
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public String getFifType() {
		return StringUtils.trim(fifType);
	}

	public void setFifType(String fifType) {
		this.fifType = fifType;
	}

	public String getGroupCode() {
		return StringUtils.trim(groupCode);
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Date getEfctBegDt() {
		return efctBegDt;
	}

	public void setEfctBegDt(Date efctBegDt) {
		this.efctBegDt = efctBegDt;
	}

	public String getGroupDesc() {
		return StringUtils.trim(groupDesc);
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public Date getEfctEndDt() {
		return efctEndDt;
	}

	public void setEfctEndDt(Date efctEndDt) {
		this.efctEndDt = efctEndDt;
	}

	public String getRequired() {
		return StringUtils.trim(required);
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public int getFifLength() {
		return fifLength;
	}

	public void setFifLength(int fifLength) {
		this.fifLength = fifLength;
	}

	public int getFifOffset() {
		return fifOffset;
	}

	public void setFifOffset(int fifOffset) {
		this.fifOffset = fifOffset;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plantCd == null) ? 0 : plantCd.hashCode());
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((modelCd == null) ? 0 : modelCd.hashCode());
		result = prime * result + ((devSeqCd == null) ? 0 : devSeqCd.hashCode());
		result = prime * result + ((baseType == null) ? 0 : baseType.hashCode());
		result = prime * result + ((fifType == null) ? 0 : fifType.hashCode());
		result = prime * result + ((groupCode == null) ? 0 : groupCode.hashCode());
		result = prime * result + ((efctBegDt == null) ? 0 : efctBegDt.hashCode());
		result = prime * result + ((groupDesc == null) ? 0 : groupDesc.hashCode());
		result = prime * result + ((efctEndDt == null) ? 0 : efctEndDt.hashCode());
		result = prime * result + ((required == null) ? 0 : required.hashCode());
		result = prime * result + fifLength;
		result = prime * result + fifOffset;
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
		Gmm090Dto other = (Gmm090Dto) obj;
		if (plantCd == null) {
			if (other.plantCd != null)
				return false;
		} else if (!plantCd.equals(other.plantCd))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (modelCd == null) {
			if (other.modelCd != null)
				return false;
		} else if (!modelCd.equals(other.modelCd))
			return false;
		if (devSeqCd == null) {
			if (other.devSeqCd != null)
				return false;
		} else if (!devSeqCd.equals(other.devSeqCd))
			return false;
		if (baseType == null) {
			if (other.baseType != null)
				return false;
		} else if (!baseType.equals(other.baseType))
			return false;
		if (fifType == null) {
			if (other.fifType != null)
				return false;
		} else if (!fifType.equals(other.fifType))
			return false;
		if (groupCode == null) {
			if (other.groupCode != null)
				return false;
		} else if (!groupCode.equals(other.groupCode))
			return false;
		if (efctBegDt == null) {
			if (other.efctBegDt != null)
				return false;
		} else if (!efctBegDt.equals(other.efctBegDt))
			return false;
		if (groupDesc == null) {
			if (other.groupDesc != null)
				return false;
		} else if (!groupDesc.equals(other.groupDesc))
			return false;
		if (efctEndDt == null) {
			if (other.efctEndDt != null)
				return false;
		} else if (!efctEndDt.equals(other.efctEndDt))
			return false;
		if (required == null) {
			if (other.required != null)
				return false;
		} else if (!required.equals(other.required))
			return false;
		if (fifLength != other.fifLength) 
			return false;
		if (fifOffset != other.fifOffset) 
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}
