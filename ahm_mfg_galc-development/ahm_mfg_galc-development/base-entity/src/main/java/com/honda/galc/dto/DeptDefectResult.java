package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>DeptDefectResult Class description</h3>
 * <p> DeptDefectResult description </p>
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
 * @author Jeffray Huang<br>
 * Nov 22, 2012
 *
 *
 */
public class DeptDefectResult implements IDto{
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag()
	private String divisionId;
	@DtoTag()
	private String shift;
	@DtoTag()
	private String inspectionPartName;
	@DtoTag()
	private String defectTypeName;
	@DtoTag()
	private int rejectionCount;
	
	public String getDivisionId() {
		return StringUtils.trim(divisionId);
	}
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	public String getShift() {
		return StringUtils.trim(shift);
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getInspectionPartName() {
		return StringUtils.trim(inspectionPartName);
	}
	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}
	public String getDefectTypeName() {
		return StringUtils.trim(defectTypeName);
	}
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}
	public int getRejectionCount() {
		return rejectionCount;
	}
	public void setRejectionCount(int rejectionCount) {
		this.rejectionCount = rejectionCount;
	}
	
	
}
