package com.honda.galc.service.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.CrankshaftHistoryDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.CrankshaftHistory;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;

public class CrankshaftTracker extends DieCastMachiningTracker<Crankshaft> {

	@Autowired
	CrankshaftHistoryDao crankshaftHistoryDao;
	@Autowired
	CrankshaftDao crankshaftDao;
	
	@Override
	@Transactional
	void updateTrackingAttributes(Crankshaft product, DailyDepartmentSchedule schedule) {
		try {
			crankshaftDao.updateTrackingAttributes(product);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, "Failed to update tracking attributes for Crankshaft, crankshaftId=", product.getProductId());
		}
	}

	@Override
	boolean isProductHistoryExist(Crankshaft product, ProcessPoint processPoint) {
		return getCrankshaftHistoryDao().hasProductHistory(product.getCrankshaftId(), processPoint.getProcessPointId());
	}

	@Override
	Crankshaft findProductById(String productId) {
		return getCrankshaftDao().findByKey(productId);
	}

	@Override
	void saveProductHistory(ProductHistory productHistory) {
		getCrankshaftHistoryDao().save((CrankshaftHistory)productHistory);
	}

	@Override
	protected boolean isProductShipped(Crankshaft product) {
		return super.isProductShipped(product) || isParentEngineShipped(product);
	}

	public CrankshaftHistoryDao getCrankshaftHistoryDao() {
		if(crankshaftHistoryDao == null)
			crankshaftHistoryDao = ServiceFactory.getDao(CrankshaftHistoryDao.class);
		return crankshaftHistoryDao;
	}

	public CrankshaftDao getCrankshaftDao() {
		if(crankshaftDao == null)
			ServiceFactory.getDao(CrankshaftDao.class);
		
		return crankshaftDao;
		
	}
	
}
