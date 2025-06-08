package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricGroupHistory;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricGroupHistoryDao extends
		IDaoService<MetricGroupHistory, BigInteger> {
	public List<MetricGroupHistory> findAllByMetricGroupId(
			BigInteger metricGroupId);
	public List<MetricGroupHistory> findAllByPriority(int priority);
	public List<MetricGroupHistory> findAllByMinimumDuration(
			BigInteger minimumDuration);
	public List<MetricGroupHistory> findAllByMaximumTimeRemaining(
			BigInteger maximumTimeRemaining);
}
