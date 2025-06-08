package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricGroupMetricsDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricGroupMetrics;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricGroupMetricsDaoImpl extends
		BaseDaoImpl<MetricGroupMetrics, BigInteger> implements
		MetricGroupMetricsDao {
	public List<MetricGroupMetrics> findAllByMetricGroupId(
			BigInteger metricGroupId) {
		return findAll(Parameters.with("metricGroupId", metricGroupId));
	}

	public List<MetricGroupMetrics> findAllByMetricId(BigInteger metricId) {
		return findAll(Parameters.with("metricId", metricId));
	}

	public List<MetricGroupMetrics> findAllByMetricGroupIdAndMetricId(
			BigInteger metricGroupId, BigInteger metricId) {
		return findAll(new Parameters().put("metricGroupId", metricGroupId)
				.put("metricId", metricId));
	}

	public List<MetricGroupMetrics> findAllByStrategy(String strategy) {
		return findAll(Parameters.with("strategy", strategy));
	}
}
