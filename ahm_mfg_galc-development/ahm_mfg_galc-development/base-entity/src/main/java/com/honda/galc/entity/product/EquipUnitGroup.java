package com.honda.galc.entity.product;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>EquipUnitGroup Class description</h3>
 * <p> EquipUnitGroup description </p>
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
 * Apr 4, 2016
 *
 *
 */
@Entity
@Table(name="EQUIP_GROUP_UNIT_TBX")
public class EquipUnitGroup extends AuditEntry {
	@EmbeddedId
	private EquipUnitGroupId id;

	private int priority;

	@ManyToOne
	@JoinColumn(name="GROUP_ID" ,updatable = false,insertable=false)
	private EquipmentGroup equipGroup;

	
	@ManyToOne
	@JoinColumn(name="UNIT_ID")
	private EquipUnitFault unitFault;

	private static final long serialVersionUID = 1L;

	public EquipUnitGroup(short groupId, short unitId) {
		super();
		id = new EquipUnitGroupId();
		id.setGroupId(groupId);
		id.setUnitId(unitId);
	}
	
	public EquipUnitGroup() {
		super();
	}

	public EquipUnitGroupId getId() {
		return this.id;
	}

	public void setId(EquipUnitGroupId id) {
		this.id = id;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public EquipmentGroup getEquipGroup() {
		return this.equipGroup;
	}

	public void setGroupId(EquipmentGroup equipGroup) {
		this.equipGroup = equipGroup;
	}
	
	public Short getUnitId() {
		return getId().getUnitId();
	}
	
	public String getUnitName() {
		return unitFault == null ? "" : unitFault.getUnitName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + priority;
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
		EquipUnitGroup other = (EquipUnitGroup) obj;
			if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}

}
