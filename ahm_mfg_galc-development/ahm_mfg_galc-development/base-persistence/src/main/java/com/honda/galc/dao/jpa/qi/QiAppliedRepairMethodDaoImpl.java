package com.honda.galc.dao.jpa.qi;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qics.DefectRepairResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectRepairResultId;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;

public class QiAppliedRepairMethodDaoImpl extends BaseDaoImpl<QiAppliedRepairMethod, String>
		implements QiAppliedRepairMethodDao {

	@Autowired
	private DefectResultDao defectResultDao;
	
	@Autowired
	private DefectRepairResultDao defectRepairResultDao;

	private static final int SAME_TRANS_TIME_RANGE = 1000; //if repair_timestamp are within the range, they are considered to be entered in same transaction (in millisecond)

	private static final String FIND_CURRENT_REPAIR_METHODS = "SELECT distinct a.REPAIR_METHOD FROM GALADM.QI_STATION_REPAIR_METHOD_TBX a WHERE a.PROCESS_POINT_ID = ?1";

	private static final String FIND_REPAIR_METHOD_BY_FILTER_DATA = "select distinct e.REPAIR_METHOD from GALADM.QI_STATION_REPAIR_METHOD_TBX e where e.REPAIR_METHOD like ?1 AND e.PROCESS_POINT_ID = ?2";

	private final static String UPDATE_FIXED_STATUS = "Update galadm.QI_REPAIR_RESULT_TBX set CURRENT_DEFECT_STATUS = ?1 , UPDATE_USER = ?2 where REPAIR_ID = ?3 ";

	private static final String FIND_APPLIED_DATA = "select a.REPAIR_ID, a.REPAIR_METHOD_SEQ, a.REPAIR_TIMESTAMP,a.CREATE_USER,a.REPAIR_METHOD,a.REPAIR_TIME,a.IS_COMPLETELY_FIXED,a.COMMENT,a.APPLICATION_ID,a.UPDATE_USER from galadm.QI_APPLIED_REPAIR_METHOD_TBX a where a.REPAIR_ID=?1 order by a.REPAIR_TIMESTAMP desc";
	
	private static final String UPDATE_NOT_COMPLETELY_FIXED = "update GALADM.QI_APPLIED_REPAIR_METHOD_TBX set IS_COMPLETELY_FIXED=0, UPDATE_USER=upper(?1) where REPAIR_ID in @REPAIR_ID@";
	
	private static final String FIND_COMMON_REPAIR_METHOD = "select * from qi_applied_repair_method_tbx a where exists (select * from (select create_user, update_user, repair_method, application_id, repair_time, is_completely_fixed, comment from qi_applied_repair_method_tbx where repair_id in @REPAIR_ID@ group by create_user, update_user, repair_method, application_id, repair_time, is_completely_fixed, comment having count(*) = ?1) as b where a.repair_id in @REPAIR_ID@ and a.create_user=b.create_user and (a.update_user=b.update_user or (a.update_user is null and b.update_user is null)) and a.repair_method=b.repair_method and a.application_id=b.application_id and a.repair_time=b.repair_time and a.is_completely_fixed=b.is_completely_fixed and a.comment=b.comment) order by create_user, update_user, repair_method, application_id, repair_time, is_completely_fixed, comment, repair_timestamp";

	private static final String DELETE_APPLIED_REPAIR_METHOD_FOR_NPF="delete from GALADM.QI_APPLIED_REPAIR_METHOD_TBX where REPAIR_ID=?1 and REPAIR_METHOD=?2";
	
	public List<QiAppliedRepairMethodDto> findAllCurrentRepairMethods(String qicsStaion) {
		Parameters params = Parameters.with("1", qicsStaion);
		return findAllByNativeQuery(FIND_CURRENT_REPAIR_METHODS, params, QiAppliedRepairMethodDto.class);
	}

	public List<QiAppliedRepairMethodDto> findAllRepairMethodsByFilter(String repairMethodFilter, String qicsStaion) {
		Parameters params = Parameters.with("1", "%" + repairMethodFilter + "%").put("2", qicsStaion);
		return findAllByNativeQuery(FIND_REPAIR_METHOD_BY_FILTER_DATA, params, QiAppliedRepairMethodDto.class);

	}

	@Transactional
	public void updateFixedStatus(Integer currentDefectStatus, long repairId, String userId) {

		Parameters params = Parameters.with("1", currentDefectStatus).put("2", userId).put("3", repairId);
		executeNativeUpdate(UPDATE_FIXED_STATUS, params);

	}

	public Integer findCurrentSequence() {
		return max("id.repairMethodSeq", Integer.class);
	}

	
	public Integer findCurrentSequence(long repairId) {
		return max("id.repairMethodSeq", Integer.class, Parameters.with("id.repairId", repairId));
	}
	
	
	public List<QiAppliedRepairMethodDto> findAllAppliedRepairMethodDataByRepairId(long repairId) {

		Parameters params = Parameters.with("1", repairId);
		return findAllByNativeQuery(FIND_APPLIED_DATA, params, QiAppliedRepairMethodDto.class);
	}

	@Transactional
	public void deleteAppliedRepairMethodById(long repairId, Integer repairMethodSeq) {
		Parameters params = Parameters.with("id.repairId", repairId).put("id.repairMethodSeq", repairMethodSeq);
		delete(params);
	}
	
	@Transactional
	public void replicateRepairResult(QiAppliedRepairMethodDto qiAppliedRepairMethodDto, String partDefectDesc, String repairProcessPointId) {
		long repairId = qiAppliedRepairMethodDto.getRepairId();
		Date repairTimestamp = qiAppliedRepairMethodDto.getRepairTimestamp();
		String repairAssociateNo = qiAppliedRepairMethodDto.getCreateUser();
		
		//get defectresultid from gal125tbx
		DefectResult oldDefectResult = defectResultDao.findByQiRepairId(repairId);
		
		if (oldDefectResult != null) {
			//update repair_status in gal125tbx to 1 (repaired) if all actual problems got fixed and original NAQ defect status is 7 (fixed) 
			defectResultDao.updateByQiDefectResultId(oldDefectResult.getNaqDefectResultId(), DefectStatus.REPAIRED.getId(), repairTimestamp, repairAssociateNo);
	
			//insert gal222tbx
			DefectRepairResult defectRepairResult = new DefectRepairResult();
			DefectRepairResultId id = new DefectRepairResultId();
			id.setProductId(oldDefectResult.getProductId());
			id.setDefectResultId(oldDefectResult.getDefectResultId());
			defectRepairResult.setId(id);
			defectRepairResult.setRepairAssociateNo(repairAssociateNo);
			defectRepairResult.setActualTimestamp((Timestamp)repairTimestamp);
			if (partDefectDesc.length() > 64) {
				partDefectDesc = partDefectDesc.substring(0, 64);
			}
			defectRepairResult.setActualProblemName(partDefectDesc); 
			defectRepairResult.setRepairMethodName(qiAppliedRepairMethodDto.getRepairMethod());
			defectRepairResult.setRepairTime(qiAppliedRepairMethodDto.getRepairTime() == null? 0 : qiAppliedRepairMethodDto.getRepairTime());			
			defectRepairResult.setRepairDept(qiAppliedRepairMethodDto.getEntryDept()); 
			
			String oldRepairProcessPointId = getDao(QiExternalSystemDefectMapDao.class).findOldAppIdByAppId(repairProcessPointId);
			if (StringUtils.isEmpty(oldRepairProcessPointId)) {
				oldRepairProcessPointId = repairProcessPointId;

			} 

			defectRepairResult.setRepairProcessPointId(oldRepairProcessPointId); 
			defectRepairResult.setComment(qiAppliedRepairMethodDto.getComment());
			defectRepairResult.setNaqRepairId(repairId);
			
			//set REPAIR_ID in GAL222TBX
			Integer maxRepairId = defectRepairResultDao.findMaxRepairId();
			if (maxRepairId == null) {
				maxRepairId = 0;
			}
			
			int initReplicateRepairId = PropertyService.getPropertyBean(QiPropertyBean.class).getInitReplicateRepairId();
			if (maxRepairId < initReplicateRepairId - 1) {
				maxRepairId = initReplicateRepairId - 1;
			}
				
			defectRepairResult.getId().setRepairId(++maxRepairId);
			
			//save repair result in GAL222TBX
			defectRepairResultDao.save(defectRepairResult);
		}
	}
	
	@Transactional
	public List<QiAppliedRepairMethod> saveAllRepairMethods(List<QiAppliedRepairMethod> qiAppliedRepairMethodList, String applicationId) {
		//update fields based on DB server timestamp
		Timestamp timestamp = getDatabaseTimeStamp();
		GpcsDivision gpcsDivision = getGpcsDivision(applicationId);
		DailyDepartmentSchedule schedule = getSchedule(gpcsDivision, timestamp);
		String team = getTeam(gpcsDivision, schedule);
		
		for (QiAppliedRepairMethod qiAppliedRepairMethod : qiAppliedRepairMethodList) {
			qiAppliedRepairMethod.setRepairTimestamp(timestamp);
			if (schedule != null) {
				qiAppliedRepairMethod.setProductionDate(schedule.getId().getProductionDate());
				qiAppliedRepairMethod.setShift(schedule.getId().getShift());
				qiAppliedRepairMethod.setTeam(team);
			}
		}
			
		return super.saveAll(qiAppliedRepairMethodList);
	}
	
	@Override
	@Transactional
	public QiAppliedRepairMethod insertRepairMethod(QiAppliedRepairMethod qiAppliedRepairMethod, String applicationId, Timestamp repairTimestamp) {
		//update fields based on DB server timestamp
		Timestamp dbTimestamp = getDatabaseTimeStamp();		
		if(repairTimestamp != null)  {
			dbTimestamp = repairTimestamp;
		}
		GpcsDivision gpcsDivision = getGpcsDivision(applicationId);
		DailyDepartmentSchedule schedule = getSchedule(gpcsDivision, dbTimestamp);
		String team = getTeam(gpcsDivision, schedule);
		qiAppliedRepairMethod.setRepairTimestamp(dbTimestamp);
		
		if (schedule != null) {
			qiAppliedRepairMethod.setProductionDate(schedule.getId().getProductionDate());
			qiAppliedRepairMethod.setShift(schedule.getId().getShift());
			qiAppliedRepairMethod.setTeam(team);
		}
		
		return super.insert(qiAppliedRepairMethod);		
	}
	
	@Override
	@Transactional
	public QiAppliedRepairMethod insertRepairMethod(QiAppliedRepairMethod qiAppliedRepairMethod, String applicationId) {
		return insertRepairMethod(qiAppliedRepairMethod, applicationId, getDatabaseTimeStamp());	
	}
	
	private GpcsDivision getGpcsDivision(String applicationId) {
		GpcsDivision gpcsDiv = null;
		if(!StringUtils.isBlank(applicationId))  {
			ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(applicationId);
			if(processPoint != null)  {
				gpcsDiv = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
			}
		}
		return gpcsDiv;
	}
	
	private DailyDepartmentSchedule getSchedule(GpcsDivision gpcsDivision, Timestamp timestamp) {
		DailyDepartmentSchedule schedule = null;
		if (gpcsDivision != null) {
			schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
					gpcsDivision.getGpcsLineNo(), gpcsDivision.getGpcsProcessLocation(), 
					gpcsDivision.getGpcsPlantCode(), timestamp);
		}
		return schedule;
	}
	
	private String getTeam(GpcsDivision gpcsDivision, DailyDepartmentSchedule schedule) {
		String team = "";
		if (schedule != null) {
			TeamRotation teamRotation = getDao(TeamRotationDao.class).findTeamDetails(
					gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
			if(teamRotation != null) {
				team = StringUtils.trimToEmpty(teamRotation.getId().getTeam());
			}
		}
		return team;
	}

	@Transactional
	public void updateAppliedRepairMethodSql(Long id, Integer seq, String text, String userId) {
		Parameters params = Parameters.with("1", text).put("2", userId).put("3", id).put("4", seq);
		executeNativeUpdate("update QI_APPLIED_REPAIR_METHOD_TBX set comment=?1,update_user=upper(?2) where repair_id=?3 and repair_method_seq=?4", params);
		
	}
	
	@Override
	@Transactional
	public void updateNotCompletelyFixed(String repairIdsString, String userId) {
		Parameters params = Parameters.with("1", userId).put("2", repairIdsString);
		executeNativeUpdate(UPDATE_NOT_COMPLETELY_FIXED.replaceAll("@REPAIR_ID@", repairIdsString), params);
	}
	
	@Override
	public boolean areAllMethodsNotCompletelyFixed(long repairId) {
		boolean areAllMethodsNotCompletelyFixed = true;
		List<QiAppliedRepairMethodDto> appliedRepairMethods = findAllAppliedRepairMethodDataByRepairId(repairId);
		if (appliedRepairMethods == null || appliedRepairMethods.size() == 0) {
			areAllMethodsNotCompletelyFixed = false;
		} else {
			for (QiAppliedRepairMethodDto dto : appliedRepairMethods) {
				if (dto.getIsCompletelyFixed() == 1) {
					areAllMethodsNotCompletelyFixed = false;
					break;
				}
			}
		}
		
		return areAllMethodsNotCompletelyFixed;
	}
	
	@Override
	public List<QiAppliedRepairMethodDto> findCommonAppliedRepairMethod(List<Long> repairIds) {
		String repairIdsString = "";
		for (int i = 0; i < repairIds.size(); i++) {
			if (i == 0) {
				repairIdsString = "(" + repairIds.get(0);
			} else {
				repairIdsString += "," + repairIds.get(i);
			}
		}
		repairIdsString += ")";
		
		Parameters params = Parameters.with("1", repairIds.size());
		
		List<QiAppliedRepairMethodDto> repairMethodList = 
				findAllByNativeQuery(FIND_COMMON_REPAIR_METHOD.replaceAll("@REPAIR_ID@", repairIdsString), params, QiAppliedRepairMethodDto.class);
		// order by create_user, update_user, repair_method, application_id, repair_time, is_completely_fixed, comment, repair_timestamp
		
		List<QiAppliedRepairMethodDto> commonRepairMethodList = new ArrayList<QiAppliedRepairMethodDto>();
		
		for (int i = 0; i < repairMethodList.size(); i = i + repairIds.size()) {
			boolean isSameTransaction = true;
			QiAppliedRepairMethodDto repairMethod = repairMethodList.get(i);
			Date repairTimestamp = repairMethod.getRepairTimestamp();
			List<Long> repairIdList = new ArrayList<Long>();
			List<Integer> repairMethodSeqList = new ArrayList<Integer>();
			repairIdList.add(repairMethod.getRepairId());
			repairMethodSeqList.add(repairMethod.getRepairMethodSeq());
			
			for (int j = 0; j < repairIds.size(); j++) {
				QiAppliedRepairMethodDto nextRepairMethod = repairMethodList.get(i + j);
				Date nextRepairTimestamp = nextRepairMethod.getRepairTimestamp();
				long timeDiff = repairTimestamp.getTime() - nextRepairTimestamp.getTime();
				if (timeDiff > SAME_TRANS_TIME_RANGE || timeDiff < -SAME_TRANS_TIME_RANGE) {
					isSameTransaction = false;
					break;
				}
				repairIdList.add(nextRepairMethod.getRepairId());
				repairMethodSeqList.add(nextRepairMethod.getRepairMethodSeq());
			}

			if (isSameTransaction) {
				repairMethod.setRepairIdList(repairIdList);
				repairMethod.setRepairMethodSeqList(repairMethodSeqList);
				commonRepairMethodList.add(repairMethod);
			}
		}
		
		//sort by timestamp desc
		commonRepairMethodList.sort(Comparator.comparing(QiAppliedRepairMethodDto :: getRepairTimestamp).reversed());
		return commonRepairMethodList;
	}

	@Override
	@Transactional
	public void deleteAppliedRepairMethodByRepairIdNPF(long repairId,String repairMethod) {
		Parameters params = Parameters.with("1", repairId).put("2", repairMethod);
		executeNativeUpdate(DELETE_APPLIED_REPAIR_METHOD_FOR_NPF, params);
	}
}
