package com.honda.galc.client.product.action;

import com.honda.galc.client.product.mvc.ProductController;

import javafx.event.ActionEvent;

/**
 * 
 * 
 * <h3>SkipAction Class description</h3>
 * <p> SkipAction description </p>
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
public class SkipAction extends AbstractProductAction{

	public SkipAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {
		getProductController().cancel();
	}

}
