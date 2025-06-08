package com.honda.galc.service.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.ConrodHistoryDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.ConrodHistory;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;

public class ConrodTracker extends DieCastMachiningTracker<Conrod> {

	@Autowired
	ConrodHistoryDao conrodHistoryDao;
	@Autowired
	ConrodDao conrodDao;
	
	@Override
	@Transactional
	void updateTrackingAttributes(Conrod product, DailyDepartmentSchedule schedule) {
		try {
			conrodDao.updateTrackingAttributes(product);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, "Failed to update tracking attributes for Conrod, conrodId=", product.getProductId());
		}
	}

	@Override
	boolean isProductHistoryExist(Conrod product, ProcessPoint processPoint) {
		return getConrodHistoryDao().hasProductHistory(product.getConrodId(), processPoint.getProcessPointId());
	}

	@Override
	Conrod findProductById(String productId) {
		return getConrodDao().findByKey(productId);
	}

	@Override
	void saveProductHistory(ProductHistory productHistory) {
		getConrodHistoryDao().save((ConrodHistory)productHistory);
	}

	@Override
	protected boolean isProductShipped(Conrod product) {
		return super.isProductShipped(product) || isParentEngineShipped(product);
	}

	public ConrodHistoryDao getConrodHistoryDao() {
		if(conrodHistoryDao == null)
			conrodHistoryDao = ServiceFactory.getDao(ConrodHistoryDao.class);
		return conrodHistoryDao;
	}

	public ConrodDao getConrodDao() {
		if(conrodDao == null)
			ServiceFactory.getDao(ConrodDao.class);
		
		return conrodDao;
		
	}
	
}
