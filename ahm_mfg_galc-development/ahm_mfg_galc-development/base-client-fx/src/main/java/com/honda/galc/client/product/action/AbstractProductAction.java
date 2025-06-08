package com.honda.galc.client.product.action;

import com.honda.galc.client.product.mvc.ProductController;

/**
 * 
 * 
 * <h3>AbstractProductAction Class description</h3>
 * <p> AbstractProductAction description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 10, 2014
 *
 *
 */
public abstract class AbstractProductAction extends AbstractAction {
	private ProductController productController;
	
	public AbstractProductAction(ProductController productController) {
		this.productController = productController;
	}

	protected ProductController getProductController() {
		return productController;
	}

	protected void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	
}
