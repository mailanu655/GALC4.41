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
 * <h3>QiStationWriteUpDepartment Class description</h3>
 * <p> QiStationWriteUpDepartment description </p>
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
 * @author L&T Infotech<br>
 * November 22, 2016
 *
 *
 */
@Entity
@Table(name = "QI_STATION_WRITE_UP_DEPT_TBX")
public class QiStationWriteUpDepartment extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiStationWriteUpDepartmentId id;
	
	@Column(name = "COLOR_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String colorDescription;
	
	@Column(name = "IS_DEFAULT")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private short isDefault;

	public QiStationWriteUpDepartment() {
		super();
	}

	public String getColorDescription() {
		return StringUtils.trimToEmpty(colorDescription);
	}

	public void setColorDescription(String colorDescription) {
		this.colorDescription = colorDescription;
	}

	public short getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(short isDefault) {
		this.isDefault = isDefault;
	}

	public void setId(QiStationWriteUpDepartmentId id) {
		this.id = id;
	}

	public QiStationWriteUpDepartmentId getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((colorDescription == null) ? 0 : colorDescription.hashCode());
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
		QiStationWriteUpDepartment other = (QiStationWriteUpDepartment) obj;
		if (colorDescription == null) {
			if (other.colorDescription != null)
				return false;
		} else if (!colorDescription.equals(other.colorDescription))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDefault != other.isDefault)
			return false;
		return true;
	}
}
