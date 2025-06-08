package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiDepartmentId Class description</h3>
 * <p>
 * QiDepartmentId contains the getter and setter of the Department composite key properties and maps this class with database and these columns .
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
 *         Oct 06, 2016
 * 
 */
@Embeddable
public class QiDepartmentId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "DEPT")
	private String department;
	
	@Column(name = "SITE", nullable=false)
	private String site;
	
	@Column(name = "PLANT", nullable=false)
	private String plant;

	public QiDepartmentId() {
		super();
	}

	public QiDepartmentId(String department, String site, String plant) {
		super();
		this.department = department;
		this.site = site;
		this.plant = plant;
	}

	public String getSite() {
		return StringUtils.trimToEmpty(this.site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPlant() {
		return StringUtils.trimToEmpty(this.plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getDepartment() {
		return StringUtils.trimToEmpty(this.department);
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
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
		QiDepartmentId other = (QiDepartmentId) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
