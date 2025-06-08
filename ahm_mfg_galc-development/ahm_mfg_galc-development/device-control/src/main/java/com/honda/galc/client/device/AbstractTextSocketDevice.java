package com.honda.galc.client.device;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.client.device.property.PlainsocketDevicePropertyBean;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.net.SocketClient;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.net.TextSocketReceiver;
import com.honda.galc.net.TextSocketSender;
import com.honda.galc.property.DevicePropertyBean;

public class AbstractTextSocketDevice extends AbstractDevice{
	
	public static final int SOCKET_RESPONSE_WAIT_TIME = 2000;

	public static final int DEFAULT_PING_INTERVAL = 10000;
	
	private String hostName = "";

	private long pingInterval = DEFAULT_PING_INTERVAL;

	private String model = "";

	private String protocol = "";

	private int port = -1;
	
	private int autoRequestInterval = 500;
	
	private int numRetries = 60;
	
	private boolean retryRequest = true;

	protected TextSocketReceiver _socketReceiver = null;
	
	protected TextSocketSender _socketSender = null;

	protected SocketClient _socketClient = null;
	
	private volatile Date _lastPingReply = null;

	private volatile boolean _finalizingSocket = false;
	
	private volatile boolean autoCloseSocket = false;
	
	protected List<DeviceListener> deviceListeners = new ArrayList<DeviceListener>();
	 
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public long getPingInterval() {
		return pingInterval;
	}

	public void setPingInterval(long pingInterval) {
		this.pingInterval = pingInterval;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public TextSocketSender getSocketSender() {
		if (_socketSender == null)
			_socketSender = new TextSocketSender(getSocket());
		return _socketSender;
	}

	public void set_socketSender(TextSocketSender sender) {
		_socketSender = sender;
	}

	public void set_socket(SocketClient _socket) {
		this._socketClient = _socket;
	}

	public Date get_lastPingReply() {
		return _lastPingReply;
	}

	public void set_lastPingReply(Date pingReply) {
		_lastPingReply = pingReply;
	}

	public boolean is_finalizingSocket() {
		return _finalizingSocket;
	}

	public void set_finalizingSocket(boolean socket) {
		_finalizingSocket = socket;
	}

	public void setDeviceProperty(DevicePropertyBean devicePropertyBean) {
		PlainsocketDevicePropertyBean property = (PlainsocketDevicePropertyBean) devicePropertyBean;
		_id = property.getDeviceId();
		_deviceType = property.getType();
		model = property.getModel();
		_enabled = property.isEnabled();
		hostName = property.getHostName();
		port = property.getPort();
		pingInterval = property.getPingInterval();
		autoRequestInterval = property.getAutoRequestInterval();
		numRetries = property.getNumRetries();
		retryRequest = property.isRetryRequest();
		autoCloseSocket = property.isAutoCloseSocket();
	}

	/**
	 * Will attempt to return a valid socket (with subscription to last tightening result).
	 * If unable to get a valid socket will return null
	 * and not throw an exception 
	 * @return
	 */
	public SocketClient getSocket() {
		try {
			if (_socketClient == null || !_socketClient.isValid()) {
				finalizeSocket();
				Socket socket = TCPSocketFactory.getSocket(hostName, port, SOCKET_RESPONSE_WAIT_TIME);
				_socketClient = new SocketClient(socket);
				startSocketReceiver();
				initCommunication();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to get a socket connection to the Torque device " + _id);
			_socketClient = null;
		}
		return _socketClient;
	}
	
	public int getAutoRequestInterval() {
		return autoRequestInterval;
	}

	public void setAutoRequestInterval(int autoRequestInterval) {
		this.autoRequestInterval = autoRequestInterval;
	}

	public int getNumRetries() {
		return numRetries;
	}

	public void setNumRetries(int numRetries) {
		this.numRetries = numRetries;
	}

	public boolean isRetryRequest() {
		return retryRequest;
	}

	public void setRetryRequest(boolean retryRequest) {
		this.retryRequest = retryRequest;
	}

	public boolean isAutoCloseSocket() {
		return autoCloseSocket;
	}

	public void setAutoCloseSocket(boolean autoCloseSocket) {
		this.autoCloseSocket = autoCloseSocket;
	}

	/**
	 * finalizes the socket connection established with the 
	 * torque device.  Only one instance of this method will 
	 * execute at a given time, duplicate calls
	 * will block.
	 */
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
		getLogger().warn("finalizing socket");

		try {
			if(_socketClient != null) _socketClient.close();
		} catch (Exception ex) {}

		_socketClient = null;
		_socketSender = null;
		_finalizingSocket = false;
	}

	/**
	 * starts socket receiver thread to wait for messages from the 
	 * torque controller
	 */
	public void startSocketReceiver() {
		if(_socketReceiver != null) return;
	
		Thread t = new Thread(_socketReceiver);
		t.setDaemon(true);
		t.start();
	}
	
	protected void createSocketReciever() {
		_socketReceiver = new TextSocketReceiver(getSocket(),  null, TextSocketReceiver.NULL, getLogger());
	}
	
	public void initCommunication(){
		
	}
	
	public synchronized boolean send(String msg) {
		
		try{
			getSocketSender().send(msg);
			getLogger().info(">>" + msg + ">>");
		}catch(ServiceInvocationException ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to send message " + msg + " to device");
			return false;
		}
		return true;
	}
	
	/**
	 * registers a listener to the device
	 */
	public boolean registerListener(DeviceListener listener) {
		synchronized (deviceListeners) {
			// add if already not registered
			if (!deviceListeners.contains(listener))
				deviceListeners.add(listener);
		}
		return true;
	}

	/**
	 * unregisters a listener to the device
	 */
	public boolean unregisterListener(DeviceListener listener) {
		synchronized (deviceListeners) {
			// delete if already registered
			if (deviceListeners.contains(listener))
				deviceListeners.remove(listener);
		}
		return true;
	}
	
	/**
	 * close socket
	 */

	public void closeSocket()  {
		finalizeSocket();
	}

	/**
	 * notifies device status change to listeners
	 * @param status
	 */
	public void notifyListeners(final IDeviceData deviceData) {
		synchronized (deviceListeners) {
			for(final DeviceListener listener : deviceListeners) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_StatusInfo");
						try {
							listener.received(getClientId(), deviceData);
							getLogger().info("Successfully notified status change to listener: "	+ listener);
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex,"Torque status change handling unsuccessful for " + listener);
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}
	

}
