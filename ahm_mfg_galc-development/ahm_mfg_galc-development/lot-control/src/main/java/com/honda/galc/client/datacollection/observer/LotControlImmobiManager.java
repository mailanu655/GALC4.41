package com.honda.galc.client.datacollection.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.immobi.IImmobiDeviceListener;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiSerialDevice;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiStateMachine;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiStateMachineType;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.exception.DeviceInUseException;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.ServiceFactory;

public class LotControlImmobiManager extends DataCollectionObserverBase
		implements IPartInstallObserver, IImmobiDeviceListener {

	protected ClientContext context;
	protected String defaultDeviceId = "immobi";
	protected Map<String, Integer> deviceAccessKeys = new HashMap<String, Integer>();
	private volatile Timer _processTimer = null;
	private DataCollectionState currentState = null;

	public LotControlImmobiManager(ClientContext context) {
		super();
		this.context = context;
	}

	public void partInstall(ProcessPart part) {
		this.currentState = part;

		try {
			if (canStartImmobiProcess()) {
				process();
			}
		} catch (Exception e) {
			part.error(new Message("MSG01", "failed to start Immobi Process."));
			Logger.getLogger("LotControlImmobiManager").error(e);
		}

	}

	private void monitorImmobilizeProcess(long timeOut) {
		_processTimer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				currentState.message(new Message("MSG01", "Operation Timed Out", MessageType.ERROR));
				releaseExclusiveDeviceAccess();
			}
		};
		_processTimer.schedule(timerTask, timeOut);
	}

	public void process() {

		if (_processTimer != null)
			_processTimer.cancel();
		String vin = currentState.getProductId();
		String mtoc = currentState.getProductSpecCode();
		String seq = currentState.getAfOnSeqNo();

		try {
			registerListeners();
			monitorImmobilizeProcess(getImmobiDevice().getTimeOut());
			if (!getImmobiDevice().isActive())
				getImmobiDevice().activate();
			updateImmobilizeStatus(DeviceMessageSeverity.info, "Communicating with Device");

			if (currentState.getScanCountOnRules() > 1) {
				getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITH_KEYSCAN);
				List<InstalledPart> partList = currentState.getProduct().getPartList();
				setKeyScans(partList);

			} else {
				// check all installed parts if keys scans and immobilizer
				// process are at different process points
				List<InstalledPart> partList = getInstalledParts();
				if (partList.size() > 0) {
					getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITH_KEYSCAN);
					setKeyScans(partList);
				} else {
					getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITHOUT_KEYSCAN);
				}
			}
			getImmobiDevice().processVIN(vin, mtoc, seq);
		} catch (DeviceInUseException ex) {
			currentState.message(new Message("MSG01",
					"Device " + ex.getDeviceName() + " is in use by " + ex.getApplicationName(), MessageType.ERROR));
			return;
		} catch (Exception ex) {
			Logger.getLogger().info("processVIN Exception : " + ex.toString());
			currentState.message(new Message("MSG01", "Exception immobilizing VIN " + vin, MessageType.ERROR));
		}
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return deviceAccessKeys.get(deviceId);
	}

	public void setDeviceAccessKey(String deviceId, Integer key) {
		deviceAccessKeys.put(deviceId, key);
	}

	public String getListenerId() {
		return "ImmobiListener";
	}

	public synchronized void handleStatusChange(ImmobiDeviceStatusInfo statusInfo) {
		try {
			int immobiState = statusInfo.getState();
			updateImmobilizeStatus(statusInfo.getMessageSeverity(), statusInfo.getDisplayMessage());
			if (immobiState == ImmobiStateMachine.ABORT) {
				String errorMessage = getImmobiDevice().getStateMachine().getErrorMessage();
				updateImmobilizeStatus(statusInfo.getMessageSeverity(), errorMessage);
				
				releaseExclusiveDeviceAccess();
				getImmobiDevice().getStateMachine().reset();
				equipmentInitiatedRefresh();
				if (errorMessage.toLowerCase().contains("abort")) {
					updateImmobilizeStatus(DeviceMessageSeverity.error,
							"REFRESH SIGNAL RECEIVED FROM IMMOBI EQUIPMENT");
					LotControlAudioManager.getInstance().playRepeatedNGSound(currentState);

				} else {
					updateImmobilizeStatus(DeviceMessageSeverity.error, errorMessage);
					LotControlAudioManager.getInstance().playNGSoundForImmobi();

				}
			} else if (getImmobiDevice().getStateMachineType().equals(ImmobiStateMachineType.WITHOUT_KEYSCAN)
					&& immobiState == ImmobiStateMachine.COMPLETED_VIN_PROCESSING
					|| getImmobiDevice().getStateMachineType().equals(ImmobiStateMachineType.WITH_KEYSCAN)
							&& immobiState == ImmobiStateMachine.COMPLETED_VIN_PROCESSING_WITH_KEYSCAN) {
				releaseExclusiveDeviceAccess();
				getImmobiDevice().getStateMachine().reset();
			}
		} catch (Exception e) {
			handleException(e.getMessage());
		}
	}

	private void equipmentInitiatedRefresh() {
		try {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					DataCollectionController.getInstance().getFsm().cancel();
				}
			}, 10000);

		} catch (Exception e) {
			handleException(e.getMessage());
		}
	}

	public ImmobiSerialDevice getImmobiDevice() throws DeviceInUseException {
		return (ImmobiSerialDevice) DeviceManager.getInstance().getDevice(defaultDeviceId, this);
	}

	protected void handleException(String info) {
		updateImmobilizeStatus(DeviceMessageSeverity.error, "Exception occurred while processing Immobi:" + info);
		throw new TaskException(info, this.getClass().getSimpleName());
	}

	protected void releaseExclusiveDeviceAccess() {
		if (_processTimer != null)
			_processTimer.cancel();

		try {
			getImmobiDevice().unregisterListener(this);
		} catch (Exception ex) {
			Logger.getLogger().error("Could not unregister as listener to immobi device " + defaultDeviceId);
			updateImmobilizeStatus(DeviceMessageSeverity.error,
					"Could not unregister as listener to immobi device " + defaultDeviceId);
		}

		if (!DeviceManager.getInstance().releaseExclusiveAccess(defaultDeviceId, this))
			Logger.getLogger()
					.info("Unable to release Immobilizer device exclusive access by " + this.getClass().toString());
	}

	public void updateImmobilizeStatus(DeviceMessageSeverity msgType, String msg) {
		Logger.getLogger().info("ThreadID = " + Thread.currentThread().getName() + " Status from immobilizer: " + msg);
		MessageType type = MessageType.INFO;
		Action action = Action.MESSAGE;
		switch (msgType) {
		case info:
			type = MessageType.INFO;
			break;
		case warning:
			type = MessageType.WARN;
			action = Action.ERROR;
			break;
		case success:
			break;
		case error:
			type = MessageType.ERROR;
			action = Action.ERROR;
			break;
		default:
			type = MessageType.INFO;
			break;
		}

		((ProcessPart) currentState).message(new Message("MSG01", msg, type));
		currentState.stateChanged(action);
		if (msg.contains("REG_OK")) {
			DataCollectionController.getInstance().getFsm().partSnOk(getInstalledPart());
		}
	}


	private boolean canStartImmobiProcess() {

		int noOfPartScans = currentState.getScanCountOnRules();
		if (noOfPartScans == 1) {
			return true;
		}

		if (noOfPartScans > 1) {
			int partListSize = currentState.getProduct().getPartList().size();
			if (partListSize < noOfPartScans - 1) {
				return false;
			}
		}
		return true;
	}

	private List<String> getKeyScanPartNames() {
		String opName = context.getProperty().getKeyScanOps();
		List<String> partNames = new ArrayList<String>();

		if (!StringUtils.isEmpty(opName)) {
			String[] items = opName.split(Delimiter.COMMA);
			for (String str : items) {
				partNames.add(str.toUpperCase());
			}
		}
		return partNames;
	}

	private List<InstalledPart> getInstalledParts() {
		return  ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(currentState.getProductId(), getKeyScanPartNames());
	}

	private void setKeyScans(List<InstalledPart> parts) {
		try {
			for (InstalledPart part : parts) {
				String value = part.getPartSerialNumber();
				if (value.startsWith(context.getProperty().getFirstKeyPartMask())) {
					getImmobiDevice().setFirstKeyScan(value);
				} else if (value.startsWith(context.getProperty().getSecondKeyPartMask())) {
					getImmobiDevice().setSecondKeyScan(value);
				}
			}
		} catch (Exception ex) {
			Logger.getLogger().info("Exception : " + ex.toString());
			updateImmobilizeStatus(DeviceMessageSeverity.error, "Exception setting keys");
		}
	}

	private InstalledPart getInstalledPart() {
		InstalledPart installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		installedPart.setProcessPointId(context.getProcessPointId());
		installedPart.setProductType(context.getProperty().getProductType());
		String productSpecCode = (currentState.getProductSpecCode()).substring(0, 9);
		StringBuilder partSerialNo = new StringBuilder();
		partSerialNo.append(currentState.getProductId()).append(",").append(productSpecCode).append(",");
		installedPart.setPartId(currentState.getCurrentLotControlRule().getParts().get(0).getId().getPartId());
		installedPart.setPartSerialNumber(partSerialNo.toString());
		installedPart.setValidPartSerialNumber(true);
		 
		return installedPart;
	}
	
	public String getApplicationName() {
		return context.getProcessPointId();
	}
	
	private void registerListeners() throws DeviceInUseException{
		getImmobiDevice().registerListener(this);
	}

	public void controlGranted(String deviceId) {
		// TODO Auto-generated method stub

	}

	public void controlRevoked(String deviceId) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void handleDataCollectionCancel(ProcessPart part) {
		// TODO Auto-generated method stub
		
	}
}
