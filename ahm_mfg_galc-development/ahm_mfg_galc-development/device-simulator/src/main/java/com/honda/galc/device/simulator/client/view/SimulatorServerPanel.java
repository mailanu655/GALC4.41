package com.honda.galc.device.simulator.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.device.simulator.client.ui.GalcPanel;
import com.honda.galc.device.simulator.client.view.cfg.DeviceSimulatorConfigManager;
import com.honda.galc.device.simulator.data.DataContainerProcessor;
import com.honda.galc.device.simulator.data.ITagValueSource;
import com.honda.galc.device.simulator.server.SimulatorServerManager;
import com.honda.galc.device.simulator.server.SimulatorSocketServer;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;
import com.honda.galc.entity.conf.Device;

import static com.honda.galc.service.ServiceFactory.getDao;

import javax.swing.JButton;

/**
 * <h3>Class description</h3>
 * This class defines a Simulator Server Panel 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Aug 9 2007</TD>
 * <TD>1.0</TD>
 * <TD></TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * <br>
 * @ver 1.0 
 * @author P.Chou
 */

public class SimulatorServerPanel extends GalcPanel implements IDeviceSelectable {	
	
	private static final long serialVersionUID = 1L;
    ITagValueSource tagValueSource;
	
	private final static String EMPTY_STRING = "";  
	private final static int BUTTON_WIDTH = 65;	
	private final static int BUTTON_HIGHT = 20;
	private Logger log = Logger.getLogger(SimulatorServerPanel.class);  //  @jve:decl-index=0:
	
	private JPanel simulatorServerPanel;
	private JLabel jLabelServerPort = null;
	private JTextField jTextServerPort = null;
	private JButton jButtonNew = null;
	private JLabel jLabelStatus = null;
	private JLabel jLabelResponse = null;
	private JTextField jTextResponse = null;  //  @jve:decl-index=0:visual-constraint="33,44"
	private JPanel jPanelResPath = null;
	private JPanel jPanelServer = null;
	private JPanel jPanelServerControl = null;
	private JPanel jPanelServerStatus = null;
	private JButton jButtonLoadResp = null;
	private JButton jButtonStart = null;
	private JButton jButtonStop = null;
	private DataContainerProcessor processor = null;
	private JLabel jLabelStatusValue = null;
	private JButton jButtonList = null;
	private String division = null;
	private boolean useXML;
	
	/* device ID */
	private Device device;
	
	/* current reply client Id */
	private String repClient;  //  @jve:decl-index=0:
	
	/* current server */
	private SimulatorSocketServer server;  //  @jve:decl-index=0:	
	SimulatorServerManager _servers;
	
	private List<Device> devices = new ArrayList<Device>();
	private boolean bNewServer;
		
	public SimulatorServerPanel() {
		super();
		initialize();
	}
	
	/**
	 * This method initializes Server Panel
	 * 
	 */
	private void initialize() {
		if(_servers == null)
			_servers = new SimulatorServerManager();
		
		setLayout(new BorderLayout());
		add(this.getSimulatorServerPanel(), BorderLayout.CENTER);
		addListeners();
		
		if(DataContainerTag.EQUIPMENT_TYPE_OPC.equals(DeviceSimulatorConfigManager.getInstance().getConfig().getEiType()))
			useXML = true;
		else
			useXML = false;
	}

