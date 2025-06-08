package com.honda.galc.client.dc.processor;

import java.util.Timer;
import java.util.TimerTask;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.view.ImmobiView;
import com.honda.galc.client.device.lotcontrol.immobi.IImmobiDeviceListener;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiSerialDevice;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiStateMachine;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiStateMachineType;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.exception.DeviceInUseException;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */

public class ImmobiRuleProcessor extends OperationProcessor implements IImmobiDeviceListener {

	public final static String IMMOBI_DEVICE_ID = "immobi";
	private volatile Timer _processTimer = null;
	public ImmobiView view;
	public final static String OPERATION_TIMED_OUT_MSG = "Operation Timed Out";
	private volatile Integer _deviceAccessKey = -1;

	public ImmobiView getView() {
		return view;
	}

	public void setView(ImmobiView view) {
		this.view = view;
	}

	
	public ImmobiRuleProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	public ApplicationContext getApplicationContext() {
		return this.getController().getModel().getProductModel().getApplicationContext();
	}

	private void monitorImmobilizeProcess(long timeOut) {
		_processTimer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				EventBusUtil.publish(new StatusMessageEvent(OPERATION_TIMED_OUT_MSG, StatusMessageEventType.ERROR));
				releaseExclusiveDeviceAccess();
			}
		};
		_processTimer.schedule(timerTask, timeOut);
	}

	private void updateImmobilizeStatus(String msg) {
		Logger.getLogger().info("ThreadID = " + Thread.currentThread().getName()+ " Status from immobilizer: " + msg);
		if (getView().getStatusTxtField().isVisible())			
		{
			getView().getStatusTxtField().setText(msg);
		}
		else
			Logger.getLogger().info("Not expecting any messages.  Discarding message: " + msg);		
	}
	public void process()
	{

		if (_processTimer != null)
			_processTimer.cancel();
		String vin = getController().getModel().getProductModel().getProductId();
		String mtoc = getController().getModel().getProductModel().getProductSpec().getProductSpecCode();
		String seq = "";
		try {
			getView().getStatusTxtField().setVisible(true);				
			getImmobiDevice().registerListener(this);
			monitorImmobilizeProcess(getImmobiDevice().getTimeOut());
			if(!getImmobiDevice().isActive())
				getImmobiDevice().activate();
			updateImmobilizeStatus( "Communicating with Device");
		} catch (DeviceInUseException ex) {
			EventBusUtil.publish(new StatusMessageEvent("Device " + ex.getDeviceName() + " is in use by "+ ex.getApplicationName(), StatusMessageEventType.ERROR));
			return;
		}
		try {
			if(getController().getModel().getOperations().size()>1)
			{
				getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITH_KEYSCAN);

			}else
			{
				getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITHOUT_KEYSCAN);
			}
			getImmobiDevice().processVIN(vin, mtoc, seq);
		} catch (Exception ex) {
			Logger.getLogger().info("processVIN Exception : " + ex.toString());
			EventBusUtil.publish(new StatusMessageEvent("Exception immobilizing VIN " + vin, StatusMessageEventType.ERROR));
		}

	}

	public void finish() {
		completeOperation(true);
	}

	public synchronized void handleStatusChange(ImmobiDeviceStatusInfo statusInfo) {
		try {
			int currentState = statusInfo.getState();
	       getView().updateImmobilizeStatus(statusInfo.getMessageSeverity().toString(),statusInfo.getSeqNumber(), statusInfo.getVIN(), statusInfo.getDisplayMessage());
			if (currentState == ImmobiStateMachine.ABORT) {
				String errorMessage = getImmobiDevice().getStateMachine().getErrorMessage();
				EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.ERROR));
				releaseExclusiveDeviceAccess();
				getImmobiDevice().getStateMachine().reset();
				equipmentInitiatedRefresh();
				if (errorMessage.toLowerCase().contains("abort")) {					
					EventBusUtil.publish(new StatusMessageEvent("REFRESH SIGNAL RECEIVED FROM IMMOBI EQUIPMENT", StatusMessageEventType.ERROR));
					getView().getAudioManager().playRepeatedNgSound();
				} else {
					EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.ERROR));
					getView().getAudioManager().playNGSound();
				}				
			}
			else if (getImmobiDevice().getStateMachineType().equals(ImmobiStateMachineType.WITHOUT_KEYSCAN)&&currentState == ImmobiStateMachine.COMPLETED_VIN_PROCESSING ||getImmobiDevice().getStateMachineType().equals(ImmobiStateMachineType.WITH_KEYSCAN)&&currentState == ImmobiStateMachine.COMPLETED_VIN_PROCESSING_WITH_KEYSCAN) {
				releaseExclusiveDeviceAccess();
				getImmobiDevice().getStateMachine().reset();
			}
		}  catch (Exception e) {
			handleException(e.getMessage());
		}
	}

	private void equipmentInitiatedRefresh() {
		try {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	getController().cancel();
	   			   getController().getView().reload();
	            }
	        },  10000);
			
		} catch (Exception e) {
			handleException(e.getMessage());
		}
	}

	public ImmobiSerialDevice getImmobiDevice() throws DeviceInUseException {
		return (ImmobiSerialDevice) DeviceManager.getInstance().getDevice(IMMOBI_DEVICE_ID, this);
	}

	protected void handleException(String info) {
		EventBusUtil.publish(new StatusMessageEvent("Exception occurred while processing Immobi:" + info, StatusMessageEventType.ERROR));
		throw new TaskException(info, this.getClass().getSimpleName());
	}

	protected void releaseExclusiveDeviceAccess() {
		if (_processTimer != null)
			_processTimer.cancel();

		try {
			((ImmobiSerialDevice) DeviceManager.getInstance().getDevice(IMMOBI_DEVICE_ID)).unregisterListener(this);
		} catch (Exception ex) {
			Logger.getLogger().error("Could not unregister as listener to immobi device "+ IMMOBI_DEVICE_ID);
			EventBusUtil.publish(new StatusMessageEvent("Could not unregister as listener to immobi device "+ IMMOBI_DEVICE_ID, StatusMessageEventType.ERROR));
			
		}

		if (!DeviceManager.getInstance().releaseExclusiveAccess(IMMOBI_DEVICE_ID, this))
			Logger.getLogger().info("Unable to release Immobilizer device exclusive access by "+ this.getClass().toString());
	}

	public String getListenerId() {
		return "ImmobiRuleProcessor";
	}

	public void controlGranted(String deviceId) {
		// TODO Auto-generated method stub

	}

	public void controlRevoked(String deviceId) {
		// TODO Auto-generated method stub

	}

	public String getApplicationName() {
		return getController().getProcessPointId();
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return _deviceAccessKey;
	}

	public void update(Object obj) {
	}
	
	
}
