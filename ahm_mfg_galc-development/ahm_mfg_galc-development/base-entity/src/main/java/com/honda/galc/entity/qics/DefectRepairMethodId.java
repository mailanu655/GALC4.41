package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

 /** * *
 * @version 1
 * @author Gangadhararao Gadde
 * @since Aug 02, 2013
 */

@Embeddable
public class DefectRepairMethodId implements  Serializable {

    @Column(name = "MODEL_CODE")
    private String modelCode;

    @Column(name = "DEFECT_TYPE_NAME")
    private String defectTypeName;

    @Column(name = "INSPECTION_PART_NAME")
    private String inspectionPartName;

    @Column(name = "SECONDARY_PART_NAME")
    private String secondaryPartName;

    @Column(name = "REPAIR_METHOD_NAME")
    private String repairMethodName;


    private static final long serialVersionUID = 1L;

	public DefectRepairMethodId() {
		super();
	}

	public DefectRepairMethodId(String modelCode, String defectTypeName,
			String inspectionPartName, String secondaryPartName,
			String repairMethodName) {
		super();
		this.modelCode = modelCode;
		this.defectTypeName = defectTypeName;
		this.inspectionPartName = inspectionPartName;
		this.secondaryPartName = secondaryPartName;
		this.repairMethodName = repairMethodName;
	}

	public String getModelCode() {
		return StringUtils.trim(this.modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getDefectTypeName() {
		return StringUtils.trim(this.defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getInspectionPartName() {
		return StringUtils.trim(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getSecondaryPartName() {
		return StringUtils.trim(this.secondaryPartName);
	}

	public void setSecondaryPartName(String secondaryPartName) {
		this.secondaryPartName = secondaryPartName;
	}

	public String getRepairMethodName() {
		return StringUtils.trim(this.repairMethodName);
	}

	public void setRepairMethodName(String repairMethodName) {
		this.repairMethodName = repairMethodName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime
				* result
				+ ((repairMethodName == null) ? 0 : repairMethodName.hashCode());
		result = prime
				* result
				+ ((secondaryPartName == null) ? 0 : secondaryPartName
						.hashCode());
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
		DefectRepairMethodId other = (DefectRepairMethodId) obj;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (repairMethodName == null) {
			if (other.repairMethodName != null)
				return false;
		} else if (!repairMethodName.equals(other.repairMethodName))
			return false;
		if (secondaryPartName == null) {
			if (other.secondaryPartName != null)
				return false;
		} else if (!secondaryPartName.equals(other.secondaryPartName))
			return false;
		return true;
	}


}
