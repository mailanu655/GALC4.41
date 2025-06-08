package com.honda.galc.entity.conf;

import java.math.BigInteger;

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
@Table(name = "MTX_GROUP_TYPE_TBX")
public class MetricGroupType extends AuditEntry {
	@Id
	@Column(name = "METRIC_GROUP_TYPE_ID")
	private BigInteger metricGroupTypeId;

	@Column(name = "METRIC_GROUP_TYPE_NAME")
	private String metricGroupTypeName;

	@Column(name = "METRIC_GROUP_TYPE_STRATEGY")
	private String metricGroupTypeStrategy;

	@Column(name = "METRIC_SCHEDULE_ID")
	private BigInteger metricScheduleId;

	public Object getId() {
		return getMetricGroupTypeId();
	}

	public BigInteger getMetricGroupTypeId() {
		return metricGroupTypeId;
	}

	public String getMetricGroupTypeName() {
		return metricGroupTypeName;
	}

	public String getMetricGroupTypeStrategy() {
		return metricGroupTypeStrategy;
	}

	public BigInteger getMetricScheduleId() {
		return metricScheduleId;
	}

	public void setMetricGroupTypeId(BigInteger metricGroupTypeId) {
		this.metricGroupTypeId = metricGroupTypeId;
	}

	public void setMetricGroupTypeName(String metricGroupTypeName) {
		this.metricGroupTypeName = metricGroupTypeName;
	}

	public void setMetricGroupTypeStrategy(String metricGroupTypeStrategy) {
		this.metricGroupTypeStrategy = metricGroupTypeStrategy;
	}

	public void setMetricScheduleId(BigInteger metricScheduleId) {
		this.metricScheduleId = metricScheduleId;
	}
}
