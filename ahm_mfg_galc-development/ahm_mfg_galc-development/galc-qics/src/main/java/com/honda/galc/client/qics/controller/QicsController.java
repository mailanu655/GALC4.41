package com.honda.galc.client.qics.controller;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dunnage.DunnageUtils;
import com.honda.galc.client.entity.manager.DailyDepartmentScheduleUtil;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.qics.config.QicsClientConfig;
import com.honda.galc.client.qics.model.ClientModel;
import com.honda.galc.client.qics.model.ProductModel;
import com.honda.galc.client.qics.property.DefaultQicsPropertyBean;
import com.honda.galc.client.qics.property.QicsPropertyBean;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.qics.DefectActualProblemDao;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.DefectRepairMethodDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.DefectTypeDescriptionDao;
import com.honda.galc.dao.qics.ImageDao;
import com.honda.galc.dao.qics.InspectionModelDao;
import com.honda.galc.dao.qics.InspectionPartDescriptionDao;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.IPPTagId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectActualProblemId;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectRepairMethod;
import com.honda.galc.entity.qics.DefectRepairMethodId;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.oif.IMissionShippingTransactionService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>QicsController Class description</h3>
 * <p> QicsController description </p>
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
 * Aug 22, 2011
 *
 *
 */
/**
 * added new method for Repair In Tracking
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public class QicsController {

	private Integer checkOneOff;

	private QicsFrame frame;
	private QicsClientConfig clientConfig;
	private ClientAudioManager audioManager;

	// === application model === //
	/**
	 * Application model
	 */
	private ClientModel clientModel;
	/**
	 * Product processing model.
	 */
	private ProductModel productModel = new ProductModel();

	private Map<String, ProductModel> subProductModel = new HashMap<String, ProductModel>();

	private boolean refreshProductPreCheckResults = false;
	private boolean refreshProductCheckResults = false;
	
	protected boolean doDunnageUpdate = true;

	public QicsController(QicsFrame frame, String clientId, ProcessPoint processPoint) {
		super();
		this.frame = frame;
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		setClientConfig(new QicsClientConfig(getProductType(), processPoint, frame.getApplication()));
		setClientModel(new ClientModel(clientId, processPoint));
		initialize();
	}

	protected void initialize() {
		createQicsPropertyBean();
		initDunnagePrinters();
	}

	protected QicsPropertyBean createQicsPropertyBean() {
		QicsPropertyBean qicsPropertyBean = null;
		try {
			qicsPropertyBean = PropertyService.getPropertyBean(QicsPropertyBean.class, getProcessPointId());
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Failed to load QicsPropertyBean(qics.properties), will create default property bean." + e, getClass().getName(), "createQicsPropertyBean");
		}
		return qicsPropertyBean;
	}

	protected void initDunnagePrinters() {
		if (!getClientConfig().isDunnageRequired()) {
			return;
		}
		String dunnagePrinter = StringUtils.trimToEmpty(getQicsPropertyBean().getDunnagePrinter());
		String dunnageForm = StringUtils.trimToEmpty(getQicsPropertyBean().getDunnageForm());
		if (StringUtils.isBlank(dunnagePrinter) || StringUtils.isBlank(dunnageForm)) {
			return;
		}
		List<BroadcastDestination> broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(getProcessPointId());
		if (broadcastDestinations == null || broadcastDestinations.isEmpty()) {
			return;
		}
		for (BroadcastDestination bd : broadcastDestinations) {
			if (bd.getDestinationId().equals(dunnagePrinter) && dunnageForm.equals(StringUtils.trim(bd.getRequestId()))) {
				getClientModel().getDunnagePrinters().add(bd);
			}
		}
	}

	public Logger getLogger() {
		return frame.getLogger();
	}

	// === client api === //
	public void submitInitializeTerminal() {

		ProcessPoint processPoint = clientModel.getProcessPoint();

		getClientModel().setPlant(findPlant(processPoint));

		getClientModel().setDailyDepartmentScheduleUtil(new DailyDepartmentScheduleUtil(processPoint));

		getClientModel().setStationResult(
				getStationResults(getClientModel().getDailyDepartmentScheduleUtil().getCurrentSchedule()));
		

		List<ProcessPoint> processPointList = findAllProcessPoints();
		Map<String, ProcessPoint> processPoints = new HashMap<String, ProcessPoint>();
		if (processPointList != null) {
			for (ProcessPoint item : processPointList) {
				processPoints.put(item.getProcessPointId(), item);
			}
		}

		getClientModel().setProcessPoints(processPoints);

		setAssociateNumberCache(createAssociateNumberCache());
		cacheAssociateNumber(frame.getUserId());
		checkOneOff = 0;
	}

	private List<ProcessPoint> findAllProcessPoints() {

		return getDao(ProcessPointDao.class).findAll();

	}

	private Plant findPlant(ProcessPoint processPoint) {
		return getDao(PlantDao.class).findById(processPoint.getSiteName(), processPoint.getPlantName());
	}

	public List<Division> getDepartments() {
		List<Division> divisions = getClientModel().getDepartments();
		if (!getClientModel().isDepartmentFullyLoaded()) {
			for (Division division : divisions) {
				division.setLines(getDao(LineDao.class).findAllByDivisionId(division, false));
				division.setZones(getDao(ZoneDao.class).findAllByDivisionId(division.getDivisionId()));
			}
		}
		return divisions;
	}

	private StationResult getStationResults(DailyDepartmentSchedule schedule) {

		if(schedule == null) return null;

		StationResultId stationResultId = new StationResultId();
		stationResultId.setApplicationId(getProcessPointId());
		stationResultId.setProductionDate(schedule.getId().getProductionDate());
		stationResultId.setShift(schedule.getId().getShift());

		return getDao(StationResultDao.class).findByKey(stationResultId);

	}

	private int getCheckOneOffLimit() {
		return getQicsPropertyBean().getCheckOneOffLimit();
	}

	public String getCheckOneOffMessage() {
		return getQicsPropertyBean().getCheckOneOffMessage();
	}

	public boolean submitProductForProcessing(String inputNumber) {

		setProductModel(new ProductModel());

		getProductModel().setInputNumber(inputNumber);

		BaseProduct product = findProduct(inputNumber);

		if (product == null)
			throw new TaskException("The product does not exist");

		getLogger().info("start to process product " + product);

		getProductModel().setProduct(product);

		getProductModel().setOwnerProduct(findOwnerProduct(product));

		selectInspectionModels(product.getModelCode());

		List<DefectResult> defectResults = 
			getDao(DefectResultDao.class).findAllByProductId(product.getProductId());

		for (DefectResult defectResult : defectResults) {
			defectResult.setEntryStation(getApplicationName(defectResult.getId().getApplicationId()));
		}

		getProductModel().addExistingDefects(defectResults);

		submitPreCheckProductState();
		
		submitWarnCheckProductState();

		if (!StringUtils.isEmpty(product.getProductionLot())) {
			ProductionLot lot = getDao(ProductionLotDao.class).findByKey(product.getProductionLot());
			product.setProdLot(lot);
		}
		/*
		 * @KM : Broadcast to QualityWorx
		 * */
		if (isSetQualityWorx()) {
			if (isShowSphViewerWhenScan()) {
				createQualityWorxMessage(product);
			}
		}
		subProductModel.clear();
		return true;
	}

	/*
	 * @KM : QualityWorx
	 * */
	private void createQualityWorxMessage(BaseProduct product) {
		getService(BroadcastService.class).asynBroadcast(getProcessPointId(), getSeqNum(), product.getProductId().toString());
	}

	private boolean isShowSphViewerWhenScan() {
		return PropertyService.getPropertyBoolean(getProcessPointId(), "SHOW_SPHVIEWER_WHEN_SCAN", true);
	}

	private boolean isSetQualityWorx() {
		if (PropertyService.getPropertyBoolean(getProcessPointId(), "BROADCAST_TO_QWX", false)) {
			if(getSeqNum()>0) return true;
			else {
				getLogger().info("No valid BROADCAST_DEST_SEQ_NUM defined");
				return false;
			}
		}else return false;
	}

	private int getSeqNum() {
		return PropertyService.getPropertyInt(getProcessPointId(), "BROADCAST_DEST_SEQ_NUM", 0);
	}

	private String getApplicationName(String appId) {

		String appName = getClientModel().getApplicationName(appId);
		if (StringUtils.isEmpty(appName)) {
			Application app = getDao(ApplicationDao.class).findByKey(appId);
			if (app != null) {
				getClientModel().putApplication(app);
				return app.getApplicationName();
			}else return null;
		}else return appName;
	}

	public void submitLpdcProductForProcessing(String inputNumber) {

		getLogger().info("excuting submitLpdcProductForProcessing(inputNumber) function");

		setProductModel(new ProductModel());

		getProductModel().setInputNumber(inputNumber);

		Head head = getDao(HeadDao.class).findByKey(inputNumber);

		if(head == null) throw new TaskException("Product not in database");

		List<InspectionModel> list = 
			getDao(InspectionModelDao.class).findAllByApplicationIdAndModelCode(getClientModel().getProcessPointId(),head.getModelCode());

		getProductModel().setProduct(head);

		getProductModel().setOwnerProduct(null);
		getProductModel().setInspectionModels(list);

	}

	// === form submit actions === //
	public void submitScrap(ExceptionalOut scrap) {

		getLogger().info("excuting submitScrap function");

		saveDefects(DefectStatus.SCRAP, null, scrap);
		
		postProcess();

	}

	public void submitIPP(String ippTagNo) {

		getLogger().info("excuting submitIPP(ippTagNo) function");

		IPPTag ippTag = new IPPTag();
		IPPTagId id = new IPPTagId();
		id.setIppTagNo(ippTagNo);
		id.setDivisionId(getClientModel().getProcessPoint().getDivisionId());
		id.setProductId(getProductModel().getProduct().getProductId());
		ippTag.setId(id);
		ippTag.setActualTimestamp(CommonUtil.getTimestampNow());
		ippTag.setProcessPointId(getProcessPointId());
		getDao(IPPTagDao.class).save(ippTag);

	}

	private Map<String, Object> submitCheckProductState(String[] productCheckTypes) {
		if (productCheckTypes == null || productCheckTypes.length == 0) {
			return new LinkedHashMap<String,Object>();
		}
		return ProductCheckUtil.check(getProductModel().getProduct(), getClientModel().getProcessPoint(), productCheckTypes);
	}

	public void submitPreCheckProductState() {
		getLogger().info("excuting submitPreCheckProductState(productCheckTypes) function");
		String[] productCheckTypes = getProductPreCheckTypes();
		Map<String, Object> checkResults = submitCheckProductState(productCheckTypes);
		getProductModel().setProductPreCheckResults(checkResults);
		if(checkResults != null && !checkResults.isEmpty() && getQicsPropertyBean().isConditionalBroadcastPreCheck())
		   conditionalBroadcast();
	}

	public void submitItemCheckProductState() {
		getLogger().info("excuting submitItemCheckProductState(productCheckTypes) function");
		Map<String, Object> checkResults = submitCheckProductState(getProductCheckTypes());
		getProductModel().setProductItemCheckResults(checkResults);
	}

	public void submitWarnCheckProductState() {
		getLogger().info("excuting submitWarnCheckProductState(productCheckTypes) function");
		Map<String, Object> checkResults = submitCheckProductState(getProductWarnCheckTypes());
		getProductModel().setProductWarnCheckResults(checkResults);
	}

	public int submitDunnage() {

		getLogger().info("excuting submitDunnage function");

		String productId = getProductModel().getProduct().getProductId();
		String dunnageNumber = getClientModel().getDunnageNumber();
		int dunnageCapacity = getDunnageCartQuantity();

		return updateDunnage(productId, dunnageNumber, dunnageCapacity);

	}

	public void removeDunnage(BaseProduct product) {
		getLogger().info("excuting removeDunnage(productId) function");

		String dunnage = product.getDunnage();
		int updateCount = ProductTypeUtil.getProductDao(getProductType()).removeDunnage(product.getProductId());

		if(updateCount > 0 )getLogger().info("Remove Dunnage " + dunnage + " for product " + product);

	}

	public void submitDirectPass() {
		getLogger().info("excuting submitDirectPass function");
		saveDefects(null, getProductModel().getChangedDefects(), null);
		postProcess();
		if (getCheckOneOffLimit() != -1)
			increaseCheckOneOff();
	}

	private void increaseCheckOneOff() {
		checkOneOff++;
	}

	public void submit() {
		// save defects
		getLogger().info("excuting submit function");

		DefectStatus defectStatus = getProductModel().getOutstandingDefects().isEmpty() ?
				DefectStatus.REPAIRED : DefectStatus.OUTSTANDING;

		saveDefects(defectStatus, getProductModel().getChangedDefects(), null);

		String[] installedProductTypes = getQicsPropertyBean().getSubProductTypes();
		if (installedProductTypes.length > 0) {
			for (String productType : installedProductTypes) {
				for (DefectResult defectResult : getSubProductModel(productType).getChangedDefects()) {
					BaseProduct installedProduct = ProductTypeUtil.getProductDao(productType).findBySn(defectResult.getId().getProductId());
					// saveSubProductDefects(installedProduct,defectResult);
					getDao(DefectResultDao.class).save(defectResult);
						defectStatus=getDao(DefectResultDao.class).findAllOutstandingByProductId(installedProduct.getProductId()).isEmpty() ?
							DefectStatus.REPAIRED : DefectStatus.OUTSTANDING;
					ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(installedProduct.getProductType());
					productDao.updateDefectStatus(installedProduct.getProductId(), defectStatus);
				}
			}
		}
		postProcess();

		// @KM Broadcast to Qualityworx
		if (isSetQualityWorx()) {
			List<DefectResult> repairedDefects = getProductModel().getUpdatedDefects();
			if (repairedDefects.isEmpty()) {
				getLogger().info("No Repair Information");
			} else {
				DataContainer dc = new DefaultDataContainer();
				dc.put(DataContainerTag.PRODUCT_ID, getProductModel().getProduct().getProductId().toString());
				dc.put(DataContainerTag.DEFECT_REPAIRED, repairedDefects);
				getService(BroadcastService.class).asynBroadcast(getProcessPointId(), getSeqNum(), dc);
			}
		}
	}

	protected void postProcess() {
		track();
		broadcast();
		if(!getQicsPropertyBean().isConditionalBroadcastPreCheck())
		  conditionalBroadcast();
	}

	protected void track() {
		if (getClientModel().isTrackingRequired()) {
			track(getProcessPointId());
		}
	}

	protected void broadcast() {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		getService(BroadcastService.class).broadcast(getProcessPointId(), getProductModel().getProduct(), dc);
	}

	/**
	 * <p>
	 * This method is used to perform broadcast only when certain conditions are met.
	 * To validate broadcast condition Product Check (ProductCheckUtil) component is used.
	 * As requests for conditional broadcast may become more common in the future, we may 
	 * consider moving this method from QICS to Broadcast framework.
	 * </p>
	 * 
	 * Usage : <br/>
	 * <ul>
	 * <li>set Broadcast Destination autoEnable flag to false</li>
	 * <li>
	 * create property of Map type:
	 * <ul>
	 * <li>
	 *                property name : CONDITIONAL_BROADCAST_CHECK_MAPPING{REQUEST_ID} or
	 *            </li>
	 * <li>
	 *                property name : CONDITIONAL_BROADCAST_CHECK_MAPPING{DESTINATION_ID}
	 *            </li>
	 * <li>
	 *                property value : ProductCheckType1,ProductCheckType2,ProductCheckType3
	 *            </li>
	 * </ul>
	 * </li>
	 * </ul>
	 * <br />
	 * Where <br />
	 * REQUEST_ID or DESTINATION_ID - are properties of Broadcast Destination entity (gal111tbx), <br />
	 * ProductCheckType1,ProductCheckType2,ProductCheckType3 - check types to be executed by ProductCheckUtil component.
	 * 
	 */
	protected void conditionalBroadcast() {
		Map<String, String> checkTypesPropertyMapping = getQicsPropertyBean().getConditionalBroadcastCheckMapping();
		if (checkTypesPropertyMapping == null || checkTypesPropertyMapping.isEmpty()) {
			return;
		}

		BroadcastDestinationDao bdDao = ServiceFactory.getDao(BroadcastDestinationDao.class);
		List<BroadcastDestination> destinations = bdDao.findAllByProcessPointId(getProcessPointId(), false);
		if (destinations == null || destinations.isEmpty()) {
			return;
		}
		for (BroadcastDestination bd : destinations) {
			if (bd == null) {
				continue;
			}
			String destinationId = bd.getDestinationId();
			String requestId = bd.getRequestId();
			String checkTypesProperty = null;

			if (StringUtils.isNotBlank(requestId)) {
				checkTypesProperty = checkTypesPropertyMapping.get(requestId);
			} else if (StringUtils.isNotBlank(destinationId)) {
				checkTypesProperty = checkTypesPropertyMapping.get(destinationId);
			}
			if (StringUtils.isNotBlank(checkTypesProperty)) {
				String[] checkTypes = checkTypesProperty.split(",");
				if (checkTypes == null || checkTypes.length == 0) {
					continue;
				}
				Map<String, Object> result = submitCheckProductState(checkTypes);
				if (result == null || result.isEmpty()) {
					continue;
				}
				DataContainer dc = new DefaultDataContainer();
				dc.put(DataContainerTag.PRODUCT_ID, getProductModel().getProduct().getProductId());
				dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
				getService(BroadcastService.class).broadcast(bd.getId().getProcessPointId(), bd.getId().getSequenceNumber(), dc);
			}
		}
	}

	private void saveDefects(DefectStatus defectStatus, List<DefectResult> defectResults, ExceptionalOut exceptionalOut) {

		DailyDepartmentSchedule schedule = getClientModel().getCurrentSchedule();
		StationResult stationResult =  getDao(DefectResultDao.class).saveAllDefectResults(
				getProductModel().getProduct(),
				defectStatus,
				getClientModel().getProcessPointId(),
				defectResults,
				schedule,
				exceptionalOut, false, true);

		if(stationResult != null) getClientModel().setStationResult(stationResult);

		if(getQicsPropertyBean().isGlobalDirectPass())
		{
			String[] gdpProcessPoints = getQicsPropertyBean().getGlobalDirectPassProcessPoint();
			if (gdpProcessPoints != null&& gdpProcessPoints.length > 0) 
			{
				for (int i = 0; i < gdpProcessPoints.length; i++)
				{
					if(gdpProcessPoints[i]!=null)
					{	
						Parameters params1 = Parameters.with("1", gdpProcessPoints[i].trim()).put("2", getProductModel().getProduct().getProductId());

						getDao(DefectResultDao.class).updateGdpDefectsVQWriteUpDept(params1);
					}
				}
			}
			if (ProductTypeUtil.getProductHistoryDao(getProductType()).hasProductHistory(getProductModel().getProduct().getProductId(), getClientModel().getProcessPointId())) {
				Parameters params2 = Parameters.with("1", getClientModel().getProcessPointId()).put("2", getProductModel().getProduct().getProductId());
				getDao(DefectResultDao.class).updateGdpDefects(params2);
			} else {
				getDao(DefectResultDao.class).updateGdpDefectsByCurrentProcess(getProductModel().getProduct().getProductId());
			}
		}
	}

	public void submitPreheat() {

		getLogger().info("excuting submitPreheat function");

		saveDefects(DefectStatus.PREHEAT_SCRAP, null, null);

		postProcess();
	}

	public void submitPreheat(ExceptionalOut scrap) {

		getLogger().info("excuting submitPreheat(scrap) function");

		saveDefects(DefectStatus.PREHEAT_SCRAP, null, scrap);

		postProcess();

	}

	public void submitProcessPreheatProduct() {

		getLogger().info("excuting submitProcessPreheatProduct function");

		// DataContainer dataContainer = new DefaultDataContainer();
		//
//		setRequestName(dataContainer, Request.PROCESS_PREHEAT_PRODUCT.getName());
		// setProduct(dataContainer, getProductModel().getProduct());
		// setProcessPoint(dataContainer, getClientModel().getProcessPoint());
		// setShift(dataContainer, getClientModel().getShift());
		// setMessagId(dataContainer, "");
		//
		// dataContainer = execute(dataContainer);
		//
		// if (isErrorMsgExists()) {
		// return;
		// }
		//
		// // === retrieve data === //
		// getClientModel().setShift(getShift(dataContainer));
		// getClientModel().setStationResult(getQicsStationCalculations(dataContainer));
		// getProductModel().setProduct(getProduct(dataContainer));
	}

	// === event handlers === //
	public void selectPartLocations(String partGroupName) {

		List<InspectionPartDescription> parts = getDao(InspectionPartDescriptionDao.class).findAllByPartGroupName(partGroupName);

		getProductModel().setParts(parts);
	}

	public void selectDefectDescriptions(String partGroupName, String partName, String locationName) {

		Map<String, List<DefectDescription>> defectsMap = new LinkedHashMap<String, List<DefectDescription>>();
		List<DefectDescription> defectDescriptions = getDao(DefectDescriptionDao.class).findAllBy(partGroupName, partName, locationName);

		for (DefectDescription description : defectDescriptions) {

			String defectTypeName = description.getDefectTypeName();
			List<DefectDescription> descriptions = defectsMap.get(defectTypeName);
			if (descriptions == null) {
				descriptions = new Vector<DefectDescription>();
				defectsMap.put(defectTypeName, descriptions);
			}
			descriptions.add(description);
		}

		getProductModel().setDefectDescriptions(defectsMap);

	}

	public List<DefectDescription> selectDefectDescriptions(String defectTypeName) {

		List<DefectDescription> defectDescriptions = getClientModel().getDefectDescriptions(defectTypeName);

		if (defectDescriptions == null) {
			defectDescriptions = getDao(DefectDescriptionDao.class).findAllByDefectType(defectTypeName);
			getClientModel().putDefectDescriptions(defectTypeName, defectDescriptions);
		}
		return defectDescriptions;

	}

	private DefectDescription selectDefectDescription(DefectResult defect) {
		List<DefectDescription> defectDescriptions = selectDefectDescriptions(defect.getDefectTypeName());
		for (DefectDescription defectDescription : defectDescriptions) {
			DefectDescriptionId id = new DefectDescriptionId();
			id.setInspectionPartName(defect.getInspectionPartName());
			id.setInspectionPartLocationName(defect.getInspectionPartLocationName());
			id.setDefectTypeName(defect.getDefectTypeName());
			id.setTwoPartPairPart(defect.getTwoPartPairPart());
			id.setTwoPartPairLocation(defect.getTwoPartPairLocation());
			id.setSecondaryPartName(defect.getSecondaryPartName());
			id.setPartGroupName(defect.getPartGroupName());
			if(defectDescription.getId().equals(id)) return defectDescription;
		}
		return null;
	}

	public void selectDefectTypes(String defectGroupName) {

		List<DefectTypeDescription> results = getClientModel().getDefectTypeDescriptions(defectGroupName);

		if (results == null) {
			results = 
				getDao(DefectTypeDescriptionDao.class).findAllByDefectGroupName(defectGroupName);
		}
		getProductModel().setDefectTypes(results);

	}

	public void selectImage(DefectGroup defectGroup) {

		if(getClientModel().getImage(defectGroup.getDefectGroupName()) != null) return;

		Image image = getDao(ImageDao.class).findByImageName(defectGroup.getImageName());
		getClientModel().putImage(defectGroup.getDefectGroupName(), image);

	}

	public void selectInspectionModels(String modelCode) {

		List<InspectionModel> inspectionModels = getClientModel().getInspectionModels(modelCode);
		if (inspectionModels == null) {
			inspectionModels = 
				getDao(InspectionModelDao.class).findAllByApplicationIdAndModelCode(getClientModel().getProcessPointId(),modelCode);
			getClientModel().putInspectionModels(modelCode, inspectionModels);
		}
		getProductModel().setInspectionModels(inspectionModels);
	}

	// === data api === //

	public List<Line> selectLines(String departmentId) {

		Division division = getDao(DivisionDao.class).findByDivisionId(departmentId);
		return getDao(LineDao.class).findAllByDivisionId(division, false);
	}

	public List<Zone> selectZones(String departmentId) {
		return getDao(ZoneDao.class).findAllByDivisionId(departmentId);
	}

	public List<BaseProduct> selectDunnageProducts(String dunnageNumber) {
		List<? extends BaseProduct> result = null;

		result = ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);

		List<BaseProduct> list = new ArrayList<BaseProduct>();
		if (result != null) {
			list.addAll(result);
		}
		return list;
	}

	// === misc public api === //

	public void cacheAssociateNumber(String associateNumber) {
		if (associateNumber == null || associateNumber.trim().length() == 0) {
			return;
		}

		if (getAssociateNumberCache().containsKey(associateNumber)) {
			getAssociateNumberCache().get(associateNumber);
		} else {
			getAssociateNumberCache().put(associateNumber, associateNumber);
		}
	}

	// === factory methods === //
	public Map<String, String> createAssociateNumberCache() {
		final int cSize = getQicsPropertyBean().getAssociateNumberCacheSize();
		Map<String, String> cache = new LinkedHashMap<String, String>(cSize, 0.75f, true) {
			private static final long serialVersionUID = 1L;
			private int cacheSize = cSize;

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
				return size() > cacheSize;
			}
		};
		// cache.put("11111", "11111");
		// cache.put("22222", "22222");
		// cache.put("55555", "55555");
		// cache.put("88888", "88888");
		// cache.put("77777", "77777");
		// cacheAssociateNumber(getFrame().getUserId());
		// cache.put(getFrame().getUserId(), getFrame().getUserId());
		return cache;
	}

	// // === utility methods === //
	protected void getProductForProcessingContainer(String inputNumber) {
		// DataContainer dataContainer = new DefaultDataContainer();
		//
		setProductModel(new ProductModel());
		//
		// setRequestName(dataContainer, Request.CHECK_PRODUCT.getName());
		// getProductModel().setInputNumber(inputNumber);
		//
		// setProductId(dataContainer, getProductModel().getInputNumber());
		// setProcessPoint(dataContainer, getClientModel().getProcessPoint());
		// setShift(dataContainer, getClientModel().getShift());
		//
		// if (getClientConfig().isForwardProcessPointIdDefined()) {
//			setForwardProcessPointId(dataContainer, getClientConfig().getForwardProcessPointId());
		// }
		// setMessagId(dataContainer, "");
		//
//		if (!StringUtils.isEmpty(getClientConfig().getSendInputNumberToDeviceId())) {
//			setSendInputNumberToDeviceId(dataContainer, getClientConfig().getSendInputNumberToDeviceId());
		// }
		//
//		setProductCheckTypes(dataContainer, getClientConfig().getProductPreCheckTypes());
		//
		// return dataContainer;
	}

	public ActionId getSubmitActionId(QicsPanel currentPanel) {
		if (currentPanel == null) {
			return null;
		}

		ActionId submitActionId = ActionId.CANCEL;
		boolean submitWithChecks = isProductCheckTypesDefined() || getClientConfig().isOffProcessPointIdDefined() || getClientConfig().isDunnageRequired();
		boolean defectsUpdated = getProductModel().isDefectsUpdated();
		if (!defectsUpdated) {
			String[] installedProductTypes = getQicsPropertyBean().getSubProductTypes();
			for (String productType : installedProductTypes) {
				if (getSubProductModel(productType) != null) {
					defectsUpdated = getSubProductModel(productType).isDefectsUpdated();
					if (defectsUpdated)
						break;

				}
			}
		}
		boolean repairTracking = getClientConfig().isRepairTracking();

		if (currentPanel.getQicsViewId().equals(QicsViewId.CHECK_RESULTS)) {
			if (defectsUpdated) {
				submitActionId = ActionId.SUBMIT_WITH_WARN;
			} else {
				submitActionId = ActionId.DIRECT_PASS_WITH_WARN;
			}
		} else if (currentPanel.getQicsViewId().equals(QicsViewId.DUNNAGE)) {
			if (defectsUpdated) {
				submitActionId = ActionId.SUBMIT_WITH_DUNNAGE;
			} else {
				submitActionId = ActionId.DIRECT_PASS_WITH_DUNNAGE;
			}
		} else if (submitWithChecks) {
			if (defectsUpdated) {
				submitActionId = ActionId.SUBMIT_WITH_CHECKS;
			} else {
				submitActionId = ActionId.DIRECT_PASS_WITH_CHECKS;
			}
		}else if(repairTracking&&!getProductModel().getOutstandingDefects().isEmpty())
		{
			submitActionId = ActionId.SUBMIT_WITH_REPAIR_TRACKING;
		}
		 else {
			if (defectsUpdated) {
				submitActionId = ActionId.SUBMIT;
			} else {
				submitActionId = ActionId.DIRECT_PASS;
			}
		}
		return submitActionId;
	}

	public boolean isProcessPointOff() {
		boolean isOffProcessPoint = getClientModel().getProcessPoint().getProcessPointType() == ProcessPointType.Off;
		return isOffProcessPoint;
	}

	// === get/set === //
	public List<String> getAssociateNumbers() {		
		List<String> list = new ArrayList<String>(getAssociateNumberCache().keySet());
		if((frame.getUserId() != null) && !list.contains(frame.getUserId()))
		{
			cacheAssociateNumber(frame.getUserId().toString()); //update Cache		
		}
		list = new ArrayList<String>(getAssociateNumberCache().keySet());  //update list
		Collections.reverse(list);
		return list;
	}
	
	protected Map<String, String> getAssociateNumberCache() {
		
		return getClientModel().getAssociateNumberCache();
	}

	protected void setAssociateNumberCache(Map<String, String> associateNumberCache) {
		this.getClientModel().setAssociateNumberCache(associateNumberCache);
	}

	public void clearAssociateNumberCache() {
		getAssociateNumberCache().clear();
	}

	public String getClientId() {
		return getClientModel().getClientId();
	}

	public void setClientId(String clientId) {
		this.getClientModel().setClientId(clientId);
	}

	public String getProcessPointId() {
		return getClientModel().getProcessPointId();
	}

	public void setProcessPointId(String processPointId) {
		this.getClientModel().setProcessPointId(processPointId);
	}

	public ClientModel getClientModel() {
		return clientModel;
	}

	private void setClientModel(ClientModel clientModel) {
		this.clientModel = clientModel;
	}

	public boolean isDefectUpdatable(DefectResult repairResultData) {
		if (repairResultData == null) {
			return false;
		}
		DefectStatus defectStatus = repairResultData.getDefectStatus();
		if (defectStatus == null) {
			return true;
		}
		boolean updatable = defectStatus.isUpdatable() || repairResultData.isNewDefect();
		return updatable;
	}

	public boolean isProductTypeScrappable() {
		ProductType productType = getProductType();
		for(String nonScrappableProductType : getQicsPropertyBean().getNonScrappableProductTypes()) {
			if(productType.getProductName().equalsIgnoreCase(nonScrappableProductType) ||
					productType.name().equalsIgnoreCase(nonScrappableProductType))	
				return false;
		}
		return true;
	}

	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}

	public void setSubProductModel(String productType, ProductModel productModel) {
		this.subProductModel.put(productType, productModel);
	}

	public ProductModel getSubProductModel(String productType) {
		if (!subProductModel.containsKey(productType))
			setSubProductModel(productType, new ProductModel());
		return subProductModel.get(productType);
	}

	public boolean isProductCheckTypesDefined() {
		boolean checkTypesExist = getProductCheckTypes() != null && getProductCheckTypes().length > 0;
		boolean warnCheckTypesExist = getProductWarnCheckTypes() != null && getProductWarnCheckTypes().length > 0;
		return checkTypesExist || warnCheckTypesExist;
	}

	public Map<String, Object> getProductPreCheckResults() {
		Map<String, Object> allResults = new LinkedHashMap<String, Object>();
		Map<String, Object> productChecks = getProductModel().getProductPreCheckResults();
		if (productChecks != null) {
			allResults.putAll(productChecks);
		}
		return allResults;
	}

	public Map<String, Object> getProductItemCheckResults() {
		Map<String, Object> allResults = new LinkedHashMap<String, Object>();
		Map<String, Object> productChecks = getProductModel().getProductItemCheckResults();
		if (productChecks != null) {
			allResults.putAll(productChecks);
		}
		checkIfUpdatedOutstandingDefects(allResults);
		checkIfNewDefectsHaveFiringFlag(allResults);
		return allResults;
	}

	public Map<String, Object> getProductWarnCheckResults() {
		Map<String, Object> allResults = new LinkedHashMap<String, Object>();
		Map<String, Object> productChecks = getProductModel().getProductWarnCheckResults();
		if (productChecks != null) {
			allResults.putAll(productChecks);
		}
		return allResults;
	}

	protected Map<String, Object> checkIfUpdatedOutstandingDefects(Map<String, Object> allResults) {
		String key = ProductCheckType.OUTSTANDING_DEFECTS_CHECK.toString();
		String[] productCheckTypes = getProductCheckTypes();
		if (Arrays.asList(productCheckTypes).contains(key)) {
			List<DefectResult> outstandingDefects = getProductModel().getOutstandingDefects();
			if (outstandingDefects.isEmpty()) {
				allResults.remove(key);
			} else {
				allResults.put(key, translateDefect(outstandingDefects));
			}
		}
		return allResults;
	}

	protected Map<String, Object> checkIfNewDefectsHaveFiringFlag(Map<String, Object> allResults) {
		String key = ProductCheckType.ENGINE_FIRING_TEST_CHECK.toString();
		String[] productCheckTypes = getProductCheckTypes();
		if (Arrays.asList(productCheckTypes).contains(key)) {
			Object firingFlag = allResults.get(key);
			if (Boolean.TRUE.equals(firingFlag)) {
				return allResults;
			} else {
				List<DefectResult> newDefects = getProductModel().getNewDefects();
				if (isDefectsRequireEngineFiring(newDefects)) {
					allResults.put(key, true);
				}
			}
		}
		return allResults;
	}

	protected boolean isDefectsRequireEngineFiring(List<DefectResult> defects) {
		if (defects == null || defects.isEmpty()) {
			return false;
		}
		for (DefectResult defect : defects) {
			if (defect != null) {
				DefectDescription defectDescription = selectDefectDescription(defect);
				if (defectDescription != null && defectDescription.getEngineFiringFlag())
					return true;
			}
		}
		return false;
	}

	protected List<String> translateDefect(List<DefectResult> defects) {

		List<String> list = new ArrayList<String>();
		for (DefectResult defect : defects) {
			if (defect == null) {
				continue;
			}
			list.add(defect.toShortString());
		}
		return list;
	}

	public QicsClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(QicsClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	public QicsPropertyBean getQicsPropertyBean() {
		return clientConfig.getQicsPropertyBean();
	}

	public boolean isProductProcessable() {
		return getProductModel().isProductProcessable();
	}
	
	public boolean isProductScrap() {
		return getProductModel().isProductScrapped();
	}

	public void validateProduct() {

		BaseProduct product = getProductModel().getProduct();
		boolean productProcessable = isProductProcessable(product, false);

		if (!productProcessable) {
			getProductModel().setProductProcessable(false);
			return;
		}

		BaseProduct ownerProduct = getProductModel().getOwnerProduct();
		if (ownerProduct != null) {
			boolean ownerProductProcessable = isProductProcessable(ownerProduct, true);
			if (!ownerProductProcessable) {
				getProductModel().setProductProcessable(false);
				return;
			}
		}
		getProductModel().setProductProcessable(true);
	}

	public boolean isProductProcessable(BaseProduct product, boolean isOwner) {

		String message = (isOwner ? "Owner product " : "") +
				         product.getProductType().getProductName() + " " +
				         product.getProductId() + " ";
		
		boolean isProductScrapped = product.isScrapStatus() || product.isPreheatScrapStatus();
		if(isProductScrapped){
			getProductModel().setScrap(true);
			String scrapTypeStr = product.isScrapStatus() ? DefectStatus.SCRAP.getName() : DefectStatus.PREHEAT_SCRAP.getName(); 
			ExceptionalOut exceptionalOut = ServiceFactory.getDao(ExceptionalOutDao.class).findMostRecentByProductId(product.getProductId());
			String msg = message + " is already in status: " + scrapTypeStr;
			if(exceptionalOut == null){
				frame.setDelayedErrorMessage(msg);
			}else{
				frame.setDelayedErrorMessage(msg + ", detail: " + exceptionalOut.getExceptionalOutComment());
			}
			return false;
		}
		
		String[] productCheckTypes = getProductCheckTypes(getQicsPropertyBean().getProductNotProcessableCheckTypes());
		Map<String, Object> checkResults = submitCheckProductState(productCheckTypes);
		if (checkResults != null && !checkResults.isEmpty()) {
			String str = ProductCheckUtil.formatTxt(checkResults);
			String msg = String.format("%s is not processable : %s%s", message, System.getProperty("line.separator"), str);
			frame.setDelayedErrorMessage(msg);
			return false;
		}
		return true;
	}
	
	public boolean isRefreshProductPreCheckResults() {
		return refreshProductPreCheckResults;
	}

	public void setRefreshProductPreCheckResults(boolean refreshProductPreCheckResults) {
		this.refreshProductPreCheckResults = refreshProductPreCheckResults;
	}

	public boolean isRefreshProductCheckResults() {
		return refreshProductCheckResults;
	}

	public void setRefreshProductCheckResults(boolean refreshProductCheckResults) {
		this.refreshProductCheckResults = refreshProductCheckResults;
	}

	public boolean showPinCheckRequiredMessage() {
		if (getCheckOneOffLimit() == -1) return false;
		if (checkOneOff.equals(getCheckOneOffLimit() - 1))
			checkOneOff = -1;
		return checkOneOff == -1;
	}

	public ProductTypeData getProductTypeData() {
		return frame.getApplicationContext().getProductTypeData();
	}

	public ProductType getProductType() {
		return frame.getProductType();
	}

	/**
	 * tracking current product to a processPoint id and call broadcast
	 * @param processPointId
	 */
	public void track(String processPointId) {
		if (processPointId == null) {
			return;
		}
		getService(TrackingService.class).track(getProductModel().getProduct(), processPointId);
	}

	public void trackOffProcessPoint() {
		track(getClientConfig().getForwardProcessPointId());
	}
	
	public boolean sendMissionShippingTransaction(String url, BaseProduct product, String processPoint){
		return getService(IMissionShippingTransactionService.class).sendMission50AShippingTransaction(url, product, processPoint);
	}

	public void trackAltOffProcessPoint() {
		track(getClientConfig().getAltForwardProcessPointId());
	}

	public List<? extends ProductHistory> selectProductHistory() {
		return ProductTypeUtil.getProductHistoryDao(getProductType()).findAllByProductId(getProductModel().getProduct().getProductId());
	}

	public BaseProduct findProduct(String productId) {
		return getProductDao().findBySn(productId, getProductType());
	}

	public BaseProduct findOwnerProduct(BaseProduct product) {
		String ownerProductId = product.getOwnerProductId();
		ProductType ownerProductType = getProductTypeData().getOwnerProductType();
		return StringUtils.isEmpty(ownerProductId)  || ownerProductType == null ? 
				null : ProductTypeUtil.getProductDao(ownerProductType).findByKey(ownerProductId); 
	}

	public int updateDunnage(String productId, String dunnageNumber, int dunnageCapacity) {
		return ProductTypeUtil.getProductDao(getProductType()).updateDunnage(productId, dunnageNumber, dunnageCapacity);
	}

	protected ProductDao<?> getProductDao() {
		return ProductTypeUtil.getProductDao(getProductType());
	}

	public Integer getDunnageCartQuantity() {
		if (ProductType.BLOCK.equals(getProductType()))
			return getQicsPropertyBean().getBlockDunnageCartQuantity();
		if (ProductType.HEAD.equals(getProductType()))
			return getQicsPropertyBean().getHeadDunnageCartQuantity();

		return getQicsPropertyBean().getDunnageCartQuantity();
	}

	public long selectCountByDunnage(String dunnage) {
		return getProductDao().countByDunnage(dunnage);
	}

	public List<IPPTag> selectIppHistory() {

		String productId = getProductModel().getProduct().getProductId();
		List<IPPTag> ippTagList = getDao(IPPTagDao.class).findAllByProductId(productId);

		return ippTagList;
	}

	public void submitRepairAll() {
		List<DefectResult> defects = getProductModel().getOutstandingDefects();
		for (DefectResult defect : defects) {
			defect.setDefectStatus(DefectStatus.REPAIRED);
			DefaultQicsPropertyBean qicsProperty = PropertyService.getPropertyBean(DefaultQicsPropertyBean.class);
			if (qicsProperty.isOutstandingFlagChangable()) {
				defect.setOutstandingFlag((short) 0);
			}
			if (!defect.isNewDefect()) {
				getProductModel().addUpdatedDefect(defect);
			}

		}
	}

	public String selectLastDunnageNumber() {
		String machineId = getQicsPropertyBean().getMachineId();
		return selectLastDunnageNumber(machineId, null);
	}

	public String selectLastDunnageNumber(String machineId, Date productionDate) {
		StringBuilder sb = new StringBuilder();
		sb.append(machineId);
		if (productionDate != null) {
			sb.append(DunnageUtils.getDateFormat().format(productionDate));
		}
		sb.append("%");

		List<Map<String, Object>> list = getProductDao().selectDunnageInfo(sb.toString(), 1);
		String dunnageNumber = null;
		if (list != null && !list.isEmpty()) {
			Map<String, Object> row = list.get(0);
			Object o = row.get("dunnage");
			dunnageNumber = (String) o;
		}
		return dunnageNumber;
	}

	public String generateDunnageNumber() {
		String machineId = getQicsPropertyBean().getMachineId();
		Date productionDate = getProductionDate();
		int sequence = 1;
		String lastDunnageNumber = selectLastDunnageNumber(machineId, productionDate);
		if (lastDunnageNumber != null) {
			Integer seq = DunnageUtils.parseSequence(lastDunnageNumber);
			if (seq != null) {
				sequence = seq + 1;
			}
		}
		String number = DunnageUtils.format(machineId, productionDate, sequence);
		return number;
	}
	
	public void printDunnage(List<? extends BaseProduct> products) {
		printDunnage(getClientModel().getDunnageNumber(), products);
	}	

	public void printDunnage(String dunnageNumber, List<? extends BaseProduct> products) {
		DataContainer dc = new DefaultDataContainer();
		ProductType productType = getProductType();
		String productTypeName = productType.getProductName();
		dc.put(DataContainerTag.DUNNAGE_NUMBER, dunnageNumber);
		dc.put(DataContainerTag.PRODUCT_TYPE_NAME, productTypeName);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, "*");
		dc.put(DataContainerTag.DATA_SOURCE, products);
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		for (BroadcastDestination bd : getClientModel().getDunnagePrinters()) {
			getService(BroadcastService.class).broadcast(bd.getId().getProcessPointId(), bd.getId().getSequenceNumber(), dc);
		}
	}

	public Date getProductionDate() {
		Date productionDate = null;
		DailyDepartmentSchedule schedule = getClientModel().getCurrentSchedule();
		if (schedule != null && schedule.getId() != null) {
			productionDate = schedule.getId().getProductionDate();
		}

		if (productionDate != null) {
			return productionDate;
		}
		getLogger().warn("Generate DunnageNumber - DailyDepartmentSchedule is null, will use current date as ProductionDate");
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		productionDate = new Date(cal.getTime().getTime());
		return productionDate;
	}

	// === data api === //
	public List<DefectActualProblem> selectActualProblems(DefectResult defectResult) throws SystemException {

		List<DefectActualProblem> items = getDao(DefectActualProblemDao.class).findAllByDefectResult(defectResult.getId().getInspectionPartName(),defectResult.getId().getDefectTypeName(),defectResult.getId().getSecondaryPartName());
		DefectActualProblem defaultActualProblem = new DefectActualProblem();
		DefectActualProblemId id = new DefectActualProblemId();
		id.setActualProblemName(defectResult.getId().getDefectTypeName());
		id.setInspectionPartName(defectResult.getId().getInspectionPartName());
		defaultActualProblem.setId(id);
		items.add(defaultActualProblem);
		return items;

	}

	public List<DefectRepairMethod> selectRepairMethods(DefectActualProblem problem) throws SystemException {
		List<DefectRepairMethod> allRepairMethods = null;
		try {
			allRepairMethods = getDao(DefectRepairMethodDao.class).findAllByProblem(problem.getId().getModelCode(),problem.getId().getInspectionPartName(),problem.getId().getDefectTypeName(),problem.getId().getSecondaryPartName());
			List<DefectRepairMethod> defaultList = findProcessPointRepairMethods();
			if (defaultList.isEmpty())
				defaultList = findDepartmentDefaultRepairMethods();

			allRepairMethods.addAll(defaultList);

		} catch (Exception e) {
			String msgId = "QICS0102";
//			log.logTrace(msgId, e + " in " + e.getStackTrace()[0] + ", " + problem, getClass().getName(), "selectRepairMethods");
			throw new SystemException(msgId);
		}

		return new SortedArrayList<DefectRepairMethod>(allRepairMethods, "getRepairMethodName");

	}

	private List<DefectRepairMethod> findProcessPointRepairMethods() {

		List<DefectRepairMethod> repairMethods = new ArrayList<DefectRepairMethod>();
		String property = PropertyService.getProperty(getProcessPointId(), "DEFECT_REPAIR_METHODS", "");
		if(StringUtils.isEmpty(property)) return repairMethods;
		for (String name : property.split(Delimiter.COMMA)) {
			if (name == null || name.trim().length() == 0) continue;
			DefectRepairMethod repairMethod = constructDefectRepairMethod(name);
			if (repairMethod != null) {
				repairMethods.add(repairMethod);
			}
		}

		return repairMethods;

	}

	private List<DefectRepairMethod> findDepartmentDefaultRepairMethods() {

		List<DefectRepairMethod> repairMethods = new ArrayList<DefectRepairMethod>();

		DefaultQicsPropertyBean propertyBean = PropertyService.getPropertyBean(DefaultQicsPropertyBean.class);

		Map<String, String> defaultRepairMeghods = propertyBean.getDefectRepairMethods();
		if(defaultRepairMeghods!=null)
		{
			String key = getClientModel().getProcessPoint().getDivisionId() + "-" + getProductType();
			String property = defaultRepairMeghods.get(key);
			if(StringUtils.isEmpty(property)) return repairMethods;

			for (String name : property.split(Delimiter.COMMA)) {
				if(StringUtils.isEmpty(name)) continue;
				DefectRepairMethod repairMethod = constructDefectRepairMethod(name);
				if (repairMethod != null) repairMethods.add(repairMethod);
			}
		}

		return repairMethods;
	}

	protected DefectRepairMethod constructDefectRepairMethod(String params) {

		if (params == null) return null;
		String[] strs = params.split(Delimiter.COLON);
		if (strs.length == 0 || StringUtils.isEmpty(strs[0])) return null;

		DefectRepairMethod method = new DefectRepairMethod();
		DefectRepairMethodId id = new DefectRepairMethodId();
		id.setRepairMethodName(strs[0]);

		method.setId(id);
		method.setRepairTime(10);
		if (strs.length > 1) {
			String s = strs[1];
			if (!StringUtils.isEmpty(strs[0])) {
				try {
					int time = Integer.parseInt(s);
					method.setRepairTime(time);
				} catch (Exception e) {
					//
				}
			}
		}
		return method;
	}

	protected String[] getProductCheckTypes() {
		Map<String, String> checkTypesMap = getQicsPropertyBean().getProductCheckTypes();
		return getProductCheckTypes(checkTypesMap);
	}

	protected String[] getProductWarnCheckTypes() {
		Map<String, String> checkTypesMap = getQicsPropertyBean().getProductWarnCheckTypes();
		return getProductCheckTypes(checkTypesMap);
	}

	protected String[] getProductPreCheckTypes() {
		Map<String, String> checkTypesMap = getQicsPropertyBean().getProductPreCheckTypes();
		return getProductCheckTypes(checkTypesMap);
	}

	protected String[] getProductCheckTypes(Map<String, String> checkTypesMap) {
		String[] checkTypes = {};
		if (checkTypesMap == null || checkTypesMap.isEmpty()) {
			return checkTypes;
		}
		String key = null;
		if (getProductModel() != null && getProductModel().getProduct() != null) {
			key = getProductModel().getProduct().getModelCode();
		}

		if (StringUtils.isBlank(key) || !checkTypesMap.keySet().contains(key)) {
			key = "*";
		}
		checkTypes = getProductCheckTypes(checkTypesMap, key);
		return checkTypes;
	}

	protected String[] getProductCheckTypes(Map<String, String> checkTypesMap, String key) {
		String[] checkTypes = {};
		if (checkTypesMap == null || checkTypesMap.isEmpty()) {
			return checkTypes;
		}
		String str = checkTypesMap.get(key);
		if (StringUtils.isBlank(str)) {
			return checkTypes;
		}
		checkTypes = StringUtils.trim(str).split(Delimiter.COMMA);
		return checkTypes;
	}

	public List<Line> findEligibleRepairLines() {
		String divisionId = getDao(ProcessPointDao.class).findByKey(getProcessPointId()).getDivisionId();
		return getDao(LineDao.class).findEligibleRepairLines(divisionId);
	}

	public void performRepairTracking(String selectedLineId) {
		Line line = ServiceFactory.getDao(LineDao.class).findByKey(selectedLineId.trim());
		getService(TrackingService.class).track(getProductModel().getProduct(), line.getEntryProcessPointId());
	}

	public int getProductNumberLength() {
		try {
			return getProductTypeData().getProductNumberDefs().get(0).getLength();
		} catch (Exception e) {
			return 0; // Number def not defined.
		}

	}

	public void getSubProductDefects(String productType) {
		// setSubProductModel(productType,new ProductModel());
		String[] installedPartNames = getQicsPropertyBean().getSubProductPartNames(String[].class).get(productType);
		for (String installedPartName : installedPartNames) {
			try {
            	ProductBuildResult buildResult = ProductTypeUtil.getProductBuildResultDao(getProductModel().getProduct().getProductType())
                                                                                    .findById(getProductModel().getProductId(), installedPartName);
				String partSN = buildResult.getPartSerialNumber();

				BaseProduct installedProduct = findProduct(partSN, productType);
				if (installedProduct != null) {

					getSubProductModel(productType).setInputNumber(installedProduct.getProductId());

					getSubProductModel(productType).setProduct(installedProduct);

					getSubProductModel(productType).setOwnerProduct(findOwnerProduct(installedProduct));

					selectInspectionModels(installedProduct.getModelCode());

	                List<DefectResult> defectResults =
	                        getDao(DefectResultDao.class).findAllByProductId(installedProduct.getProductId());

					for (DefectResult defectResult : defectResults) {
						defectResult.setEntryStation(getApplicationName(defectResult.getId().getApplicationId()));
					}

					getSubProductModel(productType).addExistingDefects(defectResults);
				}
	        }catch(Exception e){}
		}
	}

	public BaseProduct findProduct(String productId, String productType) {
		return ProductTypeUtil.getProductDao(productType).findBySn(productId);
	}
	
	public void playScrapSound() {
		try {
			if (getQicsPropertyBean().isSoundEnabled()) {
				getAudioManager().playScrapSound();
			}
		} catch (Exception e) {
			getLogger().warn(e);
		}
	}

	public void playNgSound() {
		try {
			if (getQicsPropertyBean().isSoundEnabled()) {
				getAudioManager().playNGSound();
			}
		} catch (Exception e) {
			getLogger().warn(e);
		}
	}

	public void playOkSound() {
		try {
			if (getQicsPropertyBean().isSoundEnabled()) {
				getAudioManager().playOKSound();
			}
		} catch (Exception e) {
			getLogger().warn(e);
		}
	}

	public void playWarnSound() {
		try {
			if (getQicsPropertyBean().isSoundEnabled()) {
				getAudioManager().playWarnSound();
			}
		} catch (Exception e) {
			getLogger().warn(e);
		}
	}

	protected ClientAudioManager getAudioManager() {
		return audioManager;
	}
	
	public boolean isProductPreCheckResultsExist() {
		return getProductModel().getProductPreCheckResults() != null && !getProductModel().getProductPreCheckResults().isEmpty();
	}
	
	public boolean isProductWarnCheckResultsExist() {
		return getProductModel().getProductWarnCheckResults() != null && !getProductModel().getProductWarnCheckResults().isEmpty();
	}
	
	public void doDunnageUpdate(boolean newValue){
		doDunnageUpdate = newValue;
	}
	
	public boolean doDunnageUpdate(){
		return doDunnageUpdate;
	}
}
