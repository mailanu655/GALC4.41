package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class LotControlMbpnPersistenceManager extends LotControlPersistenceManagerExt{

	public LotControlMbpnPersistenceManager(ClientContext context) {
		super(context);
	}
	
	@Override
	public  List<MbpnProduct> findProductOnServer(String productId)
	{
		try
		{
			if(!ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(context.getProperty().getProductType()))) {
				Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
				return null;
			}
			
			MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
			return (List<MbpnProduct>) mbpnProductDao.findAllBySN(productId);
		}
		catch (Exception e) {
			Logger.getLogger().warn(e, "Failed searching for proudcts by serial number.");
			return null;
		}
	}
	
	@Override
	public synchronized MbpnProduct confirmProductOnServer(String productId) {
		
		if(!ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(context.getProperty().getProductType()))) {
			Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
			return null;
		}
		
		MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		MbpnProduct mbpnProduct = mbpnProductDao.findByKey(productId);
		return mbpnProduct;
	}


}
