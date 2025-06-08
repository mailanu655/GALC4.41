package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
/**
 * 
 * <h3>IProcessProductClassic</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProcessProductClassic description </p>
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
public interface IProcessProductClassic extends IProcessCommon{

	@Notification(action = Action.INIT)
	public void init();
	
	@Notification(action = Action.RECEIVED)
	public void receivedProductId(ProductBean product);
	
	
	@Transition(state = ProcessPart.class)
	@Notification(action = Action.OK)
	public void productIdOk(ProductBean product);
	
	@Transition(state = ProcessProduct.class)
	@Notification(action = Action.NG)
	public void productIdNg(ProductBean product, String msgId, String userMsg);
	
	
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
