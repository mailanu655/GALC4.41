package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3>
 * Class for Diecast SN processing.
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
 * <TD>Jun. 31, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150731</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class DieCastSnProcessor extends BaseProductSnProcessor {

	/**
	 * Constructors
	 * @param fsm
	 */
	public DieCastSnProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected ProductSpecCode findProductSpec(String productSpec) {
		for (ProductSpecCode spec : context.getProductSpecCodes()) {
			if (spec.getProductSpecCode().trim().equals(productSpec))
				return spec;
		}
		throw new TaskException("Invalid product spec code:" + productSpec);
	}

	@Override
	public String getProductSpecCode(String productId) {
		String productType = context.getProperty().getProductType();
		BaseProduct product = ProductTypeUtil.findProduct(productType, productId);
		BaseProductSpec spec = ProductTypeUtil.getProductSpecDao(productType).findByProductSpecCode(product.getModelCode(), productType);
		return spec.getProductSpecCode();
	}
}
