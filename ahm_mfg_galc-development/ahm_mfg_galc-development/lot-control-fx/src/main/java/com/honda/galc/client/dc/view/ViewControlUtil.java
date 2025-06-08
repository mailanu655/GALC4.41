package com.honda.galc.client.dc.view;

import java.net.MalformedURLException;
import java.net.URL;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;


public class ViewControlUtil {
	public static String getHostURL(String hosturl) {
		//String hosturl = getProcessor().getController().getModel().getProductModel().getApplicationContext().getArguments().getServerURL();
		URL aURL = null;
		try {
			aURL = new URL(hosturl);
		} catch (MalformedURLException e) {
			Logger.getLogger().error("ViewControlUtil::getHostURL():MalformedURLException:"+e);
			e.printStackTrace();
		}
		return aURL.getAuthority();
	}
	
	/**
	 * Cleaning up listeners
	 */
	public static void cleanUpOldListerners(Object listenerType) {
		if(null != listenerType) {
			for (Object obj: EventBusUtil.findListenersOfType(listenerType.getClass())) {
				if (obj != listenerType) {
					EventBusUtil.unregister(obj);
				}
			}
		}
	}
}
