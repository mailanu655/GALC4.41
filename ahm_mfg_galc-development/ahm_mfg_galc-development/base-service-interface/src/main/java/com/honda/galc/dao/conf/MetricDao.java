package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.Metric;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricDao extends IDaoService<Metric, BigInteger> {
	public Metric findFirstByMetricName(String metricName);
	public List<Metric> findAllByStrategy(String strategy);
	public List<Metric> findAllByMetricTypeId(BigInteger metricTypeId);
	public List<Metric> findAllByProcessPointId(String processPointId);
	public List<Metric> findAllByScheduleId(String scheduleId);
}
