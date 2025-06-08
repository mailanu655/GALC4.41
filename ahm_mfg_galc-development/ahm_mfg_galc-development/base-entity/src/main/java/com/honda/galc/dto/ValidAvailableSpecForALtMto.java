package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>ValidAvailableSpecForALtMto Class description</h3>
 * <p>
 * ValidAvailableSpecForALtMto contains the getter and setter of the model Year code and Model
 * code and Model Type code and Engine MTO and Alt Engine MTO for engine Uncommanizaiotn purpose
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author K Maharjan<br>
 *         May 25, 2017
 * 
 */
public class ValidAvailableSpecForALtMto implements IDto {
	private static final long serialVersionUID = 1L;
	@DtoTag(outputName ="MODEL_YEAR_CODE")
	private String modelYearCode;
	@DtoTag(outputName ="MODEL_CODE")
	private String modelCode;
	@DtoTag(outputName ="MODEL_TYPE_CODE")
	private String modelTypeCode;
	@DtoTag(outputName ="ENGINE_MTO")
	private String engineMto;
	@DtoTag(outputName ="ALT_ENGINE_MTO")
	private String altEngineMto;
	
	public String getModelYearCode() {
		 return StringUtils.trim(modelYearCode);
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return StringUtils.trim(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public String getModelTypeCode() {
		return StringUtils.trim(modelTypeCode);
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}	
	
	public String getEngineMto() {
		return  StringUtils.trim(engineMto);
	}

	public void setEngineMto(String engineMto) {
		this.engineMto = engineMto;
	}

	public String getAltEngineMto() {
		return  StringUtils.trim(altEngineMto);
	}

	public void setAltEngineMto(String altEngineMto) {
		this.altEngineMto = altEngineMto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());		
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((engineMto == null) ? 0 : engineMto.hashCode());
		result = prime * result + ((altEngineMto == null) ? 0 : altEngineMto.hashCode());
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
		ValidAvailableSpecForALtMto other = (ValidAvailableSpecForALtMto) obj;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelYearCode == null) {
			if (other.modelYearCode != null)
				return false;
		} else if (!modelYearCode.equals(other.modelYearCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (engineMto == null) {
			if (other.engineMto != null)
				return false;
		} else if (!engineMto.equals(other.engineMto))
			return false;
		if (altEngineMto == null) {
			if (other.altEngineMto != null)
				return false;
		} else if (!altEngineMto.equals(other.altEngineMto))
			return false;
		return true;
	}


}
