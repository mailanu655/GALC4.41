package com.honda.galc.dao.jpa.qi;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiReportingMetricDao;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.entity.qi.QiReportingMetric;
import com.honda.galc.entity.qi.QiReportingMetricId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>QiReportingMetricDaoImpl</code>
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
public class QiReportingMetricDaoImpl extends BaseDaoImpl<QiReportingMetric, QiReportingMetricId> implements QiReportingMetricDao {
	
	private final static String FIND_MISSING_REGIONAL_METRIC_LIST = " SELECT REGIONAL_VALUE_NAME FROM REGIONAL_CODE_TBX "
			+ "WHERE REGIONAL_CODE_NAME = ?1 EXCEPT SELECT DISTINCT METRIC_NAME FROM QI_REPORTING_METRIC_TBX";

	public List<QiReportingMetric> findAllByLevel(String level) {
		return findAll(Parameters.with("id.level", level));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findMissingRegionalMetricList() {
		return findResultListByNativeQuery(FIND_MISSING_REGIONAL_METRIC_LIST, Parameters.with("1", RegionalCodeName.REGIONAL_METRIC.getName()));
	}

	public List<QiReportingMetric> findAllByMetricName(String metricName) {
		return findAll(Parameters.with("id.metricName", metricName));
	}
}
