package com.honda.galc.dao.qi;

import java.sql.Date;
import java.time.ZoneId;
import java.util.List;

import com.honda.galc.entity.qi.QiReportingTarget;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>QiReportingTargetDao</code>
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
public interface QiReportingTargetDao extends IDaoService<QiReportingTarget, Integer> {

	/**
	 * Method will fetch all the target items based on given filter.
	 * 
	 * @param site
	 * @param plant
	 * @param productType
	 * @param model
	 * @param demandType
	 * @param target
	 * @param metricName
	 * @param startDate
	 * @param endDate
	 * @param department
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetByFilter(String site, String plant, String productType, String modelGroup, 
			String modelYear, String demandType, String target, String metricName, Date startDate, Date endDate, String department);

	/**
	 * This method will return all target items filtered by metric name.
	 * 
	 * @param metricName
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetByMetricName(String metricName);

	/**
	 * This method will return all target items filtered by metric name and level.
	 * 
	 * @param metricName
	 * @param level
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetByMetricNameAndLevel(String metricName, String level);

	/**
	 * This method will be used to update all the target with new metric name.
	 * 
	 * @param oldMetricName
	 * @param newMetricName
	 * @param userId
	 */
	public void updateAllTargetByMetricName(String oldMetricName, String newMetricName, String userId);

	public List<QiReportingTarget> findAllByDepartmentAndDate(String deptName);
	
	public ZoneId getZoneId();

	public void saveTargetsByNativeQuery(String targetListString);
	
	public void deleteTargetsByNativeQuery(String site, String plant, String productType, String modelGroup, String modelYear,
			String demandType, String target, String metricName, Date startDate, Date endDate, String department);
}