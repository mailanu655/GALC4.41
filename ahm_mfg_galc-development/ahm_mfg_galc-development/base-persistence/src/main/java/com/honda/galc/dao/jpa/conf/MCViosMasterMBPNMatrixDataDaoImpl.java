package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixDataId;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMatrixId;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrixId;
import com.honda.galc.service.MfgDataLoaderService;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

public class MCViosMasterMBPNMatrixDataDaoImpl extends BaseDaoImpl<MCViosMasterMBPNMatrixData, MCViosMasterMBPNMatrixDataId> implements MCViosMasterMBPNMatrixDataDao{

	private static final String FIND_ALL_FILTERED_OPERATIONS = "SELECT * FROM galadm.MC_VIOS_MASTER_MBPN_MATRIX_TBX op "
			+ "WHERE op.VIOS_PLATFORM_ID = ?1 AND (UPPER(op.ASM_PROC_NO) LIKE ?2 OR UPPER(op.MBPN_MASK) LIKE ?2 OR UPPER(op.MTC_TYPE) LIKE ?2 OR UPPER(op.MTC_MODEL) LIKE ?2) ORDER BY op.ASM_PROC_NO";
	
	public List<MCViosMasterMBPNMatrixData> getAllData(String platformId, String filter) {
		Parameters params = Parameters.with("1", platformId).put("2", "%"+filter+"%");
		return findAllByNativeQuery(FIND_ALL_FILTERED_OPERATIONS, params);
	}
	
	public void deleteByPlatform(String viosPlatformId) {
		Parameters params = Parameters.with("id.viosPlatformId", viosPlatformId);
		delete(params);
	}
	
	public List<MCViosMasterMBPNMatrixData> findAllData(String viosPlatformId, String filter) {
		Parameters params = Parameters.with("1", viosPlatformId).put("2", "%"+filter+"%");
		
		List<MCViosMasterMBPNMatrixData> mbpnMasterDataList = findAllByNativeQuery(FIND_ALL_FILTERED_OPERATIONS, params);
		
		Map<String, MCViosMasterMBPNMatrixData> dataMap = new HashMap<String, MCViosMasterMBPNMatrixData>();
		for (MCViosMasterMBPNMatrixData mcmbpnMasterData : mbpnMasterDataList) {
			MCViosMasterMBPNMatrixDataId id =  mcmbpnMasterData.getId();
			String keyValue = id.getAsmProcNo()+"_"+id.getMbpnMask()+"_"+id.getMtcModel();
			if(dataMap.containsKey(keyValue)) {
				MCViosMasterMBPNMatrixData mbpnMaster  = dataMap.get(keyValue);
				String mtcTypes = mbpnMaster.getId().getMtcType().concat(","+mcmbpnMasterData.getId().getMtcType());
				mbpnMaster.getId().setMtcType(mtcTypes);
				dataMap.put(keyValue, mbpnMaster);
			} else {
				dataMap.put(keyValue, mcmbpnMasterData);
			}
		}
		
		List<MCViosMasterMBPNMatrixData> mcpnMasterDataList = new ArrayList<MCViosMasterMBPNMatrixData>();
		for (String keyValue : dataMap.keySet()) {
			mcpnMasterDataList.add(dataMap.get(keyValue));
		}
		
		return mcpnMasterDataList;
	}
	
