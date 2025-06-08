package com.honda.galc.client.loader;

import java.util.List;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.service.MfgDataLoaderService;
import com.honda.galc.service.ServiceFactory;

public class MfgDataLoaderMeasurementModel extends AbstractModel {

	public MfgDataLoaderMeasurementModel() {
		super();
	}
	
	public List<McOperationDataDto> findAllByOperationName(String operationName) {
		return ServiceFactory.getDao(MCOperationPartRevisionDao.class).findAllActiveByOperationName(operationName);
	}
	
	public List<McOperationDataDto> findAllApprovedRevision(){
		return ServiceFactory.getDao(MCOperationRevisionDao.class).findAllApprovedRevision();
	}
	
	public void addMeasurement(String opName, String partId, int partRev, int qty,
			String minLimit, String maxLimit, int maxAttempts, String pset, String tool) {
		ServiceFactory.getService(MfgDataLoaderService.class).addMeasurement(opName, partId, partRev, qty, minLimit, maxLimit, maxAttempts, pset, tool);
	}
	
	
	@Override
	public void reset() {
		
	}
}
