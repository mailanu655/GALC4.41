package com.honda.galc.dao.jpa.qi;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.dao.qi.QiDefectResultImageDao;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiRepairResultImageDao;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>QiRepairResultDaoImpl Class description</h3>
 * <p>
 * QiRepairResultDaoImpl is used to declare the methods required for repair entry
 * defect screen
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
 *         Nov 23, 2016
 * 
 */
public class QiRepairResultDaoImpl extends BaseDaoImpl<QiRepairResult, Long> implements QiRepairResultDao {

	@Autowired
    private	QiImageDao qiImageDao;
	
	@Autowired 
	private QiDefectResultImageDao defectResultImageDao;

	@Autowired 
	private QiRepairResultImageDao repairResultImageDao;
	
	private static final String FIND_ALL_REPAIR_ENTRY_DEFECT_BY_DEFECT_ID = "SELECT b.RESPONSIBLE_DEPT, b.ACTUAL_TIMESTAMP, b.REPAIR_ID, b.ACTUAL_PROBLEM_SEQ, b.APPLICATION_ID, b.DEFECTRESULTID, "
			+ "b.PRODUCT_ID, b.POINT_X, b.POINT_Y, b.IMAGE_NAME, b.DEFECT_CATEGORY_NAME, b.IS_REPAIR_RELATED, "
			+ "b.ENTRY_DEPT, d.DIVISION_NAME, b.ORIGINAL_DEFECT_STATUS, b.CURRENT_DEFECT_STATUS, b.INSPECTION_PART_NAME, b.INSPECTION_PART_LOCATION_NAME, "
			+ "b.INSPECTION_PART_LOCATION2_NAME, b.INSPECTION_PART2_NAME, b.INSPECTION_PART2_LOCATION_NAME, b.INSPECTION_PART2_LOCATION2_NAME, b.INSPECTION_PART3_NAME, "
			+ "b.DEFECT_TYPE_NAME, b.DEFECT_TYPE_NAME2,  b.DEFECT_TRANSACTION_GROUP_ID, b.WRITE_UP_DEPARTMENT, b.RESPONSIBLE_LEVEL1,b.RESPONSIBLE_LEVEL2,RESPONSIBLE_LEVEL3 , b.CREATE_USER, b.UPDATE_USER "
			+ "FROM GALADM.QI_REPAIR_RESULT_TBX b left join GALADM.GAL128TBX d on d.DIVISION_ID=b.ENTRY_DEPT "
			+ "WHERE b.DEFECTRESULTID = ?1";

	private static final String FIND_LATEST_REPAIR_TIMESTAMP = "SELECT MAX(REPAIR_TIMESTAMP) FROM GALADM.QI_APPLIED_REPAIR_METHOD_TBX WHERE  IS_COMPLETELY_FIXED = 1 and REPAIR_ID = ?1";

	private static final String DELETE_ALL_REPAIR_METHODS = "DELETE FROM GALADM.QI_APPLIED_REPAIR_METHOD_TBX WHERE  REPAIR_ID = ?1";

	private static final String UPDATE_ALL_REPAIR_METHOD_STATUS = "UPDATE GALADM.QI_APPLIED_REPAIR_METHOD_TBX SET IS_COMPLETELY_FIXED = ?1 , UPDATE_USER = ?2 WHERE  REPAIR_ID = ?3";
	
	private static final String UPDATE_DEFECT_RESULT_STATUS = "UPDATE GALADM.QI_DEFECT_RESULT_TBX SET CURRENT_DEFECT_STATUS = ?1 , UPDATE_USER = ?2 "
			+ "WHERE  DEFECTRESULTID = ?3";
	
