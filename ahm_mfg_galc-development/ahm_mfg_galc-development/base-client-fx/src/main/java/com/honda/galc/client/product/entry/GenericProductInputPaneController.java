package com.honda.galc.client.product.entry;

public class GenericProductInputPaneController {
	GenericProductEntryTabPane view;
	
	GenericProductInputPaneModel model;
	
	public GenericProductInputPaneController(GenericProductEntryTabPane view) {
		this.view = view;
		this.model = new GenericProductInputPaneModel(view.getApplicationContext().getApplicationId());
	}
	
	public String[] getPanels() {
		return model.getPanels();
	}
}
