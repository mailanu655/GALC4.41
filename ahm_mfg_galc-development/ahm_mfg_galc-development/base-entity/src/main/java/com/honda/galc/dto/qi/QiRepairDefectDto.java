
package com.honda.galc.dto.qi;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>QiRepairDefectDto Class description</h3>
 * <p>
 * QiRepairDefectDto
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
 * Nov. 08, 2019
 * 
 */

public class QiRepairDefectDto implements IDto {

private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName = "CURRENT_DEFECT_STATUS")
	private Short currentDefectStatus;
	
	@DtoTag(outputName = "ASSOCIATE_ID")
	private String associateId;
	
	@DtoTag(outputName = "ENTRY_DEPT")
	private String entryDept;

	@DtoTag(outputName = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_KEY")
	private Long externalSystemKey;

	@DtoTag(outputName = "REPAIR_REASON")
	private String repairReason;
	
	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod;
	
	@DtoTag(outputName = "IS_UPDATE_DEFECT_STATUS")
	private boolean isUpdateDefectStatus = false;
	
	@DtoTag(outputName = "IS_FIX_DUPLICATES")
	private boolean isFixDuplicates;
	
	@DtoTag(outputName = "IS_MAP_EXT_KEY")
	private boolean isMapExtKey;
	
	@DtoTag(outputName = "REPAIR_ID")
	private Long repairId;

	@DtoTag(outputName = "DEFECTRESULTID")
	private Long defectResultId;

	public QiRepairDefectDto() {
		super();
		this.processPointId = "";
		this.currentDefectStatus = 0;
		this.associateId = "";
		this.externalSystemName = "";
		this.externalSystemKey = 0L;
		this.repairReason = "";
		this.repairMethod = "";
		this.isUpdateDefectStatus = false;
		this.isFixDuplicates = true;
		this.isMapExtKey = true;
		this.repairId = 0L;
		this.defectResultId = 0L;
		this.entryDept = "";
	}

	public String getEntryDept() {
		return StringUtils.trimToEmpty(entryDept);
	}
	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}


	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
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

	public boolean isUpdateDefectStatus() {
		return isUpdateDefectStatus;
	}

	public void setUpdateDefectStatus(boolean isUpdateDefectStatus) {
		this.isUpdateDefectStatus = isUpdateDefectStatus;
	}

	public boolean isFixDuplicates() {
		return isFixDuplicates;
	}

	public void setFixDuplicates(boolean isFixDuplicates) {
		this.isFixDuplicates = isFixDuplicates;
	}

	public boolean isMapExtKey() {
		return isMapExtKey;
	}

	public void setMapExtKey(boolean isMapExtKey) {
		this.isMapExtKey = isMapExtKey;
	}

	public Long getRepairId() {
		return repairId;
	}

	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	public Long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(Long defectResultId) {
		this.defectResultId = defectResultId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
