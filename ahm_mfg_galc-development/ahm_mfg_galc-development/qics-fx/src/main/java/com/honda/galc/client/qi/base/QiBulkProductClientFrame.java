package com.honda.galc.client.qi.base;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.BulkProductClientFrame;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.property.DefaultQiProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class QiBulkProductClientFrame extends  BulkProductClientFrame {

	public QiBulkProductClientFrame(ApplicationContext appContext,Application application) {
		super(appContext, application, 
				PropertyService.getPropertyBean(DefaultQiProductPropertyBean.class, appContext.getProcessPointId()));
	}

}
