package com.honda.galc.entity.conf;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Todd Roling
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MTX_HISTORY_TBX")
public class MetricHistory extends AuditEntry {
	@Id
	@Column(name = "METRIC_HISTORY_ID")
	private BigInteger metricHistoryId;

	@Column(name = "METRIC_ID")
	private BigInteger metricId;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;

	@Column(name = "SCHEDULE_ID")
	private String scheduleId;

	@Column(name = "CURRENT_TIMESTAMP")
	private Date currentTimestamp;

	@Column(name = "CURRENT_VALUE")
	private double currentValue;

	@Column(name = "PREVIOUS_TIMESTAMP")
	private Date previousTimestamp;

	@Column(name = "PREVIOUS_VALUE")
	private double previousValue;

	public Object getId() {
		return getMetricHistoryId();
	}

	public BigInteger getMetricHistoryId() {
		return metricHistoryId;
	}

	public BigInteger getMetricId() {
		return metricId;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public Date getCurrentTimestamp() {
		return currentTimestamp;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public Date getPreviousTimestamp() {
		return previousTimestamp;
	}

	public double getPreviousValue() {
		return previousValue;
	}

	public void setMetricHistoryId(BigInteger metricHistoryId) {
		this.metricHistoryId = metricHistoryId;
	}

	public void setMetricId(BigInteger metricId) {
		this.metricId = metricId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public void setCurrentTimestamp(Date currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public void setPreviousTimestamp(Date previousTimestamp) {
		this.previousTimestamp = previousTimestamp;
	}

	public void setPreviousValue(double previousValue) {
		this.previousValue = previousValue;
	}
}
