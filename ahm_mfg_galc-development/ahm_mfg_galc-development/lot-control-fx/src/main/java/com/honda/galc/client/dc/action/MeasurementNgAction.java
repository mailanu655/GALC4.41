package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.device.dataformat.DataCollectionIndexData;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class MeasurementNgAction extends BaseDataCollectionAction<MeasurementInputData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		try {
			MeasurementInputData measurementInputData = (MeasurementInputData)event.getInputData();
			int measIndex = measurementInputData.getMeasurementIndex();
			
			MeasurementId measurementId = getMeasurementId(model, event, measIndex);
			if (!model.getBadMeasurementAttemptsMap().containsKey(measurementId)) {
				model.getBadMeasurementAttemptsMap().put(measurementId, 1);
			} 
			
			int maxAttempts = event.getOperation().getSelectedPart().getMeasurements().get(measIndex - 1).getMaxAttempts();
			model.getBadMeasurementAttemptsMap().put(measurementId, measurementInputData.getAttemptNumber());

			Measurement measurement = createMeasurement(measurementId, MeasurementStatus.NG, (MeasurementInputData) event.getInputData());

			
			saveMeasurement(model, event.getOperation(), measurement);
			
			if (measurementInputData.getAttemptNumber() > maxAttempts) {

				notifyMeasurementNg(measurementInputData, event.getOperation(), "Exceeded maximum number of bad attempts (" 

						+ measurementInputData.getAttemptNumber()
						+ " of "
						+ maxAttempts + ")");
				model.getBadMeasurementAttemptsMap().clear();
				EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_SKIP, getDataCollectionIndex(event.getOperation(), measIndex)));
			}
			
			markIfOpCompleted(model, event.getOperation());
		} catch (ServiceTimeoutException e) {
			getLogger().error(e, "Unable to reach server. Please check network connection.");
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_ERROR_REPORTED, "Unable to reach server. Please check network connection."));
		} catch (Exception e) {
			getLogger().error(e, "An Exception occured during Data Collection");
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_ERROR_REPORTED, "An Exception occured during Data Collection"));
		}
	}

	public MeasurementId getMeasurementId(DataCollectionModel model, DataCollectionEvent event, int measIndex) {
		String productId = model.getProductModel().getProductId();
		String partName = event.getOperation().getId().getOperationName();
		return new MeasurementId(productId, partName, measIndex);
	}
	
	public DataCollectionIndexData getDataCollectionIndex(MCOperationRevision operation, int btnIndex) {
		DataCollectionIndexData dcIndex = new DataCollectionIndexData();
		dcIndex.setInputIndex(btnIndex);
		dcIndex.setHasScanPart(DataCollectionModel.hasScanPart(operation));
		dcIndex.setExceededMaxAttempts(true);
		return dcIndex;
	}
	
	public String getCheckPointName() {
		return "";
	}


	
	public boolean dispatchReactions(List<CheckResult> checkResults, MeasurementInputData inputData) {

		// TODO Auto-generated method stub
		return false;
	}

	
}
