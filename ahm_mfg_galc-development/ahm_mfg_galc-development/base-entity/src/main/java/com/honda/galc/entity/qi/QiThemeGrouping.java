package com.honda.galc.entity.qi;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

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
@Entity
@Table(name = "QI_THEME_GROUPING_TBX")
public class QiThemeGrouping extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiThemeGroupingId id;

	
	public QiThemeGroupingId getId() {
		return this.id;
	}

	public void setId(QiThemeGroupingId id) {
		this.id = id;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(id.getThemeName());
	}

	public void setThemeName(String themeName) {
		if(id == null) id = new QiThemeGroupingId();
		id.setThemeName(themeName);
	}

	public String getThemeGroupName() {
		return StringUtils.trimToEmpty(id.getThemeGroupName());
	}

	public void setThemeGroupName(String themeGroupName) {
		if(id == null) id = new QiThemeGroupingId();
		id.setThemeGroupName(themeGroupName);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		QiThemeGrouping other = (QiThemeGrouping) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
