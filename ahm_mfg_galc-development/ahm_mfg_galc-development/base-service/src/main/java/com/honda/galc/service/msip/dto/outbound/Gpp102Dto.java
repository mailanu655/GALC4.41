package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;


/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class Gpp102Dto extends BaseOutboundDto implements IMsipOutboundDto {
	
	private static final long serialVersionUID = 1L;
	
	private String planCode;
	private String lineNo;
	private String processLocation;
	private String onOffFlg;
	private String productionSeqNo;
	private String modelYearCode;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private String extColourCode;
	private String intColourCode;
	private String productingQuantity;
	private String remainingQuantity;
	private String createDate;
	private String createTime;
	private String buildSeqFlag;
	private String buildSeqNo;
	private String filler;
	private String errorMsg;
	private Boolean isError;
	public Boolean getIsError() {
		return isError;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setIsError(Boolean isError) {
		this.isError = isError;
		
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		
	}
	
	public String getVersion() {
		return version;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getOnOffFlg() {
		return onOffFlg;
	}

	public void setOnOffFlg(String onOffFlg) {
		this.onOffFlg = onOffFlg;
	}

	public String getProductionSeqNo() {
		return productionSeqNo;
	}

	public void setProductionSeqNo(String productionSeqNo) {
		this.productionSeqNo = productionSeqNo;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getExtColourCode() {
		return extColourCode;
	}

	public void setExtColourCode(String extColourCode) {
		this.extColourCode = extColourCode;
	}

	public String getIntColourCode() {
		return intColourCode;
	}

	public void setIntColourCode(String intColourCode) {
		this.intColourCode = intColourCode;
	}

	public String getProductingQuantity() {
		return productingQuantity;
	}

	public void setProductingQuantity(String productingQuantity) {
		this.productingQuantity = productingQuantity;
	}

	public String getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(String remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBuildSeqFlag() {
		return buildSeqFlag;
	}

	public void setBuildSeqFlag(String buildSeqFlag) {
		this.buildSeqFlag = buildSeqFlag;
	}

	public String getBuildSeqNo() {
		return buildSeqNo;
	}

	public void setBuildSeqNo(String buildSeqNo) {
		this.buildSeqNo = buildSeqNo;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((onOffFlg == null) ? 0 : onOffFlg.hashCode());
		result = prime * result + ((productionSeqNo == null) ? 0 : productionSeqNo.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((extColourCode == null) ? 0 : extColourCode.hashCode());
		result = prime * result + ((intColourCode == null) ? 0 : intColourCode.hashCode());
		result = prime * result + ((productingQuantity == null) ? 0 : productingQuantity.hashCode());
		result = prime * result + ((remainingQuantity == null) ? 0 : remainingQuantity.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((buildSeqFlag == null) ? 0 : buildSeqFlag.hashCode());
		result = prime * result + ((buildSeqNo == null) ? 0 : buildSeqNo.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());		
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
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
		Gpp102Dto other = (Gpp102Dto) obj;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (onOffFlg == null) {
			if (other.onOffFlg != null)
				return false;
		} else if (!onOffFlg.equals(other.onOffFlg))
			return false;
		if (productionSeqNo == null) {
			if (other.productionSeqNo != null)
				return false;
		} else if (!productionSeqNo.equals(other.productionSeqNo))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (extColourCode == null) {
			if (other.extColourCode != null)
				return false;
		} else if (!extColourCode.equals(other.extColourCode))
			return false;
		if (intColourCode == null) {
			if (other.intColourCode != null)
				return false;
		} else if (!intColourCode.equals(other.intColourCode))
			return false;
		if (productingQuantity == null) {
			if (other.productingQuantity != null)
				return false;
		} else if (!productingQuantity.equals(other.productingQuantity))
			return false;
		if (remainingQuantity == null) {
			if (other.remainingQuantity != null)
				return false;
		} else if (!remainingQuantity.equals(other.remainingQuantity))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (buildSeqFlag == null) {
			if (other.buildSeqFlag != null)
				return false;
		} else if (!buildSeqFlag.equals(other.buildSeqFlag))
			return false;
		if (buildSeqNo == null) {
			if (other.buildSeqNo != null)
				return false;
		} else if (!buildSeqNo.equals(other.buildSeqNo))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}
