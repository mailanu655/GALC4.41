package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.fsm.AbstractDataCollectionState;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AbortJob;
import com.honda.galc.device.dataformat.InstructionCode;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * 
 * 
 * <h3>JobTorqueDeviceManager Class description</h3>
 * <p> JobTorqueDeviceManager description </p>
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
public class JobTorqueDeviceManager extends AbstractDeviceManager implements IJobTorqueDeviceObserver{

	public JobTorqueDeviceManager(DataCollectionController dcController) {
		super(dcController);
	}

	@Override
	protected void handleException(IDeviceData deviceData, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	public void abortJob(AbstractDataCollectionState torque) {
		if(hasEiTorqueDevice()) 
			abortJobEiDevice();
		else 
			abortJobSocketDevice();
	}

	public void sendStatus(ProcessProduct state) {
		// TODO Auto-generated method stub
		
	}

	public void setJob(ProcessTorque torque) {
		if(hasEiTorqueDevice()) 
			setJobEiDevice();
		else 
			setJobSocketDevice();
	}
	
	private void abortJobEiDevice() {
		getLogger().info("abortJobEiDevice.");
		IDeviceData deviceData = new AbortJob("true");
		sendEiDeviceData(deviceData );
		
	}
	
	protected void abortJobSocketDevice() {
		TorqueSocketDevice  device = (TorqueSocketDevice)DeviceManager.getInstance().getDevice(getModel().getDeviceId());
		
		if(device != null && device.isActive() && !device.isInstructionCodeSent()){
			device.abortJob();
			device.setInstructionCodeSent(false);
			getLogger().info("abortJobSocketDevice succeeded.");
		}
	}
	
	private void setJobEiDevice() {
		MCOperationRevision operation = getModel().getOperationsMap().get(getModel().getCurrentOperationName());
		String instructionCode = operation.getSelectedPart().getDeviceMsg(getModel().getCurrentMeasurementIndex());
		
		IDeviceData deviceData = new InstructionCode(getModel().getProductModel().getProductId(), instructionCode);
		getLogger().info("setJobEiDevice: " + "  job#:" + instructionCode);
		sendEiDeviceData(deviceData);
		
	}
	
	private void setJobSocketDevice() {
		String deviceId = getModel().getDeviceId();
		String instructionCode = getModel().getDeviceMsg();
		try {
			getLogger().info("start setJobSocketDevice: " + deviceId + "  job#:" + instructionCode);
			
			TorqueSocketDevice  device = (TorqueSocketDevice)DeviceManager.getInstance().getDevice(deviceId);
			if(device != null) {
			
				//Download product Id
				device.requestVinDownload(getModel().getProductModel().getProductId());
				
				//set job
				device.setJob(instructionCode);
				device.setInstructionCodeSent(true);
				getLogger().info("setJobSocketDevice succeeded.");
			}
			
		} catch (Exception e) {
			getLogger().error(e, "Exception:" + e.getMessage());
		}
	}
	
	private boolean hasEiTorqueDevice() {
		return DeviceManager.getInstance().hasEiDevice() 
				&& DeviceManager.getInstance().getEiDevice().containOutputDeviceData(AbortJob.class);
	}
	

}
