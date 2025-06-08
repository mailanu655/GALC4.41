package com.honda.galc.client.dc.view;


import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.TextField;

import com.honda.galc.client.dc.processor.MeasurementProcessor;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;

/**
 * 
 * <h3>MeasurementView Class description</h3>
 * <p> MeasurementView description </p>
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
 * Feb 24, 2014
 *
 *
 */
public class MeasurementView extends AbstractDataCollectionWidget<MeasurementProcessor>{
	
	public MeasurementView(MeasurementProcessor processor) {
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
