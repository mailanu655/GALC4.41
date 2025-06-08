package com.honda.galc.client.dc.processor;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.LetResult;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.BroadcastService;

/**
 * @author Jeffray Huang
 * @date Aug 04, 2014
 */
public class LetResultProcessor extends OperationProcessor implements IOperationProcessor, DeviceListener  {
	
	Logger logger;
	public LetResultProcessor(DataCollectionController controller, MCOperationRevision operation) {
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
		list.add(new LetResult());
		return list;
	}
	
	public IDeviceData received(String clientId, final IDeviceData deviceData) {
		if (deviceData instanceof LetResult) {
			letResultReceived((LetResult)deviceData);
		} 
		return null;
	}
	
	private void letResultReceived(LetResult letResult){
		if (letResult.getLetResult().equals("1")) EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PDDA_CONFIRM, null));
		else getController().displayErrorMessage("Let Check Result:fail!");
	}

	//send MBPN to LET device
	public void broadcastDataContainer() {
		logger = Logger.getLogger("LetResultProcessor");
		String deviceSeq = getController().getModel().getCurrentOperation().getStructure().getOperationRevisionPlatform().getDeviceId();
		if (StringUtils.isEmpty(deviceSeq)) deviceSeq = "1";
		try {
			DataContainer dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, getController().getProductModel().getProductId());
			dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
			getService(BroadcastService.class).broadcast(getController().getProductModel().getProcessPoint().getProcessPointId(), 
					                                     Integer.parseInt(deviceSeq), dc);
		} catch(Exception e) {
			getController().displayErrorMessage("failed to broadcast MBPN to deviceWise.");
			logger.error(e);
		}
		getController().displayMessage("MBPN has been sent to deviceWise successfully");
	}
	
}
