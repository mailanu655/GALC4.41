package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.entity.qi.QiStationWriteUpDepartmentId;
import com.honda.galc.service.IDaoService;

/**
* <h3>Class description</h3> <h4>Description</h4>
* <p>
* <code>QiStationWriteUpDepartmentDao</code> is a DAO interface to implement database interaction for Station Write Up Department.
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
* <TD>22/11/2016</TD>
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

public interface QiStationWriteUpDepartmentDao extends IDaoService<QiStationWriteUpDepartment, QiStationWriteUpDepartmentId>{
	/**
	 * This method is used to find WriteupDept by process point
	 */
	public List<String> findAllWriteUpDeptByProcessPoint(String processPointId);
	/**
	 * This method is used to find Writeup Dept by process point
	 */
	public List<QiStationWriteUpDepartment> findAllWriteUpDepartmentByQicsStation(String qicsStation,String site,String plant);
	/**
	 * This method finds default WriteUp Department as configured from Station Configuration Screen based on process point.
	 * @return QiStationWriteUpDepartment
	 */
	public QiStationWriteUpDepartment findDefaultWriteUpDeptByProcessPoint(String processPointId);
	/**
	 * This method is used to find all dept by Site and Plant
	 */
	public List<String> findAllDeptBySiteAndPlant(String site, String plant);
	/**
	 * This method finds WriteUp Department base on Dept
	 * @return QiStationWriteUpDepartment
	 */
	public QiStationWriteUpDepartment findWriteUpDeptByDept(QiStationWriteUpDepartmentId qiStationWriteUpDepartmentId);
	/**
	 * This method finds color code for given process point id and writeup dept
	 */
	public String findColorCodeByWriteupDeptAndProcessPointId(String dept, String processPointId);
	
	public QiStationWriteUpDepartment findDeptByWriteupDeptAndProcessPointId(String writeupDepartment,String processPointId);
	
	/**
	 * This method is used to find all dept by Site
	 */
	public List<String> findAllDeptBySite(String site);

	public long countWriteUpDepartmentByQicsStation(String qicsStation,String site,String plant);
	public int deleteByQicsStation(String qicsStation);

}
