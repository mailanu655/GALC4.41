package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricTypeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricType;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricTypeDaoImpl extends BaseDaoImpl<MetricType, BigInteger>
		implements MetricTypeDao {
	public MetricType findFirstByMetricTypeName(String metricTypeName) {
		return findFirst(Parameters.with("metricTypeName", metricTypeName));
	}

	public List<MetricType> findAllByMetricTypeStrategy(
			String metricTypeStrategy) {
		return findAll(Parameters
				.with("metricTypeStrategy", metricTypeStrategy));
	}
}
