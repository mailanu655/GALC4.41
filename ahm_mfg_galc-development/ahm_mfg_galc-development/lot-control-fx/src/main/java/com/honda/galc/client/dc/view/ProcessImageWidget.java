package com.honda.galc.client.dc.view;

import com.honda.galc.client.dc.mvc.ProcessImageModel;

import javafx.scene.layout.BorderPane;

/**
 * @author Subu Kathiresan
 * @date Jun 12, 2014
 */
public abstract class ProcessImageWidget<T extends ProcessImageModel> extends BorderPane {

	private ProcessImageModel model;
		
	ProcessImageWidget(ProcessImageModel model) {
	 this.model = model;
	 initComponents();
	}
	
	public abstract void initComponents();
	
	protected ProcessImageModel getModel() {
		return this.model;
	}
}
