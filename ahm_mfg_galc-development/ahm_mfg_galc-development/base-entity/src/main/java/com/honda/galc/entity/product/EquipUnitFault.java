package com.honda.galc.entity.product;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>EquipUnitFault Class description</h3>
 * <p> EquipUnitFault description </p>
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
@Table(name="EQUIP_UNIT_FAULT_TBX")
public class EquipUnitFault extends AuditEntry {
	@Id
	@Column(name="UNIT_ID")
	private short unitId;

	@Column(name="UNIT_NAME")
	private String unitName;

	@Column(name="CURRENT_FAULT_CODE")
	private int currentFaultCode;

	@Column(name="IS_MANUAL_IMPORT")
	private int isManualImport;

	@OneToMany(mappedBy="unitId")
	private List<EquipFaultCode> faultCodes;
	
	@OneToMany(mappedBy="unitFault",fetch = FetchType.EAGER)
	private List<EquipUnitGroup> unitGroups;

	private static final long serialVersionUID = 1L;

	public EquipUnitFault() {
		super();
	}

	public EquipUnitFault(short unitId) {
		super();
		setUnitId(unitId);
	}
	
	public Short getId() {
		return this.unitId;
	}

	public void setUnitId(short unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return StringUtils.trim(this.unitName);
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getCurrentFaultCode() {
		return this.currentFaultCode;
	}

	public void setCurrentFaultCode(int currentFaultCode) {
		this.currentFaultCode = currentFaultCode;
	}

	public boolean isManualImport() {
		return this.isManualImport == 1;
	}

	public void setManualImport(boolean isManualImport) {
		this.isManualImport = isManualImport ? 1 : 0;
	}

	public List<EquipFaultCode> getFaultCodes() {
		return this.faultCodes == null ? new ArrayList<EquipFaultCode>() : faultCodes;
	}

	public void setFaultCodes(List<EquipFaultCode> faultCodes) {
		this.faultCodes = faultCodes;
	}
	
	public List<EquipUnitGroup> getUnitGroups() {
		return unitGroups  == null ? new ArrayList<EquipUnitGroup>() : unitGroups;
	}

	public void setUnitGroups(List<EquipUnitGroup> unitGroups) {
		this.unitGroups = unitGroups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentFaultCode;
		result = prime * result + isManualImport;
		result = prime * result + unitId;
		result = prime * result
				+ ((unitName == null) ? 0 : unitName.hashCode());
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
		EquipUnitFault other = (EquipUnitFault) obj;
		if (currentFaultCode != other.currentFaultCode)
			return false;
		if (isManualImport != other.isManualImport)
			return false;
		if (unitId != other.unitId)
			return false;
		if (unitName == null) {
			if (other.unitName != null)
				return false;
		} else if (!unitName.equals(other.unitName))
			return false;
		return true;
	}

}
