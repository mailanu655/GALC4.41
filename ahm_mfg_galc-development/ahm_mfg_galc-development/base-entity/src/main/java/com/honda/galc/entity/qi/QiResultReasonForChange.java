package com.honda.galc.entity.qi;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiResultReasonForChange Class description</h3>
 * <p>
 * QiResultReasonForChange description
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
@Table(name = "QI_RESULT_REASON_FOR_CHANGE_TBX")
public class QiResultReasonForChange extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	QiResultReasonForChangeId id;

	public QiResultReasonForChangeId getId() {
		return id;
	}

	public void setId(QiResultReasonForChangeId id) {
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
		QiResultReasonForChange other = (QiResultReasonForChange) obj;
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
