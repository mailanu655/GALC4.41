package com.honda.galc.service.vios;

import java.util.List;
import java.util.Set;

import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationId;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementId;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPartId;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.UnitPartDto;
import com.honda.galc.service.IService;

public interface ViosMaintenanceService extends IService {

	public void deleteViosUnitNoConfig(MCViosMasterOperationId masterOpId);

	public void deleteViosPartConfig(MCViosMasterOperationPartId masterOpPartId);

	public void deleteViosMeasConfig(MCViosMasterOperationMeasurementId masterOpMeasId);

	public void updateOpPartRevisions(MCViosMasterOperationPart obj);

	public void uploadViosMasterPart(MCViosMasterOperationPart part);

	public void uploadViosMasterPartChecker(MCViosMasterOperationPartChecker partChecker);

	public void uploadViosMasterOperation(MCViosMasterOperation opt);

	public void uploadViosMasterOperationChecker(MCViosMasterOperationChecker optChecker);

	public void uploadViosMasterMeas(MCViosMasterOperationMeasurement meas);
	
	public void updateViosMasterMeas(MCViosMasterOperationMeasurement meas);
	
	public void uploadViosMasterMeasChecker(MCViosMasterOperationMeasurementChecker measChecker);
	
	public void  deleteByProcessPointId (String processPointId, String viosPlatform);
	
	public void uploadViosMasterPlatform(MCViosMasterPlatform platdorm);
	
	public void deleteAndInsertAllMasterMeansChecker(List<MCViosMasterOperationMeasurementChecker> meansCheckerList, MCViosMasterOperationMeasurement opMeas);
	
	public void deleteAndInsertAllMasterPartChecker(List<MCViosMasterOperationPartChecker> partCheckerList, MCViosMasterOperationPart part);
	
	public void deleteAndInsertAllMasterOperationChecker(List<MCViosMasterOperationChecker> masterOperationCheckerList, MCViosMasterOperation masterOperation);

	public void deleteByOperation(String viosPlatform, Set<String> operationSet);

	public void deleteByPartNumber(String viosPlatform, Set<UnitPartDto> partCheckerSet);

	public void deleteAllMeasChecker(String viosPlatform, Set<String> operationSet); 
	public void deleteViosMeasConfigWithoutCheckers(MCViosMasterOperationMeasurementId masterOpMeasId);

	public int getAllUnitMeasurements(MCViosMasterOperationMeasurementId id);
	
	public void deleteViosPartConfigWithoutCheckers(MCViosMasterOperationPartId masterOpPartId);
	
	public int getAllPartCheckers(MCViosMasterOperationPartId masterOpPartId);
	
	public void deleteViosUnitNoConfigWithoutCheckers(MCViosMasterOperationId masterOpId);
	
	public int getAllUnitCheckers(MCViosMasterOperationId masterOpId);

	public void deleteByPlatform(String viosPlatform);

	public void deleteMBPNData(String viosPlatformId, MCViosMasterMBPNMatrixData mcmbpnMasterData);

	public void updateMBPNMatrixData(MCViosMasterMBPNMatrixData entity, String viosPlatform);
}
