package com.honda.galc.entity.qi;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

@Entity
@Table(name = "QI_STATION_DOCUMENT_TBX")
public class QiStationDocument extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiStationDocumentId id;

	public QiStationDocument() {
		super();
	}

	public QiStationDocumentId getId() {
		return id;
	}

	public void setId(QiStationDocumentId id) {
		this.id = id;
	}
	
	public void setId(String processPointId, Integer documentId) {
		this.id = new QiStationDocumentId(processPointId, documentId);
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
		QiStationDocument other = (QiStationDocument) obj;
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
