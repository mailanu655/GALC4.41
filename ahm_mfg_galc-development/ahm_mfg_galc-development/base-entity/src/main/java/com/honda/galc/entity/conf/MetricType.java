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
@Table(name = "MTX_TYPE_TBX")
public class MetricType extends AuditEntry {
	@Id
	@Column(name = "METRIC_TYPE_ID")
	private BigInteger metricTypeId;

	@Column(name = "METRIC_TYPE_NAME")
	private String metricTypeName;

	@Column(name = "METRIC_TYPE_STRATEGY")
	private String metricTypeStrategy;

	public Object getId() {
		return getMetricTypeId();
	}
	
	public BigInteger getMetricTypeId() {
		return metricTypeId;
	}

	public String getMetricTypeName() {
		return metricTypeName;
	}

	public String getMetricTypeStrategy() {
		return metricTypeStrategy;
	}

	public void setMetricTypeId(BigInteger metricTypeId) {
		this.metricTypeId = metricTypeId;
	}

	public void setMetricTypeName(String metricTypeName) {
		this.metricTypeName = metricTypeName;
	}

	public void setMetricTypeStrategy(String metricTypeStrategy) {
		this.metricTypeStrategy = metricTypeStrategy;
	}
}
