package com.honda.galc.client.dc.action;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.dataformat.DataCollectionIndexData;
import com.honda.galc.device.dataformat.MeasurementInputData;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class MeasurementSkipAction extends BaseDataCollectionAction<MeasurementInputData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {	
		DataCollectionIndexData dcIndexData = (DataCollectionIndexData)event.getInputData();
		int measurementIndex = dcIndexData.hasScanPart() ? dcIndexData.getInputIndex() : dcIndexData.getInputIndex() + 1;
		getLogger().check("Skipping Measurement " + measurementIndex);
		
		ArrayList<Integer> skippedMeasurementsList = getSkippedMeasurementsList(model, event);
		if (!skippedMeasurementsList.contains(measurementIndex) && !dcIndexData.hasExceededMaxAttempts()) {
			skippedMeasurementsList.add(measurementIndex);
		}
		if (markIfOpCompleted(model, event.getOperation())) {
			disableTorqueDevices();
		}
		EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.SKIP_MEASUREMENT_RECEIVED, event.getOperation(), event.getInputData()));
	}

	public String getCheckPointName() {
		return "";
	}


	

	
	
	private ArrayList<Integer> getSkippedMeasurementsList(DataCollectionModel model, DataCollectionEvent event) {
		String operationName = event.getOperation().getId().getOperationName();
		if (model.getSkippedMeasurementsMap().get(operationName) == null) {
			model.getSkippedMeasurementsMap().put(operationName, new ArrayList<Integer>());
		}
		return model.getSkippedMeasurementsMap().get(operationName);
	}

	public boolean dispatchReactions(List<CheckResult> checkResults,
			MeasurementInputData inputData) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
