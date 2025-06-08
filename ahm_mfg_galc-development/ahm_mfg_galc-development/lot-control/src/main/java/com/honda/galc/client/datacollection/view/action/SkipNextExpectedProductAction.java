package com.honda.galc.client.datacollection.view.action;

import com.honda.galc.client.datacollection.ClientContext;

public class SkipNextExpectedProductAction extends SkipProductButtonAction {
	private static final long serialVersionUID = 1L;
	
	public SkipNextExpectedProductAction(ClientContext context, String name) {
		super(context, name);
	}

	protected String skipProduct() {
		populateIncomingProducts();
		getExpProductIdSelectView().updateDialog(incomingProducts);
		
		// OK button is clicked
		if (!incomingProducts.isEmpty()) getExpProductIdSelectView().done();
		return expProductIdSelectView.getSkippedToProducts();
	}
	
	protected void doSkipProduct() {
		if (context.isCheckExpectedProductId()) {
			skipSingleProduct();
		}
	}
}
