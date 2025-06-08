package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.CounterByProductSpecDao;
import com.honda.galc.dao.product.CounterByProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.CounterByModelGroupId;
import com.honda.galc.entity.product.CounterByProductSpec;
import com.honda.galc.entity.product.CounterByProductSpecId;
import com.honda.galc.entity.product.CounterByProductionLot;
import com.honda.galc.entity.product.CounterByProductionLotId;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>ProductTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTracker is base class for traditional product which are using GALC product history,
 * product build result table such as Engine, Frame </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Aug 27, 2010
 *
 * @param <T>
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public abstract class ProductTracker<T extends Product> extends ProductTrackerBase<T> {

	ProductResultDao productResultDao;
	CounterByProductionLotDao counterByProductionLotDao;
	CounterByProductSpecDao counterByProductSpecDao;
	
	private static final Object _counterByProdLotSyncLock = new Object();
	private static final Object _counterByProdSpecSyncLock = new Object();
	
	protected abstract ProductSpec getProductSpec(String productSpecCode);
	protected abstract ProductDao<T> getProductDao();
	
	@Override
	protected void setProductAttributes(T product, DailyDepartmentSchedule schedule) {
		super.setProductAttributes(product, schedule);
		
		if (schedule != null && schedule.getId().getProductionDate() != null) {
			product.setProductionDate(schedule.getId().getProductionDate());
		}
	}

	@Override
	 protected ProductHistory createProductHistory(T product, ProcessPoint processPoint, DailyDepartmentSchedule schedule,
			 Timestamp actualTimestamp, String associateNo, String approverNo){
		
		ProductResult productResult = (ProductResult) super.createProductHistory(product, processPoint, schedule, actualTimestamp, associateNo, approverNo);
		productResult.setProductionDate(schedule != null ? 	schedule.getId().getProductionDate(): new java.sql.Date(new java.util.Date().getTime()));
		return productResult;
	}


	@Override
	protected void saveProductHistory(ProductHistory productHistory){
		getProductResultDao().save((ProductResult) productHistory);
	}
		
	@Override
	boolean isProductHistoryExist(T product, ProcessPoint processPoint) {
		return ServiceFactory.getDao(ProductResultDao.class).isProductProcessed(product.getProductId(), 
				processPoint.getProcessPointId(), CommonUtil.getTimestampNow().toString());
	}
	
	@Override
	protected void doProcessCounters(T product, ProcessPoint processPoint,
			DailyDepartmentSchedule schedule) {
		
		super.doProcessCounters(product, processPoint, schedule);
		
		try {
			if (property.isProcessCountByProductionLot())
				countByProductionLot(getCounterByProductionLotId(schedule,product, processPoint));
			if (property.isProcessCountByProductSpec())
				countByProductSpec(getCounterByProductSpecId(schedule, product,	processPoint));
		} catch (Throwable e) {
			getLogger().warn(e, "Exception thrown in method doProcessCounters");
		}		
	}
	
	@Transactional
	protected void countByProductionLot(CounterByProductionLotId id) {
		if (id == null || id.getProductionLot() == null || id.getProductionLot().trim().equals(""))
			return;
		
		try {
			//TODO remove synchronized block and implement using PESSIMISTIC locking when
			//migrating to JPA 2.0
			synchronized (_counterByProdLotSyncLock) {
				CounterByProductionLot counter = getCounterByProductionLotDao().findByKey(id);
				if (counter == null) {
					getCounterByProductionLotDao().insert(new CounterByProductionLot(id));
				}
				getCounterByProductionLotDao().incrementCounter(id);
			}
		} catch (Throwable e) {
			getLogger().info(e, "Exception thrown in method countByProductionLot.");
		}		
		logFinishCountByProductionLot(id);
	}
	
	private void logFinishCountByProductionLot(CounterByProductionLotId id) {
		getLogger().info("Finished process CounterByProductionLot, ", getTrackingPointLogMsg(),
				" shift=", id.getShift(), " period=" + id.getPeriod(), " productionLot=", id.getProductionLot());
	}
	
	@Transactional
	protected void countByProductSpec(CounterByProductSpecId id) {
		if (id == null || id.getProductSpecCode() == null || id.getProductSpecCode().trim().equals(""))
			return;
		
		try {
			//TODO remove synchronized block and implement using PESSIMISTIC locking when
			//migrating to JPA 2.0
			synchronized (_counterByProdSpecSyncLock) {
				CounterByProductSpec counter = getCounterByProductSpecDao().findByKey(id);
				if (counter == null) {
					getCounterByProductSpecDao().insert(new CounterByProductSpec(id));
				}
				getCounterByProductSpecDao().incrementCounter(id);
			}
		} catch (Throwable e) {
			getLogger().info(e, "Exception thrown in method countByProductSpec.");
		}		
		logFinishCountByProductSpec(id);
	}
	
	private void logFinishCountByProductSpec(CounterByProductSpecId id) {
		getLogger().info("Finished process CounterByProductSpec, ", getTrackingPointLogMsg(),
				" shift=", id.getShift(), " period=" + id.getPeriod(), " productSpecCode=", id.getProductSpecCode());
	}

	protected CounterByProductSpecId getCounterByProductSpecId(DailyDepartmentSchedule schedule,
			T product, ProcessPoint processPoint) {
		return new CounterByProductSpecId(processPoint.getProcessPointId(), 
				schedule.getId().getProductionDate(), schedule.getId().getShift(), 
				schedule.getId().getPeriod(), product.getProductSpecCode());
	}

	protected CounterByProductionLotId getCounterByProductionLotId(
			DailyDepartmentSchedule schedule, T product,
			ProcessPoint processPoint) {
		return new CounterByProductionLotId(processPoint.getProcessPointId(), 
				schedule.getId().getProductionDate(), schedule.getId().getShift(), 
				schedule.getId().getPeriod(), product.getProductionLot());
	}

	@Override
	protected CounterByModelGroupId getCounterByModelGroupId(
			DailyDepartmentSchedule schedule, T product,
			ProcessPoint processPoint) {
		
		ProductSpec productSpec = getProductSpec(product.getProductSpecCode());
		if(productSpec == null){
			getLogger().error("Failed to find product spec, counterByModel skipped. ", 
					getTrackingPointLogMsg(), " productSpecCode=" + product.getProductSpecCode());
			return null;
		}
		
		return new CounterByModelGroupId(processPoint.getProcessPointId(), 
				schedule.getId().getProductionDate(), schedule.getId().getShift(), 
				schedule.getId().getPeriod(), productSpec.getModelCode(), productSpec.getModelYearCode());
	}

	protected ProductResultDao getProductResultDao() {
		if(productResultDao == null)
			productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		return productResultDao;
	}


	public CounterByProductionLotDao getCounterByProductionLotDao() {
		if(counterByProductionLotDao == null)
			counterByProductionLotDao = ServiceFactory.getDao(CounterByProductionLotDao.class);
		return counterByProductionLotDao;
	}

	public CounterByProductSpecDao getCounterByProductSpecDao() {
		if(counterByProductSpecDao == null)
			counterByProductSpecDao = ServiceFactory.getDao(CounterByProductSpecDao.class);
		return counterByProductSpecDao;
	}
	
	@Transactional
	@Override
	void updateTrackingAttributes(T product, DailyDepartmentSchedule schedule) {
		try {
			if (isProductOffProcessPoint() && product.getActualOffDate() == null) {
				
				// if tracking on a Product-Off process point 
				// and the actual off date has not been set, 
				// use scheduled production date as the line off date 
				if (schedule == null || schedule.getId().getProductionDate() == null) {
					getLogger().warn("No valid Schedule found. Unable to update Actual Off date for product ", product.getProductId());
				} else {
					product.setActualOffDate(schedule.getId().getProductionDate());
				}
				getProductDao().updateProductOffPPTrackingAttributes(product);
			} else {
				getProductDao().updateTrackingAttributes(product);
			}
		} catch (Exception e) {
			getLogger().error(e, "Failed to update tracking attributes for Frame, productId=", product.getProductId());
		}
	}
}
