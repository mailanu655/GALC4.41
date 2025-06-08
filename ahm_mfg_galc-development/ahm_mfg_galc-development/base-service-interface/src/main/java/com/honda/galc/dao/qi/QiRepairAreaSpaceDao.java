package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiRepairAreaSapceAssignmentDto;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.service.IDaoService;

public interface QiRepairAreaSpaceDao extends IDaoService<QiRepairAreaSpace, QiRepairAreaSpaceId> {
	public QiRepairAreaSpace findByProductId(String productId);
	public void clearRepairAreaSpace(QiRepairAreaSpaceId id, String updateUser);
	public List<QiRepairAreaSapceAssignmentDto> findAllSpaceAssignmentByRepairAreaNameAndRow(QiRepairAreaRowId areaRowId);
	public List<QiRepairAreaSpace> findAllByRepairAreaNameAndRow(String repairAreaName, int repairAreaRow);
	public boolean isRepairAreaUsed(String repairAreaName);
	public void updateAllByRepairArea(String repairMethodName,String oldRepairMethodName,String updateUser);
	public QiRepairAreaSpace findFirstAvailableByRow(QiRepairAreaRow row);
	public List<Integer> findAllAvailableByRepairAreaAndRow(String repairArea, Integer repairAreaRow);
	public void assignRepairAreaSpaceWithTarget(String productId, long defectResultId, String updateUser, String targetRespDept, String targetRepairArea, QiRepairAreaSpaceId qiRepairAreaSpaceId);
	public List<String> getProductsByRepairArea(String repairAreaName);
	public QiRepairAreaSpace getNaqParkingSpace(String repairAreaName);
	public void updateNAQParking(long defectResultId, String productId, String repairAreaName, int repairAreaSpace,
			int repairAreaRow, String string);
	public void clearNAQParking(String productId); 
}
