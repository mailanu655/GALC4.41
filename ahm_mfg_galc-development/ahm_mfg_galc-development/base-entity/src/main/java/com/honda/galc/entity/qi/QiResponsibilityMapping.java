package com.honda.galc.entity.qi;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

@Entity
@Table(name = "QI_RESPONSIBILITY_MAPPING_TBX")
public class QiResponsibilityMapping extends CreateUserAuditEntry {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiResponsibilityMappingId id;
	
	public int getDefaultRespLevel() {
		return id.getDefaultRespLevelId();
	}
	
	public int getAlternateDefault() {
		return id.getRespLevelId();
	}
	
	public String getPCode() {
		return id.getPlantCode();
	}
	
	public QiResponsibilityMapping() {
		super();
	}
	
	public QiResponsibilityMapping(QiResponsibilityMappingId id) {
		super();
		this.id = id;
	}
	 
	public QiResponsibilityMapping(int defaultRespLevelId, String plantCode, int respLevelId, String userId) {
		id = new QiResponsibilityMappingId(defaultRespLevelId, plantCode, respLevelId);
		setCreateUser(userId);
	}

	public QiResponsibilityMappingId getId() {
		return this.id;
	}
	
	public void setId(QiResponsibilityMappingId id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		QiResponsibilityMapping other = (QiResponsibilityMapping) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
