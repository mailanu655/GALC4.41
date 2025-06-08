package com.honda.galc.service.tracking;

import java.net.HttpURLConnection;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.CounterByModelGroupDao;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.CounterByModelGroup;
import com.honda.galc.entity.product.CounterByModelGroupId;
import com.honda.galc.entity.product.CounterId;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.net.HttpClient;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.ProductTrackedMessageSender;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public abstract class ProductTrackerBase<T extends BaseProduct> implements Tracker<T> {

	@Autowired
	private ProcessPointDao processPointDao;
	@Autowired
	private GpcsDivisionDao gpcsDivisionDao;
	@Autowired
	private DailyDepartmentScheduleDao dailyDepartmentScheduleDao;
	@Autowired
	private CounterDao counterDao;
	@Autowired
	private CounterByModelGroupDao counterByModelGroupDao;
	
	protected String processPointId;
	protected ProcessPoint processPoint;
	protected TrackingPropertyBean property;
	protected StringBuilder trackingPointLogMsg;
	
	private Logger logger;
	private IProductSequenceManager<T> productSequenceManager;
	
	private static final Object _counterSyncLock = new Object();
	private static final Object _counterByModelSyncLock = new Object();
	
	abstract void updateTrackingAttributes(T product, DailyDepartmentSchedule schedule);
	abstract boolean isProductHistoryExist(T product, ProcessPoint processPoint);
	abstract T findProductById(String productId);

	abstract void saveProductHistory(ProductHistory productHistory);

	public ProductTrackerBase() {
		super();
	}

	public void track(T product, String processPointId) {
		try {
			doTracking(product, processPointId, getProcessPointDao().getDatabaseTimeStamp(), "", "","");
		} catch (Throwable e) {
			getLogger().error(e, "Exception to track product:", product.getProductId(),
					" at processPoint:", processPointId);
		}		
	}
	
	
	public void track(T product, String processPointId, String deviceId) {
		try {
			doTracking(product, processPointId, getProcessPointDao().getDatabaseTimeStamp(), "", "",deviceId);
		} catch (Throwable e) {
			getLogger().error(e, "Exception to track product:", product.getProductId(),
					" at processPoint:", processPointId);
		}		
	}

	public void track(String productId, String processPointId) {
		track(findProductById(productId), processPointId);
	}
	
	public void track(String productId, String processPointId, String deviceId) {
		track(findProductById(productId), processPointId,deviceId);
	}
	public void track(ProductHistory productHistory) {
		try {
			doTracking(findProductById(productHistory.getProductId()), productHistory.getProcessPointId(), 
					checkActualTimestamp(productHistory.getActualTimestamp()), productHistory.getAssociateNo(), productHistory.getApproverNo(), productHistory.getDeviceId());
		} catch (Throwable e) {
			getLogger().error(e, "Exception to track product:", productHistory.getProductId(),
					" at processPoint:", productHistory.getProcessPointId());
		}
	}
	
	private Timestamp checkActualTimestamp(Timestamp actualTimestamp) {
		Timestamp dbTimestamp = getProcessPointDao().getDatabaseTimeStamp();
		// if actual timestamp is null or in future, use database timestamp
		if (actualTimestamp == null || actualTimestamp.after(dbTimestamp)) {
			return dbTimestamp; 
		}
		
		return actualTimestamp;
	}


	private void doTracking(T product, String processPointId, Timestamp actualTimestamp, 
			String associateNo, String approverNo, String deviceId) {
		resetLogger();
		long start = System.currentTimeMillis();
		this.processPointId = processPointId;
		
		getLogger().info("Enter tracking ...");
		if(!validateTracking(product, processPointId)) return;
		getLogger().info("Start tracking ", product.getClass().getSimpleName(), 
				", product=",product.getProductId());
		
		processPoint = getProcessPoint(product, processPointId);
		if(processPoint == null) return;
		
		backFill(product, processPoint, actualTimestamp, associateNo, approverNo);
		
		trackIt(product, processPoint, actualTimestamp, associateNo, approverNo,deviceId);
		getLogger().info("Finished tracking, product=",product.getProductId(), 
				" processPoint=", processPointId, " time spent: " + (System.currentTimeMillis() - start));
		if(getProperty().isUpdateNewLcTracking()) {
			updateTrackingMicroservice(product.getProductId(),processPointId, getProperty().getLcTrackingUrl());
		}
	}

	private boolean validateTracking(T product, String processPointId) {
		
		createTrackingPointMsg(product, processPointId);
		
		if(product == null || StringUtils.isEmpty(processPointId)){
			getLogger().info("Tracking: skipped, no product/process to be tracked. ", getTrackingPointLogMsg());
			return false;
		}
		
		if(isProductShipped(product) && !getProperty().isUpdateProductTrackingStatusAfterShipped()){
			getLogger().info("Tracking: skipped, product aleady shipped. ", getTrackingPointLogMsg());
			return false;
		}
		
		return true;
	}
	
	private ProcessPoint getProcessPoint(T product, String processPointId) { 
	
		ProcessPoint processPoint = getProcessPointDao().findByKey(processPointId);
		if(processPoint == null){
			getLogger().error("Tracking: error, invalid Process Point Id. ", getTrackingPointLogMsg());
		}
		return processPoint;
	}

	//Getters
	private ProcessPointDao getProcessPointDao() {
		if(processPointDao == null)
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		return processPointDao;
	}

	public GpcsDivisionDao getGpcsDivisionDao() {
		if(gpcsDivisionDao == null)
			gpcsDivisionDao = ServiceFactory.getDao(GpcsDivisionDao.class);
		return gpcsDivisionDao;
	}

	//Utility methods
	protected boolean isProductShipped(T product) {
		BaseProduct product1 = ProductTypeUtil.getTypeUtil(product.getProductType()).findProduct(product.getProductId());
		String lastPP = product1.getLastPassingProcessPointId();
		return isProcessPointShipping(lastPP);
	}

	protected boolean isProcessPointShipping(String lastPassingProcessPointId) {
		if(lastPassingProcessPointId == null) return false;
		
		List<String> shippingProcessPoints = getShippingProcessPoints(lastPassingProcessPointId);
		return shippingProcessPoints != null && !shippingProcessPoints.isEmpty() &&
				shippingProcessPoints.contains(lastPassingProcessPointId);
	}

	private List<String> getShippingProcessPoints(String processPointId) {
		String shippingProcessPointIds = getProperty().getShippingProcessPointIds();
		if(!StringUtils.isEmpty(shippingProcessPointIds)){
			return Arrays.asList(shippingProcessPointIds.split(","));
		}
		return null;
	}
	
	private void backFill(T product, ProcessPoint processPoint, Timestamp actualTimestamp, 
			String associateNo, String approverNo) {
		List<ProcessPoint> backFillStack = getBackFillStack(product, processPoint);
		
		for(ProcessPoint ppoint : backFillStack){
			getLogger().info("BackFill, processPoint=", ppoint.getProcessPointId(), " revocery=" + 
					ppoint.getRecoveryPointFlag(), " productId ", product.getProductId());
			
			if(ppoint.getRecoveryPointFlag() == 1){
				doBackFill(product, ppoint, actualTimestamp, associateNo, approverNo);
			}
		}
	}

	private List<ProcessPoint> getBackFillStack(T product,ProcessPoint processPoint) {
		String backFillProcessPointId = processPoint.getBackFillProcessPointId();
		String processPointId = processPoint.getProcessPointId();
		List<ProcessPoint> backFillStack = new ArrayList<ProcessPoint>();
		
		while(!StringUtils.isEmpty(backFillProcessPointId)){
			if(backFillProcessPointId.equals(processPoint.getProcessPointId())){
				getLogger().warn("Tracking", "backfill configuration error - Backfill Process Point Id=",
						" is the same as Process Point Id=", processPointId);
				break;
			}
			
			ProcessPoint backfillProcessPoint = getProcessPoint(product, backFillProcessPointId);
			if(backfillProcessPoint == null){
				getLogger().warn("Tracking, invalid back fill process point, Backfill Process Point Id=",
						backFillProcessPointId, " Process Point Id=", processPointId, " Product Id=", product.getProductId());
				break;
			}
			
			if(backFillStack.contains(backfillProcessPoint)){
				getLogger().warn("Backfill configuration error - loop detected: Process Point Id:",
						backFillStack.get(0).getProcessPointId(), " repointing to ", backFillStack.get(0).getBackFillProcessPointId());
				break;
			}
			
			if(isProductHistoryExist(product, backfillProcessPoint)){
				break;
			}
			
			backFillStack.add(0, backfillProcessPoint);
			processPointId = backfillProcessPoint.getProcessPointId();
			backFillProcessPointId = backfillProcessPoint.getBackFillProcessPointId();

		}
		return backFillStack;
	}
	
	private void doBackFill(T product, ProcessPoint ppoint, Timestamp actualTimestamp, String associateNo, String approverNo) {
		if(ppoint.getPassingCountFlag() != 1) return;
		
		getLogger().info("Tracking start Backfill,", getTrackingPointLogMsg());
		DailyDepartmentSchedule schedule = findSchedule(product, ppoint, actualTimestamp);
		setProductAttributes(product, schedule);
		
		updateHistoryAndCounters(product, ppoint, schedule, actualTimestamp, associateNo, approverNo,null);
	}
	
	@Transactional
	public int processHistory(T product, ProcessPoint processPoint,	DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo, String approverNo) {
		int historyCount = 0;
		try {
			StringBuilder msg = new StringBuilder();
		
			List<? extends ProductHistory> productHistoryList = findAllProductHistory(product, processPoint);
			if (productHistoryList != null) {
				historyCount = productHistoryList.size();
			}
			
			if (getProperty().isTrackingHistoryUnique() && historyCount >=1) {
				msg.append("Product Unique History already exists and did not create history, ");
				ProductHistory latestProductHistory = productHistoryList.get(0);
				latestProductHistory.setUpdateTimestamp(getProcessPointDao().getDatabaseTimeStamp());
				saveProductHistory(latestProductHistory);
			}else {
				if(historyCount == 0)
					msg.append("Product Unique History created, ");
				else 
					msg.append("Product History created, ");
				ProductHistory productHistory = createProductHistory(product, processPoint, schedule, 
						actualTimestamp, associateNo, approverNo);
				productHistory.setProcessCount(historyCount+1);
				saveProductHistory(productHistory);
			}
	
			getLogger().info(msg.toString(), getTrackingPointLogMsg());
			
		} catch (Exception e) {
			getLogger().error(e, "Failed to create Product History: ", getTrackingPointLogMsg());
		}
		
		return historyCount+1;
	}
	
	@Transactional
	public int processHistory(T product, ProcessPoint processPoint,	DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo, String approverNo, String deviceId) {
		int historyCount = 0;
		try {
			StringBuilder msg = new StringBuilder();
		
			List<? extends ProductHistory> productHistoryList = findAllProductHistory(product, processPoint);
			if (productHistoryList != null) {
				historyCount = productHistoryList.size();
			}
			
			if (getProperty().isTrackingHistoryUnique() && historyCount >=1) {
				msg.append("Product Unique History already exists and did not create history, ");
				ProductHistory latestProductHistory = productHistoryList.get(0);
				latestProductHistory.setUpdateTimestamp(getProcessPointDao().getDatabaseTimeStamp());
				saveProductHistory(latestProductHistory);
			}else {
				if(historyCount == 0)
					msg.append("Product Unique History created, ");
				else 
					msg.append("Product History created, ");
				ProductHistory productHistory = createProductHistory(product, processPoint, schedule, 
						actualTimestamp, associateNo, approverNo,deviceId);
				productHistory.setProcessCount(historyCount+1);
				saveProductHistory(productHistory);
			}
	
			getLogger().info(msg.toString(), getTrackingPointLogMsg());
			
		} catch (Exception e) {
			getLogger().error(e, "Failed to create Product History: ", getTrackingPointLogMsg());
		}
		
		return historyCount+1;
	}
	
	private void trackIt(T product, ProcessPoint processPoint, Timestamp actualTimestamp, 
			String associateNo, String approverNo, String deviceId) {
		short trackingPointFlag = processPoint.getTrackingPointFlag();
		short passingCountFlag = processPoint.getPassingCountFlag();
		
		if (trackingPointFlag == 0 && passingCountFlag == 0) return;
		getLogger().info("Tracking: start, ", getTrackingPointLogMsg());
		
		DailyDepartmentSchedule schedule = findSchedule(product, processPoint, actualTimestamp);
		setProductAttributes(product, schedule);
		
		updateHistoryAndCounters(product, processPoint, schedule, actualTimestamp,
				associateNo, approverNo,deviceId);																		// adds history to GAL215TBX or another product history table
		
		if (getProductHistoryDao(product.getProductType()).isMostRecent(product.getProductId(), actualTimestamp)) {
			trackInProcessProduct(product, processPoint);							// updates GAL176TBX or PRODUCT_SEQUENCE_TBX

			if(!isProductShipped(product))
				trackProduct(product, processPoint, actualTimestamp, schedule);		// updates GAL143TBX or another product table
		} else {
			getLogger().warn("Skipping tracking updates for product " + product.getProductId() + 
					", since it was processed at another process point after: " + actualTimestamp);
		}
		if(isPublishToTopic()){
			// Add MSIP message to queue
			ProductTrackedMessageSender.sendMessage(product, processPoint, actualTimestamp, approverNo, associateNo);
		}
	}
	private void trackInProcessProduct(T product, ProcessPoint processPoint) {
		IProductSequenceManager<T> productSequenceManager = getProductSequenceManager();
		
		if (isFactoryExitPP(processPoint)) {							
			productSequenceManager.productFactoryExit(product, processPoint);		// delete from sequence table and re-arrange sequence		
		} else if(processPoint.getTrackingPointFlag() == 1){
			productSequenceManager.addToLine(product, processPoint);				// move product to new line and re-arrange sequence
		} else if(processPoint.getPassingCountFlag() == 1){
			productSequenceManager.moveOnLine(product, processPoint);				// update last passing pp
		}
	}
	
	private boolean isFactoryExitPP(ProcessPoint processPoint) {
	
		return (processPoint.getProcessPointType().equals(ProcessPointType.ProductExit) 
				|| processPoint.getProcessPointType().equals(ProcessPointType.Scrap)
				|| processPoint.getProcessPointType().equals(ProcessPointType.ExceptionalOut));
	}
	
	@SuppressWarnings("unchecked")
	private IProductSequenceManager<T> getProductSequenceManager() {
		
		if (productSequenceManager == null) {
			String clazzName = getProperty().getProductSequenceManager();
			try {
				if(clazzName.contains(Delimiter.DOT)) clazzName = clazzName.substring(clazzName.lastIndexOf(Delimiter.DOT) + 1);
				productSequenceManager = (IProductSequenceManager<T>) ApplicationContextProvider.getBean(clazzName);						
				productSequenceManager.setLogger(getLogger());
			
			} catch (Throwable e) {
				throw new TaskException("Failed to create IProductSequenceManager:" + clazzName);
			}
		}
		return productSequenceManager;
	}

	private void updateHistoryAndCounters(T product, ProcessPoint ppoint, DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo, String approverNo, String deviceId) {
		
		if(getProperty().isCreateNewThreadForTracking()) {
			TrackingServiceExecutor executor = getExecutor();
			if(executor != null) { 			
				executor.invoke(this, product, ppoint, schedule, actualTimestamp, associateNo, approverNo, deviceId);
			}
		} else {
			TrackingServiceWorker<T> worker = new TrackingServiceWorker<T> (this, product, ppoint, schedule, actualTimestamp, associateNo, approverNo,deviceId);
			worker.trackProduct();
		}
	}

	public void processCounters(T product, ProcessPoint processPoint,
			DailyDepartmentSchedule schedule, int firstHistoryCreated, java.util.Date actualTimestamp) {
		if(firstHistoryCreated == 1){
			processCounters(product, processPoint, schedule, actualTimestamp);
		} else {
			getLogger().info("Process Counters skipped, no first history has been processed, ", getTrackingPointLogMsg());
		}
	}
	
	private void processCounters(T product, ProcessPoint processPoint, DailyDepartmentSchedule schedule, java.util.Date actualTimestamp) {
		// the passed in schedule might not be for the current processpoint, in case of Backfill?
		schedule = findSchedule(product, processPoint, actualTimestamp);
		if(schedule == null){
			getLogger().error("Process Counters skipped: missing schedule.", getTrackingPointLogMsg());
			return;
		}
		
		try {
			doProcessCounters(product, processPoint, schedule);
		} catch (Exception e) {
			getLogger().error(e, "Exception process counters.");
		}
	}
	
	@Transactional
	protected void doProcessCounters(T product, ProcessPoint processPoint, DailyDepartmentSchedule schedule){
		if(property.isProcessCount())
			count(getCounterId(schedule, processPoint));
		
		if(property.isProcessCountByModel())
			countByModel(getCounterByModelGroupId(schedule, product, processPoint));
	}
	
	@Transactional
	protected void count(CounterId id) {
		if (id == null)
			return;
		
		try {
			//TODO remove synchronized block and implement using PESSIMISTIC locking when
			//migrating to JPA 2.0
			synchronized(_counterSyncLock) {
				Counter counter = getCounterDao().findByKey(id);
				if (counter == null) {
					getCounterDao().insert(new Counter(id));
				}
				getCounterDao().incrementCounter(id);
			}
		} catch (Throwable e) {
			getLogger().info(e, "Exception thrown in method count.");
		}		
		logFinishCount(id);
	}

	private void logFinishCount(CounterId id) {
		getLogger().info("Finished process Counter, ", getTrackingPointLogMsg(),
				" shift=", id.getShift(), " period=" + id.getPeriod());
	}
	
	@Transactional
	protected void countByModel(CounterByModelGroupId id) {
		if(id == null) 
			return;
		
		try {
			//TODO remove synchronized block and implement using PESSIMISTIC locking when
			//migrating to JPA 2.0
			synchronized(_counterByModelSyncLock) {
				CounterByModelGroup counter = getCounterByModelGroupDao().findByKey(id);
				if (counter == null) {
					getCounterByModelGroupDao().insert(new CounterByModelGroup(id));
				}
				getCounterByModelGroupDao().incrementCounter(id);
			}
		} catch (Throwable e) {
			getLogger().info(e, "Exception thrown in method countByModel.");
		}	
		logFinishCountByModel(id);
	}

	protected List<? extends ProductHistory> findAllProductHistory(T product, ProcessPoint processPoint) {
		ProductHistoryDao<? extends ProductHistory, ?> dao = getProductHistoryDao(product.getProductType());
		List<? extends ProductHistory> productHistoryList = dao.findAllByProductIdAndSpecCodeAndProcessPoint(product.getProductId(),product.getProductSpecCode(), processPoint.getProcessPointId());
		return productHistoryList;
	}
	
	private void logFinishCountByModel(CounterByModelGroupId id) {
		getLogger().info("Finished process CounterByModel, ", getTrackingPointLogMsg(),
				" shift=", id.getShift(), " period=" + id.getPeriod(), 
				" model=", id.getModelCode(), " modelYearCode=", id.getModelYearCode());
	}
	
	protected CounterId getCounterId(DailyDepartmentSchedule schedule,
			ProcessPoint processPoint) {
		return new CounterId(processPoint.getProcessPointId(), schedule.getId().getProductionDate(),
				schedule.getId().getShift(), schedule.getId().getPeriod());
	}
	
	protected CounterByModelGroupId getCounterByModelGroupId(
			DailyDepartmentSchedule schedule, T product,
			ProcessPoint processPoint) {
		return new CounterByModelGroupId(processPoint.getProcessPointId(), 
				schedule.getId().getProductionDate(), schedule.getId().getShift(), 
				schedule.getId().getPeriod(), product.getModelCode(), "");
	}
	
	private void trackProduct(T product, ProcessPoint processPoint, java.util.Date actualTimestamp, DailyDepartmentSchedule schedule) {
		product.setLastPassingProcessPointId(processPoint.getProcessPointId());
		short trackingPointFlag = processPoint.getTrackingPointFlag();
		if (trackingPointFlag == 1) {
			product.setTrackingStatus(processPoint.getLineId());
		}
		updateTrackingAttributes(product, schedule);
		getLogger().info("Tracking status updated for product, ", getTrackingPointLogMsg());
	}
		
	protected boolean isProductOffProcessPoint() {
		return (processPoint.getProcessPointType().equals(ProcessPointType.ProductOff));
	}

	private DailyDepartmentSchedule findSchedule(T product, ProcessPoint processPoint, java.util.Date actualTimestamp) {
		
		GpcsDivision gpcsDivision = getGpcsDivisionDao().findByKey(processPoint.getDivisionId());
		if(gpcsDivision == null){
			getLogger().error("Failed to find GpcsDivision for ", getTrackingPointLogMsg());
			return null;
		}
		
		DailyDepartmentSchedule schedule = getDailyDepartmentScheduleDao().findByActualTime(
				gpcsDivision.getGpcsLineNo() , gpcsDivision.getGpcsProcessLocation(), 
				gpcsDivision.getGpcsPlantCode(), new Timestamp(actualTimestamp.getTime()));
		if(schedule == null){
			getLogger().error("Failed to find schedule, ", " lineNumber:", gpcsDivision.getGpcsLineNo(),
					" processLocation:", gpcsDivision.getGpcsProcessLocation(), " plantCode:", 
					gpcsDivision.getGpcsPlantCode(), " currentDate:" + actualTimestamp,
					" currentTime:" + new Time(actualTimestamp.getTime()));
		}
		
		return schedule;
	}
	
	protected ProductHistory createProductHistory(T product, ProcessPoint processPoint, DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo, String approverNo){

		ProductHistory productHistory = ProductTypeUtil.createProductHistory(product.getProductId(), processPoint.getProcessPointId(), product.getProductType());
		productHistory.setAssociateNo(associateNo);
		productHistory.setApproverNo(approverNo);
		productHistory.setActualTimestamp(checkActualTimestamp(actualTimestamp));
		productHistory.setCreateTimestamp(productHistory.getActualTimestamp());
		
		return productHistory;
	}
		
	protected ProductHistory createProductHistory(T product, ProcessPoint processPoint, DailyDepartmentSchedule schedule,
			Timestamp actualTimestamp, String associateNo, String approverNo, String deviceId){

		ProductHistory productHistory = ProductTypeUtil.createProductHistory(product.getProductId(), processPoint.getProcessPointId(), product.getProductType());
		productHistory.setAssociateNo(associateNo);
		productHistory.setApproverNo(approverNo);
		productHistory.setDeviceId(deviceId);
		productHistory.setActualTimestamp(checkActualTimestamp(actualTimestamp));
		productHistory.setCreateTimestamp(productHistory.getActualTimestamp());
		productHistory.setProductionDate(schedule != null ?schedule.getId().getProductionDate(): new java.sql.Date(new java.util.Date().getTime()));
		
		return productHistory;
	}
	public DailyDepartmentScheduleDao getDailyDepartmentScheduleDao() {
		if(dailyDepartmentScheduleDao == null){
			dailyDepartmentScheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		}
		return dailyDepartmentScheduleDao;
	}

	protected void setProductAttributes(T product,	DailyDepartmentSchedule schedule) {}
	
	private TrackingServiceExecutor getExecutor() {
		return (TrackingServiceExecutor)ApplicationContextProvider.getBean("TrackingServiceExecutor");
	}
	
	public TrackingPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(TrackingPropertyBean.class, processPointId);
		
		return property;
	}
	
	private void createTrackingPointMsg(T product, String processPointId) {
		trackingPointLogMsg = new StringBuilder();
		trackingPointLogMsg.append("productId=").append(product == null? "null" : product.getProductId());
		trackingPointLogMsg.append(", processPoint=").append(processPointId);
	}
	
	public String getTrackingPointLogMsg() {
		return trackingPointLogMsg.toString();
	}
	
	public CounterDao getCounterDao() {
		if(counterDao == null)
			counterDao = ServiceFactory.getDao(CounterDao.class);
		
		return counterDao;
	}
	
	public CounterByModelGroupDao getCounterByModelGroupDao() {
		if(counterByModelGroupDao == null)
			counterByModelGroupDao = ServiceFactory.getDao(CounterByModelGroupDao.class);
		return counterByModelGroupDao;
	}
	
	public ProductHistoryDao<? extends ProductHistory, ?> getProductHistoryDao(ProductType productType) {
		return ProductTypeUtil.getProductHistoryDao(productType.toString());
	}
	
	public Logger getLogger() {
		if(logger == null)
			logger = ServiceUtil.getLogger(processPointId);
		
		return logger;
	}
	
	private void resetLogger() {
		logger = null;
	}
	
	public boolean isPublishToTopic(){
		return PropertyService.getPropertyBoolean(processPointId,"PUBLISH_TO_TOPIC",false);
	}
	
	private void updateTrackingMicroservice(String productId,String processPointId, String url) {
		try {
		
		JsonObject jsonObject1 = new JsonObject();
		jsonObject1.addProperty("productId", productId);
		jsonObject1.addProperty("stationId", processPointId);
		jsonObject1.addProperty("timeStamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
		jsonObject1.addProperty("typeId", 2);
		
		HttpClient.post(url, jsonObject1.toString(), HttpURLConnection.HTTP_OK);
		
		getLogger().info("completed - VIN - ProcessPointId : ", productId +" - "+processPointId);
		} catch (Exception e) {
			getLogger().error("Error sending - VIN - ProcessPointId : ", productId +" - "+processPointId);
			getLogger().error(e.getMessage());
		}
	}
}
