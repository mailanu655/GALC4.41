package com.honda.galc.dao.jpa.qi;

import java.sql.Date;
import java.time.ZoneId;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiReportingTargetDao;
import com.honda.galc.entity.qi.QiReportingTarget;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>QiReportingTargetDaoImpl</code>
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
public class QiReportingTargetDaoImpl extends BaseDaoImpl<QiReportingTarget, Integer> implements QiReportingTargetDao {

	private static final String FIND_ALL_TARGETS_BY_FILTER = "SELECT reportingTarget FROM QiReportingTarget reportingTarget WHERE reportingTarget.site = :site "
			+ "AND reportingTarget.plant = :plant AND reportingTarget.productType = :productType AND reportingTarget.demandType = :demandType ";
	
	private static final String FIND_ALL_TARGETS_BY_METRIC_NAME = "SELECT reportingTarget FROM QiReportingTarget reportingTarget WHERE reportingTarget.metricName = :metricName";

	private static final String FIND_ALL_TARGETS_BY_METRIC_NAME_AND_LEVEL = "SELECT reportingTarget FROM QiReportingTarget reportingTarget WHERE reportingTarget.metricName = :metricName AND reportingTarget.level = :level";

	private static final String UPDATE_ALL_METRIC_NAME = "UPDATE GALADM.QI_REPORTING_TARGET_TBX SET METRIC_NAME = ?1 , UPDATE_USER = ?2 WHERE  METRIC_NAME = ?3";
	
	private static final String FIND_ALL_TARGETS_BY_DEPARTMENT = "SELECT reportingTarget FROM QiReportingTarget reportingTarget WHERE (reportingTarget.department = :department" +
			"  OR (reportingTarget.target='Department' and reportingTarget.targetItem =:department))" +
			"	AND reportingTarget.effectiveDate>= :todaysDate";
	
	private static final String INSERT_TARGETS = "insert into GALADM.QI_REPORTING_TARGET_TBX (SITE, PLANT, DEPT, PRODUCT_TYPE, MODEL_GROUP, SYSTEM, "
			+ "MODEL_YEAR_DESCRIPTION, DEMAND_TYPE, METRIC_NAME, LEVEL, METRIC_VALUE, EFFECTIVE_DATE, TARGET, TARGET_ITEM, CREATE_USER, CALCULATED_METRIC_VALUE) values ";	
	
	private static final String DELETE_TARGETS = "delete from GALADM.QI_REPORTING_TARGET_TBX where site=?1 and plant=?2 and product_type=?3 and demand_type=?4";

	public static final String TARGET_PLANT = "Plant";
	public static final String TARGET_DEPARTMENT = "Department";
	public static final String TARGET_DEPT_RESP_LEVEL = "Dept Resp Level";
	public static final String TARGET_RESPONSIBLE_LEVEL_1 = "Responsible Level 1";	
	public static final String TARGET_RESPONSIBLE_LEVEL_2 = "Responsible Level 2";
	public static final String TARGET_RESPONSIBLE_LEVEL_3 = "Responsible Level 3";
	
