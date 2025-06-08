package com.honda.galc.client.product.action;

import com.honda.galc.client.product.mvc.ProductController;

import javafx.event.ActionEvent;

/**
 * 
 * 
 * <h3>CancelAction Class description</h3>
 * <p> CancelAction description </p>
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

//for AEI DEMO, should delete later
public class CompleteAction extends AbstractProductAction{

	public CompleteAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {
		getProductController().cycleComplete();
	}

}
