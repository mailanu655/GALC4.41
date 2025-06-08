package com.honda.galc.client.dc.manager;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.dc.event.PsetCommandAcceptedEvent;
import com.honda.galc.client.dc.event.PsetErrorEvent;
import com.honda.galc.client.dc.event.TorqueDeviceErrorEvent;
import com.honda.galc.client.dc.event.TorqueEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.lotcontrol.ITorqueDeviceListener;
import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.ActivityEvent;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AbortJob;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.InstructionCode;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.openprotocol.model.SpindleStatus;

/**
 * 
 * 
 * <h3>DeviceDataManager Class description</h3>
 * <p> DeviceDataManager description </p>
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
 * @author Jeffray Huang<br>
 * Mar 4, 2014
 *
 *
 */
public class DeviceDataManager extends AbstractDeviceManager implements ITorqueDeviceListener, DeviceListener {

	public DeviceDataManager(DataCollectionController dcController) {
		super(dcController);
		initTorqueDevices();
	}

	private void initTorqueDevices() {
		initTorqueSocketDevices();
		initEiDevice();
	}
	
	private void initTorqueSocketDevices() {
		for(IDevice device : DeviceManager.getInstance().getDevices().values()){
			if(device instanceof TorqueSocketDevice && device.isEnabled()){
				initTorqueDevice((TorqueSocketDevice)device);
				//disable torque guns when starting client
				disableTorqueDevice((TorqueSocketDevice)device);
				registerTorqueDeviceListener((TorqueSocketDevice)device);
				((TorqueSocketDevice)device).requestControl(this);
			}
		}
	}
	
	private void initEiDevice() {
		if(DeviceManager.getInstance().hasEiDevice() 
				&& DeviceManager.getInstance().getEiDevice().containOutputDeviceData(AbortJob.class)){
			abortJobEiDevice();
			registerEiDeviceData();
			registerEiDeviceListener();
		}	
	}

	private void initTorqueDevice(TorqueSocketDevice device) {
		device.startDevice();
	}
	
	private void abortJobEiDevice() {
		getLogger().info("abortJobEiDevice.");
		sendDeviceData(new AbortJob("true"));
	}
	
	protected void registerEiDeviceData(){
		DeviceManager.getInstance().getEiDevice().reqisterDeviceData(getDeviceDataList()) ;
	}
	
