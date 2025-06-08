package com.honda.galc.service.qics;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dto.qi.QiCreateDefectDto;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Site;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.QiHeadlessHelper;
import com.honda.galc.service.utils.ServiceUtil;

/**
 * 
 * <h3>QicsBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Base class for Qics updaters </p>
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
 * Apr 4, 2011
 *
 * @param <T>
 */

public abstract class QicsUpdaterBase<T extends BaseProduct> implements QicsUpdater<T> {

	@Autowired
	DailyDepartmentScheduleDao scheduleDao;
	@Autowired
	GpcsDivisionDao gpcsDivisionDao;
	@Autowired
	ProcessPointDao processPointDao;
	@Autowired
	DefectResultDao defectResultDao;
	@Autowired
	DefectDescriptionDao defectDescriptionDao;
	@Autowired
	InstalledPartDao installedPartDao;
	@Autowired
	MeasurementDao measurementDao;
	
	protected String productId;
	protected String processPointId;
	protected Logger logger;
	
	protected HeadLessPropertyBean property;
	protected DefectDescription description;
	protected T product;
	protected static long start;
	
	protected abstract T findProductById(String productId);
	protected abstract List<DefectResult> createDefect(String processPointId, ProductBuildResult result);
	protected QiHeadlessHelper qiHelper;
	
	
	public boolean update(String processPointId, List<? extends ProductBuildResult> results) {
		start = System.currentTimeMillis();
		try {
			resetLogger();
			setProcessPointId(processPointId);
			logPerformance("after - resetLogger:");
			if(results == null || results.size() == 0){
				getLogger().info("Product build results is empty, skip Qics update.");
				return true;
			}

			setProdcutId(getProductId(results));
			logPerformance("after - setProductId");
			doUpdate(results);
			return true;
		} catch (Throwable t) {
			getLogger().error(t,"Exception to update Qics at " + processPointId);
			return false;
		}

	}
	
	protected String getProductId(List<? extends ProductBuildResult> results) {
		String productId = null;
		
		if(results != null && results.size() > 0)
			productId = results.get(0).getProductId();
		
		if(StringUtils.isEmpty(productId))
			throw new TaskException("Product Id is empty.", this.getClass().getSimpleName());
		
		return StringUtils.trim(productId);
	}

	private void doUpdate(List<? extends ProductBuildResult> buildResults) {
		getLogger().info("start update Qics:", processPointId, " results:", 
				System.getProperty("line.separator"),ProductBuildResult.toLogString(buildResults));
		property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);
		qiHelper = new QiHeadlessHelper(property);

		product = findProductById(getProductId());
		//defectResults will have defects created for NG build results
		List<DefectResult> defectResults = getDefectResult(processPointId, buildResults);
		logPerformance("after - getDefects:");
		DefectStatus exceptionalOut = isExceptionalOut(processPointId, buildResults);
		if(defectResults.size() == 0 && exceptionalOut == null && !property.isInlineRepair()) {
			getLogger().info("No defect for product:", getProductId(), " at process point:", processPointId );
			getLogger().info("finish update Qics.");
			return;
		}
		
		logPerformance("after - getExceptionOut:");
		if(product == null){
			throw new TaskException("Qics product:" + productId + " does not exist!");
		}

		if(exceptionalOut != null){
			processExceptionOut(exceptionalOut);
			return;
		}
		logPerformance("after - process Exception Out:");
		
		if(getProperty().isUseQicsService()) {
			List<DefectResult> existing = null;
			if(property.isInlineRepair())  {
				//existing logic: existing will have existing defects that are fixed and not fixed
				existing = inlineRepair(buildResults);
				logPerformance("after - inline repair:");
			}
			processDefectResults(defectResults, existing);
			logPerformance("after - process Defect Results:");
			getLogger().info("finish update Qics.");
		}

		ExecutorService myExecutor = null;
		int nThreads = getProperty().getNumQicsThreads();
		boolean isProfileMode = getProperty().isNaqicsProfileMode();
		
