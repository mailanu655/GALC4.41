package com.honda.galc.common.simulation;


public class KeyPressEvent extends RobotGuiEvent {

	private static final long serialVersionUID = 1L;

	public KeyPressEvent() {}
	
	@Override
	public void execute(ISimulationProcessor processor) {
		processor.enterKey(getParameterValueInt1());
	}
}
