package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Metric;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricDaoImpl extends BaseDaoImpl<Metric, BigInteger> implements
		MetricDao {
	public Metric findFirstByMetricName(String metricName) {
		return findFirst(Parameters.with("metricName", metricName));
	}

	public List<Metric> findAllByStrategy(String strategy) {
		return findAll(Parameters.with("strategy", strategy));
	}

	public List<Metric> findAllByMetricTypeId(BigInteger metricTypeId) {
		return findAll(Parameters.with("metricTypeId", metricTypeId));
	}

	public List<Metric> findAllByProcessPointId(String processPointId) {
		return findAll(Parameters.with("processPointId", processPointId));
	}

	public List<Metric> findAllByScheduleId(String scheduleId) {
		return findAll(Parameters.with("scheduleId", scheduleId));
	}
}
