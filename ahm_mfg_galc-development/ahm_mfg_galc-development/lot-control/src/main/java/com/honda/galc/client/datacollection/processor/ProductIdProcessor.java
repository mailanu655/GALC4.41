package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>ProductIdProcessor</h3>
 * <h4>
 * Product Id processor - used to verify Product Id from both of
 * device and gui client input
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class ProductIdProcessor extends BaseProductSnProcessor {

	/**
	 * Constructors
	 * @param fsm
	 */
	public ProductIdProcessor(ClientContext context) {
		super(context);
	}

	protected void confirmProductIdOnLocalCache() {
		product.setProductSpec(getProductSpecCode(product.getProductId()));
		product.setSubId(getSubId());
		if(context.isAfOnSeqNumExist()){
			product.setAfOnSequenceNumber(getAfOnSequenceNumber());
		}
		if(context.isProductLotCountExist()){
			state.setLotSize(0);
			state.setProductCount(0);
		}
		//must done before productIdOk
		loadLotControlRule();

		if(property.isCheckProcessedProduct()) 
			checkProcessedProductOnLocalCache();

	}

	/**
	 * Check if the product Id exists in database and is valid.
	 * @return
	 * @throws Exception 
	 */
	protected boolean confirmProdIdOnServer() throws Exception{
		Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");
		BaseProduct baseProduct = getProductFromServer();

		//must done before productIdOk
		if(!context.getProperty().isSkipDataCollection())
			loadLotControlRule();

		if(baseProduct instanceof Product) {
			Product aproduct = (Product) getProductFromServer();
			product.setProductionLot(aproduct.getProductionLot());
			product.setSubId(aproduct.getSubId());
			product.setKdLotNumber(aproduct.getKdLotNumber());

			validateProduct(aproduct);
			
		}
							
		doRequiredPartCheck();
		

		Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		return true;
	}

	protected void validateProduct(Product aproduct) {
		super.validateProduct(aproduct);
		
		if(context.isAfOnSeqNumExist()){
			if(!String.valueOf(aproduct.getAfOnSequenceNumber()).equalsIgnoreCase("null"))
			product.setAfOnSequenceNumber(String.valueOf(aproduct.getAfOnSequenceNumber()));
		}
	}
}