package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>QiReportingMetric</code>
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>15/11/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
@Entity
@Table(name = "QI_REPORTING_METRIC_TBX")
public class QiReportingMetric extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey=true,sequence=1)
	private QiReportingMetricId id;

	@Column(name = "METRIC_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String metricDescription;

	@Column(name = "METRIC_DATA_FORMAT")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String metricDataFormat;

	public QiReportingMetric() {
		super();
	}
	
	public QiReportingMetricId getId() {
		return this.id;
	}

	public void setId(QiReportingMetricId id) {
		this.id = id;
	}

	public QiReportingMetric(String metricName,String level) {
	    this.id = new QiReportingMetricId(metricName,level);
	}

	public String getMetricDescription() {
		return StringUtils.trimToEmpty(metricDescription);
	}

	public void setMetricDescription(String metricDescription) {
		this.metricDescription = metricDescription;
	}

	public String getMetricDataFormat() {
		return StringUtils.trimToEmpty(metricDataFormat);
	}

	public void setMetricDataFormat(String metricDataFormat) {
		this.metricDataFormat = metricDataFormat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((metricDataFormat == null) ? 0 : metricDataFormat.hashCode());
		result = prime
				* result
				+ ((metricDescription == null) ? 0 : metricDescription
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiReportingMetric other = (QiReportingMetric) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (metricDataFormat == null) {
			if (other.metricDataFormat != null)
				return false;
		} else if (!metricDataFormat.equals(other.metricDataFormat))
			return false;
		if (metricDescription == null) {
			if (other.metricDescription != null)
				return false;
		} else if (!metricDescription.equals(other.metricDescription))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiReportingMetric [id=" + id + ", metricDescription="
				+ metricDescription + ", metricDataFormat=" + metricDataFormat
				+ "]";
	}

}
