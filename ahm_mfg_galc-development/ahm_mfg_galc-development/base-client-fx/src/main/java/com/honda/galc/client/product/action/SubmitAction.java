package com.honda.galc.client.product.action;

import javafx.event.ActionEvent;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;

/**
 * 
 * 
 * <h3>SubmitAction Class description</h3>
 * <p> SubmitAction description </p>
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
public class SubmitAction extends AbstractProductAction{

	public SubmitAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {
		AbstractView<?, ?> view = (AbstractView<?, ?>) getProductController().getView().getProductProcessPane().getSelectedProcessView();
		EventBusUtil.publishAndWait(new ProductEvent(view.getViewLabel(), ProductEventType.PRODUCT_DEFECT_DONE));
	}
}
