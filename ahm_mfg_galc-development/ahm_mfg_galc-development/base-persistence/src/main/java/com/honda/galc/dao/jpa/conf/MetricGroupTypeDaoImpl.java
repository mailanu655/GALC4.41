package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricGroupTypeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricGroupType;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricGroupTypeDaoImpl extends
		BaseDaoImpl<MetricGroupType, BigInteger> implements MetricGroupTypeDao {
	public MetricGroupType findFirstByMetricGroupTypeName(
			String metricGroupTypeName) {
		return findFirst(Parameters.with("metricGroupTypeName",
				metricGroupTypeName));
	}

	public List<MetricGroupType> findAllByStrategy(String strategy) {
		return findAll(Parameters.with("metricGroupTypeStrategy", strategy));
	}

	public MetricGroupType findFirstByMetricScheduleId(
			BigInteger metricScheduleId) {
		return findFirst(Parameters.with("metricScheduleId", metricScheduleId));
	}
	
	public List<MetricGroupType> findAllByMetricScheduleId(
			BigInteger metricScheduleId) {
		return findAll(Parameters.with("metricScheduleId", metricScheduleId));
	}
}
