package com.honda.galc.client.datacollection.observer;


import java.util.List;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.ITorqueDevice;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AbortJob;
import com.honda.galc.device.dataformat.InstructionCode;
import com.honda.galc.entity.product.LotControlRule;
/**
 * 
 * <h3>LotControlJobTorqueManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlJobTorqueManager description </p>
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
 * @author Paul Chou
 * Apr 21, 2010
 *
 */
public class LotControlJobTorqueManager extends LotControlDeviceManager 
implements IJobTorqueDeviceObserver{
	public LotControlJobTorqueManager(ClientContext context) {
		super(context);
		
		initTorqueDevice(context.getAppContext().getApplicationId());
		AnnotationProcessor.process(this);
	}
	
	public void abortJob(DataCollectionState torque) {
		
		if(!hasTorque(torque.getCurrentLotControlRule())) return;
		
		if(torque.getAction() == Action.REJECT && torque.getCurrentTorqueIndex() != -1) return;
		
		doAbortJob(torque);

	}

	protected void doAbortJob(DataCollectionState torque) {
		if(isSocketDevice()){
			abortJobSocketDevice(torque);
		} else {
			abortJobEiDevice();
		}
	}

	protected void abortJobEiDevice() {
		Logger.getLogger().info("abortJobEiDevice.");
		IDeviceData deviceData = new AbortJob("true");
		sendDeviceData(deviceData );
		
	}

	protected void abortJobSocketDevice(DataCollectionState torque) {
		try {
			Logger.getLogger().info("start abortJobSocketDevice.");
			
			TorqueSocketDevice torqueSocketDevice = getTorqueDevice(torque.getCurrentLotControlRule());
			
			if(torqueSocketDevice.isActive() && isInstructionCodeSent(torque, torqueSocketDevice)){
				checkAndDoDelay(torqueSocketDevice, torque.getStateBean().getAction());
				torqueSocketDevice.abortJob();
				torqueSocketDevice.setInstructionCodeSent(false);
				Logger.getLogger().info("abortJobSocketDevice succeeded.");
			}
			
		} catch (Exception e) {
			torque.exception(new LotControlTaskException("Failed to abort Job:" + torque.getCurrentLotControlRule().getDeviceId(), this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception:" + e.getMessage());
		}
	}

	public void setJob(ProcessTorque torque) {
		if(isSocketDevice()){
			setJobSocketDevice(torque);
		} else {
			setJobEiDevice(torque);
		}

	}

	protected void setJobEiDevice(ProcessTorque torque) {
		IDeviceData deviceData = new InstructionCode(torque.getProductId(), torque.getCurrentLotControlRule().getInstructionCode());
		Logger.getLogger().info("setJobEiDevice: " + torque.getCurrentLotControlRule().getDeviceId() + 
				"  job#:" + torque.getCurrentLotControlRule().getInstructionCode());
		sendDeviceData(deviceData );
		
	}

	protected void setJobSocketDevice(ProcessTorque torque) {
		try {
			LotControlRule currentRule = torque.getCurrentLotControlRule();
			Logger.getLogger().info("start setJobSocketDevice: " + currentRule.getDeviceId() + "  job#:" + currentRule.getInstructionCode());
			
			TorqueSocketDevice torqueSocketDevice = getTorqueDevice(torque.getCurrentLotControlRule());
			
			//Download product Id
			torqueSocketDevice.requestVinDownload(torque.getProductId());
			
			//set job
			torqueSocketDevice.setJob(currentRule.getInstructionCode());
			torqueSocketDevice.setInstructionCodeSent(true);
			Logger.getLogger().info("setJobSocketDevice succeeded.");
			
		} catch (Exception e) {
			torque.exception(new LotControlTaskException("Failed to send Job:" + torque.getCurrentLotControlRule().getDeviceId(), this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception:" + e.getMessage());
		}
	}

	@Override
	protected void disableTorqueDevice(TorqueSocketDevice torqueSocketDevice) {
		try {
			Logger.getLogger().info("start disableTorqueDevice.");
			
			torqueSocketDevice.abortJob();
			Logger.getLogger().info("disableTorqueDevice succeeded.");
			
		} catch (Exception e) {
			getCurrentState(context.getAppContext().getApplicationId()).exception(new LotControlTaskException("Failed to disable torque controller.", 
					this.getClass().getSimpleName()), false);
			Logger.getLogger().error(e, "Exception:" + e.getMessage());
		}
	}

	@Override
	protected List<IDeviceData> getDeviceDataList() {
		
		List<IDeviceData> deviceDataList = super.getDeviceDataList();
		deviceDataList.add(new AbortJob());
		deviceDataList.add(new InstructionCode());
		
		return deviceDataList;
	}

	@Override
	protected void disableTorqueEiDevice() {
		abortJobEiDevice();
	}

	@Override
	public void cleanUp() {
		if(DataCollectionController.getInstance().getState() instanceof ProcessTorque)
			doAbortJob(DataCollectionController.getInstance().getState());
	}
		
	
	@EventSubscriber()
	public void onTorqueEvent(Event event) {
		if(event.isEvent(EventType.TORQUE_DISABLE_ALL)){
			handleTorqueDisableAll();
		}else if(event.isEvent(EventType.TORQUE_ENABLE)){
			handleTorqueEnable();
		}
	}
	
	protected void handleTorqueDisableAll() {
		abortJob(DataCollectionController.getInstance().getState());
		Logger.getLogger().info("Torque Controller disabled by Torque Disable All Event");
	}
	
	protected void handleTorqueEnable() {
		DataCollectionState currentState = DataCollectionController.getInstance().getState();
		if (currentState instanceof ProcessTorque) {
			setJob((ProcessTorque) currentState);
		}
		for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
			if (device instanceof ITorqueDevice) {
				TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
				torqueDevice.enableForJobMode();
			}
		}
	}

}
