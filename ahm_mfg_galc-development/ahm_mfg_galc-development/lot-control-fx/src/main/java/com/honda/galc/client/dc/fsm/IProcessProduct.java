package com.honda.galc.client.dc.fsm;

import com.honda.galc.client.dc.fsm.ActionTypes.OK;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.fsm.Fsm;
import com.honda.galc.fsm.FsmType;
import com.honda.galc.fsm.IState;
import com.honda.galc.fsm.Action;
import com.honda.galc.fsm.Transition;
/**
 * 
 * <h3>IProcessProduct</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProcessProduct description </p>
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
 * @author Paul Chou
 * May 18, 2010
 *
 */
@Fsm
public interface IProcessProduct extends IState<DataCollectionModel> {

	/**
	 * Map to product Id OK event
	 * Set product id OK action and notify observers
	 * Transit to part serial number verification state.
	 */
	@Transition(newState = ProcessPart.class)
	@Action(type=FsmType.DEFAULT, action = OK.class)
	public void productIdOk();

}