	/**
	 * Method will fetch all the target items based on given filter.
	 * 
	 * @param site
	 * @param plant
	 * @param productType
	 * @param modelGroup
	 * @param demandType
	 * @param target
	 * @param metricName
	 * @param startDate
	 * @param endDate
	 * @param department
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetByFilter(String site, String plant, String productType, String modelGroup, String modelYear,
			String demandType, String target, String metricName, Date startDate, Date endDate, String department) {

		Parameters params = Parameters.with("site", site).put("plant", plant).put("productType", productType).put("demandType", demandType);
		StringBuilder findTargetItemForSelectedValue = new StringBuilder(FIND_ALL_TARGETS_BY_FILTER);

		if (StringUtils.isNotBlank(modelGroup)) {
			params.put("modelGroup", modelGroup);
			findTargetItemForSelectedValue.append(" AND reportingTarget.modelGroup = :modelGroup ");
		}
		
		if (StringUtils.isNotBlank(modelYear)) {
			params.put("modelYearDescription", modelYear);
			findTargetItemForSelectedValue.append(" AND reportingTarget.modelYearDescription = :modelYearDescription ");
		}

		if (StringUtils.isNotBlank(target)) {
			if(target.equalsIgnoreCase("Dept Resp Level")){
				findTargetItemForSelectedValue.append(" AND reportingTarget.target in ('Responsible Level 1','Responsible Level 2','Responsible Level 3') ");
			}
			else
			{
				params.put("target", target);
				findTargetItemForSelectedValue.append(" AND reportingTarget.target = :target ");
			}
			
		}

		if (StringUtils.isNotBlank(department)) {
			params.put("department", department);
			findTargetItemForSelectedValue.append(" AND reportingTarget.department = :department ");
		}
		
		if (StringUtils.isNotBlank(metricName)) {
			params.put("metricName", metricName);
			findTargetItemForSelectedValue.append(" AND reportingTarget.metricName = :metricName ");
		}
		if (startDate != null && endDate != null) {
			findTargetItemForSelectedValue
					.append(" AND reportingTarget.effectiveDate BETWEEN '" + startDate + "' AND '" + endDate+"'");
		}

		findTargetItemForSelectedValue.append(" ORDER BY reportingTarget.effectiveDate DESC");
		return findAllByQuery(findTargetItemForSelectedValue.toString(), params);
	}
	
	/**
	 * This method will return all target items filtered by metric name.
	 * 
	 * @param metricName
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetByMetricName(String metricName) {
		Parameters params = Parameters.with("metricName", metricName);
		return findAllByQuery(FIND_ALL_TARGETS_BY_METRIC_NAME.toString(), params);
	}

	/**
	 * This method will return all target items filtered by metric name and level.
	 * 
	 * @param metricName
	 * @param level
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetByMetricNameAndLevel(String metricName, String level) {
		Parameters params = Parameters.with("metricName", metricName).put("level", level);
		return findAllByQuery(FIND_ALL_TARGETS_BY_METRIC_NAME_AND_LEVEL.toString(), params);
	}

	/**
	 * This method will be used to update all the target with new metric name.
	 * 
	 * @param oldMetricName
	 * @param newMetricName
	 * @param userId
	 */
	@Transactional
	public void updateAllTargetByMetricName(String oldMetricName, String newMetricName, String userId) {

		Parameters params = Parameters.with("1", newMetricName).put("2", userId).put("3", oldMetricName);
		executeNativeUpdate(UPDATE_ALL_METRIC_NAME, params);

	}

	public List<QiReportingTarget> findAllByDepartmentAndDate(String deptName) {
		Parameters params = Parameters.with("department", deptName).put("todaysDate",new java.sql.Date(new java.util.Date().getTime()));
		return findAllByQuery(FIND_ALL_TARGETS_BY_DEPARTMENT.toString(), params);
	}
	
	public ZoneId getZoneId() {
		return ZoneId.systemDefault();
	}
	
	/**
	 * This method will be used to create the targets with native query for better performance
	 * 
	 * @param target list in String
	 */
	@Transactional
	public void saveTargetsByNativeQuery(String targetListString) {
		Parameters params = new Parameters();
		executeNative(INSERT_TARGETS + targetListString, params);
	}
	
	/**
	 * This method will be used to delete the targets with native query for better performance
	 * 
	 */
	@Transactional
	public void deleteTargetsByNativeQuery(String site, String plant, String productType, String modelGroup, String modelYear,
			String demandType, String target, String metricName, Date startDate, Date endDate, String department) {

		Parameters params = Parameters.with("1", site).put("2", plant).put("3", productType).put("4", demandType);
		StringBuilder query = new StringBuilder(DELETE_TARGETS);

		if (StringUtils.isNotBlank(modelGroup)) {
			params.put("5", modelGroup);
			query.append(" and model_group = ?5");
		}
		
		if (StringUtils.isNotBlank(modelYear)) {
			params.put("6", modelYear);
			query.append(" and model_year_description = ?6");
		}

		if (StringUtils.isNotBlank(target)) {
			if(target.equalsIgnoreCase("Dept Resp Level")){
				query.append(" and target in ('Responsible Level 1','Responsible Level 2','Responsible Level 3') ");
			} else {
				params.put("7", target);
				query.append(" and target = ?7");
			}
		}

		if (StringUtils.isNotBlank(department)) {
			params.put("8", department);
			query.append(" and dept = ?8");
		}
		
		if (StringUtils.isNotBlank(metricName)) {
			params.put("9", metricName);
			query.append(" and metric_name = ?9");
		}
		
		if (startDate != null && endDate != null) {
			query.append(" and effective_date between '" + startDate + "' and '" + endDate+"'");
		}
		
		executeNative(query.toString(), params);
	}
}