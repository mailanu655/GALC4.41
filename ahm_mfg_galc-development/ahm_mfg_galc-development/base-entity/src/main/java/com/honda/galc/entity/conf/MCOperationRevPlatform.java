package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Apr 8, 2014
 */
@Entity
@Table(name="MC_OP_REV_PLATFORM_TBX")
public class MCOperationRevPlatform extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MCOperationRevPlatformId id;
	
	@Column(name="DEVICE_ID", length=32)
	private String deviceId;

	@Column(name="DEVICE_MSG", length=32)
	private String deviceMsg;

	@Column(name="OP_SEQ_NUM")
	private int operationSeqNum;

	@Column(name="OP_TIME")
	private int operationTime;
	
	public MCOperationRevPlatform() {}

	public MCOperationRevPlatformId getId() {
		return id;
	}

	public void setId(MCOperationRevPlatformId id) {
		this.id = id;
	}

	public String getDeviceId() {
		return StringUtils.trim(this.deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMsg() {
		return StringUtils.trim(this.deviceMsg);
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public int getOperationSeqNum() {
		return operationSeqNum;
	}

	public void setOperationSeqNum(int operationSeqNum) {
		this.operationSeqNum = operationSeqNum;
	}

	public int getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(int operationTime) {
		this.operationTime = operationTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result
				+ ((deviceMsg == null) ? 0 : deviceMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + operationSeqNum;
		result = prime * result + operationTime;
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
		MCOperationRevPlatform other = (MCOperationRevPlatform) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (deviceMsg == null) {
			if (other.deviceMsg != null)
				return false;
		} else if (!deviceMsg.equals(other.deviceMsg))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (operationSeqNum != other.operationSeqNum)
			return false;
		if (operationTime != other.operationTime)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getOperationName(), getId().getOperationRevision(),	getId().getPddaPlatformId(), 
				getOperationSeqNum(), getOperationTime(), getDeviceId(), getDeviceMsg());
	}
}
