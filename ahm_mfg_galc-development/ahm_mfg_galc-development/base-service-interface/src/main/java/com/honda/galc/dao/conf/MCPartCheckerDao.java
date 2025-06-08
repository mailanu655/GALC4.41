package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public interface MCPartCheckerDao extends IDaoService<MCPartChecker, MCPartCheckerId> {
	
	public List<MCPartChecker> findAllByOperation(String operationName, String partId, int opRevision, String checkPoint);
	public List<MCPartChecker> findAllBy(String operationName, String partId, int opRevision);
	public List<MCPartChecker> findAllBy(String operationName, int opRevision);
	
	public List<PartCheckerDto> findAllByOperationNamePartNoAndChecker(String operationName, String basePartNo, String checker, String divisionId);
	
	public List<MCPartChecker> findAllOpRevBy(String operationName, String partId, String checkPoint, int checkSeq, List<Integer> opRevList);
	
	public List<String> loadAllPartCheckerOpName(String operationName);
	
	public List<PartCheckerDto> loadPartCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode);
	
	public List<MCPartChecker> findAllPartCheckerBy(PartCheckerDto partCheckerDto);
	
	public void removeAndInsertAll(List<MCPartChecker> insertPartCheckerList, List<MCPartChecker> removePartCheckerList);
	
	public void saveEntity(MCViosMasterOperationPartChecker mcViosMasterOpPartChecker);
	public void deleteAndInsertAllPartRevisionCheckers(String operationName,String unitNo,String partNo,  List<MCViosMasterOperationPartChecker> partCheckerList);
	public void deleteAllPartRev(String viosPlatform, String unitNo, String partNo);
	public List<MCPartChecker> findAllByDetails(String operationName, String checkPointName, int opRevision);
}
