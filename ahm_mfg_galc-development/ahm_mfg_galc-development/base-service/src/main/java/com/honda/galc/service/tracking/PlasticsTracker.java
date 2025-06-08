package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;

/** * * 
 * @version 0.1 
 * @author Gangadhararao Gadde 
 * @since Dec 21, 2012
 */
public class PlasticsTracker extends ProductTrackerBase<MbpnProduct>{
	//@Autowired
	MbpnProductDao mbpnProductDao;
	ProductResultDao productResultDao;

	@Override
	@Transactional
	void updateTrackingAttributes(MbpnProduct product, DailyDepartmentSchedule schedule) {
		try {
			MbpnProductDao dao = ServiceFactory.getDao(MbpnProductDao.class);
			dao.updateTrackingAttributes(product);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, "Failed to update tracking attributes for MbpnProduct, productId=", product.getProductId());
		}

	}

	@Override
	protected ProductResult createProductHistory(MbpnProduct product, ProcessPoint processPoint, DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo, String approverNo, String deviceId) {
		
		ProductResult productResult = (ProductResult) super.createProductHistory(product, processPoint, schedule, actualTimestamp, associateNo, approverNo,deviceId);
		productResult.setProductionDate(schedule != null ? 	schedule.getId().getProductionDate(): new java.sql.Date(new java.util.Date().getTime()));
		return productResult;
	}

	@Override
	public MbpnProduct findProductById(String productId) {
		return getMbpnProductDao().findByKey(productId);
	}
	
	protected ProductResultDao getProductResultDao() {
		if(productResultDao == null) {
			productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		}
		return productResultDao;
	}

	public MbpnProductDao getMbpnProductDao() {
		if(mbpnProductDao == null) {
			mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}

	@Override
	protected void saveProductHistory(ProductHistory productHistory){
		getProductResultDao().save((ProductResult) productHistory);
	}
		
	@Override
	boolean isProductHistoryExist(MbpnProduct product, ProcessPoint processPoint) {
		return ServiceFactory.getDao(ProductResultDao.class).isProductProcessed(product.getProductId(), 
				processPoint.getProcessPointId(), CommonUtil.getTimestampNow().toString());
	}
}
