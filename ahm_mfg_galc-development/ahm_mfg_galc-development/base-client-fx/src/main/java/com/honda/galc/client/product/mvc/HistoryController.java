package com.honda.galc.client.product.mvc;

import com.honda.galc.client.product.process.AbstractProcessController;

public class HistoryController extends AbstractProcessController<HistoryModel, HistoryView>{

	public HistoryController(HistoryModel model,HistoryView view) {
		super(model,view);
	}

	@Override
	public void initEventHandlers() {
		
	}

}
