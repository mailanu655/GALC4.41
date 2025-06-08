package com.honda.galc.dao.pdda;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.TrainingDataDto;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.dto.UnitOfOperationDetails;
import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitDao extends IDaoService<Unit, Integer> {
	
	public List<Object[]> findAllSftyErgoForProcessPoint(String processPoint);
	public int getLineSpeed(String productId ,String processPoint);
	public float getUnitTotTime(int maintenanceId);
	public UnitOfOperationDetails getUnitOfOperationDetails(int maintenanceId,String ProcessPointID,String ProductID);
	public List<UnitOfOperation> getAllOperationsForProcessPoint(String productId ,String processPoint, String mode);
	public List<UnitOfOperation> getUnitOfOperationDetails(TrainingDataDto trainingDataDto);
	
	public Unit findByUnitNoAndPlatform(String unitNo, String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode);
	
	public String findBasePartNoByUnit(String unitNo, String viosPlatformId);
			
	
}
