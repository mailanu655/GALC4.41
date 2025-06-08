/**
 * 
 */
package com.honda.galc.entity.conf;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Todd Roling
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MTX_SCHEDULE_TBX")
public class MetricSchedule extends AuditEntry {
	@Id
	@Column(name = "METRIC_SCHEDULE_ID")
	private BigInteger metricScheduleId;

	@Column(name = "METRIC_SCHEDULE_NAME")
	private String metricScheduleName;

	@Column(name = "METRIC_SCHEDULE")
	private String metricSchedule;

	@Column(name = "EFFECTIVE_START")
	private Date effectiveStart;

	@Column(name = "EFFECTIVE_END")
	private Date effectiveEnd;

	public Object getId() {
		return getMetricScheduleId();
	}

	public BigInteger getMetricScheduleId() {
		return metricScheduleId;
	}

	public String getMetricScheduleName() {
		return metricScheduleName;
	}

	public String getMetricSchedule() {
		return metricSchedule;
	}

	public Date getEffectiveStart() {
		return effectiveStart;
	}

	public Date getEffectiveEnd() {
		return effectiveEnd;
	}

	public void setMetricScheduleId(BigInteger metricScheduleId) {
		this.metricScheduleId = metricScheduleId;
	}

	public void setMetricScheduleName(String metricScheduleName) {
		this.metricScheduleName = metricScheduleName;
	}

	public void setMetricSchedule(String metricSchedule) {
		this.metricSchedule = metricSchedule;
	}

	public void setEffectiveStart(Date effectiveStart) {
		this.effectiveStart = effectiveStart;
	}

	public void setEffectiveEnd(Date effectiveEnd) {
		this.effectiveEnd = effectiveEnd;
	}
}
