package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.ProductModel;

public class ProductProcessEvent extends ProductEvent {

	public ProductProcessEvent(ProductModel productModel) {
		super(productModel);
	}
}
