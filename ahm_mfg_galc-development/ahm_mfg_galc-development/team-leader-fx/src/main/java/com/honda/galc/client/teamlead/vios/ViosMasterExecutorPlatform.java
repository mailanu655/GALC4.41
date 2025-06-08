package com.honda.galc.client.teamlead.vios;

import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterExecutorPlatform  implements ViosMasterExecutor<MCViosMasterPlatform> {

	@Override
	public String doValidation(MCViosMasterPlatform entity) {
		return ViosMasterValidator.masterPlatformValidate(entity);
	
	}

	@Override
	public void uploadEntity(MCViosMasterPlatform entity, String viosPlatform, String userId) throws Exception {
		entity.setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterPlatform(entity);
		
	}
	
}
