package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.SubProductLotCache;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProductLot;

/**
 * 
 * <h3>SubProductSnProcessor</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SubProductSnProcessor description </p>
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
 * @author Paul Chou
 * Nov 10, 2010
 *
 */
public class SubProductSnProcessor extends ProductIdProcessor{

	public SubProductSnProcessor(ClientContext context) {
		super(context);
	}
	
	@Override
	protected ProductSpec findProductSpec(String productSpec) {
		ProductSpec frameSpec = context.getFrameSpec(productSpec);

		if(frameSpec == null)
        	throw new TaskException("Invalid product spec code:" + productSpec );
        else return frameSpec;
	}


	public SubProductLot findSubProductLot(String productId) {
		for(SubProductLot lot : SubProductLotCache.getInstance().getSubProductLots()){
			ProductSpec productSpec = context.getFrameSpec(lot.getProductSpecCode());

			if(productSpec == null) continue;

			if(lot.isInLot(productId, productSpec.getProductNoPrefixLength())) {
				if(!context.isOnLine()) context.setCurrentPreProductionLot(lot.getPreProductionLot());
				return lot;
			}
		}

		throw new TaskException("Invalid product:" + productId + " is not included in sub product lots.");
	}
	
	

	@Override
	protected void confirmProductIdOnLocalCache() {
		SubProductLot subLot = findSubProductLot(product.getProductId());
		product.setProductSpec(subLot.getProductSpecCode());
		product.setSubId(subLot.getSubId());
		product.setKdLotNumber(subLot.getPreProductionLot().getKdLot());
		

		//must done before productIdOk
		loadLotControlRule();

		if(property.isCheckProcessedProduct()) 
			checkProcessedProductOnLocalCache();
	}

	@Override
	public String getProductSpecCode(String productId) {
		return findSubProductLot(productId).getProductSpecCode();
	}

	@Override
	protected boolean isNeedToLoadLotControlRules(ProductBean product) {
		return super.isNeedToLoadLotControlRules(product) || context.isRemake();
	}
	
}
