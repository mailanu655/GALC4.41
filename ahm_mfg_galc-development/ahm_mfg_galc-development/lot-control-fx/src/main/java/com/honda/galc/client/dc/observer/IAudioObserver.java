package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.fsm.IProcessPart;
import com.honda.galc.client.dc.fsm.IProcessProduct;
import com.honda.galc.client.dc.fsm.IProcessTorque;
import com.honda.galc.client.dc.fsm.ActionTypes.CANCEL;
import com.honda.galc.client.dc.fsm.ActionTypes.MESSAGE;
import com.honda.galc.client.dc.fsm.ActionTypes.NG;
import com.honda.galc.client.dc.fsm.ActionTypes.NO_ACTION;
import com.honda.galc.client.dc.fsm.ActionTypes.OK;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PART;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PRODUCT;
import com.honda.galc.fsm.IState;
import com.honda.galc.fsm.Observer;
import com.honda.galc.fsm.ObserverInterface;
import com.honda.galc.fsm.Observers;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IAudioObserver</code> is ...
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
 * @author Paul Chou
 */
 /** * *
 * @version 0.2 
 * @author Jeffray Huang
 * @since Feb 19, 2014 
 */ 
@ObserverInterface
public interface IAudioObserver {

	@Observers({
		@Observer(state = IProcessPart.class,actions = { NG.class}),
		@Observer(state = IProcessProduct.class,actions = { NG.class})
	})
	public void playRepeatedNGSound(IState<?> state);
	
	@Observers({
		@Observer(state = IProcessProduct.class,actions = { SKIP_PRODUCT.class,SKIP_PART.class}),
		@Observer(state = IProcessPart.class,actions = { CANCEL.class,SKIP_PRODUCT.class,SKIP_PART.class}),
		@Observer(state = IProcessTorque.class,actions = { NG.class,CANCEL.class,SKIP_PRODUCT.class,SKIP_PART.class})
	})
	public void playNgSound(Object arg);

	@Observers({
		@Observer(state = IProcessProduct.class,actions = { OK.class}),
		@Observer(state = IProcessPart.class,actions = { OK.class}),
		@Observer(state = IProcessTorque.class,actions = { OK.class})
	})
	public void playOkSound(Object arg);
	
	@Observers({
		@Observer(state = IProcessProduct.class,actions = { MESSAGE.class}),
		@Observer(state = IProcessPart.class,actions = { MESSAGE.class}),
		@Observer(state = IProcessTorque.class,actions = { MESSAGE.class})
	})
	public void message(IState<?> state);
	
	@Observer(state = IProcessPart.class,actions = { NO_ACTION.class})
	public void playNoActionSound(Object arg);
}
