package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>EquipFaultCode Class description</h3>
 * <p> EquipFaultCode description </p>
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
@Table(name="EQUIP_FAULT_CODE_TBX")
public class EquipFaultCode extends AuditEntry {
	@EmbeddedId
	private EquipFaultCodeId id;

	@Column(name="FAULT_DESCRIPTION")
	private String faultDescription;

	@Column(name="IS_CONTROLLABLE")
	private short isControllable;

	@ManyToOne
	@JoinColumn(name="UNIT_ID")
	private EquipUnitFault unitId;

	private static final long serialVersionUID = 1L;

	public EquipFaultCode() {
		super();
	}

	public EquipFaultCode(short unitId, int faultCode) {
		super();
		this.id = new EquipFaultCodeId();
		id.setUnitId(unitId);
		id.setFaultCode(faultCode);
	}
	
	public EquipFaultCodeId getId() {
		return this.id;
	}

	public void setId(EquipFaultCodeId id) {
		this.id = id;
	}

	public int getFaultCode() {
		return id == null? 0 : id.getFaultCode();
	}
	
	public String getUnitName() {
		return unitId == null ? "" : unitId.getUnitName();
	}
	
	public String getFaultDescription() {
		return StringUtils.trim(this.faultDescription);
	}

	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	public short getIsControllable() {
		return this.isControllable;
	}

	public void setControllable(boolean isControllable) {
		this.isControllable = (short)(isControllable ? 1 : 0);
	}

	public EquipUnitFault getUnitId() {
		return this.unitId;
	}

	public void setUnitId(EquipUnitFault unitId) {
		this.unitId = unitId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((faultDescription == null) ? 0 : faultDescription.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + isControllable;
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
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
		EquipFaultCode other = (EquipFaultCode) obj;
		if (faultDescription == null) {
			if (other.faultDescription != null)
				return false;
		} else if (!faultDescription.equals(other.faultDescription))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isControllable != other.isControllable)
			return false;
		if (unitId == null) {
			if (other.unitId != null)
				return false;
		} else if (!unitId.equals(other.unitId))
			return false;
		return true;
	}

}
