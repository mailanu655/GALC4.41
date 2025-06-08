package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;

import com.honda.galc.dao.conf.MetricScheduleDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricSchedule;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricScheduleDaoImpl extends
		BaseDaoImpl<MetricSchedule, BigInteger> implements
		MetricScheduleDao {
	public MetricSchedule findFirstByScheduleName(String metricScheduleName) {
		return findFirst(Parameters.with("metricScheduleName", metricScheduleName));
	}
}