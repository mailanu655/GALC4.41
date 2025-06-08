package com.honda.galc.client.datacollection.view.info;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.LotControlAccessControlManager;
import com.honda.galc.client.common.datacollection.data.AutoCloseMessage;
import com.honda.galc.client.common.datacollection.data.AutoFocusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.InstalledPartCache;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.ui.component.SplitInfoPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
/**
 * 
 * <h3>LotControlSystemInfo</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlSystemInfo description </p>
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
 * @author Paul Chou
 * Mar 25, 2010
 *
 */
public class LotControlSystemInfo extends JFrame
implements ActionListener, ChangeListener{

    public static final int WIN_HIGHT = 680; //768
    public static final int WIN_WIDTH = 980; //1024
    public static final int TOP_PANEL_HIGHT = 40;
    public static final int BOTTOM_PANEL_HIGHT = 40;
    private static final long serialVersionUID = 1L;
    private JTabbedPane workPanel = null;
    private SplitInfoPanel statePanel;
   
	private AudioConfigPanel audioPanel = null;
    private LogInfoPanel logInfoPanel = null;
    private CacheInfoPanel cacheInfoPanel = null;
    private PropertyPanel propertyPanel = null;
	private JPanel bottomPanel = null;
    private JButton closeButton = null; 
    private JButton actionButton = null; 
    private ClientContext context;
    private ButtonAction action;
    private String lineSeparator = System.getProperty("line.separator");
    private enum ButtonAction{Save, Log_To_Server, Save_To_File}
    private LogInfoManager logManager;
    private PropertyInfoManager propertyManager;
    private DeviceInfoPanel devicesPanel;
    private DevicesInfoManager deviceInfoManager;
    
	private JComponent currentPanel;
	private StateInfoManager stateInfoManager;

	/**
     * This is the default constructor
     */
    public LotControlSystemInfo() {
        super();
        initialize();
    }

    public LotControlSystemInfo(ClientContext context) {
		this.context = context;
		initialize();
	}

	/**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
    	AnnotationProcessor.process(this);
    	
        this.setSize(WIN_WIDTH, WIN_HIGHT);
        this.setTitle("Lot Control Client Information");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );

        initComponents();
        
        initConnections();
        
    }

	private void initComponents() {
        audioPanel = getAudioPanel();
        logInfoPanel = getLogInfoPanel();
        cacheInfoPanel = getCacheInfoPanel();
        statePanel = getStatePanel();
        devicesPanel = getDevicesPanel();
        getStateInfoManager();
        
        if(context.getProperty().isShowDevicesTab())
        	getDeviceInfoManager();
        
        this.add(getWorkPanel(), BorderLayout.CENTER);
        this.add(getBottomPanel(), BorderLayout.SOUTH);
	}

    private CacheInfoPanel getCacheInfoPanel() {
		if(cacheInfoPanel == null)
			cacheInfoPanel = new CacheInfoPanel(context);
		return cacheInfoPanel;
	}

	public LogInfoPanel getLogInfoPanel() {
		if(logInfoPanel == null){
			logInfoPanel = new LogInfoPanel("Log:", context.getProperty().getClientInfoLogLevel());
		}
		return logInfoPanel;
	}

	private AudioConfigPanel getAudioPanel() {
		if(audioPanel == null){
			audioPanel = new AudioConfigPanel(context);
		}
		
		return audioPanel;
	}


    /**
     * This method initializes informationPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JComponent getWorkPanel() {
        if (workPanel == null) {
            workPanel = new JTabbedPane();
            workPanel.addChangeListener(this);
            workPanel.addTab("Property", null, getPropertyPanel(), "Lot Control Properties");
            workPanel.addTab("State", null, getStatePanel(), "Current States");
            workPanel.addTab("Log", null, getLogInfoPanel(), "Log information");
            workPanel.addTab("Cache", null, getCacheInfoPanel(), "Cached data");
            workPanel.addTab("Audio", null, getAudioPanel(), "Audio Configuration");
            
            //make the Devices Tab configurable for it still under development
            if(context.getProperty().isShowDevicesTab())
            	workPanel.addTab("Devices", null, getDevicesPanel(), "Devices Information");
        }
        return workPanel;
    }
    
  private PropertyPanel getPropertyPanel() {
		if(propertyPanel == null){
			propertyPanel = new PropertyPanel();
		}
		
		return propertyPanel;
	}

  private DeviceInfoPanel getDevicesPanel() {
		if(devicesPanel == null){
			devicesPanel = new DeviceInfoPanel("Devices",context);
		}
		
		return devicesPanel;
	}

    /**
     * This method initializes bottumPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            bottomPanel.setSize(WIN_WIDTH, BOTTOM_PANEL_HIGHT);
            bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 5));
            bottomPanel.add(getActionButton());
            bottomPanel.add(getCloseButton());
        }
        return bottomPanel;
    }
    
 
    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getActionButton() {
        if (actionButton == null) {
        	actionButton = new JButton();
        	actionButton.setSize(150, 35);
        	actionButton.setText("Log to Server");        	
        	actionButton.setVisible(true);
        }
        return actionButton;
    }
 
    /**
     * This method initializes ClearButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            actionButton.setSize(100, 35);
            closeButton.setText("Close");
            
        }
        return closeButton;
    }
    
    private void initConnections(){
        getCloseButton().addActionListener(this);
        getActionButton().addActionListener(this);
        
        logManager = LogInfoManager.getInstance();
        logManager.setConnections(getLogInfoPanel(), this);
        
        propertyManager = new PropertyInfoManager(context, getPropertyPanel());
        
    }
    
    private void audioConfig() {

    	getAudioPanel().refresh();
    	enableActionButton(ButtonAction.Save, true);
    	getActionButton().setEnabled(context.isOnLine());
    }
    
    private void performAction() {
    	if(action == null) return;
    	else if(ButtonAction.Log_To_Server == action)
			logToServer();
		else if(ButtonAction.Save_To_File == action)
			saveToFile();
		else if(ButtonAction.Save == action){
			if(currentPanel instanceof DeviceInfoPanel)
				getDevicesPanel().saveDeviceConfig();
			
			else{
				if(context.getProperty().isNeedAuthorizedUserToChangeAudioConfig() &&
					!LotControlAccessControlManager.getInstance().login())
				return;
    	
				getAudioPanel().saveAudioConfig();
			}
		}
	}

	

	private void saveToFile() {
		String cachedData = getCacheDetails();
    	if(StringUtils.isEmpty(cachedData)) return ;
    	
    	JFileChooser fc = new JFileChooser();
    	try {			
			int ret = fc.showSaveDialog(this);
			if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
			File fFile = fc.getSelectedFile();
			FileOutputStream fos = new FileOutputStream(fFile);
			fos.write(cachedData.getBytes());
			PrintStream ps = new PrintStream(fos);
			ps.flush();
			ps.close();
		} catch (Exception e) { 
			Logger.getLogger().warn(e, "Failed to save cache data to file.");
		} 	
		
	}

	@SuppressWarnings("unchecked")
	private String getCacheDetails() {
		List <Integer> keys = InstalledPartCache.getInstance().getKeys();
    	Collections.sort(keys);
    	
    	StringBuilder sb = new StringBuilder();
    	for(Integer key : keys){
    		List<InstalledPart> installedParts = InstalledPartCache.getInstance().get(key, List.class);
    		if(installedParts == null || installedParts.size() == 0) return "";
    		
    		for(InstalledPart part : installedParts)
    			sb.append(part.toString());
    	}
    	
		return sb.toString();
	}

	private void logToServer() {
		String details = getStateDetails();
		if(!StringUtils.isEmpty(details))
			Logger.getLogger().info(details);
	}

	protected void showCacheData() {
		currentPanel = getCacheInfoPanel();
    	enableActionButton(ButtonAction.Save_To_File, true);
    	
    	getCacheInfoPanel().refresh();
	}


	protected void close() {
    	if (this.isVisible()) {
    		this.dispose();
    		this.setVisible(false);
    	}
    		devicesPanel.releaseResources();
    	EventBus.publish(new AutoFocusMessage(this.getClass().getSimpleName(), null));
        
    }
    
    @EventSubscriber(eventClass = AutoCloseMessage.class)
    public void autoClose(AutoCloseMessage message){
    		close();
			
    }

	private void showLogs() {
		currentPanel = getLogInfoPanel();
    	enableActionButton(null, false);
    	getLogManager().refresh();
    	
    }
	
	private void showCurrentState() {
		currentPanel = getStatePanel();
		enableActionButton(ButtonAction.Log_To_Server, true);
		getStateInfoManager().refresh();
	}

	private String getStateDetails() {
		StringBuilder msg = new StringBuilder();
    	DataCollectionState state = getCurrentState();
    	
		msg.append("Product Spec Code: " + state.getProductSpecCode()).append(lineSeparator);
		
		if (state.getProductSpecCode() != null) 
			appendStateInfo(msg, state);
		
		
		msg.append("").append(lineSeparator);
		if(state.getLotControlRules() != null && state.getLotControlRules().size() > 0)
			appendLotControlRules(msg, state);
		
		
		msg.append("").append(lineSeparator);
		if(state.getProduct() != null){
			msg.append("---Collected Data:").append(lineSeparator);
			msg.append(state.getProduct()).append(lineSeparator);
		}
	
		if(state.hasMessage() && state.getMessage().getDescription() != null)
			msg.append("Message:" + state.getMessage().getDescription()).append(lineSeparator);
		return msg.toString();
	}

	private void enableActionButton(ButtonAction buttonAction, boolean enable) {
		this.action = buttonAction;
		if(buttonAction != null)
			getActionButton().setText(buttonAction.toString().replace("_", " "));
		
		getActionButton().setEnabled(enable);
		getActionButton().setVisible(enable);
		
	}

	private void appendLotControlRules(StringBuilder msg,
			DataCollectionState state) {
		msg.append("Lot Control Rules:").append(lineSeparator);
		for( LotControlRule r : state.getLotControlRules()){
			msg.append(r.toString()).append(lineSeparator);
		}
	}

	private void appendStateInfo(StringBuilder msg, DataCollectionState state) {
		msg.append("Product Id:" + state.getProductId()).append(lineSeparator);
		msg.append("Product Spec:" + state.getProductSpecCode()).append(lineSeparator);
		msg.append("Expected ProductId:" + state.getExpectedProductId()).append(lineSeparator);
		msg.append("Current Part Index: " + state.getCurrentPartIndex()).append(lineSeparator);
		msg.append("Current Torque Index: "	+ state.getCurrentTorqueIndex()).append(lineSeparator);
	}
    

    private DataCollectionState getCurrentState() {
		return DataCollectionController.getInstance().getState();
	}


    public SplitInfoPanel getStatePanel() {
    	if(statePanel == null){
    		statePanel = new SplitInfoPanel();
    		statePanel.initialize();
    	}
		return statePanel;
	}
    
    
	public InstalledPartCache getCache() {
		return InstalledPartCache.getInstance();
	}

	public void refresh() {
		if(currentPanel instanceof InformationPanel){
			((InformationPanel)currentPanel).refresh();
		} else if(currentPanel instanceof LogInfoPanel){
			getLogManager().refresh();
		} else if(currentPanel == getStatePanel()){
			getStateInfoManager().refresh();
		} else if (currentPanel == getCacheInfoPanel()){
			getCacheInfoPanel().refresh();
		}
		else if (currentPanel instanceof DeviceInfoPanel){
			getDevicesPanel().refresh();
		}
			
		
	}
	
	public StateInfoManager getStateInfoManager() {
		if(stateInfoManager == null){
			stateInfoManager = new StateInfoManager(context, getStatePanel());
		}
		return stateInfoManager;
	}
	
	public DevicesInfoManager getDeviceInfoManager() {
		if(deviceInfoManager == null){
			deviceInfoManager = new DevicesInfoManager(context, getDevicesPanel());
		}
		return deviceInfoManager;
	}
	

	public void actionPerformed(ActionEvent e) {

		if(getCloseButton() == e.getSource()) {
			close();
		} else if(getActionButton() == e.getSource()){
			performAction();
		}
	}

	public ClientContext getContext() {
		return context;
	}

	public LogInfoManager getLogManager() {
		return logManager;
	}

	public JComponent getCurrentPanel() {
		return currentPanel;
	}

	public void stateChanged(ChangeEvent e) {
		JTabbedPane pane = (JTabbedPane)e.getSource();

        // Get current tab
        int sel = pane.getSelectedIndex();
        Logger.getLogger().info("Tab state change called" + " tab selected : "+ sel); 
        currentPanel = null;
        
        switch(sel){
        case 0://Property
        	enableActionButton(null, false);
        	break;
        case 1://State
        	
        	showCurrentState();
        	break;
        case 2://Log
        	
        	showLogs();
        	break;
        case 3://Cache
        	
        	showCacheData();
        	break;
        case 4://Audio
        	audioConfig();
        	break;

        case 5://Devices
        showDevicedata();
        	break;
        default:

        }
      

		
	}

	protected void showDevicedata() {
		currentPanel = getDevicesPanel();
		enableActionButton(ButtonAction.Save, true);
    	getActionButton().setEnabled(context.isOnLine());
	}
	
	public PropertyInfoManager getPropertyManager() {
		return propertyManager;
	}

	public void setPropertyManager(PropertyInfoManager propertyManager) {
		this.propertyManager = propertyManager;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
