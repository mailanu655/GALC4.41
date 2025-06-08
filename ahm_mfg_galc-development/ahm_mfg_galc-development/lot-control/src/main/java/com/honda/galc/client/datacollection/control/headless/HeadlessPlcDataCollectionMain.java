package com.honda.galc.client.datacollection.control.headless;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.headless.PlcDataCollectionController;
import com.honda.galc.entity.conf.Application;

/**
 * @author Subu Kathiresan
 * @date Nov 1, 2012
 *
 */
public class HeadlessPlcDataCollectionMain extends HeadlessLotControlMain {
	
	public HeadlessPlcDataCollectionMain() {
		super();
	}
	
	@Override
	public void initialize(ApplicationContext appContext, Application application) {
		super.initialize(appContext, application);
		
		getPlcDcController().setApplicationContext(appContext);
		createDataReadyMonitors(StringUtils.trimToEmpty(application.getApplicationId()));
	}

	private void createDataReadyMonitors(String applicationId) {
		PlcDataReadyMonitorFactory drFactory = new PlcDataReadyMonitorFactory(applicationId);
		drFactory.createMonitors(getPlcDcController());
	}
	
	private PlcDataCollectionController getPlcDcController() {
		return PlcDataCollectionController.getInstance(getApplicationId());
	}
}
