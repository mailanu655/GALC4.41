package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationEntryDepartmentId;
import com.honda.galc.service.IDaoService;

public interface QiStationEntryDepartmentDao extends IDaoService<QiStationEntryDepartment, QiStationEntryDepartmentId>{
	/**
	 * This method is used to find EntryDept by process point 
	 */
	public List<String> findAllEntryDeptByProcessPoint(String processPointId);
	/**
	 * This method is used to find all EntryDept by process point
	 */
	public List<QiStationEntryDepartment> findAllEntryDeptInfoByProcessPoint(String processPointId);
	/**
	 * This method is used to find default entry department configured from station configuration screen based on process point.
	 * @param processpointId
	 * @return QiStationEntryDepartment
	 */
	public QiStationEntryDepartment findDefaultEntryDeptByProcessPoint(String processPointId);
	
	public long countEntryDeptInfoByProcessPoint(String processPointId);
	public int deleteByProcessPoint(String processPointId);
}
