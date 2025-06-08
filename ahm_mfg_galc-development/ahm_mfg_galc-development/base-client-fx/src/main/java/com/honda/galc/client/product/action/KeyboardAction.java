package com.honda.galc.client.product.action;

import com.honda.galc.client.product.mvc.ProductController;

import javafx.event.ActionEvent;

/**
 * 
 * 
 * <h3>KeyboardAction Class description</h3>
 * <p> This class is used to create keyboard popup using product button </p>
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
 * @author L&T Infotech<br>
 * Nov 24, 2016
 *
 *
 */
public class KeyboardAction extends AbstractProductAction{
	
	public KeyboardAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent event) {
		getProductController().setKeyboardPopUpVisible(!getProductController().isKeyboardPopVisible());
	}
}
