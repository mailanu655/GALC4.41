package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiPositionType;
/**
 * 
 * <h3>QiInspectionLocation Class description</h3>
 * <p>
 * QiInspectionLocation contains the getter and setter of the Location properties and maps this class with database table and properties with the database its columns .
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
 *         April 21, 2016
 * 
 */

@Entity
@Table(name = "QI_INSPECTION_LOCATION_TBX")
public class QiInspectionLocation extends QiAuditEntryTimestamp {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "INSPECTION_PART_LOCATION_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String inspectionPartLocationName;
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	@Column(name = "INSPECTION_PART_LOC_DESC_SHORT")
	private String inspectionPartLocDescShort;
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	@Column(name = "INSPECTION_PART_LOC_DESC_LONG")
	private String inspectionPartLocDescLong;
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	@Column(name = "HIERARCHY")
	private short hierarchy;
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	@Column(name = "PRIMARY_POSITION")
	private short primaryPosition;
	@Auditable
	@Column(name = "ACTIVE")
	private short active;

	public QiInspectionLocation() {
		super();
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(this.inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getInspectionPartLocDescShort() {
		return StringUtils.trimToEmpty(this.inspectionPartLocDescShort);
	}

	public void setInspectionPartLocDescShort(String inspectionPartLocDescShort) {
		this.inspectionPartLocDescShort = inspectionPartLocDescShort;
	}

	public String getInspectionPartLocDescLong() {
		return StringUtils.trimToEmpty(this.inspectionPartLocDescLong);
	}

	public void setInspectionPartLocDescLong(String inspectionPartLocDescLong) {
		this.inspectionPartLocDescLong = inspectionPartLocDescLong;
	}

	public String getProductKind() {
		return StringUtils.trimToEmpty(this.productKind);
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public short getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(short hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public boolean hasLowerHierarchy(QiInspectionLocation otherLocation)
	{
		return this.getHierarchy() < otherLocation.getHierarchy();
	}
	
	public short getPrimaryPositionValue() {
		return primaryPosition;
	}

	public void setPrimaryPositionValue(short primaryPosition) {
		this.primaryPosition = primaryPosition;
	}

	public void setPrimaryPosition(boolean primaryPosition) {
		this.primaryPosition =(short)( primaryPosition ? 1 : 0);
	}

	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}

	public Object getId() {
		return getInspectionPartLocationName();
	}
	
	public boolean isActive() {
	    return this.active ==(short)1;
	}
	
	public boolean isPrimaryPosition() {
	     return this.primaryPosition == 1;
	}
	
	public boolean isHigherHierarchy(QiInspectionLocation inspectionPartLoc) {
		return !(inspectionPartLoc != null && 
				inspectionPartLoc.isPrimaryPosition()) &&
			     getHierarchy() > inspectionPartLoc.getHierarchy();
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}

	public String getPosition() {
		return QiPositionType.getType(primaryPosition).getName();
	}

 	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + hierarchy;
		result = prime
				* result
				+ ((inspectionPartLocDescLong == null) ? 0
						: inspectionPartLocDescLong.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocDescShort == null) ? 0
						: inspectionPartLocDescShort.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName
						.hashCode());
		result = prime * result + primaryPosition;
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
		QiInspectionLocation other = (QiInspectionLocation) obj;
		if (active != other.active)
			return false;
		if (hierarchy != other.hierarchy)
			return false;
		if (inspectionPartLocDescLong == null) {
			if (other.inspectionPartLocDescLong != null)
				return false;
		} else if (!inspectionPartLocDescLong.equals(other.inspectionPartLocDescLong))
			return false;
		if (inspectionPartLocDescShort == null) {
			if (other.inspectionPartLocDescShort != null)
				return false;
		} else if (!inspectionPartLocDescShort
				.equals(other.inspectionPartLocDescShort))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName.equals(other.inspectionPartLocationName))
			return false;
		if (primaryPosition != other.primaryPosition)
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		return true;
	}
}
