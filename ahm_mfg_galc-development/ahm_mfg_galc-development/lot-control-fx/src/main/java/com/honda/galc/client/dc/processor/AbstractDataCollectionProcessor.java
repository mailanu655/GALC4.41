package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * 
 * <h3>AbstractDataCollectionProcessor Class description</h3>
 * <p> AbstractDataCollectionProcessor description </p>
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
public abstract class AbstractDataCollectionProcessor<T extends IDeviceData> implements IDataCollectionTaskProcessor<T> {
	
	private DataCollectionController controller;
	
	private final MCOperationRevision operation;
	
	public AbstractDataCollectionProcessor(DataCollectionController controller, MCOperationRevision operation){
		this.controller = controller;
		this.operation = operation;
	}
	
	public void setController(DataCollectionController controller) {
		this.controller = controller;
	}
	
	public DataCollectionController getController() {
		return controller;
	}
	
	public Logger getLogger() {
		return getController().getView().getLogger();
	}

	public MCOperationRevision getOperation() {
		return operation;
	}
}
