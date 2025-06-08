package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>QIThemeGrouping Class description</h3>
 * <p> QIThemeGrouping description </p>
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

@Embeddable
public class QiThemeGroupingId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="THEME_NAME")
	private String themeName;
	
	@Column(name="THEME_GROUP_NAME")
	private String themeGroupName;
	
	public String getThemeName() {
		return StringUtils.trimToEmpty(themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeGroupName() {
		return StringUtils.trimToEmpty(themeGroupName);
	}

	public void setThemeGroupName(String themeGroupName) {
		this.themeGroupName = themeGroupName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((themeGroupName == null) ? 0 : themeGroupName.hashCode());
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
		QiThemeGroupingId other = (QiThemeGroupingId) obj;
		if (themeGroupName == null) {
			if (other.themeGroupName != null)
				return false;
		} else if (!themeGroupName.equals(other.themeGroupName))
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return themeName + "," + themeGroupName;
	}
}
