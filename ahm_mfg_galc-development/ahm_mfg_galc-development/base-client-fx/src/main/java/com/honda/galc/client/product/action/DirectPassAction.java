package com.honda.galc.client.product.action;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * 
 * 
 * <h3>DirectPassAction Class description</h3>
 * <p> Action class for direct pass product panel button </p>
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
public class DirectPassAction extends AbstractProductAction{

	public DirectPassAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {
		if(arg0 != null && arg0.getSource() instanceof Button) {
			((Button) arg0.getSource()).setDisable(true);
		}

		AbstractView<?, ?> view = (AbstractView<?, ?>) getProductController().getView().getProductProcessPane().getSelectedProcessView();
		EventBusUtil.publishAndWait(new ProductEvent(view.getViewLabel(), ProductEventType.PRODUCT_DIRECT_PASSED));
	}
}
