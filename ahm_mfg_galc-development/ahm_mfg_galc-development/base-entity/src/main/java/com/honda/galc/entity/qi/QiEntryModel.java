package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiEntryModelVersioningStatus;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * <h3>QiEntryModel Class description</h3>
 * <p>
 * QiEntryModel contains the getter and setter of the entry model properties and maps this class with database table and properties with the database its columns .
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
 *         July 15, 2016
 * 
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
@Entity
@Table(name = "QI_ENTRY_MODEL_TBX")
public class QiEntryModel extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true)
	private QiEntryModelId id;
	@Column(name = "ENTRY_MODEL_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey=true,sequence=2)
	private String entryModelDescription;
	@Column(name = "PRODUCT_TYPE")
	private String productType;
	@Column(name = "ACTIVE")
	@Auditable
	private short active;
	
	public QiEntryModel() {
	}

	public QiEntryModel(QiEntryModelId id, String entryModelDescription, String productType, short active) {
		this.id = id;
		this.entryModelDescription = entryModelDescription;
		this.productType = productType;
		this.active = active;
	}

	public String getEntryModelDescription() {
		return StringUtils.trimToEmpty(this.entryModelDescription);
	}

	public void setEntryModelDescription(String entryModelDescription) {
		this.entryModelDescription = entryModelDescription;
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public short getActive() {
		return active;
	}

	public void setActive(short active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return this.active == (short) 1;
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}

	public void setId(QiEntryModelId id) {
		this.id = id;
	}
	
	public String getIsUsedVersion(){
		return QiEntryModelVersioningStatus.getType(getId().getIsUsed()).getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((entryModelDescription == null) ? 0 : entryModelDescription
						.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
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
		QiEntryModel other = (QiEntryModel) obj;
		if (active != other.active)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (entryModelDescription == null) {
			if (other.entryModelDescription != null)
				return false;
		} else if (!entryModelDescription.equals(other.entryModelDescription))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		return true;
	}
	public QiEntryModelId getId() {
		return this.id;
	}
	
	public String toString() {
		return toString(getId().getEntryModel(),
				getId().getIsUsed(),
				getEntryModelDescription(),
				getProductType(),
				getActive()
				);
	}
}
