package com.honda.galc.client.teamlead.vios;

import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterOperationExecutorChecker implements ViosMasterExecutor<MCViosMasterOperationChecker> {

	@Override
	public String doValidation(MCViosMasterOperationChecker entity) {
		return ViosMasterValidator.masterOperationCheckerValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterOperationChecker entity, String viosPlatform, String userId) throws Exception {
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterOperationChecker(entity);
	}

}
