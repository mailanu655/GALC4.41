package com.honda.galc.client.datacollection.observer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.lotcontrol.ITorqueDevice;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.device.scanless.ScanlessTorqueMessage;
import com.honda.galc.client.device.ubisense.UbisenseToolStatus;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AristaRfidData;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>LotControlPsetTorqueManager</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * LotControlPsetTorqueManager description
 * </p>
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
 *
 * </TABLE>
 * 
 * @author Paul Chou Apr 21, 2010
 *
 */
public class LotControlPsetTorqueManager extends LotControlDeviceManager
		implements IPsetTorqueDeviceObserver, DeviceListener {
	Map<String, ScanlessTorqueMessage> scanlessToolStatus = new HashMap<String, ScanlessTorqueMessage>();
	private ScanlessTorqueMessage scanlessTorqueMessage;

	public LotControlPsetTorqueManager(ClientContext context) {
		super(context);

		// don't connect to torque device when the measurement value is editable
		if (!context.getProperty().isMeasurementEditable()) {
			initTorqueDevice(context.getAppContext().getApplicationId());
		}

		registerDeviceListener();
		AnnotationProcessor.process(this);
		scanlessToolStatus.clear();
	}

	protected void registerDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null) {
			device.registerDeviceListener(this, getDeviceDataList());
		}
	}

	protected List<IDeviceData> getDeviceDataList() {
		List<IDeviceData> list = super.getDeviceDataList();
		list.add(new ScanlessTorqueMessage());

		return list;
	}

	public void enableDevice(ProcessTorque torque) {

		if (!context.getProperty().isScanlessEnabled()) {
			doEnableDevice(torque);
		}
		if (context.getProperty().isScanlessEnabled() && !isTorqueInAuto()) {
			doEnableDevice(torque);
		}
		if (context.getProperty().isScanlessEnabled() && isTorqueInAuto()) {

			boolean status = checkToolStatus(torque, scanlessToolStatus.get(getProcessDeviceId(torque)));

			if (status) {
				Logger.getLogger().info("Enabling Torque ");
				doEnableDevice((ProcessTorque) torque);
			}
		}
	}

	private boolean isTorqueInAuto() {
		ComponentStatus scanlessTorqueComponentStatus = ServiceFactory.getDao(ComponentStatusDao.class)
				.findByKey(context.getProcessPointId(), "SCANLESS_TORQUE_OPERATION_MODE");
		String scanlessTorqueOperationMode = scanlessTorqueComponentStatus == null ? "N/A"
				: scanlessTorqueComponentStatus.getStatusValue();
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(scanlessTorqueOperationMode);
	}

	protected void doEnableDevice(ProcessTorque torque) {
		if (!context.getProperty().isMeasurementEditable()) {
			if (torque.getAction() == Action.REJECT
					&& torque.getCurrentTorqueIndex() < torque.getCurrentPartTorqueCount() - 1)
				return;
			if (isSocketDevice()) {
				enableToqureSocketDevice(torque);
			} else {
				enableTorqeuEiDevice(torque);
			}
		}
	}

	private void enableTorqeuEiDevice(ProcessTorque torque) {
	}

	private void enableToqureSocketDevice(ProcessTorque torque) {
		try {
			LotControlRule currentRule = torque.getCurrentLotControlRule();
			Logger.getLogger().check("start enableToqureSocketDevice:" + currentRule.getDeviceId() + " PSet#:"
					+ currentRule.getInstructionCode());
			TorqueSocketDevice torqueSocketDevice = getTorqueDevice(currentRule);

			// Down load product Id/Vin make this configurable
			if (context.getProperty().isCheckTorqueProductId())
				torqueSocketDevice.requestVinDownload(torque.getProductId());

			// Enable Torque Gun
			torqueSocketDevice.enable(currentRule.getInstructionCode());
			torqueSocketDevice.setInstructionCodeSent(true);
			Logger.getLogger().check("enableToqureSocketDevice succeeded.");

		} catch (Exception e) {
			torque.exception(new LotControlTaskException(
					"Failed enable device:" + torque.getCurrentLotControlRule().getDeviceId(),
					this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception:" + e.getMessage());
		}
	}

	public void disableDevice(DataCollectionState torque) {
		// disable the torque socket device when the measurement value is editable
		if (!context.getProperty().isMeasurementEditable()) {
			if (torque.getAction() == Action.REJECT && torque.getCurrentTorqueIndex() != -1)
				return;
			if (torque.getAction() == Action.SKIP_PART
					&& torque.getCurrentTorqueIndex() != torque.getTorqueCountOnRules())
				return;

			doDisableDevice(torque);
		}
	}

	protected void doDisableDevice(DataCollectionState torque) {
		if (isSocketDevice()) {
			disableTorqueSocketDevice(torque);
		} else {
			disableTorqueEiDevice();
		}
	}

	public void disableTorqueEiDevice() {

	}

	private void disableTorqueSocketDevice(DataCollectionState torque) {
		try {
			Logger.getLogger().info("start disableTorqueSocketDevice.");
			TorqueSocketDevice torqueSocketDevice = getTorqueDevice(torque.getCurrentLotControlRule());

			if (torqueSocketDevice != null && torqueSocketDevice.isActive()
					&& isInstructionCodeSent(torque, torqueSocketDevice)) {
				checkAndDoDelay(torqueSocketDevice, torque.getStateBean().getAction());
				torqueSocketDevice.disable();
				torqueSocketDevice.setInstructionCodeSent(false);
				Logger.getLogger().info("disableTorqueSocketDevice succeeded.");
			}

		} catch (Exception e) {
			torque.exception(new LotControlTaskException(
					"Failed to disable device:" + torque.getCurrentLotControlRule().getDeviceId(),
					this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception:" + e.getMessage());
		}
	}

	@Override
	protected void disableTorqueDevice(TorqueSocketDevice torqueSocketDevice) {
		try {
			Logger.getLogger().debug("disable Torque Device Pset.");
			torqueSocketDevice.disable();

		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception:" + e.getMessage());
		}
	}

	@Override
	public void cleanUp() {
		if (DataCollectionController.getInstance().getState() instanceof ProcessTorque)
			doDisableDevice(DataCollectionController.getInstance().getState());
	}

	@EventSubscriber()
	public void onTorqueEvent(Event event) {
		if (event.isEvent(EventType.TORQUE_DISABLE_ALL)) {
			for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
				if (device instanceof ITorqueDevice) {
					TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
					Logger.getLogger().info("disabling device - " + torqueDevice.getHostName());
					torqueDevice.disable();
				}
			}
		} else if (event.isEvent(EventType.TORQUE_ENABLE)) {
			Collection<IDevice> deviceList = DeviceManager.getInstance().getDevices().values();
			LotControlRule currentRule = DataCollectionController.getInstance().getState().getCurrentLotControlRule();
			String deviceId = currentRule.getDeviceId();
			for (IDevice device : deviceList) {
				if (device instanceof ITorqueDevice) {
					TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
					if (deviceId == null || StringUtils.isEmpty(deviceId))
						deviceId = torqueDevice.getId();
					if (deviceId.equalsIgnoreCase(torqueDevice.getId())) {
						Logger.getLogger().info("enabling device - " + torqueDevice.getHostName());
						torqueDevice.enable(currentRule.getInstructionCode());
						torqueDevice.setInstructionCodeSent(true);
					}
				}
			}
		}
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if (context.getProperty().isScanlessEnabled() && isTorqueInAuto()) {
			if (deviceData instanceof ScanlessTorqueMessage) {

				DataCollectionState state = DataCollectionController
						.getInstance(context.getAppContext().getApplicationId()).getState();
				scanlessTorqueMessage = (ScanlessTorqueMessage) deviceData;
				cacheScanlessToolStatus(deviceData);
				Logger.getLogger().info("Received Scanless Torque Message " + scanlessTorqueMessage.toString());
				Logger.getLogger().info("Received Product Id " + scanlessTorqueMessage.getToolProductId());

				if (state instanceof ProcessTorque) {
					boolean status = checkToolStatus(state, scanlessToolStatus.get(getProcessDeviceId(state)));

					if (status) {
						Logger.getLogger().info("Enabling Torque ");
						doEnableDevice((ProcessTorque) state);
					} else {
						if (scanlessTorqueMessage.isSameDeviceId(getProcessDeviceId(state))) {
							disableDevice(state);
						}
					}
				}
			}
		}
		return deviceData;
	}

	private String getProcessDeviceId(DataCollectionState state) {
		return state.getCurrentLotControlRule().getId().getProcessPointId() + "-"
				+ getDeviceId(state.getCurrentLotControlRule());
	}

	private void cacheScanlessToolStatus(IDeviceData deviceData) {
		scanlessTorqueMessage = ((ScanlessTorqueMessage) deviceData).clone();
		scanlessToolStatus.put(scanlessTorqueMessage.getToolId(), scanlessTorqueMessage);
	}

	private boolean checkToolStatus(DataCollectionState state, ScanlessTorqueMessage scanlessTorqueMessage) {
		boolean enable = false;
		if (scanlessTorqueMessage != null) {
			if (scanlessTorqueMessage.isSameProductId(state.getProductId())) {
				if (scanlessTorqueMessage.isSameDeviceId(getProcessDeviceId(state))) {
					if (scanlessTorqueMessage.isSameToolInZone()) {
						enable = true;
					} else {
						Logger.getLogger()
								.info("Tool Not in Zone (expected=" + scanlessTorqueMessage.ENTER_ZONE + " OR "
										+ scanlessTorqueMessage.IN_ZONE + ", actual="
										+ scanlessTorqueMessage.getToolEventType() + ")", ", Disabling Torque");
						enable = false;
					}
				} else {
					Logger.getLogger().info("Tool Id does not Match (expected=" + getProcessDeviceId(state) + " actual="
							+ scanlessTorqueMessage.getToolId() + ")", ", Disabling Torque");
					enable = false;
				}
			} else {
				Logger.getLogger().info("Tool Product Id does not Match(expected=" + state.getProductId() + " actual="
						+ scanlessTorqueMessage.getToolProductId() + ")", ", Disabling Torque");
				enable = false;
			}
		}else {
			Logger.getLogger().info(" No Message Found for Device-"+getProcessDeviceId(state));
		}
		return enable;
	}

	public void clearScanlessToolStatus(DataCollectionState state) {
		scanlessToolStatus.clear();
		
	}
}
