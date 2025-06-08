package com.honda.galc.client.device.etcher;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.client.device.property.LecLaserDevicePropertyBean;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.DeviceType;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.net.TextSocketReceiver;
import com.honda.galc.net.TextSocketSender;
import com.honda.galc.property.DevicePropertyBean;

/**
 * @author Subu Kathiresan
 * @date Apr 19, 2017
 */
public class LecLaserSocketDevice extends AbstractDevice {

	public static final String NOT_CONNECTED = "Not connected to device: ";
	public static final String CONNECTION_RESTORED = "Connection restored to device: ";
	public static final int SOCKET_RESPONSE_WAIT_TIME = 2000;
	public static final int DEFAULT_PING_INTERVAL = 10000;
	
	private long pingInterval = DEFAULT_PING_INTERVAL;
	private List<ILecLaserDeviceListener> listeners = new ArrayList<ILecLaserDeviceListener>();
	private String hostName = "";
	private int port = -1;
	private int timeoutInSecs = 60;
	private String model = "";
	private String formId = "";
	
	private String[] commands;
	private String currentProductId = "";

	private LecLaserSocketReceiver socketReceiver = null;
	private LecLaserStateMachine stateMachine = null;
	private TextSocketSender socketSender = null;
	
	protected volatile Socket socket = null;
	private volatile long lastPingReply = new Date().getTime();
	private volatile boolean finalizingSocket = false;

	/**
	 * default constructor
	 */
	public LecLaserSocketDevice() {}
	
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		LecLaserDevicePropertyBean property = (LecLaserDevicePropertyBean) propertyBean;
		setId(property.getDeviceId());
		setType(DeviceType.TCPSocket);
		setModel(property.getModel());
		setEnabled(property.isEnabled());
		setHostName(property.getHostName());
		setPort(property.getPort());
		setPingInterval(property.getPingInterval());
		setFormId(property.getFormId());
		setTimeoutInSecs(property.getTimeoutInSecs());
	}

	public void startEtch(String productId) {
		this.setCurrentProductId(productId);
		getStateMachine().begin();
	}

	/**
	 * starts socket receiver thread to wait for messages from the 
	 * Laser controller
	 */
	private void startSocketReceiver() {
		if (socketReceiver != null) {
			socketReceiver.setReceive(false);
		}
		socketReceiver = new LecLaserSocketReceiver(this, getStateMachine(), TextSocketReceiver.END_OF_LINE, getLogger());
		Thread t = new Thread(socketReceiver);
		t.setDaemon(true);
		t.start();
	}

	public TextSocketSender getSocketSender() {
		if (socketSender == null)
			socketSender = new TextSocketSender(getSocket());
		return socketSender;
	}
	/**
	 * Will attempt to return a valid socket (with subscription to last tightening result).
	 * If unable to get a valid socket will return null
	 * and not throw an exception 
	 * @return
	 */
	public Socket getSocket() {
		try {
			if (socket == null || socket.isClosed() || !socket.isConnected() || !socket.isBound()) {
				finalizeSocket();
				socket = TCPSocketFactory.getSocket(hostName, port, SOCKET_RESPONSE_WAIT_TIME);
				if (!isConnected()) {
					notifyListeners(CONNECTION_RESTORED + getId() + " (" + getHostName() + ":" + getPort() + ")", DeviceMessageSeverity.info);
				}
				startSocketReceiver();
				setConnected(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to get a socket connection to the Laser device " + getId());
			notifyListeners(NOT_CONNECTED + getId() + " (" + getHostName() + ":" + getPort() + ")", DeviceMessageSeverity.error);
			socket = null;
			setConnected(false);
		}
		return socket;
	}

	/**
	 * sends the message to the Laser device
	 * 
	 * @param msg
	 */
	public synchronized boolean send(String msg) {
		try {
			getSocketSender().send(msg + (char)0);
			getLogger().info(">>" + msg + ">>");
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to send message " + msg + " to device");
			return false;
		}
		return true;
	}
	
	/**
	 * registers a listener to the device
	 */
	public boolean registerListener(ILecLaserDeviceListener listener) {
		synchronized (getListeners()) {
			// add if already not registered
			if (!getListeners().contains(listener))
				getListeners().add(listener);
		}
		return true;
	}

	/**
	 * unregisters a listener to the device
	 */
	public boolean unregisterListener(ILecLaserDeviceListener listener) {
		synchronized (getListeners()) {
			// delete if already registered
			if (getListeners().contains(listener))
				getListeners().remove(listener);
		}
		return true;
	}
	
	public void notifyListeners(final String msg, final DeviceMessageSeverity severity) {
		synchronized (getListeners()) {
			for(final ILecLaserDeviceListener listener : getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_StatusInfo");
						try {
							listener.handleDeviceStatusChange(msg, severity);
							getLogger().info("Successfully notified status change to listener: " + listener.getId());
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex,"Torque status change handling unsuccessful for " + listener.getId());
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}

	/**
	 * de-activates communication to the Laser device by sending a stop
	 * communication message to the Laser device
	 *
	 */
	public void deActivate() {
		super.deActivate();
		finalizeSocket();
	}

	/**
	 * finalizes the socket connection established with the 
	 * Laser device.  Only one instance of this method will 
	 * execute at a given time, duplicate calls
	 * will block.
	 */
	protected void finalizeSocket() {
		// disregard excessive calls to this method
		boolean discard = false;
		while (finalizingSocket) {
			try {
				discard = true;
				Thread.sleep(10);
			} catch (Exception ex) {}
		}

		if (discard)
			return;

		finalizingSocket = true;
		getLogger().warn("finalizing socket");

		try {
			if (socket != null || !socket.isClosed())
				socket.close();
		} catch (Exception ex) {}

		socket = null;
		socketSender = null;
		finalizingSocket = false;
	}

	public List<ILecLaserDeviceListener> getListeners() {		
		return listeners;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPingInterval(long pingInterval) {
		this.pingInterval = pingInterval;
	}

	public long getPingInterval() {
		return (pingInterval > DEFAULT_PING_INTERVAL) ? pingInterval : DEFAULT_PING_INTERVAL;		
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public long getLastPingReply() {
		return lastPingReply;
	}

	public void setLastPingReply(long date) {
		lastPingReply = date;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public String getCurrentProductId() {
		return currentProductId;
	}

	public void setCurrentProductId(String currentProductId) {
		this.currentProductId = currentProductId;
	}

	public int getTimeoutInSecs() {
		return timeoutInSecs;
	}

	public void setTimeoutInSecs(int timeoutInSecs) {
		this.timeoutInSecs = timeoutInSecs;
	}
	
	public LecLaserStateMachine getStateMachine() {
		if (stateMachine == null) {
			stateMachine = new LecLaserStateMachine(this);
		}
		return stateMachine;
	}

	public void setStateMachine(LecLaserStateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
}
