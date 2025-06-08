package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.Auditable;
import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.AuditEntry;

/**
 * <h3>MCViosMasterOperationMeasurement Class description</h3>
 * <p>
 * Entity class for galadm.MC_VIOS_MASTER_OP_MEAS_TBX
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
 * @author Hemant Kumar<br>
 *        Nov 20, 2018
 */
@Entity
@Table(name="MC_VIOS_MASTER_OP_MEAS_TBX")
public class MCViosMasterOperationMeasurement extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MCViosMasterOperationMeasurementId id;
	
	@Column(name="DEVICE_ID", length=32)
	@ExcelSheetColumn(name="DeviceId")
	private String deviceId;

	@Column(name="DEVICE_MSG", length=32)
	@ExcelSheetColumn(name="DeviceMsg")
	private String deviceMsg;

	@Column(name="MAX_LIMIT")
	@ExcelSheetColumn(name="Max (nm)")
	private double maxLimit;
	
	@Column(name="MIN_LIMIT")
	@ExcelSheetColumn(name="Min (nm)")
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
	

	@Column(name = "USER_ID")
    private String userId;
	
	@Transient
	@ExcelSheetColumn(name = "Num bolts")
	private int numOfBolts;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterOperationMeasurement() {
		super();
	}

	public MCViosMasterOperationMeasurement(String viosPlatformId, String unitNo, PartType partType, int measurementSeqNum, String deviceId, String deviceMsg,
			double maxLimit, double minLimit, int check, int maxAttempts, String processor, int time, String type,
			String view) {
		super();
		this.id = new MCViosMasterOperationMeasurementId(viosPlatformId, unitNo, partType, measurementSeqNum);
		this.deviceId = deviceId;
		this.deviceMsg = deviceMsg;
		this.maxLimit = maxLimit;
		this.minLimit = minLimit;
		this.check = check;
		this.maxAttempts = maxAttempts;
		this.processor = processor;
		this.time = time;
		this.type = type;
		this.view = view;
	}

	public MCViosMasterOperationMeasurementId getId() {
		return id;
	}

	public void setId(MCViosMasterOperationMeasurementId id) {
		this.id = id;
	}

	public String getDeviceId() {
		return StringUtils.trimToEmpty(deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMsg() {
		return StringUtils.trimToEmpty(deviceMsg);
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
		return StringUtils.trimToEmpty(processor);
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
		return StringUtils.trimToEmpty(type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getView() {
		return StringUtils.trimToEmpty(view);
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public int getNumOfBolts() {
		return numOfBolts;
	}

	public void setNumOfBolts(int numOfBolts) {
		this.numOfBolts = numOfBolts;
	}

	public String getOperationName() {
		return getId().getUnitNo()
				+Delimiter.UNDERSCORE
				+getId().getViosPlatformId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + check;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((deviceMsg == null) ? 0 : deviceMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + maxAttempts;
		long temp;
		temp = Double.doubleToLongBits(maxLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((processor == null) ? 0 : processor.hashCode());
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
		MCViosMasterOperationMeasurement other = (MCViosMasterOperationMeasurement) obj;
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
		if (Double.doubleToLongBits(maxLimit) != Double.doubleToLongBits(other.maxLimit))
			return false;
		if (Double.doubleToLongBits(minLimit) != Double.doubleToLongBits(other.minLimit))
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
	
}
