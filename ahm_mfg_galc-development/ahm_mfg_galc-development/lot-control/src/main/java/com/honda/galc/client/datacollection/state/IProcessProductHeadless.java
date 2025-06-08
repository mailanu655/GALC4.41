/**
 * 
 */
package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;

/**
 * @author Subu Kathiresan
 * @Date April 15, 2012
 *
 */
@Fsm(type = FsmType.HEADLESS)
public interface IProcessProductHeadless extends IProcessCommon {
	
	@Notification(action = Action.INIT)
	public void init();
	
	@Notification(action = Action.RECEIVED)
	public void receivedProductId(ProductBean product);
	
	@Transition(state = ProcessPart.class)
	@Notification(action = Action.OK)
	public void productIdOk(ProductBean product);
	
	@Transition(state = ProcessProduct.class, init="complete", condition = "isLotControlRuleNotDefined")
	@Notification(action = Action.NG)
	public void productIdNg(ProductBean product, String msgId, String userMsg);
	
	@Transition(state = ProcessProduct.class, init="init")
	@Notification(action = Action.COMPLETE)
	public void complete();
}
