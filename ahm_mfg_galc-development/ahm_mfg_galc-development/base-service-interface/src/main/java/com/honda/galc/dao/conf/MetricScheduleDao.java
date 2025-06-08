package com.honda.galc.dao.conf;

import java.math.BigInteger;

import com.honda.galc.entity.conf.MetricSchedule;
import com.honda.galc.service.IDaoService;

/**
 * @author Todd Roling
 */
public interface MetricScheduleDao extends
		IDaoService<MetricSchedule, BigInteger> {
	public MetricSchedule findFirstByScheduleName(String metricScheduleName);
}