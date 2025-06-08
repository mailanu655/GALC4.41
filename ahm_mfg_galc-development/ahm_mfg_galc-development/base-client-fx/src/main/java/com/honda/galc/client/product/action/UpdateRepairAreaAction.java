package com.honda.galc.client.product.action;

import javafx.event.ActionEvent;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;

public class UpdateRepairAreaAction extends AbstractProductAction{

	public UpdateRepairAreaAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {
		AbstractView<?, ?> view = (AbstractView<?, ?>) getProductController().getView().getProductProcessPane().getSelectedProcessView();
		EventBusUtil.publishAndWait(new ProductEvent(view.getViewLabel(), ProductEventType.PRODUCT_UPDATE_REPAIR_AREA));
	}
}
