package com.honda.galc.client.linesidemonitor.view;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.component.EnhancedStatusMessagePanel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.DeviceDataDispatcher;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.datacollection.view.action.ResetSequenceButtonAction;
import com.honda.galc.client.linesidemonitor.property.LineSideMonitorPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>LineSideMonitorWindow Class description</h3>
 * <p> LineSideMonitorWindow description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 16, 2011
 *
 *
 */

public class LineSideMonitorWindow extends MainWindow{

	private static final long serialVersionUID = 1L;
	public static final String viewManagerKey = "VIEW_MANAGER";

	protected LineSideMonitorPanel lsmPanel ;
	protected ClientContext clientContext;
	protected DeviceDataDispatcher deviceDataDispatcher;
	protected IViewObserver dataCollectionViewManager;
	protected ViewManagerPropertyBean dataCollectionViewProperty;
	protected JMenuItem resetSequenceMenu;

	public LineSideMonitorWindow(ApplicationContext appContext,
			Application application) {
		super(appContext, application,true);

		setSize(1024,768);
		initClientPanel();
		
		addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e){	
				if(getDataCollectionViewManager() != null ) getDataCollectionViewManager().setProductInputFocused();						
			}
		});
	}

	protected void initClientPanel() {
		if (getLineSideMonitorProperty().isAllowDataCollection()) initDataCollection();
		try {
			Class<? extends LineSideMonitorPanel> forName = (Class<? extends LineSideMonitorPanel>) Class.forName(getLineSideMonitorProperty().getMainPanelClass());
			Class[] parameterTypes = { MainWindow.class };
			Object[] parameters = { this };
			Constructor constructor = forName.getConstructor(parameterTypes);
			lsmPanel = (LineSideMonitorPanel) constructor.newInstance(parameters);

		} catch (Exception e) {
			e.printStackTrace();
		}
		setClientPanel(lsmPanel);

	}

	@Override
	public void cleanUp() {
		super.cleanUp();
	}

	@Override
	protected JPanel initStatusMessagePanel() {
		if (getLineSideMonitorProperty().isAllowDataCollection()) {
			statusMessagePanel = new EnhancedStatusMessagePanel(getClientContext());
			return statusMessagePanel;
		}
		return super.initStatusMessagePanel();
	}

	@Override
	protected JMenu createSystemMenu() {
		JMenu systemMenu = super.createSystemMenu();
		getResetSequenceMenu().addActionListener(this);
		systemMenu.insert(getResetSequenceMenu(), 2);

		return systemMenu;
	}

	private JMenuItem getResetSequenceMenu() {
		if (this.resetSequenceMenu == null) {
			this.resetSequenceMenu = new JMenuItem("Reset Sequence");
		}
		return this.resetSequenceMenu;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);
		if (actionEvent.getSource() == getResetSequenceMenu()) {
			ResetSequenceButtonAction action = new ResetSequenceButtonAction(getClientContext(), "SET SEQ");
			action.actionPerformed(actionEvent);
			lsmPanel.getController().expectedProductUpdate();
		}
	}

	public DataCollectionController getDataCollectionController() {
		return DataCollectionController.getInstance(getApplication().getApplicationId().trim());
	}

	public ClientContext getClientContext() {
		if (this.clientContext == null) {
			this.clientContext = new ClientContext(getApplicationContext(), this);
		}
		return this.clientContext;
	}

	public DeviceDataDispatcher getDeviceDataDispatcher() {
		if (this.deviceDataDispatcher == null) {
			this.deviceDataDispatcher = new DeviceDataDispatcher(getApplication().getApplicationId().trim());
		}
		return this.deviceDataDispatcher;
	}

	public IViewObserver getDataCollectionViewManager() {
		if (this.dataCollectionViewManager == null) {
			String viewManagerClass = getViewManagerClass();
			if(StringUtils.isEmpty(viewManagerClass)){
				Logger.getLogger().error("Mandatory View Manager class is not configured!");
			}
			this.dataCollectionViewManager = (IViewObserver) getClientContext().createObserver(viewManagerClass);
		}
		return this.dataCollectionViewManager;
	}

	private String getViewManagerClass() {
		try {
			String viewManagerClass = PropertyService.getProperty(getClientContext().getProcessPointId(), viewManagerKey);
			if (viewManagerClass == null) {
				viewManagerClass = getDataCollectionViewProperty().getViewManager();
			}
			return viewManagerClass;
		} catch (Exception e) {
			Logger.getLogger().info(e, "Exception to get View Manager class.");
		}

		return null;
	}

	public ViewManagerPropertyBean getDataCollectionViewProperty() {
		if (this.dataCollectionViewProperty == null) {
			this.dataCollectionViewProperty = getClientContext().getProperty();
		}
		return this.dataCollectionViewProperty;
	}

	public LineSideMonitorPropertyBean getLineSideMonitorProperty() {
		return PropertyService.getPropertyBean(LineSideMonitorPropertyBean.class, getApplication().getApplicationId());
	}

	protected void initDataCollection() {
		try {
			EventBus.publish(new ProgressEvent(70, "Registering processors ..."));
			getDataCollectionViewProperty();

			getDataCollectionController().setClientContext(getClientContext());
			getDataCollectionController().registerProcessors();

			EventBus.publish(new ProgressEvent(80, "Preparing View Manager ..."));
			getDataCollectionViewManager();

			EventBus.publish(new ProgressEvent(90, "Adding Observers ..."));
			getDeviceDataDispatcher();

			getDataCollectionController().addObservers(getDataCollectionViewManager());
			getDataCollectionController().init();
			getDataCollectionController().setClientStarted(true);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public LineSideMonitorPanel getPanel() {
		return this.lsmPanel;
	}


}
