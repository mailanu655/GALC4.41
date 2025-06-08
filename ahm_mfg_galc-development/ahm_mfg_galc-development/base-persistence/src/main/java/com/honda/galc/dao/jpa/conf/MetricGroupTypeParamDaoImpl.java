package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricGroupTypeParamDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricGroupTypeParam;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricGroupTypeParamDaoImpl extends
		BaseDaoImpl<MetricGroupTypeParam, BigInteger> implements
		MetricGroupTypeParamDao {
	public List<MetricGroupTypeParam> findAllByMetricGroupTypeId(
			BigInteger metricGroupTypeId) {
		return findAll(Parameters.with("metricGroupTypeId", metricGroupTypeId));
	}

	public MetricGroupTypeParam findFirstByMetricGroupTypeParamName(
			String metricGroupTypeParamName) {
		return findFirst(Parameters.with("metricGroupTypeParamName",
				metricGroupTypeParamName));
	}

	public MetricGroupTypeParam findFirstLikeMetricGroupTypeParamName(
			String likeMetricGroupTypeParamName) {
		return findFirstByQuery(selectClause
				+ " where e.metricGroupTypeParamName like '%"
				+ likeMetricGroupTypeParamName + "%'");
	}
}
