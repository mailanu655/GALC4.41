package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Interface description</h3>
 * <p>
 * <code>QiResponsibleLevelDao</code> is a DAO interface to implement database interaction for Responsible level.
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

public interface QiResponsibleLevelDao extends IDaoService<QiResponsibleLevel, Integer> {
	
	public List<String> findAllLevel1BySitePlantAndDept(String site, String plant,String deptName);
		
	public List<QiResponsibleLevel> findAllBySitePlantAndDepartment(String site, String plant, String department, short level, Integer upperResponsibleLevelId);
	
	public QiResponsibleLevel findBySitePlantDepartmentAndLevelName(String site, String plant, String department, String levelName);
	
	public QiResponsibleLevel findByNameAndDeptId(String responsibleLevelName, String dept);
	
	public List<QiResponsibleLevel> findBySitePlantDepartmentAndLevel(String site ,String plant,String department,short level);
	
	public List<QiResponsibleLevel> findAllByLevel (short level);
	
	public QiResponsibleLevel findBySitePlantDepartmentLevelNameAndLevel(String site, String plant, String department, String levelName, short level);
	
	public QiResponsibleLevel findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(String site, String plant, String department, String levelName, short level,int upperResponsibleId);	
	
	public void updateResponsibleLevel(String responsibleLevelName, String responsibleLevelDesc, int upperResponsibleLevelId, int responsibleLevelId, int active, String updateUser);
	
	public void updatePlantById(String newPlantName, String updateUser, int id);

	public void inactivateResponsibleLevel(int id);

	public void updateDeptById(String department, String updateUser, int id);

	public void updateSiteById(String siteName, String updateUser, int id);

	public List<QiResponsibleLevel> findAllByUpperResponsibleLevel(int id);

	public List<QiResponsibleLevel> findAllBySitePlantDepartment(String site, String plant, String dept);
	
	public List<QiResponsibleLevel> findAllBySitePlantDepartmentLevel(String site, String plant,String deptName,short level);
	
	public QiResponsibleLevel findByResponsibleLevelId(Integer responsibleLevelId) ;
	
	public List<QiDefectResultDto> findAllLevel2AndLevel3ByLevel1(String site, String plant, String dept, String level1);
	
	public List<QiResponsibleLevel> findAllActiveBySitePlantAndDeptList(List<String> sitePlantDeptList);

	public List<QiResponsibleLevel> findAllAssignedRespLevelsBySitePlantDeptName(String site, String plant, String dept, String respName);
	
	public List<QiResponsibleLevel> findAllRespLevelsBySitePlantDeptandName(String site, String plant, String dept,  String respName);

	public List<String> findAllActiveLevel1BySitePlantAndDept(String site, String plant,String deptName);

	List<QiResponsibleLevel> findAllLevel3HavingSameLevel1(String site, String plant, String deptName, String lvl1Name);

	List<QiResponsibleLevel> findAllLevel2HavingSameLevel1(String site, String plant, String deptName, String lvl1Name);

	List<QiResponsibleLevel> findAllLevel2HavingSameLevel3(String site, String plant, String deptName, String lvl1Name);

	List<QiResponsibleLevel> findAllLevel1HavingSameLevel3(String site, String plant, String deptName, String lvl1Name);

	List<QiResponsibleLevel> findAllLevel3HavingSameLevel2(String site, String plant, String deptName, String lvl1Name);

	List<QiResponsibleLevel> findAllLevel1HavingSameLevel2(String site, String plant, String deptName, String lvl1Name);

	QiResponsibleLevel findResponsibleLevel1BySitePlantDeptL1L2L3Names(String site, String plant, String dept, String l1Name,
			String l2Name, String l3Name);

	QiResponsibleLevel findBySitePlantDeptLvlAndNameAndUpperLvlAndName(String site, String plant, String dept, String lvlName,
			String upperLvlName, short lvl, short upperLvl);	
	
	public List<Object[]> findAllActiveLevelsBySitePlantDeptName(String site, String plant, String dept);
	List<QiResponsibleLevel> findAllLevel2HavingAssignedLevel1(String site, String plant, String deptName, String processPointId);

	List<QiResponsibleLevel> findAllLevel3HavingAssignedLevel1(String site, String plant, String deptName, String processPointId);

	List<QiResponsibleLevel> findAllAssignedLevel1HavingSameLevel3(String site, String plant, String deptName, String lvlName3, String processPointId);

	List<QiResponsibleLevel> findAllAssignedLevel1HavingSameLevel2(String site, String plant, String deptName, String lvlName2, String processPointId);

	List<QiResponsibleLevel> findAllAssignedLevel2HavingSameLevel1(String site, String plant, String deptName, String lvlName1, String processPointId);	

	public QiResponsibleLevel getResponsibleLevelId(String site, String plant, String dept, String level, String respLevelName);
	
	public QiResponsibleLevel findDefaultResponsibleLevel1(QiDefectResultHist defectResultHist);
}
