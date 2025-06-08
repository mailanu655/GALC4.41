package com.honda.galc.client.product.action;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;

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
public class CancelAction extends AbstractProductAction{

	public CancelAction(ProductController productController) {
		super(productController);
	}

	@Override
	public void handle(ActionEvent arg0) {

		if (getProductController().getView().getProductProcessPane().isNavigatedFromRepairEntry()) {
			EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_REPAIR_DEFECT_DONE));
		} else {
			getProductController().cancel();
		}
	}

}
