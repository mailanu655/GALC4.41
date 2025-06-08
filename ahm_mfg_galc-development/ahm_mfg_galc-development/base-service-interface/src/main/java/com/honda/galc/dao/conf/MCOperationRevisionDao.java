package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCOperationRevisionDao extends IDaoService<MCOperationRevision, MCOperationRevisionId>{
	
	public List<MCOperationRevision> findAllByOperationName(String operationName);
	
	public List<MCOperationRevision> findAllByOperationRevision(int operationRevision);
	
	public List<MCOperationRevision> findAllByRevision(long revisionId);
	
	public List<MCOperationRevision> findAllByOperationNamesForProcessPoint(String processPointId);
	
	public List<MCOperationRevision> findAllExceptOperationRevision(String operationName, int opRev);
	
	public List<MCOperationRevision> findAllBy(int pddaPlatformId, long revisionId);
	
	public List<MCOperationRevision> findAllBy(int pddaPlatformId, long revisionId, String operationName);
	
	public List<MCOperationRevision> findAllActiveBy(int pddaPlatformId, long revisionId);
	
	public List<Object[]> getOperationSeq(String processPointId, String productSpecCode, long structureRevision);

	public int getMaxRevisionForOperation(String operation);
	
	public String getRevStatusForOperationRev(long revId);
	
	public int getMaxAprvdRevisionForOperation(String operation);
	
	public List<Object[]> findAllOperationViewAndProcessor();
	
	public List<MCOperationRevision> findAllBy(String operationName, long revisionId);
	
	public MCOperationRevision findApprovedOperationBy(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String operationName, int activeDuration);
	
	public List<String> findByOperationName(String opName);
	
	public List<Integer> findAllActiveByOperationName(String opName);
	
	public List<PartDetailsDto> loadOperationRevisionAndPartIdBy(String operationName, String partNo, String partSectionCode, 
			String partItemNo, String partType, String revisions, boolean active);
	
	public List<MCOperationRevision> findAllByOperationAndRevisions(String operationName, List<Integer> revisionIds, boolean active);
	
	public List<McOperationDataDto> findAllApprovedRevision();
	
	public List<MCOperationRevision> findAllByProcessPointAndPlatform(String processPointId, String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode);
	
	public MCOperationRevision findApprovedOperationBy(String operationName);
	
	public MCOperationRevision findApprovedByOpNameAndRevId(String operationName, long revId);
	
	public String findOperationNameByCommonName(String commonName);
	
	public List<String> findOperationsNameByPartNamelist(List<String> commonNameList);
	
	public List<MCOperationRevision> findOperationsNameByCommonNames(List<String> commonNameList);
	
	public String findOperationNameByCommonPartName(String commonName);
	
	public List<String> findOperationNamesByCommonPartName(String commonName);
	
	public void saveEntity(MCViosMasterOperation masterOp);
	
	public String getSelectOperationName(long structureRev, String selectProperty);
	
	public List<String> getAllListPickOperations(long structureRev, String selectProperty);
}
