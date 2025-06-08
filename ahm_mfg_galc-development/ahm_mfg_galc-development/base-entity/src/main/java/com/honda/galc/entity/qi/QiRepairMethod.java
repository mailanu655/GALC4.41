package com.honda.galc.entity.qi;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.enumtype.QiActiveStatus;

/**
 * 
 * <h3>QiRepairMethod Class description</h3>
 * <p>
 * QiRepairMethod contains the getter and setter of the Repair Method properties and maps this class with database table and properties with the database its columns .
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
 * 
 */


@Entity
@Table(name = "QI_REPAIR_METHOD_TBX")
public class QiRepairMethod extends QiAuditEntryTimestamp{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "REPAIR_METHOD")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String repairMethod;
	
	@Column(name = "REPAIR_METHOD_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String repairMethodDescription;
	
	@Column(name = "ACTIVE")
	@Auditable
	private short active;
	
	public QiRepairMethod() {
		super();
	}
	
	public String getRepairMethod() {
		return StringUtils.trimToEmpty(this.repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public String getRepairMethodDescription() {
		return StringUtils.trimToEmpty(this.repairMethodDescription);
	}

	public void setRepairMethodDescription(String repairMethodDescription) {
		this.repairMethodDescription = repairMethodDescription;
	}

	public short getActive() {
		return active;
	}

	public void setActive(short active) {
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime
				* result
				+ ((repairMethodDescription == null) ? 0
						: repairMethodDescription.hashCode());
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
		QiRepairMethod other = (QiRepairMethod) obj;
		if (active != other.active)
			return false;
		if (repairMethod == null) {
			if (other.repairMethod != null)
				return false;
		} else if (!repairMethod.equals(other.repairMethod))
			return false;
		if (repairMethodDescription == null) {
			if (other.repairMethodDescription != null)
				return false;
		} else if (!repairMethodDescription
				.equals(other.repairMethodDescription))
			return false;
		return true;
	}

	public Object getId() {
		return getRepairMethod();
	}

}
