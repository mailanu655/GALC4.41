package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.entity.product.ExpectedProduct;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IPersistenceObserver</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Sep 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */

@ObserverInterface
public interface IPersistenceObserver {
	
	@ProcessProductState(actions = { Action.INIT})
	public void getExpectedProductId(ProcessProduct state);
	
	@ProcessProductState(actions = { Action.MESSAGE})
	public void message(DataCollectionState state);
	
	@ProcessProductState(actions = { Action.COMPLETE })
	public void saveCompleteData(ProcessProduct state);

	@ProcessPartState(actions = { Action.INIT })
	public void initPart(ProcessPart state);

	@ProcessPartState(actions = { Action.COMPLETE })
	public void completePart(ProcessPart state);
	
	@ProcessTorqueState(actions = { Action.COMPLETE})
	public void completeTorque(ProcessTorque state);
	
	public List<ExpectedProduct> findAllExpectedProduct();
}
