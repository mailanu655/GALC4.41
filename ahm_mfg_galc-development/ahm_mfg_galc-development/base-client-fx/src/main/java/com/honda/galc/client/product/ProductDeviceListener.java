package com.honda.galc.client.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.application.Platform;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.ActivityEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.Acknowledgment;
import com.honda.galc.device.dataformat.CarrierId;
import com.honda.galc.device.dataformat.CycleComplete;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ProductIdRefresh;
import com.honda.galc.device.dataformat.ProductIdReset;
import com.honda.galc.util.CarrierUtil;

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
 *  @see
 * @ver 0.2
 * @author Jeffray Huang
 */
public class ProductDeviceListener implements DeviceListener {

	private ProductController controller;
	
	private Logger logger;

	public ProductDeviceListener(ProductController controller,Logger logger) {
		this.controller = controller;
		this.logger = logger;
		registerDeviceListener();
	}

	public void registerDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null)
			device.registerDeviceListener(this, getDeviceData());
	}
	
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		
		EventBusUtil.publish(new ActivityEvent());

		if (deviceData instanceof ProductId) {
			return productIdReceived(clientId, (ProductId) deviceData);
		} else if (deviceData instanceof CarrierId) {
			return carrierIdReceived(clientId, (CarrierId)deviceData);
		} else if (deviceData instanceof CycleComplete) {
			return cycleCompleteReceived(clientId, (CycleComplete) deviceData);
		}else if(deviceData instanceof ProductIdRefresh) {
			return productIdRefreshReceived(clientId, (ProductIdRefresh) deviceData);
		}else if(deviceData instanceof ProductIdRefresh) {
			return productIdResetReceived(clientId, (ProductIdReset) deviceData);
		}else {
			String msg = String.format("Received not processable DeviceData: %s", deviceData);
			getLogger().warn(msg);
		}
		return DataCollectionComplete.NG();
	}

	private IDeviceData carrierIdReceived(String clientId, CarrierId carrierId) {
		if (getController().isInIdleState()) {
			String msg = String.format("Received Carrier: %s will start to process number", carrierId.getCarrierId());
			getLogger().info(msg);
			
			String productId = CarrierUtil.findProductIdByCarrier(getController().getView().getApplicationPropertyBean().getTrackingArea(), carrierId.getCarrierId());
			
			//Requirement WW:
			//1. carrier associated with VIN
			//2. passed in VIN as carrierId 
			if(StringUtils.isEmpty(productId))
				productId = carrierId.getCarrierId();
			
			return productIdReceived(clientId, new ProductId(productId));
		}else {
			String msg = String.format("Client is not in Idle state, received Carrier: %s will not be processed", carrierId.getCarrierId());
			getLogger().warn(msg);
			return DataCollectionComplete.NG();
		}
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
	public IDeviceData productIdReceived(String clientId, final ProductId productId) {
		if (getController().isInIdleState()) {
			String msg = String.format("Received PIN: %s will start to process number", productId);
			getLogger().info(msg);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					getController().processInputNumberInvoke(productId.getProductId());
				}
			});

			if (!getController().checkInputNumber(productId.getProductId())) return DataCollectionComplete.NG();
			return DataCollectionComplete.OK();
		} else {
			String msg = String.format("Client is not in Idle state, received PIN: %s will not be processed", productId);
			getLogger().warn(msg);
			return DataCollectionComplete.NG();
		}
	}

	public IDeviceData cycleCompleteReceived(String clientId, CycleComplete cycleComplete) {
		if (!cycleComplete.isCycleComplete()) {
			return DataCollectionComplete.NG();
		}
		if (getController().isInIdleState()) {
			String msg = String.format("Client is already in Idle state, recieved CycleComplete request will be ignored");
			getLogger().warn(msg);
			return DataCollectionComplete.NG();
		} else {
			String msg = String.format("Received CycleComplete request, client will move to Idle state");
			getLogger().info(msg);
			if (!getController().getModel().isTrainingMode()) getController().cycleComplete();
			return DataCollectionComplete.OK();
		}
	}
	
	// === protected api === //
	public IDeviceData productIdRefreshReceived(String clientId, ProductIdRefresh productIdRefresh) {
		if (getController().isInIdleState()) {
			getLogger().info("Received product id refresh signal");
			getController().productIdRefresh();
			return DataCollectionComplete.OK();
		} else {
			String msg = String.format("Client is not in Idle state, Received product id refresh signal will not be processed");
			getLogger().warn(msg);
			return DataCollectionComplete.NG();
		}
	}
	
	// === protected api === //
	public IDeviceData productIdResetReceived(String clientId, ProductIdReset productIdReset) {
		if (!getController().isInIdleState()) {
			getLogger().info("Received product id reset signal");
			getController().cancel();
			return DataCollectionComplete.OK();
		} else {
			String msg = String.format("Client is in Idle state, Received product id reset signal will not be processed");
			getLogger().warn(msg);
			return DataCollectionComplete.NG();
		}
	}
	
	
	protected List<IDeviceData> getDeviceData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new DataCollectionComplete());
		list.add(new CycleComplete());
		list.add(new ProductIdRefresh());
		list.add(new ProductIdReset());
		list.add(new CarrierId());
		return list;
	}

	protected void sendDeviceData(IDeviceData deviceData) {
		try {
			String msg = String.format("start sendDeviceData: %s", deviceData.getClass().getSimpleName());
				getLogger().info(msg);
			EiDevice device = DeviceManager.getInstance().getEiDevice();
			Acknowledgment acknowlegement = (Acknowledgment) device.syncSend(deviceData);
			if (!acknowlegement.isSuccess()) {
				getLogger().error(acknowlegement.getTransmitException());
			}
			String msg2 = String.format("sendDeviceData %s succeeded.", deviceData);
			getLogger().info(msg2);
		} catch (Throwable e) {
			String msg = String.format("Failed to send data %s to device, ex: %s", deviceData, e);
			getController().getView().setErrorMessage(msg);
		}
	}

	// === get/set === //
	protected ProductController getController() {
		return controller;
	}
	
	protected Logger getLogger() {
		return logger;
	}
}
