package com.honda.galc.system.oif.svc.common;

/**
 * <h3>IEventTaskExecutable</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * An interface for pre-scheduled tasks
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 10, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL009</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Jul 09, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL035</TD>
 * <TD>added getApplicationName() for monitoring by extending ApplicationNameProvider</TD>
 * </TR>
 *
 * </TABLE>
 */
public interface IEventTaskExecutable {

	/**
	 * Executes task
	 * 
	 * @param args - parameters passed to the task
	 */
	public void execute(Object[] args);

}
