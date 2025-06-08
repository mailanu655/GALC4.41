package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.fsm.IProcessPart;
import com.honda.galc.client.dc.fsm.IProcessProduct;
import com.honda.galc.client.dc.fsm.IProcessTorque;
import com.honda.galc.client.dc.fsm.ProcessPart;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.fsm.ActionTypes.CANCEL;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.ActionTypes.ERROR;
import com.honda.galc.client.dc.fsm.ActionTypes.INIT;
import com.honda.galc.client.dc.fsm.ActionTypes.MESSAGE;
import com.honda.galc.client.dc.fsm.ActionTypes.NG;
import com.honda.galc.client.dc.fsm.ActionTypes.OK;
import com.honda.galc.client.dc.fsm.ActionTypes.RECEIVED;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PART;
import com.honda.galc.fsm.IState;
import com.honda.galc.fsm.Observer;
import com.honda.galc.fsm.ObserverInterface;
import com.honda.galc.fsm.Observers;


/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IViewObserver</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Sep 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */
@ObserverInterface
public interface IViewObserver  {
	
	
	@Observer(state=IProcessProduct.class,actions = {OK.class})
	void productIdOk(final ProcessProduct state);
	
	//Part verification action mapping
	
	@Observer(state=IProcessPart.class,actions = {INIT.class})
	void initPartSn(final ProcessPart state);
	
	@Observer(state=IProcessPart.class,actions = {OK.class})
	void partSnOk(final ProcessPart state);
	
	@Observer(state=IProcessPart.class,actions = {NG.class})
	void partSnNg(final ProcessPart state);
	
	@Observer(state=IProcessPart.class,actions = {RECEIVED.class})
	void receivedPartSn(final ProcessPart state);
	
	@Observer(state=IProcessPart.class,actions = {COMPLETE.class})
	void completePartSerialNumber(ProcessPart state);
	
	//Torque collection action mapping
	
	@Observer(state=IProcessTorque.class,actions = {INIT.class})
	void initTorque(final ProcessTorque state);
	
	@Observer(state=IProcessTorque.class,actions = {OK.class})
	void torqueOk(final ProcessTorque state);
	
	@Observer(state=IProcessTorque.class,actions = {NG.class})
	void torqueNg(final ProcessTorque state);
	
	@Observer(state=IProcessTorque.class,actions = {COMPLETE.class})
	void completeCollectTorques(final ProcessTorque state);

	// Common mapping for all states
	
	@Observer(state=IProcessPart.class,actions = {SKIP_PART.class})
	void skipPart(IState<?> state);
	
	@Observer(state=IProcessTorque.class,actions = {SKIP_PART.class})
	void skipCurrentInput(final ProcessTorque state);
	
	
	@Observers({
		@Observer(state=IProcessProduct.class,actions = {CANCEL.class}),
		@Observer(state=IProcessPart.class,actions = {CANCEL.class}),
		@Observer(state=IProcessTorque.class,actions = {CANCEL.class})
	})
	void resetScreen(IState<?> state);
	
	@Observers({
		@Observer(state=IProcessProduct.class,actions = {ERROR.class}),
		@Observer(state=IProcessPart.class,actions = {ERROR.class}),
		@Observer(state=IProcessTorque.class,actions = {ERROR.class})
	})
	void notifyError(IState<?> state);
	
	@Observers({
		@Observer(state=IProcessProduct.class,actions = {MESSAGE.class}),
		@Observer(state=IProcessPart.class,actions = {MESSAGE.class}),
		@Observer(state=IProcessTorque.class,actions = {MESSAGE.class})
	})
	void message(IState<?> state);
	
}
