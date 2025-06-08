package com.honda.galc.client.dc.view;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.processor.IDataCollectionTaskProcessor;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;

public interface IDataCollectionWidget<P extends IDataCollectionTaskProcessor<? extends IDeviceData>> {
	
	public void populateCollectedData(InstalledPart installedPart);
	
	public MCOperationRevision getOperation();
	
	public ArrayList<TextField> getInputFields();
	
	public int getCurrentInputFieldIndex();
	
	public void setCurrentInputFieldIndex(int inputFieldIndex);
	
	public void setFocusToExpectedInputField();
	
	public void prepareExpectedInputField(int inputFieldIndex);
	
	public void register();
	
	public Node getDataCollectionPanel();
	
	public void addMeasurementBoxes(VBox vBox);
}
