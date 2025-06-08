package com.honda.galc.client.qics.view.frame;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.common.component.EnhancedStatusMessagePanel;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.device.QicsDeviceDataDispatcher;
import com.honda.galc.client.qics.device.QicsDeviceManager;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.IdlePanel;
import com.honda.galc.client.qics.view.screen.MainPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Frame for Qics clients.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */

public class QicsFrame extends MainWindow implements ConnectionStatusListener {

	private static final long serialVersionUID = 1L;

	private IdlePanel idlePanel;
	private MainPanel mainPanel;

	private QicsViewId currentInputView;

	private QicsPanel currentPanel;

	private boolean plcSupported;
	private boolean terminalSupported;

	private QicsController qicsController;

	

	private Timer timeoutTimer;

	protected static final int STATUS_PANEL_HIGHT = 75;

	protected QicsDeviceManager deviceManager;

	protected QicsDeviceDataDispatcher deviceDataDispatcher;

	protected EnhancedStatusMessagePanel enhancedMessagePanel;

	/**
	 * @param clientId
	 * @param screenId
	 * @param windowTitle
	 * @param applicationId
	 * @param clientMain
	 * @param socketRequestDispatcher
	 * @throws com.honda.global.galc.common.SystemException
	 */
	public QicsFrame(ApplicationContext appContext, Application application) {
		super(appContext, application, true);

		setSize(1024, 768);

		initialize();
	}

	protected void initialize() {

		initFrameAttributes();

		initTerminal();

		startFrame();

		ServiceMonitor.getInstance().registerHttpServiceListener(this);

		initDeviceManager();
	}

	protected void initDeviceManager() {

		if (isPlcSupported()) {
			deviceDataDispatcher = new QicsDeviceDataDispatcher(this, false);
			deviceManager = new QicsDeviceManager(this);
		}
	}

	protected void initTerminal() {

		setQicsController(createQicsController());

		getQicsController().submitInitializeTerminal();
		setPlcSupported(DeviceManager.getInstance().hasEiDevice());

	}

	protected void initFrameAttributes() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		setFont(Fonts.DIALOG_PLAIN_14);
		setSize(1024, 768);
		setResizable(false);
		// setContentPane(getScreenContentPane());
		// setStatusBar(getStatusMessagePane());
		addWindowListener(createWindowListener());

