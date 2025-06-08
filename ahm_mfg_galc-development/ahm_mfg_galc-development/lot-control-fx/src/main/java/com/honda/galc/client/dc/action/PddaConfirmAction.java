package com.honda.galc.client.dc.action;

import java.util.List;
import java.util.Map;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Subu Kathiresan
 * @date Sep 5, 2014
 */
public class PddaConfirmAction extends BaseDataCollectionAction<InputData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		// Notify the data collection framework, the part is completed
		try {
			model.markPending(event.getOperation().getId().getOperationName(), event.getOperation());

			for (Map.Entry<String,MCOperationRevision> entry : model.getPendingOpsMap().entrySet()) {
				MCOperationRevision operation = entry.getValue();
				InstalledPart installedPart = getInstalledPart(model, operation);
				if (installedPart == null) {
					installedPart = createInstalledPart(model, null, operation, InstalledPartStatus.OK);
				}
				savePart(installedPart, operation);
				
				EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_COMPLETED_FOR_PART, operation));				
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

	public boolean dispatchReactions(List<CheckResult> checkResults,
			InputData inputData) {
		// TODO Auto-generated method stub
		return false;
	}
}
