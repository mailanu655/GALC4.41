package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiPlantDepartmentDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.service.IDaoService;

/**
 * <h3>QiDepartmentDao description</h3> <h4>Description</h4>
 * <p>
 * <code>QiDepartmentDao</code> is an interface
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

public interface QiDepartmentDao extends IDaoService<QiDepartment, QiDepartmentId> {

	public void updateDepartment(QiDepartmentId oldDepartmentId, QiDepartment newDept);

	public List<String> findAllDeptBySiteAndPlant(String site, String plant);

	public void updatePlantById(String updateUser, String newPlantName, QiDepartmentId id);

	public void inactivateDepartment(QiDepartmentId id);

	public void updateSiteById(String updateUser, String siteName, QiDepartmentId id);

	public List<QiDepartment> findAllBySiteAndPlant(String siteName, String plantName);

	public List<String> findAllActiveDepartmentsBySiteAndPlant(String site, String plant);
	
	public List<QiDepartment> findAllActiveBySitePlantList(List<String> sitePlantList);

	public List<String> findAllAssignedDepts();

	List<QiDepartment> findAllBySite(String siteName);

	QiDepartment findFirstBySiteAndDepartment(String siteName, String departmentId);

	QiDepartment findBySitePlantAndDepartment(String site, String plant, String dept);
	
	public QiPlantDepartmentDto findByPddaData(String pddaPlantCode, String pddaLine, String pddaDept, String pddaTeamNumber);
}
