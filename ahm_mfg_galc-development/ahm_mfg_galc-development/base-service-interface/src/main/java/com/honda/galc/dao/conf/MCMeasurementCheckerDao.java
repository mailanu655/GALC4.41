package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public interface MCMeasurementCheckerDao extends IDaoService<MCMeasurementChecker, MCMeasurementCheckerId> {
	
	public List<MCMeasurementChecker> findAllByOperation(String operationName, String partId, int opRevision, String checkPoint);
	public List<MCMeasurementChecker> findAllBy(String operationName, String partId, int opRevision);
	public List<MCMeasurementChecker> findAllBy(String operationName, int opRevision);
	
	public List<MeasurementCheckerDto> findAllByOperationNamePartNoAndChecker(String operationName, String basePartNo, String checker, String divisionId);
	
	public List<MCMeasurementChecker> findAllBy(MeasurementCheckerDto measCheckerDto, List<Integer> opRevList);
	
	public List<String> loadAllMeasurementCheckerOpName(String operationName);
	
	public List<MeasurementCheckerDto> loadMeasurementCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode);
	
	public List<MCMeasurementChecker> findAllOpRevBy(String operationName, String partId, String checkPoint, int checkSeq, int measurementSeqNumber, String checkName);

	public List<MCMeasurementChecker> findAllCheckerBy(MeasurementCheckerDto measCheckerDto);
	
	public void removeAndInsertAll(List<MCMeasurementChecker> insertMeasCheckersList, List<MCMeasurementChecker> removeMeasCheckersList);
	
	public List<MCMeasurementChecker> findAllBy(String operationName, String partId, int opRevision, int measurementSeqNumber);
	
	public void saveEntity(MCViosMasterOperationMeasurementChecker mcViosmOpsMeasChecker);
	
	public void deleteAndInsertAllMeasurementCheckerRevision(String viosPlatformId, String unitNo,int measSeq, List<MCViosMasterOperationMeasurementChecker> meansCheckerList);
	public void deleteAllRevOperation(String viosPlatform, String unitNo);
	public void deleteAllMeasurementRevision(String viosPlatformId, String unitNo,int measSeq);
}
