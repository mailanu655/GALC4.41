package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiResponsibleLevelDaoImpl</code> is an implementation class for QiResponsibleLevelDao interface.
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

public class QiResponsibleLevelDaoImpl extends BaseDaoImpl<QiResponsibleLevel, Integer> implements QiResponsibleLevelDao {
	
	private static final String  FIND_ALL_LEVEL1_BY_SITE_PLANT_AND_DEPT = "SELECT RESPONSIBLE_LEVEL_NAME FROM GALADM.QI_RESPONSIBLE_LEVEL_TBX " +
			"WHERE SITE=?1 AND PLANT=?2 AND DEPT=?3 AND LEVEL=1";
	
	private final static String FIND_RESPOSIBLE_LEVEL_BY_NAME_DEPT = "select distinct e from QiResponsibleLevel e where  e.responsibleLevelName = :responsibleLevel and e.department = :department ";

	private final static String UPDATE_RESPONSIBLE_LEVEL = "Update galadm.QI_RESPONSIBLE_LEVEL_TBX set RESPONSIBLE_LEVEL_NAME=?1, RESPONSIBLE_LEVEL_DESCRIPTION=?2, UPPER_RESPONSIBLE_LEVEL_ID=?3, ACTIVE=?4, UPDATE_USER=?5 where RESPONSIBLE_LEVEL_ID=?6 ";
	
	private static final String FIND_LEVEL2_AND_LEVEL3_BY_LEVEL1 =
			"SELECT b.RESPONSIBLE_LEVEL_NAME AS LEVEL_TWO,c.RESPONSIBLE_LEVEL_NAME AS LEVEL_THREE " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" LEFT OUTER JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID " +
			" LEFT OUTER JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " + 
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.RESPONSIBLE_LEVEL_NAME=?4 and a.LEVEL=1";
	
	private static final String FIND_LEVEL_BY_LVL_AND_NAME_AND_UPPER_LVL_AND_NAME =
			"SELECT a.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID " +
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.RESPONSIBLE_LEVEL_NAME=?4 and b.RESPONSIBLE_LEVEL_NAME=?5 and a.LEVEL=?6 and b.LEVEL=?7 and a.ACTIVE=1";
	
	private static final String FIND_RESP_LEVEL_BY_SITE_PLANT_DEPT_L1L2L3NAMES =
			"SELECT a.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " +
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.RESPONSIBLE_LEVEL_NAME=?4 and a.LEVEL=1 and a.ACTIVE=1 and " +
			" b.RESPONSIBLE_LEVEL_NAME=?5 and b.LEVEL=2 and " +
			" c.RESPONSIBLE_LEVEL_NAME=?6 and c.LEVEL=3";
	
	private static final String FIND_ALL_L2_HAVING_SAME_L1 =
			"SELECT b.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and a.RESPONSIBLE_LEVEL_NAME=?4 and b.ACTIVE=1" ;
	
	//new
	private static final String FIND_ALL_L2_HAVING_ASSIGNED_L1 =
			"SELECT b.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX b " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX a on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID " +
			" JOIN galadm.QI_STATION_RESPONSIBILITY_TBX c on a.RESPONSIBLE_LEVEL_ID = c.RESPONSIBLE_LEVEL_ID and c.PROCESS_POINT_ID=?4 " +
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and b.ACTIVE=1" ;
	
	//new
	private static final String FIND_ALL_ASSIGNED_L2_HAVING_SAME_L1 =
			"SELECT b.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX b " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX a on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID " +
			" JOIN galadm.QI_STATION_RESPONSIBILITY_TBX c on a.RESPONSIBLE_LEVEL_ID = c.RESPONSIBLE_LEVEL_ID and c.PROCESS_POINT_ID=?5 " +
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and a.RESPONSIBLE_LEVEL_NAME=?4 and b.ACTIVE=1 " ;
	
	
	private static final String FIND_ALL_L3_HAVING_SAME_L1 =
			"SELECT c.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on " +
				" b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID or a.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " +
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and c.LEVEL=3 and a.RESPONSIBLE_LEVEL_NAME=?4 and c.ACTIVE=1" ;
	
