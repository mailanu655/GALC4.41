
package com.honda.galc.util;

import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.IService;
import com.honda.galc.service.property.PropertyService;

public class GtsHttpServiceProvider {
	
	private static String getGALCUrl() {
		return PropertyService.getProperty("APP_INFO", "GTS_URL");
	}
	
    public static <T extends IService> T getService(Class<T> service) {
    	return HttpServiceProvider.getService(getGALCUrl(),service);
    }

}
