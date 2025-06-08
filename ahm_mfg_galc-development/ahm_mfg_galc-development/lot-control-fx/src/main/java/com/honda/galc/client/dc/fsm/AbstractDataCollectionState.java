package com.honda.galc.client.dc.fsm;

import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.fsm.AbstractState;

/**
 * 
 * <h3>AbstractDataCollectionState Class description</h3>
 * <p> AbstractDataCollectionState description </p>
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
public abstract class AbstractDataCollectionState extends AbstractState<DataCollectionModel>{
	
	public MCOperationRevision getCurrentOperation() {
		return getContext().getCurrentOperation();
	}
	
	/**
	 * Add part to part list of current product
	 * @param part
	 */
	public void addToPartList(String partSN) {
		getContext().addInstalledPart(partSN);
	}
	
	public DataCollectionModel getModel() {
		return getContext();
	}
	
	/** get state name
	 * 
	 */
	public String getName() {
		return "#"+getClass().getSimpleName();
	}
	
	
	
}
