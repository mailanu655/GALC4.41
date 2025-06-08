package com.honda.galc.common.simulation;

public class FindTextAndClickEvent extends RobotGuiEvent {

	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void execute(ISimulationProcessor processor) {
		processor.findTextAndClick(getComponentName());

	}

}
