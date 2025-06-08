package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;


public class ManualMeasurementOKAction extends BaseDataCollectionAction<MeasurementInputData>  {
	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		MeasurementInputData measurementInputData = (MeasurementInputData)event.getInputData();
		int measIndex = measurementInputData.getMeasurementIndex();
		MeasurementId measurementId = getMeasurementId(model, event, measIndex);
		Measurement measurement = createMeasurement(measurementId, MeasurementStatus.OK, (MeasurementInputData) event.getInputData());
		saveMeasurement(measurement);
	}

	public MeasurementId getMeasurementId(DataCollectionModel model, DataCollectionEvent event, int measIndex) {
		String productId = model.getProductModel().getProductId();
		String partName = event.getOperation().getId().getOperationName();
		return new MeasurementId(productId, partName, measIndex);
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
