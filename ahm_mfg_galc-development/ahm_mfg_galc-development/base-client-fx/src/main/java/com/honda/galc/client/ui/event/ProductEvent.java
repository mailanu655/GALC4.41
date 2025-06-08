package com.honda.galc.client.ui.event;

import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.ui.IEvent;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ProductEvent</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray
 */
public class ProductEvent implements IEvent{

	private ProductModel productModel;

	public ProductEvent(ProductModel productModel) {
		setProductModel(productModel);
	}
	
	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}
}
