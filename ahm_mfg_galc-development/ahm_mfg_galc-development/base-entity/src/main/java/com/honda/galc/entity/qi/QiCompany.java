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
 * <h3>QiCompany Class description</h3>
 * <p>
 * QiCompany contains the getter and setter of the Company maps this class with database and these columns .
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
@Table(name = "QI_COMPANY_TBX")
public class QiCompany extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "COMPANY", nullable=false)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String company;
	
	@Column(name = "COMPANY_DESCRIPTION")
	@Auditable
	private String companyDesc;
	
	@Column(name = "ACTIVE")
	@Auditable
	private int active;
	
	public QiCompany() {}
	
    public QiCompany(String company) {
      this.setCompany(company);
    }

	public String getCompany() {
		return StringUtils.trimToEmpty(company);
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyDesc() {
		return StringUtils.trimToEmpty(companyDesc);
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
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
		result = prime * result
				+ ((companyDesc == null) ? 0 : companyDesc.hashCode());
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
		QiCompany other = (QiCompany) obj;
		if (active != other.active)
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (companyDesc == null) {
			if (other.companyDesc != null)
				return false;
		} else if (!companyDesc.equals(other.companyDesc))
			return false;
		return true;
	}

	public Object getId() {
		return getCompany();
	}
    
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
   
}