	private static final String FIND_ALL_DEFECT_BY_PRODUCT_ID = "SELECT b.RESPONSIBLE_DEPT, b.ACTUAL_TIMESTAMP, b.APPLICATION_ID, b.DEFECTRESULTID, b.PRODUCT_ID, b.POINT_X, b.POINT_Y, b.IMAGE_NAME, "
			+ " b.ENTRY_DEPT, b.ORIGINAL_DEFECT_STATUS, b.CURRENT_DEFECT_STATUS, b.INSPECTION_PART_NAME, b.INSPECTION_PART_LOCATION_NAME, "
			+ " b.INSPECTION_PART_LOCATION2_NAME, b.INSPECTION_PART2_NAME, b.INSPECTION_PART2_LOCATION_NAME, b.INSPECTION_PART2_LOCATION2_NAME, b.INSPECTION_PART3_NAME, "
			+ " b.DEFECT_TYPE_NAME, b.DEFECT_TYPE_NAME2, b.DEFECT_CATEGORY_NAME, b.IS_REPAIR_RELATED, b.WRITE_UP_DEPARTMENT, D.DIVISION_NAME, b.DEFECT_TRANSACTION_GROUP_ID, "
			+ " b.CREATE_USER, b.UPDATE_USER, "
			+ " RESPONSIBLE_LEVEL1,RESPONSIBLE_LEVEL2,RESPONSIBLE_LEVEL3, "
			+ " k.KICKOUT_ID, kl.DIVISION_ID, kl.LINE_ID, kl.PROCESS_POINT_ID , p.PROCESS_POINT_NAME"
			+ " FROM GALADM.QI_DEFECT_RESULT_TBX b "
			+ " left join GALADM.GAL128TBX D on D.DIVISION_ID=B.ENTRY_DEPT "
			+ " left join GALADM.KICKOUT_TBX k on k.KICKOUT_ID = b.KICKOUT_ID "
			+ " left join GALADM.KICKOUT_LOCATION_TBX kl on b.KICKOUT_ID = kl.KICKOUT_ID "
			+ " left join GALADM.GAL214TBX p on kl.PROCESS_POINT_ID = p.PROCESS_POINT_ID "
			+ " WHERE b.PRODUCT_ID = ?1 AND b.DELETED = 0";
	
	private static final String FIND_ALL_DEFECT_BY_PRODUCT_IDS = "SELECT b.RESPONSIBLE_DEPT, b.ACTUAL_TIMESTAMP, b.APPLICATION_ID, b.DEFECTRESULTID, b.PRODUCT_ID, b.POINT_X, b.POINT_Y, b.IMAGE_NAME, "
			+ " b.ENTRY_DEPT, b.ORIGINAL_DEFECT_STATUS, b.CURRENT_DEFECT_STATUS, b.INSPECTION_PART_NAME, b.INSPECTION_PART_LOCATION_NAME, "
			+ " b.INSPECTION_PART_LOCATION2_NAME, b.INSPECTION_PART2_NAME, b.INSPECTION_PART2_LOCATION_NAME, b.INSPECTION_PART2_LOCATION2_NAME, b.INSPECTION_PART3_NAME, "
			+ " b.DEFECT_TYPE_NAME, b.DEFECT_TYPE_NAME2, b.DEFECT_CATEGORY_NAME, b.IS_REPAIR_RELATED, b.WRITE_UP_DEPARTMENT, D.DIVISION_NAME, b.DEFECT_TRANSACTION_GROUP_ID, "
			+ " b.CREATE_USER, b.UPDATE_USER,"
			+ " RESPONSIBLE_LEVEL1,RESPONSIBLE_LEVEL2,RESPONSIBLE_LEVEL3, "
			+ " k.KICKOUT_ID, k.KICKOUT_STATUS, kl.DIVISION_ID, kl.LINE_ID, kl.PROCESS_POINT_ID, p.PROCESS_POINT_NAME"
			+ " FROM GALADM.QI_DEFECT_RESULT_TBX b "
			+ " left join GALADM.GAL128TBX D on D.DIVISION_ID=B.ENTRY_DEPT "
			+ " left join GALADM.KICKOUT_TBX k on k.KICKOUT_ID = b.KICKOUT_ID "
			+ " left join GALADM.KICKOUT_LOCATION_TBX kl on b.KICKOUT_ID = kl.KICKOUT_ID "
			+ " left join GALADM.GAL214TBX p on kl.PROCESS_POINT_ID = p.PROCESS_POINT_ID "
			+ " WHERE b.PRODUCT_ID IN (@productIds@) AND b.DELETED = 0";
	
	private static final String FIND_ALL_DEFECT_BY_PRODUCT_ID_ENTRY_DEPTS = FIND_ALL_DEFECT_BY_PRODUCT_ID + " AND b.ENTRY_DEPT in ";
	
	private static final String UPDATE_REPAIR_RESULT_INCIDENT_ID="UPDATE GALADM.QI_REPAIR_RESULT_TBX SET INCIDENT_ID = ?1 , UPDATE_USER = ?2 ,UPDATE_TIMESTAMP=?3  WHERE  DEFECTRESULTID IN ";
	private static final String FIND_SEARCH_RESULT = "SELECT * FROM galadm.QI_REPAIR_RESULT_TBX  c LEFT JOIN galadm.QI_INCIDENT_TBX g ON c.INCIDENT_ID = g.INCIDENT_ID where ";
	private static final String FIND_ALL_REPAIRED_DEFECT_BY_SHIFT ="SELECT PRODUCT_ID ,CREATE_USER ,CREATE_TIMESTAMP CREATE_TIME FROM GALADM.QI_REPAIR_RESULT_TBX "+
			"WHERE SHIFT= ?1 and PRODUCTION_DATE= ?2 and APPLICATION_ID = ?3 and deleted = 0 and PRODUCT_TYPE= ?4 ";
	
