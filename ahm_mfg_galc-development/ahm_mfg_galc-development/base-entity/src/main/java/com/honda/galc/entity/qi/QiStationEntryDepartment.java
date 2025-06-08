package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.conf.Division;

/**
 * 
 * <h3>QiEntryStationDepartment Class description</h3>
 * <p>
 * QiEntryStationDepartment contains the getter and setter of the Part
 * properties and maps this class with database table and properties with the
 * database its columns .
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
 *         Oct 25,2016
 * 
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
@Entity
@Table(name = "QI_STATION_ENTRY_DEPT_TBX")
public class QiStationEntryDepartment extends CreateUserAuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QiStationEntryDepartment() {
	}

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiStationEntryDepartmentId id;
	@Auditable
	@Column(name = "IS_DEFAULT")
	private short isDefault;
	@ManyToOne
	@JoinColumn(name="DIVISION_ID" ,updatable = false,insertable=false, nullable=false)
    private Division division;
	

	public QiStationEntryDepartmentId getId() {
		return this.id;
	}

	public short getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(short isDefault) {
		this.isDefault = isDefault;
	}

	public void setId(QiStationEntryDepartmentId id) {
		this.id = id;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + isDefault;
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
		QiStationEntryDepartment other = (QiStationEntryDepartment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDefault != other.isDefault)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProcessPointId(), getId().getDivisionId(), getIsDefault());
	}

}
