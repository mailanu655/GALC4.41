package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

public class QiDefectDescriptionDto {
	@DtoTag(name = "RESPONSIBLE_DEPT")
	private String responsibleDept;
	
	@DtoTag(name = "INSPECTION_PART_NAME")
	private String inspectionPartName;
	
	@DtoTag(name = "INSPECTION_PART_LOCATION_NAME")
	private String inspectionPartLocationName;
	
	@DtoTag(name = "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;
	
	@DtoTag(name = "INSPECTION_PART2_NAME")
	private String inspectionPart2Name;
	
	@DtoTag(name = "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;
	
	@DtoTag(name = "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;
	
	@DtoTag(name = "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;
	
	@DtoTag(name = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@DtoTag(name = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	
	@DtoTag(name = "RESPONSIBLE_LEVEL1")
	private String responsibleLevel1;
	
	public String getResponsibleLevel1() {
		return responsibleLevel1;
	}

	public void setResponsibleLevel1(String responsibleLevel1) {
		this.responsibleLevel1 = responsibleLevel1;
	}

	public String getResponsibleDept() {
		return this.responsibleDept;
	}
	
	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public String getInspectionPartName() {
		return inspectionPartName;
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = StringUtils.trim(inspectionPartName);
	}

	public String getInspectionPartLocationName() {
		return inspectionPartLocationName;
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = StringUtils.trim(inspectionPartLocationName);
	}

	public String getInspectionPartLocation2Name() {
		return inspectionPartLocation2Name;
	}

	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = StringUtils.trim(inspectionPartLocation2Name);
	}

	public String getInspectionPart2Name() {
		return inspectionPart2Name;
	}

	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = StringUtils.trim(inspectionPart2Name);
	}

	public String getInspectionPart2LocationName() {
		return inspectionPart2LocationName;
	}

	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = StringUtils.trim(inspectionPart2LocationName);
	}

	public String getInspectionPart2Location2Name() {
		return inspectionPart2Location2Name;
	}

	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = StringUtils.trim(inspectionPart2Location2Name);
	}

	public String getInspectionPart3Name() {
		return inspectionPart3Name;
	}

	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = StringUtils.trim(inspectionPart3Name);
	}

	public String getDefectTypeName() {
		return defectTypeName;
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = StringUtils.trim(defectTypeName);
	}

	public String getDefectTypeName2() {
		return defectTypeName2;
	}

	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = StringUtils.trim(defectTypeName2);
	}

	public String  getDisplayNameLong() {
		return StringUtils.trimToEmpty(
				responsibleDept + " " +
				responsibleLevel1 + " " +
				inspectionPartName + " " +
				inspectionPartLocationName + " " +
				inspectionPartLocation2Name + " " +
				inspectionPart2Name + " " +
				inspectionPart2LocationName + " " +
				inspectionPart2Location2Name + " " +
				inspectionPart3Name + " " +
				defectTypeName + " " +
				defectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	public String getDisplayNameShort() {
		return StringUtils.trimToEmpty(
				responsibleDept + " " +
				responsibleLevel1 + " " +
				inspectionPart2Name + " " +
				inspectionPart2LocationName + " " +
				inspectionPart3Name + " " + 
				defectTypeName).replaceAll("null", "").replaceAll("\\s+"," ");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((responsibleDept == null) ? 0 : responsibleDept.hashCode());
		result = prime * result + ((responsibleLevel1 == null) ? 0 : responsibleLevel1.hashCode());
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
		QiDefectDescriptionDto other = (QiDefectDescriptionDto) obj;
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
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null)
				return false;
		} else if (!inspectionPart2Location2Name.equals(other.inspectionPart2Location2Name))
			return false;
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null)
				return false;
		} else if (!inspectionPart2LocationName.equals(other.inspectionPart2LocationName))
			return false;
		if (inspectionPart2Name == null) {
			if (other.inspectionPart2Name != null)
				return false;
		} else if (!inspectionPart2Name.equals(other.inspectionPart2Name))
			return false;
		if (inspectionPart3Name == null) {
			if (other.inspectionPart3Name != null)
				return false;
		} else if (!inspectionPart3Name.equals(other.inspectionPart3Name))
			return false;
		if (inspectionPartLocation2Name == null) {
			if (other.inspectionPartLocation2Name != null)
				return false;
		} else if (!inspectionPartLocation2Name.equals(other.inspectionPartLocation2Name))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName.equals(other.inspectionPartLocationName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (responsibleDept == null) {
			if (other.responsibleDept != null)
				return false;
		} else if (!responsibleDept.equals(other.responsibleDept))
			return false;
		if (responsibleLevel1 == null) {
			if (other.responsibleLevel1 != null)
				return false;
		} else if (!responsibleLevel1.equals(other.responsibleLevel1))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiDefectDescriptionDto [responsibleDept=" + responsibleDept + ", responsibleLevel1=" + responsibleLevel1
				+ ", inspectionPartName=" + inspectionPartName + ", inspectionPartLocationName="
				+ inspectionPartLocationName + ", inspectionPartLocation2Name=" + inspectionPartLocation2Name
				+ ", inspectionPart2Name=" + inspectionPart2Name + ", inspectionPart2LocationName="
				+ inspectionPart2LocationName + ", inspectionPart2Location2Name=" + inspectionPart2Location2Name
				+ ", inspectionPart3Name=" + inspectionPart3Name + ", defectTypeName=" + defectTypeName
				+ ", defectTypeName2=" + defectTypeName2 + "]";
	}
}
