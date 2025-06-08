package com.honda.galc.common.logging;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.status.StatusLogger;

import com.honda.galc.net.TCPSocketFactory;

/**
 * @author Subu Kathiresan
 * @Date Aug 09, 2012
 *
 */
public abstract class SocketAppenderBase extends AppenderBase {

	public static final int APPENDER_NONE = 0;
	public static final int APPENDER_FILE = 1;
	public static final int APPENDER_SOCKET_PRIMARY = 2;
	public static final int APPENDER_SOCKET_SECONDARY = 3;
	public static final int DEFAULT_PORT = 55555;
	
    @PluginBuilderAttribute
    @Required
	private String _primaryHost;

    @PluginBuilderAttribute
	private String _secondaryHost;
	private String _host = "";

	private int _port = -1;
	private int _queueSize = -1;
	private int _currentAppender = APPENDER_FILE;
	
    @PluginBuilderAttribute
    @Required
	private int _primaryPort = DEFAULT_PORT;

    @PluginBuilderAttribute
	private int _secondaryPort = DEFAULT_PORT;
	private FileAppender _fileAppender;
	private boolean _locationInfo = false;
	private volatile boolean _finalizingSocket = false;
	private volatile Socket _socket;
	private volatile ObjectOutputStream _writer;
	
	public SocketAppenderBase(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}

	public SocketAppenderBase(String name, Filter filter, Layout<? extends Serializable> layout, 
			String primaryHost, int primaryPort, String secondaryHost, int secondaryPort, int queueSize) {
			super(name, filter, layout);
			setPrimaryHost(primaryHost);
			setPrimaryPort(primaryPort);
			setSecondaryHost(secondaryHost);
			setSecondaryPort(secondaryPort);
			setQueueSize(queueSize);
	}
	
	/**
	 * invoked by log4j to activate custom appender
	 */
	public void activateOptions() {
		if (!(connect(APPENDER_SOCKET_PRIMARY)))
			connect(APPENDER_SOCKET_SECONDARY);
	}
	
	protected synchronized boolean connect(int currAppender) {
		setCurrentAppender(currAppender);

		try {
			if(currAppender == APPENDER_SOCKET_PRIMARY) {
				_host = getPrimaryHost();
				_port = getPrimaryPort();
			} else {
				_host = getSecondaryHost();
				_port = getSecondaryPort();
			}	
			createSocket();
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected boolean switchConnection() {
		if(!hasSecondaryHost()) 
			return false;
		
		if(getCurrentAppender() == APPENDER_SOCKET_PRIMARY) {
			StatusLogger.getLogger().warn("Attempting to switch from Primary to Secondary Log Server");
			return connect(APPENDER_SOCKET_SECONDARY);
		}else { 
			StatusLogger.getLogger().warn("Attempting to switch from Secondary to Primary Log Server");
			return connect(APPENDER_SOCKET_PRIMARY);
		}
	}
	
	protected boolean socketWrite(LogEvent event) {
		try {
			getLogWriter().writeObject(getLogRecord(event));
			getLogWriter().flush();
		} catch(Exception e) {
			finalizeSocket();
			return false;
		}
		return true;
	}
	
	public synchronized void close() {
		if(super.isStopped()) 
			return;

		super.stop();
	}
	
	protected void finalizeSocket() {
		// disregard excessive calls to this method
		boolean discard = false;
		while (_finalizingSocket) {
			try {
				discard = true;
				Thread.sleep(10);
			} catch (Exception ex) {}
		}
		
		if (discard)
			return;
		
		_finalizingSocket = true;
		StatusLogger.getLogger().debug("finalizing socket");
		if(_writer != null) {
			try {
				_writer.flush();
			} catch(Exception ex) {}
			
			try {	
				_writer.close();
			} catch(Exception ex) {}
		}
		
		try {
			_socket.close();
		} catch(Exception ex) {}
		
		_socket = null;
		_writer = null;
		_finalizingSocket = false;
	}

	/**
	 * @return the socket
	 */
	protected Socket createSocket() {
		try {
			if (_socket == null || _socket.isClosed() || !_socket.isConnected() || !_socket.isBound()) {
				StatusLogger.getLogger().warn("Attempting to create new socket connection to " + _host + ":" + _port);	
				_socket = TCPSocketFactory.getSocket(_host, _port);
				StatusLogger.getLogger().warn("Successfully created new socket connection to " + _host + ":" + _port);
			}
		} catch (Exception ex) {
			StatusLogger.getLogger().error("Could not connect to remote host at [" + _host + ":" + _port + "]" + ex.getMessage());
			finalizeSocket();
			ex.printStackTrace();
		}
		return _socket;
	}
	
	protected ObjectOutputStream getLogWriter() {
		if (_writer == null) {
			try {
				_writer = new ObjectOutputStream(createSocket().getOutputStream());
			} catch(Exception ex) {}
		}
		return _writer;
	}
	
	protected void fileWrite(LogEvent event) {
		setCurrentAppender(APPENDER_FILE);
		if(_fileAppender != null) 
			_fileAppender.append(event);
	}
	
	protected boolean hasSecondaryHost(){
		return _secondaryHost != null && _secondaryPort > 0;
	}
	
	public void addFileAppender(FileAppender appender) {
		_fileAppender = appender;
	}
	
	/**
	 * The SocketAppender does not use a layout. Hence, this method
	 * returns <code>false</code>.  
	 * */
	public boolean requiresLayout() {
		return false;
	}

	/**
     The <b>LocationInfo</b> option takes a boolean value. If true,
     the information sent to the remote host will include location
     information. By default no location information is sent to the server.
	 */
	public void setLocationInfo(boolean locationInfo) {
		_locationInfo = locationInfo;
	}

	/**
     Returns value of the <b>LocationInfo</b> option.
	 */
	public boolean getLocationInfo() {
		return _locationInfo;
	}
	
	public String getPrimaryHost() {
		return _primaryHost;
	}

	public void setPrimaryHost(String primaryHost) {
		_primaryHost = primaryHost;
	}

	public int getPrimaryPort() {
		return _primaryPort;
	}

	public void setPrimaryPort(int primaryPort) {
		_primaryPort = primaryPort;
	}

	public String getSecondaryHost() {
		return _secondaryHost;
	}

	public void setSecondaryHost(String secondaryHost) {
		_secondaryHost = secondaryHost;
	}

	public int getSecondaryPort() {
		return _secondaryPort;
	}

	public void setSecondaryPort(int secondaryPort) {
		_secondaryPort = secondaryPort;
	}
	
	public int getQueueSize() {
		return _queueSize;
	}

	public void setQueueSize(int queueSize) {
		_queueSize = queueSize;
	}

	protected void setCurrentAppender(int currentAppender) {
		_currentAppender = currentAppender;
	}

	protected int getCurrentAppender() {
		return _currentAppender;
	}
	
	public boolean isConnected() {
		return _socket != null && _socket.isConnected();
	}

	public Socket getSocket() {
		return _socket;
	}

	public void setSocket(Socket socket) {
		_socket = socket;
	}
}