		if(nThreads > 0)  {
			myExecutor = NAQicsThreadPoolExecutor.getFixedPool(nThreads);  //create static thread pool
		}
		else {
			NAQicsThreadPoolExecutor.shutdown();
			if(nThreads == 0)  {
				myExecutor = Executors.newSingleThreadExecutor();		//create single local thread to terminate after execution
			}
		}
		if(qiHelper.isSendToNAQics()) { //isUseQicsService=false, send to NAQICS
			//existingRepaired will represent all build results that have been repaired 
			//defectResults are NG results
			final List<? extends ProductBuildResult> finalBuildResults = buildResults;
			final List<DefectResult> finalDefectResults = defectResults;
			final List<QiRepairDefectDto> existingRepaired = buildRepairList(finalBuildResults);
			logPerformance("after - inline repair:");
			if(nThreads >= 0 && myExecutor != null)  {  //if Qics threads are enabled
				try {
					myExecutor.execute
						(
						new Runnable() {
							@Override
							public void run()  {
								sendQicsDefects(finalDefectResults, existingRepaired, qiHelper.isNAQicsSecondary());
							}
						}
						);
				} catch (Exception ex) {
					getLogger().error(ex, "Exception executing NAQics thread");
				}
				if(isProfileMode && NAQicsThreadPoolExecutor.isActive())  {
					getLogger().info(NAQicsThreadPoolExecutor.getPoolStats());
				}
			}
			else  {  //do not create threads
				sendQicsDefects(finalDefectResults, existingRepaired, qiHelper.isNAQicsSecondary());
			}
		}

