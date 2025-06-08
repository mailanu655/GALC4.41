package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>MtcToModelGroupDto Class description</h3>
 * <p>
 * MtcToModelGroupDto contains the getter and setter of the model Year code and Model
 * code and Mtc model maps this class with database table and properties with the database its columns .
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
 * @author LnTInfotech<br>
 *         May 17, 2017
 * 
 */
public class MtcToModelGroupDto implements IDto {
	private static final long serialVersionUID = 1L;
	@DtoTag(outputName ="MODEL_YEAR_CODE")
	private String modelYearCode;
	@DtoTag(outputName ="MODEL_CODE")
	private String modelCode;
	@DtoTag(outputName ="MTC_CODE")
	private String mtcModel;
	@DtoTag(outputName ="CLASS_NO")
	private String classNo;
	@DtoTag(outputName ="MODEL_YEAR_DESCRIPTION")
	private String modelYearDescription;
	@DtoTag(outputName ="MODEL_DESCRIPTION")
	private String modelDescription;
	@DtoTag(outputName ="SHORT_MODEL_DESCRIPTION")
	private String shortModelDescription;
	private String availableMtcModels;
	public String getClassNo() {
		 return StringUtils.trim(classNo);
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}
	
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
	
	public String getMtcModel() {
		return StringUtils.trim(mtcModel);
	}

	public void setMtcModelForPane(String modelYearCode, String modelCode) {
		this.mtcModel = (StringUtils.trimToEmpty(modelYearCode).concat(StringUtils.trimToEmpty(modelCode))) ;
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}
	
	public String getAvailablelMtcModels() {
		availableMtcModels = (StringUtils.trimToEmpty(modelYearCode).concat(StringUtils.trimToEmpty(modelCode)));
		return availableMtcModels;
	}
	public String getModelYearDescription() {
		return StringUtils.trimToEmpty(modelYearDescription);
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getModelDescription() {
		return StringUtils.trimToEmpty(modelDescription);
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getShortModelDescription() {
		return StringUtils.trimToEmpty(shortModelDescription);
	}

	public void setShortModelDescription(String shortModelDescription) {
		this.shortModelDescription = shortModelDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availableMtcModels == null) ? 0 : availableMtcModels.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelYearCode == null) ? 0 : modelYearCode.hashCode());
		result = prime * result + ((mtcModel == null) ? 0 : mtcModel.hashCode());
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
		MtcToModelGroupDto other = (MtcToModelGroupDto) obj;
		if (availableMtcModels == null) {
			if (other.availableMtcModels != null)
				return false;
		} else if (!availableMtcModels.equals(other.availableMtcModels))
			return false;
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
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		return true;
	}


}
