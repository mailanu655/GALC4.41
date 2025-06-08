package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.dataformat.DataCollectionIndexData;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.entity.product.MeasurementId;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class MeasurementRejectAction extends BaseDataCollectionAction<MeasurementInputData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		DataCollectionIndexData dcIndexData = (DataCollectionIndexData)event.getInputData();
		int measurementIndex = dcIndexData.hasScanPart() ? dcIndexData.getInputIndex() : dcIndexData.getInputIndex() + 1;
		getLogger().check("Rejecting Measurement " + measurementIndex);

		deleteMeasurement(model, measurementIndex);
		removeFromCompleteOpsMap(event);
		EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.REJECT_MEASUREMENT_RECEIVED, event.getOperation(), event.getInputData()));
	}

	public void deleteMeasurement(DataCollectionModel model, int measurementIndex) {
		MeasurementId id = new MeasurementId();
		id.setProductId(model.getProductModel().getProductId());
		id.setPartName(model.getCurrentOperationName());
		id.setMeasurementSequenceNumber(measurementIndex);
		rejectMeasurement(id);
	}
	
	public String getCheckPointName() {
		return "";
	}



	public boolean dispatchReactions(List<CheckResult> checkResults, MeasurementInputData inputData) {

		// TODO Auto-generated method stub
		return false;
	}

	
}
