package com.honda.galc.common.simulation;



/**
 * 
 * <h3>TableSelectEvent Class description</h3>
 * <p> TableSelectEvent description </p>
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
 * May 15, 2012
 * @author Fredrick Yessaian - Aug 22, 2018 : Conditions moved to Simulation Processor.
 *
 */
public class ListBoxSelectEvent extends RobotGuiEvent {
	
	private static final long serialVersionUID = 1L;
	
	public ListBoxSelectEvent() {}
	
	@Override
	public void execute(ISimulationProcessor processor) {		
		processor.selectListBox( getComponentName(), getParameterValue1());
	}
}