	public void setDevice(Device device) {
		this.device = device;
		this.bNewServer = false;
		this.getSimulatorServerPanel();
	}
	/**
	 * This method is to add listeners
	 */
	private void addListeners(){
		getJButtonStart().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
				displayMessage("");
			}
		});
		getJButtonStop().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopServer();
				displayMessage("");
			}
		});
		getJButtonNew().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newServer();
				displayMessage("");
			}
		});
		getJButtonList().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listServers();
				displayMessage("");
			}
		});
		getJButtonLoadResp().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMessage("");
				loadRespFile();				
			}
		});
	}
	
	/*
	 * This method is to compose simulator Server panel
	 */
	private JPanel getSimulatorServerPanel(){
		if(simulatorServerPanel == null){	
		
			simulatorServerPanel = new JPanel();
			TitledBorder border = new TitledBorder("Simulator Equipment Server");
			simulatorServerPanel.setBorder(border);
			simulatorServerPanel.setLayout(new FlowLayout());			
			simulatorServerPanel.add(getJPanelServer());		
			simulatorServerPanel.add(getJPanelServerControl());
			
		}
		
		//reset server to get rid of the previous client's server
		server = null;
		
		// find/create simulator server
		getSimulatorServer();
		
		//populate Server Panel
		populateServerPanel();
		
		return simulatorServerPanel;
	}
	
	/**
	 * Check if a server for a reply client exist. if it is, then
	 * retrieve the server otherwise create a new server
	 * 
	 */
	private void getSimulatorServer() {
		//reset server
		server = null;
		repClient = null;
		
		if(device == null) return;
		
       
      	if(isNeedServer(device))
		{
			//Reply device is defined need a server object
			//Check if the server is already exist
			repClient = device.getClientId().trim();
			int iPort = device.getEifPort();
			
			Object oServer = _servers.findServerOnPort(iPort);
			if( oServer != null)
			{
				server = (SimulatorSocketServer)oServer;				
			}
			else
			{	
				//use XML if it is OPC server
				server = new SimulatorSocketServer(iPort, useXML);
			    _servers.add(server.getPort(), server);				
			}
		}		
	}
	/**
	 * populate the server panel 
	 *
	 */
	public void populateServerPanel()
	{
		if(server == null && !bNewServer)
		{
			//clean up 
			getJTextServerPort().setText("");
			getJTextResponse().setText("");	
			URL img = getClass().getClassLoader().getResource("resource/blank.GIF");
			ImageIcon icon = new ImageIcon(img,"");
			jLabelStatusValue.setIcon(icon);
			
			setServerFieldEnabled(false);
		}
		else
		{
			getJTextServerPort().setText(EMPTY_STRING+server.getPort());
			getJTextResponse().setText(server.getResProp());
			
			setStatus(server.isRunning());
			setServerFieldEnabled(true);
		}
		
	}

	/**
	 * Enable/Disable sever attribute fields
	 * @param b
	 */
	private void setServerFieldEnabled(boolean b) {
		getJTextServerPort().setEnabled(b);
		getJTextServerPort().setEditable(b);
		
		getJTextResponse().setEditable(b);
		getJTextResponse().setEnabled(b);	
		
	}
	
	/**
	 * Start a server to wait for connection
	 *
	 */
	private void startServer()
	{
		if(server != null && !bNewServer)
		{
			startClientServer();
		}
		else if(repClient == null && bNewServer)
		{
			addNewServer();
		}
		else
		{
			displayError("Simulator Server is not defined properly!");
		}
	}

	/**
	 * Add a new server which does not belong to a client
	 */
	private void addNewServer() {
		if(!validPort())
		{
			JOptionPane.showMessageDialog(
					getParent(),
					"Server port: " + getJTextServerPort().getText().trim() + " is out of range.",
					"Invalid Server Port",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String sPort = getJTextServerPort().getText().trim();
		int iPort = Integer.valueOf(sPort).intValue();
		
		if(_servers.findServerOnPort(sPort) != null)
		{
		   server = (SimulatorSocketServer)_servers.findServer(sPort, useXML);
		}
		else
		{
			server = new SimulatorSocketServer(iPort, useXML);
            //still we need to track the status of this server
			//use port as key because this server does not has client ID
			_servers.add(server.getPort(), server);
			
		}
		Device aDevice = new Device();
		aDevice.setClientId(sPort);
		aDevice.setEifPort(iPort);
		devices.add(device);
		startClientServer();
		
	}

	/**
	 * start a server for a client
	 */
	private void startClientServer() {
		if(server.isRunning())
		{
			log.info("Equipment Server is already running ...");
			return;
		}
		
		//get the param from UI 
		String sPath = getJTextResponse().getText().trim();
		if(sPath != null && sPath.length() != 0)
		   server.setResProp(sPath);
				
		server.setDcProcessor(processor);
		server.startServer();	
				
		//update server status on display
		populateServerPanel();
	}
	

	/**
	 * Stop a server to wait for connection
	 *
	 */
	private void stopServer()
	{
		if(server != null)
		{			
			server.stopRunning();			
			
			//update server status on display
			populateServerPanel();		
			
		}
		else
		{
			displayError("stopServer: no server is defined!");
		}
	}
	
	/**
	 * create a new server
	 *
	 */
	private void newServer()
	{
		//reset server
		server = null;
		repClient = null;
		device = null;
		getJTextServerPort().setText("");
		getJTextResponse().setText("");	
		bNewServer = true;
		
		setServerFieldEnabled(true);
		
	}


	/**
	 * This method initializes jTextServerPort	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextServerPort() {
		if (jTextServerPort == null) {
			jTextServerPort = new JTextField();
			jTextServerPort.setDisabledTextColor(Color.gray);
			jTextServerPort.setSelectedTextColor(Color.white);
			jTextServerPort.setPreferredSize(new Dimension(80, 20));			
		}
		return jTextServerPort;
	}

	/**
	 * This method initializes jToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JButton getJButtonNew() {
		if (jButtonNew == null) {
			jButtonNew = new JButton();
			jButtonNew.setPreferredSize(new Dimension(40, 20));			
			jButtonNew.setToolTipText("start a new Server");
			jButtonNew.setText("New");
		}
		return jButtonNew;
	}


	/**
	 * This method initializes jTextResponse	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextResponse() {
		if (jTextResponse == null) {
			jTextResponse = new JTextField();
			jTextResponse.setPreferredSize(new Dimension(100, 20));
		}
		return jTextResponse;
	}
	
	/**
	 * Check if a simulator Server may needed for a reply device 
	 *
	 * @param dev
	 * @return
	 */
	public boolean isNeedServer(Device dev)
	{ 
		 //It's does not make sense to create a server that 
		 //there will be no client to connect to it, so
		 //EIF host must be localhost or 127.0.0.1 
		 //OR it may be the IP of the local machine
		 String sIP = dev.getEifIpAddress().trim();
		 
		 //we don't define a server if no EI IP defined
		 if(sIP == null)
			 return false;
		 
		 if(!DevSimulatorUtil.isLocalIP(sIP))
			 return false;
		 
		 //Port must be defined
		 Integer iPort = dev.getEifPort();
		 if(iPort == null || iPort.intValue() == 0)
			 return false;
		 
		return true;
	}
	
	

	public DataContainerProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(DataContainerProcessor processor) {
		this.processor = processor;
	}

	
	/**
	 * This method initializes jPanelServer	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServer() {
		if (jPanelServer == null) {
			jPanelServer = new JPanel();
			TitledBorder border = new TitledBorder("Server Attributes");
			jPanelServer.setBorder(border);	
			jPanelServer.setLayout(new BorderLayout());				
			jPanelServer.add(getJPanelServerStatus(), BorderLayout.NORTH);
			jPanelServer.add(getJPanelResPath(), BorderLayout.CENTER);
			
		}
		return jPanelServer;
	}

	/**
	 * This method initializes jPanelServerControl	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServerControl() {
		if (jPanelServerControl == null) {
			jPanelServerControl = new JPanel();
			TitledBorder tBorder = new TitledBorder("Server Control");
			jPanelServerControl.setBorder(tBorder);
			jPanelServerControl.setLayout(new GridLayout(2, 2, 10, 10));
			jPanelServerControl.add(getJButtonStart());
			jPanelServerControl.add(getJButtonStop());
			jPanelServerControl.add(getJButtonNew());
			jPanelServerControl.add(getJButtonList(), null);
			jPanelServerControl.add(getJButtonList());
		}
		return jPanelServerControl;
	}
	
	/**
	 * This method initializes jPanelServerControl	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServerStatus() {
		if (jPanelServerStatus == null) {
			jLabelStatusValue = new JLabel();
			URL imgURL = getClass().getClassLoader().getResource("resource/blank.GIF");
			ImageIcon icon = new ImageIcon(imgURL, "");
			jLabelStatusValue.setIcon(icon);
			jPanelServerStatus = new JPanel();
			jPanelServerStatus.setLayout(new FlowLayout());	
			
			//Port
			jLabelServerPort = new JLabel();
			jLabelServerPort.setText("Port:");
			jPanelServerStatus.add(jLabelServerPort, null);
			jPanelServerStatus.add(getJTextServerPort(), null);
			
			//status
			jLabelStatus = new JLabel();
			jLabelStatus.setText("Status:");
			jPanelServerStatus.add(jLabelStatus, null);
			jPanelServerStatus.add(jLabelStatusValue, null);
		}
		return jPanelServerStatus;
	}
	
	/**
	 * This method initializes jPanelResPath	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelResPath() {
		if (jPanelResPath == null) {
			jPanelResPath = new JPanel();
			jPanelResPath.setLayout(new FlowLayout());
			
			jLabelResponse = new JLabel();
			jLabelResponse.setText("Response:");
			jPanelResPath.add(jLabelResponse, null);
			jPanelResPath.add(getJTextResponse(), null);			
			
			//load button
			jPanelResPath.add(getJButtonLoadResp(), null);
		}
		return jPanelResPath;
	}

	/**
	 * This method initializes jButtonResp	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonLoadResp() {
		if (jButtonLoadResp == null) {
			jButtonLoadResp = new JButton();
			jButtonLoadResp.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HIGHT));
			jButtonLoadResp.setText("Load");
		}
		return jButtonLoadResp;
	}

	/**
	 * This method initializes jButtonStart	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			jButtonStart.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HIGHT));
			jButtonStart.setText("Start");
		}
		return jButtonStart;
	}

	/**
	 * This method initializes jButtonStop	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStop() {
		if (jButtonStop == null) {
			jButtonStop = new JButton();
			jButtonStop.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HIGHT));
			jButtonStop.setText("Stop");
		}
		return jButtonStop;
	}
    /**
     * set status value icon
     *
     */
    public void setStatus(boolean bR)
    {
    	ImageIcon icon;
    	if(bR)
    	{
			URL imgURL = getClass().getClassLoader().getResource("resource/running.GIF");			
    		icon = new ImageIcon(imgURL, "server is running");
    	}   		
    	else
    	{
			URL imgURL = getClass().getClassLoader().getResource("resource/running.GIF");			
    		icon = new ImageIcon(imgURL, "server is stopped");
    	}
    	jLabelStatusValue.setIcon(icon);
    } 

	/**
	 * This method initializes jButtonList	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonList() {
		if (jButtonList == null) {
			jButtonList = new JButton();
			jButtonList.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HIGHT));
			jButtonList.setText("List...");
		}
		return jButtonList;
	}
	
	private String loadRespFile()
	{
		String sFile = null;
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle ("Open File");
        fc.setFileSelectionMode ( JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog (this);
        if (ret != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File loadedFile = fc.getSelectedFile();
        sFile = loadedFile.getPath();       
        
        //Show File Path
        getJTextResponse().setText(sFile);
        //Update Server 
        server.setResProp(sFile);
        
		return sFile;
	}
	
	/**
	 * list all of the running servers
	 *
	 */
	public void listServers()
	{
		java.awt.Container parentScreen = this.getRootPane().getParent();
		//release the reference of current server so let the
		//manager be able to stop/remove the server
		int port = 0;
		if(server != null)
		   port = server.getPort();
		
		server = null;
		
		//populate the server manager client list
		populateClients();		
		SimulatorServerListView serverList = new SimulatorServerListView(_servers, devices);
		serverList.setLocationRelativeTo(parentScreen);
		serverList.setModal(true);
		serverList.setVisible(true);	
		
		//set the current server
		if(port != 0)
		server = _servers.findServerOnPort(port);
		//update the current view panel	
		populateServerPanel();
	}
	
	/**
	 * Check if the input server port is a valid port
	 * @return
	 */
	private boolean validPort()
	{
		
		String sPort = getJTextServerPort().getText().trim();
		if(sPort == null || sPort.length() == 0 )
			return false;
		
		int iPort = Integer.valueOf(sPort).intValue();
		if(iPort < 0 || iPort > 0xFFFF)
		{
			log.info("Port value out of range: " + iPort);
			return false;
		}
		
		return true;
	}	
	
	/**
	 * populate the clients list to includes all the clients under 
	 * the same division
	 *
	 */
	public void populateClients()
	{
		if(device == null)
			return;
		
		String sDivision = device.getDivisionId();
		if(!sDivision.equals(division))
		{
			division = sDivision;
			
			List<Device> deviceList = getDao(DeviceDao.class).findAllByDivisionId(sDivision);

			//populate the client list only with reply clients which
			//have EI IP as local and have valid port
			for(Device d: deviceList)
			{
				String sClient = d.getClientId();			
//				if(sRClient == null || sRClient.length() == 0)
//					continue;
				
				if(sClient == null)
					continue;
				
				
				if (true)//if(isNeedServer(resDevice))
				{
					devices.add(d);
				}
			
			}//for
		}//if		
	}//populateClients


}
