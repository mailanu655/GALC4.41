package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.device.DeviceStatusWidgetEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.InputData;

/**
 * 
 * <h3>OperationCompleteAction</h3>
 * <h3>The class is an operation action to mark operation as completed without saving data to DB. </h3>
 * <h4>  </h4>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>

 * @author Hale Xie
 * Oct 16, 2014
 *
 */
public class OperationCompleteAction extends BaseDataCollectionAction<InputData> {

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.action.IDataCollectionAction#perform(com.honda.galc.client.dc.mvc.DataCollectionModel, com.honda.galc.client.dc.event.DataCollectionEvent)
	 */
	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		// Notify the data collection framework, the part is completed
		model.getCompletedOpsMap().put(event.getOperation().getId().getOperationName(), true);
		EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_COMPLETED_FOR_PART, event.getOperation()));
		EventBusUtil.publish(new DeviceStatusWidgetEvent<IDevice>(null, false));
	}



	@Override
	public String getCheckPointName() {
		// TODO Auto-generated method stub
		return null;
	}



	public boolean dispatchReactions(List checkResults, InputData inputData) {
		// TODO Auto-generated method stub
		return false;
	}

}
