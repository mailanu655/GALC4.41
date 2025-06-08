package com.honda.galc.common.simulation;



/**
 * 
 * <h3>MouseClickEvent Class description</h3>
 * <p> MouseClickEvent description </p>
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
 * May 14, 2012
 *
 *
 */
public class MouseClickEvent extends RobotGuiEvent {
	
	private static final long serialVersionUID = 1L;

	public MouseClickEvent() {}

	@Override
	public void execute(ISimulationProcessor processor) {
		processor.mouseClick(getComponentName(), getMouseButton(), getPoint(), getMouseClickDelay());
	}
}
