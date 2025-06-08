package com.honda.galc.client.dc.view;

import com.honda.galc.client.dc.processor.OperationProcessor;

import javafx.scene.Node;
public class RearSealTiltCalculationView extends OperationInfoWidgetFX  {
	private Node dataCollectionPanel = null;

	public RearSealTiltCalculationView(OperationProcessor opProcessor) {
		super(opProcessor);
	}
	
	public Node getDataCollectionPanel() {
		if (dataCollectionPanel == null) {
			dataCollectionPanel = new RearSealTiltDCView(getProcessor());
			this.setRight(dataCollectionPanel);
		}
		return dataCollectionPanel;
	}

}
