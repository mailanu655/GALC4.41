package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ITimeoutObserver</code> is the observer to control lot control timer by the notification it received
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
 * <TD>YX</TD>
 * <TD>2014.05.22</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see LotControlTimeoutManager
 * @version 0.1
 * @author YX
 */

@ObserverInterface
public interface ITimeoutObserver {
	@ProcessProductState(actions = { Action.OK })
	public void startTimer(DataCollectionState arg);
	
	@ProcessPartState(actions = { Action.RECEIVED, Action.OK, Action.NG, Action.MISSING, Action.SKIP_PART, Action.REJECT })
	@ProcessTorqueState(actions = { Action.RECEIVED, Action.OK, Action.NG, Action.MISSING, Action.SKIP_PART, Action.REJECT })
	public void restartTimer(DataCollectionState arg);
	
	@ProcessProductState(actions = { Action.CANCEL })
	@ProcessPartState(actions = { Action.CANCEL })
	@ProcessTorqueState(actions = { Action.CANCEL })
	@ProcessRefreshState(actions = { Action.COMPLETE})
	public void stopTimer(DataCollectionState arg);

}
