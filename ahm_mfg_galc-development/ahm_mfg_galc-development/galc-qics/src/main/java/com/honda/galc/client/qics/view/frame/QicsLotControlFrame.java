package com.honda.galc.client.qics.view.frame;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.IObserver;
import com.honda.galc.client.common.component.EnhancedStatusMessagePanel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.datacollection.view.DataCollectionPanelBase;
import com.honda.galc.client.datacollection.view.IViewManager;
import com.honda.galc.client.datacollection.view.LotControlMain;
import com.honda.galc.client.qics.device.QicsDeviceDataDispatcher;
import com.honda.galc.client.qics.device.QicsDeviceManager;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QicsLotControlMain</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsLotControlMain is the client to combine Qics and Lot Control functions </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 15, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 15, 2011
 */

public class QicsLotControlFrame extends QicsFrame{
	private static final long serialVersionUID = 1L;
	protected ClientContext clientContext;
	
	protected IViewObserver viewManager;
	protected ViewManagerPropertyBean viewProperty;
	
	public QicsLotControlFrame(ApplicationContext appContext,
			Application application) {
		super(appContext, application);
		
	}

	public void initialize() {
		
		viewProperty = getClientContext().getProperty();
		DataCollectionController.getInstance(getApplication().getApplicationId()).setClientContext(clientContext);		
		viewManager = createViewManager();
		
		EventBus.publish(new ProgressEvent(70, "Setting Frame Properties ..."));
		super.initialize();
		
		EventBus.publish(new ProgressEvent(80, "Registering processors ..."));
		DataCollectionController.getInstance().registerProcessors();
	
		EventBus.publish(new ProgressEvent(90, "Adding Observers ..."));
		deviceDataDispatcher = new QicsDeviceDataDispatcher(this);
		DataCollectionController.getInstance().addObservers(viewManager);

		DataCollectionController.getInstance().init();
		DataCollectionController.getInstance().setClientStarted(true);
		
	}
	
	@Override
	protected void initDeviceManager() {
		if(isPlcSupported()){
			deviceManager = new QicsDeviceManager(this);
		}
	}

	private IViewObserver createViewManager() {
		String viewManagerClass = getViewManagerClass();
		
		if(StringUtils.isEmpty(viewManagerClass)){
			Logger.getLogger().error("Mandatory View Manager class is not configured!");
			//We are not return here, so 
			//let exception throw later, view manager is mandatory for lot control client
		}
		
		IObserver observer = clientContext.createObserver(viewManagerClass);
		disableProductId(((IViewManager)observer).getView()); 
		
		return (IViewObserver)observer;
	}

	private void disableProductId(DataCollectionPanelBase view) {
		view.getTextFieldProdId().setEnabled(false);
		view.getTextFieldProdId().setVisible(false);
		view.getLabelProdId().setEnabled(false);
		view.getLabelProdId().setVisible(false);
		
	}

	private String getViewManagerClass() {

		try {
			String viewManagerClass = PropertyService.getProperty(clientContext.getProcessPointId(), LotControlMain.viewManagerKey);

			if (viewManagerClass == null) {
				viewManagerClass = viewProperty.getViewManager();
			}

			return viewManagerClass;

		} catch (Exception e) {
			Logger.getLogger().info(e, "Exception to get View Manager class.");
		}

		return null;
	}
	
	private ClientContext getClientContext() {
		if(clientContext == null)
			clientContext = new ClientContext(applicationContext, this);

		return clientContext;
	}
	
	protected void handleException(Throwable exception) {
		Logger.getLogger().error(exception, this.getClass().getSimpleName());
	}
	
	public IViewObserver getViewManager() {
		return viewManager;
	}

	@Override
	protected JPanel initStatusMessagePanel() {
		enhancedMessagePanel = new EnhancedStatusMessagePanel(getClientContext());
		statusMessagePanel = enhancedMessagePanel;
		return statusMessagePanel;
	}
	
	public void logon() {
		logon(true);
	}
	
}
