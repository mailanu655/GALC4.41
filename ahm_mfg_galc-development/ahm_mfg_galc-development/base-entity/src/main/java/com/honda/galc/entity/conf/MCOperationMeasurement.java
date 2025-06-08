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
 * Feb 10, 2014
 */
@Entity
@Table(name="MC_OP_MEAS_TBX")
public class MCOperationMeasurement extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCOperationMeasurementId id;

	@Column(name="DEVICE_ID", length=32)
	private String deviceId;

	@Column(name="DEVICE_MSG", length=32)
	private String deviceMsg;

	@Column(name="MAX_LIMIT")
	private double maxLimit;
	
	@Column(name="MIN_LIMIT")
	private double minLimit;

	@Column(name="MEAS_CHECK")
	private int check;

	@Column(name="MEAS_MAX_ATTEMPTS")
	private int maxAttempts;

	@Column(name="MEAS_PROCESSOR", length=255)
	private String processor;

	@Column(name="MEAS_TIME")
	private int time;

	@Column(name="MEAS_TYPE", length=32)
	private String type;

	@Column(name="MEAS_VIEW", length=255)
	private String view;

    public MCOperationMeasurement() {}

	public MCOperationMeasurementId getId() {
		return id;
	}

	public void setId(MCOperationMeasurementId id) {
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

	public double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(double maxLimit) {
		this.maxLimit = maxLimit;
	}
	
	public double getMinLimit() {
		return minLimit;
	}
	
	public void setMinLimit(double minLimit) {
		this.minLimit = minLimit;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public String getProcessor() {
		return StringUtils.trim(this.processor);
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getType() {
		return StringUtils.trim(this.type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getView() {
		return StringUtils.trim(this.view);
	}

	public void setView(String view) {
		this.view = view;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + check;
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result
				+ ((deviceMsg == null) ? 0 : deviceMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + maxAttempts;
		long temp;
		temp = Double.doubleToLongBits(maxLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((processor == null) ? 0 : processor.hashCode());
		result = prime * result + time;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((view == null) ? 0 : view.hashCode());
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
		MCOperationMeasurement other = (MCOperationMeasurement) obj;
		if (check != other.check)
			return false;
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
		if (maxAttempts != other.maxAttempts)
			return false;
		if (Double.doubleToLongBits(maxLimit) != Double
				.doubleToLongBits(other.maxLimit))
			return false;
		if (Double.doubleToLongBits(minLimit) != Double
				.doubleToLongBits(other.minLimit))
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		if (time != other.time)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (view == null) {
			if (other.view != null)
				return false;
		} else if (!view.equals(other.view))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return toString(getId().getOperationName(), getId().getMeasurementSeqNum(), getId().getPartId(), getId().getPartRevision(),
				getCreateTimestamp(), getDeviceId(), getDeviceMsg(), getMaxLimit(), getMinLimit(), getCheck(), 
				getType(), getMaxAttempts(), getProcessor(), getView(), getTime());
	}
}