package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.service.IDaoService;

public interface QiRepairAreaRowDao extends IDaoService<QiRepairAreaRow, QiRepairAreaRowId>{
	public List<QiRepairAreaRow> findAllByRepairAreaName(String repairAreaName);
	public List<Integer> findAllAvailableByRepairArea(String repairArea);
	public void updateAllByRepairArea(String repairMethodName,String oldRepairMethodName,String updateUser);
	public boolean isRepairAreaUsed(String repairAreaName);
	public QiRepairAreaRow findRepairAreaRowByRepairArea(QiRepairArea repairArea);
}
