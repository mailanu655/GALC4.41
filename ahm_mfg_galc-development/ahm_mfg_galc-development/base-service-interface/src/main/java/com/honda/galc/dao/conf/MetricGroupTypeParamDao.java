package com.honda.galc.dao.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.entity.conf.MetricGroupTypeParam;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricGroupTypeParamDao extends
		IDaoService<MetricGroupTypeParam, BigInteger> {
	public MetricGroupTypeParam findFirstByMetricGroupTypeParamName(
			String metricGroupTypeParamName);
	public List<MetricGroupTypeParam> findAllByMetricGroupTypeId(
			BigInteger metricGroupTypeId);
	public MetricGroupTypeParam findFirstLikeMetricGroupTypeParamName(
			String likeMetricGroupTypeParamName);
}
