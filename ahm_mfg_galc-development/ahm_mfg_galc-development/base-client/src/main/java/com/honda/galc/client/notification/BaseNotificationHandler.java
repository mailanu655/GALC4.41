/**
 * 
 */
package com.honda.galc.client.notification;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.common.logging.Logger;

/**
 * @author Alex Johnson
 * @date Jul 15, 2015
 */
public abstract class BaseNotificationHandler {
	
	private ApplicationContext appContext = null;
	
	public BaseNotificationHandler() {
		try {
			this.appContext = ClientMain.getInstance().getApplicationContext();
			Logger.getLogger().info("Instantiating handler " + this.getClass().getSimpleName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

}
