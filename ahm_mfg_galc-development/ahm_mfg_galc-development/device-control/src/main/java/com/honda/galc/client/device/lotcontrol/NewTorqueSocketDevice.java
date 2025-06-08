/**
 * 
 */
package com.honda.galc.client.device.lotcontrol;

import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.honda.galc.client.device.NewTorqueMessageHandler;
import com.honda.galc.client.device.TorqueMessageHandler;
import com.honda.galc.client.device.property.TorqueDevicePropertyBean;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.net.TextSocketReceiver;
import com.honda.galc.property.DevicePropertyBean;

/**
 * @author vcc44349
 * New torque socket device that does the following:
 * With the Atlas Copco PF6000 and PowerMac torque controllers, the device had to wait for a response to the communication start
 * before sending the last_tightening_data_subscribe.  Otherwise
 *
 */
public class NewTorqueSocketDevice extends TorqueSocketDevice {
	
	private boolean _delaySubscribe = false;
	private long _delayInMillis = 500;
	private volatile int _maxTries = 10;

	@Override
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		TorqueDevicePropertyBean property = (TorqueDevicePropertyBean) propertyBean;
		super.setDeviceProperty(property);
		_delaySubscribe = property.getDelaySubscribe();
		_delayInMillis = property.getDelayInMillis();
		_maxTries = property.getMaxTries();
	}

	@Override
	public Socket getSocket() {
		try {
			if (_socket == null || _socket.isClosed() || !_socket.isConnected()	|| !_socket.isBound()) {
				finalizeSocket();
				_socket = TCPSocketFactory.getSocket(_hostName, _port, _socketTimeout);
				startSocketReceiver();
				initCommunication();
				Callable<Integer> waitForStart = new Callable<Integer>()  {
					int numTries = 0;
					int status = 0;
					public Integer call()  {
						try {
							while(numTries < _maxTries)  {
									Thread.sleep(_delayInMillis);
									if(isCommunicationStart())  {
										getLogger().debug(String.format("Last_tightening_result_data_subscribe: Waited for %d tries", numTries));
										break;
									}
									numTries++;
							}
							if(numTries >= _maxTries)  {
								getLogger().error(String.format("Last_tightening_result_data_subscribe: Communication start not acknowledged, waited for %d ms:", numTries * _delayInMillis));							
							}
						} catch (InterruptedException e) {
							status = 1;
							e.printStackTrace();
							getLogger().error(e,"Last_tightening_result_data_subscribe: Exception while waiting to send tightening subscribe");
						}
						return status;
					}
				};
				if(_delaySubscribe)  {
					ExecutorService delayStart = Executors.newSingleThreadExecutor();
					Future<Integer> f = delayStart.submit(waitForStart);
					f.get();
				}
				subscribeToTighteningResultData();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Unable to get a socket connection to the Torque device " + _id);
			_socket = null;
		}
		return _socket;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.device.lotcontrol.TorqueSocketDevice#startSocketReceiver()
	 */
	@Override
	/**
	 * starts socket receiver thread to wait for messages from the 
	 * torque controller
	 */
	public void startSocketReceiver() {
		super.startSocketReceiver();
		if (_socketReceiver != null)
			_socketReceiver.setReceive(false);
		
		_socketReceiver = new TorqueSocketReceiver(this, new NewTorqueMessageHandler(this), TextSocketReceiver.NULL, getLogger());
		Thread t = new Thread(_socketReceiver);
		t.setDaemon(true);
		t.start();
	}
	
}
