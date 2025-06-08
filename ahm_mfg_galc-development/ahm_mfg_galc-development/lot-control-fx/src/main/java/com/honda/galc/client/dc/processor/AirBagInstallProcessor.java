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
import com.honda.galc.device.dataformat.DeviceDataResult;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.BroadcastService;

public class AirBagInstallProcessor  extends OperationProcessor implements IOperationProcessor, DeviceListener {

	Logger logger;
	
	public AirBagInstallProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		EventBusUtil.register(this);
		registerDeviceListener();
		broadcastDataContainer();
	}
	
	
	private void registerDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null)
			device.registerDeviceListener(this, getDeviceData());
		
	}
	
	

	public void broadcastDataContainer() {
		logger = Logger.getLogger("AirBagInstallProcessor");
		DataContainer dc = new DefaultDataContainer();
		String prodSpec = getController().getProductModel().getProductSpec().getProductSpecCode();
		String prodId = getController().getProductModel().getProductId();
		dc.put("PRODUCT_SPEC", prodSpec.trim());
		dc.put("PRODUCT_ID", prodId.trim());
		dc.put("OPERATION_NAME", getOperation().getId().getOperationName().trim());
		dc.put("PROCESS_POINT", getController().getProductModel().getProcessPoint().getProcessPointId().trim());
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());

		String deviceSeq = getController().getModel().getCurrentOperation().getStructure()
				.getOperationRevisionPlatform().getDeviceId();
		if (StringUtils.isEmpty(deviceSeq))
			deviceSeq = "1";
		try {
			getService(BroadcastService.class).broadcast(
					getController().getProductModel().getProcessPoint().getProcessPointId(),
					Integer.parseInt(deviceSeq), dc);
		} catch (Exception e) {
			getController().displayErrorMessage("failed to broadcast Product to deviceWise.");
			logger.error(e);
		}
		getController().displayMessage("Product has been sent to deviceWise successfully");

	}

	public IDeviceData received(String clientId, final IDeviceData deviceData) {
		if (deviceData instanceof DeviceDataResult) {

			DeviceDataResult airBagInstallResult = (DeviceDataResult) deviceData;
			if (!airBagInstallResult.getSkipOperation()) {
				if (airBagInstallResult.isOk()) {
					EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_OK, null));
				} else {
					EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_NG, null));
				}
			} else {
				EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_SKIP, null));
				EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.OP_COMPLETE, null));
			}

		}

		return null;
	}
	
	protected List<IDeviceData> getDeviceData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new DeviceDataResult());
		return list;
	}
	

}
