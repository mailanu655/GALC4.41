package com.honda.galc.client.qi.base;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.process.AbstractProcessModel;
import com.honda.galc.client.qi.defectentry.DefectEntryCacheUtil;
import com.honda.galc.client.teamleader.qi.stationconfig.AdditionalStationConfigSettings;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.dao.qi.QiDefectDeviceDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiRepairAreaRowDao;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.dao.qi.QiRepairAreaSpaceHistoryDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.ExistingDefectDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.InRepairArea;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectDevice;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceHistory;
import com.honda.galc.entity.qi.QiRepairAreaSpaceHistoryId;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;
/**
 * <h3>QiProcessModel description</h3> <h4>Description</h4>
 * <p>
 * <code>QiProcesModel</code> is model for Defect Entry Screen
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>26/11/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class QiProcessModel extends AbstractProcessModel{
	
	private QiPropertyBean property;

	private DefectEntryCacheUtil defectEntryCacheUtil;
	
	public QiDefectResult defectResult;
	
	public List<QiDefectResult> cachedDefectResultList;
	
	private Map<String, ProcessPoint> processPoints;
	
	protected String currentWorkingProcessPointId = new String("");
	protected String currentWorkingEntryDept = new String("");
	protected ProcessPoint currentWorkingProcessPoint;
	private static final short DEFAULT_PROD_LINE_NO = 1;
	
	public QiProcessModel() {
		super();
		cachedDefectResultList = new ArrayList<QiDefectResult>();
		defectEntryCacheUtil = new DefectEntryCacheUtil();
		processPoints = new HashMap<String, ProcessPoint>();
		//initialize working process point
		currentWorkingProcessPointId = getProcessPointId();
		currentWorkingProcessPoint = getDao(ProcessPointDao.class).findById(currentWorkingProcessPointId);
	}
	
	/**
	 * This method is used to get Application Context
	 * @return
	 */
	public ApplicationContext getApplicationContext() {
		return ApplicationContext.getInstance(); 
	}
	/**
	 * This method is used to get Product Kind
	 * @return
	 */
	public String getProductKind() {
		return StringUtils.trimToEmpty(PropertyService.getPropertyBean(ApplicationPropertyBean.class).getProductKind());
	}
	/**
	 * This method is used to get Product Id
	 * @return
	 */
	public String getProductId() {
		if(getProductModel()!=null)
			return StringUtils.trimToEmpty(getProductModel().getProduct().getProductId());
		return null;
	}
	/**
	 * This method is used to get Application Id
	 * @return
	 */
	public String getApplicationId() {
		return StringUtils.trimToEmpty(getApplicationContext().getApplicationId());
	}
	/**
	 * This method is used to get Process Point Id
	 * @return
	 */
	public String getProcessPointId() {
			return ClientMainFx.getInstance().currentApplicationId;
	}
	/**
	 * This method is used to get Product Spec Code
	 * @return
	 */
	public String getProductSpecCode() {
		if(getProductModel()!=null && null!=getProductModel().getProduct())
			return StringUtils.trimToEmpty(getProductModel().getProduct().getProductSpecCode());
		return StringUtils.EMPTY;
	}
	/**
	 * This method is used to get Production Lot
	 */
	public String getProductionLot() {
		if(null!=getProductModel() && null!=getProductModel().getProduct()){
			return StringUtils.trimToEmpty(getProductModel().getProduct().getProductionLot());
		}
		return StringUtils.EMPTY;
	}
	/**
	 * This method is used to get Production Date
	 * @return
	 */
	public Date getProductionDate() {
		DailyDepartmentSchedule schedule = getSchedule();
		if (schedule == null)
			return null;
		return schedule.getId().getProductionDate();
	}
	/**
	 * This method is used to get Shift
	 * @return
	 */
	public String getShift() {
		DailyDepartmentSchedule schedule = getSchedule();
		if (schedule == null)
			return null;
		return schedule.getId().getShift();
	}
	/**
	 * This method is used to get Associate User Id
	 * @return
	 */
	public String getUserId() {
		return StringUtils.trimToEmpty(getApplicationContext().getUserId().toUpperCase());
	}
	/**
	 * This method is used to get Logger
	 * @return
	 */
	public Logger getLogger() {
		return getApplicationContext().getLogger();
	}
	/**
	 * This method is used to get Daily Department Schedule
	 * @return
	 */
	public DailyDepartmentSchedule getSchedule() {
		return getApplicationContext().getDailyDepartmentScheduleUtil().getCurrentSchedule();
	}
	/**
	 * This method is used to get Terminal Name
	 * @return
	 */
	public String getTerminalName() {
		return StringUtils.trimToEmpty(getApplicationContext().getTerminal().getHostName());
	}
	/**
	 * This method is used to get Entry Site Name
	 * @return
	 */
	public String getEntrySiteName() {
		return StringUtils.trimToEmpty(getApplicationContext().getProcessPoint().getSiteName());
	}
	/**
	 * This method is used to get Entry Plant Name
	 * @return
	 */
	public String getEntryPlantName() {
		return StringUtils.trimToEmpty(getApplicationContext().getProcessPoint().getPlantName());
	}
	
	/**
	 * This method is used to get Entry Plant Prod Line No
	 * @return
	 */
	public short getEntryProdLineNo() {

		String processPointId = getCurrentWorkingProcessPointId();
		if(StringUtils.isBlank(processPointId))  return DEFAULT_PROD_LINE_NO;
        short prodLineNo = DEFAULT_PROD_LINE_NO;
        try {
        	ProcessPoint pp = getDao(ProcessPointDao.class).findById(processPointId);
             if(pp != null) {
                GpcsDivision gpcs = ServiceFactory.getDao(GpcsDivisionDao.class).findByKey(pp.getDivisionId());
		        if(gpcs != null) {
				 prodLineNo = Short.parseShort(gpcs.getGpcsLineNo());
		        }
             }
        }catch(Exception e) {
        	prodLineNo =1;
        	getLogger().error(e, "Exception when getting Production Line No",
					this.getClass().getSimpleName());
        }
		 return prodLineNo;
	}
	/**
	 * This method is used to get Product Type
	 * @return
	 */
	public String getProductType() {
		return StringUtils.trimToEmpty(getProductModel().getProductType());
	}
	public QiPropertyBean getProperty() {
		if(property == null) {
			property= PropertyService.getPropertyBean(QiPropertyBean.class, getProcessPointId());
		}
		return property;
	}
	/**
	 * This method is used to get MTC Model by Product Spec Code
	 */
	public String getMtcModel() {
		if(!getProductModel().getMbpnProductMap().isEmpty()){
			return (String) getMbpnProductMapValue("mtcModel");
		}else if(ProductType.MBPN.equals(getProductModel().getProduct().getProductType())) {
				return getProductModel().getProduct().getModelCode();
		}else if(!StringUtils.isEmpty(StringUtils.trimToEmpty(getProductSpecCode()))) {
			if(getProductSpecCode().length() > 3) {
				return getProductSpecCode().substring(0,4);
			} else {
				return getProductSpecCode();
			}
		}
		return StringUtils.EMPTY;
	}
	/**
	 * This method is used to get main no for the MBPN
	 */
	public String getMainNo() {
		if (!ProductType.MBPN.equals(getProductModel().getProduct().getProductType()))
			return null;
		String mbpnSpec = ((MbpnProduct) getProductModel().getProduct()).getCurrentProductSpecCode();
		Mbpn mbpn = getDao(MbpnDao.class).findByKey(mbpnSpec);
		return mbpn == null ? null : mbpn.getMainNo();
	}
	
	/**
	 * This method is used for Product Tracking
	 * @param productId 
	 * @param trackerTimeStamp 
	 */
	public void trackProduct(String productId, Timestamp trackerTimeStamp, String processPointId) {
		ProductHistory productHistory = createProductHistory(productId, trackerTimeStamp, processPointId);
		try {
			if(!StringUtils.isEmpty(getProductType()))	{
				ServiceFactory.getService(TrackingService.class).track(ProductType.getType(getProductType()), productHistory);
				getLogger().info("Tracking Successfully completed for product " + productId);
			} else {
				getLogger().error("Product Type is unknown for product " + productId);
			}
		} catch (Exception ex) {
			throw new ServiceException("Tracking was not Successful for product " + productId, ex);
		}
	}
	
	/**
	 * This method is used to create Product History
	 * @param productId
	 * @param trackerTimeStamp
	 * @param processPointId
	 * @return
	 */
	public ProductHistory createProductHistory(String productId, Timestamp trackerTimeStamp, String processPointId) {
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(productId, processPointId,getProductType());
		if (productHistory != null) {
			productHistory.setAssociateNo(getUserId());
			productHistory.setApproverNo(StringUtils.EMPTY);
			productHistory.setProcessPointId(processPointId);
			productHistory.setProductId(productId);
			productHistory.setActualTimestamp(trackerTimeStamp);
		}
		return productHistory;
	}
	
	/**
	 * This method is used to save Product Result
	 */
	public void saveAllProductResult(List<ProductResult> productResultList) {
		getDao(ProductResultDao.class).saveAll(productResultList);
	}
	
	public boolean isProductProcessed(DailyDepartmentSchedule shiftFirstPeriod) {
		
		String ppId = getProcessPointId();
		MultiLineHelper qiMultiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
		if(qiMultiLineHelper.isMultiLine())  {
				ProcessPoint newPP = qiMultiLineHelper.getProcessPointToUse(getProductModel().getProduct());
				ppId = newPP.getProcessPointId();				
		}

		ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(getProductType());
		return historyDao.isProductProcessedOnOrAfter(getProductId(), ppId, shiftFirstPeriod.getStartTimestamp());
	} 
	
	public String getQuantity() {
		return (String) getMbpnProductMapValue("quantity");
	}

	
	public String getEntryDept() {
			return (String) getMbpnProductMapValue("department");
	}

	public String getInspectionPartName() {
		return (String) getMbpnProductMapValue("inspectionPartName");
	}
	
	@SuppressWarnings("unchecked")
	public List<MbpnProduct> getProductList() {
			return (List<MbpnProduct>) getMbpnProductMapValue("products");
	}

	public void setProperty(QiPropertyBean property) {
		this.property = property;
	}

	public Object getMbpnProductMapValue(String key) {
		return !getProductModel().getMbpnProductMap().isEmpty() ? getProductModel().getMbpnProductMap().get(key) : "";
		}

	/**
	 * This method is used to find Entry Station by processPointId
	 * 
	 * @return
	 */
	public QiStationConfiguration findEntryStationConfigById(String propertyKey) {
		return getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProcessPointId(), propertyKey);
	}

	/**
	 * This method will be used to read station configuration for scrap reason.
	 * If no configuration present then it will read default value.
	 */
	public boolean isScrapReasonRequired() {
		boolean isScrapReasonRequired;
		QiStationConfiguration entryStation = findEntryStationConfigById("Scrap Reason");
		if (entryStation != null) {
			isScrapReasonRequired = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isScrapReasonRequired = QiEntryStationConfigurationSettings.SCRAP_REASON.getDefaultPropertyValue().equalsIgnoreCase("Yes") ? true : false;
		}
		return isScrapReasonRequired;
	}

	public GpcsDivision getGpcsDivisionDetails(ProcessPoint processPoint) {
		return getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
	}


	public TeamRotation getTeamDetails() {
		GpcsDivision gpcsDivision = getGpcsDivisionDetails(ApplicationContext.getInstance().getProcessPoint());
		DailyDepartmentSchedule schedule = getSchedule();
		if(null != gpcsDivision && null != schedule)
			return getDao(TeamRotationDao.class).findTeamDetails(gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
		return null;
	}
	
	public List<ExistingDefectDto> getCacheDefects() {
		List<QiDefectResult> qiDefectResults = getCachedDefectResultList();
		List<ExistingDefectDto> existingDefectDtos = new ArrayList<ExistingDefectDto>();
		qiDefectResults.stream().forEach(d -> existingDefectDtos.add(createExistingDefectDto(d)));
		return existingDefectDtos;
	}
	
	
	public ExistingDefectDto createExistingDefectDto(QiDefectResult qiDefectResult) {
		ExistingDefectDto existingDefectDto = new ExistingDefectDto();
		existingDefectDto.setQiDefectResult(qiDefectResult);
		QiDefectDevice qiDefectDevice = getDao(QiDefectDeviceDao.class).findByKey(qiDefectResult.getDefectResultId());
		String processPointId = qiDefectDevice == null ? qiDefectResult.getApplicationId() : qiDefectResult.getApplicationId() + " - " + qiDefectDevice.getDeviceId();
		existingDefectDto.setProcessPointId(processPointId);
		return existingDefectDto;
	}
	
	/**
	 * This method is used to get Division id
	 * @return
	 */
	public String getDivisionId() {
			return StringUtils.trimToEmpty(getApplicationContext().getProcessPoint().getDivisionId());
	}
	
	public String getProcessPointName(String processPointId) {
		ProcessPoint processPoint = getProcessPoint(processPointId);
		return processPoint == null ? null : processPoint.getProcessPointName();
	}
	
	public void setProcessPoints(Map<String, ProcessPoint> processPoints) {
		this.processPoints = processPoints;
	}

	public ProcessPoint getProcessPoint(String processPointId) {
		return processPoints == null ? null : processPoints.get(processPointId);
	}
	
	/**
	 * This method is used to find all process points
	 * @return
	 */
	public List<ProcessPoint> findAllProcessPoints() {
		return getDao(ProcessPointDao.class).findAll();
	}
	
	public ProcessPoint findProcessPoint(String processPointId) {
		return getDao(ProcessPointDao.class).findByKey(processPointId);
	}
	
	public QiDefectResult getDefectResult() {
		return defectResult;
	}

	public void setDefectResult(QiDefectResult defectResult) {
		this.defectResult = defectResult;
	}

	public List<QiDefectResult> getCachedDefectResultList() {
		return cachedDefectResultList;
	}

	public void setCachedDefectResultList(
			List<QiDefectResult> cachedDefectResultList) {
		this.cachedDefectResultList = cachedDefectResultList;
	}
	
	public Timestamp getProcessPointTimeStamp() {
		return getDao(ProcessPointDao.class).getDatabaseTimeStamp();
	}

	public DefectEntryCacheUtil getDefectEntryCacheUtil() {
		return defectEntryCacheUtil;
	}
	
	/**
	 * This method is used to get station result by id
	 * @param stationResultId
	 * @return
	 */
	public StationResult findStationResultById(StationResultId stationResultId) {
		return getDao(StationResultDao.class).findByKey(stationResultId);
	}
	
	/**
	 * This method is used to find Daily Department Schedule by current schedule
	 * @return
	 */
	public DailyDepartmentSchedule findDailyDepartmentScheduleByCurrentSchedule() {
		return getDao(DailyDepartmentScheduleDao.class).findShiftFirstPeriod(getSchedule());
	}
	
	/**
	 * This method is used to save station result
	 * @param stationResult
	 * @return
	 */
	public StationResult saveStationResult(StationResult stationResult) {
		return getDao(StationResultDao.class).save(stationResult);
	}
	
	/**
	 * This method is used to get QiRepairArea by using selected repair area name.
	 * @return QiRepairArea
	 */
	public QiRepairArea findRepairAreaByName(String repairAreaName) {
		return getDao(QiRepairAreaDao.class).findByKey(repairAreaName);
	}
	
	/**
	 * This method is used to find First Available Row by Repair Area
	 * @param repairArea
	 * @return
	 */
	public QiRepairAreaRow findFirstAvailableRowByRepairArea(QiRepairArea  repairArea) {
		return getDao(QiRepairAreaRowDao.class).findRepairAreaRowByRepairArea(repairArea);
	}
	
	/**
	 * This method is used to find First Available Space by Row
	 * @param row
	 * @return
	 */
	public QiRepairAreaSpace findFirstAvailableSpaceByRow(QiRepairAreaRow  row) {
		return getDao(QiRepairAreaSpaceDao.class).findFirstAvailableByRow(row);
	}
	
	/**
	 * This method is used to assign Repair Area Space
	 * @param productId
	 * @param defectResultId
	 * @param updateUser
	 * @param qiRepairAreaSpaceId
	 */
	public void assignRepairAreaSpace(String productId, long defectResultId, String updateUser, QiRepairAreaSpaceId qiRepairAreaSpaceId) {
		QiDefectResult qiDefectResult = null;
		String targetRespDept = "";
		String targetRepairArea = "";
		
		if (defectResultId > 0) {
			qiDefectResult = getDao(QiDefectResultDao.class).findByKey(defectResultId);
			targetRespDept = qiDefectResult.getResponsibleDept();
			targetRepairArea = qiDefectResult.getRepairArea();
		} 
		createRepairAreaSpaceHistory(qiRepairAreaSpaceId);
		assignRepairAreaSpaceWithTarget(productId, defectResultId, 
				updateUser, targetRespDept, targetRepairArea, qiRepairAreaSpaceId);
	}
	
	public void assignRepairAreaSpaceWithTarget(String productId, long defectResultId, String updateUser, 
			String targetRespDept, String targetRepairArea, QiRepairAreaSpaceId qiRepairAreaSpaceId) {
		getDao(QiRepairAreaSpaceDao.class).assignRepairAreaSpaceWithTarget(productId, defectResultId, 
				updateUser, targetRespDept, targetRepairArea, qiRepairAreaSpaceId);
		
		//replicate repair area result to GAL177TBX
		if (PropertyService.getPropertyBean(QiPropertyBean.class, getProcessPointId()).isReplicateRepairAreaResult()) {
			replicateRepairAreaResult(productId, defectResultId, updateUser, qiRepairAreaSpaceId);
		}
	}
	
	/**
	 * This method is used to deAssignRepairAreaSpace
	 * @param id
	 */
	public void deassignRepairAreaSpaceByProductId() {
		QiRepairAreaSpace repairAreaSpace = getDao(QiRepairAreaSpaceDao.class).findByProductId(getProductId());
		if(null != repairAreaSpace) {
			getDao(QiRepairAreaSpaceDao.class).clearRepairAreaSpace(repairAreaSpace.getId(), getUserId());
			
			removeInRepairAreaProduct(getProductId());
		}
	}
	
	public void removeInRepairAreaProduct(String productId) {
		//remove repair area result from GAL177TBX if configured, primary key is product_id
		if (PropertyService.getPropertyBean(QiPropertyBean.class, getProcessPointId()).isReplicateRepairAreaResult()) {
			getDao(InRepairAreaDao.class).removeByKey(productId);	
		}
	}
	
	/**
	 * This method is used to assign Unit to Repair Area
	 * @param repairAreaName
	 * @param defectResultId
	 * @return
	 */
	public String assignUnitToRepairArea(String repairAreaName, long defectResultId) {
		String message = StringUtils.EMPTY;
		if(StringUtils.isBlank(repairAreaName))  {
			message = "Repair area name is blank";
			return  message;
		}
		//repair area name not blank
		if(repairAreaName.equalsIgnoreCase(QiConstant.CLEAR_REPAIR_AREA))  {
			deassignRepairAreaSpaceByProductId();
			message = "Cleared repair area";
			return message;
		}
		//repair area name is neither blank nor "Clear Repair Area"
		QiRepairArea qiRepairArea = findRepairAreaByName(repairAreaName);

		if(null != qiRepairArea)
		{
			QiRepairAreaRow qiRepairAreaRow = findFirstAvailableRowByRepairArea(qiRepairArea);

			if(null != qiRepairAreaRow){
				QiRepairAreaSpace qiRepairAreaSpace = findFirstAvailableSpaceByRow(qiRepairAreaRow);
				if(null != qiRepairAreaSpace ){
					deassignRepairAreaSpaceByProductId();
					assignRepairAreaSpace(getProductId(), defectResultId, getUserId(), qiRepairAreaSpace.getId());
					message = "Unit assigned to Configured Repair Area Space successfully.";
				} else {
					message = "No Space is available in Configured Repair Area.";
				}
			} else {
				message = "No Row is available in Configured Repair Area.";
			}
		} else {
			message = "Could not find Configured Repair Area.";
		}
		return message;
	}
	
	/**
	 * This method is used to find count of all not fixed defects
	 * @return
	 */
	public Long findNotFixedDefectCountByProductId() {
		return getDao(QiDefectResultDao.class).findNotFixedDefectCountByProductId(getProductId());
	}
	
	/**
	 * This method is used to add unit to configured Repair Area
	 */
	public String addUnitToConfiguredRepairArea(long defectResultId) throws Exception {
		String message = StringUtils.EMPTY;
		String configuredRepairAreaName = getConfiguredRepairAreaName();
		if(!StringUtils.isEmpty(configuredRepairAreaName) && findNotFixedDefectCountByProductId() == 0) {
			message = assignUnitToRepairArea(configuredRepairAreaName, defectResultId);
		}
		return message;
	}
	
	public String getConfiguredRepairAreaName() {
		String configuredRepairAreaName = null;
		QiStationConfiguration setting = findEntryStationConfigById(AdditionalStationConfigSettings.REPAIR_AREA.getSettingName());
		if(null != setting) {
			configuredRepairAreaName = setting.getPropertyValue();
		}
		return configuredRepairAreaName;
	}
	
	/**
	 * This method is used to create Repair Area Space History data. 
	 *
	 */
	private void createRepairAreaSpaceHistory(QiRepairAreaSpaceId repairAreaSpaceId) {
		QiRepairAreaSpace qiRepairAreaSpace = getDao(QiRepairAreaSpaceDao.class).findByKey(repairAreaSpaceId);
		QiRepairAreaSpaceHistory repairAreaSpaceHistory = setRepairAreaSpaceHistoryData(qiRepairAreaSpace);
		getDao(QiRepairAreaSpaceHistoryDao.class).save(repairAreaSpaceHistory);
	}
	/**
	 * This method is used to set Repair Area Space History data. 
	 *
	 */
	public QiRepairAreaSpaceHistory setRepairAreaSpaceHistoryData(QiRepairAreaSpace qiRepairAreaSpace) {
		QiRepairAreaSpaceHistoryId qiRepairAreaSpaceHistoryId= new QiRepairAreaSpaceHistoryId(qiRepairAreaSpace);
		QiRepairAreaSpaceHistory qiRepairAreaSpaceHistory= new QiRepairAreaSpaceHistory();
		qiRepairAreaSpaceHistory.setId(qiRepairAreaSpaceHistoryId);
		qiRepairAreaSpaceHistory.setDefectResultId(qiRepairAreaSpace.getDefectResultId());
		qiRepairAreaSpaceHistory.setTargetRespDept(qiRepairAreaSpace.getTargetRespDept());
		qiRepairAreaSpaceHistory.setTargetRepairArea(qiRepairAreaSpace.getTargetRepairArea());
		qiRepairAreaSpaceHistory.setCreateUser(getUserId());
		return qiRepairAreaSpaceHistory;
	}
	
	public void replicateRepairAreaResult(String productId, long defectResultId, String updateUser, QiRepairAreaSpaceId qiRepairAreaSpaceId) {
		
		QiDefectResult qiDefectResult = getDao(QiDefectResultDao.class).findByKey(defectResultId);
		String respDept = "";
		String rejection = "";
		String repairAreaName = "";
		String qiRepairAreaName = qiRepairAreaSpaceId.getRepairAreaName();
		
		if (qiDefectResult != null) {
			respDept = qiDefectResult.getResponsibleDept();
			rejection = qiDefectResult.getPartDefectDesc();
		}
		
		if (!StringUtils.isBlank(qiRepairAreaName)) {
			repairAreaName = getDao(QiExternalSystemDefectMapDao.class).findOldRepairAreaByRepairArea(qiRepairAreaName);
			if (StringUtils.isBlank(repairAreaName)) {
				repairAreaName = qiRepairAreaName;
			}
		}
		
		InRepairArea inRepairArea = new InRepairArea();
		inRepairArea.setProductId(productId);
		inRepairArea.setRepairAreaName(repairAreaName);
		inRepairArea.setActualTimestamp(new java.util.Date());
		inRepairArea.setResponsibleDept(respDept);	
		inRepairArea.setRejection(rejection);
		getDao(InRepairAreaDao.class).save(inRepairArea);		
	}
	
	/**
	 * Method to get repairTimestamp
	 * 
	 * @param repairTimestamp
	 * @return	Timestamp 
	 */
	public Timestamp getRepairTimestamp(Timestamp repairTimestamp) {
		Timestamp dbTimestamp = getProcessPointTimeStamp();
		if (repairTimestamp == null || repairTimestamp.after(dbTimestamp)) {
			return dbTimestamp; 
		}
		return repairTimestamp;
	}
	
	public Long getDefectTransactionGroupId(QiDefectResult qiDefectResult) {
		return getDao(QiDefectResultDao.class).getNextDefectTransactionGrounpId(qiDefectResult);
		
	}

	protected Map<String, ProcessPoint> getProcessPoints() {
		if (processPoints == null) {
			processPoints = new HashMap<String, ProcessPoint>();
		}
		return processPoints;
	}
	
	/**
	 * This method is used to save Installed Part Result
	 */
	public void saveAlllInstalledPartResult(List<InstalledPart> installedPartResultList) {
		getDao(InstalledPartDao.class).saveAll(installedPartResultList);
	}
	
	public boolean isTrackingPoint() {
		ProcessPoint processPoint = getApplicationContext().getProcessPoint();
		return processPoint.isTrackingPoint();
	}
	public String getCurrentWorkingEntryDept() {
		return currentWorkingEntryDept;
	}
	public void setCurrentWorkingEntryDept(String newEntryDept) {
		this.currentWorkingEntryDept = newEntryDept;
	}
	public ProcessPoint getCurrentWorkingProcessPoint() {
		return currentWorkingProcessPoint;
	}
	public void setCurrentWorkingProcessPoint(ProcessPoint currentWorkingProcessPoint) {
		this.currentWorkingProcessPoint = currentWorkingProcessPoint;
	}
	public String getCurrentWorkingPlantName()  {
		return getCurrentWorkingProcessPoint().getPlantName();
	}
	public String getCurrentWorkingProcessPointId() {
		return currentWorkingProcessPointId;
	}
	public void setCurrentWorkingProcessPointId(String newProcessPointId) {
		this.currentWorkingProcessPointId = newProcessPointId;
		ProcessPoint pp = getProcessPoints().get(currentWorkingProcessPointId);
		if (pp == null) {
			pp = getDao(ProcessPointDao.class).findById(currentWorkingProcessPointId);
			getProcessPoints().put(currentWorkingProcessPointId, pp);
		}
		currentWorkingProcessPoint = pp;
	}
	
	
	public List<ProductCarrier> findAllByProductId(String ProductId) {
		return getDao(ProductCarrierDao.class).findAllByProductId(ProductId);
	}
}
