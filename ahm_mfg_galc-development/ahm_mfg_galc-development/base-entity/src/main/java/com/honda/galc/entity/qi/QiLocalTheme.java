package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>QiLocalTheme Class description</h3>
 * <p> QiLocalTheme description </p>
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
 *        Oct 06, 2016
 * 
 */

@Entity
@Table(name = "QI_LOCAL_THEME_TBX")
public class QiLocalTheme extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "LOCAL_THEME")
	private String localTheme;
	
	@Column(name = "LOCAL_THEME_DESCRIPTION")
	private String localThemeDescription;
	
	@Column(name = "ACTIVE")
	private short active;
	
	public String getLocalTheme() {
		return StringUtils.trimToEmpty(this.localTheme);
	}

	public void setLocalTheme(String localTheme) {
		this.localTheme = localTheme;
	}

	public short getActive() {
		return active;
	}

	public void setActive(short active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active == (short) 1;
	}

	public void setActive(boolean active) {
		this.active = (short) (active ? 1 : 0);
	}
	
	public Object getId() {
		return localTheme;
	}

	public String getLocalThemeDescription() {
		return StringUtils.trimToEmpty(localThemeDescription);
	}

	public void setLocalThemeDescription(String localThemeDescription) {
		this.localThemeDescription = localThemeDescription;
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((localTheme == null) ? 0 : localTheme.hashCode());
		result = prime * result
				+ ((localThemeDescription == null) ? 0 : localThemeDescription.hashCode());
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
		QiLocalTheme other = (QiLocalTheme) obj;
		if (active != other.active)
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
			return false;
		if (localThemeDescription == null) {
			if (other.localThemeDescription != null)
				return false;
		} else if (!localThemeDescription.equals(other.localThemeDescription))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiLocalTheme [localTheme=" + localTheme + ", localThemeDescription=" + localThemeDescription
				+ ", active=" + active + "]";
	}

	
}
