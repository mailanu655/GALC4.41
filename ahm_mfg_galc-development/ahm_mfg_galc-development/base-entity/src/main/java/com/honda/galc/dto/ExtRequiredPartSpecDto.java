/**
 * 
 */
package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * @author vf031824
 *
 */
public class ExtRequiredPartSpecDto implements IDto {
	
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String partName;
	
	@DtoTag()
	private String partId;
	
	@DtoTag()
	private String partDescription;
	
	@DtoTag()
	private String partSerialNumberMask;
	
	@DtoTag()
	private int maxAttempts;
	
	@DtoTag()
	private int measurementCount;
	
	@DtoTag()
	private String comment;
	
	@DtoTag()
	private String partMark;
	
	@DtoTag()
	private String partNumber;
	
	@DtoTag()
	private String partGroup;
	
	@DtoTag()
	private String parseStrategy;
	
	@DtoTag(name = "PARSE_INFORMATION")
	private String parserInformation;
	
	@DtoTag()
	private String productSpecCode;
	
	public ExtRequiredPartSpecDto() {}
	
	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartId() {
		return StringUtils.trim(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getPartDescription() {
		return StringUtils.trim(this.partDescription);
	}

	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	public String getPartSerialNumberMask() {
		return StringUtils.trim(this.partSerialNumberMask);
	}

	public void setPartSerialNumberMask(String partMask) {
		this.partSerialNumberMask = partMask;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public int getMeasurementCount() {
		return this.measurementCount;
	}

	public void setMeasurementCount(int measurementCount) {
		this.measurementCount = measurementCount;
	}

	public String getComment() {
		return StringUtils.trim(this.comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPartMark() {
		return StringUtils.trim(this.partMark);
	}

	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}

	public String getPartNumber() {
		return StringUtils.trim(this.partNumber);
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public String getPartGroup() {
		return StringUtils.trim(this.partGroup);
	}
	
	public void setPartGroup(String partGroup) {
		this.partGroup = partGroup;
	}

	public String getParseStrategy() {
		return StringUtils.trim(this.parseStrategy);
	}

	public void setParseStragety(String parseStrategy) {
		this.parseStrategy = parseStrategy;
	}

	public String getParserInformation() {
		return StringUtils.trim(this.parserInformation);
	}

	public void setParserInformation(String parserInformation) {
		this.parserInformation = parserInformation;
	}
	
	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
}