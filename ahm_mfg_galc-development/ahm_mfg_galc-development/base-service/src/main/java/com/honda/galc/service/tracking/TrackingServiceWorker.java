package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.utils.ServiceUtil;

/**
 * @author Subu Kathiresan
 * @date May 5, 2014
 */
public class TrackingServiceWorker<T extends BaseProduct> implements Runnable {
	
	private ProductTrackerBase<T> tracker;
	private T product;
	private ProcessPoint processPoint;
	private DailyDepartmentSchedule schedule;
	private Timestamp actualTimestamp;
	private String associateNo;
	private String approverNo;
	private String deviceId;
	private Logger logger;

	public TrackingServiceWorker(ProductTrackerBase<T> tracker, T product,
			ProcessPoint processPoint, DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo,
			String approverNo, String deviceId) {
		this.tracker = tracker;
		this.product = product;
		this.processPoint = processPoint;
		this.schedule = schedule;
		this.actualTimestamp = actualTimestamp;
		this.associateNo = associateNo;
		this.approverNo = approverNo;
		this.deviceId = deviceId;
	}

	public void run() {
		trackProduct();
	}
	
	public void trackProduct() {
		long startTime = System.currentTimeMillis();
		
		int firstHistoryCreated= tracker.processHistory(product, processPoint, schedule, actualTimestamp, associateNo, approverNo,deviceId);
		tracker.processCounters(product, processPoint, schedule, firstHistoryCreated, actualTimestamp);
		
		logThreadMetrics(startTime);
	}

	private void logThreadMetrics(long startTime) {
		getLogger().info((System.currentTimeMillis() - startTime), 
				TrackingServiceWorker.class.getSimpleName() 
				+ " tracked product " 
				+ StringUtils.trimToEmpty(product.getProductId())
				+ " at process point "
				+ StringUtils.trimToEmpty(processPoint.getProcessPointId()));
	}
	
	public Logger getLogger() {
		if(logger == null)
			logger = ServiceUtil.getLogger(ServiceUtil.JPA_LOG);
		
		return logger;
	}
}