package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>WebStartDefaultBuild Class description</h3>
 * <p> WebStartDefaultBuild description </p>
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
@Table(name="WSDEFBUILDS_TBX")
public class WebStartDefaultBuild extends AuditEntry {
	@Id
	@Column(name="ENVIRONMENT")
	private String environment;

	@Column(name="DEF_BUILD_ID")
	private String defaultBuildId;

	@Column(name="ENV_DESCRIPTION")
	private String envDescription;

	private static final long serialVersionUID = 1L;

	public WebStartDefaultBuild() {
		super();
	}
	
	public WebStartDefaultBuild(String environment,String defaultBuildId, String description) {
		
		this.environment = environment;
		this.defaultBuildId = defaultBuildId;
		this.envDescription = description;
		this.setCreateTimestamp(CommonUtil.getTimestampNow());
		
	}

	public String getEnvironment() {
		return StringUtils.trim(this.environment);
	}
	
	public String getId() {
		return getEnvironment();
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getDefaultBuildId() {
		return StringUtils.trim(this.defaultBuildId);
	}

	public void setDefaultBuildId(String defaultBuildId) {
		this.defaultBuildId = defaultBuildId;
	}

	public String getEnvDescription() {
		return StringUtils.trim(this.envDescription);
	}

	public void setEnvDescription(String envDescription) {
		this.envDescription = envDescription;
	}

	@Override
	public String toString() {
		return toString(getDefaultBuildId(),getEnvironment(),getEnvDescription());
	}

}
