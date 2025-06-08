package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class MetricGroupMetricsHistoryId implements Serializable {
	@Column(name = "METRIC_GROUP_HISTORY_ID")
	private BigInteger metricGroupHistoryId;

	@Column(name = "METRIC_HISTORY_ID")
	private BigInteger metricHistoryId;

	public BigInteger getMetricGroupHistoryId() {
		return metricGroupHistoryId;
	}

	public BigInteger getMetricHistoryId() {
		return metricHistoryId;
	}

	public void setMetricGroupHistoryId(BigInteger metricGroupHistoryId) {
		this.metricGroupHistoryId = metricGroupHistoryId;
	}

	public void setMetricHistoryId(BigInteger metricHistoryId) {
		this.metricHistoryId = metricHistoryId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof MetricGroupMetricsHistoryId)) {
			return false;
		}
		MetricGroupMetricsHistoryId other = (MetricGroupMetricsHistoryId) o;
		return getMetricGroupHistoryId()
				.equals(other.getMetricGroupHistoryId())
				&& getMetricHistoryId().equals(other.getMetricHistoryId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + getMetricGroupHistoryId().hashCode();
		hash = hash * prime + getMetricHistoryId().hashCode();
		return hash;
	}

}
