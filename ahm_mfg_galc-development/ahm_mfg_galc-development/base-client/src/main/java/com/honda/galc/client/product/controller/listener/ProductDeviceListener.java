package com.honda.galc.client.product.controller.listener;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.product.controller.ProductController;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.Acknowledgment;
import com.honda.galc.device.dataformat.CycleComplete;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductDeviceListener</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProductDeviceListener implements DeviceListener {

	private ProductController controller;

	public ProductDeviceListener(ProductController controller) {
		this.controller = controller;
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {

		if (deviceData instanceof ProductId) {
			ProductId productId = (ProductId) deviceData;
			return received(clientId, productId);
		} else if (deviceData instanceof CycleComplete) {
			CycleComplete cycleComplete = (CycleComplete) deviceData;
			return received(clientId, cycleComplete);
		} else {
			String msg = String.format("Received not processable DeviceData: %s", deviceData);
			Logger.getLogger(getClass().getName()).warn(msg);
		}
		return DataCollectionComplete.NG();
	}

	public void sendDataCollectionComplete() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null && device.containOutputDeviceData(DataCollectionComplete.class)) {
			sendDeviceData(DataCollectionComplete.OK());
		}
	}

	public void sendDataCollectionInComplete() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null && device.containOutputDeviceData(DataCollectionComplete.class)) {
			sendDeviceData(DataCollectionComplete.NG());
		}
	}

	// === protected api === //
	public IDeviceData received(String clientId, ProductId productId) {
		if (getController().isInIdleState()) {
			String msg = String.format("Received PIN: %s will start to process number", productId);
			Logger.getLogger(getClass().getName()).info(msg);
			getController().processInputNumberInvoke(productId.getProductId());
			return DataCollectionComplete.OK();
		} else {
			String msg = String.format("Client is not in Idle state, received PIN: %s will not be processed", productId);
			Logger.getLogger(getClass().getName()).warn(msg);
			return DataCollectionComplete.NG();
		}
	}

	public IDeviceData received(String clientId, CycleComplete cycleComplete) {
		if (!cycleComplete.isCycleComplete()) {
			return DataCollectionComplete.NG();
		}
		if (getController().isInIdleState()) {
			String msg = String.format("Client is already in Idle state, recieved CycleComplete request will be ignored");
			Logger.getLogger(getClass().getName()).warn(msg);
			return DataCollectionComplete.NG();
		} else {
			String msg = String.format("Received CycleComplete request, client will move to Idle state");
			Logger.getLogger(getClass().getName()).info(msg);
			getController().cycleComplete();
			return DataCollectionComplete.OK();
		}
	}

	protected void sendDeviceData(IDeviceData deviceData) {
		try {
			String msg = String.format("start sendDeviceData: %s", deviceData.getClass().getSimpleName());
			Logger.getLogger().info(msg);
			EiDevice device = DeviceManager.getInstance().getEiDevice();
			Acknowledgment acknowlegement = (Acknowledgment) device.syncSend(deviceData);
			if (!acknowlegement.isSuccess()) {
				Logger.getLogger().error(acknowlegement.getTransmitException());
			}
			String msg2 = String.format("sendDeviceData %s succeeded.", deviceData);
			Logger.getLogger().info(msg2);
		} catch (Throwable e) {
			String msg = String.format("Failed to send data %s to device, ex: %s", deviceData, e);
			getController().getView().setErrorMessage(msg);
		}
	}

	// === get/set === //
	protected ProductController getController() {
		return controller;
	}
}
