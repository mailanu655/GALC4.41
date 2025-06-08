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
 * <h3>QiTheme Class description</h3>
 * <p> QiTheme description </p>
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
@Table(name = "QI_THEME_TBX")

public class QiTheme extends CreateUserAuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="THEME_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String themeName;
	
	@Column(name="THEME_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String themeDescription;
	
	@Column(name = "ACTIVE")
	@Auditable
	private short active;

	public QiTheme()
	{
		super();
	}
	public String getThemeName() {
		return StringUtils.trimToEmpty(this.themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeDescription() {
		return StringUtils.trimToEmpty(this.themeDescription);
	}

	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
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
		return themeName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime
				* result
				+ ((themeDescription == null) ? 0 : themeDescription.hashCode());
		result = prime * result
				+ ((themeName == null) ? 0 : themeName.hashCode());
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
		QiTheme other = (QiTheme) obj;
		if (active != other.active)
			return false;
		if (themeDescription == null) {
			if (other.themeDescription != null)
				return false;
		} else if (!themeDescription.equals(other.themeDescription))
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		return true;
	}
}
