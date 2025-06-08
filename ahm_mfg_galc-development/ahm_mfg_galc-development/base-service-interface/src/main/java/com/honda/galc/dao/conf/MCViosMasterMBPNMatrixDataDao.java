package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixDataId;
import com.honda.galc.service.IDaoService;

public interface MCViosMasterMBPNMatrixDataDao extends IDaoService<MCViosMasterMBPNMatrixData, MCViosMasterMBPNMatrixDataId>{

	public List<MCViosMasterMBPNMatrixData> getAllData(String platformId, String filter);

	public void deleteByPlatform(String viosPlatform);

	public List<MCViosMasterMBPNMatrixData> findAllData(String viosPlatformId, String filter);
	
	public void updateMBPNMasterRevision(MCViosMasterMBPNMatrixData entity, String viosPlatform);

	public void deleteMBPNMaster(MCViosMasterMBPNMatrixData mcmbpnMasterData, String viosPlatformId);

	public List<MCViosMasterMBPNMatrixData> findbyASMProcAndMBPNMask(String platformId, String mbpnMask, String mbpnProcessNumber);

}
