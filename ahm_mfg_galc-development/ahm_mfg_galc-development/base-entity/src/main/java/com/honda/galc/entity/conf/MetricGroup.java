package com.honda.galc.entity.conf;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.*;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Todd Roling
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MTX_GROUP_TBX")
@SequenceGenerator(name = "hminMetricGroupSeq", sequenceName = "MTX_UNIVERSALSEQ", allocationSize = 1)
public class MetricGroup extends AuditEntry implements Comparable<MetricGroup> {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hminMetricGroupSeq")
	@Column(name = "METRIC_GROUP_ID")
	private BigInteger metricGroupId;

	@Column(name = "METRIC_GROUP_NAME")
	private String metricGroupName;

	@Column(name = "METRIC_GROUP_DESC")
	private String metricGroupDescription;

	@Column(name = "STRATEGY")
	private String strategy;

	@Column(name = "METRIC_GROUP_TYPE_ID")
	private BigInteger metricGroupTypeId;

	@Column(name = "PRIORITY")
	private int priority;

	@Column(name = "START_TIME")
	private Date startTime;

	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "DURATION_MSEC")
	private BigInteger durationMsec;

	@Column(name = "TIME_REMAINING_MSEC")
	private BigInteger timeRemainingMsec;

	@Column(name = "LOCATION_LEVEL_ID")
	private char[] locationLevelId;

	@Column(name = "DISPLAY_TYPE")
	private String displayType;

	public Object getId() {
		return getMetricGroupId();
	}

	public BigInteger getMetricGroupId() {
		return metricGroupId;
	}

	public String getMetricGroupName() {
		return metricGroupName;
	}

	public String getMetricGroupDescription() {
		return metricGroupDescription;
	}

	public String getStrategy() {
		return strategy;
	}

	public BigInteger getMetricGroupTypeId() {
		return metricGroupTypeId;
	}

	public int getPriority() {
		return priority;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public BigInteger getDurationMsec() {
		return durationMsec;
	}

	public BigInteger getTimeRemainingMsec() {
		return timeRemainingMsec;
	}

	public char[] getLocationLevelId() {
		return locationLevelId;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setMetricGroupId(BigInteger metricGroupId) {
		this.metricGroupId = metricGroupId;
	}

	public void setMetricGroupName(String metricGroupName) {
		this.metricGroupName = metricGroupName;
	}

	public void setMetricGroupDescription(String metricGroupDescription) {
		this.metricGroupDescription = metricGroupDescription;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public void setMetricGroupTypeId(BigInteger metricGroupTypeId) {
		this.metricGroupTypeId = metricGroupTypeId;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setDurationMsec(BigInteger durationMsec) {
		this.durationMsec = durationMsec;
	}

	public void setTimeRemainingMsec(BigInteger timeRemainingMsec) {
		this.timeRemainingMsec = timeRemainingMsec;
	}

	public void setLocationLevelId(char[] locationLevelId) {
		this.locationLevelId = locationLevelId;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	/**
	 * @Returns -1 if this MetricGroup has a lower priority value (higher actual
	 *          priority) than metricGroup.
	 * @Returns 1 if this MetricGroup has a higher priority value (lower actual
	 *          priority) than metricGroup.
	 * @Returns -1 if this MetricGroup and metricGroup have the same priority
	 *          and this MetricGroup has less time remaining (higher actual
	 *          priority).
	 * @Returns 1 if this MetricGroup and metricGroup have the same priority and
	 *          this MetricGroup has more time remaining (lower actual
	 *          priority).
	 * @Returns 0 if this MetricGroup and metricGroup have the same priority and
	 *          time remaining.
	 */
	public int compareTo(MetricGroup metricGroup) {
		if (this.priority < metricGroup.priority)
			return -1;
		if (this.priority > metricGroup.priority)
			return 1;
		if (metricGroup.timeRemainingMsec == null)
			return -1;
		if (this.timeRemainingMsec == null)
			return 1;
		return this.timeRemainingMsec.compareTo(metricGroup.timeRemainingMsec);
	}
}
