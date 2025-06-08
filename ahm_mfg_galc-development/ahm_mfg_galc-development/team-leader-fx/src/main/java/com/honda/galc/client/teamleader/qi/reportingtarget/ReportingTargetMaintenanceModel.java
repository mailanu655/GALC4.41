package com.honda.galc.client.teamleader.qi.reportingtarget;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.product.ModelGroupDao;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiLocalThemeDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiReportingMetricDao;
import com.honda.galc.dao.qi.QiReportingTargetDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiThemeDao;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.ModelGrouping;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.entity.qi.QiReportingMetric;
import com.honda.galc.entity.qi.QiReportingMetricId;
import com.honda.galc.entity.qi.QiReportingTarget;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>ReportingTargetMaintenanceModel</code>
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

public class ReportingTargetMaintenanceModel extends QiModel {

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
	
	private boolean hasLevel2Resp = false;
	private boolean hasLevel3Resp = false;
	private static ZoneId serverZoneId = null;
	private static boolean isServerBeforeClient = false;
	private static int BATCH_SIZE = 10000;
	
	public List<QiReportingTarget> findAllTargetByFilter(String site, String plant, String productType, String modelGroup,
			String modelYear, String demandType, String target, String metricName, Date startDate,
			Date endDate, String department) {

		List<QiReportingTarget> list = getDao(QiReportingTargetDao.class).findAllTargetByFilter(site, plant, productType, modelGroup, modelYear, demandType,
			target, metricName, startDate, endDate, department);
			
		getServerZoneId();

		if (isServerBeforeClient) {
			for (QiReportingTarget t : list) {
				//adjust time zone difference
				t.setEffectiveDate(new java.sql.Date(java.util.Date.from(t.getEffectiveDate().toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
			}
		}
		
		return list;
	}
	
	/**
	 * Find All Plant for selected site
	 * 
	 * @param site
	 */
	public List<String> findAllPlantBySite(String site) {
		return StringUtil.trimStringList(getDao(QiPlantDao.class).findAllPlantBySite(site));
	}

	/**
	 * This method finds all Product Type
	 * 
	 * @return List<String>
	 */
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}
	
	/**
	 * This method finds all Metric data.
	 * 
	 * @return List<String>
	 */
	public List<QiReportingMetric> findAllByLevel(String level) {
		return getDao(QiReportingMetricDao.class).findAllByLevel(level);
	}
	
	/**
	 * Method will be used to delete {@link QiReportingMetric} entity.
	 * 
	 * @param qiReportingMetric
	 */
	public void deleteMetricsData(QiReportingMetric qiReportingMetric) {
		getDao(QiReportingMetricDao.class).remove(qiReportingMetric);
	}
	
	/**
	 * Method will be used to update {@link QiReportingMetric} entity.
	 * 
	 * @param qiReportingMetric
	 */
	public void updateMetricsData(QiReportingMetric qiReportingMetric) {
		getDao(QiReportingMetricDao.class).update(qiReportingMetric);
	}
	
	/**
	 * Method will be used to update {@link QiReportingTarget} entity.
	 * 
	 * @param metricName
	 */
	public void updateAllTargetByMetricName(String oldMetricName, String newMetricName, String userId) {
		getDao(QiReportingTargetDao.class).updateAllTargetByMetricName(oldMetricName, newMetricName, userId);
	}

	/**
	 * Find All Department for selected Site and Plant
	 * 
	 * @param site
	 * @param plant
	 */
	public List<String> findAllDepartmentBySiteAndPlant(String site, String plant) {
		return StringUtil.trimStringList(getDao(QiDepartmentDao.class).findAllActiveDepartmentsBySiteAndPlant(site, plant));
	}

	/**
	 * This method will be used to find all active themes.
	 */
	public List<String> findAllActiveThemeNames() {
		List<String> themeNameList = new ArrayList<String>();
		
		List<QiTheme> themeList = getDao(QiThemeDao.class).findAllActiveThemes();
		if (themeList != null && !themeList.isEmpty()) {
			for (QiTheme qiTheme : themeList) {
				themeNameList.add(qiTheme.getThemeName());
			}
		}
		return themeNameList;
	}

	/**
	 * This method will be used to find all active Temporary Tracking names.
	 */
	public List<String> findAllActiveTemporaryTrackingNames() {
		List<String> temporaryTrackingNameList = new ArrayList<String>();
		
		List<QiLocalTheme> localThemeList  = getDao(QiLocalThemeDao.class).findAllActiveLocalTheme();
		if (localThemeList != null && !localThemeList.isEmpty()) {
			for (QiLocalTheme qiLocalTheme : localThemeList) {
				temporaryTrackingNameList.add(qiLocalTheme.getLocalTheme());
			}
		}
		return temporaryTrackingNameList;
	}

	/**
	 * Method will return all the targets filtered by metric name
	 * 
	 * @param metricName
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetsByMetricName(String metricName) {
		return getDao(QiReportingTargetDao.class).findAllTargetByMetricName(metricName);
	}

	/**
	 * Method will return all the targets filtered by metric name and level
	 * 
	 * @param metricName
	 * @param level
	 * @return
	 */
	public List<QiReportingTarget> findAllTargetsByMetricNameAndLevel(String metricName, String level) {
		return getDao(QiReportingTargetDao.class).findAllTargetByMetricNameAndLevel(metricName, level);
	}
	
	/**
	 * Method will be used to final all the responsible level based on given filters.
	 * 
	 * @param site
	 * @param plant
	 * @param dept
	 * @param level
	 * @return responsibleLevelList
	 */
	public List<String> findAllResponsibleLevelBySitePlantDeptAndLevel(String site, String plant, String dept, short level) {
		List<String> responsibleLevelList = new ArrayList<String>();

		List<QiResponsibleLevel> resultList = getDao(QiResponsibleLevelDao.class).findAllBySitePlantDepartmentLevel(site, plant, dept, level);
		if (resultList != null && !resultList.isEmpty()) {
			for (QiResponsibleLevel qiResponsibleLevel : resultList) {
				responsibleLevelList.add(qiResponsibleLevel.getResponsibleLevelName());
			}
		}
		return responsibleLevelList;
	}
	
	/**
	 * This method is used to save {@link QiReportingMetric} object.
	 * 
	 * @param qiReportingMetric
	 * @return QiReportingMetric
	 */
	public QiReportingMetric saveReportingMetric(QiReportingMetric qiReportingMetric) {
		return getDao(QiReportingMetricDao.class).save(qiReportingMetric);
	}
	
	/**
	 * This method is used to save {@link QiReportingTarget} object.
	 * 
	 * @param qiReportingTarget
	 * @return QiReportingMetric
	 */
	public QiReportingTarget saveReportingTarget(QiReportingTarget qiReportingTarget) {
		return getDao(QiReportingTargetDao.class).save(qiReportingTarget);
	}

	/**
	 * This method is used to save list of {@link QiReportingTarget} objects by native query
	 * 
	 * @param qiReportingTargets
	 */
	public void saveAllReportingTargetsByNativeQuery(List<QiReportingTarget> qiReportingTargets) {
		
		//insert into qi_reporting_target_tbx (SITE, PLANT, DEPT, PRODUCT_TYPE, MODEL_GROUP, SYSTEM, 
		//MODEL_YEAR_DESCRIPTION, DEMAND_TYPE, METRIC_NAME, LEVEL, METRIC_VALUE, EFFECTIVE_DATE, 
		//TARGET, TARGET_ITEM, CREATE_USER, CALCULATED_METRIC_VALUE) values (...), (...) 
		
		int size = qiReportingTargets.size();

		for (int i = 0; i <= size / BATCH_SIZE; i++) {
			int j = i * BATCH_SIZE;
			QiReportingTarget target = null;
			StringBuilder targetListString = new StringBuilder();
			while (j < ((i + 1) * BATCH_SIZE) && j < size) {
				target = qiReportingTargets.get(j++);
				targetListString.append("('" + target.getSite() + "','" + target.getPlant() + "','" + target.getDepartment() + "','" + target.getProductType() 
						+ "','" + target.getModelGroup() + "','" + target.getSystem() + "','" + target.getModelYearDescription() + "','" + target.getDemandType() 
						+ "','" + target.getMetricName() + "','" + target.getLevel() + "'," + target.getMetricValue() + ",'" + target.getEffectiveDate()
						+ "','" + target.getTarget() + "','" + target.getTargetItem() + "','" + target.getCreateUser() + "'," + target.getCalculatedMetricValue() + "),");
			}
			int length = targetListString.length();
			if (length > 0) {
				targetListString.deleteCharAt(length - 1); //remove ","
				getDao(QiReportingTargetDao.class).saveTargetsByNativeQuery(targetListString.toString());
			}
		}
	}
	
	/**
	 * Method will be used to delete targets by native query
	 * 
	 */
	public void deleteAllReportingTargetsByNativeQuery(String site, String plant, String productType, 
			String modelGroup, String modelYear, String demandType, String target, String metricName, 
			Date startDate, Date endDate, String department) {
		getDao(QiReportingTargetDao.class).deleteTargetsByNativeQuery(site, plant, productType,
				modelGroup, modelYear, demandType, target, metricName, startDate, endDate, department);
	}
	
	public List<String> findAllModelCodeByProductType(String productType) {
		return getDao(ModelGroupDao.class).findAllQicsModelGroupsByProductType(productType);
	}
	
	/**
	 * This method fetches the all Model Grouping for a particular Model Group.
	 * @param ModelGroup
	 */
	public List<ModelGrouping> findAllModelGroupingsByModelGroup(String ModelGroup) {
		return getDao(ModelGroupingDao.class).findAllByModelGroup(ModelGroup);
	}
	
	/**
	 * This method sets the Available Mtc Model based on product type selection
	 * and filter.
	 * @param filter
	 * @param productType
	 */
	public List<QiMtcToEntryModelDto> findAllByFilterAndProductType(String filter, String productType) {
		List<QiMtcToEntryModelDto> availableMtcList = new ArrayList<QiMtcToEntryModelDto>();
		if (ProductTypeUtil.getProductSpecDao(productType)!=null)
			availableMtcList =  ProductTypeUtil.getProductSpecDao(productType).findAllMtcModelYearCodesByFilter(filter, productType);
		return availableMtcList;
	}

	public List<String> findAllDemandType() {
		return getDao(ProductionLotDao.class).findAllDemandType();
	}
	
	public List<String> findMissingRegionalMetricList() {
		return getDao(QiReportingMetricDao.class).findMissingRegionalMetricList();
	}
	
	public QiReportingMetric findByMetricId(QiReportingMetricId metricId) {
		return getDao(QiReportingMetricDao.class).findByKey(metricId);
	}
	
	public List<QiReportingMetric> findAllByMetricName(String metricName) {
		return getDao(QiReportingMetricDao.class).findAllByMetricName(metricName);
	}
	
	public List<Object[]> findAllActiveLevelsBySitePlantDeptName(String site, String plant, String dept) {
		return getDao(QiResponsibleLevelDao.class).findAllActiveLevelsBySitePlantDeptName(site, plant, dept);
	}
	
	
	//find all valid resp levels based on level 1. 
	//valid levels: Level 1 only, Level 1 + Level 2, Level 1 + Level 2 + Level 3
	public List<String> findAllValidRespLevelBySitePlantDeptAndLevel(String site, String plant, String dept, short level) {
		List<String> responsibleLevelList = new ArrayList<String>();
		List<Object[]> allLevelList = getDao(QiResponsibleLevelDao.class).findAllActiveLevelsBySitePlantDeptName(site, plant, dept);
		hasLevel2Resp = false;
		hasLevel3Resp = false;
		
		for (Object[] levelObject : allLevelList) {
			if (levelObject[1] != null) {
				hasLevel2Resp = true;
			}
			if (levelObject[2] != null) {
				hasLevel3Resp = true;
			}
		}
		
		//valid levels: Level 1 only, Level 1 + Level 2, Level 1 + Level 2 + Level 3
		List<Object[]> tobeRemoved = new ArrayList<Object[]>();
		if (hasLevel3Resp) {
			//remove those without level3
			for (Object[] levelObject : allLevelList) {
				if (levelObject[2] == null) {
					tobeRemoved.add(levelObject);
				}
			}
			if (!tobeRemoved.isEmpty()) {
				allLevelList.removeAll(tobeRemoved);
			}
		} else if (level == 3) {
			return responsibleLevelList;
		}
		
		tobeRemoved.clear();
		if (hasLevel2Resp) {
			//remove those without level2
			for (Object[] levelObject : allLevelList) {
				if (levelObject[1] == null) {
					tobeRemoved.add(levelObject);
				}
			}
			if (!tobeRemoved.isEmpty()) {
				allLevelList.removeAll(tobeRemoved);
			}
		} else if (level == 2) {
			return responsibleLevelList;
		}
		
		if (level == 1) {
			for (Object[] levelObject : allLevelList) {
				responsibleLevelList.add(levelObject[2] + QiConstant.SEPARATOR + levelObject[1] + QiConstant.SEPARATOR + levelObject[0]);
			}
		} else if (level == 2) {
			for (Object[] levelObject : allLevelList) {
				if (!responsibleLevelList.contains(levelObject[2] + QiConstant.SEPARATOR + levelObject[1])) {
					responsibleLevelList.add(levelObject[2] + QiConstant.SEPARATOR + levelObject[1]);
				}
			}
		} else if (level == 3) {
			for (Object[] levelObject : allLevelList) {
				if (!responsibleLevelList.contains(levelObject[2])) {
					responsibleLevelList.add((String)levelObject[2]);
				}
			}
		}
		
		return responsibleLevelList;
	}

	public boolean hasLevel2Resp() {
		return hasLevel2Resp;
	}

	public boolean hasLevel3Resp() {
		return hasLevel3Resp;
	}

	public boolean hasRespLevel(String site, String plant, String dept, short level) {
		List<QiResponsibleLevel> respLevels = getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentAndLevel(site, plant, dept, level);
		return respLevels != null && !respLevels.isEmpty();
	}
	
	public ZoneId getServerZoneId() {
		if (serverZoneId == null) {
			serverZoneId = getDao(QiReportingTargetDao.class).getZoneId();
			
			LocalDateTime localDateTime = LocalDateTime.now();
		    // LocalDateTime -> ZonedDateTime
		    ZonedDateTime zonedDateTimeServer = localDateTime.atZone(serverZoneId);
		    ZonedDateTime zonedDateTimeClient = localDateTime.atZone(ZoneId.systemDefault());
		    if (zonedDateTimeServer.isBefore(zonedDateTimeClient)) {
		    	isServerBeforeClient = true;
		    }
		} 
		return serverZoneId;
	}
	
	public boolean isServerBeforeClient() {
		return isServerBeforeClient;
	}
}