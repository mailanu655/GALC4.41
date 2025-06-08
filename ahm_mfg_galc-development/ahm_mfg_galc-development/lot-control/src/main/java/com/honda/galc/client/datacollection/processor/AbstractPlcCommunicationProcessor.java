/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.omron.FinsSocketPlcDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.AfbDataDao;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

/**
 * @author Subu Kathiresan
 * @date Jan 31, 2012
 */
public class AbstractPlcCommunicationProcessor extends ProcessorBase 
	implements ConnectionStatusListener {
	
	protected EngineDao engineDao = null;
	protected AfbDataDao afbDataDao = null;
	protected HoldResultDao holdResultDao = null;
	protected DeviceFormatDao deviceFormatDao = null;
	protected ProcessPointDao processPointDao = null;
	protected PartSpecDao partSpecDao = null;
	
	protected LotControlRule rule = null;
	protected PartSpec partSpec = null;
	protected InstalledPart installedPart = null;
	
	protected FinsSocketPlcDevice finsPlcDevice = null;
	
	public AbstractPlcCommunicationProcessor(ClientContext context) {
		super(context);
	}

	public void statusChanged(ConnectionStatus status) {
		Logger.getLogger().info("statusChanged:" + status.getConnectionId() + ":" + status.getStatus());
		Message msg = new Message(status.isConnected()? StatusMessage.DEVICE_CONNECT : StatusMessage.DEVICE_DISCONNECT, 
				status.isConnected()? "Plc Connection restored" : "Plc not connected", MessageType.INFO);
		
		if (status.isConnected())
			playConnectionRestoredSound();
		else
			playDisconnectedSound();
		
		getController().getFsm().message(msg);
	}
	
	/**
	 * returns the plc device specified by the
	 * current lot control rule
	 * 
	 * @return
	 */
	protected FinsSocketPlcDevice getPlc() {
		if (finsPlcDevice == null) {
			finsPlcDevice = (FinsSocketPlcDevice) DeviceManager.getInstance().getDevice(rule.getDeviceId());
			finsPlcDevice.registerListener(this);
			if (!finsPlcDevice.isActive())
				finsPlcDevice.activate();
		}
		return finsPlcDevice;
	}

	/**
	 * retrieves the part spec for the current part
	 * 
	 * @return
	 */
	protected PartSpec getPartSpec() {
		if (partSpec == null) {
			PartSpecId partSpecId = new PartSpecId();
			partSpecId.setPartName(rule.getPartName().getPartName());
			partSpecId.setPartId(rule.getParts().get(0).getId().getPartId());
			partSpec = getPartSpecDao().findByKey(partSpecId);
		}
		return partSpec;
	}
	
	protected ProcessPointDao getProcessPointDao() {
		if (processPointDao == null) {
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
		
	protected EngineDao getEngineDao() {
		if (engineDao == null) {
			engineDao = ServiceFactory.getDao(EngineDao.class);
		}
		return engineDao;
	}
	
	protected AfbDataDao getAfbDataDao() {
		if (afbDataDao == null) {
			afbDataDao = ServiceFactory.getDao(AfbDataDao.class);
		}
		return afbDataDao;
	}
	
	protected HoldResultDao getHoldResultDao() {
		if (holdResultDao == null) {
			holdResultDao = ServiceFactory.getDao(HoldResultDao.class);
		}
		return holdResultDao;
	}
		
	protected DeviceFormatDao getDeviceFormatDao() {
		if (deviceFormatDao == null) {
			deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		}
		return deviceFormatDao;
	}
	
	protected PartSpecDao getPartSpecDao() {
		if (partSpecDao == null) {
			partSpecDao = ServiceFactory.getDao(PartSpecDao.class);
		}
		return partSpecDao;
	}
	
	protected TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}

	protected void playNgSound() {
		LotControlAudioManager.getInstance().playNGSound();
	}
	
	protected void playOkSound() {
		LotControlAudioManager.getInstance().playOkSound();
	}
	
	protected void playConnectionRestoredSound() {
		LotControlAudioManager.getInstance().playConnectedSound();
	}
	
	protected void playDisconnectedSound() {
		LotControlAudioManager.getInstance().playDisconnectedSound();
	}

	public void registerDeviceListener(DeviceListener listener) {
	}
}