		String iconUrl = "/resource/images/common/hcm.gif";
		try {
			ImageIcon frameIcon = new ImageIcon(getClass().getResource(iconUrl));
			setIconImage(frameIcon.getImage());
		} catch (Exception e) {
			System.err.println("Failed to load frame icon from url:'" + iconUrl + "'");
		}
	}

	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	public void startFrame() {

		idlePanel = new IdlePanel(this);
		mainPanel = new MainPanel(this);

		displayIdleView();

		// getMainPanel().add(getCurrentPanel(), "Center");
		getCurrentPanel().requestFocus();
	}

	/**
	 * <p>
	 * Sends data do plc when:
	 * <ul>
	 * <li>client (frame) is supporting plc communication, in this case
	 * <code>isUnique</code> flag indicates support for plc</li>
	 * <li>device is defined in task argument
	 * <code>ARG_TYPE_DEVICE=deviceName</code>
	 * </ul>
	 * If none of above is met method returns with no action.
	 * </p>
	 * 
	 * @param model
	 */
	public void sendDataCollectionCompleteToPlcIfDefined() {
		if (isPlcSupported()) {
			getDeviceManager().sendDataCollectionComplete();
		}
	}

	public void sendDataCollectionNotCompleteToPlcIfDefined() {
		if (isPlcSupported()) {
			getDeviceManager().sendDataCollectionInComplete();
		}
	}

	public void createDataCollectionCompleteDataContainer() {
		// Subclass Responsibilty
	}

	public void createDataCollectionCompleteDataContainer(int id) {
		// Subclass Responsibilty
	}

	// =============================================
	public void displayView(QicsPanel panel) {
		cancelTimeout();
		if(getQicsController().getQicsPropertyBean().isTimeoutInQicsProcess())
			setTimeout();
		
		clearMessage();
		setCurrentPanel(panel);
		panel.setVisible(false);

		clearClientPanel();

		// getMainPanel().requestFocus();

		getCurrentPanel().startPanel();

		setClientPanel(getCurrentPanel());
		getCurrentPanel().setVisible(true);
		resetMenuForCurrentScreen(panel);
		setInitialFocus(panel);

	}

	public void displayIdleView() {
		displayView(getIdlePanel());
		getLogger().info("display idle view screen");
		getMainPanel().resetSelectedPanelIndex();
		getMainPanel().removePanel(QicsViewId.PRE_CHECK_RESULTS);
		getMainPanel().removePanel(QicsViewId.CHECK_RESULTS);
		getMainPanel().removePanel(QicsViewId.DUNNAGE);
		getMainPanel().removePanel(QicsViewId.REPAIR_IN);
		setTimeout();
	}

	public void displayMainView() {
		displayView(getMainPanel());
		if (getQicsController().showPinCheckRequiredMessage()) {
			JOptionPane.showMessageDialog(getMainPanel(), getQicsController().getCheckOneOffMessage());
			displayView(getMainPanel());
		}
	}

	public void resetMenuForCurrentScreen(QicsPanel panel) {
		getSwitchUserMenu().setEnabled(isIdle());
		getCloseMenu().setEnabled(isIdle());
		getExitMenu().setEnabled(isIdle());
		getLogoutMenu().setEnabled(isIdle() && isUserLoggedIn());
	}

	public void updateUserLogedin() {
		if (isIdle()) {
			getIdlePanel().resetPanel();
			resetMenuForCurrentScreen(getCurrentPanel());
			setTimeout();
			if (!getQicsController().getQicsPropertyBean().isOverrideRepairAssociateEnabled()) {
				getQicsController().clearAssociateNumberCache();
			}
			if (isUserLoggedIn()) {
				getQicsController().cacheAssociateNumber(getUserId());
			}
		}
	}

	public QicsController createQicsController() {
		ProcessPoint processPoint = deriveProcessPoint();
		QicsController qicsController = new QicsController(this, getApplicationContext().getHostName(), processPoint);
		return qicsController;

	}

	protected ProcessPoint deriveProcessPoint() {

		return getDao(ProcessPointDao.class).findByKey(getApplication().getApplicationId());

	}

	

	protected WindowListener createWindowListener() {

		WindowListener listener = new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				if (!isUserLoggedIn()) {
					switchUser(false, true);
					resetMenuForCurrentScreen(getCurrentPanel());
				}
				getCurrentPanel().startPanel();
				getCurrentPanel().requestFocus();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
				setExtendedState(Frame.NORMAL);
				toFront();
				getCurrentPanel().requestFocus();
			}
		};
		return listener;
	}

	public QicsPanel getCurrentPanel() {
		return currentPanel;
	}

	protected void setCurrentPanel(QicsPanel currentPanel) {
		this.currentPanel = currentPanel;
	}

	protected boolean isPlcSupported() {
		return plcSupported;
	}

	protected void setPlcSupported(boolean plcSupported) {
		this.plcSupported = plcSupported;
	}

	// protected void setPlcSupported(DataContainer dataContainer) {
	// boolean defined = false;
	// defined = getPlcManager().isPlcDefined(dataContainer);
	// setPlcSupported(defined);
	// }
	//
	// protected void setTerminalSupported(DataContainer dataContainer) {
	// boolean defined = false;
	// defined = getPlcManager().isTerminalDefined(dataContainer);
	// setTerminalSupported(defined);
	// }

	protected QicsViewId getCurrentInputView() {
		return currentInputView;
	}

	protected void setCurrentInputView(QicsViewId viewId) {
		if (QicsViewId.PICTURE_INPUT.equals(viewId) || QicsViewId.TEXT_INPUT.equals(viewId)) {
			this.currentInputView = viewId;
		}
	}

	public boolean isIdle() {
		return currentPanel instanceof IdlePanel;
	}

	// === 'tweak' - to accomodate second level login (for associate) === //
	// === clients usually are started with generic terminal user === //
	public boolean isUserLoggedIn() {
		return !StringUtils.equals(getUserId(), getClientHostName());
	}

	protected boolean isTerminalSupported() {
		return terminalSupported;
	}

	protected void setTerminalSupported(boolean terminalSupported) {
		this.terminalSupported = terminalSupported;
	}

	public QicsController getQicsController() {
		return qicsController;
	}

	protected void setQicsController(QicsController qicsController) {
		this.qicsController = qicsController;
	}

	public void setInitialFocus() {
		setInitialFocus(getCurrentPanel());
	}

	public void setInitialFocus(JComponent element) {
		if (element != null) {
			if (element instanceof JButton) {
				getRootPane().setDefaultButton((JButton) element);
			}
		}
	}

	protected boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		if (str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public IdlePanel getIdlePanel() {
		return idlePanel;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public JMenuItem getLogoutMenu() {
		return logoutMenuItem;
	}

	public List<QicsFrame> getQicsApplications() {
		List<QicsFrame> list = new ArrayList<QicsFrame>();
		for (MainWindow o : ClientMain.getInstance().getLaunchedWindows().values()) {
			if (o instanceof QicsFrame) {
				QicsFrame qf = (QicsFrame) o;
				list.add(qf);
			}
		}
		return list;
	}

	public List<QicsFrame> getQicsApplicationsInProcess() {
		List<QicsFrame> list = new ArrayList<QicsFrame>();
		for (QicsFrame qf : getQicsApplications()) {
			if (!qf.isIdle()) {
				list.add(qf);
			}
		}
		return list;
	}

	public List<String> getQicsApplicationsInProcessNames() {
		List<String> list = new ArrayList<String>();
		for (QicsFrame qf : getQicsApplicationsInProcess()) {
			String application = qf.getQicsController().getClientModel().getProcessPoint().getProcessPointName();
			application = application + " (" + qf.getApplication().getApplicationId() + ")";
			list.add(application);
		}
		return list;
	}

	public boolean isApplicationInProcessExistCheck() {
		List<String> applicationNames = getQicsApplicationsInProcessNames();
		if (applicationNames.size() > 0) {
			String msg = "The following QICS applications are still in process: \n" + applicationNames;
			JOptionPane.showMessageDialog(this, msg);
			QicsFrame appInProcess = getQicsApplicationsInProcess().get(0);
			ClientMain.getInstance().launchApplication(appInProcess.getApplication());
			return true;
		}
		return false;
	}

	@Override
	protected void exit() {
		if(isApplicationInProcessExistCheck()) return;
		super.exit();
	}
	
	public void logon() {
		logon(false);
	}
	
	protected void logon(boolean allowExit) {
		ClientMain.getInstance().login();
		getApplicationContext().setUserId(ClientMain.getInstance().getAccessControlManager().getUserName());
		getCurrentPanel().startPanel();
		switchUser(allowExit,true);
		getCurrentPanel().startPanel();
	}

	@Override
	public void switchUser(boolean allowExit, boolean cancelOption) {
		cancelTimeout();
		super.switchUser( allowExit, cancelOption);
		DefectRepairPanel qicsPanel = (DefectRepairPanel)mainPanel.getInputPanel(QicsViewId.REPAIR);
		if(qicsPanel != null ) qicsPanel.refreshAssociateNumbers();
		setTimeout();
	}
	
	protected void cancelTimeout() {
		if (getTimeoutTimer() != null) {
			try {
				getTimeoutTimer().cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
			setTimeoutTimer(null);
		}
	}

	public synchronized void setTimeout() {

		cancelTimeout();

		if (getQicsController().getQicsPropertyBean().getTimeout() < 1) {
			return;
		}
		// if (!getQicsApplicationsInProcess().isEmpty()) {
		// return;
		// }
		if (!isUserLoggedIn()) {
			return;
		}

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				timeout();
			}
		};
		Timer timer = new Timer("timeoutTimer", true);
		long time = getQicsController().getQicsPropertyBean().getTimeout() * 60 * 1000L;
		timer.schedule(task, time);
		setTimeoutTimer(timer);
	}

	protected void timeout() {
		if (isRequireLogin()) {
			if (getQicsController().getQicsPropertyBean().isLogoutOnTimeout()) {
				if(getLogoutMenu().isEnabled())
		            getLogoutMenu().doClick();
		        else
		            switchUser(false, false, false);
		    }
			else if(!getCurrentPanel().equals(getIdlePanel())) {
				displayIdleView();
			}
		 }
	}		
	
	public void switchUser(boolean allowExit, boolean cancelOption, boolean enableChangePsswd) {
		cancelTimeout();
		super.switchUser( allowExit, cancelOption, enableChangePsswd);
		DefectRepairPanel qicsPanel = (DefectRepairPanel)mainPanel.getInputPanel(QicsViewId.REPAIR);
		if(qicsPanel != null ) qicsPanel.refreshAssociateNumbers();
		setTimeout();
		
	}

	private boolean isRequireLogin() {
		if(!getQicsController().getQicsPropertyBean().isTimeoutInQicsProcess())
			return isIdle() && isUserLoggedIn() && getQicsApplicationsInProcess().isEmpty() && !getQicsApplications().isEmpty();
		else
			return isUserLoggedIn() && !getQicsApplications().isEmpty();
		
	}
	
	public synchronized Timer getTimeoutTimer() {
		return timeoutTimer;
	}

	public synchronized void setTimeoutTimer(Timer timeoutTimer) {
		this.timeoutTimer = timeoutTimer;
	}

	@Override
	protected JPanel initStatusMessagePanel() {
		if (enhancedMessagePanel == null) {
			enhancedMessagePanel = new EnhancedStatusMessagePanel();
			enhancedMessagePanel.setProperty(getDevicePropertyBean());
			enhancedMessagePanel.setSize(1000, STATUS_PANEL_HIGHT);
			enhancedMessagePanel.setLocation(10, 640);
			enhancedMessagePanel.getStatusPanel().setUser(getUserId());
			statusMessagePanel = enhancedMessagePanel;
		}
		return enhancedMessagePanel;
	}

	private DevicePropertyBean getDevicePropertyBean() {
		return PropertyService.getPropertyBean(DevicePropertyBean.class, applicationContext.getProcessPointId());
	}

	public void statusChanged(ConnectionStatus status) {

		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, status.isConnected()));

	}

	public QicsDeviceManager getDeviceManager() {
		return deviceManager;
	}

	public void setDeviceManager(QicsDeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}
}
