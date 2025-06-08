package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;

@Fsm(type = FsmType.REPAIR)
public interface IProcessRefreshRepair  extends IProcessCommon {

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


}
