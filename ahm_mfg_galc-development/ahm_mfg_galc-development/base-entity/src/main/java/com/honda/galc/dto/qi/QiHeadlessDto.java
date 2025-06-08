package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

public class QiHeadlessDto implements IDto {

	private static final long serialVersionUID = 1L;


	@DtoTag(outputName = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;
	
	@DtoTag(outputName = "EXTERNAL_PART_CODE")
	private String externalPartCode;
	
	@DtoTag(outputName = "EXTERNAL_DEFECT_CODE")
	private String externalDefectCode;
	
	
	@DtoTag(outputName = "REGIONAL_DEFECT_COMBINATION_ID")
	private Integer regionalDefectCombinationId;
	
	
	public QiHeadlessDto() {
	}


	public String getExternalSystemName() {
		return StringUtils.trimToEmpty(externalSystemName);
	}


	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}


	public String getExternalPartCode() {
		return StringUtils.trimToEmpty(externalPartCode);
	}


	public void setExternalPartCode(String externalPartCode) {
		this.externalPartCode = externalPartCode;
	}


	public String getExternalDefectCode() {
		return StringUtils.trimToEmpty(externalDefectCode);
	}


	public void setExternalDefectCode(String externalDefectCode) {
		this.externalDefectCode = externalDefectCode;
	}


	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}


	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}


	
}
