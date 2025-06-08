package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiRepairAreaRowDao;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.service.Parameters;

public class QiRepairAreaRowDaoImpl extends BaseDaoImpl<QiRepairAreaRow, QiRepairAreaRowId> implements QiRepairAreaRowDao {
	
	private static final String UPDATE_REPAIR_AREA_ROW_BY_REPAIR_AREA = "update GALADM.QI_REPAIR_AREA_ROW_TBX  set REPAIR_AREA_NAME = ?1, UPDATE_USER = ?2" +			
			" where REPAIR_AREA_NAME = ?3";
	private static final String FIND_REPAIR_AREA_ROW_BY_REPAIR_AREA = "SELECT * FROM GALADM.QI_REPAIR_AREA_ROW_TBX R  WHERE R.REPAIR_AREA_NAME = ?1 "
			+ " AND R.REPAIR_AREA_ROW IN (SELECT E.REPAIR_AREA_ROW FROM GALADM.QI_REPAIR_AREA_SPACE_TBX E "
			+ " WHERE E.PRODUCT_ID IS NULL AND E.DEFECTRESULTID IS NULL AND E.REPAIR_AREA_NAME= ?1 AND E.ACTIVE = 1 )  ";

	private static final String FIND_ALL_AVAILABLE_BY_REPAIR_AREA = "select DISTINCT a.REPAIR_AREA_ROW from galadm.QI_REPAIR_AREA_ROW_TBX a where "
			+ " a.REPAIR_AREA_NAME= ?1 and a.REPAIR_AREA_ROW in (select e.REPAIR_AREA_ROW from GALADM.QI_REPAIR_AREA_SPACE_TBX e "
			+ "where e.PRODUCT_ID IS NULL and e.REPAIR_AREA_NAME= ?1 and e.active = 1 ) order by a.REPAIR_AREA_ROW";

	public List<QiRepairAreaRow> findAllByRepairAreaName(String repairAreaName){
		Parameters params = Parameters.with("id.repairAreaName", repairAreaName);
		return findAll(params, new String[]{"id.repairAreaRow"}, true);
	}
	
	public List<Integer> findAllAvailableByRepairArea(String repairArea) {
		Parameters params = Parameters.with("1", repairArea);
		return findAllByNativeQuery(FIND_ALL_AVAILABLE_BY_REPAIR_AREA,params, Integer.class);
	}
	
	@Transactional
	public void updateAllByRepairArea(String repairMethodName, String oldRepairMethodName, String updateUser) {
		Parameters params = Parameters.with("1", repairMethodName)
				.put("2", updateUser)
				.put("3", oldRepairMethodName);			
		executeNativeUpdate(UPDATE_REPAIR_AREA_ROW_BY_REPAIR_AREA, params);
	}
	
	public QiRepairAreaRow findRepairAreaRowByRepairArea(QiRepairArea repairArea) {
		Parameters params = Parameters.with("1", repairArea.getRepairAreaName());
		StringBuilder queryString = new StringBuilder(FIND_REPAIR_AREA_ROW_BY_REPAIR_AREA);
		if(repairArea.getRowFillSeq() == 'D') {
			queryString.append(" ORDER BY R.REPAIR_AREA_ROW DESC ");
		}
		
		else {
			queryString.append(" ORDER BY R.REPAIR_AREA_ROW  ");
		}
		
		return findFirstByNativeQuery(queryString.toString(), params, QiRepairAreaRow.class);
	}
	
	public boolean isRepairAreaUsed(String repairAreaName) {
		QiRepairAreaRow qiRepairAreaRow = findFirst(Parameters.with("id.repairAreaName", repairAreaName)); 
		if (qiRepairAreaRow != null) {
			return true;
		} else {
			return false;
		}
	}
}
