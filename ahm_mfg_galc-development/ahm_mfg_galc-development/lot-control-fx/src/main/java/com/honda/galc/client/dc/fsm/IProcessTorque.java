package com.honda.galc.client.dc.fsm;

import com.honda.galc.client.dc.fsm.ActionTypes.ABORT;
import com.honda.galc.client.dc.fsm.ActionTypes.CANCEL;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.ActionTypes.INIT;
import com.honda.galc.client.dc.fsm.ActionTypes.NG;
import com.honda.galc.client.dc.fsm.ActionTypes.OK;
import com.honda.galc.client.dc.fsm.ActionTypes.REJECT;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PART;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PRODUCT;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.fsm.Action;
import com.honda.galc.fsm.Fsm;
import com.honda.galc.fsm.IState;
import com.honda.galc.fsm.Transition;
/**
 * 
 * <h3>IProcessTorque</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProcessTorque description </p>
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

@Fsm
public interface IProcessTorque extends IState<DataCollectionModel>{
	@Transition(newState = ProcessPart.class, init="complete", condition = "hasNoTorque")
	@Action(action = INIT.class, preAction="initState", condition = "hasTorque")
	public void init();

	@Transition(newState = ProcessPart.class, init="complete", condition = "isLastTorqueOnCurrentPart")
	@Action(action = OK.class, postAction = "torqueOkPostAction")
	public void torqueOk(Measurement torque);
	
	@Action(action = COMPLETE.class, condition = "isLastTorqueOnCurrentPart")
	public void torqueOkPostAction();
	
	@Transition(newState = ProcessTorque.class, init="abort", condition="isTorqueMaxAttemptsExceeded")
	@Action(action = NG.class)
	public void torqueNg(Measurement torque, String msgId, String userMsg);
	
	@Action(action = COMPLETE.class)
	public void complete();
	
	@Transition(newState = ProcessPart.class, init="complete")
	@Action(action = ABORT.class)
	public void abort();
	
	@Transition(newState = ProcessProduct.class, init="complete")
	@Action(action = SKIP_PRODUCT.class)
	public void skipProduct();
	
	@Transition(newState = ProcessPart.class, init="complete")
	@Action(action = SKIP_PART.class)
	public void skipPart();
	
	@Transition(newState = ProcessPart.class, init="complete", condition = "isLastTorqueOnCurrentPart")
	@Action(action = SKIP_PART.class, postAction = "torqueSkipPostAction")
	public void skipCurrentInput();
	
	@Action(action = COMPLETE.class, condition = "isLastTorqueOnCurrentPart")
	public void torqueSkipPostAction();
	
	@Transition(newState = ProcessProduct.class)
	@Action(action = CANCEL.class)
	public void cancel();

	@Transition(newState = ProcessPart.class, init = "initForRejection", condition="isFirstTorqueOnAllRulesRejected")
	@Action(action = REJECT.class)
	public void reject();

	@Transition(newState = ProcessPart.class, init = "initRejectionFromTorque", condition="isFirstTorqueOnCurrentPartRejected")
	@Action(action = REJECT.class)
	public void reject1();

}
