package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;
/**
 * 
 * <h3>ModelGroup Class description</h3>
 * <p>
 * ModelGroup contains the getter and setter of the model group properties and maps this class with database table and properties with the database its columns .
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
 *         March 31, 2017
 * 
 */
@Entity
@Table(name = "MODEL_GROUP_TBX")
public class ModelGroup extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private ModelGroupId id;
	
	@Column(name = "MODEL_GROUP_DESCRIPTION")
    @Auditable(isPartOfPrimaryKey = false, sequence = 2)
	private String modelGroupDescription;
	
	@Column(name = "PRODUCT_TYPE")
    @Auditable(isPartOfPrimaryKey = false, sequence = 3)
	private String productType;

	@Column(name = "ACTIVE")
	private short active;
	
	@Column(name = "CREATE_USER")
	private String createUser;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	public ModelGroup() {
	}

	public ModelGroupId getId() {
		return id;
	}

	public void setId(ModelGroupId id) {
		this.id = id;
	}

	public String getModelGroup() {
		return this.id.getModelGroup();
	}

	public void setModelGroup(String modelGroup) {
		this.id.setModelGroup(modelGroup);
	}

	public String getSystem() {
		return this.id.getSystem();
	}

	public void setSystem(String system) {
		this.id.setSystem(system);
	}

	public String getModelGroupDescription() {
		return StringUtils.trimToEmpty(this.modelGroupDescription);
	}

	public void setModelGroupDescription(String modelGroupDescription) {
		this.modelGroupDescription = modelGroupDescription;
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
	
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return StringUtils.trimToEmpty(updateUser);
	}

	public void setUpdateUser(String upteUser) {
		this.updateUser = upteUser;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modelGroupDescription == null) ? 0 : modelGroupDescription.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
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
		ModelGroup other = (ModelGroup) obj;
		if (active != other.active)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modelGroupDescription == null) {
			if (other.modelGroupDescription != null)
				return false;
		} else if (!modelGroupDescription.equals(other.modelGroupDescription))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		return true;
	}

		
}
