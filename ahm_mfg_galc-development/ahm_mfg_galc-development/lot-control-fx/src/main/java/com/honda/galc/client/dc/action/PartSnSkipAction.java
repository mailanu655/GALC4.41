package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialScanData;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class PartSnSkipAction extends BaseDataCollectionAction<PartSerialScanData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		getLogger().check("Skipping Part");
		model.markSkippedPart(event.getOperation().getId().getOperationName(),true);
		EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.SKIP_PART_SCAN_RECEIVED, event.getOperation(), event.getInputData()));
	}
	
	public String getCheckPointName() {
		return "";
	}

	public boolean dispatchReactions(List<CheckResult> checkResults, PartSerialScanData inputData) {
		return false;
	}
}


