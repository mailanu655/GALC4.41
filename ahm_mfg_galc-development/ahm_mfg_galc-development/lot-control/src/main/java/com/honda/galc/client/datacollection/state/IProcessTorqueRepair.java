package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.entity.product.Measurement;

@Fsm(type = FsmType.REPAIR)
public interface IProcessTorqueRepair extends IProcessCommon{
	@Transition(state = ProcessPart.class, init="completeRepair", condition = "hasNoTorqueToRepair")
	@Notification(action = Action.INIT, preAction="initState", condition = "hasTorque")
	public void init();

	@Transition(state = ProcessPart.class, init="complete", condition = "isLastTorqueOnCurrentPart")
	@Notification(action = Action.OK, postAction = "torqueOkPostAction")
	public void torqueOk(Measurement torque);
	
	@Notification(action = Action.COMPLETE, condition = "isLastTorqueOnCurrentPart")
	public void torqueOkPostAction();
	
	@Notification(action = Action.NG)
	public void torqueNg(Measurement torque, String msgId, String userMsg);
	
	@Notification(action = Action.COMPLETE)
	public void complete();
	
	@Transition(state = ProcessPart.class, init="complete")
	@Notification(action = Action.ABORT)
	public void abort();
	
	@Transition(state = ProcessProduct.class, init="complete")
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();
	
	@Transition(state = ProcessPart.class, init="complete")
	@Notification(action = Action.SKIP_PART)
	public void skipPart();
	
	@Transition(state = ProcessPart.class, init="complete", condition = "isLastTorqueOnCurrentPart")
	@Notification(action = Action.SKIP_PART, postAction = "torqueSkipPostAction")
	public void skipCurrentInput();
	
	@Notification(action = Action.COMPLETE, condition = "isLastTorqueOnCurrentPart")
	public void torqueSkipPostAction();
	
	@Transition(state = ProcessProduct.class)
	@Notification(action = Action.CANCEL)
	public void cancel();

	@Transition(state = ProcessPart.class, init = "initForRejection", condition="isFirstTorqueOnAllRulesRejected")
	@Notification(action = Action.REJECT)
	public void reject();

	@Transition(state = ProcessPart.class, init = "initRejectionFromTorque", condition="isFirstTorqueOnCurrentPartRejected")
	@Notification(action = Action.REJECT)
	public void reject1();

	@Transition(state = ProcessPart.class)
	@Notification(action = Action.REPAIR)
	public void repairPartSelected();
	
}
