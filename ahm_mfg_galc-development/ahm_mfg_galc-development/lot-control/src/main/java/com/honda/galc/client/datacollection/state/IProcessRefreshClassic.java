package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
/**
 * 
 * <h3>ITorqueDataCollectionClassic</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ITorqueDataCollectionClassic description </p>
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
@Fsm(type = FsmType.CLASSIC)
public interface IProcessRefreshClassic extends IProcessCommon{

	@Transitions({
		@Transition(state = ProcessRefresh.class, init = "complete", condition = "isSkippedProduct"), 
		@Transition(state = ProcessRefresh.class, init = "complete", condition = "isDelayComplete"),
		@Transition(state = ProcessRefresh.class, init = "continueDelay", condition = "isDelayContinued") 
	})
	@Notification(action = Action.INIT)
	public void init();

	@Transitions({
		@Transition(state = ProcessRefresh.class, init = "complete", condition = "isDelayComplete") 
	})
	@Notification(action = Action.NONE)
	public void continueDelay();

	@Transition(state = ProcessProduct.class) 
	@Notification(action = Action.COMPLETE)
	public void complete();
	
	@Transition(state = ProcessRefresh.class, init = "complete") 
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();


	@Transition(state = ProcessRefresh.class, init = "complete") 
	@Notification(action = Action.CANCEL)
	public void cancel();

	
	@Transitions({
		@Transition(state = ProcessTorque.class, init = "reject",condition="hasTorqueRules"),
		@Transition(state = ProcessPart.class, init = "initForRejection", condition="hasScanRules")
	})
	@Notification(action = Action.REJECT)
	public void reject();


}
