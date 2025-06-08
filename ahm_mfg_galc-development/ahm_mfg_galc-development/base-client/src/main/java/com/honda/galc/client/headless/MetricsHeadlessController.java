package com.honda.galc.client.headless;

import static com.honda.galc.common.logging.Logger.getLogger;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.metrics.monitors.MetricsScheduleManager;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.entity.conf.Application;

/**
 * @author Subu Kathiresan
 * Aug 15, 2011
 * @author Todd Roling
 */
public class MetricsHeadlessController implements IHeadlessMain {
	public MetricsHeadlessController() throws SystemException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	}

	public void initialize(ApplicationContext appContext, Application application) {
		initialize(appContext);
	}
	
	public void initialize(ApplicationContext appContext) {		
		initializeMetricsMonitors();
		startPeerCommunicationChannel();
	}
	
	private void startPeerCommunicationChannel() {
		getLogger().info("starting peer communication channel");
	}
	
	private void initializeMetricsMonitors() {
		getLogger().info("Initializing metric monitors");
		
		try {
			MetricsScheduleManager.getInstance().startManager();
		}
		catch(Exception e) {
			getLogger().error("Unable to start Metrics Schedule Manager");
		}
	}
}
