package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>QiExternalSystemDefectIdMap Class description</h3>
 * <p> QiExternalSystemDefectIdMap description </p>
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
 * @author vcc44349<br>
 * Oct 22, 2019
 *
 *
 */

@Entity
@Table(name = "QI_EXTERNAL_SYSTEM_DEFECT_ID_TBX")
public class QiExternalSystemDefectIdMap extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private QiExternalSystemDefectId id;

	@Column(name = "IS_QICS_REPAIR_REQD")
	private short isQicsRepairReqd;

	@Column(name = "IS_EXT_SYS_REPAIR_REQD")
	private short isExtSysRepairReqd;

	public QiExternalSystemDefectIdMap() {
		super();
	}

	public QiExternalSystemDefectIdMap(QiExternalSystemDefectId id, short isQicsRepairReqd) {
		super();
		this.id = id;
		this.isQicsRepairReqd = isQicsRepairReqd;
	}

	public QiExternalSystemDefectId getId() {
		return id;
	}

	public void setId(QiExternalSystemDefectId id) {
		this.id = id;
	}

	public short getIsQicsRepairReqd() {
		return isQicsRepairReqd;
	}

	public boolean isQicsRepairReqd() {
		return isQicsRepairReqd == 1;
	}

	public void setIsQicsRepairReqd(short isQicsRepairReqd) {
		this.isQicsRepairReqd = isQicsRepairReqd;
	}
	
	public short getIsExtSysRepairReqd() {
		return isExtSysRepairReqd;
	}

	public boolean isExtSysRepairReqd() {
		return isExtSysRepairReqd == 1;
	}

	public void setIsExtSysRepairReqd(short isExtSysRepairReqd) {
		this.isExtSysRepairReqd = isExtSysRepairReqd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + isQicsRepairReqd;
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
		QiExternalSystemDefectIdMap other = (QiExternalSystemDefectIdMap) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isQicsRepairReqd != other.isQicsRepairReqd)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
