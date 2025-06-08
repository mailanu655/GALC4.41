package com.honda.galc.client.device;


import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceType;
import com.honda.galc.device.IDevice;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;


public abstract class AbstractDevice implements IDevice {

	protected String _id = "";

	protected String _name = "";

	protected String _deviceType = "";

	protected volatile boolean _enabled = false;

	protected String _clientId = "";

	protected volatile boolean _active = false;

	protected volatile int _state;

	protected Logger _logger;

	protected volatile boolean _isConnected = false;

	private List<ConnectionStatusListener> _connectionStatusListeners = new ArrayList<ConnectionStatusListener>();

	/**
	 * notifies connection status to listeners 
	 * @param connectionStatus
	 */
	public void notifyListeners(final ConnectionStatus connectionStatus) {
		synchronized (_connectionStatusListeners) {
			for(final ConnectionStatusListener listener : _connectionStatusListeners) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						initThread("NotifyListeners_Connection_Status");
						try {
							listener.statusChanged(connectionStatus);
							getLogger().info("Successfully notified connection status to listener: " + listener.getClass().getSimpleName());
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex,"Could not notify connection status to listener: " + listener.getClass().getSimpleName());
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}

	public void setConnected(boolean connectionStatus) {
		boolean notify = _isConnected != connectionStatus;
		_isConnected = connectionStatus;
		if(notify) 
			notifyDeviceStatus();		
	}
	
	public Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(getApplicationId());
			_logger.getLogContext().setApplicationInfoNeeded(true);
			// disable multi-line log and not send the log to the center log
			_logger.getLogContext().setMultipleLine(false);
			_logger.getLogContext().setCenterLog(false);
		}
		_logger.getLogContext().setThreadName(Thread.currentThread().getName());
		return _logger;
	}

	public void registerListener(ConnectionStatusListener listener) {
		if(!_connectionStatusListeners.contains(listener))
			_connectionStatusListeners.add(listener);
	}

	public void unregisterListener(ConnectionStatusListener listener) {
		if(!_connectionStatusListeners.contains(listener))
			_connectionStatusListeners.remove(listener);
	}

	protected void notifyDeviceStatus() {
		notifyListeners(new ConnectionStatus(this.getId(),isConnected()));
	}

	protected void initThread(String threadName) {
		Thread.currentThread().setName(threadName);
	}

	public boolean isActive() {
		return _active;
	}
	
	public void activate() {
		_active = true;
	}

	public void deActivate() {
		_active = false;
	}

	public DeviceType getType() {
		return Enum.valueOf(DeviceType.class, _deviceType);
	}
	
	public void setType(DeviceType deviceType) {
		_deviceType = deviceType.toString();
	}
	
	public String getApplicationId() {
		return getClientId() + "_" + getId();
	}

	public void setApplicationId(String applicationId) {
	}

	public String getClientId() {
		return _clientId;
	}

	public void setClientId(String clientId) {
		_clientId = clientId;
	}
	
	public int getCurrentState() {
		return _state;
	}

	public void setState(int state) {
		_state = state;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	public boolean isConnected() {
		return _isConnected;
	}

	public boolean isEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean enabled) {
		_enabled = enabled;
	}
}
