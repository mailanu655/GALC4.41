package com.honda.galc.client.dc.processor;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.VoltageMeterResult;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * @author Jeffray Huang
 * @date Aug 04, 2014
 */
public class VoltageMeterResultProcessor extends OperationProcessor implements IOperationProcessor, DeviceListener  {
	
	
	public VoltageMeterResultProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		EventBusUtil.register(this);
		registerDeviceListener();
	}
	
	private void registerDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null)
			device.registerDeviceListener(this, getDeviceData());
	}
	private List<IDeviceData> getDeviceData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new VoltageMeterResult());
		return list;
	}
	
	public IDeviceData received(String clientId, final IDeviceData deviceData) {
		if (deviceData instanceof VoltageMeterResult) {
			voltageMeterResultReceived((VoltageMeterResult)deviceData);
		} 
		return null;
	}
	
	private void voltageMeterResultReceived(VoltageMeterResult voltageMeterResult){
		if (voltageMeterResult.getVoltageMeterResult().equals("1")) EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PDDA_CONFIRM, null));
		else getController().displayErrorMessage("Voltage Meter Result Check:fail!");
	}
	
	
}
