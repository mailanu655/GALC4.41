package com.honda.galc.entity.qi;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiSite Class description</h3>
 * <p>
 * QiSite contains the getter and setter of the QiSite and maps this class with database and these columns .
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
 *        Jul 26, 2016
 * 
 */
@Entity
@Table(name = "QI_SITE_TBX")
public class QiSite extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SITE", nullable=false)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String site;
	
	@Column(name = "SITE_DESCRIPTION")
	@Auditable
	private String siteDesc;
	
	@Column(name = "COMPANY")
	@Auditable
	private String company;
	
	@Column(name = "ACTIVE")
	@Auditable
	private int active;
	
	public QiSite() {}
	
    public QiSite(String site) {
      this.setSite(site);
    }

	public String getSite() {
		return StringUtils.trimToEmpty(site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSiteDesc() {
		return StringUtils.trimToEmpty(siteDesc);
	}

	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}

	public String getCompany() {
		return StringUtils.trimToEmpty(company);
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active == (short) 1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result
				+ ((siteDesc == null) ? 0 : siteDesc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiSite other = (QiSite) obj;
		if (active != other.active)
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (siteDesc == null) {
			if (other.siteDesc != null)
				return false;
		} else if (!siteDesc.equals(other.siteDesc))
			return false;
		return true;
	}

	public Object getId() {
		return getSite();
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
   
}
