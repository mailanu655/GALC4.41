package com.honda.galc.client.product.process;

import com.honda.galc.client.mvc.IModel;
import com.honda.galc.client.product.mvc.ProductModel;

/**
 * 
 * <h3>AbstractProcessModel Class description</h3>
 * <p> AbstractProcessModel description </p>
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
 * Feb 24, 2014
 *
 *
 */
public abstract class AbstractProcessModel implements IModel{

	protected ProductModel productModel;

	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}
	
	public void reset() {
		if(productModel != null)
			productModel.reset();
	}
}
