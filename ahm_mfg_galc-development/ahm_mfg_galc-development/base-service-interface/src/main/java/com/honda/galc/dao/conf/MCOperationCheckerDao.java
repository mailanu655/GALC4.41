package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationCheckerId;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public interface MCOperationCheckerDao extends IDaoService<MCOperationChecker, MCOperationCheckerId> {
	public List<MCOperationChecker> findAllBy(String operationName, int operationRevision);
	
	public List<OperationCheckerDto> findAllByOperationNameAndChecker(String opName, String checker, String divisionId);
	
	public List<MCOperationChecker> findAllBy(String operationName, String checkPoint, int checkSeq, List<Integer> opRevList);
	
	public List<MCOperationChecker> loadOperationCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode);
	
	public List<String> findByOperationName(String opName);
	
	public void deleteCheckerByOpNameCheckPtAndSeq(String operationName, String checkPoint, int checkSeq);
	
	public void removeAndInsertAll(List<MCOperationChecker> insertOpCheckerList, List<MCOperationChecker> removeOpCheckerList);
	
	public void saveEntity(MCViosMasterOperationChecker masterOpChecker);

	public void deleteAndInsertAllOperatioRevCheckers(String viosPlatformId, String unitNo, List<MCViosMasterOperationChecker> masterOperationCheckerList);

	public void deleteAllRevOperation(String viosPlatform, String unitNo);
}
