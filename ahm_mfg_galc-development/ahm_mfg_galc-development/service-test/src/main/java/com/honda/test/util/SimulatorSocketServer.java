package com.honda.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.net.BufferedInputStream;
import com.honda.galc.net.NotificationRequest;
import com.honda.galc.net.Request;

 
public class SimulatorSocketServer{
	private ServerSocket serverSocket = null;
	private boolean running = false;
	private boolean useXML = true;

	private List<DataContainer> outputDCList = new ArrayList<DataContainer>();
	
	private Stack<DataContainer> receiveDCList = new Stack<DataContainer>();
	
	private List<NotificationRequest> receivedRequestList = new ArrayList<>();
	
	int port = 5555;
//	private static final String END_DATACONTAINER_XML = "/" + DataContainerXMLUtil.DATA_CONTAINER_XML_TAG;
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
	

    /**
     * Create TCP Server
     *
     */
	private void createServerSocket() {
	    closeSocket();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
		}
	}

	/**
	 * Tell the serverSocket to start accepting incoming requests.
	 *
	 */	
	public void acceptMessage() {
        try {
			while(isRunning()) {
				Socket socket = serverSocket.accept();
				if(socket != null) {
					handleRequest(socket);
				}
			}

		} catch (IOException e) {
			if(running) {
			}
		}		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void startServer() {
//       setPriority(Thread.NORM_PRIORITY);
        this.createServerSocket();
        
        if(serverSocket == null) {
            setRunning(false);
            return;
        } else {
            setRunning(true);
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
		outputDCList.clear();
		receiveDCList.clear();
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
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
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
				 else processObject(bis);
			} catch (Exception e) {
			}
			
		} catch (SystemException se) {
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

		System.out.println("Server Received: " + data);			
	
		DataContainer rData= new DefaultDataContainer();
		rData.setClientID(data.getClientID());
        
		if(DataContainerUtil.isEquipmentRead(data)) {
			if(!outputDCList.isEmpty()) {
				DataContainer rDC = findDC(outputDCList,data.getClientID());
				if(rDC != null) {
					rDC.setClientID(data.getClientID());
					rData = rDC;
				}
			}
		}else {
			receiveDCList.add(data);
			
			//write back a response			
			rData.put(DataContainerTag.DATA, "1");
			rData.put(DataContainerTag.CLIENT_ID, "OPCACK");
		}

		DataContainerXMLUtil.convertToXML(rData, os);
		os.flush();
	}
	 
	private void processObject(BufferedInputStream bis){
	    Object obj = readObject(bis);
	    if(obj == null) return;
	    if(obj instanceof NotificationRequest) receivedRequestList.add((NotificationRequest)obj);
	}
	
	private Object readObject(BufferedInputStream bis) {
    	ObjectInputStream ois = null;
        
    	try {
	    	ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger().error(e, "Exception occured when reading object from socket");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Logger.getLogger().error(e, "Exception occured when reading object from socket");
		}finally{
			try {
                if(ois != null) ois.close();
            } catch (IOException e) {
            	Logger.getLogger().error(e, "Exception occured when closing object input stream");
            }
		}
	
		return null;
    }
	 
	private DataContainer findDC(List<DataContainer> dcList, String clientId) {
		
		for(DataContainer dc:dcList) {
			if(dc.getClientID().equalsIgnoreCase(clientId)) {
				dcList.remove(dc);
				return dc;
			}
		}
		
		return null;
	}
	 
	public DataContainer getReceivedData(String clientId) {
		return findDC(receiveDCList,clientId);
	}
	
	public List<NotificationRequest> getReceivedRequestList() {
		return this.receivedRequestList;
	}
	
	public  NotificationRequest getRequest(Class<?> notificationClass, String command) {
		for(NotificationRequest request : getReceivedRequestList()) {
			if(request.getTargetClass().equalsIgnoreCase(notificationClass.getName()) && 
			   command.equalsIgnoreCase(request.getCommand())) return request;
		}
		return null;
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
	
	public void add(DataContainer dc) {
		outputDCList.add(dc);
	}
	
	public void addAll(List<DataContainer> dcList) {
		outputDCList.addAll(dcList);
	}
}
