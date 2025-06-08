package com.honda.galc.client.dc.fsm;

import com.honda.galc.client.dc.fsm.ActionTypes.CANCEL;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.ActionTypes.INIT;
import com.honda.galc.client.dc.fsm.ActionTypes.NG;
import com.honda.galc.client.dc.fsm.ActionTypes.OK;
import com.honda.galc.client.dc.fsm.ActionTypes.RECEIVED;
import com.honda.galc.client.dc.fsm.ActionTypes.REJECT;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PART;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PRODUCT;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.fsm.Action;
import com.honda.galc.fsm.Fsm;
import com.honda.galc.fsm.IState;
import com.honda.galc.fsm.Transition;
import com.honda.galc.fsm.Transitions;

/**
 * 
 * <h3>IProcessPart</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProcessPart description </p>
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
public interface IProcessPart extends IState<DataCollectionModel>{
	
	@Transitions({
		@Transition(newState = ProcessTorque.class,	condition = "isNotScanPartSnAndHasTorque"),
		@Transition(newState = ProcessPart.class, init="complete", condition = "isPartMark")
	})
	@Action(action = INIT.class, preAction="initState")
	public void init();
	
	@Action(action = RECEIVED.class)
	public void receivedPartSn(InstalledPart part);
	
	@Transitions({
		@Transition(newState = ProcessTorque.class, condition = "hasTorque"),	
		@Transition(newState = ProcessPart.class, init = "complete")
	})
	@Action(action = OK.class)
	public void partSnOk(String partSN);
	
	@Action(action = NG.class)
	public void partSnNg(InstalledPart part, String msgId, String userMsg);
	
	@Transitions({
		@Transition(newState = ProcessProduct.class, init="complete", condition = "isLastPart"),
		@Transition(newState = ProcessPart.class)
	})
	@Action(action = COMPLETE.class)
	public void complete();
	
	
	@Transition(newState = ProcessProduct.class, init="complete")
	@Action(action = SKIP_PRODUCT.class)
	public void skipProduct();
	
	@Transition(newState = ProcessPart.class, init="complete")
	@Action(action = SKIP_PART.class)
	public void skipPart();
	
	@Transitions({
		@Transition(newState = ProcessTorque.class, condition = "hasTorque"),	
		@Transition(newState = ProcessPart.class, init = "complete")
	})
	@Action(action = SKIP_PART.class)
	public void skipCurrentInput();
	
	@Transition(newState = ProcessProduct.class)
	@Action(action = CANCEL.class)
	public void cancel();
	
	@Action(action = REJECT.class)
	public void reject();
	
	@Action(action = REJECT.class)
	public void initForRejection();

	@Transition(newState = ProcessTorque.class, init="initState", condition = "isNotScanPartSnAndHasTorque")
	@Action(action = REJECT.class)
	public void reject1();

	@Transition(newState = ProcessTorque.class, init="initState", condition = "isNotScanPartSnAndHasTorque")
	@Action(action = REJECT.class)
	public void initRejectionFromTorque();

}
