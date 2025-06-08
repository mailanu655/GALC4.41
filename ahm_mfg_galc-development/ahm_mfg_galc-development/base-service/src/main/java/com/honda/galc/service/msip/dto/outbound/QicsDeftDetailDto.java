package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.service.msip.util.OutputData;
import com.honda.galc.util.ToStringUtil;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class QicsDeftDetailDto extends BaseOutboundDto implements IMsipOutboundDto {
	private static final long serialVersionUID = 1L;
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
	private String errorMsg;
	private Boolean isError;
	public Boolean getIsError() {
		return isError;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setIsError(Boolean isError) {
		this.isError = isError;
		
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		
	}
	
	public String getVersion() {
		return version;
	}	

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
	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		result = prime * result + ((reportedDefectID == null) ? 0 : reportedDefectID.hashCode());
		result = prime * result + ((entryTimestamp == null) ? 0 : entryTimestamp.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		result = prime * result + ((partLocationName == null) ? 0 : partLocationName.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((secondaryPartName == null) ? 0 : secondaryPartName.hashCode());
		result = prime * result + ((secondaryPartLoc == null) ? 0 : secondaryPartLoc.hashCode());
		result = prime * result + ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result + ((responsibleDepartment == null) ? 0 : responsibleDepartment.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());		
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
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
		QicsDeftDetailDto other = (QicsDeftDetailDto) obj;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		if (reportedDefectID == null) {
			if (other.reportedDefectID != null)
				return false;
		} else if (!reportedDefectID.equals(other.reportedDefectID))
			return false;
		if (entryTimestamp == null) {
			if (other.entryTimestamp != null)
				return false;
		} else if (!entryTimestamp.equals(other.entryTimestamp))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (partLocationName == null) {
			if (other.partLocationName != null)
				return false;
		} else if (!partLocationName.equals(other.partLocationName))
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (secondaryPartName == null) {
			if (other.secondaryPartName != null)
				return false;
		} else if (!secondaryPartName.equals(other.secondaryPartName))
			return false;
		if (secondaryPartLoc == null) {
			if (other.secondaryPartLoc != null)
				return false;
		} else if (!secondaryPartLoc.equals(other.secondaryPartLoc))
			return false;
		if (responsiblePlant == null) {
			if (other.responsiblePlant != null)
				return false;
		} else if (!responsiblePlant.equals(other.responsiblePlant))
			return false;
		if (responsibleDepartment == null) {
			if (other.responsibleDepartment != null)
				return false;
		} else if (!responsibleDepartment.equals(other.responsibleDepartment))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
