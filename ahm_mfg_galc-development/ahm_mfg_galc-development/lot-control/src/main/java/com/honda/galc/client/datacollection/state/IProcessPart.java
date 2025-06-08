package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
import com.honda.galc.entity.product.InstalledPart;
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
@Fsm()
public interface IProcessPart extends IProcessCommon{
	
	@Transitions({
		@Transition(state = ProcessTorque.class,	condition = "isNotScanPartSnAndHasTorque"),
		@Transition(state = ProcessPart.class, init="complete", condition = "isPartMark")
	})
	
	@Notification(action = Action.INIT, preAction="initState")
	public void init();
	
	@Notification(action = Action.RECEIVED)
	public void receivedPartSn(InstalledPart part);
	
	@Transitions({
		@Transition(state = ProcessTorque.class, condition = "hasTorque"),	
		@Transition(state = ProcessPart.class, init = "complete")
	})
	@Notification(action = Action.OK)
	public void partSnOk(InstalledPart part);
	
	@Notification(action = Action.NG)
	public void partSnNg(InstalledPart part, String msgId, String userMsg);
	
	@Transitions({
		@Transition(state = ProcessProduct.class, init="complete", condition = "isLastPart"),
		@Transition(state = ProcessPart.class)
	})
	@Notification(action = Action.COMPLETE)
	public void complete();
	
	
	@Transition(state = ProcessProduct.class, init="complete")
	@Notification(action = Action.SKIP_PRODUCT)
	public void skipProduct();
	
	@Transition(state = ProcessPart.class, init="complete")
	@Notification(action = Action.SKIP_PART)
	public void skipPart();
	
	@Transitions({
		@Transition(state = ProcessTorque.class, condition = "hasTorque"),	
		@Transition(state = ProcessPart.class, init = "complete")
	})
	@Notification(action = Action.SKIP_PART)
	public void skipCurrentInput();
	
	@Transition(state = ProcessProduct.class)
	@Notification(action = Action.CANCEL)
	public void cancel();
	
	@Notification(action = Action.REJECT)
	public void reject();
	
	@Notification(action = Action.REJECT)
	public void initForRejection();

	@Transition(state = ProcessTorque.class, init="initState", condition = "isNotScanPartSnAndHasTorque")
	@Notification(action = Action.REJECT)
	public void reject1();

	@Transition(state = ProcessTorque.class, init="initState", condition = "isNotScanPartSnAndHasTorque")
	@Notification(action = Action.REJECT)
	public void initRejectionFromTorque();

	@Notification(action = Action.OK_WAIT)
	public void partSnOkButWait(InstalledPart part);
	
	@Notification(action = Action.BYPASS)
	public void receivedBypass(InstalledPart part);
	
	@Notification(action = Action.AUTO)
	public void receivedAuto(InstalledPart part);
}