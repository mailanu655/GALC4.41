package com.honda.galc.client.dc.view;


import javafx.scene.Node;
import com.honda.galc.client.dc.processor.OperationProcessor;

/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */

public class InstructionWithoutCompleteBtnView extends OperationInfoWidgetFX {

	private Node dataCollectionPanel = null;
	public InstructionWithoutCompleteBtnView(OperationProcessor processor) {
		super(processor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Node getDataCollectionPanel() {
		if (dataCollectionPanel == null) {
			dataCollectionPanel = new InstructionWithoutCompleteBtnViewWidget((OperationProcessor) getProcessor());
			this.setRight(dataCollectionPanel);
		}
		return dataCollectionPanel;
	}
}
