package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricType;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricTypeDao extends IDaoService<MetricType, BigInteger> {
	public MetricType findFirstByMetricTypeName(String metricTypeName);
	public List<MetricType> findAllByMetricTypeStrategy(
			String metricTypeStrategy);
}
