package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>ModelGrouping Class description</h3>
 * <p>
 * ModelGrouping contains the getter and setter of the Model Grouping properties and
 * maps this class with database table and properties with the database its
 * columns . </p>
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
@Table(name = "MODEL_GROUPING_TBX")
public class ModelGrouping extends AuditEntry {

	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private ModelGroupingId id;

	@Column(name = "CREATE_USER")
	private String createUser;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	
	public ModelGrouping() {
		super();
	}

	public ModelGroupingId getId() {
		return this.id;
	}

	public void setId(ModelGroupingId id) {
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
	
	public String getMtcModel() {
		return this.id.getMtcModel();
	}
	
	public void setMtcModel(String mtcModel) {
		this.id.setMtcModel(mtcModel);
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ModelGrouping other = (ModelGrouping) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
		

}
