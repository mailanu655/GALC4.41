package com.honda.galc.entity.product;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>EquipmentGroup Class description</h3>
 * <p> EquipmentGroup description </p>
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
 * @author Jeffray Huang<br>
 * Dec 17, 2012
 *
 *
 */
@Entity
@Table(name="EQUIP_GROUP_TBX")
public class EquipmentGroup extends AuditEntry {
	@Id
	@Column(name="GROUP_ID")
	private short groupId;

	@Column(name="GROUP_NAME")
	private String groupName;

	@Column(name="GROUP_DESCRIPTION")
	private String groupDescription;

	@Column(name="FAULT_COUNT")
	private short faultCount;

	@OneToMany(mappedBy="equipGroup", fetch = FetchType.EAGER)
	private List<EquipUnitGroup> unitGroups;

	private static final long serialVersionUID = 1L;

	public EquipmentGroup() {
		super();
	}

	public Short getId() {
		return this.groupId;
	}

	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return StringUtils.trim(this.groupName);
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return StringUtils.trim(this.groupDescription);
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public short getFaultCount() {
		return this.faultCount;
	}

	public void setFaultCount(short faultCount) {
		this.faultCount = faultCount;
	}

	public List<EquipUnitGroup> getUnitGroups() {
		return unitGroups == null ? new ArrayList<EquipUnitGroup>() : unitGroups;
	}

	public void setUnitGroups(List<EquipUnitGroup> unitGroups) {
		this.unitGroups = unitGroups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + faultCount;
		result = prime
				* result
				+ ((groupDescription == null) ? 0 : groupDescription.hashCode());
		result = prime * result + groupId;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
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
		EquipmentGroup other = (EquipmentGroup) obj;
		if (faultCount != other.faultCount)
			return false;
		if (groupDescription == null) {
			if (other.groupDescription != null)
				return false;
		} else if (!groupDescription.equals(other.groupDescription))
			return false;
		if (groupId != other.groupId)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

}
