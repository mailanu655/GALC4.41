package com.honda.galc.client.product.entry;

import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class GenericProductInputPaneModel {

	ApplicationPropertyBean applicationPropertyBean;
	String applicationId;

	public GenericProductInputPaneModel(String applicationId) {
		this.applicationId = applicationId;
	}

	public String[] getPanels() {
		return getApplicationPropertyBean().getSearchPanels();
	}
	
	ApplicationPropertyBean getApplicationPropertyBean() {
		if(applicationPropertyBean == null) {
			applicationPropertyBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, applicationId);
		}
		return applicationPropertyBean;
	}
}
