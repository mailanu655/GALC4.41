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
@Table(name = "MTX_TYPE_PARAM_TBX")
public class MetricTypeParam extends AuditEntry {
	@Id
	@Column(name = "METRIC_TYPE_PARAM_ID")
	private BigInteger metricTypeParamId;

	@Column(name = "METRIC_TYPE_ID")
	private BigInteger metricTypeId;

	@Column(name = "METRIC_TYPE_PARAM_NAME")
	private String metricTypeParamName;

	@Column(name = "METRIC_TYPE_PARAM_VALUE")
	private String metricTypeParamValue;

	public Object getId() {
		return getMetricTypeParamId();
	}
	
	public BigInteger getMetricTypeParamId() {
		return metricTypeParamId;
	}

	public BigInteger getMetricTypeId() {
		return metricTypeId;
	}

	public String getMetricTypeParamName() {
		return metricTypeParamName;
	}

	public String getMetricTypeParamValue() {
		return metricTypeParamValue;
	}

	public void setMetricTypeParamId(BigInteger metricTypeParamId) {
		this.metricTypeParamId = metricTypeParamId;
	}

	public void setMetricTypeId(BigInteger metricTypeId) {
		this.metricTypeId = metricTypeId;
	}

	public void setMetricTypeParamName(String metricTypeParamName) {
		this.metricTypeParamName = metricTypeParamName;
	}

	public void setMetricTypeParamValue(String metricTypeParamValue) {
		this.metricTypeParamValue = metricTypeParamValue;
	}
}
