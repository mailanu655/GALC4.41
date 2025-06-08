package com.honda.galc.dao.jpa.conf;

import java.math.BigInteger;
import java.util.List;

import com.honda.galc.dao.conf.MetricGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MetricGroup;
import com.honda.galc.service.Parameters;

/**
 * @author Todd Roling
 */
public class MetricGroupDaoImpl extends BaseDaoImpl<MetricGroup, BigInteger>
		implements MetricGroupDao {

	public MetricGroup findFirstByMetricGroupName(String metricGroupName) {
		return findFirst(Parameters.with("metricGroupName", metricGroupName));
	}

	public List<MetricGroup> findAllByStrategy(String strategy) {
		return findAll(Parameters.with("strategy", strategy));
	}

	public List<MetricGroup> findAllByMetricGroupTypeId(
			BigInteger metricGroupTypeId) {
		return findAll(Parameters.with("metricGroupTypeId", metricGroupTypeId));
	}

	public List<MetricGroup> findAllByPriority(int priority) {
		return findAll(Parameters.with("priority", priority));
	}

	public List<MetricGroup> findAllByMinimumDuration(BigInteger minimumDuration) {
		return findAllByQuery(getFindAllSql() + " where e.durationMsec > "
				+ minimumDuration);
	}

	public List<MetricGroup> findAllByMaximumTimeRemaining(
			BigInteger maximumTimeRemaining) {
		return findAllByQuery(getFindAllSql() + " where e.timeRemainingMsec < "
				+ maximumTimeRemaining);
	}

	public List<MetricGroup> findAllByDisplayType(String displayType) {
		return findAll(Parameters.with("displayType", displayType));
	}

	/**
	 * @return Returns all active Metric Groups ordered by lowest priority
	 *         value, then by highest duration value.
	 */
	public List<MetricGroup> findAllActiveOrdered() {
		return findAllByQuery(getFindAllSql()
				+ " where e.endTime is null order by e.priority asc, e.timeRemainingMsec asc");
	}
}
