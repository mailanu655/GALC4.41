package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dto.qi.QiPlantDepartmentDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.service.Parameters;

/**
 * <h3>QiDepartmentDaoImpl description</h3> <h4>Description</h4>
 * <p>
 * <code>QiDepartmentDaoImpl</code> is an implementation for QiDepartmentDao interface
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */

public class QiDepartmentDaoImpl extends BaseDaoImpl<QiDepartment, QiDepartmentId> implements QiDepartmentDao {

	private static final String FIND_ALL_BY_SITE_AND_PLANT = "SELECT e.id.department FROM QiDepartment e WHERE e.id.site = :site AND e.id.plant = :plant  order by e.id.department" ;
	
	private final static String UPDATE_DEPARTMENT = "Update QiDepartment d set d.departmentName=:departmentName, d.pddaDept=:pddaDept, d.active=:active, "
			+ " d.updateUser=:updateUser, d.id.department =:newDepartment, d.departmentDescription=:departmentDescription where"
			+ " d.id.site= :site and d.id.plant= :plant and d.id.department= :oldDepartment ";
	
	private final static String UPDATE_PLANT_FOR_DEPT = "update galadm.QI_DEPT_TBX set UPDATE_USER=?1, PLANT=?2 where SITE=?3 AND DEPT=?4 AND PLANT=?5 ";
	
	private final static String UPDATE_SITE_FOR_DEPT = "update galadm.QI_DEPT_TBX set UPDATE_USER=?1, SITE=?2  where SITE=?3 AND DEPT=?4 AND PLANT=?5";

	private static final String FIND_ALL_ACTIVE_BY_SITE_AND_PLANT = "SELECT e.id.department FROM QiDepartment e WHERE e.id.site = :site AND e.id.plant = :plant AND e.active = 1 order by e.id.department" ;

	private static final String FIND_ALL_ACTIVE_BY_SITE_PLANT_LIST = "SELECT * FROM galadm.QI_DEPT_TBX WHERE ACTIVE=1 ";
	
	private final static String FIND_ALL_ASSIGNED_DEPT_NAME = "SELECT DISTINCT * FROM ((select distinct TRIM(a.DEPT) from GALADM.QI_DEPT_TBX a "
			+ "join GALADM.QI_RESPONSIBLE_LEVEL_TBX b on a.DEPT = b.DEPT "
			+ "join GALADM.QI_STATION_RESPONSIBILITY_TBX c on b.RESPONSIBLE_LEVEL_ID = c.RESPONSIBLE_LEVEL_ID) "
			+ "UNION ALL (SELECT distinct TRIM(a.DEPT) FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a "
    		+ "JOIN galadm.QI_LOCAL_DEFECT_COMBINATION_TBX b on a.RESPONSIBLE_LEVEL_ID = b.RESPONSIBLE_LEVEL_ID))";
	
	private final static String FIND_QI_PLANT_DEPARTMENT_BY_PDDA_DATA = "SELECT A.SITE, A.PLANT, A.ENTRY_SITE, A.ENTRY_PLANT, B.DEPT, B.PDDA_DEPT " 
			+ "FROM QI_PLANT_TBX A, QI_DEPT_TBX B, QI_RESPONSIBLE_LEVEL_TBX C WHERE A.SITE=B.SITE AND A.PLANT=B.PLANT "
			+ "AND A.SITE=C.SITE AND A.PLANT=C.PLANT AND B.DEPT=C.DEPT AND A.HAM_PLANT_CODE=?1 AND A.PDDA_LINE=?2 "
			+ "AND B.PDDA_DEPT=?3 AND C.RESPONSIBLE_LEVEL_NAME=?4 AND C.LEVEL=1";
	
	@Transactional
	public void updateDepartment(QiDepartmentId oldDepartmentId, QiDepartment newDept) {
		Parameters params = Parameters.with("departmentName", newDept.getDepartmentName())
				.put("pddaDept", newDept.getPddaDept())
				.put("active", newDept.getActive())
				.put("updateUser", newDept.getUpdateUser())
				.put("newDepartment", newDept.getId().getDepartment())
				.put("departmentDescription", newDept.getDepartmentDescription())
				.put("site", oldDepartmentId.getSite())
				.put("plant", oldDepartmentId.getPlant())
				.put("oldDepartment", oldDepartmentId.getDepartment());
				executeUpdate(UPDATE_DEPARTMENT, params);
	}
	
	private static final String FIND_BY_SITE_PLANT_DEPT =
			"SELECT e FROM QiDepartment e WHERE trim(e.id.site) = trim(:site) AND trim(e.id.plant) = trim(:plant) "
			+ " and trim(e.id.department)=trim(:dept) order by e.id.department" ;
	
