package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.view.RearSealTiltCalculationView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class MeasurementOkAction extends BaseDataCollectionAction<MeasurementInputData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		try {
			MeasurementInputData measurementInputData = (MeasurementInputData)event.getInputData();
			model.setCurrentMeasurementIndex(measurementInputData.getMeasurementIndex());
			String deviceId = model.getCurrentMeasurement(measurementInputData.getMeasurementIndex()-1).getDeviceId();
			
			Measurement measurement = createMeasurement(model, measurementInputData, event.getOperation(), MeasurementStatus.OK);
			saveMeasurement(model, event.getOperation(), measurement);
			notifyMeasurementOk((MeasurementInputData) event.getInputData(), event.getOperation());

			if(!event.getOperation().getView().equalsIgnoreCase(RearSealTiltCalculationView.class.getCanonicalName().toString())) {
				if (markIfOpCompleted(model, event.getOperation())) {
					disableTorqueDevice(deviceId);
				}
			}
		} catch (ServiceTimeoutException e) {
			getLogger().error(e, "Unable to reach server. Please check network connection.");
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_ERROR_REPORTED, "Unable to reach server. Please check network connection."));
		} catch (Exception e) {
			getLogger().error(e, "An Exception occured during Data Collection");
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_ERROR_REPORTED, "An Exception occured during Data Collection"));
		}
	}
	
	public String getCheckPointName() {
		return "";
	}


	public boolean dispatchReactions(List<CheckResult> checkResults, MeasurementInputData inputData) {

		// TODO Auto-generated method stub
		return false;
	}


}
