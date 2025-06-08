package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.entity.AuditEntry;

 /** * *
 * @version 1 
 * @author Gangadhararao Gadde 
 * @since Aug 02, 2013 
 */ 
 
@Entity
@Table(name="DEFECT_REPAIR_METHOD_TBX")
public class DefectRepairMethod extends AuditEntry {
	
	@EmbeddedId
    private DefectRepairMethodId id;

	@Column(name="REPAIR_TIME")
	private int repairTime;

	private static final long serialVersionUID = 1L;

	public DefectRepairMethod() {
		super();
	}

	public DefectRepairMethod(DefectRepairMethodId id, int repairTime) {
		super();
		this.id = id;
		this.repairTime = repairTime;
	}

	public DefectRepairMethodId getId() {
		return id;
	}

	public void setId(DefectRepairMethodId id) {
		this.id = id;
	}

	public int getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(int repairTime) {
		this.repairTime = repairTime;
	}
	
	public String toString() {
		return getId().getRepairMethodName();
	}

}
