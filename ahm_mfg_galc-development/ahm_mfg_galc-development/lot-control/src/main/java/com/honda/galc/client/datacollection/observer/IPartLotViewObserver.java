package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessTorque;
/**
 * <h3>ClientContext</h3>
 * <h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep 29, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 *<TR>
 * <TD>Meghana Ghanekar</TD>
 * <TD>Jan 14, 2011</TD>
 * <TD>0.2</TD>
 * <TD>Modified to add ViewManager field used to access 
 * view in FormFeedAction</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
@ObserverInterface
public interface IPartLotViewObserver {
	@ProcessPartState(actions = { Action.REJECT})
	void rejectPart(ProcessPart state);
	
	@ProcessTorqueState(actions = { Action.REJECT})
	void rejectTorque(ProcessTorque state);
}

