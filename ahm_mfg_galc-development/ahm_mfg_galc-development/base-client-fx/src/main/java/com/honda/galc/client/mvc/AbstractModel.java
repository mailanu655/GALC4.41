package com.honda.galc.client.mvc;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

public abstract class AbstractModel implements IModel{
	protected ApplicationContext applicationContext;
	
	public AbstractModel () {
	}
	
	public AbstractModel (ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public String getUserId(){
		return getApplicationContext().getUserId().toUpperCase();
	}
	
	public Logger getLogger() {
		return applicationContext.getLogger();
	}
	
	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}
}
