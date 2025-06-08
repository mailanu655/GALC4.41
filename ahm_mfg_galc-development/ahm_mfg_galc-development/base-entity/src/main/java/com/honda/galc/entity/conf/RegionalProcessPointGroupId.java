package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */

@Embeddable
public class RegionalProcessPointGroupId implements Serializable {

	private static final long serialVersionUID = 5772118975660598262L;

	@Column(name = "CATEGORY_CODE")
    @Auditable(isPartOfPrimaryKey = true, sequence = 1)
    private short categoryCode;
    
    @Column(name = "SITE")
    @Auditable(isPartOfPrimaryKey = true, sequence = 2)
    private String site;

    @Column(name = "PROCESS_POINT_GROUP_NAME")
    @Auditable(isPartOfPrimaryKey = true, sequence = 3)
    private String processPointGroupName;
 
	public RegionalProcessPointGroupId() {
		super();
	}

	public RegionalProcessPointGroupId(short categoryCode, String site, String processPointGroupName) {
		super();
		this.categoryCode = categoryCode;
		this.site = site;
		this.processPointGroupName = processPointGroupName;
	}

	public short getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(short categoryCode) {
		this.categoryCode = categoryCode;
	}



	public String getSite() {
		return StringUtils.trimToEmpty(site);
	}


	public void setSite(String site) {
		this.site = site;
	}


	public String getProcessPointGroupName() {
		return StringUtils.trimToEmpty(processPointGroupName);
	}


	public void setProcessPointGroupName(String processPointGroupName) {
		this.processPointGroupName = processPointGroupName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime + categoryCode;
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((processPointGroupName == null) ? 0 : processPointGroupName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		RegionalProcessPointGroupId other = (RegionalProcessPointGroupId) obj;

		return getCategoryCode() == other.getCategoryCode()
				&& StringUtils.equals(getSite(), other.getSite())
				&& StringUtils.equals(getProcessPointGroupName(), other.getProcessPointGroupName());
	}
	    
	@Override
	public String toString() {		
	  return StringUtil.toString(getClass().getSimpleName(), getCategoryCode(), getSite(), getProcessPointGroupName());
	}
 
}
