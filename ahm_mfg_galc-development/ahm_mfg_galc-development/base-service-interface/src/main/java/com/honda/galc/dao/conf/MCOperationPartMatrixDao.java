package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrixId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCOperationPartMatrixDao 
	extends IDaoService<MCOperationPartMatrix, MCOperationPartMatrixId>{
	
	public List<MCOperationPartMatrix> findAllSpecCodeForOperationPartIdAndPartRev(String operationName, String partId, int partRevision);
	
	public List<MCOperationPartMatrix> findAllMatrixByPDDAPlatformId(int pddaPltformId);
	
	public List<MCOperationPartMatrix> findAllMatrixByPDDAPlatformIdAndSpecCodeMask(int pddaPltformId, String specCodeMask);

	public void deletePartMatrix(String operationName, String partId, int partRevision, String specCodeMask);
	
	public List<MCOperationPartMatrix> findByOperationNamePartIdRevisionAndMask(String operationName, String mfgPartId, int mfgPartRevision, String refPartId, int refPartRevision);
	
	public List<McOperationDataDto> findAllByDeptCodeAndAsmProc(String deptCode, String asmProcNumber, String modelCode);
	
	public int getMatrixCountBy(String operationName, String partId, int partRev, String specCodeType, String mbpnMask);
	
	public List<McOperationDataDto> findAllPartMatrixByPlatformAndAsmProc (String viosPlatformId, String asmProcNumber, String modelCode);

	public void deleteMBPNPartMatrixData(MCViosMasterMBPNMatrixData mcmbpnMasterData, String viosPlatformId, boolean isUpload);
	
	public List<Object[]> findByChildPart(String operationName, String productSpecCode);
}
