package com.honda.galc.common.simulation;




public class TableCellTextEntryEvent extends RobotGuiEvent {
	
	private static final long serialVersionUID = 1L;
	
	public TableCellTextEntryEvent() {}
	
	@Override public void execute(ISimulationProcessor processor) 
	{ 
		processor.enterTableCellValue(getComponentName(), getParameterValue1(), getParameterValue2(), getParameterValue3()); 
	}
}
