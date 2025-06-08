package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationResponsibilityDao;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.qi.QiStationResponsibility;
import com.honda.galc.entity.qi.QiStationResponsibilityId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QiStationResponsibilityDaoImpl Class description</h3>
 * <p>
 * QiStationResponsibilityDaoImpl: DaoImpl class for Limit Responsibility
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
 *        June 13, 2017
 * 
 */
public class QiStationResponsibilityDaoImpl extends BaseDaoImpl<QiStationResponsibility,QiStationResponsibilityId> implements QiStationResponsibilityDao {

	private static final String FIND_ALL_BY_SITE = "SELECT * FROM QI_STATION_RESPONSIBILITY_TBX a join QI_RESPONSIBLE_LEVEL_TBX b on b.RESPONSIBLE_LEVEL_ID=a.RESPONSIBLE_LEVEL_ID" +
			" WHERE b.SITE = ?1";
	private static final String FIND_ALL_BY_PLANT = "SELECT * FROM QI_STATION_RESPONSIBILITY_TBX a join QI_RESPONSIBLE_LEVEL_TBX b on b.RESPONSIBLE_LEVEL_ID=a.RESPONSIBLE_LEVEL_ID" +
			" WHERE b.PLANT = ?1";
	private static final String FIND_ALL_BY_DEPT = "SELECT * FROM QI_STATION_RESPONSIBILITY_TBX a join QI_RESPONSIBLE_LEVEL_TBX b on b.RESPONSIBLE_LEVEL_ID=a.RESPONSIBLE_LEVEL_ID" +
			" WHERE b.DEPT = ?1";
	
	private static final String FIND_ALL_ASSIGNED_RESPONSIBILITIES_BY_PROCESS_POINT = "SELECT b.RESPONSIBLE_LEVEL_ID,b.SITE,b.PLANT,b.DEPT,b.RESPONSIBLE_LEVEL_NAME " +
			"FROM GALADM.QI_STATION_RESPONSIBILITY_TBX AS a JOIN GALADM.QI_RESPONSIBLE_LEVEL_TBX AS b ON a.RESPONSIBLE_LEVEL_ID = b.RESPONSIBLE_LEVEL_ID " +
			"WHERE a.PROCESS_POINT_ID=?1 AND b.ACTIVE=1 AND b.LEVEL=1";
	
	private static final String FIND_ALL_BY_RESPONSIBLE_LEVEL = "SELECT * FROM QI_STATION_RESPONSIBILITY_TBX a join QI_RESPONSIBLE_LEVEL_TBX b on b.RESPONSIBLE_LEVEL_ID=a.RESPONSIBLE_LEVEL_ID" +
			" WHERE b.RESPONSIBLE_LEVEL_NAME = ?1";
	
	/**
	 * This method is used to delete all station responsibilities by process point.
	 */
	@Transactional
	public void deleteAllByProcessPoint(String processPoint) {
		Parameters params = Parameters.with("id.processPointId", processPoint);
		delete(params);
	}
	
	/**
	 * This method is used to find all station responsibilities by process point.
	 */
	public List<QiStationResponsibility> findAllByProcessPoint(String processPoint) {
		Parameters params = Parameters.with("id.processPointId", processPoint);
		return findAll(params);
	}
	
	/**
	 * This method is used to find all station responsibilities by responsible level
	 */
	public List<QiStationResponsibility> findAllByResponsibleLevel(String responsibleLevel) {
		return findAllByNativeQuery(FIND_ALL_BY_RESPONSIBLE_LEVEL,Parameters.with("1", responsibleLevel));
	}

	/**
	 * This method is used to find all station responsibilities by site
	 */
	public List<QiStationResponsibility> findAllBySite(String siteName) {
		return findAllByNativeQuery(FIND_ALL_BY_SITE,Parameters.with("1", siteName));
	}

	/**
	 * This method is used to find all station responsibilities by plant
	 */
	public List<QiStationResponsibility> findAllByPlant(String plantName) {
		return findAllByNativeQuery(FIND_ALL_BY_PLANT,Parameters.with("1", plantName));
	}

	/**
	 * This method is used to find all station responsibilities by department
	 */
	public List<QiStationResponsibility> findAllByDepartment(String department) {
		return findAllByNativeQuery(FIND_ALL_BY_DEPT,Parameters.with("1", department));
	}

	/**
	 * This method is used to find all assigned station responsibilities by process point
	 */
	public List<QiStationResponsibilityDto> findAllAssignedRespByProcessPoint(String processPoint) {
		Parameters params = Parameters.with("1", processPoint);
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_RESPONSIBILITIES_BY_PROCESS_POINT, params, QiStationResponsibilityDto.class);
	}

	/**
	 * This method is count all assigned station responsibilities by process point
	 */
	public long countAssignedRespByProcessPoint(String processPoint) {
		Parameters params = Parameters.with("id.processPointId", processPoint);
		return count(params);
	}

	/**
	 * This method is count all assigned station responsibilities by responsibleLevelId
	 */
	public long countByResponsibleLevelId(int responsibleLevelId) {
		Parameters params = Parameters.with("id.responsibleLevelId", responsibleLevelId);
		return count(params);
	}

	/**
	 * This method is count all assigned station responsibilities by responsibleLevelId
	 */
	private static final String COUNT_ALL_BY_RESPONSIBILITY_LEVEL2 =
			"SELECT count(e) FROM QiStationResponsibility e WHERE e.id.responsibleLevelId in "
			+ "(SELECT l1.responsibleLevelId FROM QiResponsibleLevel l1 "
			+ "WHERE l1.upperResponsibleLevelId= :responsibleLevelId )";
	
	public long countByResponsibleLevel2(int responsibleLevelId) {
		Parameters params = Parameters.with("responsibleLevelId", responsibleLevelId);
		return count(COUNT_ALL_BY_RESPONSIBILITY_LEVEL2, params);
	}

	private static final String COUNT_ALL_BY_RESPONSIBILITY_LEVEL3 =
			"SELECT count(e) FROM QiStationResponsibility e WHERE e.id.responsibleLevelId in "
			+ "(SELECT l1.responsibleLevelId FROM QiResponsibleLevel l1, QiResponsibleLevel l2 "
			+ "WHERE l1.upperResponsibleLevelId = l2.responsibleLevelId AND l2.upperResponsibleLevelId = :responsibleLevelId) ";
		
	public long countByResponsibleLevel3(int responsibleLevelId) {
		Parameters params = Parameters.with("responsibleLevelId", responsibleLevelId);
		return count(COUNT_ALL_BY_RESPONSIBILITY_LEVEL3, params);
	}

	/**
	 * This method is used to delete EntryDept by process point
	 */
	@Transactional
	public int deleteByProcessPoint(String processPointId) {
		return delete(Parameters.with("id.processPointId", processPointId));
	}
	
}
