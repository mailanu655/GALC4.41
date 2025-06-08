package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMatrixId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCOperationMatrixDao 
	extends IDaoService<MCOperationMatrix, MCOperationMatrixId>{

	public List<MCOperationMatrix> findAllMatrixForOperationAndRevId(String operationName, Integer opRevId);
	
	public List<MCOperationMatrix> findAllMatrixForOperationAndProcessPoint(String ProcessPoint, String operationName, Integer revisionId);
	
	public List<MCOperationMatrix> findAllBy(
			String operationName, int opRevId, int pddaPltformId);

	public int deleteExcludedOperations(String operationName, Integer opRevId, Integer pddaPltformId);
	
	public List<MCOperationMatrix> findAllMatrixByPDDAPlatformId(int pddaPltformId);
	
	public List<MCOperationMatrix> findAllMatrixByPDDAPlatformIdAndSpecCodeMask(int pddaPltformId, String specCodeMask);
	
	public List<McOperationDataDto> findAllByDeptCodeAndAsmProc(String deptCode, String asmProcNumber, String model_code);
	
	public int getMatrixCountBy(String operationName, int opRev, int pddaPltformId, String specCodeType, String mbpnMask);
	
	public List<McOperationDataDto> findAllByPlatformAndAsmProc(String platform, String asmProcNumber, String model_code);

	public void deleteMBPNOpMatrixData(MCViosMasterMBPNMatrixData mcmbpnMasterData, String viosPlatformId, boolean isUpload);
}
