package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiDepartment Class description</h3>
 * <p>
 * QiDepartment contains the getter and setter of the Department properties and maps this class with database table and properties with the database its columns .
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
@Table(name = "QI_DEPT_TBX")
public class QiDepartment extends CreateUserAuditEntry{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiDepartmentId id;
	
	@Column(name = "DEPT_NAME")
	@Auditable
	private String departmentName;
	
	@Column(name = "PDDA_DEPT")
	@Auditable
	private String pddaDept;
	
	@Column(name = "DEPT_DESCRIPTION")
	@Auditable
	private String departmentDescription;
	
	@Column(name = "ACTIVE")
	@Auditable
	private int active;

	
	
	public QiDepartment() {
		super();
	}

	public QiDepartmentId getId() {
		return id;
	}

	public void setId(QiDepartmentId id) {
		this.id = id;
	}
	
	public String getDepartment() {
		return getId().getDepartment();
	}

	public String getDepartmentName() {
		return StringUtils.trimToEmpty(this.departmentName);
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentDescription() {
		return StringUtils.trimToEmpty(this.departmentDescription);
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}
	
	public String getPddaDept() {
		return StringUtils.trimToEmpty(pddaDept);
	}

	public void setPddaDept(String pddaDept) {
		this.pddaDept = pddaDept;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active == (short) 1;
	}

	public void setActive(boolean active) {
		this.active = (short) (active ? 1 : 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((departmentDescription == null) ? 0 : departmentDescription.hashCode());
		result = prime * result + ((departmentName == null) ? 0 : departmentName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pddaDept == null) ? 0 : pddaDept.hashCode());
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
		QiDepartment other = (QiDepartment) obj;
		if (active != other.active)
			return false;
		if (departmentDescription == null) {
			if (other.departmentDescription != null)
				return false;
		} else if (!departmentDescription.equals(other.departmentDescription))
			return false;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pddaDept == null) {
			if (other.pddaDept != null)
				return false;
		} else if (!pddaDept.equals(other.pddaDept))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiDepartment [id=" + id + ", departmentName=" + departmentName + ", pddaDept=" + pddaDept
				+ ", departmentDescription=" + departmentDescription + ", active=" + active + "]";
	}

}
