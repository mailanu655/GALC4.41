package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.constant.PartType;
import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCOperationPartRevisionDao 
	extends IDaoService<MCOperationPartRevision, MCOperationPartRevisionId>{
	
	public List<MCOperationPartRevision> findAllPartForOperationAndOpRevision(String operationName, int revision);
	
	public List<MCOperationPartRevision> findAllPartsForOperationAndProcessPoint(String ProcessPoint, String operationName, Integer revisionId);
	
	public List<MCOperationPartRevision> findAllPartsForOperation(String operationName);
	
	public int getMaxRevisionForOperationPart(String operationPart);
	
	public List<MCOperationPartRevision> findAllByRevision(long revisionId);
	
	public List<MCOperationPartRevision> findAllExceptPartRevision(String operationName, String partId, int partRev);
	
	public List<MCOperationPartRevision> findAllBy(String operationName, long revisionId);
	
	public int  getMaxAprvdRevisionForOperationPart(String operationPart, String partId);
	
	public List<MCOperationPartRevision> findAllActiveBy(String operationName, long revisionId);
	
	public List<MCOperationPartRevision> findLatestApvdMfgPartsBy(String operationName, long revisionId);
	
	public List<MCOperationPartRevision> findAllParts(String productSpecCode, long structureRevision, String processPointId, boolean isEffectivePartQuery);
	
	public List<MCOperationPartRevision> findAllParts(String productSpecCode, long structureRevision, String processPointId);
	
	public String findMaxPartId(String operationName, int partRev);
	
	public String findMinPartId(String operationName, int partRev);
	
	public boolean getMFGPartType(String operationName, String partId);
	
	public List<MCOperationPartRevision> findAllPartsPP(String productSpecCode, long structureRevision, String[] processPointId);
	
	public List<String> findAllByOperationName(String operationName);
	
	public List<PartDetailsDto> findAllByOperationPartAndPartType(String operationName, String partNo, String partType);
	
	public List<PartDetailsDto> findAllByPartByOpPartTypeAndMeasurement(String operationName, String partNo, String partType);
	
	public List<String> findAllPartNoByOperationName(String partNo, String operationName);
	
	public MCOperationPartRevision findAllByPartNoSecCodeItemNoAndType(String opName, String partNo, String partItemNo, String partSectionCode, PartType partType);
	
	public String getMaxPartId(String operationName);
	
	public List<MCOperationPartRevision> findAllActivePartsByOperationName(String operationName);
	
	public List<McOperationDataDto> findAllActiveByOperationName(String operationName);
	
	public MCOperationPartRevision findApprovedByPartNoAndType(String opName, String partNo,  String partType);

	public List<MCOperationPartRevision> findAllActivePartsByOperationNameForMFG(String operationName);

	public List<MCOperationPartRevision> findAllApprovedByPartNoAndType(String operationName, String partNo, String partType);
}
