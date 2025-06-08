package com.honda.galc.service.msip.dto.inbound;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date June, 2017
 */

public class Gmm091Dto implements IMsipInboundDto {
	private static final long serialVersionUID = 1L;

	private String plantCd;
	private String modelYear;
	private String modelCd;
	private String devSeqCd;
	private String fifCode;
	private String fifType;
	private String groupCd;
	private Date efctBegDt;	
	private String fifDesc;
	private Date efctEndDt;

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

	public String getFifCode() {
		return StringUtils.trim(fifCode);
	}

	public void setFifCode(String fifCode) {
		this.fifCode = fifCode;
	}

	public String getFifType() {
		return StringUtils.trim(fifType);
	}

	public void setFifType(String fifType) {
		this.fifType = fifType;
	}

	public String getGroupCd() {
		return StringUtils.trim(groupCd);
	}

	public void setGroupCd(String groupCd) {
		this.groupCd = groupCd;
	}

	public Date getEfctBegDt() {
		return efctBegDt;
	}

	public void setEfctBegDt(Date efctBegDt) {
		this.efctBegDt = efctBegDt;
	}

	public String getFifDesc() {
		return StringUtils.trim(fifDesc);
	}

	public void setFifDesc(String fifDesc) {
		this.fifDesc = fifDesc;
	}

	public Date getEfctEndDt() {
		return efctEndDt;
	}

	public void setEfctEndDt(Date efctEndDt) {
		this.efctEndDt = efctEndDt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plantCd == null) ? 0 : plantCd.hashCode());
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((modelCd == null) ? 0 : modelCd.hashCode());
		result = prime * result + ((devSeqCd == null) ? 0 : devSeqCd.hashCode());
		result = prime * result + ((fifCode == null) ? 0 : fifCode.hashCode());
		result = prime * result + ((fifType == null) ? 0 : fifType.hashCode());
		result = prime * result + ((groupCd == null) ? 0 : groupCd.hashCode());
		result = prime * result + ((efctBegDt == null) ? 0 : efctBegDt.hashCode());
		result = prime * result + ((fifDesc == null) ? 0 : fifDesc.hashCode());
		result = prime * result + ((efctEndDt == null) ? 0 : efctEndDt.hashCode());
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
		Gmm091Dto other = (Gmm091Dto) obj;
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
		if (fifCode == null) {
			if (other.fifCode != null)
				return false;
		} else if (!fifCode.equals(other.fifCode))
			return false;
		if (fifType == null) {
			if (other.fifType != null)
				return false;
		} else if (!fifType.equals(other.fifType))
			return false;
		if (groupCd == null) {
			if (other.groupCd != null)
				return false;
		} else if (!groupCd.equals(other.groupCd))
			return false;
		if (efctBegDt == null) {
			if (other.efctBegDt != null)
				return false;
		} else if (!efctBegDt.equals(other.efctBegDt))
			return false;
		if (fifDesc == null) {
			if (other.fifDesc != null)
				return false;
		} else if (!fifDesc.equals(other.fifDesc))
			return false;
		if (efctEndDt == null) {
			if (other.efctEndDt != null)
				return false;
		} else if (!efctEndDt.equals(other.efctEndDt))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}