	private static final String DELETE_REPAIR_RESULT = "UPDATE GALADM.QI_REPAIR_RESULT_TBX SET DELETED = 1 WHERE DEFECTRESULTID = ?1";

	private static final String FIND_MAX_SEQ = "SELECT COALESCE(MAX(E.ACTUAL_PROBLEM_SEQ),0) FROM QI_REPAIR_RESULT_TBX E WHERE E.DEFECTRESULTID = ?1";
	
	private static final String FIND_CURRENT_DEFECT_COUNT="select count(*) from galadm.QI_REPAIR_RESULT_TBX where PRODUCT_ID=?1 and CURRENT_DEFECT_STATUS=?2";
	
	private static final String FIND_ALL_BY_PRODUCT_ID = "select qiRepairResult from QiRepairResult qiRepairResult where qiRepairResult.productId = :productId" ;
	
	private static final String DELETE_REPAIR_RESULT_FOR_NPF="delete from GALADM.QI_REPAIR_RESULT_TBX where DEFECTRESULTID=?1 and DEFECT_TYPE_NAME=?2";
	
	public List<QiRepairResultDto> findAllRepairEntryDefectsByDefectResultId(long defectResultId) {
		Parameters params = Parameters.with("1", defectResultId);
		List<QiRepairResultDto> qiRepairResultDtolst =findAllByNativeQuery(FIND_ALL_REPAIR_ENTRY_DEFECT_BY_DEFECT_ID, params, QiRepairResultDto.class);
		//get image_data by additional loop to avoid outer join
		populateImages(qiRepairResultDtolst);
		return qiRepairResultDtolst;
	}

	public Date findLatestRepairTimestampByRepairId(long repairId) {
		Parameters params = Parameters.with("1", repairId);
		return findFirstByNativeQuery(FIND_LATEST_REPAIR_TIMESTAMP, params, Date.class);
	}

	@Transactional
	public void deleteAllRepairMethodsByRepairId(long repairId) {
		Parameters params = Parameters.with("1", repairId);
		executeNativeUpdate(DELETE_ALL_REPAIR_METHODS, params);

	}

	@Transactional
	public void updateAllRepairMethodStatusById(long repairId, boolean isCompletelyFixed, String userId) {
		Parameters params = Parameters.with("1", isCompletelyFixed ? 1 : 0).put("2", userId).put("3", repairId);
		executeNativeUpdate(UPDATE_ALL_REPAIR_METHOD_STATUS, params);
	}
	
	@Transactional
	public void updateDefectResultStatusById(long defectResultId, int currDefectStatus, String userId) {
		Parameters params = Parameters.with("1", currDefectStatus).put("2", userId).put("3", defectResultId);
		executeNativeUpdate(UPDATE_DEFECT_RESULT_STATUS, params);
	}
	
	/**
	 * This method is used to find all Defects by Product Id
	 */
	public List<QiRepairResultDto> findAllDefectsByProductId(String productId) {
		Parameters params = Parameters.with("1", StringUtils.trim(productId));
		List<QiRepairResultDto> qiRepairResultDtolst = findAllByNativeQuery(FIND_ALL_DEFECT_BY_PRODUCT_ID, params, QiRepairResultDto.class);
		//get image_data by additional loop to avoid outer join
		populateImages(qiRepairResultDtolst);
		return qiRepairResultDtolst;
	}

	public List<QiRepairResult> findAllByFilter(String searchString,String filterData, int defectDataRange ,int searchResultLimit){
		StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(FIND_SEARCH_RESULT).append(searchString );
		if(filterData!=null && !filterData.equalsIgnoreCase("")){
			searchQuery.append(" AND (REPLACE(REPLACE(REPLACE(c.INSPECTION_PART_NAME || ' ' || COALESCE (c.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE (c.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "+
					" (c.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE (c.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE (c.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "+
					" (c.INSPECTION_PART3_NAME, '') || ' ' || COALESCE  (c.DEFECT_TYPE_NAME, '') || ' ' || COALESCE  (c.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) like  '%" +filterData+ "%'");
		}
		searchQuery.append(" AND ACTUAL_TIMESTAMP > current timestamp - "+(defectDataRange-1)+" DAY");
		return findAllByNativeQuery(searchQuery.toString(), null,searchResultLimit);
	}
	
