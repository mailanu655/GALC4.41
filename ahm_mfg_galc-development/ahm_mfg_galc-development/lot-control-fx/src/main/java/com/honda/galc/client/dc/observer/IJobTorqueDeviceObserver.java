package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.fsm.AbstractDataCollectionState;
import com.honda.galc.client.dc.fsm.IProcessPart;
import com.honda.galc.client.dc.fsm.IProcessProduct;
import com.honda.galc.client.dc.fsm.IProcessTorque;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.fsm.ActionTypes.ABORT;
import com.honda.galc.client.dc.fsm.ActionTypes.CANCEL;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.ActionTypes.INIT;
import com.honda.galc.client.dc.fsm.ActionTypes.REJECT;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PART;
import com.honda.galc.client.dc.fsm.ActionTypes.SKIP_PRODUCT;
import com.honda.galc.fsm.Observer;
import com.honda.galc.fsm.ObserverInterface;
import com.honda.galc.fsm.Observers;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IDeviceObserver</code> is ...
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
 * @see
 * @ver 0.2
 * @author Jeffray Huang
 */

@ObserverInterface
public interface IJobTorqueDeviceObserver {
	
	@Observer(state=IProcessTorque.class,actions = { INIT.class })
	public void setJob(ProcessTorque torque);
	
	@Observers({
		@Observer(state=IProcessTorque.class,
			actions = { ABORT.class, REJECT.class, CANCEL.class, COMPLETE.class, SKIP_PART.class, SKIP_PRODUCT.class}),
		@Observer(state=IProcessPart.class,actions = {SKIP_PART.class, SKIP_PRODUCT.class})
	})
	public void abortJob(AbstractDataCollectionState torque);
	
	@Observer(state=IProcessProduct.class,actions = { COMPLETE.class })
	public void sendStatus(ProcessProduct state);

}
