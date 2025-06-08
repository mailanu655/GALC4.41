package com.honda.galc.client.teamleader.let.util;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.SmartEyeLabelDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * @author Sachin Kudikala
 * @date Nov 20, 2014
 */
public class TrackingMaintenanceUtil {

	public DataContainer updateSmartEyeLabel(DataContainer dc){
		Product product = (Product)dc.get("PRODUCT");
		Logger.getLogger().debug("Updating SmartEye i.e. GAL228TBX table for product id:" + product.getProductId());
		SmartEyeLabelDao dao = ServiceFactory.getDao(SmartEyeLabelDao.class);
		String productId = product.getProductId();
		int updateSuccess = dao.updateSmartEyeLabel(productId);
		if(updateSuccess == 0)
		{
			Logger.getLogger().warn("No SmartEye lable is assigned to product: " + product.getProductId());
			dc.put("MESSAGE", "No SmartEye lable is assigned to product: " + product.getProductId());
		}
		else{
			Logger.getLogger().debug("Updated SmartEye i.e. GAL228TBX table for product id:" + product.getProductId());
			dc.put("MESSAGE", "Removed SmartEye (i.e. GAL228TBX table) for product id:" + product.getProductId());
		}
		
		return dc;
	
	}
	
}
