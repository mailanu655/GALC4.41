package com.honda.galc.client.dc.view;


import javafx.scene.Node;
import com.honda.galc.client.dc.processor.LetResultProcessor;
import com.honda.galc.constant.OperationType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Min Sun
 * 
 */

public class LetResultView extends OperationInfoWidgetFX {

	private Node dataCollectionPanel = null;
	public LetResultView(LetResultProcessor processor) {
		super(processor);
		this.setRight(getLetButton());
		// TODO Auto-generated constructor stub
	}
	
	public Node getLetButton() {
		if (dataCollectionPanel == null) {
			dataCollectionPanel = new LetResultViewWidget((LetResultProcessor) getProcessor());
		}
		return dataCollectionPanel;
	}
	
	@Override
	public Node getDataCollectionPanel() {
		return null;
	}

	@Override
	public void populateCollectedData(InstalledPart installedPart) {
		if (getOperation().getType().equals(OperationType.INSTRUCTION)
				&& installedPart.getInstalledPartStatus().equals(InstalledPartStatus.OK)) {
			((LetResultViewWidget) dataCollectionPanel).getCompleteRejectBtn().setText("REJECT");
		} else {
			((LetResultViewWidget) dataCollectionPanel).getCompleteRejectBtn().setText("LETCheck");
		}
	}

}
