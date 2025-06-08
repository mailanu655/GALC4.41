package com.honda.galc.client.datacollection.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.scanless.ScanlessScannerMessage;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;

import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class ScanlessScannerMessageReceiver extends DataCollectionObserverBase
		implements DeviceListener, IScanlessObserver {

	protected ClientContext context;

	private ScanlessScannerMessage scanlessScannerMessage;

	public ScanlessScannerMessageReceiver(ClientContext context) {
		super();
		this.context = context;
		registerDeviceListener();
	}

	protected List<IDeviceData> getDeviceDataList() {
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ScanlessScannerMessage());

		return list;
	}

	protected void registerDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null) {
			device.registerDeviceListener(this, getDeviceDataList());
		}
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if (context.getProperty().isScanlessEnabled() && isScannerInAuto()) {
			if (deviceData instanceof ScanlessScannerMessage) {
			
				scanlessScannerMessage = (ScanlessScannerMessage) deviceData;
				cacheScanlessScannerStatus(deviceData);
				Logger.getLogger().info("Received Scanless Scanner Message " + scanlessScannerMessage.toString());
				Logger.getLogger().info("Received Product Id " + scanlessScannerMessage.getToolProductId());
			}
		}
		return deviceData;
	}

	private void cacheScanlessScannerStatus(IDeviceData deviceData) {
		scanlessScannerMessage = ((ScanlessScannerMessage) deviceData).clone();
		DataCollectionController.getInstance(context.getAppContext().getApplicationId()).addScanlessScannerStatus(scanlessScannerMessage);
	}

	private boolean isScannerInAuto() {
		ComponentStatus componentStatus = getComponentStatusScanlessScanner();
		String operationMode = componentStatus == null ? "N/A" : componentStatus.getStatusValue();
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(operationMode);
	}

	private ComponentStatusDao getComponentStatusDao() {
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}

	private ComponentStatus getComponentStatusScanlessScanner() {
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "SCANLESS_SCANNER_OPERATION_MODE");
	}

	@Override
	public void clearScanlessMessages(DataCollectionState state) {
		DataCollectionController.getInstance(context.getAppContext().getApplicationId()).clearScanlessScannerStatus();;
	}

}
