package com.honda.galc.entity.conf;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Todd Roling
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MTX_TBX")
@SequenceGenerator(name = "hminMetricSeq", sequenceName = "MTX_UNIVERSALSEQ", allocationSize = 1)
public class Metric extends AuditEntry {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hminMetricSeq")
	@Column(name = "METRIC_ID")
	private BigInteger metricId;

	@Column(name = "METRIC_NAME")
	private String metricName;

	@Column(name = "METRIC_DESC")
	private String metricDesc;

	@Column(name = "STRATEGY")
	private String strategy;

	@Column(name = "METRIC_TYPE_ID")
	private BigInteger metricTypeId;

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
		return getMetricId();
	}

	public BigInteger getMetricId() {
		return metricId;
	}

	public String getMetricName() {
		return metricName;
	}

	public String getMetricDesc() {
		return metricDesc;
	}

	public String getStrategy() {
		return strategy;
	}

	public BigInteger getMetricTypeId() {
		return metricTypeId;
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

	public void setMetricId(BigInteger metricId) {
		this.metricId = metricId;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public void setMetricDesc(String metricDesc) {
		this.metricDesc = metricDesc;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public void setMetricTypeId(BigInteger metricTypeId) {
		this.metricTypeId = metricTypeId;
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