	/**  This method is used to find all Department by Site and Plant
	 * 
	 * @param site
	 * @param plant
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllDeptBySiteAndPlant(String site, String plant){
		return findResultListByQuery(FIND_ALL_BY_SITE_AND_PLANT , Parameters.with("site", site).put("plant", plant));
	}

	/**
	 * This method is used to make department inactive 
	 * 
	 * @param QiDepartmentId
	 */
	@Transactional
	public void inactivateDepartment(QiDepartmentId id) {
		update(Parameters.with("active", 0), Parameters.with("id.site", id.getSite()).put("id.plant",id.getPlant()).put("id.department",id.getDepartment()));
	}

	/**
	 * This method is used to update plantName for department 
	 * 
	 * @param updateUser
	 * @param newPlantName
	 * @param siteName
	 * @param oldPlantName
	 */
	@Transactional
	public void updatePlantById(String updateUser, String newPlantName, QiDepartmentId id) {
		Parameters params =Parameters.with("1", updateUser).put("2",newPlantName ).put("3", id.getSite()).put("4", id.getDepartment()).put("5", id.getPlant());
		executeNativeUpdate(UPDATE_PLANT_FOR_DEPT, params);
	}

	/**
	 * This method is used to update Site name for department 
	 * 
	 * @param updateUser
	 * @param siteName
	 * @param oldDepartmentId
	 */
	@Transactional
	public void updateSiteById(String updateUser, String siteName, QiDepartmentId id) {
		Parameters params =Parameters.with("1", updateUser).put("2",siteName ).put("3", id.getSite()).put("4", id.getDepartment()).put("5", id.getPlant());
		executeNativeUpdate(UPDATE_SITE_FOR_DEPT, params);
	}
	
	/**  This method is used to find all Departments by Site name and Plant name
	 * 
	 * @param siteName
	 * @param plantName
	 */
	public List<QiDepartment> findAllBySite(String siteName) {
		return findAll(Parameters.with("id.site", siteName),new String[]{"departmentName"});
	}
	
	/**  This method is used to find all Departments by Site name and Plant name
	 * 
	 * @param siteName
	 * @param plantName
	 */
	public QiDepartment findFirstBySiteAndDepartment(String siteName, String departmentId) {
		return findFirst(Parameters.with("id.site", siteName).put("id.department", departmentId));
	}
	
	/**  This method is used to find all Departments by Site name and Plant name
	 * 
	 * @param siteName
	 * @param plantName
	 */
	public List<QiDepartment> findAllBySiteAndPlant(String siteName, String plantName) {
		return findAll(Parameters.with("id.site", siteName).put("id.plant",plantName),new String[]{"departmentName"});
	}
	
	/**  This method is used to find all Department by Site and Plant
	 * 
	 * @param site
	 * @param plant
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllActiveDepartmentsBySiteAndPlant(String site, String plant){
		return findResultListByQuery(FIND_ALL_ACTIVE_BY_SITE_AND_PLANT , Parameters.with("site", site).put("plant", plant));
	}
	
	/**
	 * This method is used find all QiDepartment by Site-Plant List
	 */
	public List<QiDepartment> findAllActiveBySitePlantList(List<String> sitePlantList) {
		StringBuilder selectedValue = new StringBuilder(FIND_ALL_ACTIVE_BY_SITE_PLANT_LIST);
		if(!sitePlantList.isEmpty()) {
			selectedValue.append(" AND TRIM(SITE)||'-'||TRIM(PLANT) IN ("+StringUtils.join(sitePlantList,',')+") ");
		}
		selectedValue.append("ORDER BY SITE,PLANT,DEPT");
		return findAllByNativeQuery(selectedValue.toString(), null);
	}

	public List<String> findAllAssignedDepts() {
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_DEPT_NAME, null,String.class);
	}
	
	/**  This method is used to find all Department by Site and Plant
	 * 
	 * @param site
	 * @param plant
	 * @param dept
	 */
	@Override
	public QiDepartment findBySitePlantAndDepartment(String site, String plant, String dept){
		return findFirstByQuery(FIND_BY_SITE_PLANT_DEPT , Parameters.with("site", site).put("plant", plant).put("dept", dept));
	}
	
	@Override
	public QiPlantDepartmentDto findByPddaData(String pddaPlantCode, String pddaLine, String pddaDept, String pddaTeamNumber) {
		Parameters params = Parameters.with("1", pddaPlantCode).put("2", pddaLine).put("3", pddaDept).put("4", pddaTeamNumber);
		return findFirstByNativeQuery(FIND_QI_PLANT_DEPARTMENT_BY_PDDA_DATA, params, QiPlantDepartmentDto.class);
	}
}
