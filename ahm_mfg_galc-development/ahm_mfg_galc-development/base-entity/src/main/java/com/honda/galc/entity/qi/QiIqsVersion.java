package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

@Entity
@Table(name = "QI_IQS_VERSION_TBX")
public class QiIqsVersion extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "IQS_VERSION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String iqsVersion;

	public QiIqsVersion() {
		super();
	}
	public String getIqsVersion() {
		return StringUtils.trimToEmpty(iqsVersion);
	}
	public void setIqsVersion(String iqsVersion) {
		this.iqsVersion = iqsVersion;
	}
	public Object getId() {
		return iqsVersion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
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
		QiIqsVersion other = (QiIqsVersion) obj;
		if (iqsVersion == null) {
			if (other.iqsVersion != null)
				return false;
		} else if (!iqsVersion.equals(other.iqsVersion))
			return false;
		return true;
	}
	
	public void clear() {
		setIqsVersion(StringUtils.EMPTY);
	}
	
}
