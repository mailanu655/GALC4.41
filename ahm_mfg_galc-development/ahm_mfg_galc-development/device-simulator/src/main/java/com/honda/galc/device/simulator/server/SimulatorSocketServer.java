package com.honda.galc.device.simulator.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.data.DataContainerCommonUtil;
import com.honda.galc.device.simulator.data.DataContainerProcessor;
import com.honda.galc.net.BufferedInputStream;

/**
 * <h3>Class description</h3>
 * This class defines a TCP/IP server to received data from GALC.
 * It functions similar to the EI TCP server on BL Socket 
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
 * <TD>Aug 8, 2007</TD>
 * <TD>1.0</TD>
 * <TD></TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 * @see 
 * <br>
 * @ver 1.0 
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class SimulatorSocketServer{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private boolean running = false;
	private DataContainerProcessor dcprocessor;
	private boolean useXML = false;

	int port = 5555;
	String resProp = null;
//	private static final String END_DATACONTAINER_XML = "/" + DataContainerXMLUtil.DATA_CONTAINER_XML_TAG;
	private Logger log = Logger.getLogger(SimulatorSocketServer.class);

	/**
	 * Constructors
	 *
	 */
	public SimulatorSocketServer() {	
		super();		
	}
	public SimulatorSocketServer(int port, boolean useXML) {	
		super();
		this.port = port;
		this.useXML = useXML;
	}
	
	public SimulatorSocketServer(int port, boolean useXML, String path) {	
		super();
		this.port = port;
		this.useXML = useXML;
		this.resProp = path;
	}

    /**
     * Create TCP Server
     *
     */
	private void createServerSocket() {
	    closeSocket();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}

	/**
	 * Tell the serverSocket to start accepting incoming requests.
	 *
	 */	
	public void acceptMessage() {

		try {
			while(isRunning()) {
				socket = serverSocket.accept();
				if(socket != null) {
					handleRequest(socket);
				}
			}

		} catch (IOException e) {
			if(running) {
				log.info(e.getMessage());
			}
		}		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void startServer() {
	    log.info("Starting Simulator Server......");
 //       setPriority(Thread.NORM_PRIORITY);
        this.createServerSocket();
        
        if(serverSocket == null) {
            log.info("ServerSocket is null. Please make sure the server is initialized properly.");
            setRunning(false);
            return;
        } else {
            setRunning(true);
            log.info("Simulator Server is waiting for connections.");
        }

        startSocketThread();
	}
	
	
  
	private void startSocketThread() {
        SocketThread thread = new SocketThread();
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(true);
        thread.start();
    }
    /**
	 * Stop the server.
	 *
	 */
	public void stopRunning() {		
		setRunning(false);
		closeSocket();
		log.info("Simulator Server at port:" + port + " stopped.");
	}
	
	private void closeSocket() {
	       try {
	            if(serverSocket != null){
	                serverSocket.close();
	                serverSocket = null;
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;		
	}

	/**
	 *  Compare if the two servers are the same based on
	 *  server port number
	 *  
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof SimulatorSocketServer))
			return false;
		if(((SimulatorSocketServer)obj).getPort() != this.port || 
			((SimulatorSocketServer)obj).isUseXML() != this.useXML)
			return false;	
		
		return true;
	}
	/**
	 * 
	 */
	@Override
	public int hashCode()
	{
		return this.port;
	}
	
	public DataContainerProcessor getDcProcessor() {
		return dcprocessor;
	}


	public void setDcProcessor(DataContainerProcessor handler) {
		this.dcprocessor = handler;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getResProp() {
		return resProp;
	}
	
	public void setResProp(String resProp) {
		this.resProp = resProp;
	}
	
	/**
	 * Convert a properties to a DataContainer
	 * @param pP
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataContainer propertiesToDataContainer(Properties pP)
	{
		DefaultDataContainer dContainer = new DefaultDataContainer();
		
		for(Enumeration e = pP.propertyNames(); e.hasMoreElements();)			
		{
			String sKey = (String)e.nextElement();
			String sValue = pP.getProperty(sKey);
			dContainer.put(sKey, sValue);
		}

		return dContainer;
	}
	/**
	 * process request data
	 * @param socket
	 */
	private void handleRequest(Socket socket)
	{	
		try{			
			BufferedInputStream bis = null;
	    	try {
	    		InputStream is = socket.getInputStream();
	    		OutputStream os = socket.getOutputStream();
	    		bis = new BufferedInputStream(is);
				if(bis.isXML()) 
					handleXMLRequest(bis, os);
				else handleContainerRequest(is,os);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			
		} catch (SystemException se) {
			log.info("Unexpected System exception processing socket request:" + port + se);			
			se.printStackTrace();
		}
	}//handleRequest
	
	/**
	 * Handle xml request and prepare for reply data container
	 * @param socket
	 * @throws IOException
	 * @throws SystemException
	 */
	 private void handleXMLRequest(InputStream is, OutputStream os) throws IOException, SystemException {
		
		DataContainer data;

		data = DataContainerXMLUtil.readDeviceDataFromXML(is);

		log.info("Server Received: " + data);			
		DataContainer rsDc = null; 
		if(dcprocessor != null)	
			rsDc = dcprocessor.processDataContainer(data);

		DataContainer rData= new DefaultDataContainer();
        
		if(DataContainerCommonUtil.isEquipmentRead(data)) {
			rData = rsDc;
		}else {
		
			//write back a response			
			rData.put(DataContainerTag.DATA, "1");
			rData.put(DataContainerTag.CLIENT_ID, "OPCACK");
        
			if(resProp != null && resProp.length() != 0)
			{
				rData = loadResponseProperties(data);

			}//if
		}

		DataContainerXMLUtil.convertToXML(rData, os);
		os.flush();
	}
	
	/**
	 * Handle data container request
	 * @param socket
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void handleContainerRequest(InputStream is, OutputStream os) throws IOException, ClassNotFoundException {
		DataContainer data;
		ObjectOutputStream oout =new ObjectOutputStream(os); 
		ObjectInputStream oin = new ObjectInputStream(is);

		data = (DataContainer)oin.readObject();
		log.info("Server Received: " + data);		
		dcprocessor.processDataContainer(data);

		//Properties in the reponse properties file will be extract 
		//and put into Data Container and write back.
		//Empty Data Container is sent if the response properties file
		//is not defined. 
		DataContainer rData= new DefaultDataContainer();
		if(resProp != null && resProp.length() != 0)
		{
			rData = loadResponseProperties(data);

		}//if
		oout.writeObject(rData);
		oout.flush();
	}
	/**
	 * Load reponse data from a properties file. If client ID is not
	 * defined in properties file use the one passsed e.g. from request
	 * @return
	 */
	private DataContainer loadResponseProperties(DataContainer dc) {
		DataContainer rData = new DefaultDataContainer();
		//load response properties file
		File responseFile = new File(resProp);
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(responseFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("Error finding response properties file: " + resProp); 
		}
		Properties prop = new Properties();
		try {
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error loading the properties:" + resProp); 
		}

		//Add properties to the Container
		rData = propertiesToDataContainer(prop);
		
		//If client Id is not defined use request client id
		if(rData.getClientID() == null)
			rData.setClientID(dc.getClientID());
		
		return rData;
	}
	public boolean isUseXML() {
		return useXML;
	}
	public void setUseXML(boolean useXML) {
		this.useXML = useXML;
	}
	
	private class SocketThread extends Thread {
	    
	    public void run() { 
	        acceptMessage();
	    }
	}
}
