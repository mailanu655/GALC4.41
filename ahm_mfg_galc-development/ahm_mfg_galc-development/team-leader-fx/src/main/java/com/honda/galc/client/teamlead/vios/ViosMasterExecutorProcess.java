package com.honda.galc.client.teamlead.vios;

import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.service.ServiceFactory;

public class ViosMasterExecutorProcess implements ViosMasterExecutor<MCViosMasterProcess> {

	@Override
	public String doValidation(MCViosMasterProcess entity) {
		return ViosMasterValidator.masterProcessValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterProcess entity, String viosPlatform, String userId) throws Exception {
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		ServiceFactory.getDao(MCViosMasterProcessDao.class).removeByKey(entity.getId());
		ServiceFactory.getDao(MCViosMasterProcessDao.class).insert(entity);
	}

}
