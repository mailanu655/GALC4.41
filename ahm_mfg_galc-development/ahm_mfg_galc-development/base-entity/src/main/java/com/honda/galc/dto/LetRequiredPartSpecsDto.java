/**
 * 
 */
package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.enumtype.LetParamType;

/**
 * @author vf031824
 *
 */
public class LetRequiredPartSpecsDto implements IDto {

	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String partName;
	
	@DtoTag()
	private String partId;
	
	@DtoTag()
	private int sequenceNumber;
	
	@DtoTag()
	private String partDescription;
	
	@DtoTag()
	private String partSerialNumberMask;
	
	@DtoTag()
	private int paramType;
	
	@DtoTag()
	private String inspectionPgmName;
	
	@DtoTag()
	private String inspectionParamName;
	
	public LetRequiredPartSpecsDto() {}
	
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
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public String getDescription() {
		return StringUtils.trim(this.partDescription);
	}
	
	public void setDesctiption(String partDescription) {
		this.partDescription = partDescription;
	}
	
	public int getParamType() {
		return this.paramType;
	}
	
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
	
	public String getPartSerialNumberMask() {
		return StringUtils.trim(this.partSerialNumberMask);
	}
	
	public void setPartSerialNumberMask(String partSerialNumberMask) {
		this.partSerialNumberMask = partSerialNumberMask;
	}
	
	public String getInspectionPgmName() {
		return StringUtils.trim(this.inspectionPgmName);
	}
	
	public void setInspectionPgmName(String inspectionPgmName) {
		this.inspectionPgmName = inspectionPgmName;
	}
	
	public String getInspectionParamName() {
		return StringUtils.trim(this.inspectionParamName);
	}
	
	public void setInspectionParamName(String inspectionParamName) {
		this.inspectionParamName = inspectionParamName;
	}
	
	public LetParamType getLetParamType() {
		return LetParamType.getType(paramType);
	}
}