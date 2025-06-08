/**
 * 
 */
package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;

/**
 * @author Subu Kathiresan
 * @Date May 1, 2012
 *
 */
@Fsm(type = FsmType.HEADLESS)
public interface IProcessRefreshHeadless extends IProcessCommon {

	@Notification(action = Action.INIT)
	public void init();
	
	@Transition(state = ProcessRefresh.class, init="complete")
	@Notification(action = Action.OK)
	public void messageSentOk();
	
	@Transition(state = ProcessRefresh.class, init="complete")
	@Notification(action = Action.NG)
	public void messageSentNg();

	@Transition(state = ProcessProduct.class, init="complete")
	@Notification(action = Action.COMPLETE)
	public void complete();
	
	@Transition(state = ProcessRefresh.class, init = "complete") 
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();

	@Transition(state = ProcessRefresh.class, init = "complete") 
	@Notification(action = Action.CANCEL)
	public void cancel();
}
