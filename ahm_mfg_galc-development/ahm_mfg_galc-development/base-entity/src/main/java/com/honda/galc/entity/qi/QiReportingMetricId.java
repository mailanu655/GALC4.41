package com.honda.galc.entity.qi;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiReportingMetricId Class description</h3>
 * <p>
 * QiReportingMetricId contains the getter and setter of the QiReportingMetric mapping composite key and maps this class with database and these columns .
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *        Jul 26, 2016
 * 
 */
@Embeddable
public class QiReportingMetricId  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "METRIC_NAME", nullable=false)
	private String metricName;
	
	@Column(name = "LEVEL", nullable=false)
	private String level;
	
	
	
	public QiReportingMetricId() {
		super();
	}
	
	public QiReportingMetricId(String metricName,String level) {
		this.setMetricName(metricName);
		this.setLevel(level);
	}
	
	public String getMetricName() {
		return StringUtils.trimToEmpty(metricName);
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getLevel() {
		return StringUtils.trimToEmpty(level);
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result
				+ ((metricName == null) ? 0 : metricName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiReportingMetricId other = (QiReportingMetricId) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (metricName == null) {
			if (other.metricName != null)
				return false;
		} else if (!metricName.equals(other.metricName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
	
   
}
