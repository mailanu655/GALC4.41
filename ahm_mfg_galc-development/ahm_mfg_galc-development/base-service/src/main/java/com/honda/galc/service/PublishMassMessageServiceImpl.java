package com.honda.galc.service;

import com.honda.galc.notification.service.IMassMessageNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.PublishMassMessageService;


public class PublishMassMessageServiceImpl implements PublishMassMessageService{

	@Override
	public void notifyClients(String plantName, String divisionId, String lineId) {

		ServiceFactory.getNotificationService(IMassMessageNotification.class).execute(plantName, divisionId, lineId);
		
	}

}
