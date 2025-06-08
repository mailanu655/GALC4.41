package com.honda.galc.client.product.pane;

import com.honda.galc.client.mvc.AbstractPane;
import com.honda.galc.client.product.mvc.ReasonEntryPaneController;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedTextField;

import javafx.geometry.Insets;
import javafx.scene.control.TabPane;

public class ReasonEntryPane extends AbstractPane{

	private ReasonEntryPaneController controller;
	
	private LabeledTextField reasonTextField;
	boolean isHorizontal;
	
	public ReasonEntryPane(TabPane parentView, boolean isHorizontal) {
		super(parentView);
		this.isHorizontal = isHorizontal;
		init();
		controller = new ReasonEntryPaneController(this);
	}

	private void init() {
		this.setCenter(createReasonTextField());
	}

	private LabeledTextField createReasonTextField() {
		reasonTextField = new LabeledTextField("Reason :   ", isHorizontal, new Insets(0, 0, 0, 0), false, false);
		reasonTextField.styleProperty().bind(labelStyle);
		reasonTextField.getControl().styleProperty().bind(textStyle);
		reasonTextField.getControl().prefHeightProperty().bind(heightProperty().multiply(0.80));
		return reasonTextField;
	}
	
	public LoggedTextField getReasonTextFieldControl() { 
		return this.reasonTextField.getControl();
	}
	
	public LabeledTextField getReasonTextField() {
		return this.reasonTextField;
	}
}
