package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationEntryDepartmentDao;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationEntryDepartmentId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationEntryDepartmentDaoImpl</code> is an implementation class for QiStationEntryDepartmentDao interface.
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
public class QiStationEntryDepartmentDaoImpl extends BaseDaoImpl<QiStationEntryDepartment, QiStationEntryDepartmentId> implements QiStationEntryDepartmentDao {
	
	private static String FIND_ALL_ENTRY_DEPARTMENT_BY_PROCESS_POINT = "select e.id.divisionId from QiStationEntryDepartment e " +
			"where e.id.processPointId= :processPointId order by e.id.divisionId"  ;

	/**
	 * This method is used to find EntryDept by process point 
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllEntryDeptByProcessPoint(String processPointId) {
		return findResultListByQuery(FIND_ALL_ENTRY_DEPARTMENT_BY_PROCESS_POINT, Parameters.with("processPointId", processPointId));
	}

	/**
	 * This method is used to find all EntryDept by process point
	 */
	public List<QiStationEntryDepartment> findAllEntryDeptInfoByProcessPoint(String processPointId) {
		return findAll(Parameters.with("id.processPointId", processPointId),new String[]{"id.divisionId"},true);
	}
	/**
	 * This method is used to delete EntryDept by process point
	 */
	@Transactional
	public int deleteByProcessPoint(String processPointId) {
		return delete(Parameters.with("id.processPointId", processPointId));
	}
	/**
	 * This method is used to find all EntryDept by process point
	 */
	public long countEntryDeptInfoByProcessPoint(String processPointId) {
		return count(Parameters.with("id.processPointId", processPointId));
	}
	
	/**
	 * This method is used to find default entry department configured from station configuration screen based on process point.
	 * @param processpointId
	 * @return QiStationEntryDepartment
	 */
	public QiStationEntryDepartment findDefaultEntryDeptByProcessPoint(String processPointId) {
		return findFirst(Parameters.with("id.processPointId",StringUtils.trimToEmpty(processPointId)).put("isDefault",(short)1));
	}
}
