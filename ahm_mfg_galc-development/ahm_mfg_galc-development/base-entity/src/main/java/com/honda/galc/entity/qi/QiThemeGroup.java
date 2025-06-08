package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;

/**
 * 
 * <h3>QIThemeGroup Class description</h3>
 * <p> QIThemeGroup description </p>
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
 * July 2016
 *
 *
 */
@Entity
@Table(name="QI_THEME_GROUP_TBX")
public class QiThemeGroup extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="THEME_GROUP_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String themeGroupName;
	
	@Column(name="THEME_GROUP_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String themeGroupDescription;
	
	@Column(name="ACTIVE")
	@Auditable
	private short active;
	
	public QiThemeGroup()
	{
		super();
	}
	
	public String getThemeGroupName() {
		return StringUtils.trimToEmpty(this.themeGroupName);
	}

	public void setThemeGroupName(String themeGroupName) {
		this.themeGroupName = themeGroupName;
	}

	public String getThemeGroupDescription() {
		return StringUtils.trimToEmpty(this.themeGroupDescription);
	}

	public void setThemeGroupDescription(String themeGroupDescription) {
		this.themeGroupDescription = themeGroupDescription;
	}

	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }
	
	public boolean isActive() {
        return this.active ==(short) 1;
    }

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}
	
	public String getStatus() {
		return StringUtils.trimToEmpty(isActive()?QiActiveStatus.ACTIVE.getName():QiActiveStatus.INACTIVE.getName());
	}
	
	public Object getId() {
		return themeGroupName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime
				* result
				+ ((themeGroupDescription == null) ? 0 : themeGroupDescription
						.hashCode());
		result = prime * result
				+ ((themeGroupName == null) ? 0 : themeGroupName.hashCode());
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
		QiThemeGroup other = (QiThemeGroup) obj;
		if (active != other.active)
			return false;
		if (themeGroupDescription == null) {
			if (other.themeGroupDescription != null)
				return false;
		} else if (!themeGroupDescription.equals(other.themeGroupDescription))
			return false;
		if (themeGroupName == null) {
			if (other.themeGroupName != null)
				return false;
		} else if (!themeGroupName.equals(other.themeGroupName))
			return false;
		return true;
	}
}
