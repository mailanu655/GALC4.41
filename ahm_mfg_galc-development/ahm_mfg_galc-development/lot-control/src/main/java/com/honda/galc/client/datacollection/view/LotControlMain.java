package com.honda.galc.client.datacollection.view;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.common.component.EnhancedStatusMessagePanel;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.info.LogInfoManager;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.net.Request;
import com.honda.galc.service.property.PropertyService;

 /** * *
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LotControlMain extends LotControlMainBase {

	private static final long serialVersionUID = 1L;
	public static final String viewManagerKey = "VIEW_MANAGER";

	public LotControlMain(ApplicationContext appContext,Application application) {
		super(appContext,application);

		initialize();
		AnnotationProcessor.process(this);

	}

	private void initialize() {
		//Start monitor logging info
		LogInfoManager.getInstance();
	}

	@Override
	protected void setFrameProperties() {
		try {
			setName(getApplicationContext().getApplicationId()==null?"LotControlMain":getApplicationContext().getApplicationId());
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setSize(1024, 768);
			setSize(viewProperty.getMainWindowWidth(), viewProperty.getMainWindowHeight());

			viewManager = createViewManager();
			clientContext.setViewManager(viewManager);
	        setClientPanel(viewManager.getClientPanel());
			initConnections();


		} catch (java.lang.Throwable t) {
			handleException(t);
		}
	}


	private IViewObserver createViewManager() {
		String viewManagerClass = getViewManagerClass();

		if(StringUtils.isEmpty(viewManagerClass)){
			Logger.getLogger().error("Mandatory View Manager class is not configured!");
			//We are not return here, so
			//let exception throw later, view manager is mandatory for lot control client
		}

		return (IViewObserver)clientContext.createObserver(viewManagerClass);
	}

	private String getViewManagerClass() {

		try {
			String viewManagerClass = PropertyService.getProperty(clientContext.getProcessPointId(), viewManagerKey);

			if (viewManagerClass == null) {
				viewManagerClass = viewProperty.getViewManager();
			}

			return viewManagerClass;

		} catch (Exception e) {
			Logger.getLogger().info(e, "Exception to get View Manager class.");
		}

		return null;
	}
	
	protected boolean isProcessPartTorqueDataCollectionState()
	{
		return (DataCollectionController.getInstance(getApplication().getApplicationId().trim()).getState() instanceof ProcessPart || DataCollectionController.getInstance(getApplicationContext().getApplicationId().trim()).getState() instanceof ProcessTorque);
	}

	protected void switchUser() {
		ClientMain.getInstance().switchUser(this);
		((ViewManagerBase) getViewManager()).getMessagePanel().getStatusPanel().setUser(applicationContext.getUserId());
	}

	//@Override
	protected JPanel initStatusMessagePanel() {
		statusMessagePanel = new EnhancedStatusMessagePanel(getClientContext());
		return statusMessagePanel;
	}
	
	@Override
	protected void enableDisableSystemMenuOptions()
	{
		getLogger().info("System menu item clicked");
		if(isProcessPartTorqueDataCollectionState())
		{
			getSwitchUserMenu().setEnabled(false);
			getSwitchModeMenu().setEnabled(false);
			getLogoutMenuItem().setEnabled(false);
		}else
		{
			getSwitchUserMenu().setEnabled(true);
			getSwitchModeMenu().setEnabled(true);
			getLogoutMenuItem().setEnabled(true);
			
		}
			
	}
}
