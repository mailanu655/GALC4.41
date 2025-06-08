package com.honda.galc.client.product.action;

import javafx.event.ActionEvent;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;

public class ProductCheckDoneAction extends AbstractProductAction{

	public ProductCheckDoneAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {
		AbstractView<?, ?> view = (AbstractView<?, ?>) getProductController().getView().getProductProcessPane().getSelectedProcessView();
		EventBusUtil.publishAndWait(new ProductEvent(view.getViewLabel(), ProductEventType.PRODUCT_CHECK_DONE));
	}
}
