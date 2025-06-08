package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricTypeParamDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricTypeParam;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricTypeParamDaoImpl extends
		BaseDaoImpl<MetricTypeParam, BigInteger> implements
		MetricTypeParamDao {
	public MetricTypeParam findFirstByMetricTypeParamName(
			String metricTypeParamName) {
		return findFirst(Parameters.with("metricTypeParamName",
				metricTypeParamName));
	}

	public List<MetricTypeParam> findAllByMetricTypeId(
			BigInteger metricTypeId) {
		return findAll(Parameters.with("metricTypeId", metricTypeId));
	}
}
