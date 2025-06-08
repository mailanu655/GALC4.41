package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Suriya Sena
 * @date February 23, 2015
 */
public class PddaRejectAction extends BaseDataCollectionAction<InputData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		try {
			InstalledPart installedPart = getInstalledPart(model, event.getOperation());
			if (installedPart == null) {
				installedPart = createInstalledPart(model, null, event.getOperation(), InstalledPartStatus.NG);
			}
			rejectPart(installedPart);	
			
			// Notify the data collection framework, the part has been rejected
			model.getCompletedOpsMap().put(event.getOperation().getId().getOperationName(), false);
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_REJECTED_FOR_PART, event.getOperation()));
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

	public boolean dispatchReactions(List<CheckResult> checkResults,
			InputData inputData) {
		// TODO Auto-generated method stub
		return false;
	}


}