		return;

	}

	private void sendQicsDefects(final List<DefectResult> defectResults, final List<QiRepairDefectDto> existingRepaired, boolean isSecondary) {
		processQicsDefects(defectResults, existingRepaired, isSecondary);    	
		logPerformance("NAQics - after process Defect Results:");
		getLogger().info("NAQics - finish update Qics.");
    }
	
	private static final class NAQicsThreadPoolExecutor  {
		private static ThreadPoolExecutor _fixedInstance = null;
		private NAQicsThreadPoolExecutor() {}
		public static ExecutorService getFixedPool(int nThreads)  {
			if(_fixedInstance == null || _fixedInstance.isShutdown())  {
				_fixedInstance = 
						new ThreadPoolExecutor(
							nThreads, //core pool size
							nThreads, //max pool size
                            0L,	//idling time before terminating
                            TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>() //unbounded queue
                            );
			}
			else if(nThreads > 0 && nThreads != _fixedInstance.getCorePoolSize())  {
				_fixedInstance.setCorePoolSize(nThreads);
				_fixedInstance.setMaximumPoolSize(nThreads);
			}
			return _fixedInstance;
		}
		
		public static void shutdown()  {
			if( _fixedInstance != null && !_fixedInstance.isShutdown())  {
				_fixedInstance.shutdown();
			}
		}
		
		public static boolean isActive()  {
			return _fixedInstance != null && !_fixedInstance.isShutdown();
		}
		
		public static String getPoolStats()  {
			if(_fixedInstance == null || _fixedInstance.isShutdown())  return "";			
			StringBuilder sb = new StringBuilder();
			sb.append("\nNAQics cached thread pool stats:")
			.append("\nActive threads:").append(_fixedInstance.getActiveCount())
			.append("\nCurrentPoolSize:").append(_fixedInstance.getPoolSize())
			.append("\nCorePoolSize:").append(_fixedInstance.getCorePoolSize())
			.append("\nMaxPoolSize:").append(_fixedInstance.getMaximumPoolSize())
			.append("\nLargest simultaneous pool size:").append(_fixedInstance.getLargestPoolSize())
			.append("\nCompleted:").append(_fixedInstance.getCompletedTaskCount())
			.append("\nTotal tasks:").append(_fixedInstance.getTaskCount());
			return sb.toString();
		}
	}
	
	private void logPerformance(String tag) {
		if(getProperty().isLogPerformance()){
			getLogger().info("performance - ", tag + (start - System.currentTimeMillis()) );
			start = System.currentTimeMillis();
		}
		
	}
	
	
	public Logger getLogger() {

		if(logger == null)
			logger = ServiceUtil.getLogger(processPointId);
		
		return logger;
	}
	
	private void resetLogger() {
		logger = null;
		
	}
	
	private void processDefectResults(List<DefectResult> defectResults, List<DefectResult> existing) {
		if(property.isInlineRepair() && !property.isInlineRepairCreateDefect()){
			if(defectResults.size() > 0){
				getLogger().info("Inline repair qick fix, not creating defect");
				defectResults.clear();
			}
		}
			
		if(defectResults != null && defectResults.size() > 0){
			product.setDefectStatusValue(DefectStatus.OUTSTANDING.getId());
			getLogger().info("set product:", getProductId(), " defect status:", DefectStatus.OUTSTANDING.toString());
		}
		
		if(existing != null && existing.size() > 0 )
			defectResults.addAll(existing);
		
		defectResultDao.saveAllDefectResults(product, product.getDefectStatus(), processPointId, defectResults, findSchedule(processPointId), null, false, true);
		
		getLogger().info("Qics save defects:", getDefectResultsStr(defectResults));
	}
	
	private void processQicsDefects(final List<DefectResult> defectResults, final List<QiRepairDefectDto> existingRepaired, boolean isNAQicsSecondary) {
		
		List<QiCreateDefectDto> qiDefectList = null;
		
		if(property.isInlineRepair() && !property.isInlineRepairCreateDefect()){
			if(defectResults.size() > 0)  {
				getLogger().info("Inline repair qick fix, not creating defect");
				defectResults.clear();
			}
		}
			
		if(defectResults != null && defectResults.size() > 0){
			product.setDefectStatusValue(DefectStatus.OUTSTANDING.getId());
			getLogger().info("set product:", getProductId(), " defect status:", DefectStatus.OUTSTANDING.toString());
			qiDefectList = createQiCreateDefectList(defectResults);
		}
		
		if(qiDefectList != null && !qiDefectList.isEmpty())  {
			try {
				ServiceFactory.getService(QiHeadlessDefectService.class).createDefects(qiDefectList);
			} catch (Exception ex) {
				Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
			}
		}
		if(existingRepaired != null && !existingRepaired.isEmpty())  {
			try {
				//The product defect status is being updated below, so we dont want Qics to update the product defect status.
				ServiceFactory.getService(QiHeadlessDefectService.class).repairDefects(existingRepaired);
			} catch (Exception ex) {
				Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
			}		
		}

    	//  call headless qics service to get the overall defect status of the product
		DefectStatus productDefectStatus = DefectStatus.OUTSTANDING;
		DefectStatus qiDefectStatus = ServiceFactory.getService(QiHeadlessDefectService.class).getDefectStatus(product.getProductId());
		if(qiDefectStatus.equals(DefectStatus.FIXED))  {
			productDefectStatus = DefectStatus.REPAIRED;
		}
		if(!isNAQicsSecondary)  {
			defectResultDao.saveLotControlResults(product, productDefectStatus, processPointId, defectResults, findSchedule(processPointId), null, false, true);
		}

		getLogger().info("Qics save defects:", getDefectResultsStr(defectResults));
	}
	
	private List<QiCreateDefectDto> createQiCreateDefectList(List<DefectResult> defectResults)  {
		if(defectResults == null || defectResults.isEmpty())  return null;
		List<QiCreateDefectDto> dtoList = new ArrayList<QiCreateDefectDto>();
		for(DefectResult defect : defectResults)  {
			if(!defect.isQicsDefect() && property.isQicsLcRuleRequired())  {  //does not require Qics defect
				continue;
			}
			QiCreateDefectDto dto = new QiCreateDefectDto();
			if(StringUtils.isBlank(defect.getAssociateNo()))  {
				dto.setAssociateId("HeadLess");
			}
			else  {
				dto.setAssociateId(defect.getAssociateNo());
			}
			dto.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
			dto.setEntryDepartment(defect.getEntryDept());
			dto.setEntrySite(getSiteName());
			if(defect.getErrorCode() == null || defect.getErrorCode().trim().equalsIgnoreCase("false"))  {
				dto.setExternalDefectCode("0");
			}
			else  {
				dto.setExternalDefectCode(defect.getErrorCode());
			}
			dto.setExternalPartCode(defect.getId().getInspectionPartName());
			dto.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
			dto.setExternalSystemKey(defect.getDefectRefId());
			dto.setImageName(defect.getImageName());
			dto.setOriginalDefectStatus((short)DefectStatus.NOT_REPAIRED.getId());
			ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class).findById(defect.getApplicationId());
			dto.setPlantCode(pp.getPlantName());
			dto.setProcessPointId(defect.getApplicationId());
			dto.setProductId(defect.getProductId());
			dto.setProductType(property.getProductType());
			dto.setWriteupDepartment(defect.getWriteUpDepartment());
			dto.setxAxis(defect.getPointX());
			dto.setyAxis(defect.getPointY());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	
	private String getSiteName()  {
		String site = "";
		SystemPropertyBean sBean = PropertyService.getSystemPropertyBean();
		if(sBean != null)  {
			site =	sBean.getSiteName();
		}
		else  {
			List<Site> sites = ServiceFactory.getDao(SiteDao.class).findAll();
			if(sites != null && !sites.isEmpty())  {
				site = sites.get(0).getSiteName();
			}
		}
		return site;
	}
	
	private String getDefectResultsStr(List<DefectResult> defectResults) {
		StringBuilder sb = new StringBuilder();
		for(DefectResult result : defectResults){
			if(sb.length() > 0) sb.append(",");
			sb.append(result.toString());
		}
		return sb.toString();
	}
	
	private List<DefectResult> inlineRepair(List<? extends ProductBuildResult> buildResults) {
		List<DefectResult> defects = defectResultDao.findAllDefectsByProductId(getProductId());
		
		//for each build result, repair all existing defects that have the same part name as the build result
		for(ProductBuildResult result : buildResults){
			if(result.getInstalledPartStatus() == InstalledPartStatus.OK){
				repair(defects, result);
			}
		}

		
		if(isAllRepaired(defects)){
			product.setDefectStatusValue(DefectStatus.REPAIRED.getId());
			getLogger().info("product:", getProductId(), " is repaired.");
		}
		
		//this list will contain all existing defects, repaired or not
		return defects;
			
	}
	
	protected List<QiRepairDefectDto> buildRepairList (List<? extends ProductBuildResult> buildResults) {
		if(buildResults == null || buildResults.isEmpty())  return null;
		//get Installed parts that already have a defect, i.e., DEFECT_REF_ID is > 0
		List<InstalledPart> existing = installedPartDao.findAllPartsWithDefect(productId);
		//attach measurements for each installed part
		for(InstalledPart p : existing)  {
			List<Measurement> measList = measurementDao.findAll(productId, p.getPartName());
			p.setMeasurements(measList);
		}
		if(existing == null || existing.isEmpty())  return null;
		List<QiRepairDefectDto> repairList = new ArrayList<QiRepairDefectDto>();
		for(ProductBuildResult result : buildResults){
			if(result.getInstalledPartStatus() == InstalledPartStatus.OK){
				List<QiRepairDefectDto> repairListForBuildResult = getRepairDtoList(existing, result, qiHelper.isNAQicsSecondary());
				if(repairListForBuildResult != null && !repairListForBuildResult.isEmpty())  {
					repairList.addAll(repairListForBuildResult);
				}
			}
		}

		if(isAllRepairedParts(existing)){
			product.setDefectStatusValue(DefectStatus.REPAIRED.getId());
			getLogger().info("product:", getProductId(), " is repaired.");
		}
		
		return repairList;
			
	}
	
	
	
	private boolean isAllRepaired(List<DefectResult> defects) {
		if(defects.size() > 0){
			int count = 0;
			for(DefectResult result : defects){
				if(result.getDefectStatus() == DefectStatus.REPAIRED)
					count++;
			}
			
			return defects.size() == count;
		} 
		return false;
	}
	
	private boolean isAllRepairedParts(List<InstalledPart> installedParts) {
		if(installedParts != null && installedParts.size() > 0){
			int count = 0;
			for(InstalledPart result : installedParts){
				if(result.getDefectStatus() != null && (result.getDefectStatus() == DefectStatus.REPAIRED.getId()))  {
					count++;
				}
			}
			
			return installedParts.size() == count;
		} 
		return false;
	}
	
	private void repair(List<DefectResult> defects, ProductBuildResult buildResult) {
		for(DefectResult result : defects){
			if(buildResult.getPartName().equals(result.getId().getInspectionPartName()))
			{	
				result.setDefectStatus(DefectStatus.REPAIRED);
				if (property.isOutstandingFlagChangable()) {
					result.setOutstandingFlag(result.isOutstandingStatus());
				}
				result.setRepairTimestamp(new Timestamp(System.currentTimeMillis()));
				
				if(!buildResult.getAssociateNo().equals(""))
					result.setRepairAssociateNo(buildResult.getAssociateNo());
				else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
					result.setRepairAssociateNo(this.processPointId);
				String updateReason = property.getRepairReason();
				installedPartDao.repairHeadless(buildResult.getProductId(), 
						buildResult.getPartName(), buildResult.getAssociateNo(), updateReason);
				
				getLogger().info("Qics repair product:", result.getId().getProductId(), " partName:", 
						buildResult.getPartName(), "DefectResultId:" + result.getId().getDefectResultId()," repaired.");
			}
		}
	}
	
	private List<QiRepairDefectDto> getRepairDtoList(List<InstalledPart> existing, ProductBuildResult buildResult, boolean isSecondary) {
		List<QiRepairDefectDto> dtoList = new ArrayList<QiRepairDefectDto>();
		for(InstalledPart iPart : existing){
			if(buildResult.getPartName().equals(iPart.getPartName()))
			{	
				QiRepairDefectDto qiRepair = new QiRepairDefectDto();
				qiRepair.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
				qiRepair.setProcessPointId(this.processPointId);
				String associateId = "";
				if(!buildResult.getAssociateNo().equals(""))  {
					associateId = buildResult.getAssociateNo();
				} else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))  {
					associateId = this.processPointId;
				}
				qiRepair.setAssociateId(associateId);
				String updateReason = property.getRepairReason();
				qiRepair.setRepairReason(updateReason);
				
				iPart.setDefectStatus(DefectStatus.REPAIRED.getId());
				iPart.setAssociateNo(associateId);
				iPart.setInstalledPartReason(updateReason);
				
				qiRepair.setExternalSystemKey(iPart.getDefectRefId());
				qiRepair.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
				dtoList.add(qiRepair);
				qiRepair.setFixDuplicates(property.isFixDuplicates());
				
		   		if(iPart.getMeasurements() != null && !iPart.getMeasurements().isEmpty()) {
		   			for(Measurement meas : iPart.getMeasurements())  {
						if(meas.getDefectRefId() > 0)  {
							QiRepairDefectDto qiRepairMeas = new QiRepairDefectDto();
							qiRepairMeas.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
							qiRepairMeas.setProcessPointId(this.processPointId);
							qiRepairMeas.setAssociateId(associateId);
							qiRepairMeas.setRepairReason(updateReason);
							qiRepairMeas.setExternalSystemKey(meas.getDefectRefId());
							qiRepairMeas.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
							dtoList.add(qiRepairMeas);
						}
		   			}
				}
		   		
		   		if(!isSecondary)  {
					installedPartDao.repairHeadless(buildResult.getProductId(), 
							buildResult.getPartName(), buildResult.getAssociateNo(), updateReason);
					
					getLogger().info("Qics repair product:", iPart.getProductId(), " partName:", 
							buildResult.getPartName(), "Defect ref id:" + iPart.getDefectRefId()," repaired.");
		   		}
				
			}
		}
		return dtoList;
	}
	
	private void processExceptionOut(DefectStatus defectStatus) {
		
		product.setDefectStatusValue(defectStatus.getId());
		
		getLogger().info("Exceptional Out product:", getProductId(), " defect status:" + defectStatus);
		
		defectResultDao.saveAllDefectResults(product, defectStatus, processPointId, null, findSchedule(processPointId),null, false, true);
		
	}
	
	
	protected List<DefectResult> getDefectResult(String processPointId, 
			List<? extends ProductBuildResult> buildResults) {
		List<DefectResult> defects = new ArrayList<DefectResult>();
		for(ProductBuildResult result : buildResults){
			if(result.getInstalledPartStatus() == InstalledPartStatus.NG && 
					getDefectStatus(result) == DefectStatus.OUTSTANDING){
				
				List<DefectResult> newDefects = createDefect(processPointId, result);
				if(newDefects.size() > 0)  defects.addAll(newDefects);
			}
		}

		return defects;
	}
	
	protected DefectStatus isExceptionalOut(String processPointId, 
			List<? extends ProductBuildResult> buildResults) {
		for(ProductBuildResult result : buildResults){
			if(isExceptionOut(result)){
				return getDefectStatus(result);
			}
		}

		return null;
	}

	
	private DailyDepartmentSchedule findSchedule(String processPointId) {
		ProcessPoint processPoint = processPointDao.findByKey(processPointId);
		 DailyDepartmentSchedule findSchedule = findSchedule(processPoint);
		return findSchedule;
	}
	
	private DailyDepartmentSchedule findSchedule(ProcessPoint processPoint) {

		GpcsDivision gpcsDivision = gpcsDivisionDao.findByKey(processPoint.getDivisionId());
		if(gpcsDivision == null){
			getLogger().error("Failed to find GpcsDivision for ", processPoint.getProcessPointId());
			return null;
		}

		long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
		DailyDepartmentSchedule schedule = scheduleDao.findByActualTime(
				gpcsDivision.getGpcsLineNo() , gpcsDivision.getGpcsProcessLocation(), 
				gpcsDivision.getGpcsPlantCode(), new Timestamp(currentTimeInMillis));
		if(schedule == null){
			getLogger().error("Failed to find schedule, ", " lineNumber:", gpcsDivision.getGpcsLineNo(),
					" processLocation:", gpcsDivision.getGpcsProcessLocation(), " plantCode:", 
					gpcsDivision.getGpcsPlantCode(), " currentDate:" + new Date(currentTimeInMillis),
					" currentTime:" + new Time(currentTimeInMillis));
		}

		return schedule;

	}
	
	private boolean isExceptionOut(ProductBuildResult result) {
		return getDefectStatus(result) == DefectStatus.SCRAP ||
		       getDefectStatus(result) == DefectStatus.PREHEAT_SCRAP;
	}
	
	protected DefectResultId createDefectResultId(String processPointId, ProductBuildResult result) {
		DefectResultId id = new DefectResultId();
		id.setProductId(result.getProductId());
		id.setInspectionPartName(result.getPartName());
		
		id.setApplicationId(processPointId);
		id.setTwoPartPairPart("");
		id.setTwoPartPairLocation("");
		id.setSecondaryPartName("NONE");
		
		return id;
	}
	
	/**
	 * set properties from defect group if defect group defined
	 * @param defect
	 * @param part 
	 */
	protected void setDefectProperties(DefectResult defect, ProductBuildResult part) {
		String groupName = QicsDefectInfoManager.getInstance().getGroupName(processPointId, 
				defect.getId().getInspectionPartName(),	defect.getId().getInspectionPartLocationName());
		
		description = getDefectDescription(defect, groupName);
		
		//set default properties values
		defect.setImageName("");
		defect.setIqsCategoryName("");
		defect.setIqsItemName("");
		defect.setRegressionCode("");
		defect.setAssociateNo("");
		defect.setOutstandingFlag((short)1);
		
		if(description != null){
			defect.setEngineFiring(description.getEngineFiringFlag());
			//description.getLockMode(); PC: not used, add to DefectResult if needed
			defect.setIqsCategoryName(description.getIqsCategoryName());
			defect.setIqsItemName(description.getIqsItemName());
			defect.setRegressionCode(description.getRegressionCode());
			defect.setResponsibleDept(description.getResponsibleDept());
			defect.setResponsibleLine(description.getResponsibleLine());
			defect.setResponsibleZone(description.getResponsibleZone());
			
		} else if(getProperty().isUseQicsService()) {  //defect properties not needed for NAQICS when isUseQicsService=false
			getLogger().info("Defect description is missing for:", 
					" InspectionPartName:", defect.getId().getInspectionPartName(),
					" InspectionPartLocationName:", defect.getId().getInspectionPartLocationName(),
					" DefectTypeName:",defect.getId().getDefectTypeName(),
					" SecondaryPartName:",defect.getId().getSecondaryPartName(),
					" TwoPartPairPart:", defect.getId().getTwoPartPairPart(),
					" TwoPartPairLocation",defect.getId().getTwoPartPairLocation(),
					" PartGroupName:", groupName);
		}
		
		setResponsibleProperties(defect);
		
		defect.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		defect.setCreateTimestamp(defect.getActualTimestamp());
		setPointXy(part, defect);
		
		setImageName(defect);
		
		defect.setNewDefect(true);
	}
	
	protected void setImageName(DefectResult defect) {
		
		if(!StringUtils.isEmpty(property.getImageName()))
			defect.setImageName(property.getImageName());
		
		if(!StringUtils.isEmpty(getImageNameByModel()))
			defect.setImageName(getImageNameByModel());
	}
	
	protected String getImageNameByModel() {
		 if(product == null) return "";
		 return (property.getImageNames() != null && property.getImageNames().size() > 0 && !StringUtils.isEmpty(property.getImageNames().get(product.getModelCode()))) ?
				 property.getImageNames().get(product.getModelCode()) : ""; 
	}
	private void setResponsibleProperties(DefectResult defect) {
		ProcessPoint processPoint = getProcessPoint();
		defect.setEntryDept(processPoint.getDivisionId());
		defect.setWriteUpDepartment(processPoint.getDivisionName());
		
		//set property attributes
		if(!StringUtils.isEmpty(QicsDefectInfoManager.getInstance().getResponsibleZone(processPointId))){
			defect.setResponsibleZone(QicsDefectInfoManager.getInstance().getResponsibleZone(processPointId));
		}
		
		if(!StringUtils.isEmpty(QicsDefectInfoManager.getInstance().getResponsibleLine(processPointId))){
			defect.setResponsibleLine(QicsDefectInfoManager.getInstance().getResponsibleLine(processPointId));
		}
		
		if(!StringUtils.isEmpty(QicsDefectInfoManager.getInstance().getResponsibleDepartment(processPointId))){
			defect.setResponsibleDept(QicsDefectInfoManager.getInstance().getResponsibleDepartment(processPointId));
		}
		
		//If responsible line and department not defined, take from process point
		if(StringUtils.isEmpty(defect.getResponsibleLine())){
			defect.setResponsibleLine(processPoint.getLineId());
		} 
		
		if(StringUtils.isEmpty(defect.getResponsibleDept())){
			defect.setResponsibleDept(processPoint.getDivisionId());
		}
	}
	

	private DefectDescription getDefectDescription(DefectResult defect,	String groupName) {
		DefectDescriptionId id = new DefectDescriptionId();
		id.setInspectionPartName(defect.getId().getInspectionPartName());
		id.setInspectionPartLocationName(defect.getId().getInspectionPartLocationName());
		id.setDefectTypeName(defect.getId().getDefectTypeName());
		id.setSecondaryPartName(defect.getId().getSecondaryPartName());
		id.setTwoPartPairPart(defect.getId().getTwoPartPairPart());
		id.setTwoPartPairLocation(defect.getId().getTwoPartPairLocation());
		id.setPartGroupName(groupName);
		
		try {
			return defectDescriptionDao.findByKey(id);
		} catch (Exception e) {
			getLogger().info("failed to get defect description", e.toString() + ":" + e.getCause());
		}
		
		return null;
	}
	
	
	//------------ helper functions ----------------
	protected DefectStatus getDefectStatus(ProductBuildResult result) {

		return (result.getDefectStatus() != null) ? 
				DefectStatus.getType(result.getDefectStatus()) :
					DefectStatus.getType(property.getDefectStatus());
	}
	
	protected String getInspectionPartLocationName(ProductBuildResult result,String name) {
		if(!StringUtils.isEmpty(result.getDefectLocation())) return result.getDefectLocation();
		return QicsDefectInfoManager.getInstance().getDefectLocation(processPointId,result.getPartName(), name);
	}
	
	protected String getDefectTypeName(ProductBuildResult result, String value){
		if(!StringUtils.isEmpty(result.getDefectName())) return result.getDefectName();
		
		return QicsDefectInfoManager.getInstance().getDefectName(processPointId, result.getPartName(), result.getErrorCode(), value);
	}
	
	protected String getDefectTypeName(Measurement measurement) {
		
		return QicsDefectInfoManager.getInstance().getDefectName(processPointId, measurement.getId().getPartName(), measurement.getErrorCode(), 
				InstalledPartStatus.NG.toString());
	}
	
	protected boolean isEngineFiring(List<DefectResult> defectResults) {
		if(defectResults == null || defectResults.size() == 0) return false;
		
		for(DefectResult result: defectResults)
			if(result != null && result.isEngineFiring()) return true;
		
		return false;
	}
	
	protected void setPointXy(ProductBuildResult part, DefectResult defect) {
		defect.setPointX(part.getPointX());
		defect.setPointY(part.getPointY());
	}
	
	protected HeadLessPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);
		return property;
	}
	
	//----------- getters && setters ---------------
	public String getProductId() {
		return productId;
	}
	public void setProdcutId(String prodcutId) {
		this.productId = prodcutId;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	protected ProcessPoint getProcessPoint() {
		return processPointDao.findByKey(processPointId);
	}

}
