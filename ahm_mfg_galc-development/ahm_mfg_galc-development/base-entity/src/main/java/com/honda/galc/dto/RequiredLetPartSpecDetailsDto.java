/**
 * 
 */
package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * @author VF031824
 *
 */
public class RequiredLetPartSpecDetailsDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag()
	private String partName;
	
	@DtoTag()
	private String partId;
	
	@DtoTag()
	private String productSpecCode;
	
	@DtoTag()
	private int inspectionProgramId;

	@DtoTag()
	private int inspectionParamId;
	
	@DtoTag()
	private int sequenceNumber;
	
	@DtoTag()
	private String productType;
	
	public RequiredLetPartSpecDetailsDto() {}
	
	
	public String getPartName() {
		return StringUtils.trim(partName);
	}
	
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getPartID() {
		return StringUtils.trim(this.partId);
	}
	
	public void setPartId(String partId) {
		this.partId = partId;
	}
	
	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public int getInspectionProgramId() {
		return this.inspectionProgramId;
	}
	
	public void setInspectionProgramId(int inspectionProgramId) {
		this.inspectionProgramId = inspectionProgramId;
	}
	
	public int getInspectionParamId() {
		return this.inspectionParamId;
	}
	
	public void setInspectionParamId(int inspectionParamId) {
		this.inspectionParamId = inspectionParamId;
	}
	
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public String getProductType() {
		return StringUtils.trim(this.productType);
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
}