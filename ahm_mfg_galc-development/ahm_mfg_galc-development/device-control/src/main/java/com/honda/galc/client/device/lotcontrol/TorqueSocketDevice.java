package com.honda.galc.client.device.lotcontrol;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.honda.galc.client.device.TorqueMessageHandler;
import com.honda.galc.client.device.property.TorqueDevicePropertyBean;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.net.TextSocketReceiver;
import com.honda.galc.net.TextSocketSender;
import com.honda.galc.openprotocol.OPMessageType;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.property.DevicePropertyBean;

/**
 * @author Subu Kathiresan
 * Feb 02, 2009
 * 
 * This implementation of ITorqueDevice is specific to torque controllers.  The
 * DeviceManager will manage creating and destroying this class.  This class extends
 * AbstractOPTorqueDevice to utilize the Open protocol based methods available in 
 * that class. The communication between the client and the Torque controller is over
 * a TCP/IP Socket connection on a configured port
 */
public class TorqueSocketDevice extends AbstractOPTorqueDevice implements ITorqueDevice {

	public static final String DEVICE_NOT_CONNECTED = "Not connected to device: ";

	public static final String DEVICE_CONNECTION_RESTORED = "Connection restored to device: ";

	public static final String DEVICE_MESSAGE_PROCESSING_ERROR = "Unable to process message";

	public static final String DEVICE_MESSAGE_INVALID = "Invalid message received";
	
	protected int _socketTimeout = 2000;

	public static final int DEFAULT_PING_INTERVAL = 10000;
	
	protected String _hostName = "";

	private long _pingInterval = DEFAULT_PING_INTERVAL;

	private String _model = "";

	private String _protocol = "";

	protected int _port = -1;

	private List<ITorqueDeviceListener> _listeners = new ArrayList<ITorqueDeviceListener>();
	
	private Timer _keepAliveTimer = null;

	private Timer _notificationTimer = null;
	
	private boolean _instructionCodeSent = false;
	
	public ITorqueDeviceListener _master, _prevMaster = null;
	
	protected TorqueSocketReceiver _socketReceiver = null;
	
	private TextSocketSender _socketSender = null;

	protected volatile Socket _socket = null;
	
	private volatile long _lastPingReply = new Date().getTime();

	private volatile boolean _finalizingSocket = false;
	
	private TorqueDevicePropertyBean property;
	
