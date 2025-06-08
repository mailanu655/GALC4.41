package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricGroupType;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricGroupTypeDao extends
		IDaoService<MetricGroupType, BigInteger> {
	public MetricGroupType findFirstByMetricGroupTypeName(
			String metricGroupTypeName);
	public List<MetricGroupType> findAllByStrategy(String strategy);
	public MetricGroupType findFirstByMetricScheduleId(
			BigInteger metricScheduleId);
	public List<MetricGroupType> findAllByMetricScheduleId(
			BigInteger metricScheduleId);
}
