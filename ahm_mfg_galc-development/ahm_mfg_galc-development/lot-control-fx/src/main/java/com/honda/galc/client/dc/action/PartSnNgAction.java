package com.honda.galc.client.dc.action;

import java.util.List;


import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class PartSnNgAction extends BaseDataCollectionAction<PartSerialScanData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		try {
			MCOperationRevision operation = event.getOperation();
			InstalledPart installedPart = getInstalledPart(model, operation);
			PartSerialScanData partSerialScanData = (PartSerialScanData) event.getInputData();
			if (installedPart == null || !isInstalledPartValid(operation, installedPart)) {
				installedPart = createInstalledPart(model, partSerialScanData, event.getOperation(), InstalledPartStatus.NG);
			}
			
			if(isInstalledPartValid(operation, installedPart)) {
				savePart(installedPart, event.getOperation());	

				notifyPartSnNg((PartSerialScanData) event.getInputData(), event.getOperation(), "Part serial number ng");
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


	
	public boolean dispatchReactions(List<CheckResult> checkResults, PartSerialScanData partSerialScanData) {

		// TODO Auto-generated method stub
		return false;
	}


}
