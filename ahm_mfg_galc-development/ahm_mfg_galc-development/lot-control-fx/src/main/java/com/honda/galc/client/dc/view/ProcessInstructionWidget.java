package com.honda.galc.client.dc.view;

import com.honda.galc.client.dc.mvc.ProcessInstructionModel;

import javafx.scene.layout.BorderPane;

/**
 * @author Subu Kathiresan
 * @date Jun 12, 2014
 */
public abstract class ProcessInstructionWidget<T extends ProcessInstructionModel> extends BorderPane {

	private static final long serialVersionUID = 1L;

	private ProcessInstructionModel model;
		
	ProcessInstructionWidget(ProcessInstructionModel model) {
	 this.model = model;
	 initComponents();
	}
	
	public abstract void initComponents();
	
	protected ProcessInstructionModel getModel() {
		return this.model;
	}
}
