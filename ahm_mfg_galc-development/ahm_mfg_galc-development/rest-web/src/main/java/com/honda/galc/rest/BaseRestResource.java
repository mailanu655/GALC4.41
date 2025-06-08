package com.honda.galc.rest;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Mar 22, 2018
 */
public abstract class BaseRestResource {
	
	protected String getPrefix(String name) {
		// convert first character to upper case
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	protected InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);
	}
	
	protected MeasurementDao getMeasurementDao() {
		return ServiceFactory.getDao(MeasurementDao.class);
	}
	
	protected PreProductionLotDao getPreProductionLotDao() {
		return ServiceFactory.getDao(PreProductionLotDao.class);
	}
	
	protected Logger getLogger(String name){
		return Logger.getLogger(name);
	}
}
