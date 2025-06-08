package com.honda.galc.client.dc.view;


import javafx.scene.Node;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Min Sun
 * 
 */

public class GenericDeviceOperationView extends OperationInfoWidgetFX {

	private Node dataCollectionPanel = null;
	public GenericDeviceOperationView(OperationProcessor processor) {
		super(processor);
		this.setRight(getOperationView());
		// TODO Auto-generated constructor stub
	}
	
	public Node getOperationView() {
		if (dataCollectionPanel == null) {
			dataCollectionPanel = new GenericDeviceOperationViewWidget((OperationProcessor) getProcessor());
		}
		return dataCollectionPanel;
	}
	
	@Override
	public Node getDataCollectionPanel() {
		return null;
	}

	@Override
	public void populateCollectedData(InstalledPart installedPart) {
		DataCollectionViewUtil dcViewUtil = new DataCollectionViewUtil((GenericDeviceOperationViewWidget) dataCollectionPanel, getProcessor().getController().getModel());
		dcViewUtil.populateCollectedData(installedPart);
	}
}
