package com.honda.galc.client.dc.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.device.DeviceStatusWidgetEvent;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.DataCollectionIndexData;
import com.honda.galc.device.dataformat.MeasurementInputData;

/**
 * @author Subu Kathiresan
 * @date Dec 4, 2014
 */

public class WaitingForMeasurementAction extends BaseDataCollectionAction<MeasurementInputData> {


	
	private static String checkPointName = CheckPoints.BEFORE_MEASUREMENT_INPUT.toString();
	
	public WaitingForMeasurementAction() {
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}
	

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		DataCollectionIndexData dcIndexData = (DataCollectionIndexData) event.getInputData();
		int measIndex = DataCollectionModel.hasScanPart(getOperation()) ? dcIndexData.getInputIndex() - 1 : dcIndexData.getInputIndex();
		model.getMeasIndexMap().put(getOperation().getId().getOperationName(), measIndex);
		
		String deviceId = model.getCurrentMeasurement().getDeviceId();
		if (StringUtils.isNotBlank(deviceId)) {
			IDevice device = DeviceManager.getInstance().getDevice(deviceId);
			EventBusUtil.publish(new DeviceStatusWidgetEvent<IDevice>(device, true));
			if (device instanceof TorqueSocketDevice) {
				enableTorqueDevice(model, device);
			}
		}
	}

	private void enableTorqueDevice(DataCollectionModel model, IDevice device) {
		String instructionCode = model.getCurrentMeasurement().getDeviceMsg();
			TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
			if (!torqueDevice.isEnabled() ||
					!torqueDevice.isToolEnabled() ||
					torqueDevice.getCurrentInstructionCode() == null ||
					!torqueDevice.getCurrentInstructionCode().equals(instructionCode)) {
				//bak - 20150730 - Only send VIN to controller when enabling, do not need to set on every Pset Change
				boolean sendVin = (!torqueDevice.isEnabled() || !torqueDevice.isToolEnabled() || torqueDevice.getCurrentInstructionCode() == null);
				torqueDevice.enable(instructionCode);
				
				if (sendVin) torqueDevice.requestVinDownload(getModel().getProductModel().getProductId());								
			}
	}

	public boolean dispatchReactions(List<CheckResult> checkResults, DataCollectionIndexData inputData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCheckPointName() {
		return checkPointName;
	}


	public boolean dispatchReactions(List<CheckResult> checkResults,
			MeasurementInputData inputData) {
		// TODO Auto-generated method stub
		return false;
	}
}
