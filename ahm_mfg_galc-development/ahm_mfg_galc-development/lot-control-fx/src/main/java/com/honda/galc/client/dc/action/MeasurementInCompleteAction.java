package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.dataformat.MeasurementInputData;

public class MeasurementInCompleteAction extends BaseDataCollectionAction<MeasurementInputData>{

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		model.markComplete(event.getOperation().getId().getOperationName(), false);
		model.markSkippedPart(event.getOperation().getId().getOperationName(), false);
		EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_REJECTED_FOR_PART, event.getOperation()));
	}

	public boolean dispatchReactions(List<CheckResult> checkResults, MeasurementInputData inputData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCheckPointName() {
		// TODO Auto-generated method stub
		return null;
	}

}
