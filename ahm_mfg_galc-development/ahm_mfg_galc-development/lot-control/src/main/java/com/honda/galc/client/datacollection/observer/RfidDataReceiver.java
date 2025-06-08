package com.honda.galc.client.datacollection.observer;

import java.util.List;

import javax.swing.SwingUtilities;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.service.property.PropertyService;

public abstract class RfidDataReceiver  extends DataCollectionObserverBase implements  DeviceListener{

	protected ClientContext context;
	protected final ClientAudioManager audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));

	
	protected void registerDeviceListener(){
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null) {
			device.registerDeviceListener(this, getDeviceDataList());
		}
	}
	
	protected abstract List<IDeviceData> getDeviceDataList() ;
	
	protected void runInSeparateThread(final Object request) {
		Thread t = new Thread(){
			public void run() {
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).received(request);
			}
		};
		
		t.start();
	
	}
}
