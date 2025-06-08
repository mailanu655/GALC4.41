/**
 * 
 */
package com.honda.galc.client.device.ipuqatester;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.client.device.property.IpuqatesterDevicePropertyBean;
import com.honda.galc.property.DevicePropertyBean;

/**
 * this device class communicates with the IPU QA Tester device and receives
 * results XML string from them synchronously as tests are performed on the
 * IPU units.  These result XML is then converted to UnitInTest objects and 
 * relayed back to the clients who are using this device.
 * 
 * @author Subu Kathiresan
 * @date Feb 3, 2012
 */
public class IpuQaTesterSocketDevice extends AbstractDevice {
	
	private int _port = -1;
	private Timer _socketReceiverTimer = null;
	
	private volatile ServerSocket _serverSocket = null;
	private volatile List<IpuQaTesterDeviceListener> _ipuQaTesterResultsListeners = new ArrayList<IpuQaTesterDeviceListener>();
	
	public IpuQaTesterSocketDevice() {
	}
	
	public IpuQaTesterSocketDevice(int port) {
		_port = port;
	}

	public void startDevice() {
		try {
			_serverSocket = new ServerSocket(_port);
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().error("Unable to create server socket on port: " + _port);
		}
		
		if (_serverSocket == null)
			return;
		
		Runnable runnable = new Runnable() {
			public void run() {
				startConnectionListener();
			}
		};

		// start thread to listen for IPU QA tester client socket connections
		new Thread(runnable).start(); 
	}
	
	/**
	 * starts a daemon process which will listen on the socket port and
	 * accept client connections from the IPU QA Tester device
	 */
	public void startConnectionListener() {
		while(isActive()) {
			try {
				Socket socket = _serverSocket.accept();
				new IpuQaTesterMessageHandler(this, socket).start();
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error("IpuQaTesterSocketDevice.startXmlReceiverDaemon(): Unable to accept client connection");
			}
		}
	}
	
	/**
	 * process incoming xml message, nofity all listeners
	 * @param xmlString
	 */
	public void processMessage(StringBuffer xmlString) {
		try {
			getLogger().info("Received Message: " + xmlString);
			notifyListeners(xmlString);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Could not process received data: " + xmlString);
		}
	}

	/**
	 * notifies new IpuQaTester result to listeners 
	 * @param unitInTest
	 */
	public void notifyListeners(final StringBuffer unitInTestXmlString) {
		synchronized (getIpuQaTesterResultsListeners()) {
			for(final IpuQaTesterDeviceListener listener : getIpuQaTesterResultsListeners()) {
				Runnable notifyWorker = new Runnable() {
					public void run() {
						try {
							listener.processIpuUnit(unitInTestXmlString);
							getLogger().info("Successfully notified received IpuUnit data to listener: " + listener.getListenerName());
						} catch (Exception ex) {
							ex.printStackTrace();
							getLogger().error(ex,"Could not notify received IpuUnit data to listener " + listener.getListenerName());
						}
					}
				};

				Thread worker = new Thread(notifyWorker);
				worker.start();
			}
		}
	}
	
	public int getPort() {
		return _port;
	}
	
	@Override
	public void activate() {
		super.activate();
		startDevice();
	}
	
	@Override
	public void deActivate() {
		super.deActivate();
	}

	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		IpuqatesterDevicePropertyBean property = (IpuqatesterDevicePropertyBean) propertyBean;
		_id = property.getDeviceId();
		_enabled = property.isEnabled();
		_port = property.getPort();
	}

	public List<IpuQaTesterDeviceListener> getIpuQaTesterResultsListeners() {		
		return _ipuQaTesterResultsListeners;
	}
	
	public boolean registerListener(IpuQaTesterDeviceListener listener) {
		synchronized (getIpuQaTesterResultsListeners()) {
			// add if already not registered
			if (!getIpuQaTesterResultsListeners().contains(listener))
				getIpuQaTesterResultsListeners().add(listener);
		}
		return true;
	}

	public boolean unregisterListener(IpuQaTesterDeviceListener listener) {
		synchronized (getIpuQaTesterResultsListeners()) {
			// delete if already registered
			if (getIpuQaTesterResultsListeners().contains(listener))
				getIpuQaTesterResultsListeners().remove(listener);
		}
		return true;
	}
	
	public Timer getSocketReceiverTimer() {
		if (_socketReceiverTimer == null)
			_socketReceiverTimer = new Timer();

		return _socketReceiverTimer;
	}
}