	//new
	private static final String FIND_ALL_L3_HAVING_ASSIGNED_L1 =
			"SELECT c.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on " +
				" b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID or a.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " +
			" JOIN galadm.QI_STATION_RESPONSIBILITY_TBX d on a.RESPONSIBLE_LEVEL_ID = d.RESPONSIBLE_LEVEL_ID and d.PROCESS_POINT_ID=?4 " +
			" where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and c.LEVEL=3 and c.ACTIVE=1" ;
	
	private static final String FIND_ALL_L2_HAVING_SAME_L3 =
			"SELECT b.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX b " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID "+
			" where c.SITE=?1 and c.PLANT=?2 and c.DEPT=?3 and c.LEVEL=3 and b.LEVEL=2 and c.RESPONSIBLE_LEVEL_NAME=?4 and b.ACTIVE=1" ;
	
	private static final String FIND_ALL_L1_HAVING_SAME_L3 =
			"SELECT a.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on " +
				" b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID or a.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " +
			" where c.SITE=?1 and c.PLANT=?2 and c.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and c.LEVEL = 3 and c.RESPONSIBLE_LEVEL_NAME=?4 and a.ACTIVE=1" ;
	//new
	private static final String FIND_ALL_ASSIGNED_L1_HAVING_SAME_L3 =
			"SELECT a.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on " +
				" b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID or a.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " +
			" JOIN galadm.QI_STATION_RESPONSIBILITY_TBX d on a.RESPONSIBLE_LEVEL_ID = d.RESPONSIBLE_LEVEL_ID and d.PROCESS_POINT_ID=?5 " +
			" where c.SITE=?1 and c.PLANT=?2 and c.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and c.LEVEL = 3 and c.RESPONSIBLE_LEVEL_NAME=?4 and a.ACTIVE=1" ;
	
	private static final String FIND_ALL_L3_HAVING_SAME_L2 =
			"SELECT c.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX c " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID "+
			" where b.SITE=?1 and b.PLANT=?2 and b.DEPT=?3 and b.LEVEL=2 and c.LEVEL=3 and b.RESPONSIBLE_LEVEL_NAME=?4 and c.ACTIVE=1" ;
	
	private static final String FIND_ALL_L1_HAVING_SAME_L2 =
			"SELECT a.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on "
			+ 	" a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID and b.LEVEL=2"+
			" where b.SITE=?1 and b.PLANT=?2 and b.DEPT=?3 and a.LEVEL=1 and b.RESPONSIBLE_LEVEL_NAME=?4 and a.ACTIVE=1" ;
	
	private static final String FIND_ALL_ASSIGNED_L1_HAVING_SAME_L2 =
			"SELECT a.* " +
			" FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			" JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on "
			+ 	" a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			" JOIN galadm.QI_STATION_RESPONSIBILITY_TBX d on a.RESPONSIBLE_LEVEL_ID = d.RESPONSIBLE_LEVEL_ID and d.PROCESS_POINT_ID=?5 " +
			" where b.SITE=?1 and b.PLANT=?2 and b.DEPT=?3 and a.LEVEL=1 and b.LEVEL=2 and b.RESPONSIBLE_LEVEL_NAME=?4 and a.ACTIVE=1" ;
	
	private static final String FIND_ALL_ACTIVE_BY_SITE_PLANT_AND_DEPT_LIST = "SELECT * FROM galadm.QI_RESPONSIBLE_LEVEL_TBX WHERE ACTIVE=1 AND LEVEL=1 ";
	
	private final static String FIND_ALL_ASSIGNED_RESP_LEVEL =
			"SELECT distinct * FROM ( "
			+ "(select a.* from GALADM.QI_RESPONSIBLE_LEVEL_TBX a "
			+	"where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.RESPONSIBLE_LEVEL_NAME=?4 "
			+ 	"and a.RESPONSIBLE_LEVEL_ID in (select RESPONSIBLE_LEVEL_ID from GALADM.QI_STATION_RESPONSIBILITY_TBX)) "
			+ "UNION ALL "
			+ 	"(SELECT a.* FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a "
			+	"where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.RESPONSIBLE_LEVEL_NAME=?4 "
    		+ 	"and a.RESPONSIBLE_LEVEL_ID in (select RESPONSIBLE_LEVEL_ID from QI_LOCAL_DEFECT_COMBINATION_TBX)) "
			+ ")";
	
