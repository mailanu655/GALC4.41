package com.honda.galc.client.teamlead.vios;

import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterExecutorOperation  implements ViosMasterExecutor<MCViosMasterOperation> {


	@Override
	public String doValidation(MCViosMasterOperation entity) {
		return ViosMasterValidator.masterOperationValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterOperation entity, String viosPlatform, String userId) throws Exception {
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterOperation(entity);
		
	}

}
