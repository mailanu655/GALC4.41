/**
 * 
 */
package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Subu Kathiresan
 * @Date Apr 26, 2012
 *
 */
@Fsm(type = FsmType.HEADLESS)
public interface IProcessPartHeadless extends IProcessCommon {

	@Notification(action = Action.INIT, preAction = "initState")
	public void init();
	
	/**
	 * Map to part serial number received event and notify observers
	 */
	@Notification(action = Action.RECEIVED)
	public void receivedPartSn(InstalledPart part);
	
	/**
	 * Map to part serial number verification OK event
	 * Set Part OK action and notify observers.
	 */
	@Transitions({
		@Transition(state = ProcessPart.class, init="complete", condition="isLastPart"),
		@Transition(state=ProcessPart.class, condition="isNotLastPart")			
	})
	@Notification(type=FsmType.HEADLESS, action = Action.OK)
	public void partSnOk(InstalledPart part);
	
	@Transitions({
		@Transition(state = ProcessPart.class, init="complete", condition="isLastPart"),
		@Transition(state=ProcessPart.class, condition="isNotLastPart")			
	})
	@Notification(type=FsmType.HEADLESS, action = Action.MISSING)
	public void partSnMissing(InstalledPart part);
	
	/**
	 * Map to part serial number verification NG event
	 * Set part part NG action and notify observers
	 * 
	 */
	@Notification(action = Action.NG)
	public void partSnNg(InstalledPart part, String msgId, String userMsg);

	@Transition(state = ProcessRefresh.class, init="init")
	@Notification(action = Action.COMPLETE)
	public void complete();
	
	/**
	 * Set skip engine action and notify observers
	 * Map to user skip engine event
	 */
	@Transition(state = ProcessProduct.class, init="complete")
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();

	/**
	 * Set skip part action and notify observers
	 * Map to user skip engine event
	 */
	@Transition(state = ProcessPart.class, init="complete")
	@Notification(action = Action.SKIP_PART)
	public void skipPart();
	
	@Transitions({
		@Transition(state = ProcessPart.class, init="complete", condition="isLastPart"),
		@Transition(state=ProcessPart.class, condition="isNotLastPart")			
	})
	@Notification(type=FsmType.HEADLESS, action = Action.SKIP_PART)
	public void skipCurrentInput();

	/**
	 * Set cancel action and notify observers
	 * Map to user cancel event
	 */
	@Transition(state = ProcessProduct.class)
	@Notification(action = Action.CANCEL)
	public void cancel();
	
	/**
	 * Initialize user Reject
	 */
	@Transition(state = ProcessPart.class, init = "complete",	condition = "isLastPart") 
	@Notification(action = Action.REJECT)
	public void initForRejection();
	
	/**
	 * Map to user reject
	 */
	@Notification(action = Action.REJECT)
	public void reject();
}
