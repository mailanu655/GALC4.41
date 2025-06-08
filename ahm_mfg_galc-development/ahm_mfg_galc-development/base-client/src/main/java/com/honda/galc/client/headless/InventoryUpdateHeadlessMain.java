package com.honda.galc.client.headless;

import static com.honda.galc.common.logging.Logger.getLogger;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.inventory.InventoryUpdateProcessor;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;

/**
 * @author Subu Kathiresan
 * @date Feb 9, 2012
 */
public class InventoryUpdateHeadlessMain implements IHeadlessMain, ConnectionStatusListener {
	
	public InventoryUpdateHeadlessMain() throws SystemException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	}
	
	public void initialize(ApplicationContext appContext, Application application) {
		try {
			startInventoryUpdateProcessor(appContext, application);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("IpuQaTesterHeadlessController could not be initialized.");
			getLogger().error(ex.getMessage());
		}
	}

	private void startInventoryUpdateProcessor(ApplicationContext appContext, Application application) {
		try {
			InventoryUpdateProcessor inventoryUpdateProcessor = new InventoryUpdateProcessor(appContext, application);
			inventoryUpdateProcessor.start();
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("IpuQaTesterResultsProcessor could not be started.");
			getLogger().error(ex.getMessage());
		}
	}

	public void statusChanged(ConnectionStatus status) {
		//TODO add implementation
	}
}

