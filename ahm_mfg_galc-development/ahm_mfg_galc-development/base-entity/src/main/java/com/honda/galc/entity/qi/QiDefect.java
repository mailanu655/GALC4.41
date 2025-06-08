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
 * <h3>QIDefect Class description</h3>
 * <p> QIDefect description </p>
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
 * @author L&T Infotech<br>
 * April 20, 2016
 *
 *
 */
@Entity
@Table(name = "QI_DEFECT_TBX")
public class QiDefect extends QiAuditEntryTimestamp {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "DEFECT_TYPE_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String defectTypeName;
	
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	@Column(name = "DEFECT_TYPE_DESCRIPTION_SHORT")
	private String defectTypeDescriptionShort;
	
	@Column(name = "DEFECT_TYPE_DESCRIPTION_LONG")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String defectTypeDescriptionLong;
	
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	
	@Column(name = "DEFECT_CATEGORY_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String defectCategoryName;
	
	@Column(name = "PRIMARY_POSITION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private short primaryPosition;
	
	@Column(name = "ACTIVE")
	@Auditable
	private short active;

	public QiDefect() {
		super();
	}
	
	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(this.defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getDefectTypeDescriptionShort() {
		return StringUtils.trimToEmpty(this.defectTypeDescriptionShort);
	}

	public void setDefectTypeDescriptionShort(String defectTypeDescriptionShort) {
		this.defectTypeDescriptionShort = defectTypeDescriptionShort;
	}

	public String getDefectTypeDescriptionLong() {
		return StringUtils.trimToEmpty(this.defectTypeDescriptionLong);
	}

	public void setDefectTypeDescriptionLong(String defectTypeDescriptionLong) {
		this.defectTypeDescriptionLong = defectTypeDescriptionLong;
	}

	public String getProductKind() {
		return StringUtils.trimToEmpty(this.productKind);
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(this.defectCategoryName);
	}

	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}

	public short getPrimaryPositionValue() {
		return primaryPosition;
	}
	
	public void setPrimaryPositionValue(short primaryPosition) {
		this.primaryPosition = primaryPosition;
	}

	public boolean isPrimaryPostion() {
        return this.primaryPosition ==(short) 1;
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
	
	public boolean isActive() {
        return this.active ==(short) 1;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime
				* result
				+ ((defectCategoryName == null) ? 0 : defectCategoryName
						.hashCode());
		result = prime
				* result
				+ ((defectTypeDescriptionLong == null) ? 0
						: defectTypeDescriptionLong.hashCode());
		result = prime
				* result
				+ ((defectTypeDescriptionShort == null) ? 0
						: defectTypeDescriptionShort.hashCode());
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
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
		QiDefect other = (QiDefect) obj;
		if (active != other.active)
			return false;
		if (defectCategoryName == null) {
			if (other.defectCategoryName != null)
				return false;
		} else if (!defectCategoryName.equals(other.defectCategoryName))
			return false;
		if (defectTypeDescriptionLong == null) {
			if (other.defectTypeDescriptionLong != null)
				return false;
		} else if (!defectTypeDescriptionLong
				.equals(other.defectTypeDescriptionLong))
			return false;
		if (defectTypeDescriptionShort == null) {
			if (other.defectTypeDescriptionShort != null)
				return false;
		} else if (!defectTypeDescriptionShort
				.equals(other.defectTypeDescriptionShort))
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
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
		return defectTypeName;
	}

}
