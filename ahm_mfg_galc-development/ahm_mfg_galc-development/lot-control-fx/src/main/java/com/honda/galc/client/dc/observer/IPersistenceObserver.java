package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.fsm.AbstractDataCollectionState;
import com.honda.galc.client.dc.fsm.IProcessPart;
import com.honda.galc.client.dc.fsm.IProcessProduct;
import com.honda.galc.client.dc.fsm.IProcessTorque;
import com.honda.galc.client.dc.fsm.ProcessPart;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.ActionTypes.INIT;
import com.honda.galc.client.dc.fsm.ActionTypes.MESSAGE;
import com.honda.galc.fsm.Observer;
import com.honda.galc.fsm.ObserverInterface;
import com.honda.galc.fsm.Observers;

/**
 * 
 * <h3>IPersistenceObserver Class description</h3>
 * <p> IPersistenceObserver description </p>
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
 * Feb 25, 2014
 *
 *
 */

@ObserverInterface
public interface IPersistenceObserver {
	
	@Observers({
		@Observer(state=IProcessProduct.class,actions = {MESSAGE.class}),
		@Observer(state=IProcessPart.class,actions = {MESSAGE.class}),
		@Observer(state=IProcessTorque.class,actions = {MESSAGE.class})
	})public void message(AbstractDataCollectionState state);
	
	@Observer(state=IProcessProduct.class,actions = {COMPLETE.class})
	public void saveCompleteData(ProcessProduct state);

	@Observer(state=IProcessPart.class,actions = {INIT.class})
	public void initPart(ProcessPart state);

	@Observer(state=IProcessPart.class,actions = {COMPLETE.class})
	public void completePart(ProcessPart state);
	
	@Observer(state=IProcessTorque.class,actions = {COMPLETE.class})
	public void completeTorque(ProcessTorque state);
	
}
