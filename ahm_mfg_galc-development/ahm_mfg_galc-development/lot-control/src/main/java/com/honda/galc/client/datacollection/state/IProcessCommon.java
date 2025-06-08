package com.honda.galc.client.datacollection.state;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.fsm.FsmType;
import com.honda.galc.client.datacollection.fsm.Notification;
/**
 * 
 * <h3>IProcessCommon</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProcessCommon description </p>
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
 * May 12, 2010
 *
 */
public interface IProcessCommon {
	@Notification(type=FsmType.DEFAULT,action = Action.ERROR)
	public void error(Message message);
	
	@Notification(type=FsmType.DEFAULT,action = Action.MESSAGE)
	public void message(Message message);
}
