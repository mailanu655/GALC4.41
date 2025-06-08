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
@Table(name = "MTX_GROUP_METRICS_TBX")
public class MetricGroupMetrics extends AuditEntry {
	@Id
	@Column(name = "METRIC_GROUP_METRICS_ID")
	private BigInteger metricGroupMetricsId;

	@Column(name = "METRIC_GROUP_ID")
	private BigInteger metricGroupId;

	@Column(name = "METRIC_ID")
	private BigInteger metricId;

	@Column(name = "STRATEGY")
	private String strategy;

	@Column(name = "LIMIT_LOWER")
	private double limitLower;

	@Column(name = "LIMIT_UPPER")
	private double limitUpper;

	public Object getId() {
		return getMetricGroupMetricsId();
	}

	public BigInteger getMetricGroupMetricsId() {
		return metricGroupMetricsId;
	}

	public BigInteger getMetricGroupName() {
		return metricGroupId;
	}

	public BigInteger getMetricId() {
		return metricId;
	}

	public String getStrategy() {
		return strategy;
	}

	public double getLimitLower() {
		return limitLower;
	}

	public double getLimitUpper() {
		return limitUpper;
	}

	public void setMetricGroupMetricsId(BigInteger metricGroupMetricsId) {
		this.metricGroupMetricsId = metricGroupMetricsId;
	}

	public void setMetricGroupName(BigInteger metricGroupName) {
		this.metricGroupId = metricGroupName;
	}

	public void setMetricId(BigInteger metricId) {
		this.metricId = metricId;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public void setLimitLower(double limitLower) {
		this.limitLower = limitLower;
	}

	public void setLimitUpper(double limitUpper) {
		this.limitUpper = limitUpper;
	}
}
