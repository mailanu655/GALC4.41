package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;

@Fsm(type = FsmType.REPAIR)
public interface IProcessProductRepair extends IProcessCommon {

	@Notification(action = Action.INIT)
	public void init();
	
	@Notification(action = Action.RECEIVED)
	public void receivedProductId(ProductBean product);
	
	/**
	 * Map to product Id OK event
	 * Set product id OK action and notify observers
	 * Transit to part serial number verification state.
	 */
	@Transitions({
		@Transition(state = ProcessPart.class, init ="partNoAction", condition ="isNotAutoAdvanceRepairPart" ),
		@Transition(state = ProcessPart.class)
	})
	@Notification(type=FsmType.REPAIR, action = Action.OK)
	public void productIdOk(ProductBean product);
	
	/**
	 * Map to production Id verification failed event
	 * Notify observers product Id verification failed and error message.
	 * 
	 */
	@Transition(state = ProcessProduct.class, init="complete", condition = "isLotControlRuleNotDefined")
	@Notification(action = Action.NG)
	public void productIdNg(ProductBean product, String msgId, String userMsg);
	
	/**
	 * Last step in processing the current product. 
	 * 
	 */
	@Transitions({
		@Transition(state = ProcessPart.class, init = "completeRepair", condition ="isNotAutoAdvanceRepairPart" ),
		@Transition(state = ProcessPart.class)
	})
	@Notification(action = Action.COMPLETE)
	public void complete();
	
	
	/**
	 * Map to user skip engine event
	 */
	@Transition(state = ProcessProduct.class, init="complete")
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();
	
	@Transition(state = ProcessProduct.class)
	@Notification(action = Action.CANCEL)
	public void cancel();

}