	protected void registerEiDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		device.registerDeviceListener(this, getEiProcessData());
	}
	
	protected List<IDeviceData> getEiProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new LastTighteningResult());
		return list;
	}
	
	protected List<IDeviceData> getDeviceDataList() {
		
		List<IDeviceData> deviceDataList = new ArrayList<IDeviceData>();
		deviceDataList.add(new AbortJob());
		deviceDataList.add(new InstructionCode());
		
		return deviceDataList;
	}
	
	public void disableTorqueDevice(TorqueSocketDevice torqueSocketDevice) {
		try {
			getLogger().check("Attempting to disable Torque device " + torqueSocketDevice.getId());
			
			torqueSocketDevice.disable();
			getLogger().check("Torque device " + torqueSocketDevice.getId() + " disabled");
		} catch (Exception ex) {
			getLogger().error(ex, "Exception:" + ex.getMessage());
		}
	}
	
	public void enableTorqueDevice(TorqueSocketDevice torqueSocketDevice) {
		try {
			getLogger().check("Attempting to enable Torque device " + torqueSocketDevice.getId());
			
			torqueSocketDevice.enable();
			getLogger().check("Torque device " + torqueSocketDevice.getId() + " enabled");
		} catch (Exception ex) {
			getLogger().error(ex, "Exception:" + ex.getMessage());
		}
	}
	
	public void enableTorqueDevices() {
		for(IDevice device : DeviceManager.getInstance().getDevices().values()){
			if(device instanceof TorqueSocketDevice){
				enableTorqueDevice((TorqueSocketDevice)device);
			}
		}
	}
	
	public void disableTorqueDevices() {
		for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
			if (device instanceof TorqueSocketDevice) {
				disableTorqueDevice((TorqueSocketDevice) device);
			}
		}
	}
	
	protected void registerTorqueDeviceListener(TorqueSocketDevice device){
		device.registerListener(this);
	}

	@Override
	protected void handleException(IDeviceData deviceData, Throwable e) {
		// TODO Auto-generated method stub
	}

	public String getId() {
		return this.getClass().getSimpleName();
	}

	public void handleStatusChange(String deviceId, TorqueDeviceStatusInfo statusInfo) {
		// if any type of command error occurs then error msg will be displayed
		if (statusInfo.getCommandError() != null) {
			getLogger().error(statusInfo.getMessage());
			switch (statusInfo.getCommandError()) {
			case psetNotPresent:
			case psetCannotBeSet:
			case psetNotRunning:
				EventBusUtil.publish(new PsetErrorEvent(deviceId, statusInfo));
				break;
			default:
				EventBusUtil.publish(new TorqueDeviceErrorEvent(deviceId, statusInfo));
			}
		}
	}

	public void processLastTighteningResult(String deviceId, LastTighteningResult tighteningResult) {
		EventBusUtil.publish(new TorqueEvent(tighteningResult));
	}

	public void processMultiSpindleResult(String deviceId,
			MultiSpindleResultUpload multiSpindleResult) {
		//if any of the status failed then we fail the all the results
		int allAngleStatus = 1;
		int allTorqueStatus = 1;
		
		EventBusUtil.publish(new ActivityEvent());
		
		for(SpindleStatus status : multiSpindleResult.getSpindleStatusList()){
			allAngleStatus = allAngleStatus & status.getAngleStatus();
			allTorqueStatus = allTorqueStatus & status.getTorqueStatus();
		}
		
		for(SpindleStatus status : multiSpindleResult.getSpindleStatusList()){
			LastTighteningResult tighteningResult = createTighteningResult(multiSpindleResult, status, allTorqueStatus, allAngleStatus);
			DataCollectionComplete returnStatus = 
				(DataCollectionComplete)getController().processTorqueData(deviceId,tighteningResult);
			
			if(returnStatus.getCompleteFlag().equals("0")){
				getLogger().warn("Failed on multi-spindle result, spindle number:" + status.getSpindleNumber());
				return;
			}
		}
	}
	
	private LastTighteningResult createTighteningResult(MultiSpindleResultUpload multiSpindleResult, 
			SpindleStatus status, int allTorqueStatus, int allAngleStatus) {
		LastTighteningResult result = new LastTighteningResult();
		
		result.setTighteningId(multiSpindleResult.getSyncTighteningId());
		result.setProductId(multiSpindleResult.getProductId().trim());
		
		//If any of the two torque failed then we would like to fail both of them, even the 1st one
		result.setTighteningStatus(status.getOverallStatus() & multiSpindleResult.getSyncOverallStatus());
		result.setTorque(status.getTorqueResult());
		result.setTorqueStatus(status.getTorqueStatus() & allTorqueStatus);
		result.setAngleStatus(status.getAngleStatus() & allAngleStatus);
		result.setAngle(status.getAngleResult());
		
		return result;
	}


	public void controlGranted(String deviceId) {
		IDevice iDevice = DeviceManager.getInstance().getDevice(deviceId);
		if (iDevice instanceof TorqueSocketDevice){
			((TorqueSocketDevice) DeviceManager.getInstance().getDevice(deviceId)).disable();
		}
	}

	public void controlRevoked(String deviceId) {
		
	}
	
	public String getApplicationName() {
		return null;
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return null;
	}

	public void update(Object obj) {
		
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void handleCommandAccepted(String deviceId, CommandAccepted commandAccepted) {
		EventBusUtil.publish(new PsetCommandAcceptedEvent(deviceId, commandAccepted));		
	}
}
