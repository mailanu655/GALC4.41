package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiDefectResult Class description</h3>
 * <p>
 * QiDefectResult
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
 * @author LnTInfotech<br>
 *        May 14, 2017
 * 
 */
@Entity
@Table(name = "QI_OLD_COMBINATION_TBX")
public class QiOldCombination extends AuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMBINATION", nullable=false)
	private String combination; 
	
	@Column(name = "INSPECTION_PART_NAME", nullable=false)
	private String inspectionPartName;
	
    @Column(name = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;
    
	@Column(name = "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;
	
	@Column(name = "INSPECTION_PART2_NAME")
	private String inspectionPart2Name;
	
	@Column(name = "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;
	
	@Column(name = "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;
	
	@Column(name = "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;
	
	@Column(name = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@Column(name = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	
	@Column(name = "COMBINATION_CODE")
	private Integer combinationCode; 
		
	
	public Object getId() {
		return getCombination();
	}


	public String getCombination() {
		return StringUtils.trimToEmpty(combination);
	}


	public void setCombination(String combination) {
		this.combination = combination;
	}


	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}


	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}


	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(inspectionPartLocationName);
	}


	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}


	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(inspectionPartLocation2Name);
	}


	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}


	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Name);
	}


	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}


	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(inspectionPart2LocationName);
	}


	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}


	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Location2Name);
	}


	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}


	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(inspectionPart3Name);
	}


	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}


	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}


	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}


	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(defectTypeName2);
	}


	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}


	public Integer getCombinationCode() {
		return combinationCode;
	}


	public void setCombinationCode(Integer combinationCode) {
		this.combinationCode = combinationCode;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((combination == null) ? 0 : combination.hashCode());
		result = prime * result + ((combinationCode == null) ? 0 : combinationCode.hashCode());
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
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiOldCombination other = (QiOldCombination) obj;
		if (combination == null) {
			if (other.combination != null)
				return false;
		} else if (!combination.equals(other.combination))
			return false;
		if (combinationCode == null) {
			if (other.combinationCode != null)
				return false;
		} else if (!combinationCode.equals(other.combinationCode))
			return false;
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
		return true;
	}

}
