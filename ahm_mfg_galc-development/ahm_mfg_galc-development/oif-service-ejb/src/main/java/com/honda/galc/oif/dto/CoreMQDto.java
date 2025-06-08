package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;
/**
 * 
 * <h3>CoreMQDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> POJO class to hold the Core MQ(Market Quality) data </p>
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
 * <TR>
 * <TD>Kenneth Gibson</TD>
 * <TD>Jan 9, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Kenneth Gibson
 * @created Jan 9, 2015
 */

public class CoreMQDto implements IOutputFormat{ 
	
	@OutputData(value="VIN")
	private String vin;
	@OutputData(value="REPORTED_DEFECT_ID")
	private String reportedDefectID;
	@OutputData(value="ENTRY_TIMESTAMP")
	private String entryTimestamp;
	@OutputData(value="PART_NAME")
	private String partName;
	@OutputData(value="PART_LOCATION_NAME")
	private String partLocationName;
	@OutputData(value="DEFECT_TYPE_NAME")
	private String defectTypeName;
	@OutputData(value="SECONDARY_PART_NAME")
	private String secondaryPartName;
	@OutputData(value="SECONDARY_PART_LOC")
	private String secondaryPartLoc;
	@OutputData(value="RESPONSIBLE_PLANT")
	private String responsiblePlant;
	@OutputData(value="RESPONSIBLE_DEPARTMENT")
	private String responsibleDepartment;
	@OutputData(value="FILLER")
	private String filler;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getReportedDefectID() {
		return reportedDefectID;
	}

	public void setReportedDefectID(String reportedDefectID) {
		this.reportedDefectID = reportedDefectID;
	}

	public String getEntryTimestamp() {
		return entryTimestamp;
	}

	public void setEntryTimestamp(String entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartLocationName() {
		return partLocationName;
	}

	public void setPartLocationName(String partLocationName) {
		this.partLocationName = partLocationName;
	}

	public String getDefectTypeName() {
		return defectTypeName;
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getSecondaryPartName() {
		return secondaryPartName;
	}

	public void setSecondaryPartName(String secondaryPartName) {
		this.secondaryPartName = secondaryPartName;
	}

	public String getSecondaryPartLoc() {
		return secondaryPartLoc;
	}

	public void setSecondaryPartLoc(String secondaryPartLoc) {
		this.secondaryPartLoc = secondaryPartLoc;
	}

	public String getResponsiblePlant() {
		return responsiblePlant;
	}

	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}

	public String getResponsibleDepartment() {
		return responsibleDepartment;
	}

	public void setResponsibleDepartment(String responsibleDepartment) {
		this.responsibleDepartment = responsibleDepartment;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	

}


