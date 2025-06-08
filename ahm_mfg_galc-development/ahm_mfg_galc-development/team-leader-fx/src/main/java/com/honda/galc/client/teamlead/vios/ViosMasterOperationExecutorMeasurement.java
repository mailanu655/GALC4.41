package com.honda.galc.client.teamlead.vios;


import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterOperationExecutorMeasurement implements ViosMasterExecutor<MCViosMasterOperationMeasurement> {


	public static int MAX_ATTEMPTS = 3;
	
	@Override
	public String doValidation(MCViosMasterOperationMeasurement entity) {
		return ViosMasterValidator.masterOperationMeasValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterOperationMeasurement entity, String viosPlatform, String userId)
			throws Exception {
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		entity.setMaxAttempts(MAX_ATTEMPTS);
		ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterMeas(entity);
	}

}
