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
@Table(name = "MTX_GROUP_HISTORY_TBX")
public class MetricGroupHistory extends AuditEntry {
	@Id
	@Column(name = "METRIC_GROUP_HISTORY_ID")
	private BigInteger metricGroupHistoryId;

	@Column(name = "METRIC_GROUP_ID")
	private BigInteger metricGroupId;

	@Column(name = "PRIORITY")
	private int priority;

	@Column(name = "START_TIME")
	private Date startTime;

	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "DURATION_MSEC")
	private BigInteger durationMsec;

	@Column(name = "TIME_REMAINING")
	private BigInteger timeRemaining;

	public Object getId() {
		return getMetricGroupHistoryId();
	}

	public BigInteger getMetricGroupHistoryId() {
		return metricGroupHistoryId;
	}

	public BigInteger getMetricGroupId() {
		return metricGroupId;
	}

	public int getPriority() {
		return priority;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public BigInteger getDurationMsec() {
		return durationMsec;
	}

	public BigInteger getTimeRemaining() {
		return timeRemaining;
	}

	public void setMetricGroupHistoryId(BigInteger metricGroupHistoryId) {
		this.metricGroupHistoryId = metricGroupHistoryId;
	}

	public void setMetricGroupId(BigInteger metricGroupId) {
		this.metricGroupId = metricGroupId;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setDurationMsec(BigInteger durationMsec) {
		this.durationMsec = durationMsec;
	}

	public void setTimeRemaining(BigInteger timeRemaining) {
		this.timeRemaining = timeRemaining;
	}
}
