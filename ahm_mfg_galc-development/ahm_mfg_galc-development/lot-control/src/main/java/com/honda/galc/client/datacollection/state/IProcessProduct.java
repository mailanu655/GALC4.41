package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
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
@Fsm()
public interface IProcessProduct extends IProcessCommon {

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
		@Transition(state = ProcessProduct.class, init="complete", condition = "noDataCollection"),
		@Transition(state = ProcessPart.class)
	})
	@Notification(type=FsmType.DEFAULT, action = Action.OK)
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
	@Transition(state = ProcessRefresh.class, init="init")
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
