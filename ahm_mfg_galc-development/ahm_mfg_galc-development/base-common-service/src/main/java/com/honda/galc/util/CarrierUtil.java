package com.honda.galc.util;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.device.dataformat.CarrierId;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * @author vec15809
 *
 */
public class CarrierUtil {
	
	static GtsCarrierDao carrierDao;
	public static String findProductIdByCarrier(String trackingArea, String carrierId) {
		GtsCarrier carrier = null;
		if(StringUtil.isNumeric(carrierId)) {
			int carrierIdInt = Integer.parseInt(carrierId);
			if(carrierIdInt != 0) //Empty Carrier
				carrier = getCarrierDao().findByCarrierId(trackingArea, carrierIdInt);
		}
		carrier =  getCarrierDao().findByKey(new GtsCarrierId(trackingArea,carrierId));
		
		return carrier == null ? "" : carrier.getProductId();
		 
	}
	
	public static GtsCarrierDao getCarrierDao() {
		if(carrierDao == null)
			carrierDao = ServiceFactory.getDao(GtsCarrierDao.class);
		return carrierDao;
	}
	
	public static ProductId findProductIdByCarrier(String trackingArea, CarrierId carrierId) {
		String productId = findProductIdByCarrier(trackingArea, carrierId.getCarrierId());
		return new ProductId(productId);
	}
}
