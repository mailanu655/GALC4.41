/**
 * 
 */
package com.honda.galc.fsm;

/**
 * 
 * <h3>IState Class description</h3>
 * <p> IState description </p>
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
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public interface IState<C>{
	public void stateChanged(Class<? extends IActionType> action);
	public C getContext();
	public void setContext(C context);
	public String getName();
}
