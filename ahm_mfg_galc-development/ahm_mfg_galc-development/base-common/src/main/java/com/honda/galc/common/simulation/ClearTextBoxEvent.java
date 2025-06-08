package com.honda.galc.common.simulation;
/**
 * 
 *    
 * @version 1
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 */
public class ClearTextBoxEvent extends RobotGuiEvent {

	
	private static final long serialVersionUID = 1L;
	
	public ClearTextBoxEvent() {} 
	
	@Override
	public void execute(ISimulationProcessor processor) {
		if(getParameterValueInt1() != null)
			processor.clearTextBox( getComponentName(), getParameterValueInt1());
	}

}
