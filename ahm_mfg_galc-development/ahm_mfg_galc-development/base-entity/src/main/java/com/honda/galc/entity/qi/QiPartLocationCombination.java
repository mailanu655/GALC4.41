package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QIPartLocationCombination</code> is an entity class for QI_PART_LOCATION_COMBINATION_TBX table.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>04/05/2016</TD>
 * <TD>0.1.2</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1.2
 * @author L&T Infotech
 */

@Entity
@Table(name = "QI_PART_LOCATION_COMBINATION_TBX")
public class QiPartLocationCombination extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PART_LOCATION_ID")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private Integer partLocationId;

	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	@Column(name = "INSPECTION_PART_NAME")
	private String inspectionPartName;

	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	@Column(name = "INSPECTION_PART_LOCATION_NAME")
	private String inspectionPartLocationName;

	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	@Column(name = "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;

	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	@Column(name = "INSPECTION_PART2_NAME")
	private String inspectionPart2Name;

	@Auditable(isPartOfPrimaryKey = true, sequence = 6)
	@Column(name = "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;

	@Auditable(isPartOfPrimaryKey = true, sequence = 7)
	@Column(name = "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;

	@Auditable(isPartOfPrimaryKey = true, sequence = 8)
	@Column(name = "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;

	@Column(name = "PRODUCT_KIND")
	private String productKind;

	@Auditable
	@Column(name = "ACTIVE")
	private short active;
	
	
	public QiPartLocationCombination() {
		super();
	}

	public Integer getPartLocationId() {
		return partLocationId;
	}

	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = StringUtils.trimToEmpty(inspectionPartName).toUpperCase();
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(this.inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = StringUtils.trimToNull(inspectionPartLocationName);
	}

	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(this.inspectionPartLocation2Name);
	}

	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = StringUtils.trimToNull(inspectionPartLocation2Name);
	}

	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(this.inspectionPart2Name);
	}

	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = StringUtils.trimToNull(inspectionPart2Name);
	}

	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(this.inspectionPart2LocationName);
	}

	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = StringUtils.trimToNull(inspectionPart2LocationName);
	}

	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(this.inspectionPart2Location2Name);
	}

	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = StringUtils.trimToNull(inspectionPart2Location2Name);
	}

	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(this.inspectionPart3Name);
	}

	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = StringUtils.trimToNull(inspectionPart3Name);
	}

	public String getProductKind() {
		return StringUtils.trimToEmpty(this.productKind);
	}

	public void setProductKind(String productKind) {
		this.productKind = StringUtils.trimToEmpty(productKind).toUpperCase();
	}

	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }
	
	public boolean isActive() {
        return this.active ==(short) 1;
    }

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}
	
	public void swapPart12() {
		String tmp;
		
		tmp =inspectionPartName;
		inspectionPartName = inspectionPart2Name;
		inspectionPart2Name = tmp;
		
		tmp =inspectionPartLocationName;
		inspectionPartLocationName = inspectionPart2LocationName;
		inspectionPart2LocationName = tmp;
		
		tmp =inspectionPartLocation2Name;
		inspectionPartLocation2Name = inspectionPart2Location2Name;
		inspectionPart2Location2Name = tmp;
		
	}
	
	public void swapPart23() {
		String tmp;
		
		tmp =inspectionPart2Name;
		inspectionPart2Name = inspectionPart3Name;
		inspectionPart3Name = tmp;
		
	}
	
	public void swapPartLocation12() {
		String tmp;
		tmp =inspectionPartLocationName;
		inspectionPartLocationName = inspectionPartLocation2Name;
		inspectionPartLocation2Name = tmp;
	}
	
	
	public void swapPart2Location12() {
		String tmp;
		tmp =inspectionPart2LocationName;
		inspectionPart2LocationName = inspectionPart2Location2Name;
		inspectionPart2Location2Name = tmp;
	}
	
	public String getFullPartDesc() {
		return StringUtils.trimToEmpty(inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name).replaceAll("null", "").replaceAll("\\s+"," ");
	}
		
	public void clear() {
		setInspectionPartName(StringUtils.EMPTY);
		setInspectionPart2Name(StringUtils.EMPTY);
		setInspectionPart3Name(StringUtils.EMPTY);
		setInspectionPartLocationName(StringUtils.EMPTY);
		setInspectionPart2Location2Name(StringUtils.EMPTY);
		setInspectionPart2LocationName(StringUtils.EMPTY);
		setInspectionPart2Location2Name(StringUtils.EMPTY);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((partLocationId == null) ? 0 : partLocationId.hashCode());
		result = prime
				* result
				+ ((inspectionPart2Location2Name == null) ? 0
						: inspectionPart2Location2Name.hashCode());
		result = prime
				* result
				+ ((inspectionPart2LocationName == null) ? 0
						: inspectionPart2LocationName.hashCode());
		result = prime
				* result
				+ ((inspectionPart2Name == null) ? 0 : inspectionPart2Name
						.hashCode());
		result = prime
				* result
				+ ((inspectionPart3Name == null) ? 0 : inspectionPart3Name
						.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocation2Name == null) ? 0
						: inspectionPartLocation2Name.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocationName == null) ? 0
						: inspectionPartLocationName.hashCode());
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((productKind == null) ? 0 : productKind.hashCode());
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
		QiPartLocationCombination other = (QiPartLocationCombination) obj;
		if (active != other.active)
			return false;
		if (partLocationId == null) {
			if (other.partLocationId != null)
				return false;
		} else if (!partLocationId.equals(other.partLocationId))
			return false;
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null)
				return false;
		} else if (!inspectionPart2Location2Name
				.equals(other.inspectionPart2Location2Name))
			return false;
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null)
				return false;
		} else if (!inspectionPart2LocationName
				.equals(other.inspectionPart2LocationName))
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
		} else if (!inspectionPartLocation2Name
				.equals(other.inspectionPartLocation2Name))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName
				.equals(other.inspectionPartLocationName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		return true;
	}

	public Object getId() {
		return partLocationId;
	}

}
