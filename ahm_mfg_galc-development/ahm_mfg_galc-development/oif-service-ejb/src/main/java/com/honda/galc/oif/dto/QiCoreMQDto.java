package com.honda.galc.oif.dto;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.util.OutputData;

/**
 * 
 * <h3>QiCoreMQDTO.java</h3>
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
 * <TD>vcc01417</TD>
 * <TD>April 7, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author vcc01417
 * @created April 7, 2017
 */

public class QiCoreMQDto implements IOutputFormat{ 
	
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
		return StringUtils.trimToEmpty(vin);
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getReportedDefectID() {
		return StringUtils.trimToEmpty(reportedDefectID);
	}

	public void setReportedDefectID(String reportedDefectID) {
		this.reportedDefectID = reportedDefectID;
	}

	public String getEntryTimestamp() {
		return StringUtils.trimToEmpty(entryTimestamp);
	}

	public void setEntryTimestamp(String entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}

	public String getPartName() {
		return StringUtils.trimToEmpty(partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartLocationName() {
		return StringUtils.trimToEmpty(partLocationName);
	}

	public void setPartLocationName(String partLocationName) {
		this.partLocationName = partLocationName;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getResponsiblePlant() {
		return StringUtils.trimToEmpty(responsiblePlant);
	}

	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}

	public String getResponsibleDepartment() {
		return StringUtils.trimToEmpty(responsibleDepartment);
	}

	public void setResponsibleDepartment(String responsibleDepartment) {
		this.responsibleDepartment = responsibleDepartment;
	}

	public String getSecondaryPartName() {
		return StringUtils.trimToEmpty(secondaryPartName);
	}

	public void setSecondaryPartName(String secondaryPartName) {
		this.secondaryPartName = secondaryPartName;
	}

	public String getSecondaryPartLoc() {
		return StringUtils.trimToEmpty(secondaryPartLoc);
	}

	public void setSecondaryPartLoc(String secondaryPartLoc) {
		this.secondaryPartLoc = secondaryPartLoc;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}
	

	@Override
	public String toString() {
		return "QiCoreMQDto [vin=" + vin + ", reportedDefectID=" + reportedDefectID + ", entryTimestamp="
				+ entryTimestamp + ", partName=" + partName + ", partLocationName=" + partLocationName
				+ ", defectTypeName=" + defectTypeName + ", secondaryPartName=" + secondaryPartName
				+ ", secondaryPartLoc=" + secondaryPartLoc + ", responsiblePlant=" + responsiblePlant
				+ ", responsibleDepartment=" + responsibleDepartment + ", filler=" + filler + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((entryTimestamp == null) ? 0 : entryTimestamp.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((partLocationName == null) ? 0 : partLocationName.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		result = prime * result + ((reportedDefectID == null) ? 0 : reportedDefectID.hashCode());
		result = prime * result + ((responsibleDepartment == null) ? 0 : responsibleDepartment.hashCode());
		result = prime * result + ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result + ((secondaryPartLoc == null) ? 0 : secondaryPartLoc.hashCode());
		result = prime * result + ((secondaryPartName == null) ? 0 : secondaryPartName.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
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
		QiCoreMQDto other = (QiCoreMQDto) obj;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (entryTimestamp == null) {
			if (other.entryTimestamp != null)
				return false;
		} else if (!entryTimestamp.equals(other.entryTimestamp))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (partLocationName == null) {
			if (other.partLocationName != null)
				return false;
		} else if (!partLocationName.equals(other.partLocationName))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (reportedDefectID == null) {
			if (other.reportedDefectID != null)
				return false;
		} else if (!reportedDefectID.equals(other.reportedDefectID))
			return false;
		if (responsibleDepartment == null) {
			if (other.responsibleDepartment != null)
				return false;
		} else if (!responsibleDepartment.equals(other.responsibleDepartment))
			return false;
		if (responsiblePlant == null) {
			if (other.responsiblePlant != null)
				return false;
		} else if (!responsiblePlant.equals(other.responsiblePlant))
			return false;
		if (secondaryPartLoc == null) {
			if (other.secondaryPartLoc != null)
				return false;
		} else if (!secondaryPartLoc.equals(other.secondaryPartLoc))
			return false;
		if (secondaryPartName == null) {
			if (other.secondaryPartName != null)
				return false;
		} else if (!secondaryPartName.equals(other.secondaryPartName))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		return true;
	}

	

}