	@Transactional
	public void updateIncidentIdByDefectResultId(Integer qiIncidentId, String user,String defectResultIdSet,Timestamp updatedTimestamp) {
		Parameters params = Parameters.with("1", qiIncidentId).put("2", user).put("3", updatedTimestamp);
		executeNativeUpdate(UPDATE_REPAIR_RESULT_INCIDENT_ID+defectResultIdSet, params);		
	}
	
	public long findMaxRepairId(){
		return max("repairId", Long.class);
	}
	
	public List<QiRepairResult> findAllByDefectResultId(long defectResultId) {
		return findAll(Parameters.with("defectResultId", defectResultId));
	}
	
	@Override
	public int findMaxSequenceByDefectResultId(long defectResultId) {
		return findFirstByNativeQuery(FIND_MAX_SEQ, Parameters.with("1", defectResultId), Integer.class);
	}
	
	@Override
	public List<QiRepairResult> findNotFixedByDefectResultId(long defectResultId) {
		return findAllByDefectIdAndCurrentStatus(defectResultId, DefectStatus.NOT_FIXED);
	}
	
	@Override
	public List<QiRepairResult> findFixedByDefectResultId(long defectResultId) {
		return findAllByDefectIdAndCurrentStatus(defectResultId, DefectStatus.FIXED);
	}
	
	@Override
	public List<QiRepairResult> findAllByDefectIdAndCurrentStatus(long defectResultId, DefectStatus currentStatus) {
		return findAll(Parameters.with("defectResultId", defectResultId).put("currentDefectStatus", (short) currentStatus.getId()));
	}
	
	public List<QiDefectResultDto> findAllByShiftAndDate(String shift,
			java.sql.Date productionDate, String applicationId, String productType) {
		Parameters params = Parameters.with("1", shift).put("2",productionDate).put("3", applicationId).put("4", productType);
		return  findAllByNativeQuery(FIND_ALL_REPAIRED_DEFECT_BY_SHIFT,params,QiDefectResultDto.class);
	}
	
	public QiRepairResult findLatestFixedRepairResult(long defectResultId) {
		return findFirst(Parameters.with("defectResultId", defectResultId).put("currentDefectStatus", (short)7), new String[]{"actualTimestamp"}, false);
	}
	
	@Transactional
	public QiRepairResult createRepairResult(QiRepairResult qiRepairResult, QiDefectResult previousQiDefectResult) {
		if (previousQiDefectResult == null) {
			//update fields based on DB server timestamp
			Timestamp timestamp = getDatabaseTimeStamp();
			qiRepairResult.setActualTimestamp(timestamp);
			qiRepairResult.setGroupTimestamp(timestamp);
			
			String applicationId = qiRepairResult.getApplicationId();
			ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(applicationId);
			if (processPoint != null) {
				GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
					
				if (gpcsDivision != null) {
					DailyDepartmentSchedule schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
							gpcsDivision.getGpcsLineNo(), gpcsDivision.getGpcsProcessLocation(), 
							gpcsDivision.getGpcsPlantCode(), timestamp);
					
					if (schedule != null) {
						qiRepairResult.setProductionDate(schedule.getId().getProductionDate());
						qiRepairResult.setShift(schedule.getId().getShift());
						TeamRotation teamRotation= getDao(TeamRotationDao.class).findTeamDetails(
								gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
						if(teamRotation != null) {
							qiRepairResult.setTeam(StringUtils.trimToEmpty(teamRotation.getId().getTeam()));
						}
					}
				}
			}
		} else  {
			qiRepairResult.setActualTimestamp(previousQiDefectResult.getActualTimestamp());
			qiRepairResult.setGroupTimestamp(previousQiDefectResult.getGroupTimestamp());
			qiRepairResult.setProductionDate(previousQiDefectResult.getProductionDate());
			qiRepairResult.setShift(previousQiDefectResult.getShift());
			qiRepairResult.setTeam(previousQiDefectResult.getTeam());
		}
	
		return super.insert(qiRepairResult);
	}
	
