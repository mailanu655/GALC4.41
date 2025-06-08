package com.honda.galc.client.teamlead.vios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixDataId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterMBPNMatrixExcecutor implements ViosMasterExecutor<MCViosMasterMBPNMatrixData> {

	@Override
	public String doValidation(MCViosMasterMBPNMatrixData entity) {
		return ViosMasterValidator.MBPNMasterDataValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterMBPNMatrixData entity, String viosPlatform, String userId) throws Exception {

		List<String> mbpnMaskList = new ArrayList<String>();
		//Creating Model type - MBPN mask
		mbpnMaskList.add(entity.getId().getMbpnMask());
		String[] modelTypeArray = entity.getId().getMtcType().split(",");
		if(entity.getId().getMtcType().contains(",")) {
			for(String modelType : modelTypeArray) {
				MCViosMasterMBPNMatrixData masterData = new MCViosMasterMBPNMatrixData();
				masterData.setUserId(userId);
	        	MCViosMasterMBPNMatrixDataId masterDataId =  new MCViosMasterMBPNMatrixDataId(entity.getId().getAsmProcNo(),
	        			entity.getId().getMbpnMask(), StringUtils.trimToEmpty(modelType), entity.getId().getMtcModel());
	        	masterDataId.setViosPlatformId(viosPlatform);
	        	masterData.setId(masterDataId);
				ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).insert(masterData);
			}
		} else {
			entity.getId().setViosPlatformId(viosPlatform);
			entity.setUserId(userId);
			ServiceFactory.getDao(MCViosMasterMBPNMatrixDataDao.class).insert(entity);
		}
		ServiceFactory.getService(ViosMaintenanceService.class).updateMBPNMatrixData(entity, viosPlatform);
		
	}
	

	

}

