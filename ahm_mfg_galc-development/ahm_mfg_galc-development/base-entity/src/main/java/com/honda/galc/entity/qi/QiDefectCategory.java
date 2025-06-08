package com.honda.galc.entity.qi;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.CreateUserAuditEntry;


@Entity
@Table(name = "QI_DEFECT_CATEGORY_TBX")
public class QiDefectCategory extends CreateUserAuditEntry {
	
	private static final long serialVersionUID = 1L;
	public static final String REAL_PROBLEM_DEF_CATG = "REAL PROBLEM";
	public static final String INFORMATIONAL_DEF_CATG = "INFORMATIONAL";
	public static final String SYMPTOM_DEF_CATG = "SYMPTOM";

	@Id
	@Column(name = "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;
	
	
	public QiDefectCategory() {
		super();
	}


	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(this.defectCategoryName);
	}


	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}
	
	
	public Object getId() {
		return defectCategoryName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((defectCategoryName == null) ? 0 : defectCategoryName
						.hashCode());
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
		QiDefectCategory other = (QiDefectCategory) obj;
		if (defectCategoryName == null) {
			if (other.defectCategoryName != null)
				return false;
		} else if (!defectCategoryName.equals(other.defectCategoryName))
			return false;
		return true;
	}
}
