package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;

/**
 * <h3>Class description</h3>
 * Pulley ID processor.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Nov. 19, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20141119</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class PulleySnProcessor extends SubProductSnProcessor {

	public PulleySnProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected ProductSpec findProductSpec(String productSpec) {
		for(ProductSpecCode spec : context.getProductSpecCodes()) {
			if(spec.getProductSpecCode().trim().equals(productSpec)) 
				return spec;
		}

		throw new TaskException("Invalid product spec code:" + productSpec );
	}
	
	@Override
	protected Product getProductFromServer() {
		Product aProduct = (Product) super.getProductFromServer();
		if(!context.getProperty().getProductType().equals(aProduct.getProductType().name())) {
			String message = context.getProperty().getProductType() + " Product not exist: " + product.getProductId();
			Logger.getLogger().warn(message);
			handleException(message);
		}
		return aProduct;
	}
}