	@Transactional
	public void updateMBPNMasterRevision(MCViosMasterMBPNMatrixData entity, String viosPlatform){
		//Creating model type to MBPN mapping
		Map<String, List<String>> modelTypeMbpnMap = new HashMap<String, List<String>>();
		List<String> mbpnMaskList = new ArrayList<String>();
		//Creating Model type - MBPN mask
		mbpnMaskList.add(entity.getId().getMbpnMask());
		String[] modelTypeArray = entity.getId().getMtcType().split(",");
		for(String modelType : modelTypeArray) {
			modelType =StringUtils.trimToEmpty(modelType);
			if(StringUtils.isNotEmpty(modelType)) {
				modelTypeMbpnMap.put(modelType, mbpnMaskList);
			}
		}
		Set<MCOperationMatrix> opMatrixSet = new LinkedHashSet<MCOperationMatrix>();
		List<McOperationDataDto> operationMatrixs = ServiceFactory.getDao(MCOperationMatrixDao.class).findAllByPlatformAndAsmProc(viosPlatform, entity.getId().getAsmProcNo(), entity.getId().getMtcModel());
		for (McOperationDataDto opMatrix : operationMatrixs) {
			String opName = StringUtils.isNotBlank(opMatrix.getOperationName()) ? opMatrix.getOperationName().trim() : "";
			int opRev = opMatrix.getOperationRevision();
			int pddaPltfrmId = opMatrix.getPddaPlatformId();
			String specCodeType = opMatrix.getSpecCodeType();
			specCodeType = StringUtils.isNotBlank(specCodeType)?specCodeType.trim():"";
			String specCodeMask = opMatrix.getSpecCodeMask();
			specCodeMask = StringUtils.isNotBlank(specCodeMask)?specCodeMask.trim():"";
			
			String modelType = StringUtils.substring(specCodeMask, 4, 7);
			List<String> mbpnMasks = modelTypeMbpnMap.get(modelType);
			if(mbpnMasks!=null && !mbpnMasks.isEmpty()) {
				for(String mbpnMask : mbpnMasks) {
					mbpnMask = StringUtils.trimToEmpty(mbpnMask);
						opMatrixSet.add(convertToOperationMatrixEntity(opName, opRev, pddaPltfrmId, specCodeType, mbpnMask));
					
				}
			}
		}
		
		Set<MCOperationPartMatrix> partMatrixSet = new LinkedHashSet<MCOperationPartMatrix>();
		List<McOperationDataDto> partMatrixs = ServiceFactory.getDao(MCOperationPartMatrixDao.class).findAllPartMatrixByPlatformAndAsmProc(viosPlatform, entity.getId().getAsmProcNo(), entity.getId().getMtcModel());
		
		for (McOperationDataDto partMatrix : partMatrixs) {
			String opName = partMatrix.getOperationName();
			opName = StringUtils.isNotBlank(opName)?opName.trim():"";
			String partId = partMatrix.getPartId();
			partId = StringUtils.isNotBlank(partId)?partId.trim():"";
			int partRev = partMatrix.getPartRevision();
			String specCodeType = partMatrix.getSpecCodeType();
			specCodeType = StringUtils.isNotBlank(specCodeType)?specCodeType.trim():"";
			String specCodeMask = partMatrix.getSpecCodeMask();
			specCodeMask = StringUtils.isNotBlank(specCodeMask)?specCodeMask.trim():"";
			
			String modelType = StringUtils.substring(specCodeMask, 4, 7);
			List<String> mbpnMasks = modelTypeMbpnMap.get(modelType);
			if(mbpnMasks!=null && !mbpnMasks.isEmpty()) {
				for(String mbpnMask : mbpnMasks) {
					mbpnMask = StringUtils.trimToEmpty(mbpnMask);
						partMatrixSet.add(convertToPartMatrixEntity(opName, partId, partRev, specCodeType, mbpnMask));
				}
			}
		}
		ServiceFactory.getService(MfgDataLoaderService.class).saveMatrix(opMatrixSet, partMatrixSet);
	}
	
	
	private MCOperationMatrix convertToOperationMatrixEntity(String operationName, int opRev, int pddaPltformId, String specCodeType, String mbpnMask) {
		MCOperationMatrix matrix = new MCOperationMatrix();
		MCOperationMatrixId id = new MCOperationMatrixId();
		id.setOperationName(operationName);
		id.setOperationRevision(opRev);
		id.setPddaPlatformId(pddaPltformId);
		id.setSpecCodeMask(mbpnMask);
		id.setSpecCodeType(specCodeType);
		matrix.setId(id);
		return matrix;
	}
	
	private MCOperationPartMatrix convertToPartMatrixEntity(String operationName, String partId, int partRev, String specCodeType, String mbpnMask) {
		MCOperationPartMatrix matrix = new MCOperationPartMatrix();
		MCOperationPartMatrixId id = new MCOperationPartMatrixId();
		id.setOperationName(operationName);
		id.setPartId(partId);
		id.setPartRevision(partRev);
		id.setSpecCodeMask(mbpnMask);
		id.setSpecCodeType(specCodeType);
		matrix.setId(id);
		
		return matrix;
	}
	
	
	public void deleteMBPNMaster(MCViosMasterMBPNMatrixData mcmbpnMasterData, String viosPlatformId) {
		Parameters params = Parameters.with("id.viosPlatformId", viosPlatformId);
		params.put("id.asmProcNo", mcmbpnMasterData.getId().getAsmProcNo());
		params.put("id.mbpnMask", mcmbpnMasterData.getId().getMbpnMask());
		params.put("id.mtcModel", mcmbpnMasterData.getId().getMtcModel());
		delete(params);
	}
	
	public List<MCViosMasterMBPNMatrixData> findbyASMProcAndMBPNMask(String viosPlatformId, String mbpnMask, String mbpnProcessNumber) {
		Parameters params = Parameters.with("id.viosPlatformId", viosPlatformId);
		params.put("id.asmProcNo", mbpnProcessNumber);
		params.put("id.mbpnMask", mbpnMask);
		 
		return findAll(params);
	}
}
