package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiReasonForChangeCategory Class description</h3>
 * <p>
 * QiReasonForChangeCategory description
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

@Entity
@Table(name = "QI_REASON_FOR_CHANGE_CATEGORY_TBX")
public class QiReasonForChangeCategory extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CATEGORY_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private int categoryId;
	
	@Auditable
	@Column(name = "SITE", nullable=false)
	private String site;
	
	@Auditable
	@Column(name = "PLANT", nullable=false)
	private String plant;
	
	@Auditable
	@Column(name = "DEPT", nullable=false)
	private String department;
	
	@Auditable
	@Column(name = "CATEGORY", nullable=false)
	private String category;

	public QiReasonForChangeCategory() {
		super();
	}

	public Object getId() {
		return getCategoryId();
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getSite() {
		return StringUtils.trimToEmpty(site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPlant() {
		return StringUtils.trimToEmpty(plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getDepartment() {
		return StringUtils.trimToEmpty(department);
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCategory() {
		return StringUtils.trimToEmpty(category);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + categoryId;
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
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
		QiReasonForChangeCategory other = (QiReasonForChangeCategory) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (categoryId != other.categoryId)
			return false;
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
