package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricGroupMetrics;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricGroupMetricsDao extends
		IDaoService<MetricGroupMetrics, BigInteger> {
	public List<MetricGroupMetrics> findAllByMetricGroupId(
			BigInteger metricGroupId);
	public List<MetricGroupMetrics> findAllByMetricId(BigInteger metricId);
	public List<MetricGroupMetrics> findAllByMetricGroupIdAndMetricId(
			BigInteger metricGroupId, BigInteger metricId);
	public List<MetricGroupMetrics> findAllByStrategy(String strategy);
}
