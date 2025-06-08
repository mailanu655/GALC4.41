package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiReportingMetric;
import com.honda.galc.entity.qi.QiReportingMetricId;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>QiReportingMetricDao</code>
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
public interface QiReportingMetricDao extends IDaoService<QiReportingMetric, QiReportingMetricId> {

	List<QiReportingMetric> findAllByLevel(String level);
	
	public List<String> findMissingRegionalMetricList();
	
	public List<QiReportingMetric> findAllByMetricName(String metricName);
}
