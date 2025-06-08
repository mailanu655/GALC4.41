package com.honda.galc.client.datacollection.observer;


import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AFOnRfidData;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;




public class AFOnRfidDataReceiver extends RfidDataReceiver {

	
	private AFOnRfidData rfidData;
	
		
	public AFOnRfidDataReceiver(ClientContext context){
		super();
		this.context = context;
		registerDeviceListener();
	}

	protected List<IDeviceData> getDeviceDataList() {
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new AFOnRfidData());
		
		return list;
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if(isInAuto()){
			if (deviceData instanceof AFOnRfidData) {
				rfidData = (AFOnRfidData)deviceData;
				if(rfidData.isUnitPresent()){
					Logger.getLogger().info("Received Product Id " + rfidData.getProductId());
					ProductId productId = new ProductId(rfidData.getProductId());
					runInSeparateThread(productId);
				}else{
					runInSeparateThread(new Request("cancel"));
				}
			
			}
		}
		return deviceData;
	}

	private boolean isInAuto() {
		ComponentStatus componentStatus = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(context.getProcessPointId(), "OPERATION_MODE");
		if(componentStatus != null){
			String operationMode = componentStatus.getStatusValue();
			return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(operationMode);
		}
		return false;
	}
	
		
}
