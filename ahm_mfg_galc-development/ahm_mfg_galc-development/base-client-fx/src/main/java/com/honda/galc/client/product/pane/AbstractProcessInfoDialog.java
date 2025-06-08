package com.honda.galc.client.product.pane;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.FxDialog;

/**
 * 
 * 
 * <h3>AbstractProcessInfoDialog Class description</h3>
 * <p> AbstractProcessInfoDialog description </p>
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
 * Mar 17, 2014
 *
 *
 */
public class AbstractProcessInfoDialog extends FxDialog{
	
	ProductController productController ;
	
	public AbstractProcessInfoDialog(String title, ProductController controller) {
		super(title, ClientMainFx.getInstance().getStage());
		this.productController = controller;
		this.toFront();
	}

	protected ProductController getProductController() {
		return productController;
	}

	protected void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	

}
