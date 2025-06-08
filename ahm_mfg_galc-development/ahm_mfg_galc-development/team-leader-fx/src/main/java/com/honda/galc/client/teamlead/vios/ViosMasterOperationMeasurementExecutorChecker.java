package com.honda.galc.client.teamlead.vios;

import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementDao;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterOperationMeasurementExecutorChecker implements ViosMasterExecutor<MCViosMasterOperationMeasurementChecker> {

	@Override
	public String doValidation(MCViosMasterOperationMeasurementChecker entity) {
		return ViosMasterValidator.uploadMasterOperationMeasurementCheckerValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterOperationMeasurementChecker entity, String viosPlatform, String userId)
			throws Exception {
		
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		int meansSeqNumber = ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class).getFirstMeasurementSequenceNumber(viosPlatform, entity.getId().getUnitNo());
		entity.getId().setMeasurementSeqNum(meansSeqNumber); 
		ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterMeasChecker(entity);
		
	}

}
