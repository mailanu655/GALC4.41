package com.honda.ahm.lc.service;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.ShippingStatus;

@Service(value = "shippingStatusService")
public class ShippingStatusService extends BaseGalcService<ShippingStatus, String> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected Logger getLogger() {
		return logger;
	}

	public ShippingStatus findByProductId(String galcUrl, String productId) {

		try {
			getLogger().info("Find ShippingStatus record by ProductId-"+productId);
			ShippingStatus shippingStatus = findByProductId(galcUrl,productId, GalcDataType.SHIPPING_STATUS);
			return shippingStatus;
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	public ShippingStatus saveShippingStatus( String galcUrl, ShippingStatus entity) {

		try {
			getLogger().info("Save ShippingStatus record -"+entity.toString());
			return save( galcUrl,entity, GalcDataType.SHIPPING_STATUS);

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

}