	private static final String FIND_ALL_ACTIVE_LEVEL1_BY_SITE_PLANT_AND_DEPT = "SELECT RESPONSIBLE_LEVEL_NAME FROM GALADM.QI_RESPONSIBLE_LEVEL_TBX WHERE SITE=?1 AND PLANT=?2 AND DEPT=?3 AND LEVEL=1 AND ACTIVE=1";
	
	private static final String FIND_ALL_BY_SITE_PLANT_DEPT_AND_RESPONSIBLE_NAME = "SELECT * FROM galadm.QI_RESPONSIBLE_LEVEL_TBX WHERE SITE=?1 AND PLANT=?2 AND DEPT=?3 AND RESPONSIBLE_LEVEL_NAME=?4 ";

	private static final String FIND_ALL_ACTIVE_LEVEL1_WITH_UPPER_LEVEL_BY_SITE_PLANT_AND_DEPT =
			"SELECT a.RESPONSIBLE_LEVEL_NAME AS LEVEL1, b.RESPONSIBLE_LEVEL_NAME AS LEVEL2, " + 
			"c.RESPONSIBLE_LEVEL_NAME AS LEVEL3 FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			"LEFT OUTER JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID " +
			"LEFT OUTER JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID " + 
			"where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and a.ACTIVE=1";
	
	private static final String FIND_LEVEL1_BY_LEVEL1_LEVEL2 =
			"SELECT a.* FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			"JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on " +
			"a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			"where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and a.RESPONSIBLE_LEVEL_NAME=?4 " + 
			"and b.LEVEL=2 and b.RESPONSIBLE_LEVEL_NAME=?5 and b.UPPER_RESPONSIBLE_LEVEL_ID=0";
	
	private static final String FIND_LEVEL1_BY_LEVEL1_LEVEL2_LEVEL3 =
			"SELECT a.* FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a " +
			"JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX b on " +
			"a.UPPER_RESPONSIBLE_LEVEL_ID=b.RESPONSIBLE_LEVEL_ID "+
			"JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX c on " +
			"b.UPPER_RESPONSIBLE_LEVEL_ID=c.RESPONSIBLE_LEVEL_ID "+
			"where a.SITE=?1 and a.PLANT=?2 and a.DEPT=?3 and a.LEVEL=1 and a.RESPONSIBLE_LEVEL_NAME=?4 " + 
			"and b.LEVEL=2 and b.RESPONSIBLE_LEVEL_NAME=?5 " + 
			"and c.LEVEL=3 and c.RESPONSIBLE_LEVEL_NAME=?6";	
	
