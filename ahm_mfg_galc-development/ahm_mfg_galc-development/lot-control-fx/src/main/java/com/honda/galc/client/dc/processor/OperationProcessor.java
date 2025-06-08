package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;


/**
 * 
 * <h3>TorqueProcessor Class description</h3>
 * <p> TorqueProcessor description </p>
 * 
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
 *   
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public class OperationProcessor extends AbstractDataCollectionProcessor<InputData> implements IOperationProcessor{
	
	public OperationProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	public boolean execute(InputData data) {
		getLogger().info("Input data received: " + data);
		return false;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public void destroy() {
	}

	public IDeviceData processReceived(InputData deviceData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerDeviceListener(DeviceListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setController(DataCollectionController controller) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Select the next work unit in the work unit list.
	 */
	public void selectNextOperation(){
		EventBusUtil.publish(new UnitNavigatorEvent(UnitNavigatorEventType.MOVETO, getController().getModel().getCurrentOperationIndex()+1));
	}
	
	/**
	 * Mark the operation is completed. The function is supposed to use by the
	 * non-standard lot control processor to notify the data collection framework that
	 * the work unit is completed
	 */
	public void completeOperation() {

	}
	
	/**
	 * Complete the operation. The data collection view will mark the operation
	 * as completed and save a record to gal185tbx if savePart is true.
	 * 
	 * @param savePart
	 *            the save part to gal185tbx if true
	 */
	public void completeOperation(boolean savePart){
		DataCollectionEvent dataCollectionEvent =null;
		if(savePart){
			dataCollectionEvent = new DataCollectionEvent(DataCollectionEventType.PDDA_CONFIRM, null);
		}
		else{
			dataCollectionEvent = new DataCollectionEvent(DataCollectionEventType.OP_COMPLETE, null);
		}
		dataCollectionEvent.setOperation(getOperation());
		EventBusUtil.publish(dataCollectionEvent);
	}
}
