package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricHistoryDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricHistory;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricHistoryDaoImpl extends
		BaseDaoImpl<MetricHistory, BigInteger> implements MetricHistoryDao {
	public List<MetricHistory> findAllByMetricId(BigInteger metricId) {
		return findAll(Parameters.with("metricId", metricId));
	}

	public List<MetricHistory> findAllByProcessPointId(String processPointId) {
		return findAll(Parameters.with("processPointId", processPointId));
	}

	public List<MetricHistory> findAllByScheduleId(String scheduleId) {
		return findAll(Parameters.with("scheduleId", scheduleId));
	}
}
