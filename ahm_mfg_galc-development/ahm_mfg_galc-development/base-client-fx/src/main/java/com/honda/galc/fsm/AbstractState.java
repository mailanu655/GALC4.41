package com.honda.galc.fsm;

/**
 * 
 * <h3>AbstractState Class description</h3>
 * <p> AbstractState description </p>
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
public abstract class AbstractState<C> implements IState<C>{

	C context;
	@Override
	public void stateChanged(Class<? extends IActionType> action) {
		
	}
	
	public C getContext() {
		return context;
	}
	
	public void setContext(C context) {
		this.context = context;
	}

}
