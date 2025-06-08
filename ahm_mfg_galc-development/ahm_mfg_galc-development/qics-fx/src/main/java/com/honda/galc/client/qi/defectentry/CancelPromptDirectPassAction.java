package com.honda.galc.client.qi.defectentry;

import com.honda.galc.client.product.action.AbstractProductAction;
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
public class CancelPromptDirectPassAction extends AbstractProductAction{

	private ProductController productController;
	public CancelPromptDirectPassAction(ProductController productController) {
		super(productController);
		this.productController=productController;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		
		
		CancelPromptActionHandler cancelPromptActionHandler=new CancelPromptActionHandler(actionEvent,productController,this);
		cancelPromptActionHandler.perfromCancelPromptAction();
}
}