	public List<String> findAllActiveLevel1BySitePlantAndDept(String site, String plant,String deptName){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName);
		return findAllByNativeQuery(FIND_ALL_ACTIVE_LEVEL1_BY_SITE_PLANT_AND_DEPT, params, String.class);
	}
	
	
	@Override
	public List<QiResponsibleLevel> findAllLevel2HavingSameLevel1(String site, String plant,String deptName, String lvl1Name){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvl1Name);
		return findAllByNativeQuery(FIND_ALL_L2_HAVING_SAME_L1, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllLevel2HavingAssignedLevel1(String site, String plant,String deptName, String processPointId){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", processPointId);
		return findAllByNativeQuery(FIND_ALL_L2_HAVING_ASSIGNED_L1, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllAssignedLevel2HavingSameLevel1(String site, String plant,String deptName, String lvlName1, String processPointId){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvlName1)
				.put("5", processPointId);
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_L2_HAVING_SAME_L1, params, QiResponsibleLevel.class);
	}
	@Override
	public List<QiResponsibleLevel> findAllAssignedLevel1HavingSameLevel2(String site, String plant,String deptName, String lvlName2, String processPointId){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvlName2)
				.put("5", processPointId);
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_L1_HAVING_SAME_L2, params, QiResponsibleLevel.class);
	}
		@Override
	public List<QiResponsibleLevel> findAllLevel3HavingSameLevel1(String site, String plant,String deptName, String lvl1Name){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvl1Name);
		return findAllByNativeQuery(FIND_ALL_L3_HAVING_SAME_L1, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllLevel3HavingAssignedLevel1(String site, String plant,String deptName, String processPointId){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", processPointId);
		return findAllByNativeQuery(FIND_ALL_L3_HAVING_ASSIGNED_L1, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllLevel1HavingSameLevel2(String site, String plant,String deptName, String lvl1Name){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvl1Name);
		return findAllByNativeQuery(FIND_ALL_L1_HAVING_SAME_L2, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllLevel3HavingSameLevel2(String site, String plant,String deptName, String lvl1Name){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvl1Name);
		return findAllByNativeQuery(FIND_ALL_L3_HAVING_SAME_L2, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllLevel1HavingSameLevel3(String site, String plant,String deptName, String lvl1Name){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvl1Name);
		return findAllByNativeQuery(FIND_ALL_L1_HAVING_SAME_L3, params, QiResponsibleLevel.class);
	}
	@Override
	public List<QiResponsibleLevel> findAllAssignedLevel1HavingSameLevel3(String site, String plant,String deptName, String lvlName3, String processPointId){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvlName3)
				.put("5", processPointId);
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_L1_HAVING_SAME_L3, params, QiResponsibleLevel.class);
	}
	
	@Override
	public List<QiResponsibleLevel> findAllLevel2HavingSameLevel3(String site, String plant,String deptName, String lvl1Name){
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName)
				.put("4", lvl1Name);
		return findAllByNativeQuery(FIND_ALL_L2_HAVING_SAME_L3, params, QiResponsibleLevel.class);
	}
	
	public List<String> findAllLevel1BySitePlantAndDept(String site, String plant,String deptName) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", deptName);
		return findAllByNativeQuery(FIND_ALL_LEVEL1_BY_SITE_PLANT_AND_DEPT, params, String.class);
	} 	

	public List<QiResponsibleLevel> findAllBySitePlantAndDepartment(String site, String plant, String department, short level, Integer upperResponsibleLevelId) {
		Parameters params = new Parameters();
		params.put("site", site).put("plant", plant).put("department", department);
		if(level != 0)
			params.put("level", level);
		if(upperResponsibleLevelId != 0)
			params.put("upperResponsibleLevelId", upperResponsibleLevelId);
		return findAll(params, new String[]{"responsibleLevelName"}, true);
	}

	public QiResponsibleLevel findBySitePlantDepartmentAndLevelName(String site, String plant, String department, String levelName) {
		return findFirst(Parameters.with("site", site).put("plant", plant).put("department", department).put("responsibleLevelName", levelName));
	}
	
	public QiResponsibleLevel findByResponsibleLevelId(int responsibleLevelId) {
		return findByKey(responsibleLevelId);
	}
	
	public QiResponsibleLevel findByNameAndDeptId(String responsibleLevelName , String dept) {
		Parameters params = Parameters.with("responsibleLevel", responsibleLevelName).put("department", dept);
		return findFirstByQuery(FIND_RESPOSIBLE_LEVEL_BY_NAME_DEPT,params);
	}

	public List<QiResponsibleLevel> findBySitePlantDepartmentAndLevel(String site ,String plant,String department,short level) {
		return findAll(Parameters.with("site", site).put("plant",plant).put("department",department).put("level",level));
	}
	
	public List<QiResponsibleLevel> findAllByLevel (short level) {
		return findAll(Parameters.with("level",level));
	}
	
	public QiResponsibleLevel findBySitePlantDepartmentLevelNameAndLevel(String site, String plant, String department, String levelName, short level) {
		return findFirst(Parameters.with("site", site).put("plant", plant).put("department", department).put("responsibleLevelName", levelName).put("level", level));
	}

	@Override
	@Transactional  
	public QiResponsibleLevel findBySitePlantDeptLvlAndNameAndUpperLvlAndName(String site,String plant, String dept, String lvlName, String upperLvlName, short lvl, short upperLvl) {
		Parameters params = Parameters.with("1", site).put("2", plant).put("3", dept)
				.put("4", lvlName).put("5", upperLvlName)
				.put("6", lvl).put("7",upperLvl);
		List<QiResponsibleLevel> rList = findAllByNativeQuery(FIND_LEVEL_BY_LVL_AND_NAME_AND_UPPER_LVL_AND_NAME, params, QiResponsibleLevel.class);
		if(rList != null && !rList.isEmpty())  return rList.get(0);
		else  return null;
	}

	@Override
	@Transactional  
	public QiResponsibleLevel findResponsibleLevel1BySitePlantDeptL1L2L3Names(String site,String plant, String dept, String l1Name, String l2Name, String l3Name) {
		Parameters params = Parameters.with("1", site).put("2", plant).put("3", dept)
				.put("4", l1Name).put("5", l2Name).put("6", l3Name);
		List<QiResponsibleLevel> rList = findAllByNativeQuery(FIND_RESP_LEVEL_BY_SITE_PLANT_DEPT_L1L2L3NAMES, params, QiResponsibleLevel.class);
		if(rList != null && !rList.isEmpty())  return rList.get(0);
		else  return null;
	}

	@Transactional  
	public void updateResponsibleLevel(String responsibleLevelName, String responsibleLevelDesc,
			int upperResponsibleLevelId, int responsibleLevelId, int active, String updateUser) {
		Parameters params = Parameters.with("1", responsibleLevelName).put("2", responsibleLevelDesc).put("3", upperResponsibleLevelId).put("4", active).put("5", updateUser).put("6", responsibleLevelId);
		 executeNativeUpdate(UPDATE_RESPONSIBLE_LEVEL, params);
		
	}
	
	/**
	 * This method is used to update plantName for Responsible Level 
	 * 
	 * @param updateUser
	 * @param newPlantName
	 * @param siteName
	 * @param oldPlantName
	 */
	@Transactional
	public void updatePlantById(String newPlantName, String updateUser, int id) {
		update(Parameters.with("updateUser", updateUser).put("plant", newPlantName), Parameters.with("responsibleLevelId", id));
	}

	/**
	 * This method is used to make department inactive 
	 * 
	 * @param responsibleLevelId
	 */
	@Transactional
	public void inactivateResponsibleLevel(int id) {
		update(Parameters.with("active", 0), Parameters.with("responsibleLevelId", id));
	}

	/**
	 * This method is used to update department for Responsible Level 
	 *
	 * @param deptId
	 * @param updateUser
	 * @param id
	 */
	@Transactional
	public void updateDeptById(String department, String updateUser, int id) {
		update(Parameters.with("updateUser", updateUser).put("department", department), Parameters.with("responsibleLevelId", id));
	}

	
	/**
	 * This method is used to update site for Responsible Level 
	 *
	 * @param siteName
	 * @param updateUser
	 * @param id
	 */
	@Transactional
	public void updateSiteById(String siteName, String updateUser, int id) {
		update(Parameters.with("site", siteName).put("updateUser", updateUser), Parameters.with("responsibleLevelId", id));
	}

	public List<QiResponsibleLevel> findAllByUpperResponsibleLevel(int id) {
		return findAll(Parameters.with("upperResponsibleLevelId",id));
	}

	public List<QiResponsibleLevel> findAllBySitePlantDepartment(String site, String plant, String dept) {
		return findAll(Parameters.with("site", site).put("plant",plant).put("department",dept));
	}
	
	public List<QiResponsibleLevel> findAllBySitePlantDepartmentLevel(String site, String plant,String deptName,short level) {
		Parameters params = Parameters.with("site", site)
				.put("plant", plant)
				.put("department", deptName)
				.put("level", level)
				.put("active", (int)1);
		return findAll( params,new String[]{"responsibleLevelName"}, true);
	}
	
	public QiResponsibleLevel findByResponsibleLevelId(Integer responsibleLevelId) {
		Parameters params = Parameters.with("responsibleLevelId", responsibleLevelId)
				.put("active", (int)1);
		return findFirst(params);
	}

	/**
	 * This method is used to find Level2 and Level3 by Level1
	 */
	public List<QiDefectResultDto> findAllLevel2AndLevel3ByLevel1(String site,
			String plant, String dept, String level1) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", dept)
				.put("4", level1);
		return findAllByNativeQuery(FIND_LEVEL2_AND_LEVEL3_BY_LEVEL1,params,QiDefectResultDto.class);
	}
	
	/**
	 * This method is used to find all QiResponsibile by Site-Plant-Dept List
	 */
	public List<QiResponsibleLevel> findAllActiveBySitePlantAndDeptList(List<String> sitePlantDeptList) {
		StringBuilder selectedValue = new StringBuilder(FIND_ALL_ACTIVE_BY_SITE_PLANT_AND_DEPT_LIST);
		if(!sitePlantDeptList.isEmpty()) {
			selectedValue.append(" AND TRIM(SITE)||'-'||TRIM(PLANT)||'-'||TRIM(DEPT) IN ("+StringUtils.join(sitePlantDeptList,',')+") ");
		}
		selectedValue.append("ORDER BY SITE,PLANT,DEPT,RESPONSIBLE_LEVEL_NAME");
		return findAllByNativeQuery(selectedValue.toString(), null);
	}

	public List<QiResponsibleLevel> findAllAssignedRespLevelsBySitePlantDeptName(String site, String plant, String dept,  String respName) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", dept)
				.put("4", respName);
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_RESP_LEVEL, params,QiResponsibleLevel.class);
	}

	public QiResponsibleLevel findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(String site,
			String plant, String department, String levelName, short level, int upperResponsibleId) {
		return findFirst(Parameters.with("site", site).put("plant", plant).put("department", department).put("responsibleLevelName", levelName).put("level", level).put("upperResponsibleLevelId", upperResponsibleId));
	} 
	
	public List<QiResponsibleLevel> findAllRespLevelsBySitePlantDeptandName(String site, String plant, String dept,  String respName) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", dept)
				.put("4", respName);
		return findAllByNativeQuery(FIND_ALL_BY_SITE_PLANT_DEPT_AND_RESPONSIBLE_NAME, params,QiResponsibleLevel.class);
		}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findAllActiveLevelsBySitePlantDeptName(String site, String plant, String dept) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant)
				.put("3", dept);
		return findResultListByNativeQuery(FIND_ALL_ACTIVE_LEVEL1_WITH_UPPER_LEVEL_BY_SITE_PLANT_AND_DEPT, params);
	}	
	
	public QiResponsibleLevel getResponsibleLevelId(String site, String plant, String dept, String level, String respLevelName) {
		short respLevel = (short) Integer.parseInt(level);
		return findFirst(Parameters.with("site", site).put("plant", plant).put("department", dept).put("responsibleLevelName", respLevelName).put("level", respLevel));
	}
	
	//find responsible level 1 based on defect result history
	public QiResponsibleLevel findDefaultResponsibleLevel1(QiDefectResultHist defectResultHist) {
		QiResponsibleLevel defaultResponsibleLevel1 = null;
		String site = defectResultHist.getResponsibleSite();
		String plant = defectResultHist.getResponsiblePlant();
		String department = defectResultHist.getResponsibleDept();
		String respLevel1 = defectResultHist.getResponsibleLevel1();
		String respLevel2 = defectResultHist.getResponsibleLevel2();
		String respLevel3 = defectResultHist.getResponsibleLevel3();
		
		Parameters params;
		List<QiResponsibleLevel> respLevel1List;
		
		if (StringUtils.isBlank(respLevel2)) { //no level 2 or 3
			params = Parameters.with("site", site).put("plant", plant).put("department", department);
			params.put("responsibleLevelName", respLevel1).put("level", (short)1).put("upperResponsibleLevelId", 0);
			defaultResponsibleLevel1 = findFirst(params);
		} else {
			if (StringUtils.isBlank(respLevel3)) {//no level 3 but level 2
				params = Parameters.with("1", site).put("2", plant).put("3", department).put("4", respLevel1).put("5", respLevel2);
				respLevel1List = findAllByNativeQuery(FIND_LEVEL1_BY_LEVEL1_LEVEL2, params, QiResponsibleLevel.class);
				defaultResponsibleLevel1 = respLevel1List.get(0);
			} else { //level 2 and 3
				params = Parameters.with("1", site).put("2", plant).put("3", department).put("4", respLevel1).put("5", respLevel2).put("6", respLevel3);
				respLevel1List = findAllByNativeQuery(FIND_LEVEL1_BY_LEVEL1_LEVEL2_LEVEL3, params, QiResponsibleLevel.class);
				defaultResponsibleLevel1 = respLevel1List.get(0);
			}
		}
		return defaultResponsibleLevel1;
	}
}
