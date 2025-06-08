package com.honda.galc.util;

import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.IService;
import com.honda.galc.service.property.PropertyService;

public class GALCHttpServiceProvider {
	
	private static String getGALCUrl() {
		return PropertyService.getProperty("APP_INFO", "GALC_URL");
	}
	
    public static <T extends IService> T getService(Class<T> service) {
    	return HttpServiceProvider.getService(getGALCUrl(),service);
    }

}
