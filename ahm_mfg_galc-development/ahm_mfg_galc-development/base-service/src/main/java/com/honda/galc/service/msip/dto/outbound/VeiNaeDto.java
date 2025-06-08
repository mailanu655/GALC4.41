package com.honda.galc.service.msip.dto.outbound;

import java.sql.Timestamp;


/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */

public class VeiNaeDto extends BaseOutboundDto implements IMsipOutboundDto {
	private static final long serialVersionUID = 1L;
	private String engineId;
	private String modelYear;
	private String modelCode;
	private String modelTypeCode;
	private String modelOptionCode;
	private Timestamp timeStamp;
	private String prefix;
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

	public String getEngineId() {
		return engineId;
	}

	public void setEngineId(String engineId) {
		this.engineId = engineId;
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

	public Timestamp getTimestamp() {
		return timeStamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timeStamp = timestamp;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelYear() {
		return modelYear;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("ValidEinDTO:");
		result.append("\nengineId: " + engineId);
		result.append("\nmodelYear: " + modelYear);
		result.append("\nmodelCode: " + modelCode);
		result.append("\nmodelTypeCode: " + modelTypeCode);
		result.append("\nmodelOptionCode: " + modelOptionCode);
		result.append("\nprefix: " + prefix);
		result.append("\ntimestamp: " + timeStamp);
		return result.toString();
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
		result = prime * result + ((engineId == null) ? 0 : engineId.hashCode());
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
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
		VeiNaeDto other = (VeiNaeDto) obj;
		if (engineId == null) {
			if (other.engineId != null)
				return false;
		} else if (!engineId.equals(other.engineId))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
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
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
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

}
