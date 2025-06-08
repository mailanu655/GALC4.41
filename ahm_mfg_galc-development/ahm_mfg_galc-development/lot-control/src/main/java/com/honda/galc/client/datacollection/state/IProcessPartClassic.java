package com.honda.galc.client.datacollection.state;

import java.util.List;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
import com.honda.galc.entity.product.InstalledPart;
/**
 * 
 * <h3>IProcessPartClassic</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProcessPartClassic description </p>
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
public interface IProcessPartClassic extends IProcessCommon{
	
	@Transitions({
		@Transition(state = ProcessPart.class, init = "complete",	condition = "isLastPartAndNotScanPartSnAndHasTorque") ,
		@Transition(state = ProcessPart.class, init="autoProcess", condition = "isPartMark")
	})
	@Notification(action = Action.INIT, preAction = "initStateClassic")
	public void init();

	
	/**
	 * Map to part serial number received event and notify observers
	 */
	@Notification(action = Action.RECEIVED)
	public void receivedPartSn(InstalledPart part);

	
	/**
	 * Map to part serial number verification OK event
	 * Set Part OK action and notify observers.
	 * Transit to Torque Collection state if the part need to collection torque, 
	 * otherwise, complete the current part collection.
	 * 
	 */
	@Transitions({
		@Transition(state = ProcessPart.class, init="complete", condition="isLastPart"),
		@Transition(state=ProcessPart.class, condition="isNotLastPart")			
	})
	@Notification(type=FsmType.DEFAULT, action = Action.OK)
	public void partSnOk(InstalledPart part);

	/**
	 * Map to part serial number verification OK event
	 * Set Part OK action and notify observers.
	 * Transit to Torque Collection state if the part need to collection torque, 
	 * otherwise, complete the current part collection.
	 * 
	 */
	@Transitions({
		@Transition(state = ProcessPart.class, init="complete", condition="isLastPart"),
		@Transition(state=ProcessPart.class, condition="isNotLastPart")			
	})
	@Notification(type=FsmType.DEFAULT, action = Action.OK)
	public void partsSnOk(List<InstalledPart> parts, InstalledPart part);

	/**
	 * Map to part serial number verification NG event
	 * Set part part NG action and notify observers
	 * 
	 */
	@Notification(action = Action.NG)
	public void partSnNg(InstalledPart part, String msgId, String userMsg);
	
	@Transitions({
		@Transition(state = ProcessProduct.class, init="complete",  condition = "isLastPartAndNoTorqueOnRules"), 
		@Transition(state = ProcessTorque.class, condition = "isLastPartAndHasTorqueOnRules"), 
		@Transition(state = ProcessPart.class)
	})
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
	@Notification(type=FsmType.DEFAULT, action = Action.SKIP_PART)
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
	@Transition(state = ProcessPart.class, init = "complete",	condition = "isLastPartAndNotScanPartSnAndHasTorque") 
	@Notification(action = Action.REJECT)
	public void initForRejection();
	
	/**
	 * Map to user reject
	 */
	@Notification(action = Action.REJECT)
	public void reject();
	
	//@Transition(state = ProcessPart.class, init="complete")
	public void autoProcess();

	@Transition(state = ProcessPart.class, init="complete")
	@Notification(action = Action.NO_ACTION)
	public void partNoAction();
}