	@Transactional
	public List<QiRepairResult> createRepairResults(List<QiRepairResult> qiRepairResultList) {
		for (QiRepairResult qiRepairResult : qiRepairResultList) {
			if (qiRepairResult.getRepairId() == 0) { //new repair result, update timestamp, production date, shift and team
				//update fields based on DB server timestamp
				Timestamp timestamp = getDatabaseTimeStamp();
				String applicationId = qiRepairResult.getApplicationId();
				ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(applicationId);
				GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
				DailyDepartmentSchedule schedule = null;
				TeamRotation teamRotation = null;	
				
				if (gpcsDivision != null) {
					schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
							gpcsDivision.getGpcsLineNo(), gpcsDivision.getGpcsProcessLocation(), 
							gpcsDivision.getGpcsPlantCode(), timestamp);
					if (schedule != null) {
						teamRotation = getDao(TeamRotationDao.class).findTeamDetails(
							gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
					}
				}
				
				qiRepairResult.setActualTimestamp(timestamp);
				qiRepairResult.setGroupTimestamp(timestamp);
				if (schedule != null) {
					qiRepairResult.setProductionDate(schedule.getId().getProductionDate());
					qiRepairResult.setShift(schedule.getId().getShift());
					if(teamRotation != null) {
						qiRepairResult.setTeam(StringUtils.trimToEmpty(teamRotation.getId().getTeam()));
					}
				}
			}
		}
	
		return super.saveAll(qiRepairResultList);
	}
	
	public List<QiRepairResultDto> findAllDefectsByProductIdEntryDepts(String productId, String entryDepts) {
		Parameters params = Parameters.with("1", StringUtils.trim(productId));
		List<QiRepairResultDto> qiRepairResultDtolst = findAllByNativeQuery(FIND_ALL_DEFECT_BY_PRODUCT_ID_ENTRY_DEPTS + entryDepts, params, QiRepairResultDto.class);
		//get image_data by additional loop to avoid outer join
		populateImages(qiRepairResultDtolst);
		return qiRepairResultDtolst;
	}
	
	public List<QiRepairResultDto> findAllDefectsByProductIds(List<String> productIds) {
		List<QiRepairResultDto> dtoList = findAllByNativeQuery(FIND_ALL_DEFECT_BY_PRODUCT_IDS.replace("@productIds@",StringUtil.toSqlInString(productIds)), null, QiRepairResultDto.class);
		populateImages(dtoList);
		return dtoList;
	}
	
	public List<QiRepairResultDto> findAllDefectsByProductIdsNoImage(List<String> productIds) {
		List<QiRepairResultDto> dtoList = findAllByNativeQuery(FIND_ALL_DEFECT_BY_PRODUCT_IDS.replace("@productIds@",StringUtil.toSqlInString(productIds)), null, QiRepairResultDto.class);
		return dtoList;
	}
	
	@Transactional
	public void deleteRepairResultByDefectResultId(long defectResultId) {
		Parameters params = Parameters.with("1", defectResultId);
		executeNativeUpdate(DELETE_REPAIR_RESULT, params);
	}
	
	@Override
	public int findCurrentDefectStatusCount(String productId) {
		Parameters params = new Parameters();
		params.put("1", productId);
		params.put("2", 7);
		return findFirstByNativeQuery(FIND_CURRENT_DEFECT_COUNT, params, Integer.class);
	}

	private void populateImages(List<QiRepairResultDto> dtoList) {
		for(QiRepairResultDto dto : dtoList){
			QiImage qiImage = qiImageDao.findImageByImageName(dto.getImageName());
			if (qiImage != null) dto.setImageData(qiImage.getImageData());
			if(dto.getRepairId() > 0) {
				dto.setRepairResultImages(repairResultImageDao.findAllByRepairId(dto.getRepairId()));
			} else {
				dto.setDefectResultImages(defectResultImageDao.findAllByDefectResultId(dto.getDefectResultId()));
			}
		}
	}
	
	@Override
	public List<QiRepairResult> findAllByProductIdAndCurrentDefectStatus(String productId, short defectStatus) {
		return findAll(Parameters.with("productId", productId).put("currentDefectStatus", defectStatus));
	}	

	@Override
	public List<QiRepairResult> findAllByProductId(String productId, List<Short> statusList) {
		StringBuilder selectedValue = new StringBuilder(FIND_ALL_BY_PRODUCT_ID);
		Parameters params = Parameters.with("productId", productId);
		if(!statusList.isEmpty()) {
			params.put("status", statusList);
			selectedValue.append(" and qiRepairResult.currentDefectStatus in (:status)");
		}
		return findAllByQuery(selectedValue.toString(), params);
	}

	@Override
	@Transactional
	public void deleteRepairResultByDefectResultIdNPF(long defectResultId,String defectTypeName) {
		Parameters params = Parameters.with("1", defectResultId).put("2",defectTypeName);
		executeNativeUpdate(DELETE_REPAIR_RESULT_FOR_NPF, params);
	}

}
