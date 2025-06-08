package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
import com.honda.galc.client.datacollection.fsm.Transition;
import com.honda.galc.client.datacollection.fsm.Transitions;
import com.honda.galc.entity.product.InstalledPart;


@Fsm(type = FsmType.REPAIR)
public interface IProcessPartRepair extends IProcessCommon{
	
	@Transitions({
		@Transition(state = ProcessTorque.class,	condition = "isRepairAndNotScanPartSnAndHasTorque")
	})
	@Notification(action = Action.INIT, preAction="initRepairState")
	public void init();
	
	@Transition(state = ProcessPart.class)
	@Notification(action = Action.REPAIR)
	public void repairPartSelected();
	
	
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
	
	@Transition(state = ProcessProduct.class, init="complete")
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

	@Transition(state = ProcessTorque.class, init="initState", condition = "isRepairAndNotScanPartSnAndHasTorque")
	@Notification(action = Action.REJECT)
	public void reject1();

	@Transition(state = ProcessTorque.class, init="initState", condition = "isRepairAndNotScanPartSnAndHasTorque")
	@Notification(action = Action.REJECT)
	public void initRejectionFromTorque();

}
