package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.dto.Auditable;

/** * * 
* 
* 
* 
*/
@Entity
@Table(name="REPAIR_PROCESS_POINT_TBX")
public class RepairProcessPoint extends AuditEntry {
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private RepairProcessPointId id;
	
	@Column(name = "DEVICE_ID")
	@Auditable(isPartOfPrimaryKey= false,sequence=2)
    private String deviceId;
	
	@Column(name="INSTRUCTION_CODE")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
	private String instructionCode;
	
	@Column(name="SEQUENCE_NUMBER")
	@Auditable(isPartOfPrimaryKey= false,sequence=4)
	private Integer sequenceNo;
	

	private static final long serialVersionUID = 1L;

	public RepairProcessPoint() {
		super();
	}

	public RepairProcessPointId getId() {
		return this.id;
	}

	public void setId(RepairProcessPointId id) {
		this.id = id;
	}

	public String getDeviceId() {
		return StringUtils.trim(this.deviceId);
	}

	public String getInstructionCode() {
		return StringUtils.trim(this.instructionCode);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((instructionCode == null) ? 0 : instructionCode.hashCode());
		result = prime * result + ((sequenceNo == null) ? 0 : sequenceNo.hashCode());
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
		RepairProcessPoint other = (RepairProcessPoint) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (instructionCode == null) {
			if (other.instructionCode != null)
				return false;
		} else if (!instructionCode.equals(other.instructionCode))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "RepairProcessPoint [id=" + getId().toString() + ", deviceId=" + deviceId
				+ ", instructionCode=" + instructionCode + ", sequenceNo=" + sequenceNo + "]";
	}
	
}
