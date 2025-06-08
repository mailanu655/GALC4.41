package com.honda.galc.dto.qi;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class QiRegionalPartDefectLocationDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer regionalDefectCombinationId;
	private Integer partLocationId;
	private String defectTypeName;
	private String defectTypeName2;
	private String inspectionPartName;
	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}
	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}
	public Integer getPartLocationId() {
		return partLocationId;
	}
	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}
	public String getDefectTypeName() {
		return defectTypeName;
	}
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}
	public String getDefectTypeName2() {
		return defectTypeName2;
	}
	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}
	public String getInspectionPartName() {
		return inspectionPartName;
	}
	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result
				+ ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((partLocationId == null) ? 0 : partLocationId.hashCode());
		result = prime
				* result
				+ ((regionalDefectCombinationId == null) ? 0
						: regionalDefectCombinationId.hashCode());
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
		QiRegionalPartDefectLocationDto other = (QiRegionalPartDefectLocationDto) obj;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (defectTypeName2 == null) {
			if (other.defectTypeName2 != null)
				return false;
		} else if (!defectTypeName2.equals(other.defectTypeName2))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (partLocationId == null) {
			if (other.partLocationId != null)
				return false;
		} else if (!partLocationId.equals(other.partLocationId))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId
				.equals(other.regionalDefectCombinationId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getInspectionPartName() +" "+ getDefectTypeName() +" "+ getDefectTypeName2();
	}
	
	public String toStringNoNulls() {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.isBlank(getInspectionPartName()) ? "" :  getInspectionPartName().trim()).append(" ")
			.append(StringUtils.isBlank(getDefectTypeName()) ? "" :  getDefectTypeName().trim()).append(" ")
			.append(StringUtils.isBlank(getDefectTypeName2()) ? "" :  getDefectTypeName2().trim());
		return sb.toString();
	}

}
