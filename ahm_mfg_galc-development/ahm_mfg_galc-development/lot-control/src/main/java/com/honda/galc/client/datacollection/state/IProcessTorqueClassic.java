package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
import com.honda.galc.entity.product.Measurement;

/**
 * 
 * <h3>IProcessTorqueClassic</h3> <h3>Class description</h3> <h4>
 * Description</h4>
 * <p>
 * IProcessTorqueClassic description
 * </p>
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
 * @author Paul Chou May 18, 2010
 * 
 */
@Fsm(type = FsmType.CLASSIC)
public interface IProcessTorqueClassic extends IProcessCommon {

	@Transition(state = ProcessTorque.class, init = "complete", condition = "hasNoTorqueOnRules")
	@Notification(action = Action.INIT, preAction = "initStateClassic")
	public void init();

	@Transitions( {
			@Transition(state = ProcessTorque.class, init = "complete", condition = "isLastTorquedPartAndLastTorque"),
			@Transition(state = ProcessTorque.class, condition = "isLastTorqueOnCurrentPart") })
	@Notification(action = Action.OK, postAction = "torqueOkPostAction")
	public void torqueOk(Measurement torque);

	@Notification(action = Action.COMPLETE, condition = "isLastTorqueOnCurrentPart")
	public void torqueOkPostAction();

	@Transition(state = ProcessTorque.class, init="abort", condition="isTorqueMaxAttemptsExceeded")
	@Notification(action = Action.NG)
	public void torqueNg(Measurement torque, String msgId, String userMsg);

	@Transition(state = ProcessProduct.class, init = "complete")
	@Notification(action = Action.COMPLETE)
	public void complete();

	@Transition(state = ProcessTorque.class, init = "complete")
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();

	@Transition(state = ProcessPart.class, init = "complete")
	@Notification(action = Action.SKIP_PART)
	public void skipPart();

	@Transitions( {
			@Transition(state = ProcessTorque.class, init = "complete", condition = "isLastTorquedPartAndLastTorque"),
			@Transition(state = ProcessTorque.class, condition = "isLastTorqueOnCurrentPart") })
	@Notification(action = Action.SKIP_PART, postAction = "torqueSkipPostAction")
	public void skipCurrentInput();

	@Notification(action = Action.COMPLETE, condition = "isLastTorqueOnCurrentPart")
	public void torqueSkipPostAction();

	@Transition(state = ProcessProduct.class)
	@Notification(action = Action.CANCEL)
	public void cancel();

	@Transitions( {
			@Transition(state = ProcessPart.class, init = "initForRejection", condition = "isFirstTorqueOnAllRulesRejectedAndHasScanParts"),
			@Transition(state = ProcessTorque.class, condition = "isLastTorqueOnCurrentPart") })
	@Notification(action = Action.REJECT)
	public void reject();
	
	@Transition(state = ProcessProduct.class, init="complete")
	@Notification(action = Action.ABORT, preAction = "complete")
	public void abort();
}
