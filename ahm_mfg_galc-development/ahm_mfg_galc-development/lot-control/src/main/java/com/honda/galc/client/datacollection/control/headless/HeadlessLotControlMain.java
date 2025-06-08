package com.honda.galc.client.datacollection.control.headless;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.DeviceDataDispatcher;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.headless.IHeadlessMain;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.notification.service.ILogLevelNotification;
import com.honda.galc.notification.service.IPropertyNotification;

/**
 * @author Subu Kathiresan
 * Apr 23, 2011
 */
public class HeadlessLotControlMain 
	implements IHeadlessMain, ILogLevelNotification, IPropertyNotification {

	protected ApplicationContext _applicationContext;
	protected Application _application;
	protected ClientContext _clientContext;
	protected DeviceDataDispatcher _deviceDataDispatcher;

	public HeadlessLotControlMain() {
		super();
	}
	
	public void initialize(ApplicationContext appContext, Application application) {		
		_applicationContext = appContext;
		_application = application;
		EventBus.publish(new ProgressEvent(70, "Registering processors ..."));
	
		getController().setClientContext(getClientContext());
		getController().registerProcessors();

		EventBus.publish(new ProgressEvent(90, "Adding Observers ..."));
		_deviceDataDispatcher = new DeviceDataDispatcher(getApplicationId());
	
		getController().addObservers();
		getController().setClientStarted(true);
		getController().init();
	}
	
	private DataCollectionController getController() {
		return DataCollectionController.getInstance(getApplication().getApplicationId().trim());
	}
	
	private ClientContext getClientContext() {
		if(_clientContext == null) {
			_clientContext = new ClientContext(_applicationContext);
		}
		return _clientContext;
	}
	
	public Application getApplication() {
		return _application;
	}
	
	public String getApplicationId() {
		return getApplication().getApplicationId().trim();
	}
	
	public ApplicationContext getApplicationContext() {
		return _applicationContext;
	}


	public void execute(String componentId, String level) {
	}

	public void execute(ComponentProperty componentProperty) {
	}
}
