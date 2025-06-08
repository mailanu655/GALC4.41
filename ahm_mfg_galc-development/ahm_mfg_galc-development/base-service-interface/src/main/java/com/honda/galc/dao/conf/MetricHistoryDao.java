package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricHistory;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricHistoryDao extends
		IDaoService<MetricHistory, BigInteger> {
	public List<MetricHistory> findAllByMetricId(BigInteger metricId);
	public List<MetricHistory> findAllByProcessPointId(String processPointId);
	public List<MetricHistory> findAllByScheduleId(String scheduleId);
}
