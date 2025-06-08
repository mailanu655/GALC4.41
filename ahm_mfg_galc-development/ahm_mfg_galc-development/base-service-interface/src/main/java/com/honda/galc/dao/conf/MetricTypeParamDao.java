package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricTypeParam;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricTypeParamDao extends
		IDaoService<MetricTypeParam, BigInteger> {
	public MetricTypeParam findFirstByMetricTypeParamName(
			String metricTypeParamName);
	public List<MetricTypeParam> findAllByMetricTypeId(
			BigInteger metricTypeId);
}
