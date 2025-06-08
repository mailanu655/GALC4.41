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
@Table(name = "MTX_GROUP_TYPE_PARAM_TBX")
public class MetricGroupTypeParam extends AuditEntry {
	@Id
	@Column(name = "METRIC_GROUP_TYPE_PARAM_ID")
	private BigInteger metricGroupTypeParamId;

	@Column(name = "METRIC_GROUP_TYPE_ID")
	private BigInteger metricGroupTypeId;

	@Column(name = "METRIC_GROUP_TYPE_PARAM_NAME")
	private String metricGroupTypeParamName;

	@Column(name = "METRIC_GROUP_TYPE_PARAM_VALUE")
	private String metricGroupTypeParamValue;

	public Object getId() {
		return getMetricGroupTypeParamId();
	}

	public BigInteger getMetricGroupTypeParamId() {
		return metricGroupTypeParamId;
	}

	public BigInteger getMetricGroupTypeId() {
		return metricGroupTypeId;
	}

	public String getMetricGroupTypeParamName() {
		return metricGroupTypeParamName;
	}

	public String getMetricGroupTypeParamValue() {
		return metricGroupTypeParamValue;
	}

	public void setMetricGroupTypeParamId(BigInteger metricGroupTypeParamId) {
		this.metricGroupTypeParamId = metricGroupTypeParamId;
	}

	public void setMetricGroupTypeId(BigInteger metricGroupTypeId) {
		this.metricGroupTypeId = metricGroupTypeId;
	}

	public void setMetricGroupTypeParamName(String metricGroupTypeParamName) {
		this.metricGroupTypeParamName = metricGroupTypeParamName;
	}

	public void setMetricGroupTypeParamValue(String metricGroupTypeParamValue) {
		this.metricGroupTypeParamValue = metricGroupTypeParamValue;
	}
}
