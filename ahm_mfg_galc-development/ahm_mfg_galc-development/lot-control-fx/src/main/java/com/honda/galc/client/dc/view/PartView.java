package com.honda.galc.client.dc.view;


import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.TextField;

import com.honda.galc.client.dc.processor.PartProcessor;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;

public class PartView extends AbstractDataCollectionWidget<PartProcessor> {
	
	public PartView(PartProcessor processor) {
		super(processor);
	}
	
	public void register() {}
	
	public void initComponents() {}

	public void populateCollectedData(InstalledPart installedPart) {}

	public MCOperationRevision getOperation() {
		return getProcessor().getOperation();
	}

	public ArrayList<TextField> getInputFields() {
		return null;
	}

	public int getCurrentInputFieldIndex() {
		return 0;
	}

	public Node getDataCollectionPanel() {
		return this;
	}
	
	public void setCurrentInputFieldIndex(int inputFieldIndex) {}

	public void setFocusToExpectedInputField() {}

	public void prepareExpectedInputField(int inputFieldIndex) {}
}
