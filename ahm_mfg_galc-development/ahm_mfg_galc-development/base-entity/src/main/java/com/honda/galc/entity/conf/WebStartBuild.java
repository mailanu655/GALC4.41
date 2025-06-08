package com.honda.galc.entity.conf;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>WebStartBuild Class description</h3>
 * <p> WebStartBuild description </p>
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
 * @author Jeffray Huang<br>
 * Jul 19, 2010
 *
 *
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="WSBUILDS_TBX")
public class WebStartBuild extends AuditEntry {
	@Id
	@Column(name="BUILD_ID")
	private String buildId;

	@Column(name="URL")
	private String url;

	@Column(name="DEFAULT_BUILD")
	private String defaultBuild;

	@Column(name="BUILD_DATE")
	private Date buildDate;

	private String description;

	private static final long serialVersionUID = 1L;

	public WebStartBuild() {
		super();
	}

	public String getBuildId() {
		return StringUtils.trim(this.buildId);
	}
	
	public String getId() {
		return getBuildId();
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDefaultBuild() {
		return StringUtils.trim(this.defaultBuild);
	}

	public void setDefaultBuild(String defaultBuild) {
		this.defaultBuild = defaultBuild;
	}
	
	public boolean isDefaultBuild() {
		return "Y".equalsIgnoreCase(getDefaultBuild());
	}

	public Date getBuildDate() {
		return this.buildDate;
	}

	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return toString(getBuildId(),getDefaultBuild(),getDescription());
	}
}
