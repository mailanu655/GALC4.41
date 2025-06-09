package com.honda.ahm.lc.service;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.ParkChange;


@Service(value = "parkChangeService")
public class ParkChangeService extends BaseGalcService<ParkChange, String> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected Logger getLogger() {
		return logger;
	}

	public ParkChange findByProductId(String galcUrl, String productId) {

		try {
			getLogger().info("Find ParkChange record by ProductId-"+productId);
			ParkChange parkChange = findByProductId(galcUrl,productId, GalcDataType.PARK_CHG);
			return parkChange;
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	public void deleteParkChange(String galcUrl, String productId) {
		try {
			getLogger().debug("delete ParkChange record by ProductId-" + productId);
			deleteByProductId(galcUrl, productId, GalcDataType.PARK_CHG);
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	public ParkChange saveParkChange( String galcUrl, ParkChange entity) {

		try {
			getLogger().info("Save ParkChange record -"+entity.toString());
			return save( galcUrl,entity, GalcDataType.PARK_CHG);

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

}
