package com.honda.galc.client.datacollection.view;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.common.LotControlAccessControlManager;
import com.honda.galc.client.common.datacollection.data.AutoCloseMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.DeviceDataDispatcher;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.datacollection.view.action.CancelButtonAction;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.ITorqueDeviceListener;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDevice;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>LotControlMainBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlMainBase description </p>
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
 * <TD>Jan 26, 2012</TD>
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
 * @since Jan 26, 2012
 */

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class LotControlMainBase extends MainWindow {
	private static final long serialVersionUID = 1L;
	protected ClientContext clientContext;
	protected DeviceDataDispatcher deviceDataDispatcher;
	protected IViewObserver viewManager;
	protected ViewManagerPropertyBean viewProperty;
	protected JMenuItem switchModeMenu;
	protected JMenuItem refreshMenu;
	private String authorizedUser;
	private JMenuItem clientModeMenuItem;
	protected enum LotControlMode{ON_LINE_MODE, OFF_LINE_MODE};
	
	public LotControlMainBase(ApplicationContext appContext,Application application) {
		super(appContext,application, true);
		initialize();
	}
	
	private void initialize() {
		
		addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        EventBus.publish(new AutoCloseMessage(this.getClass().getSimpleName(), null));
		    }
		});

		EventBus.publish(new ProgressEvent(70, "Registering processors ..."));
		viewProperty = getClientContext().getProperty();
		
		getController().setClientContext(getClientContext());
		getController().registerProcessors();

		EventBus.publish(new ProgressEvent(80, "Setting Frame Properties ..."));
		setFrameProperties();
		
		EventBus.publish(new ProgressEvent(90, "Adding Observers ..."));
		deviceDataDispatcher = new DeviceDataDispatcher(getApplication().getApplicationId().trim());
		
		getController().addObservers(viewManager);
	//	getController().init();
		getController().setClientStarted(true);
		getController().init();
	}

	private DataCollectionController getController() {
		return DataCollectionController.getInstance(getApplication().getApplicationId().trim());
	}

	protected ClientContext getClientContext() {
		if(clientContext == null)
			clientContext = new ClientContext(applicationContext, this);

		return clientContext;
	}


	protected abstract void setFrameProperties();

	protected void initConnections()  throws java.lang.Exception {
		// user code begin {1}		
		addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e){				
				viewManager.getView().setProductInputFocused();						
			}
		});
	}

	protected void handleException(Throwable exception) {
		Logger.getLogger().error(exception, this.getClass().getSimpleName());
	}

	@Override
	protected JMenu createAboutMenu() {
		JMenu aboutMenu = super.createAboutMenu();
		TerminalPropertyBean propertyBean = PropertyService.getPropertyBean(TerminalPropertyBean.class, applicationContext.getTerminalId());
		aboutMenu.add(createAboutMenuItem("--------------  LOT CONTROL  --------------"));
		aboutMenu.add(createAboutMenuItem("PRODUCT TYPE: " + propertyBean.getProductType()));
		aboutMenu.add(createAboutMenuItem("FSM         : " + propertyBean.getFsmType()));
		clientModeMenuItem = createAboutMenuItem(getClientModeText());
		aboutMenu.add(clientModeMenuItem);
		
		
		return aboutMenu;
	}

	private String getClientModeText() {
		return "CLIENT MODE : " + (getClientContext().isUserOverrideOnline() ? "ON-LINE" : "OFF-LINE");
	}
	
	

	@Override
	protected JMenu createSystemMenu() {
		JMenu systemMenu = super.createSystemMenu();
		getSwitchModeMenu().addActionListener(this);
		getRefreshMenu().addActionListener(this);
		
		if(getApplicationPropertyBean().isSwitchModeSystemMenuEnabled())
			systemMenu.insert(getSwitchModeMenu(), 1);
		systemMenu.insert(getRefreshMenu(), 2);
		
		return systemMenu;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() == switchModeMenu) switchMode();
		else if(e.getSource() == refreshMenu) refreshScreen();
	}

	private void refreshScreen() {
		
		getClientContext().refresh();
		DataCollectionController.getInstance(getApplication().getApplicationId().trim()).getFsm().cancel();
	}

	private void switchMode() {
		Logger.getLogger().info("mode:" + clientContext.isUserOverrideOnline());

		if(!login()) return;
		
		LotControlMode newMode = (LotControlMode) JOptionPane.showInputDialog(
				new JFrame(), "Please select Lot Control Client Mode:",
				"Switch Lot Control On Line Mode", JOptionPane.INFORMATION_MESSAGE,
				null, LotControlMode.values(), LotControlMode.ON_LINE_MODE);

		if(newMode != null){
			Logger.getLogger().info("User's input: " + newMode.toString());
			boolean newModeBoolean = newMode == LotControlMode.ON_LINE_MODE ? true : false;
			if(newModeBoolean != clientContext.isUserOverrideOnline()){
				Logger.getLogger().info("User Override On Line Mode changed from ", 
						clientContext.isUserOverrideOnline() ? LotControlMode.ON_LINE_MODE.toString() : LotControlMode.OFF_LINE_MODE.toString(),
						" to ", newMode.toString(), " by user ", authorizedUser);
				
				clientContext.setUserOverrideOnline(newModeBoolean);
				clientModeMenuItem.setText(getClientModeText());
			}
		}


	}
	
	protected boolean login() {
		if(LotControlAccessControlManager.getInstance().login()){
			authorizedUser = LotControlAccessControlManager.getInstance().getAuthorizedUser();
			return true;
		} else {
			authorizedUser = null;
			return false;
		}
	}
	
	@Override
	protected void close() {
		//simulate pressing the cancel button
		if (getApplicationPropertyBean().isModal() || getApplicationPropertyBean().isModalParent()) {
			//stop alarm
			if(LotControlAudioManager.isExist()) LotControlAudioManager.getInstance().stopRepeatedSound();
			
			//cancel processing
			CancelButtonAction cancel = new CancelButtonAction(clientContext, "Cancel");
			cancel.doCancel();
			
			//remove torque device listener associated with this instance and swap master in torque socket device
			for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
				ITorqueDeviceListener removeListener = null;
				for (final ITorqueDeviceListener listener : ((TorqueSocketDevice)device).getListeners()) {
					//since it is a modal client the master listener will always be the listener associated with this instance
					//we just have to find it from the list of listeners in the torque socket device
					if (listener == ((TorqueSocketDevice)device).getMaster()) {
						removeListener = listener;
						break; //listener found, no need to continue
					}
				}
				//remove listener from the list
				if (removeListener != null) ((TorqueSocketDevice)device).unregisterListener(removeListener);
				
				((TorqueSocketDevice)device).swapMaster();
			}
		}
		//close client
		super.close();
	}
	
	
	@Override
	public void cleanUp() {
		super.cleanUp();
		getController().cleanUp();
		
	}

	@Override
	protected void exit() {
		if(MessageDialog.confirm(this, "Are you sure that you want to exit?")){
			getController().cleanUp();
			ClientMain.getInstance().exitApplication(0);
		}
	}

	public IViewObserver getViewManager() {
		return viewManager;
	}

	public void setViewManager(IViewObserver viewManager) {
		this.viewManager = viewManager;
	}

	public JMenuItem getSwitchModeMenu() {
		if(switchModeMenu == null)
			switchModeMenu = new JMenuItem("Switch Mode");
		return switchModeMenu;
	}

	public JMenuItem getRefreshMenu() {
		if(refreshMenu == null)
			refreshMenu = new JMenuItem("Refresh");
		
		return refreshMenu;
	}

	
}
