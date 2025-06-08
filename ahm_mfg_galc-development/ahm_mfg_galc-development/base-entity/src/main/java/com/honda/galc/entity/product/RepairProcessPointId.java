package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.entity.UserAuditEntry;
import com.honda.galc.dto.Auditable;
import org.apache.commons.lang.StringUtils;


@Embeddable
public class RepairProcessPointId implements Serializable {
	
	
	@Column(name="PROCESS_POINT_ID")
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private String processPointId;

	@Column(name="PART_NAME")
	@Auditable(isPartOfPrimaryKey= true,sequence=2)
	private String partName;
	
	@Column(name="PART_ID")
	@Auditable(isPartOfPrimaryKey= true,sequence=3)
	private String partId;
	

	private static final long serialVersionUID = 1L;

	public RepairProcessPointId() {
		super();
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public String getPartId() {
		return StringUtils.trim(this.partId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
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
		RepairProcessPointId other = (RepairProcessPointId) obj;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return processPointId + "," + partName + ","
				+ partId;
	}

}
