package com.honda.galc.client.datacollection.strategy;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.datacollection.processor.TorqueProcessor;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.HubPressData;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;

/**
 * 
 * <h3>HubPressStrategy</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HubPressStrategy description </p>
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
 * Dec 10, 2010
 *
 */
public class HubPressStrategy implements IDataCollectionTaskProcessor<HubPressData>{

	private DataCollectionController controller;
	private ClientContext context;
	private boolean overallStatus = true;
	
	public HubPressStrategy(ClientContext context) {
		this.context = context;
	}

	public boolean execute(HubPressData data) {
		// TODO Auto-generated method stub
		return false;
	}

	public void init() {
		controller = DataCollectionController.getInstance();
		
	}

	public IDeviceData processReceived(IDeviceData deviceData) {
		overallStatus = true;
		cleanUp();
		
		try {
			HubPressData hubPressData = (HubPressData) deviceData;
			
			//adjust the data here --- requested by plc guys
			//stroke /10 and pressure /100
			overallStatus = checkTorque(createMeasurement(hubPressData.getStoke()/10.0));
			checkTorque(createMeasurement(hubPressData.getPressure()/100.0));
		
		} catch (SystemException se){
			Logger.getLogger().error(se, this.getClass().getSimpleName() + " " + se.getMessage());
			controller.getFsm().error(new Message("HUB_PRESS", se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " " + e.toString());
			controller.getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, this.getClass().getSimpleName() + " "+ t.toString());
			controller.getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("TorqueProcessor :: Exit execute ng ");
		return DataCollectionComplete.OK();
	}

	private void cleanUp() {
		List<InstalledPart> partList = controller.getState().getProduct().getPartList();
		if(partList != null && partList.size() > 0){
			for(InstalledPart part : partList){
				List<Measurement> torqueList = part.getMeasurements();
				if(torqueList != null) torqueList.clear();
			}
		}
		
	}

	private boolean checkTorque(Measurement torque) {
		try {
			TorqueProcessor processor = (TorqueProcessor) controller.getProcessor(ProcessTorque.class);
			processor.checkTorqueValue(torque);
			torque.setMeasurementStatus(MeasurementStatus.OK);
			
			if(overallStatus)
				controller.getFsm().torqueOk(torque);
			else {
				controller.getFsm().torqueNg(torque, "OVERALL_STATUS_FAILED", "");
				controller.getFsm().reject1(); //get ready for re-do press
			}
			
			return true;
		}  catch (TaskException te) {
			Logger.getLogger().warn(this.getClass().getSimpleName() + " " + te.getMessage());
			handleInvalidTorque(te, torque);
		}
		return false;
	}

	private void handleInvalidTorque(TaskException te, Measurement measurement) {
		measurement.setMeasurementStatus(MeasurementStatus.NG);
		StringBuilder sb = new StringBuilder();
		sb.append(controller.getState().getCurrentPartName()).append(" ");
		sb.append(te.getMessage().substring(16)).append(" -").append(" press failed");
		
		controller.getFsm().torqueNg(measurement, "HUB_PRESS", sb.toString());
		if(controller.getState().getCurrentPartIndex() == 0){
			controller.getFsm().skipPart(); //to let the next torque go through
		}else if(controller.getState().getCurrentPartIndex() == 1)
			controller.getFsm().reject1(); //to let the re-do the press
		
	}

	private Measurement createMeasurement(double value) {
		Measurement measurement = new Measurement();
		measurement.setMeasurementValue(value);
		measurement.setMeasurementAngleStatusId(MeasurementStatus.OK.getId());
		measurement.setMeasurementValueStatusId(MeasurementStatus.OK.getId());
		measurement.setLastTighteningStatus(MeasurementStatus.OK);
		measurement.setPartSerialNumber("");
		
		return measurement;
	}

	public void registerDeviceListener(DeviceListener listener) {
		registerListener(EiDevice.NAME, listener, getProcessData());
	}

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new HubPressData());
		return list;
	}
	
	protected void registerListener(String deviceName, DeviceListener listener, List<IDeviceData> dataList) {
		IDevice eiDevice = DeviceManager.getInstance().getDevice(deviceName);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(listener, dataList);
		}
	}

	public ClientContext getContext() {
		return context;
	}

	public void setContext(ClientContext context) {
		this.context = context;
	}

}