	/**
	 * default constructor
	 */
	public TorqueSocketDevice() {
	}
	
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		property = (TorqueDevicePropertyBean) propertyBean;
		_id = property.getDeviceId();
		_deviceType = property.getType();
		_model = property.getModel();
		_enabled = property.isEnabled();
		_hostName = property.getHostName();
		_port = property.getPort();
		_pingInterval = property.getPingInterval();
		_spindleId = property.getSpindleId();
		_socketTimeout =  property.getSocketTimeOut();
		setMultiSpindle(property.isMultiSpindle());
		getTags().put("SPINDLE_ID",_spindleId );
	}
		
	/**
	 * 
	 */
	public void startDevice() {
		activate();
		try {
			startKeepAliveTimer();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to Initialize communication with the Torque device");
		}
	}
	
	/**
	 * starts socket receiver thread to wait for messages from the 
	 * torque controller
	 */
	public void startSocketReceiver() {
		if (_socketReceiver != null)
			_socketReceiver.setReceive(false);
		
		_socketReceiver = new TorqueSocketReceiver(this, new TorqueMessageHandler(this), TextSocketReceiver.NULL, getLogger());
		Thread t = new Thread(_socketReceiver);
		t.setDaemon(true);
		t.start();
	}
	
	public TextSocketSender getSocketSender() {
		if (_socketSender == null)
			_socketSender = new TextSocketSender(getSocket());
		return _socketSender;
	}
	
	/**
	 * Will attempt to return a valid socket (with subscription to last tightening result).
	 * If unable to get a valid socket will return null
	 * and not throw an exception 
	 * @return
	 */
	public Socket getSocket() {
		try {
			if (_socket == null || _socket.isClosed() || !_socket.isConnected()	|| !_socket.isBound()) {
				finalizeSocket();
				_socket = TCPSocketFactory.getSocket(_hostName, _port, _socketTimeout);
				startSocketReceiver();
				initCommunication();
				subscribeToTighteningResultData();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to get a socket connection to the Torque device " + _id);
			_socket = null;
		}
		return _socket;
	}

	/**
	 * Sends a Device connection restored message
	 */
	private void handleConnectionRestored() {
		setConnected(true);
		notifyListeners(DEVICE_CONNECTION_RESTORED + getId() + " (" + getHostName() + ":" + getPort() + ")", DeviceMessageSeverity.info);
	}

	/**
	 * sends a ping message to the device, if there was no activity
	 * for a period that exceeds the pingInterval time
	 */
	public void pingDevice() {
		try {
			// if not connected, start over 
			if (!isConnected()) {
				finalizeSocket();
			}

			send(getOpMessageHelper().createMessage(OPMessageType.keepAlive));			
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to send Keep Alive message to torque device ");
		}
	}

	/**
	 * starts the keep alive timer to keep the connection to the device alive
	 */
	private void startKeepAliveTimer() {
		// TODO check if already running, and exit if true

		TimerTask timerTask = new TimerTask() {
			public void run() {
				initThread("KeepAlive");
				while (isActive()) {
					try {
						pingDevice();
						if (getLastPingReply() + getPingInterval() + _socketTimeout < new Date().getTime()) {
							setConnected(false);
							notifyListeners(DEVICE_NOT_CONNECTED + getId() + " (" + getHostName() + ":" + getPort() + ")", DeviceMessageSeverity.error);
						} else{
							if(!isConnected()){
								handleConnectionRestored();
								if(getCurrentInstructionCode() != null){
									enable(getCurrentInstructionCode());
								}
							}
						}
						Thread.sleep(getPingInterval());
					} catch (Exception ex) {
						ex.printStackTrace();
						getLogger().error(ex,"Could not perform keepAlive check");
					}
				}

				_keepAliveTimer.cancel();
				getLogger().info("Exiting keepAlive() for TorqueSocketDevice " + _id);
			}
		};

		// start keepalive in a new thread, 100 millisecs from now
		getKeepAliveTimer().schedule(timerTask, 100);
	}

	/**
	 * sends an OPMessage to the torque device
	 * 
	 * @param nullValue
	 * @param commStartMsg
	 */
	public synchronized boolean send(String msg) {
		
		try{
			getSocketSender().send(msg + (char)0);
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
	public boolean registerListener(ITorqueDeviceListener listener) {
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
	public boolean unregisterListener(ITorqueDeviceListener listener) {
		synchronized (getListeners()) {
			// delete if already registered
			if (getListeners().contains(listener))
				getListeners().remove(listener);
		}
		return true;
	}

	/**
	 * de-activates communication to the torque device by sending a stop
	 * communication OPMessage to the torque device
	 *
	 */
	public void deActivate() {
		try {
			stopCommunication();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to de-activate Torque device " + _id );
		} finally {
			_active = false;
			finalizeSocket();
		}
	}
	
	public void deActive() {
		_active = false;
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
			if (_socket != null || !_socket.isClosed())
				_socket.close();
		} catch (Exception ex) {}

		_socket = null;
		_socketSender = null;
		_finalizingSocket = false;
	}

	/**
	 * notifies listeners
	 * 
	 * @param msg
	 * @param msgType
	 */
	private void notifyListeners(String msg, DeviceMessageSeverity msgType) {
		try {
			TorqueDeviceStatusInfo status = new TorqueDeviceStatusInfo();
			status.setMessage(msg);
			status.setMessageSeverity(msgType);
			notifyListeners(status);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Could not send '" + msg + "'");
		}
	}

	/**
	 * notifies device status change to listeners
	 * @param status
	 */
	public void notifyListeners(final TorqueDeviceStatusInfo status) {
		synchronized (getListeners()) {
			for(final ITorqueDeviceListener listener : getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_StatusInfo");
						try {
							listener.handleStatusChange(getDeviceId(),status);
							getLogger().info("Successfully notified status change to listener: "	+ listener.getId());
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
	 * notifies last tightening result to listeners 
	 * @param tighteningResult
	 */
	public void notifyListeners(final LastTighteningResult tighteningResult) {
		synchronized (getListeners()) {
			for (final ITorqueDeviceListener listener : getListeners()) {
				if (listener == _master) {
					Runnable notifyWorker = new Runnable() {
						public void run() {
							initThread("NotifyListeners_LastTighteningResult");
							try {
								listener.processLastTighteningResult(getDeviceId(),tighteningResult);
								getLogger().info("Successfully notified tightening result to listener: " + listener.getId());
							} catch (Exception ex) {
								ex.printStackTrace();
								getLogger().error(ex, "Last tightening result processing unsuccessful for "	+ listener.getId());
							}
						}
					};

					Thread worker = new Thread(notifyWorker);
					worker.start();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param multiSpindleResult
	 */
	public void notifyListeners(final MultiSpindleResultUpload multiSpindleResult) {
		synchronized (getListeners()) {
			for(final ITorqueDeviceListener listener : getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_MultiSpindleResultUpload");
						try {
							listener.processMultiSpindleResult(getDeviceId(),multiSpindleResult);
							getLogger().info("Successfully notified multi spindle result to listener: " + listener.getId());
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex,"Multi spindle result processing unsuccessful for " + listener.getId());
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}
		
	public List<ITorqueDeviceListener> getListeners() {		
		return _listeners;
	}

	public void activate() {
		_active = true;
	}

	public String getHostName() {
		return _hostName;
	}

	public void setHostName(String hostName) {
		_hostName = hostName;
	}

	public int getPort() {
		return _port;
	}

	public void setPort(int port) {
		_port = port;
	}

	public void setPingInterval(long pingInterval) {
		_pingInterval = pingInterval;
	}

	public long getPingInterval() {
		return (_pingInterval > DEFAULT_PING_INTERVAL) ? _pingInterval : DEFAULT_PING_INTERVAL;		
	}

	public String getModel() {
		return _model;
	}

	public void setModel(String model) {
		_model = model;
	}

	public String getProtocol() {
		return _protocol;
	}

	public void setProtocol(String protocol) {
		_protocol = protocol;
	}

	public long getLastPingReply() {
		return _lastPingReply;
	}

	public void setLastPingReply(long date) {
		_lastPingReply = date;
	}

	public Timer getKeepAliveTimer() {
		if (_keepAliveTimer == null)
			_keepAliveTimer = new Timer();

		return _keepAliveTimer;
	}

	public void destroyKeepAliveTimer(){
		if (_keepAliveTimer != null){
			_keepAliveTimer.cancel();
			_keepAliveTimer.purge();
		}
		_keepAliveTimer  = null;
	}
	
	public Timer getNotificationTimer() {
		if (_notificationTimer == null)
			_notificationTimer = new Timer();

		return _notificationTimer;
	}
	
	public String getDeviceId(){
			return _id;
	}

	public boolean isInstructionCodeSent() {
		return _instructionCodeSent;
	}

	public void setInstructionCodeSent(boolean instructionCodeSent) {
		_instructionCodeSent = instructionCodeSent;
	}

	public boolean requestControl(ITorqueDeviceListener listener) {
		if (_master != null && _master != listener) {
			_prevMaster = _master;
			_prevMaster.controlRevoked(this.getDeviceId());
		}
		_master = listener;
		return true;
	}
	
	public void releaseControl(){
		_master = _prevMaster;
		if (_prevMaster != null){
			_prevMaster.controlGranted(this.getDeviceId());
		}
		_prevMaster=null;
	}

	public ITorqueDeviceListener getMaster() {
		return _master;
	}

	public void setMaster(ITorqueDeviceListener master) {
		_master = master;
	}

	public ITorqueDeviceListener getPrevMaster() {
		return _prevMaster;
	}
	
	public void swapMaster( ) {
		ITorqueDeviceListener temp = _master;
		_master = _prevMaster;
		_prevMaster = _master;
	}
	
	public void setPrevMaster(ITorqueDeviceListener prevMaster) {
		_prevMaster = prevMaster;
	}
	
	public TorqueDevicePropertyBean getTorqueDevicePropertyBean() {
		return property;
	}
	
	public void notifyListeners(CommandAccepted commandAccepted) {
		synchronized (getListeners()) {
			for(final ITorqueDeviceListener listener : getListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_StatusInfo");
						try {
							listener.handleCommandAccepted(getDeviceId(),commandAccepted);
							getLogger().info("Successfully notified status change to listener: "	+ listener.getId());
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
}
