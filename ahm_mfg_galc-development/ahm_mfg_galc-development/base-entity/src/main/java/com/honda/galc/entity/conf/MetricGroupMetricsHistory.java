package com.honda.galc.entity.conf;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Todd Roling
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MTX_GROUP_METRICS_HISTORY_TBX")
public class MetricGroupMetricsHistory extends AuditEntry {
	@EmbeddedId
	private MetricGroupMetricsHistoryId id;

	public MetricGroupMetricsHistoryId getId() {
		return id;
	}

	public void setId(MetricGroupMetricsHistoryId id) {
		this.id = id;
	}
}
