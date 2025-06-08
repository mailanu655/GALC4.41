package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.entity.qi.QiStationWriteUpDepartmentId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationWriteUpDepartmentDaoImpl</code> is an implementation class for QiStationWriteUpDepartmentDao interface.
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

public class QiStationWriteUpDepartmentDaoImpl extends BaseDaoImpl<QiStationWriteUpDepartment, QiStationWriteUpDepartmentId> implements QiStationWriteUpDepartmentDao {
	
	private static String FIND_ALL_WRITE_UP_DEPARTMENT_BY_PROCESS_POINT = "select e.id.divisionId from QiStationWriteUpDepartment e " +
																			"where e.id.processPointId= :processPointId order by e.id.divisionId"  ;
	private static String FIND_DEFAULT_WRITE_UP_DEPARTMENT_BY_PROCESS_POINT = "select e from QiStationWriteUpDepartment e where e.id.processPointId= :processPointId and e.isDefault=1" ;
	private static String FIND_DEPT_BY_SITE_AND_PLANT = "select distinct dpt.DEPT from galadm.GAL211TBX plnt, galadm.QI_PLANT_TBX qplnt , galadm.QI_DEPT_TBX dpt , galadm.GAL238TBX gpcs " +
							" where plnt.SITE_NAME=qplnt.ENTRY_SITE and plnt.PLANT_NAME =qplnt.ENTRY_PLANT and plnt.SITE_NAME = gpcs.GPCS_PLANT_CODE and " + 
							" gpcs.GPCS_LINE_NO = qplnt.PROD_LINE_NO and qplnt.SITE=dpt.SITE and qplnt.PLANT=dpt.PLANT and plnt.SITE_NAME=?1 and plnt.PLANT_NAME=?2 and dpt.ACTIVE = 1";
	private static String FIND_COLOR_CODE_BY_DEPT_AND_PROCESS_POINT = "select e.id.colorCode from QiStationWriteUpDepartment e where e.id.divisionId= :divisionId and e.id.processPointId= :processPointId ";

	private static String FIND_DEPT_BY_SITE = "select distinct dpt.DEPT from galadm.QI_PLANT_TBX qplnt , galadm.QI_DEPT_TBX dpt " +
			" where qplnt.SITE=dpt.SITE and qplnt.ENTRY_SITE=?1 and dpt.ACTIVE = 1";

	/**
	 * This method is used to find WriteupDept by process point
	 */
	public List<String> findAllWriteUpDeptByProcessPoint(String processPointId) {
		return findResultListByQuery(FIND_ALL_WRITE_UP_DEPARTMENT_BY_PROCESS_POINT, Parameters.with("processPointId", processPointId));
	}

	/**
	 * This method is used to find Writeup Dept by process point
	 */
	public List<QiStationWriteUpDepartment> findAllWriteUpDepartmentByQicsStation(String qicsStation,String site,String plant){
		return findAll(Parameters.with("id.processPointId", qicsStation).put("id.site", site).put("id.plant", plant),new String[]{"id.divisionId"},true);
	}

	/**
	 * This method is used to find Writeup Dept by process point
	 */
	public long countWriteUpDepartmentByQicsStation(String qicsStation,String site,String plant){
		return count(Parameters.with("id.processPointId", qicsStation).put("id.site", site).put("id.plant", plant));
	}

	@Transactional
	public int deleteByQicsStation(String processPointId) {
		return delete(Parameters.with("id.processPointId", StringUtils.trimToEmpty(processPointId)));
	}
	/**
	 * This method finds default WriteUp Department as configured from Station Configuration Screen based on process point.
	 * @return QiStationWriteUpDepartment
	 */
	public QiStationWriteUpDepartment findDefaultWriteUpDeptByProcessPoint(String processPointId) {
		Parameters params = Parameters.with("processPointId", processPointId);
		return findFirstByQuery(FIND_DEFAULT_WRITE_UP_DEPARTMENT_BY_PROCESS_POINT,params);
	}
	/**
	 * This method is used to find all dept by Site and Plant
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllDeptBySiteAndPlant(String site, String plant){
		return findResultListByNativeQuery(FIND_DEPT_BY_SITE_AND_PLANT, Parameters.with("1", site).put("2",plant));
	}
	/**
	 * This method finds WriteUp Department base on Dept
	 * @return QiStationWriteUpDepartment
	 */
	public QiStationWriteUpDepartment findWriteUpDeptByDept(QiStationWriteUpDepartmentId qiStationWriteUpDepartmentId) {
		return findFirst(Parameters.with("id.site", qiStationWriteUpDepartmentId.getSite()).put("id.plant", qiStationWriteUpDepartmentId.getPlant()).put("id.divisionId",qiStationWriteUpDepartmentId.getDivisionId()).put("id.processPointId", qiStationWriteUpDepartmentId.getProcessPointId()));
	}
	/**
	 * This method finds color code for given process point id and writeup dept
	 */
	public String findColorCodeByWriteupDeptAndProcessPointId(
			String dept, String processPointId) {
		Parameters params = Parameters.with("divisionId", dept).put("processPointId", processPointId);
		return findFirstByQuery(FIND_COLOR_CODE_BY_DEPT_AND_PROCESS_POINT, String.class,params);
	}
	
	public QiStationWriteUpDepartment findDeptByWriteupDeptAndProcessPointId(String writeupDepartment,
			String processPointId) {
		return findFirst(Parameters.with("id.divisionId", writeupDepartment).put("id.processPointId", processPointId));
	}
	
	/**
	 * This method is used to find all dept by Site
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllDeptBySite(String site){
		return findResultListByNativeQuery(FIND_DEPT_BY_SITE, Parameters.with("1", site));
	}
}
