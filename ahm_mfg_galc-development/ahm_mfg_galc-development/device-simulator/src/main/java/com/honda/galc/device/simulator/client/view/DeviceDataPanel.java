package com.honda.galc.device.simulator.client.view;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.appclient.ApplicationServerClientManager;
import com.honda.galc.device.simulator.client.IEiSender;
import com.honda.galc.device.simulator.client.OpcEiSender;
import com.honda.galc.device.simulator.client.RestServiceClient;
import com.honda.galc.device.simulator.client.TorqueSender;
import com.honda.galc.device.simulator.client.ui.GalcPanel;
import com.honda.galc.device.simulator.client.view.cfg.DeviceSimulatorConfigManager;
import com.honda.galc.device.simulator.client.view.cfg.SimulatorConfig;
import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;
import com.honda.galc.device.simulator.data.DataContainerCommonUtil;
import com.honda.galc.device.simulator.data.DataContainerProcessor;
import com.honda.galc.device.simulator.data.ITagValueSource;
import com.honda.galc.device.simulator.data.SimulatorConstants.ServerType;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.service.ServiceFactory;


/**
 * <h3>DeviceDataPanel</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Update to support device simulator configuration.</TD>
 * </TR>
 * </TABLE>
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class DeviceDataPanel extends GalcPanel implements IDeviceSelectable, DataContainerProcessor {

	private static final long serialVersionUID = 1L;
    ITagValueSource tagValueSource;  //  @jve:decl-index=0:
    IDeviceSelectListener deviceSelector;

    private final static int SIZE_FOR_DIVIDER= 8;
    private String DEFAULT_SERVICE_NAME="DataCollectionService";
    private String INVOKING_METHOD="execute";
	
	private final static String TAG = "tag";
	private final static String DOT = ".";
	
	private JSplitPane splitPane = null;
	private JScrollPane deviceRequestPane = null;
	private JScrollPane deviceResponsePane = null;
	private JTable requestDataTable = null;
	private JTable responseDataTable = null;
	private JButton loadButton = null;
	private JButton saveButton = null;
	private JButton sendButton = null;
	private JButton clearButton = null;	
	private JButton clearResponseButton = null;
    private JLabel requestPaneTitle = new JLabel("");
    private JLabel responsePaneTitle = new JLabel("");
	private DataContainer dc;
	private SimulatorServerPanel simulatorServerPanel = null;
	private Device device; // client id passed from the left panel.
	private String repClientID;	
	boolean bGunEnabled = true;
	boolean isShowTag = true;
	private TorqueServerPanel torqueServerPanel;
	private TorqueSender torqueSender;
	

	private Logger log = Logger.getLogger(this.getClass().getName());
	ApplicationServerClientManager clnMgr = null;
	

    /**
	 * This method initializes the DeviceDataPanel
	 * 
	 */
	public DeviceDataPanel() {
		super();
        this.dc = new DefaultDataContainer();
		initialize();
	}
	
	/*
	 * This method is to received device ID from the left panel
	 * @see com.honda.global.device.simulator.IDeviceSelectable#setDevice(java.lang.String)
	 */
	public void setDevice(Device device) {	
		DeviceSimulatorConfigManager cfgMgr = DeviceSimulatorConfigManager.getInstance();
		isShowTag = cfgMgr.getConfig().isSenderShowTag();

		if(this.device != null){
   //         collectRequestTableDataFromGUI();
   //         dataCache.setDataCache(dc,device.getClientId());
   //         DataContainer aDC = collectResponseTableDataFromGUI();
   //         dataCache.setDataCache(aDC, device.getReplyClientId());
            //reset dc now to clean up data for previous client
    //        dc = new DefaultDataContainer();        
        }
		this.getSplitPane();
		
		this.device = device;
		getRequestDataTable();
		getResponseDataTable();
//		populateDataToRequestTable(device.getClientId());
        requestPaneTitle.setText(device == null ? "" : device.getClientId());               
 //       populateResponseToTable(device.getReplyClientId());
        responsePaneTitle.setText(device == null ? "" : device.getReplyClientId());
        
  	}
	
	/*
	 * This method is to get the device ID
	 * 
	 */
	public Device getDevice() {
		return device;
	}
	
	public JTable getRequestTable() {
		return requestDataTable;
	}
	
	public JTable getResponseTable() {
		return responseDataTable;
	}

	/**
	 * Sets tag value source to be used when populating tag values
	 * 
	 * @param tagValueSource tag value source
	 */
	public void setTagValueSource(ITagValueSource tagValueSource)
	{
		this.tagValueSource = tagValueSource;
	}
	
	/**
	 * Sets deviceListener to be used when calling the DeviceSelector
	 * @param deviceSelector
	 */
	public void setDeviceSelector(IDeviceSelectListener deviceSelector) {
		this.deviceSelector = deviceSelector;
	}
	
	/**
	 * This method is to populate the loaded properties data into the GUI request area.
	 */
	public void populateLoadedPropToRequestTable(){
		
		for(DeviceFormat df : device.getDeviceDataFormats()) {
		    String value = tagValueSource.getTagValue(getTagPrefix(), df.getTag());
		    if(value != null) df.setValue(value);
		}
		
		requestDataTable.updateUI();
	}

	/**
	 * This method is to store all the data from GUI into a Properties object
	 */
	public Properties getDataKeyValues() {
		log.info("To save file for the client id:"+device.getClientId());
		Properties prop = new Properties();
		prop.setProperty(getEntry()+"Device", device.getClientId());
		for(DeviceFormat df : device.getDeviceDataFormats()) {
		    prop.setProperty(getTagPrefix() + df.getTag(), df.getValue().toString());
		}
		
	    for(DeviceFormat df : device.getReplyDeviceDataFormats()) {
	            prop.setProperty(getTagPrefix() + df.getTag(), df.getValue().toString());
	    }

		return prop;
	}
	

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {	
		
		simulatorServerPanel = new SimulatorServerPanel();        
		simulatorServerPanel.setProcessor(this);
		
		clnMgr = new ApplicationServerClientManager();
		
		setLayout(new BorderLayout());		
		add(this.getSplitPane(), BorderLayout.CENTER);
		addListeners();	

	}
	
	/**
	 * This method is to add listeners
	 */
    private void addListeners(){
	    getSendButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean dataIsComplete = collectRequestTableDataFromGUI();
				if (!dataIsComplete){
					displayError("Please enter the complete data before you send to GALC!");
				} else {
					DataContainer resDC = send();
					if ( resDC==null) {
						//This is the case that the DATA is 0, then no need to populate the data in to GUI
						displayError("There is a problem when GALC processing your request, please check the log!");					
					} else {
						log.info("Returned Data:" + resDC);
					    populateDataContainerToResponseTable(resDC);
						displayMessage("");
					}
				}
			}
		});	
		getClearButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                clearDeviceData();
                displayMessage("");
			}
		});
		
		getSaveButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean dataIsComplete = collectRequestTableDataFromGUI();
       
				if (!dataIsComplete || device == null){
					displayError("Please enter the complete data before you save to a file!");
				} else {
					tagValueSource.saveEntries(getDataKeyValues());
        			displayMessage("");
				}
			}
		});
		getLoadButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMessage("");
				tagValueSource.loadEntries(null);
				String deviceId = tagValueSource.getDeviceId("");
				if (deviceId==null){
					displayError("The file you try to load is not the proper DATA file, please check the file!");
					return;
				}
				
				deviceSelector.setDeviceSelection(deviceId);
				populateLoadedPropToRequestTable();
                collectRequestTableDataFromGUI();
 			}
		});
		getClearResponseButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                clearResponseData();
				displayMessage("");
			}
		});
		
		getTorqueSendButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getTorqueSender().send(getTorque());
			}
		});	
		
    }
     
	protected void clearResponseData() {
		device.resetReplyFormats();
	    DeviceFormatTableModel tm = new DeviceFormatTableModel(device.getReplyDeviceDataFormats(),responseDataTable);
        
	    responseDataTable.setModel(tm);
		responseDataTable.updateUI();
		
	}
	protected TorqueSender getTorqueSender() {
		if(torqueSender == null)
			torqueSender = new TorqueSender();
		
		return torqueSender;
	}

	/**
	 * This method initializes splitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
		}     
 	
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerSize(SIZE_FOR_DIVIDER);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
		splitPane.setTopComponent(getDeviceRequestPane());
		splitPane.setBottomComponent(getDeviceResponsePane());
		splitPane.setResizeWeight(0.50);
		return splitPane;
	}

	/**
	 * This method initializes device Request Scrollpane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDeviceRequestPane() {
		JPanel aPanel = new JPanel();
		
		if (deviceRequestPane == null) {
			deviceRequestPane = new JScrollPane();
		}
		try{
			deviceRequestPane.getViewport().add(getRequestDataTable());
		} catch(Exception e){ //not able to access the DB
			deviceRequestPane.getViewport().add(new JButton("Data is NOT available"));
			e.printStackTrace();
		}
		//aPanel.setSize(new Dimension(100,200));
		aPanel.setPreferredSize(new Dimension(300,100));
		deviceRequestPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		deviceRequestPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		aPanel.setLayout(new BorderLayout());		
		aPanel.add(getTitle(requestPaneTitle),BorderLayout.NORTH);
		aPanel.add(deviceRequestPane,BorderLayout.CENTER);
		aPanel.add(getRequestButtonsPanel(), BorderLayout.SOUTH);
		
		return aPanel;
	}

	/**
	 * This method initializes device response Scrollpane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDeviceResponsePane() {
		JPanel aPanel = new JPanel();
		
		if (deviceResponsePane == null) {
			deviceResponsePane = new JScrollPane();		
		}
		JTable responseTable = getResponseDataTable();
		deviceResponsePane.setPreferredSize(new Dimension(300, 30));
		deviceResponsePane.getViewport().add(responseTable);
		deviceResponsePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		deviceResponsePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		aPanel.setLayout(new BorderLayout());		
		aPanel.add(getTitle(responsePaneTitle),BorderLayout.NORTH);
		aPanel.add(deviceResponsePane,BorderLayout.CENTER);		
		aPanel.add(getResponseControlPanel(), BorderLayout.SOUTH);
		
		return aPanel;
	}
	
	/*
	 * This is to collect the data from the request table
	 */
	private JTable getRequestDataTable(){
		
	    if(requestDataTable == null){
	        requestDataTable = new JTable();
	        requestDataTable.addMouseListener(new MouseInputAdapter() {
	            public void mousePressed(MouseEvent e) {
	                displayMessage("");
	            }
	        });
	    }
	    List<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
	    if(device != null) deviceFormats = device.getDeviceDataFormats();
	    DeviceFormatTableModel tm = new DeviceFormatTableModel(deviceFormats,requestDataTable);
        
	    requestDataTable.setModel(tm);
		return requestDataTable;
	}
	
	private JTable getResponseDataTable(){
	    if(responseDataTable == null){
	        responseDataTable = new JTable();
	        responseDataTable.addMouseListener(new MouseInputAdapter() {
                public void mousePressed(MouseEvent e) {
                    displayMessage("");
                }
            });
        }
        List<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
        if(device != null) deviceFormats = device.getReplyDeviceDataFormats();
        DeviceFormatTableModel tm = new DeviceFormatTableModel(deviceFormats,responseDataTable);
        
        responseDataTable.setModel(tm);
        
        return responseDataTable;
	}
	
	/*
	 * This method is to compose the button panel for the request table
	 */
	private JPanel getRequestButtonsPanel(){
		JPanel buttonsPanel = new JPanel();	
		buttonsPanel.add(getLoadButton());
		buttonsPanel.add(getSaveButton());
		buttonsPanel.add(getSendButton());
		buttonsPanel.add(getClearButton());
		return buttonsPanel;
	}
	
	private JPanel getResponseControlPanel(){
		JPanel resCtrPanel = new JPanel();
		resCtrPanel.setLayout(new BorderLayout());
		resCtrPanel.add(getResponseButtonsPanel(), BorderLayout.NORTH);
		resCtrPanel.add(getSimulatorServerPanel(), BorderLayout.CENTER);
		resCtrPanel.add(getTorqueServerPanel(), BorderLayout.SOUTH);
		
		return resCtrPanel;
	}
	/*
	 * This method is to compose the button panel for the response table
	 */
	private JPanel getResponseButtonsPanel(){
		JPanel buttonsPanel = new JPanel();	
		buttonsPanel.add(getClearResponseButton());		
		return buttonsPanel;
	}
	
	private JButton getLoadButton(){
		if (loadButton == null) {
			loadButton = new JButton("Load");
			loadButton.setName("Load");
		}
		return loadButton;
	}
	
	private JButton getSaveButton(){
		if (saveButton == null) {
			saveButton = new JButton("Save");
			saveButton.setName("SAVE");
		}
		return saveButton;
	}
	
	private JButton getSendButton(){
		if (sendButton == null) {
			sendButton = new JButton("Send");
			sendButton.setName("SEND");
		}
		return sendButton;
	}
	
	private JButton getClearButton(){
		if (clearButton == null) {
			clearButton = new JButton("Clear");
			clearButton.setName("CLEAR");
		}
		return clearButton;
	}
	
	private JButton getClearResponseButton(){
		if (clearResponseButton == null) {
			clearResponseButton = new JButton("Clear");
			clearResponseButton.setName("CLEAR_RESPONSE");
		}
		return clearResponseButton;
	}	
	
	private JPanel getTitle(JLabel titleLabel){
		JPanel titlePanel = new JPanel();
		titlePanel.add(titleLabel);
		return titlePanel;
	}
	


 
	/*
	 * This method is to collect the data entered by client
	 */
	private boolean collectRequestTableDataFromGUI(){
		boolean dataCompleted = true;
		if(device == null) return false;
		if ( requestDataTable!=null ){
			if( requestDataTable.getCellEditor()!=null ){
				requestDataTable.getCellEditor().stopCellEditing();
			}

			// change to support OPC
			// collect data for OPC if device type is OPC, otherwise
			// the works the same as before
			
			if(device.isOPCType()) {
				DataContainer aDC = collectTableDataForOPCDevice();
				dc.putAll(aDC);
			}else {
				dc = collectDataForEquipment();
			}
		}
		return dataCompleted;
	}

	private DataContainer collectDataForEquipment() {

		DataContainer aDC = new DefaultDataContainer();
        boolean isUnsolicitedDevice = isUnsolicitedDevice();
		for(DeviceFormat df : device.getDeviceDataFormats()) {
			if(df.getValue() != null || !isUnsolicitedDevice)
				aDC.put(df.getTag(), df.getValue());
		}
		return aDC;

	}
	
	private boolean isUnsolicitedDevice() {
		return device.getDeviceDescription().contains("unsolicited");
	}
	
	private DataContainer collectTableDataForOPCDevice() {
		
	    DataContainer aDC = new DefaultDataContainer();
		
	    for(DeviceFormat df : device.getDeviceDataFormats()) {
	    	if(df.getDeviceTagType().equals(DeviceTagType.TAG))
	    		aDC.put(df.getTagValue(), df.getValue() == null ? "": df.getValue());
		}
		return aDC;
		
	}

	private void clearDeviceData(){
	    if(device == null) return;
	    device.resetDeviceDataFormatValues();
	    requestDataTable.updateUI();
	    responseDataTable.updateUI();
	}
	
	/**
	 * This method send the data container object to the GALC applicaiton for the process
	 * @return DataContainer
	 */
	private DataContainer send(){
		try{
			log.info("Input data for DataContainer is:"+dc);
			
			ConfigValueObject cfgV = DeviceSimulatorConfigManager.getInstance().getConfig();
			DataContainer sDc = prepareSendData();
			
			if(cfgV.getServerType() == ServerType.Rest_Service){
				log.debug("Send rest service!");
				return (new RestServiceClient(getRestServiceUrl())).send(sDc);
			}
			
			return doSend(cfgV, sDc);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getRestServiceUrl() {
		String taskName = ServiceFactory.getDao(ApplicationTaskDao.class).findHeadlessTaskName(device.getIoProcessPointId());
		String ServiceName = (taskName == null || StringUtils.isEmpty(taskName)) ? DEFAULT_SERVICE_NAME : taskName.trim();
		return SimulatorConfig.getInstance().getRestServiceURL() + ServiceName + "/" + INVOKING_METHOD;
	}

	private DataContainer doSend(ConfigValueObject cfgV, DataContainer sDc)
			throws Exception {
		IEiSender sender = null;
		DataContainer resDC = null;
		
		int clnmode = cfgV.getServerClientMode() + 1;
		int clntype = cfgV.getServerClientType() + 1;
		
		if(device.isOPCType())
		{
			try {
				//if instance defined, then override client mode/type from the value in instance entry
				if (cfgV.isUseOPCServerClientCfg()){
					try {
						sender = new OpcEiSender(cfgV.getOpcInstanceName(),	device);
					    log.info("New OPC sender - OPC instance name: " + cfgV.getOpcInstanceName());
					} catch (Exception e) {
						sender = getSender(cfgV, clnmode, clntype);
					}
				} else {
					sender = getSender(cfgV, clnmode, clntype);
				}
			} catch (BaseException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
				throw e;					
			}				
			resDC = sender.send(sDc);
			
		} else {
			sender = getSender(cfgV, clnmode, clntype);
			resDC = sender.send(sDc);
		}
			
		//log.info("The result data for DataContainer is:"+resDC);
		return resDC;
	}


	/**
	 * Valid the OPC instance name is configured in Database
	 * @param opcInsName
	 * @return
	 */
	private boolean validOpcInstance(String opcInsName) {
	
	    List<OpcConfigEntry> opcCfgEntries;
		try {
			opcCfgEntries = getDao(OpcConfigEntryDao.class).findAllByOpcInstance(opcInsName);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return opcCfgEntries.size() > 0;

	}

	private IEiSender getSender(ConfigValueObject cfgV, int clnmode, int clntype) throws Exception {
		IEiSender sender;
		sender = new OpcEiSender(SimulatorConfig.getInstance().getDeviceURL(), clntype, clnmode, device.getClientId());
		log.info("New OPC sender - URL: " + SimulatorConfig.getInstance().getDeviceURL());
		return sender;
	}

	/**
	 * Prepare a data container to send GALC with right format
	 * @return
	 */
    private DataContainer prepareSendData() {
    	
    	dc.put(DataContainerTag.SENDER + "__HOST", DevSimulatorUtil.getLocalHostName() + 
    			":" + DevSimulatorUtil.getLocalHostIP());
    	
    	dc.put(DataContainerTag.APPLICATION_ID, device.getIoProcessPointId());
    	dc.put(DataContainerTag.PROCESS_POINT_ID, device.getIoProcessPointId());
    	dc.setClientID(device.getClientId());
    	return dc;

	}

 
	/**
     * This method is to populate the data container data into the response JTable.
     * @param dc
     */
    private void populateDataToRequestTable(DataContainer dc){
    	Device thisDevice = this.deviceSelector.getDevice(dc.getClientID());
		if(thisDevice == null) {
			System.out.println("process Data populate - cannot find device " + dc.getClientID() );
			return;
		}
		
		populateDataContainerToRequestTable(dc,thisDevice);
//        	if(device.isOPCType()) 
//        	    populateDataContainerToRequestTable(dc);
//        	else {
  //      		String dcString = (String)dc.get("DATA");
 //           	populateDataStringToRequestTable(dcString, dc.getClientID());
 //       	}
    }
    
    /**
     * populate response table from data container
     * @param dc
     */
    private void populateDataContainerToResponseTable(DataContainer dc)
    {       
        if(device == null) return;
       
        if(device.getDeviceType() == DeviceType.OPC|| 
        		device.getDeviceType() == DeviceType.EQUIPMENT||
        		device.getDeviceType() == DeviceType.DEVICE_WISE)
        	populateOpcDataContainerToResponseTable(dc);
        else 
        	populateEquipementDataContainerToResponseTable(dc);
        	
        responseDataTable.updateUI();
    }

	private void populateEquipementDataContainerToResponseTable(DataContainer dc) {
		for(DeviceFormat df : device.getReplyDeviceDataFormats()) {
            Object value = dc.get(df.getTag());
            if(value == null) value = dc.get(df.getId().getTag());
            df.setValue(value == null ? null : value.toString());
        }
		
	}

	private void populateOpcDataContainerToResponseTable(DataContainer dc) {
		for(DeviceFormat df : device.getReplyDeviceDataFormats()) {
            Object value = device.isOPCType() ? dc.get(df.getTagValue()):dc.get(df.getTag());
            if(value == null) value = dc.get(df.getId().getTag());
            df.setValue(value == null ? null : value.toString());
        }
	}
    
    /**
     * populate response table from data container
     * @param dc
     */
    private void populateDataContainerToRequestTable(DataContainer dc,Device thisDevice)
    {       
        boolean isWriteDevice = thisDevice.isOPCType() || thisDevice.getDeviceType() == DeviceType.EQUIPMENT;
        
    	for(DeviceFormat df : thisDevice.getDeviceDataFormats()) {
        	
            Object strValue = isWriteDevice? dc.get(df.getTag()) : dc.get(df.getTagValue());
            if(StringUtils.equalsIgnoreCase("NULL", strValue.toString())) df.setValue(null);
            df.setValue(strValue.toString());
        }
    	
    	if(device != null && device == thisDevice) {
        
    		int rowCount = requestDataTable.getRowCount();

		    for (int i=0; i<rowCount; i++) {
		    	String tagOrTagvalue = ((String)requestDataTable.getValueAt(i, 0)).trim();
		    	String strValue = (String)dc.get(tagOrTagvalue);
		    	if(StringUtils.equalsIgnoreCase("NULL", strValue)) strValue = null; 
		             
		    	requestDataTable.setValueAt(strValue, i, 2);
		    }
		    
			requestDataTable.repaint();
		    
    	}
    }
    

	private String getEntry(){
		return ITagValueSource.DEFAULT_ENTRY+DOT;
	}
	
	/**
	 * This method is to get the tag prefix for saving test data into the property file.
	 * @return String
	 */
	private String getTagPrefix(){
		return getEntry()+TAG+DOT;
	}
	
	/**
	 * This method is to get the HttpClient instance
	 * @return ClientSideNetworkClient
	 * @throws MalformedURLException
	 */
//	private HTTPClient getHttpClient() throws MalformedURLException{
//		if (_HTTPClient ==null){
//			_HTTPClient = new HTTPClient(dispatcherURL);
//		}
//		return _HTTPClient;
//	}
	
	public DataContainer processDataContainer(DataContainer data)
	{		
        if (data == null) return null;  
       
        if(DataContainerCommonUtil.isEquipmentRead(data))
        	return processDataRead(data);
        if(data.getClientID().trim().equalsIgnoreCase(repClientID))
        	populateDataContainerToResponseTable(data);
        else populateDataToRequestTable(data);	
        
        ConfigValueObject cfgV = DeviceSimulatorConfigManager.getInstance().getConfig();
        if(cfgV != null && cfgV.getResponseDc() != null)
        	return cfgV.getResponseDc();
        else return null;
	}
	
	public DataContainer processDataRead(DataContainer data) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(data.getClientID());
		Device device = this.deviceSelector.getDevice(data.getClientID());
		if(device == null) {
			System.out.println("process Data read - cannot find device " + data.getClientID() );
			return dc;
		}
		
		for(DeviceFormat deviceFormat : device.getDeviceDataFormats()) {
			if(deviceFormat.getValue() != null) {
				dc.put(deviceFormat.getTag(),deviceFormat.getValue());
			}
		}
		
		return dc;
		
	}


	public SimulatorServerPanel getSimulatorServerPanel() {		
		
		return simulatorServerPanel;
	}	

	 private class DeviceFormatTableModel extends SortableTableModel<DeviceFormat>{
	     private static final long serialVersionUID = 1L;
         
         public DeviceFormatTableModel(List<DeviceFormat> deviceDataFormats, JTable table){
             super(deviceDataFormats,new String[] {"Tag", "Length", "Value"},table);
         }
         
         public boolean isCellEditable (int row, int column){
             return column == 2;
         }
         
         public Object getValueAt(int rowIndex, int columnIndex) {
             if(rowIndex >= getRowCount()) return null;
             DeviceFormat deviceFormat = items.get(rowIndex);
             switch(columnIndex){
                 case 0: return deviceFormat.getTag().trim();
                 case 1: return deviceFormat.getLength();
                 case 2: return deviceFormat.getValue();
             }
             return null;
         }
         
         public void setValueAt(Object value, int row, int column) {
             if(row >= getRowCount() || column != 2) return;
             DeviceFormat deviceFormat = items.get(row);
             String val = (String) value;
             val = StringUtils.isEmpty(val) ? null : val;
             deviceFormat.setValue(val);
             
         }
	 }


	 public TorqueServerPanel getTorqueServerPanel() {
		 if(torqueServerPanel == null){
			 torqueServerPanel = new TorqueServerPanel();
		 }
		 return torqueServerPanel;
	 }

	 private JButton getTorqueSendButton() {
		 return getTorqueServerPanel().getSendTorqueButton();
	 }

	 protected DataContainer getTorque() {
		 return getTorqueServerPanel().getTorque();
	 }
}

