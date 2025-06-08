package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricGroup;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricGroupDao extends IDaoService<MetricGroup, BigInteger> {
	public MetricGroup findFirstByMetricGroupName(String metricGroupName);	
	public List<MetricGroup> findAllByStrategy(String strategy);
	public List<MetricGroup> findAllByMetricGroupTypeId(BigInteger metricGroupTypeId);
	public List<MetricGroup> findAllByPriority(int priority);
	public List<MetricGroup> findAllByMinimumDuration(BigInteger minimumDuration);
	public List<MetricGroup> findAllByMaximumTimeRemaining(
			BigInteger maximumTimeRemaining);
	public List<MetricGroup> findAllByDisplayType(String displayType);
	public List<MetricGroup> findAllActiveOrdered();
}
