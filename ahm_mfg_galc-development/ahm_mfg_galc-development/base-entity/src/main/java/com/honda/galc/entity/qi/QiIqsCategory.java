package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

@Entity
@Table(name = "QI_IQS_CATEGORY_TBX")
public class QiIqsCategory extends CreateUserAuditEntry{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "IQS_CATEGORY")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String iqsCategory;
	
	public QiIqsCategory() {
		super();
	}
	public String getIqsCategory() {
		return StringUtils.trimToEmpty(this.iqsCategory);
	}

	public void setIqsCategory(String iqsCategory) {
		this.iqsCategory = iqsCategory;
	}

	public Object getId() {
		return iqsCategory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
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
		QiIqsCategory other = (QiIqsCategory) obj;
		if (iqsCategory == null) {
			if (other.iqsCategory != null)
				return false;
		} else if (!iqsCategory.equals(other.iqsCategory))
			return false;
		return true;
	}
	public void clear() {
		setIqsCategory(StringUtils.EMPTY);
	}

}
