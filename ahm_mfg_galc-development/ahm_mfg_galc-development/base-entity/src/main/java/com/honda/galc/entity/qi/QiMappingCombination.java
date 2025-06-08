package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiMappingCombination Class description</h3>
 * <p>
 * QiMappingCombination
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
@Table(name = "QI_MAPPING_COMBINATION_TBX")
public class QiMappingCombination extends AuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiMappingCombinationId id;
	
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

	@Column(name = "OLD_INSPECTION_PART_NAME")
	private String oldInspectionPartName;
	
    @Column(name = "OLD_INSPECTION_PART_LOCATION_NAME")
    private String oldInspectionPartLocationName;
    
	@Column(name = "OLD_INSPECTION_PART_LOCATION2_NAME")
	private String oldInspectionPartLocation2Name;
	
	@Column(name = "OLD_INSPECTION_PART2_NAME")
	private String oldInspectionPart2Name;
	
	@Column(name = "OLD_INSPECTION_PART2_LOCATION_NAME")
	private String oldInspectionPart2LocationName;
	
	@Column(name = "OLD_INSPECTION_PART2_LOCATION2_NAME")
	private String oldInspectionPart2Location2Name;
	
	@Column(name = "OLD_INSPECTION_PART3_NAME")
	private String oldInspectionPart3Name;
	
	@Column(name = "OLD_DEFECT_TYPE_NAME")
	private String oldDefectTypeName;
	
	@Column(name = "OLD_DEFECT_TYPE_NAME2")
	private String oldDefectTypeName2;
	
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	
	@Column(name = "OLD_COMBINATION_CODE")
	private Integer oldCombinationCode; 
	
	
	public String getPartDefectDesc() {
		return StringUtils.trimToEmpty(inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name+" " +
				   defectTypeName+" " +
				   defectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	/**
	 * @return the inspectionPartName
	 */
	public String getNaqNameOfPart() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}
	
	/**
	 * @return the inspectionPartName
	 */
	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}

	/**
	 * @param inspectionPartName the inspectionPartName to set
	 */
	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	/**
	 * @return the inspectionPartLocationName
	 */
	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(inspectionPartLocationName);
	}

	/**
	 * @param inspectionPartLocationName the inspectionPartLocationName to set
	 */
	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	/**
	 * @return the inspectionPartLocation2Name
	 */
	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(inspectionPartLocation2Name);
	}

	/**
	 * @param inspectionPartLocation2Name the inspectionPartLocation2Name to set
	 */
	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}

	/**
	 * @return the inspectionPart2Name
	 */
	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Name);
	}

	/**
	 * @param inspectionPart2Name the inspectionPart2Name to set
	 */
	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}

	/**
	 * @return the inspectionPart2LocationName
	 */
	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(inspectionPart2LocationName);
	}

	/**
	 * @param inspectionPart2LocationName the inspectionPart2LocationName to set
	 */
	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}

	/**
	 * @return the inspectionPart2Location2Name
	 */
	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Location2Name);
	}

	/**
	 * @param inspectionPart2Location2Name the inspectionPart2Location2Name to set
	 */
	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}

	/**
	 * @return the inspectionPart3Name
	 */
	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(inspectionPart3Name);
	}

	/**
	 * @param inspectionPart3Name the inspectionPart3Name to set
	 */
	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}

	/**
	 * @return the defectTypeName
	 */
	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	/**
	 * @param defectTypeName the defectTypeName to set
	 */
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	/**
	 * @return the defectTypeName2
	 */
	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(defectTypeName2);
	}

	/**
	 * @param defectTypeName2 the defectTypeName2 to set
	 */
	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	

	/**
	 * @return the id
	 */
	public QiMappingCombinationId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(QiMappingCombinationId id) {
		this.id = id;
	}

	/**
	 * @return the oldInspectionPartName
	 */
	public String getOldInspectionPartName() {
		return StringUtils.trimToEmpty(oldInspectionPartName);
	}

	/**
	 * @param oldInspectionPartName the oldInspectionPartName to set
	 */
	public void setOldInspectionPartName(String oldInspectionPartName) {
		this.oldInspectionPartName = oldInspectionPartName;
	}

	/**
	 * @return the oldInspectionPartLocationName
	 */
	public String getOldInspectionPartLocationName() {
		return StringUtils.trimToEmpty(oldInspectionPartLocationName);
	}

	/**
	 * @param oldInspectionPartLocationName the oldInspectionPartLocationName to set
	 */
	public void setOldInspectionPartLocationName(String oldInspectionPartLocationName) {
		this.oldInspectionPartLocationName = oldInspectionPartLocationName;
	}

	/**
	 * @return the oldInspectionPartLocation2Name
	 */
	public String getOldInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(oldInspectionPartLocation2Name);
	}

	/**
	 * @param oldInspectionPartLocation2Name the oldInspectionPartLocation2Name to set
	 */
	public void setOldInspectionPartLocation2Name(String oldInspectionPartLocation2Name) {
		this.oldInspectionPartLocation2Name = oldInspectionPartLocation2Name;
	}

	/**
	 * @return the oldInspectionPart2Name
	 */
	public String getOldInspectionPart2Name() {
		return StringUtils.trimToEmpty(oldInspectionPart2Name);
	}

	/**
	 * @param oldInspectionPart2Name the oldInspectionPart2Name to set
	 */
	public void setOldInspectionPart2Name(String oldInspectionPart2Name) {
		this.oldInspectionPart2Name = oldInspectionPart2Name;
	}

	/**
	 * @return the oldInspectionPart2LocationName
	 */
	public String getOldInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(oldInspectionPart2LocationName);
	}

	/**
	 * @param oldInspectionPart2LocationName the oldInspectionPart2LocationName to set
	 */
	public void setOldInspectionPart2LocationName(String oldInspectionPart2LocationName) {
		this.oldInspectionPart2LocationName = oldInspectionPart2LocationName;
	}

	/**
	 * @return the oldInspectionPart2Location2Name
	 */
	public String getOldInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(oldInspectionPart2Location2Name);
	}

	/**
	 * @param oldInspectionPart2Location2Name the oldInspectionPart2Location2Name to set
	 */
	public void setOldInspectionPart2Location2Name(String oldInspectionPart2Location2Name) {
		this.oldInspectionPart2Location2Name = oldInspectionPart2Location2Name;
	}

	/**
	 * @return the oldInspectionPart3Name
	 */
	public String getOldInspectionPart3Name() {
		return StringUtils.trimToEmpty(oldInspectionPart3Name);
	}

	/**
	 * @param oldInspectionPart3Name the oldInspectionPart3Name to set
	 */
	public void setOldInspectionPart3Name(String oldInspectionPart3Name) {
		this.oldInspectionPart3Name = oldInspectionPart3Name;
	}

	/**
	 * @return the oldDefectTypeName
	 */
	public String getOldDefectTypeName() {
		return StringUtils.trimToEmpty(oldDefectTypeName);
	}

	/**
	 * @param oldDefectTypeName the oldDefectTypeName to set
	 */
	public void setOldDefectTypeName(String oldDefectTypeName) {
		this.oldDefectTypeName = oldDefectTypeName;
	}

	/**
	 * @return the oldDefectTypeName2
	 */
	public String getOldDefectTypeName2() {
		return StringUtils.trimToEmpty(oldDefectTypeName2);
	}

	/**
	 * @param oldDefectTypeName2 the oldDefectTypeName2 to set
	 */
	public void setOldDefectTypeName2(String oldDefectTypeName2) {
		this.oldDefectTypeName2 = oldDefectTypeName2;
	}

	/**
	 * @return the productKind
	 */
	public String getProductKind() {
		return StringUtils.trimToEmpty(productKind);
	}

	/**
	 * @param productKind the productKind to set
	 */
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}
	
	/**
	 * @return the oldCombinationCode
	 */
	public Integer getOldCombinationCode() {
		return oldCombinationCode;
	}

	/**
	 * @param oldCombinationCode the oldCombinationCode to set
	 */
	public void setOldCombinationCode(Integer oldCombinationCode) {
		this.oldCombinationCode = oldCombinationCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((oldCombinationCode == null) ? 0 : oldCombinationCode.hashCode());
		result = prime * result + ((oldDefectTypeName == null) ? 0 : oldDefectTypeName.hashCode());
		result = prime * result + ((oldDefectTypeName2 == null) ? 0 : oldDefectTypeName2.hashCode());
		result = prime * result
				+ ((oldInspectionPart2Location2Name == null) ? 0 : oldInspectionPart2Location2Name.hashCode());
		result = prime * result
				+ ((oldInspectionPart2LocationName == null) ? 0 : oldInspectionPart2LocationName.hashCode());
		result = prime * result + ((oldInspectionPart2Name == null) ? 0 : oldInspectionPart2Name.hashCode());
		result = prime * result + ((oldInspectionPart3Name == null) ? 0 : oldInspectionPart3Name.hashCode());
		result = prime * result
				+ ((oldInspectionPartLocation2Name == null) ? 0 : oldInspectionPartLocation2Name.hashCode());
		result = prime * result
				+ ((oldInspectionPartLocationName == null) ? 0 : oldInspectionPartLocationName.hashCode());
		result = prime * result + ((oldInspectionPartName == null) ? 0 : oldInspectionPartName.hashCode());
		result = prime * result + ((productKind == null) ? 0 : productKind.hashCode());
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
		QiMappingCombination other = (QiMappingCombination) obj;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (oldCombinationCode == null) {
			if (other.oldCombinationCode != null)
				return false;
		} else if (!oldCombinationCode.equals(other.oldCombinationCode))
			return false;
		if (oldDefectTypeName == null) {
			if (other.oldDefectTypeName != null)
				return false;
		} else if (!oldDefectTypeName.equals(other.oldDefectTypeName))
			return false;
		if (oldDefectTypeName2 == null) {
			if (other.oldDefectTypeName2 != null)
				return false;
		} else if (!oldDefectTypeName2.equals(other.oldDefectTypeName2))
			return false;
		if (oldInspectionPart2Location2Name == null) {
			if (other.oldInspectionPart2Location2Name != null)
				return false;
		} else if (!oldInspectionPart2Location2Name.equals(other.oldInspectionPart2Location2Name))
			return false;
		if (oldInspectionPart2LocationName == null) {
			if (other.oldInspectionPart2LocationName != null)
				return false;
		} else if (!oldInspectionPart2LocationName.equals(other.oldInspectionPart2LocationName))
			return false;
		if (oldInspectionPart2Name == null) {
			if (other.oldInspectionPart2Name != null)
				return false;
		} else if (!oldInspectionPart2Name.equals(other.oldInspectionPart2Name))
			return false;
		if (oldInspectionPart3Name == null) {
			if (other.oldInspectionPart3Name != null)
				return false;
		} else if (!oldInspectionPart3Name.equals(other.oldInspectionPart3Name))
			return false;
		if (oldInspectionPartLocation2Name == null) {
			if (other.oldInspectionPartLocation2Name != null)
				return false;
		} else if (!oldInspectionPartLocation2Name.equals(other.oldInspectionPartLocation2Name))
			return false;
		if (oldInspectionPartLocationName == null) {
			if (other.oldInspectionPartLocationName != null)
				return false;
		} else if (!oldInspectionPartLocationName.equals(other.oldInspectionPartLocationName))
			return false;
		if (oldInspectionPartName == null) {
			if (other.oldInspectionPartName != null)
				return false;
		} else if (!oldInspectionPartName.equals(other.oldInspectionPartName))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		return true;
	}

}
