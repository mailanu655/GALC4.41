package com.honda.galc.client.qi.base;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.ProductClientFrame;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.property.DefaultQiProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class QiProductClientFrame extends ProductClientFrame {

	public QiProductClientFrame(ApplicationContext appContext,Application application) {
		super(appContext, application, 
			  PropertyService.getPropertyBean(DefaultQiProductPropertyBean.class, appContext.getProcessPointId()));
	}
	
}
