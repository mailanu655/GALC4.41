package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialScanData;

/**
 * @author Subu Kathiresan
 * @date Dec 4, 2014
 */

public class WaitingForPartSnAction extends BaseDataCollectionAction<PartSerialScanData> {



	private static String checkPointName = CheckPoints.BEFORE_PART_SCAN.toString();
	
	public WaitingForPartSnAction() {
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}
	
	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		disableTorqueDevices();
	}


	
	public String getCheckPointName() {
		return checkPointName;
	}

	public boolean dispatchReactions(List<CheckResult> checkResults,
			PartSerialScanData inputData) {
		// TODO Auto-generated method stub
		return false;
	}
}
