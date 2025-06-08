package com.honda.galc.service.vios;

import com.honda.galc.service.IService;

public interface PublishMassMessageService extends IService {

	public void notifyClients(String plantName, String divisionId, String lineId);
	
}
