package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class MBPNProcessor extends ProductIdProcessor {

	public MBPNProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	public Mbpn findProductSpec(String productSpecCode) {
		MbpnDao mbpnSpecDao = ServiceFactory.getDao(MbpnDao.class);
		Mbpn mbpnSpec = mbpnSpecDao.findByProductSpecCode(productSpecCode, context.getProperty().getProductType());
		return mbpnSpec;
		
	}
	
	@Override
	public String getProductSpecCode(String productId) {
		String productType = context.getProperty().getProductType();
		MbpnProduct mbpnProduct = (MbpnProduct) ProductTypeUtil.findProduct(productType, productId);
		return ProductTypeUtil.getProductSpecDao(productType).findByProductSpecCode(mbpnProduct.getModelCode(), productType).getProductSpecCode();
	}
	
	protected boolean confirmProdIdOnServer() throws Exception{
		Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");
		MbpnProduct aproduct = (MbpnProduct) getProductFromServer();

		//must done before productIdOk
		loadLotControlRule();
		
		product.setProductionLot(aproduct.getProductionLot());
		validateProduct(aproduct);

		doRequiredPartCheck();


		Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		return true;
	}

}
