package com.honda.galc.client.teamlead.vios;


import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterOperationPartExecutorChecker implements ViosMasterExecutor<MCViosMasterOperationPartChecker> {

	@Override
	public String doValidation(MCViosMasterOperationPartChecker entity) {
		return ViosMasterValidator.uploadMasterPartCheckerValidate(entity);
	}
	

	@Override
	public void uploadEntity(MCViosMasterOperationPartChecker entity, String viosPlatform, String userId)
			throws Exception {
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		 ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterPartChecker(entity);
		
	}

}
