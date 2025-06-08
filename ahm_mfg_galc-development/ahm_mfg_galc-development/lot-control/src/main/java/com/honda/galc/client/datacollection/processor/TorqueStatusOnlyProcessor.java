package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.openprotocol.model.LastTighteningResult;

public class TorqueStatusOnlyProcessor extends TorqueProcessor {

	public TorqueStatusOnlyProcessor(ClientContext context) {
		super(context);
	}

	public void confirmTorqueValue(LastTighteningResult lastTighteningResult) throws TaskException
	{
		if(context.getProperty().isCheckTighteningId()) {
			validateTighteningId(lastTighteningResult);
		}
		measurement = getMeasurementStatusOnly(lastTighteningResult);
	}

	private Measurement getMeasurementStatusOnly(LastTighteningResult lastTighteningResult) {
		String msgString = null;
		try {
			measurement = new Measurement();
			measurement.setLastTighteningStatusId(lastTighteningResult.getTighteningStatus());
			measurement.setMeasurementStatusId(lastTighteningResult.getTighteningStatus());
			MeasurementId id = new MeasurementId();
			id.setProductId(lastTighteningResult.getProductId() == null ? 
					DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState().getProductId() : lastTighteningResult.getProductId().trim());
			measurement.setId(id);
			measurement.setPartSerialNumber("");
			measurement.setMeasurementValue(0);
		

		} catch (Exception e) {
			handleException("Failed to get Torque - exception:" + e.getMessage());
		}

		if (measurement.getLastTighteningStatus() != MeasurementStatus.OK)
			msgString = ("OverallTighteningStatus:" + measurement.getLastTighteningStatus());

		if(msgString != null) handleException(msgString);

		return measurement;
	}
}
