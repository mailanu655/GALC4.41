package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiFlag;
import com.honda.galc.entity.enumtype.QiPositionType;
/**
 * 
 * <h3>QIInspectionPart Class description</h3>
 * <p>
 * QIInspectionPart contains the getter and setter of the Part properties and maps this class with database table and properties with the database its columns .
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
@Table(name = "QI_INSPECTION_PART_TBX")
public class QiInspectionPart extends QiAuditEntryTimestamp{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "INSPECTION_PART_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String inspectionPartName;
	@Column(name = "INSPECTION_PART_DESC_SHORT")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String inspectionPartDescShort;
	@Column(name = "INSPECTION_PART_DESC_LONG")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String inspectionPartDescLong;
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	@Column(name = "HIERARCHY")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private short hierarchy;
	@Column(name = "PRIMARY_POSITION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private short primaryPosition;
	@Column(name = "ALLOW_MULTIPLE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 6)
	private short allowMultiple;
	@Column(name = "PART_CLASS")
	@Auditable(isPartOfPrimaryKey = true, sequence = 7)
	private String partClass;
	@Column(name = "ACTIVE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 8)
	private short active;
	
	
	public QiInspectionPart() {
		super();
	}
	
	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPartDescShort() {
		return StringUtils.trimToEmpty(this.inspectionPartDescShort);
	}
	public void setInspectionPartDescShort(String inspectionPartDescShort) {
		this.inspectionPartDescShort = inspectionPartDescShort;
	}

	public String getInspectionPartDescLong() {
		return StringUtils.trimToEmpty(this.inspectionPartDescLong);
	}

	public void setInspectionPartDescLong(String inspectionPartDescLong) {
		this.inspectionPartDescLong = inspectionPartDescLong;
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
	
	public short getPrimaryPositionValue() {
		return primaryPosition;
	}
	
	public void setPrimaryPositionValue(short primaryPosition) {
		this.primaryPosition = primaryPosition;
	}

	public void setPrimaryPosition(boolean primaryPosition) {
		this.primaryPosition =(short)( primaryPosition ? 1 : 0);
	}

	public boolean isPrimaryPosition() {
		return this.primaryPosition >=1;
	}
	
	public short getAllowMultipleValue() {
		return allowMultiple;
	}
	public void setAllowMultipleValue(short allowMultiple) {
		this.allowMultiple = allowMultiple;
	}
	
	public boolean isAllowMultiple() {
		return this.allowMultiple>=1;
	}
	
	public void setAllowMultiple(boolean allowMultiple) {
		this.allowMultiple =(short)( allowMultiple ? 1 : 0);
	}
	
	public boolean isAllowMultiple(QiInspectionPart inspectionPart) {
		return !(inspectionPart != null && 
		       inspectionPart.getInspectionPartName().equals(getInspectionPartName()) &&
		       !inspectionPart.isAllowMultiple());
	}
	
	public boolean isHigherHierarchy(QiInspectionPart inspectionPart) {
		return !(inspectionPart != null && 
			     inspectionPart.isPrimaryPosition()) &&
			     getHierarchy() > inspectionPart.getHierarchy();
	}

	public String getPartClass() {
		return StringUtils.trimToEmpty(this.partClass);
	}

	public void setPartClass(String partClass) {
		this.partClass = partClass;
	}
	
	
	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }
	
	public boolean isActive() {
		return this.active>=1;
	}

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}
	
	public String getPosition() {
		return QiPositionType.getType(primaryPosition).getName();
	}

	public String getAllowMltpl() {
		return QiFlag.getType(allowMultiple).getName();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + allowMultiple;
		result = prime * result + hierarchy;
		result = prime
				* result
				+ ((inspectionPartDescLong == null) ? 0
						: inspectionPartDescLong.hashCode());
		result = prime
				* result
				+ ((inspectionPartDescShort == null) ? 0
						: inspectionPartDescShort.hashCode());
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((partClass == null) ? 0 : partClass.hashCode());
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
		QiInspectionPart other = (QiInspectionPart) obj;
		if (active != other.active)
			return false;
		if (allowMultiple != other.allowMultiple)
			return false;
		if (hierarchy != other.hierarchy)
			return false;
		if (inspectionPartDescLong == null) {
			if (other.inspectionPartDescLong != null)
				return false;
		} else if (!inspectionPartDescLong.equals(other.inspectionPartDescLong))
			return false;
		if (inspectionPartDescShort == null) {
			if (other.inspectionPartDescShort != null)
				return false;
		} else if (!inspectionPartDescShort
				.equals(other.inspectionPartDescShort))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (partClass == null) {
			if (other.partClass != null)
				return false;
		} else if (!partClass.equals(other.partClass))
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
	public Object getId() {
		return getInspectionPartName();
	}
	public boolean hasLowerHierarchy(QiInspectionPart otherPart)
	{
		return this.getHierarchy() < otherPart.getHierarchy();
	}
}
