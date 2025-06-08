package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricGroupHistoryDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricGroupHistory;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricGroupHistoryDaoImpl extends
		BaseDaoImpl<MetricGroupHistory, BigInteger> implements
		MetricGroupHistoryDao {
	public List<MetricGroupHistory> findAllByMetricGroupId(
			BigInteger metricGroupId) {
		return findAll(Parameters.with("metricGroupId", metricGroupId));
	}

	public List<MetricGroupHistory> findAllByPriority(int priority) {
		return findAll(Parameters.with("priority", priority));
	}

	public List<MetricGroupHistory> findAllByMinimumDuration(
			BigInteger minimumDuration) {
		return findAllByQuery(getFindAllSql() + " where e.durationMsec > "
				+ minimumDuration);
	}

	public List<MetricGroupHistory> findAllByMaximumTimeRemaining(
			BigInteger maximumTimeRemaining) {
		return findAllByQuery(getFindAllSql() + " where e.timeRemainingMsec < "
				+ maximumTimeRemaining);
	}
}
