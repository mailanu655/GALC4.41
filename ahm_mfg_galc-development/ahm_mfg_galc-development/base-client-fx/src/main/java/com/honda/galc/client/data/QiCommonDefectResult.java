package com.honda.galc.client.data;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.qi.QiDefectResult;

public class QiCommonDefectResult {
	
	private String inspectionPartName;
	private String inspectionPart2Name;
	private String inspectionPart3Name;
	private String inspectionPartLocationName;
	private String inspectionPartLocation2Name;
	private String inspectionPart2LocationName;
	private String inspectionPart2Location2Name;
	private String defectTypeName;
	private String defectTypeName2;
	private String defectCatagoryName;
	private short originalDefectStatus;
	private short currentDefectStatus;
	private String applicationId;
	private String entryDept;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public QiCommonDefectResult() {}
	
	public QiCommonDefectResult(QiDefectResult defectResult) {
		this.inspectionPartName = defectResult.getInspectionPartName();
		this.inspectionPart2Name = defectResult.getInspectionPart2Name();
		this.inspectionPart3Name = defectResult.getInspectionPart3Name();
		this.inspectionPartLocationName = defectResult.getInspectionPartLocationName();
		this.inspectionPartLocation2Name = defectResult.getInspectionPartLocation2Name();
		this.inspectionPart2LocationName = defectResult.getInspectionPart2LocationName();
		this.inspectionPart2Location2Name = defectResult.getInspectionPart2Location2Name();
		this.defectTypeName = defectResult.getDefectTypeName();
		this.defectTypeName2 = defectResult.getDefectTypeName2();
		this.originalDefectStatus = defectResult.getOriginalDefectStatus();
		this.currentDefectStatus = defectResult.getCurrentDefectStatus();
		this.applicationId = defectResult.getApplicationId();
		this.entryDept = defectResult.getEntryDept();
	}

	public QiCommonDefectResult(QiRepairResultDto defectResult) {
		this.inspectionPartName = defectResult.getInspectionPartName();
		this.inspectionPart2Name = defectResult.getInspectionPart2Name();
		this.inspectionPart3Name = defectResult.getInspectionPart3Name();
		this.inspectionPartLocationName = defectResult.getInspectionPartLocationName();
		this.inspectionPartLocation2Name = defectResult.getInspectionPartLocation2Name();
		this.inspectionPart2LocationName = defectResult.getInspectionPart2LocationName();
		this.inspectionPart2Location2Name = defectResult.getInspectionPart2Location2Name();
		this.defectTypeName = defectResult.getDefectTypeName();
		this.defectTypeName2 = defectResult.getDefectTypeName2();
		this.originalDefectStatus = defectResult.getOriginalDefectStatus();
		this.currentDefectStatus = defectResult.getCurrentDefectStatus();
		this.applicationId = defectResult.getApplicationId();
		this.entryDept = defectResult.getEntryDept();
	}

	public String getInspectionPartName() {
		return StringUtils.trim(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPart2Name() {
		return StringUtils.trim(this.inspectionPart2Name);
	}

	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}

	public String getInspectionPart3Name() {
		return StringUtils.trim(this.inspectionPart3Name);
	}

	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trim(this.inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getInspectionPartLocation2Name() {
		return StringUtils.trim(this.inspectionPartLocation2Name);
	}

	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}

	public String getInspectionPart2LocationName() {
		return StringUtils.trim(this.inspectionPart2LocationName);
	}

	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}

	public String getInspectionPart2Location2Name() {
		return StringUtils.trim(this.inspectionPart2Location2Name);
	}

	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}

	public String getDefectTypeName() {
		return StringUtils.trim(this.defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getDefectTypeName2() {
		return StringUtils.trim(this.defectTypeName2);
	}

	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	public String getDefectCatagoryName() {
		return StringUtils.trim(this.defectCatagoryName);
	}

	public void setDefectCatagoryName(String defectCatagoryName) {
		this.defectCatagoryName = defectCatagoryName;
	}

	public short getOriginalDefectStatus() {
		return this.originalDefectStatus;
	}

	public void setOriginalDefectStatus(short originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}

	public short getCurrentDefectStatus() {
		return this.currentDefectStatus;
	}

	public void setCurrentDefectStatus(short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}
	
	public String getEntryDept() {
		return entryDept;
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defectCatagoryName == null) ? 0 : defectCatagoryName.hashCode());
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
		result = prime * result + originalDefectStatus;
		result = prime * result + currentDefectStatus;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((entryDept == null) ? 0 : entryDept.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		QiCommonDefectResult other = (QiCommonDefectResult) obj;
		if (defectCatagoryName == null) {
			if (other.defectCatagoryName != null) {
				return false;
			}
		} else if (!defectCatagoryName.equals(other.defectCatagoryName)) {
			return false;
		}
		if (defectTypeName == null) {
			if (other.defectTypeName != null) {
				return false;
			}
		} else if (!defectTypeName.equals(other.defectTypeName)) {
			return false;
		}
		if (defectTypeName2 == null) {
			if (other.defectTypeName2 != null) {
				return false;
			}
		} else if (!defectTypeName2.equals(other.defectTypeName2)) {
			return false;
		}
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null) {
				return false;
			}
		} else if (!inspectionPart2Location2Name.equals(other.inspectionPart2Location2Name)) {
			return false;
		}
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null) {
				return false;
			}
		} else if (!inspectionPart2LocationName.equals(other.inspectionPart2LocationName)) {
			return false;
		}
		if (inspectionPart2Name == null) {
			if (other.inspectionPart2Name != null) {
				return false;
			}
		} else if (!inspectionPart2Name.equals(other.inspectionPart2Name)) {
			return false;
		}
		if (inspectionPart3Name == null) {
			if (other.inspectionPart3Name != null) {
				return false;
			}
		} else if (!inspectionPart3Name.equals(other.inspectionPart3Name)) {
			return false;
		}
		if (inspectionPartLocation2Name == null) {
			if (other.inspectionPartLocation2Name != null) {
				return false;
			}
		} else if (!inspectionPartLocation2Name.equals(other.inspectionPartLocation2Name)) {
			return false;
		}
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null) {
				return false;
			}
		} else if (!inspectionPartLocationName.equals(other.inspectionPartLocationName)) {
			return false;
		}
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null) {
				return false;
			}
		} else if (!inspectionPartName.equals(other.inspectionPartName)) {
			return false;
		}
		if (originalDefectStatus != other.originalDefectStatus) {
			return false;
		}
		if(currentDefectStatus != other.currentDefectStatus) {
			return false;
		}
		if (applicationId == null) {
			if (other.applicationId != null) {
				return false;
			}
		} else if (!applicationId.equals(other.applicationId)) {
			return false;
		}
		if (entryDept == null) {
			if (other.entryDept != null) {
				return false;
			}
		} else if (!entryDept.equals(other.entryDept)) {
			return false;
		}
		return true;
	}
	


}