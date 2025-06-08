package com.honda.galc.client.datacollection.observer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.device.ubisense.UbisenseDevice;
import com.honda.galc.client.device.ubisense.UbisenseToolStatus;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.exception.DeviceInUseException;

/**
 * Ubisense (ACS) device is a server software communicating with GALC 
 * client. GALC Lot Control client would communicate with Ubisense (ACS) 
 * through a TCP/IP socket connection by registering CLIENT_ID upon launch.
 * 
 * @author Bernard Leong
 * @date Jun 21, 2017
 */
public class LotControlJobTorqueUbisenseManager extends LotControlJobTorqueManager implements DeviceListener {
	UbisenseDevice uDevice = null;
	Map<String, UbisenseToolStatus> ubiSenseToolsStatus = new HashMap<String, UbisenseToolStatus>();
	private UbisenseToolStatus toolStatus; //this is the current tool status
	private boolean connected = false;
	
	public LotControlJobTorqueUbisenseManager(ClientContext context) {
		// Initialize torque controller
		super(context);
		registerUbisenseDeviceData();
		AnnotationProcessor.process(this);
		ubiSenseToolsStatus.clear();
	}
	
	private UbisenseDevice getUbisenseDevice() {
		if (uDevice == null) 
			for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
				if (device instanceof UbisenseDevice) {
					uDevice = (UbisenseDevice) device;
					uDevice.init();
					break;
				}
			}
		return uDevice;
	}
	
	private boolean isInTorqueState() {
		return getCurrentState() instanceof ProcessTorque;
	}
	
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if (deviceData instanceof UbisenseToolStatus) {
			cacheUbisenseToolStatus(deviceData);
			// If client is in torque state then enable/disable torque controller immediately
			if (toolStatus.isConnected() && isInTorqueState() && toolStatus.isSameZone(getZoneId()))
					enableDisableTorqueDevice();
		}
		return deviceData;
	}
	
	private void cacheUbisenseToolStatus(IDeviceData deviceData) {
		toolStatus = ((UbisenseToolStatus) deviceData).clone();
		ubiSenseToolsStatus.put(toolStatus.getZoneId(), toolStatus);
		connected = toolStatus.isConnected();
		
	}

	private void enableDisableTorqueDevice() {
		try {	
			DataCollectionState state = getCurrentState();
			TorqueSocketDevice torqueSocketDevice = getTorqueDevice(state.getCurrentLotControlRule());
			
			boolean status = checkToolStatus(getZoneId(), state.getProductId());
			
			if (status)	
				torqueSocketDevice.enableForJobMode();
			else 
				torqueSocketDevice.disable();
			
		} catch (Exception e) {
			getCurrentState(context.getAppContext().getApplicationId()).exception(new LotControlTaskException("Failed to enable/disable torque controller.", 
					this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception: " + e.getMessage());
		}
	}

	private DataCollectionState getCurrentState() {
		return DataCollectionController.getInstance().getState();
	}

	// Validating Ubisense telegram with GALC data to determine enabling/disabling torque tool
	public boolean checkToolStatus(String zoneId, String clientProductId) {
		boolean enabled = false;

		UbisenseToolStatus gunUbisenseStatus = getUbisenseStatus(zoneId);
		if (gunUbisenseStatus == null) {
			// this should not happen 
			Logger.getLogger().warn("Ubisense tool status does not exist!");
			return false;
		}

		if (gunUbisenseStatus.isInError()) return true;


		//new function change:
		//on dis-association message the VIN is blank and the SameProductId check will always return false
		if (gunUbisenseStatus.isSameProductId(clientProductId) && gunUbisenseStatus.isToolInZone(zoneId)) 
			enabled = true;
		return enabled;
	}

	private UbisenseToolStatus getUbisenseStatus(String zoneId) {
		if(!StringUtils.isEmpty(zoneId))
			return ubiSenseToolsStatus.get(zoneId);
		else
			return toolStatus;

	}

	// Validating Ubisensee telegram with GALC data to determine enabling/disabling torque tool
	private boolean checkToolStatus(DataCollectionState torque) {
			return checkToolStatus(getZoneId(torque), torque.getProductId());
	}

	private String getZoneId(DataCollectionState torque) {
		return StringUtils.trimToEmpty(torque.getCurrentLotControlRule().getDeviceId());
	}


	// Register notification for Ubisense
	protected void registerUbisenseDeviceData() {
		if (getUbisenseDevice() != null) {
			getUbisenseDevice().registerListener(this);
			getUbisenseDevice().registerListener((DeviceListener)context.getCurrentViewManager());
		}
	}
	
	// This method is called when the client is launched
	@Override
	protected void disableTorqueDevice(TorqueSocketDevice torqueSocketDevice) {
		super.disableTorqueDevice(torqueSocketDevice);
		try {
			torqueSocketDevice.enableForJobMode();
		} catch (Exception e) {
			getCurrentState(context.getAppContext().getApplicationId()).exception(new LotControlTaskException("Failed to enable torque controller.", 
					this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception: " + e.getMessage());
		}
	}
	
	// This method is called when the client is programming the torque controller for 
	// a torque lot control rule
	@Override
	protected void setJobSocketDevice(ProcessTorque torque) {
		super.setJobSocketDevice(torque);
		try {	
			
			// If NOT connected to Ubisense then client will continue working. No need
			// to enable/disable the torque controller based on telegram.
			if (isConnected()) {
				TorqueSocketDevice torqueSocketDevice = getTorqueDevice(torque.getCurrentLotControlRule());
				if (checkToolStatus(torque)) 
					torqueSocketDevice.enableForJobMode();
				else 
					torqueSocketDevice.disable();
			}
		} catch (Exception e) {
			getCurrentState(context.getAppContext().getApplicationId()).exception(new LotControlTaskException("Failed to enable/disable torque controller.", 
					this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception: " + e.getMessage());
		}
	}

	private UbisenseToolStatus getUbiSenseStatus(String deviceId) {
		return getUbisenseStatus(deviceId);
	}
	
	// This method is called when the client finishes processing the current product
	// and sends the processed product ID to Ubisense and Ubisense sends the NEXT expectecd product ID
	@Override
	public void sendStatus(ProcessProduct state) {
		try {
			if (isConnected() && !(state.getProduct() == null || StringUtils.isEmpty(state.getProductId()))) {
				context.getDbManager().saveExpectedProductId(state.getProductId());
				getUbisenseDevice().sendQueryTelegram(state.getProductId());
				getUbisenseDevice().resetQueryResendAttempts();
				if (isSkipped(state))
					// This is done otherwise the torque device is lockecd in an
					// open protocol when it has already been (job) programmed and disabled
					getCurrentTorqueDevice(state).enableForJobMode();
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Ubisense connect exception: " + e.getMessage());
		}
		super.sendStatus(state); 
	}

	private TorqueSocketDevice getCurrentTorqueDevice(ProcessProduct state)
			throws DeviceInUseException {
		TorqueSocketDevice torqueSocketDevice = getTorqueDevice(state.getCurrentLotControlRule());
		return torqueSocketDevice;
	}
	
	// Receive publish event from the UbisenseViewManager when associate disables communication to Ubisense
	@EventSubscriber()
	public void onTorqueEvent(Event event) {
		if (event.isEvent(EventType.TORQUE_ENABLE)) {
			setConnected(false);
			getUbisenseDevice().closeSocket();
			for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
				if (device instanceof TorqueSocketDevice) {
					TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
					torqueDevice.enableForJobMode();
				}
			}
		}
	}

	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connect) {
		this.connected = connect;
	}

	public UbisenseToolStatus getToolStatus() {
		return getUbiSenseStatus(getZoneId());
		       
	}

	private String getCurrentDeviceId() {
		return getCurrentState().getCurrentLotControlRule().getDeviceId();
	}
	
	private String getZoneId() {
		return StringUtils.trimToEmpty(getCurrentDeviceId());
	}
	
}
