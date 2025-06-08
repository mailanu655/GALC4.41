
package com.honda.galc.dto.rest;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>RepairDefectDto Class description</h3>
 * <p>
 * RepairDefectDto
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
 * @author vcc44349<br>
 * Nov. 14, 2019
 * 
 */

public class RepairDefectDto implements IDto {

private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "CURRENT_DEFECT_STATUS")
	private Short currentDefectStatus;
	
	@DtoTag(outputName = "ASSOCIATE_ID")
	private String associateId;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_KEY")
	private Long externalSystemKey;

	@DtoTag(outputName = "REPAIR_REASON")
	private String repairReason;
	
	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod;
	
	@DtoTag(outputName = "PRODUCT_TYPE")
	private String productType;
	
	public RepairDefectDto() {
		super();
		this.currentDefectStatus = 0;
		this.associateId = "";
		this.externalSystemName = "";
		this.externalSystemKey = 0L;
		this.repairReason = "";
		this.repairMethod = "";
		this.productType = "";
	}

	public Short getCurrentDefectStatus() {
		return currentDefectStatus;
	}

	public void setCurrentDefectStatus(Short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}

	public String getAssociateId() {
		return associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getExternalSystemName() {
		return externalSystemName;
	}

	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	public Long getExternalSystemKey() {
		return externalSystemKey;
	}

	public void setExternalSystemKey(Long externalSystemKey) {
		this.externalSystemKey = externalSystemKey;
	}

	public String getRepairReason() {
		return repairReason;
	}

	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}

	public String getRepairMethod() {
		return repairMethod;
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
