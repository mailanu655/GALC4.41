package com.honda.galc.client.device.ubisense;

import com.honda.galc.client.device.AbstractTextSocketDevice;
import com.honda.galc.client.device.property.UbisenseDevicePropertyBean;
import com.honda.galc.device.dataformat.UbisenseTelegram;
import com.honda.galc.net.UbisenseTextSocketReceiver;
import com.honda.galc.property.DevicePropertyBean;

/**
 * Ubisense device is a server software communicating with GALC client.
 * GALC Lot Control client would communicate with Ubisense through
 * a TCP/IP socket connection by registering CLIENT_ID upon launch.
 * 
 * @author Bernard Leong
 * @date Jun 21, 2017
 */

public class UbisenseDevice extends AbstractTextSocketDevice {
	private UbisenseToolStatus toolStatus = null;
	private UbisenseProductId productIdStatus = null;
	private UbisenseMessageHandler messageHandler = null;
	public UbisenseDevice() {}
	public UbisenseDevice(String terminalName) {
		setClientId(terminalName);
	}
	
	public void init() {
		getSocket();
		initUbisenseToolStatus();
		initProductIdStatus();
	}
	
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		UbisenseDevicePropertyBean property = (UbisenseDevicePropertyBean) propertyBean;
		setId(property.getDeviceId());
		setHostName(property.getHostName());
		setPort(property.getPort());
		setEnabled(true);
	}

	public void sendInitTelegram() {
		send(telegram().initTelegram(getClientId()));
	}
	
	public void sendQueryTelegram(String productId) {
		send(telegram().queryTelegram(productId));
	}

	private UbisenseTelegram telegram() {
		UbisenseTelegram telegram = new UbisenseTelegram();
		return telegram;
	}
	
	public void initUbisenseToolStatus() {
		if (toolStatus == null) {
			toolStatus = new UbisenseToolStatus();
		}
	}
	
	public void initProductIdStatus() {
		if (productIdStatus == null) {
			productIdStatus = new UbisenseProductId();
		}
	}
	
	public void notifyToolStatusListeners(String telegram) {
		notifyListeners(toolStatus.parseTelegram(telegram));
	}
	
	public void notifyToolStatusListeners(boolean connected) {
		notifyListeners(toolStatus.connected(connected));
	}
	
	public void notifyProductIdStatusListeners(String telegram) {
		notifyListeners(new UbisenseProductId(telegram));
	}
	
	@Override
	public void startSocketReceiver() {
		messageHandler = new UbisenseMessageHandler(this); 
		_socketReceiver = new UbisenseTextSocketReceiver(getSocket(),
				messageHandler, Character.MAX_VALUE,
				getLogger());
		Thread t = new Thread(_socketReceiver);
		t.setDaemon(true);
		t.start();
	}
	
	public void resetQueryResendAttempts() {
		messageHandler.resetQueryResendAttempts();
	}
	@Override
	public void initCommunication() {
		sendInitTelegram();
		getLogger().info(getClientId() + " initializing communication with Ubisense ...");
	}
}
