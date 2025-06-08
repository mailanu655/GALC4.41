package com.honda.galc.common.simulation;

public class TableCellSelectEvent extends RobotGuiEvent {

	private static final long serialVersionUID = 1L;

	public TableCellSelectEvent() {}

	@Override
	public void execute(ISimulationProcessor processor) {
		 processor.enterTableCellValue(getComponentName(), getParameterValue1(), getParameterValue2(),getParameterValue3());
	}
}