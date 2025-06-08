package com.honda.galc.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>TcpSocket</code> is ...
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
 * <TD>P.Chou</TD>
 * <TD>Oct 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
public class SocketClient {
	
	private Socket socket = null;
	
	private String hostname;
	private int port;
	private int connectTimeout = 3000; //same as OPC default 
	private int sotimeout = 3000;
	private boolean newSocket = true;
	
	private BufferedReader bufferedReader = null;
	private BufferedWriter bufferedWriter = null;
	
	private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    
    public SocketClient(Socket socket) {
    	super();
    	if (socket == null) {
    		throw new IllegalArgumentException("Invalid socket: socket is null.");
    	}
    	this.hostname = socket.getInetAddress().getHostAddress();
    	this.port = socket.getPort();
    	this.socket = socket;
    }
    
    public SocketClient(String hostname, int port) {
    	super();
		this.hostname = hostname;
		this.port = port;
		createSocket();
    }
    
	public SocketClient(String hostname, int port, int connectTimeout) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.connectTimeout = connectTimeout;
		
		createSocket();
		
	}

	public SocketClient(String hostname, int port, int connectionTimeout,
			int sotimeout) {
		
		this.hostname = hostname;
		this.port = port;
		this.connectTimeout = connectionTimeout;
		this.sotimeout = sotimeout;
		
		createSocket();
	}

	public boolean isConnected() {
		
		return socket != null && socket.isConnected() && !socket.isClosed() && 
		         !socket.isInputShutdown() && !socket.isOutputShutdown();

	}
	
	public boolean isValid(){

		if(socket == null) {
			logMessage("Invalid socket: socket is null.");
			return false;
		}
		
		if(socket.isClosed() || 
				!socket.isConnected() || !socket.isBound() ||
				socket.isInputShutdown() || socket.isOutputShutdown())
		{
			
			logMessage("Invalid socket:" + " isClosed:" + socket.isClosed() + " isConnected:" +
					socket.isConnected() + " isBound:" + socket.isBound() + " isInputShutdown:" +
					socket.isInputShutdown() + " isOutputShutdown:" + socket.isOutputShutdown());
			return false;
		}
		else 
			return true;
	}

	protected void createSocket() throws ServiceTimeoutException{
        try {
            socket = TCPSocketFactory.getSocket(hostname, port, connectTimeout);
            socket.setReuseAddress(true);
        } catch (Exception e) {
            throw createServiceTimeoutException(e);
        }    
    }
	
	public SocketClient getSocketClient(){
		if (!isValid())
		{
			closeConnections();
			try {
				logMessage("Connecting to " + hostname + " port: " + port);
				socket = new Socket();
				SocketAddress endpoint = new InetSocketAddress(hostname, port);
				socket.connect(endpoint, connectTimeout);
				socket.setSoTimeout(sotimeout);
				setNewSocket(true);
			} catch (Exception e) {
				e.printStackTrace();
				socket = null;
				return null;
			}			
		}
		
		return this;
	}
	
	public BufferedReader getBufferedReader()
	{
		try
		{
			if (bufferedReader == null)
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception ex)
		{
			bufferedReader = null;
			ex.printStackTrace();
			logMessage("Unable to get an Buffered Reader " + " : " + ex.getStackTrace());
		}
		
		return bufferedReader;
	}
	
	
	public BufferedWriter getBufferedWriter()
	{
		try
		{
			if (bufferedWriter == null)
				bufferedWriter = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logMessage(ex.getMessage());
			logMessage("Unable to get an Buffered Writer " + ": " + ex.getStackTrace());
			
			bufferedWriter = null;
		}
		return bufferedWriter;
	}
	
	public OutputStream getOutputStream() {
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			throw new ServiceInvocationException("Unable to get an  output stream ", e);
		}
	}
	
	public InputStream getInputStream() {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			throw new ServiceInvocationException("Unable to get an input stream ", e);
		}
	}

	public ObjectInputStream getObjectInputStream() {
		
		try {
			if(objectInputStream == null)
				objectInputStream = new ObjectInputStream(socket.getInputStream());
		}catch(Exception ex) {
			throw new ServiceInvocationException("Unable to get an object input stream ", ex);
		}
		
		return objectInputStream;
	}
	
	public ObjectOutputStream getObjectOutputStream() {
		
		try {
			if(objectOutputStream == null)
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		}catch(Exception ex) {
			throw new ServiceInvocationException("Unable to get an object output stream ", ex);
		}
		
		return objectOutputStream;
	}

	public void cleanup()
	{
		closeConnections();

	}

	public synchronized void close()
	{
		closeConnections();
	}

	protected synchronized void closeConnections()
	{
		try
		{
			if (socket != null){
				logMessage("Closing socket");
				socket.close();
			}
		}catch (Throwable t){
			logError("Error closing socket: " + t);
		}

		try{
			if (bufferedReader != null){
				logMessage("Closing input stream");
				bufferedReader.close();
			}
		}catch (Throwable t){
			logError("Error closing input stream: " + t);
		}

		try{
			if (bufferedWriter != null){
				logMessage("Closing output stream");
				bufferedWriter.flush();
				bufferedWriter.close();
			}
		}catch (Throwable t){
			logError("Error closing output stream: " + t);
		}

		try{
			if (objectInputStream != null){
				logMessage("Closing object input stream");
				objectInputStream.close();
			}
		}catch (Throwable t){
			logError("Error closing object input stream: " + t);
		}
		
		try{
			if (objectOutputStream != null){
				logMessage("Closing object output stream");
				objectOutputStream.close();
			}
		}catch (Throwable t){
			logError("Error closing object input stream: " + t);
		}

		socket = null;
		bufferedReader = null;
		bufferedWriter = null;
		objectInputStream = null;
		objectOutputStream = null;
	}
	
	
	private void logMessage(String msg) {
		Logger.getLogger().debug(msg);
	}

	private void logError(String error) {
		Logger.getLogger().error(error);
	}
	
	private void logError(Throwable throwable,String error) { 
	   Logger.getLogger().error(throwable,error); 
	} 
	
	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isNewSocket() {
		return newSocket;
	}

	public void setNewSocket(boolean newSocket) {
		this.newSocket = newSocket;
	}
	
	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
	
	protected ServiceTimeoutException createServiceTimeoutException(Exception e) {
		return new ServiceTimeoutException("server at " + hostname + ":" + port + " is not available due to " + e.toString());
    }
	
	public void setSoTimeout(int timeout){
		try {
			socket.setSoTimeout(timeout);
		} catch (Exception e) {
			logMessage("Exception to set socket timeout." + e.toString());
		}
	}

}
