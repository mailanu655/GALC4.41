package com.honda.galc.client.datacollection.processor;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SubProductLot;

/**
 * 
 * <h3>KnucklesProductSnProcessor</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnucklesProductSnProcessor description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 22, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 22, 2012
 */
public class KnucklesProductSnProcessor extends SubProductSnProcessor{

	public KnucklesProductSnProcessor(ClientContext context) {
		super(context);
	}

	public void checkExpectedProduct(ProductId productId) {
		String expectedProductId = state.getExpectedProductId();
		if(expectedProductId != null && !StringUtils.isEmpty(expectedProductId) && !expectedProductId.equals(productId.getProductId()))
			handleException("Unexpected product received:" + productId.getProductId() + " while waiting " + expectedProductId);
	
		
	}

	@Override
	protected void validateProduct(Product aproduct) {

		SubProductLot subProductLot = findSubProductLot(aproduct.getProductId());
		if(!subProductLot.getPreProductionLot().getProductionLot().equals(context.getProductionLot()))
			throw new TaskException("Invalid product:" + aproduct.getProductId() + " is in current lot.");			

		super.validateProduct(aproduct);
	}
	
	
	
}
