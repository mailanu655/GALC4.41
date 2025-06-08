package com.honda.galc.client.dc.mvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.view.IDataCollectionWidget;
import com.honda.galc.client.dc.view.OperationView;
import com.honda.galc.client.product.process.ProcessModel;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.constant.OperationType;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCOpEfficiencyHistoryDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.MCOpEfficiencyHistory;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.enumtype.OperationEfficiencyStatus;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.OpEfficiencyUtil;

/**
 * 
 * <h3>DataCollectionModel Class description</h3>
 * <p> DataCollectionModel description </p>
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
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public class DataCollectionModel extends ProcessModel {
	
	private volatile ConcurrentHashMap<MCOperationRevision, OperationView> operationViewMap = new ConcurrentHashMap<MCOperationRevision, OperationView>();
	private volatile ConcurrentHashMap<MCOperationRevision, IDataCollectionWidget<OperationProcessor>> dataCollectionViewMap = new ConcurrentHashMap<MCOperationRevision, IDataCollectionWidget<OperationProcessor>>();
	
	private volatile ConcurrentHashMap<String, OperationProcessor> opProcessorsMap = new ConcurrentHashMap<String, OperationProcessor>();
	
	// installed part
	private volatile ConcurrentHashMap<String, List<ProductBuildResult>> productBuildResults = new ConcurrentHashMap<String, List<ProductBuildResult>>();
	private volatile ConcurrentHashMap<String, InstalledPart> installedPartsMap = new ConcurrentHashMap<String, InstalledPart>();
	private volatile ConcurrentHashMap<String, Boolean> skippedPartsMap = new ConcurrentHashMap<String, Boolean>();

	// measurements
	private volatile ConcurrentHashMap<String, Integer> measIndexMap = new ConcurrentHashMap<String, Integer>();
	private volatile ConcurrentHashMap<String, ArrayList<Integer>> skippedMeasurementsMap = new ConcurrentHashMap<String, ArrayList<Integer>>();
	private volatile ConcurrentHashMap<MeasurementId, Integer> badMeasurementAttemptsMap = new ConcurrentHashMap<MeasurementId, Integer>();
	
	//Operation Efficiency History
	private MCOpEfficiencyHistory operationEfficiencyHistory;
	//Daily Department Schedule Map
	private volatile ConcurrentHashMap<String, List<DailyDepartmentSchedule>> dailyDeptNonWorkSchedulesMap = new ConcurrentHashMap<String, List<DailyDepartmentSchedule>>();
	private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	public DataCollectionModel() {}
		
	/**
	 * Checks if the data collection is completed.
	 *
	 * @return true, if the data collection is completed
	 */
	public boolean isDataCollectionComplete() {
		for(MCOperationRevision operation: getOperations()){
			if(!isOperationComplete(operation.getId().getOperationName())){
				return false;
			}
		}
		return true;
	}
	
	public void buildMeasurementIndexMap() {
		for(MCOperationRevision operation: getOperations()) {
			measIndexMap.put(operation.getId().getOperationName(), new Integer(0));
		}
	}
	
	public List<ProductBuildResult> getBuildResultList(String operationName){
		return productBuildResults.get(operationName);
	}

	public boolean hasUncollectedMeasurements(MCOperationRevision operation) {
		if(operation == null || operation.getId().getOperationName() == null) return false;
		return (getGoodMeasurementsCount(operation.getId().getOperationName()) < operation.getSelectedPart().getMeasurements().size());
	}
	
	public boolean hasSkippedMeasurements(MCOperationRevision operation) {
		String operationName = operation.getId().getOperationName();
		int goodMeasurementCount = getGoodMeasurementsCount(operationName);
		if (hasMeasurements(operation) && hasUncollectedMeasurements(operation)) {
			if (goodMeasurementCount > 0) {
				return true;
			}
			if (goodMeasurementCount == 0 && hasSkippedMeasurements(operationName)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasSkippedMeasurements(String operationName) {
		if (getSkippedMeasurementsMap().containsKey(operationName)) {
			ArrayList<Integer> skippedMeasIndexes = getSkippedMeasurementsMap().get(operationName);
			if (skippedMeasIndexes != null && skippedMeasIndexes.size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public int getGoodMeasurementsCount(String operationName) {
		Set<Integer> countSet = new HashSet<Integer>();
		InstalledPart installedPart = getInstalledPartsMap().get(operationName);
		if (installedPart != null) {
			List<Measurement> measurements = installedPart.getMeasurements();
			if (measurements != null ) {
				for (Measurement meas: measurements) {
					if (meas.getMeasurementStatus().equals(MeasurementStatus.OK)) {
						countSet.add(meas.getId().getMeasurementSequenceNumber());
					}
				}
			}
		}
		return countSet.size();
	}

	public MCOperationMeasurement getCurrentMeasurement() {
		return getCurrentOperation().getSelectedPart().getMeasurements().get(getCurrentMeasurementIndex());
	}

	public MCOperationMeasurement getCurrentMeasurement(int measIndex) {
		return getCurrentOperation().getSelectedPart().getMeasurements().get(measIndex);
	}

	public void addInstalledPart(String partSN) {
		ProductBuildResult buildResult = ProductTypeUtil.createBuildResult(
				getProductModel().getProductType(),
				getProductModel().getProduct().getProductId(),
				partSN);
		buildResult.setAssociateNo(getProductModel().getApplicationContext().getUserId());
		buildResult.setInstalledPartStatus(InstalledPartStatus.OK);
		getCurrentProductBuildList().add(buildResult);
	}
	
	public List<ProductBuildResult> getCurrentProductBuildList() {
		if(!productBuildResults.containsKey(getCurrentOperationName()))
			productBuildResults.put(getCurrentOperationName(), new ArrayList<ProductBuildResult>());
		return productBuildResults.get(getCurrentOperationName());
	}
	
	public ConcurrentHashMap<String, Integer> getMeasIndexMap() {
		if (measIndexMap == null) {
			measIndexMap = new ConcurrentHashMap<String, Integer>();
			buildMeasurementIndexMap();
		}
		return measIndexMap;
	}

	public void setMeasIndexMap(ConcurrentHashMap<String, Integer> measIndexMap) {
		this.measIndexMap = measIndexMap;
	}
	
	public int getMeasurementCount(MCOperationRevision operation) {
		if (operation.getSelectedPart() != null && operation.getSelectedPart().getMeasurements() != null) {
			return operation.getSelectedPart().getMeasurements().size();
		} else {
			return 0;
		}
	}

	public int getCurrentMeasurementIndex() {
		return getMeasIndexMap().get(getCurrentOperationName());
	}

	public void setCurrentMeasurementIndex(int currentMeasurementIndex) {
		getMeasIndexMap().put(getCurrentOperationName(), currentMeasurementIndex);
	}
	
	public void incrementMeasurementIndex() {
		int	currIndex = getMeasIndexMap().get(getCurrentOperationName());
		setCurrentMeasurementIndex(++currIndex);
	}
	
	public void decrementMeasurementIndex() {
		int	currIndex = getMeasIndexMap().get(getCurrentOperationName());
		setCurrentMeasurementIndex(--currIndex);
	}
	
	public ConcurrentHashMap<MCOperationRevision, OperationView> getOperationViewMap() {
		return operationViewMap;
	}

	public void setOperationViewMap(ConcurrentHashMap<MCOperationRevision, OperationView> operationViewMap) {
		this.operationViewMap = operationViewMap;
	}
	
	public ConcurrentHashMap<MCOperationRevision, IDataCollectionWidget<OperationProcessor>> getDataCollectionViewMap() {
		return dataCollectionViewMap;
	}

	public void setDataCollectionViewMap(ConcurrentHashMap<MCOperationRevision, IDataCollectionWidget<OperationProcessor>> dataCollectionViewMap) {
		this.dataCollectionViewMap = dataCollectionViewMap;
	}
	
	public ConcurrentHashMap<String, InstalledPart> getInstalledPartsMap() {
		return installedPartsMap;
	}

	public void setInstalledPartsMap(ConcurrentHashMap<String, InstalledPart> installedPartsMap) {
		this.installedPartsMap = installedPartsMap;
	}
	
	public ConcurrentHashMap<String, OperationProcessor> getOpProcessors() {
		return opProcessorsMap;
	}

	public void setOpProcessors(ConcurrentHashMap<String, OperationProcessor> opProcessorMap) {
		this.opProcessorsMap = opProcessorMap;
	}

	public String getDeviceId() {
		return hasUncollectedMeasurements(getCurrentOperation()) ? getCurrentOperation().getSelectedPart().getDeviceId(getCurrentMeasurementIndex()) : "";
	}
	
	public String getDeviceMsg() {
		return hasUncollectedMeasurements(getCurrentOperation()) ? getCurrentOperation().getSelectedPart().getDeviceMsg(getCurrentMeasurementIndex()) : "";
	}

	public ConcurrentHashMap<String, ArrayList<Integer>> getSkippedMeasurementsMap() {
		return skippedMeasurementsMap;
	}

	public void setSkippedMeasurementsMap(ConcurrentHashMap<String, ArrayList<Integer>> skippedMeasurementsMap) {
		this.skippedMeasurementsMap = skippedMeasurementsMap;
	}

	public ConcurrentHashMap<MeasurementId, Integer> getBadMeasurementAttemptsMap() {
		return badMeasurementAttemptsMap;
	}

	public void setBadMeasurementAttemptsMap(ConcurrentHashMap<MeasurementId, Integer> badMeasurementAttemptsMap) {
		this.badMeasurementAttemptsMap = badMeasurementAttemptsMap;
	}
	
	public static boolean hasScanPart(MCOperationRevision operation) {
		if (operation == null) return false;
		
		return (operation.getType().equals(OperationType.GALC_SCAN) || 
				operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS) ||
				operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL));
	}

	public static boolean hasManufacturingPart(MCOperationRevision operation) {
		for (MCOperationPartRevision part : operation.getParts()) {
			if (part.getPartType() == PartType.MFG) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isSkippedPart(String partname) {
		Boolean result =  skippedPartsMap.get(partname);
		return (result == null ? false : result);
	}
	
	public void markSkippedPart(String partname, boolean value) {
		skippedPartsMap.put(partname,value);
	}
	
	public boolean isComplete(MCOperationRevision operation) {
		String operationName = operation.getId().getOperationName();

		if (operation.getSelectedPart() != null && DataCollectionModel.hasMeasurements(operation)) {
			int goodCount = getGoodMeasurementsCount(operationName);
			int expectedCount = operation.getSelectedPart().getMeasurementCount();
			if (goodCount != expectedCount) {
				return false;
			}
		}

		return isOperationComplete(operationName);
	}
	
	public boolean isPending(MCOperationRevision operation) {
		String operationName = operation.getId().getOperationName();
		return isOperationPending(operationName);
	}
	
	public boolean isProductComplete() {
		for(MCOperationRevision operation: getOperations()){
			if(!isComplete(operation)){
				return false;
			}
		}
		return true;
	}
	
	public MCOpEfficiencyHistory getOperationEfficiencyHistory() {
		return operationEfficiencyHistory;
	}

	public void setOperationEfficiencyHistory(MCOpEfficiencyHistory operationEfficiencyHistory) {
		this.operationEfficiencyHistory = operationEfficiencyHistory;
	}
	
	public void setOpEfficiencyStatus(OperationEfficiencyStatus opEfficiencyStatus) {
		if(this.operationEfficiencyHistory!=null) {
			this.operationEfficiencyHistory.setStatus(opEfficiencyStatus);
		}
	}
	
	public void setOpEfficiencyActualTime() {
		if(this.operationEfficiencyHistory!=null) {
			operationEfficiencyHistory.setEndTimestamp(new Date(System.currentTimeMillis()));
			operationEfficiencyHistory.setAssociateNo(getProductModel().getApplicationContext().getUserId());
			long actualTime = calculateActualTime(operationEfficiencyHistory.getStartTimestamp(), 
					operationEfficiencyHistory.getEndTimestamp());
			operationEfficiencyHistory.setActualTime(actualTime);
		}
	}
	
	public void resetOpEfficiency() {
		this.operationEfficiencyHistory = null;
	}
	
	public void resetDailyDeptNonWorkSchedules() {
		this.operationEfficiencyHistory = null;
	}
	
	public long calculateActualTime(Date startTimestamp, Date endTimestamp) {
		//Get the list of non work daily department schedule
		List<DailyDepartmentSchedule> nonWorkSchedule = new ArrayList<DailyDepartmentSchedule>();
		if(OpEfficiencyUtil.compareYearMonthDate(startTimestamp, endTimestamp)) {
			nonWorkSchedule.addAll(getNonWorkSchedule(
							new java.sql.Date(startTimestamp.getTime())));
		}
		else {
			//If Unit Start and End Date is different, then get non work schedule for all dates
			List<java.sql.Date> dates = OpEfficiencyUtil.getAllDates(startTimestamp, endTimestamp);
			for(java.sql.Date date: dates) {
				nonWorkSchedule.addAll(getNonWorkSchedule(date));
			}
		}
		
		//Get total non work time
		long nonWorkTime = OpEfficiencyUtil.calculateNonWorkTime(nonWorkSchedule, 
				startTimestamp, endTimestamp);
		
		//Calculate Total Time
		long totalTime = TimeUnit.MILLISECONDS.toSeconds(endTimestamp.getTime() - startTimestamp.getTime());
		
		//Return actual time
		return (totalTime - nonWorkTime);
	}
	
	public List<DailyDepartmentSchedule> getNonWorkSchedule(java.sql.Date productionDate) {
		String productionDateKey = DATE_FORMATTER.format(productionDate);
		if(!this.dailyDeptNonWorkSchedulesMap.containsKey(productionDateKey)) {
			List<DailyDepartmentSchedule> dailyDeptNonWorkSchedule = new ArrayList<DailyDepartmentSchedule>();
			//Fetch daily department Non Work schedule
			dailyDeptNonWorkSchedule = ServiceFactory.getDao(DailyDepartmentScheduleDao.class)
					.findAllByDivisionAndPlan(getProductModel().getProcessPoint().getDivisionId(), "N", productionDate);
			//Setting Non Work Schedule in Map
			dailyDeptNonWorkSchedulesMap.put(productionDateKey, dailyDeptNonWorkSchedule);
		}
		return dailyDeptNonWorkSchedulesMap.get(productionDateKey);
	}
	
	public static boolean hasMeasurements(MCOperationRevision operation) {
		if (operation == null) return false;
		
		boolean hasMeasurements = operation.getType().equals(OperationType.GALC_MEAS)||
				operation.getType().equals(OperationType.GALC_MEAS_MANUAL)||
				operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS)||
				operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL);
		return hasMeasurements;
	} 
	
	public static boolean hasManualMeasurement(MCOperationRevision operation) {
		if (operation == null) return false;
		
		return (operation.getType().equals(OperationType.GALC_MEAS_MANUAL) ||
				operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL));
	}
	
	@Override
	public void reset(){
		super.reset();
		operationViewMap.clear();
		dataCollectionViewMap.clear();
		opProcessorsMap.clear();
		skippedPartsMap.clear();
		installedPartsMap.clear();
		skippedMeasurementsMap.clear();
		badMeasurementAttemptsMap.clear();
		measIndexMap = null;
		productBuildResults.clear();
	}
	
	public static boolean isMeasOnlyOperation(MCOperationRevision operation) 	{ 	   
    if (operation == null) 
    	return false;
    return (operation.getType().equals(OperationType.GALC_MEAS) ||
			operation.getType().equals(OperationType.GALC_MEAS_MANUAL));
      
	}
	
	public UnitNavigatorEvent createUnitNavigatorUnit(
			int index,UnitNavigatorEventType eventType) {
		MCOperationRevision operation = getOperations().get(index);
		int operationTime = getOpsTimeMap().get(operation.getId().getOperationName()
				.trim());
		int totalTimeForCompltedUnits = 0;
		for(String key : getCompletedOpsMap().keySet()){
			totalTimeForCompltedUnits += getOpsTimeMap().get(key.trim());
		}
		UnitNavigatorEvent event = new UnitNavigatorEvent(eventType, index,
				operationTime, totalTimeForCompltedUnits);
		event.setOperation(operation);

		return event;
	}
	
	//Unit Progress Widget Enhancements - Add Summery Record
	public void saveSummaryEffHistoryRecord(){
		
		Integer sumunitTotalTime=0;
		//Calculate Unit Total Time
		for(Integer unitTotalTime: this.getOpsTimeMap().values()){
			sumunitTotalTime += unitTotalTime;
		}
		
		ServiceFactory.getDao(MCOpEfficiencyHistoryDao.class).
							saveSummaryEffHistory(this.getProductModel().getProductId(),
									this.getProductModel().getApplicationContext().getHostName(),
										this.getProductModel().getApplicationContext().getProcessPointId(),
										this.getProductModel().getApplicationContext().getUserId(), sumunitTotalTime);
	}
	
	public boolean isSpecialOperation(MCOperationRevision operation) {
		 return (getSpecialOps()!= null 
				 && !getSpecialOps().isEmpty() && operation != null 
				 && getSpecialOps().get(operation.getId().getOperationName().trim()));
		
	}
}

