package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.IService;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.datacollection.task.CollectorTask;
import com.honda.galc.service.datacollection.work.AfterProcess;
import com.honda.galc.service.datacollection.work.CollectorWork;
import com.honda.galc.service.datacollection.work.CustomTask;
import com.honda.galc.service.datacollection.work.DataCollection;
import com.honda.galc.service.datacollection.work.PersistenceWork;
import com.honda.galc.service.datacollection.work.PreProcess;
import com.honda.galc.service.datacollection.work.ProductStateCheck;
import com.honda.galc.service.datacollection.work.Script;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.ProductResultUtil;

/**
 * 
 * <h3>ProductDataCollectorBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductDataCollectorBase description </p>
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
 * <TD>P.Chou</TD>
 * <TD>May 3, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 3, 2011
 * @param <T>
 */

public abstract class ProductDataCollectorBase implements DataCollector, IService{

	@Autowired
	public QicsService qicsService;
	@Autowired
	public TrackingService trackingService;
	@Autowired
	public LotControlRuleDao lotControlRuleDao;
	@Autowired
	public InstalledPartDao installedPartDao;
	@Autowired
	public RequiredPartDao requiredPartDao;
	@Autowired
	public ProcessPointDao procesPointDao;
	@Autowired
	public MeasurementDao measurementDao;
	
	// add for op of mbpn product
	@Autowired
	public MbpnProductDao mbpnProductDao;

	protected DeviceFormatDao deviceFormatDao;

	protected List<LotControlRule> rules;

	CollectorWork collectorWork;

	protected HeadlessDataCollectionContext context = new HeadlessDataCollectionContext();

	protected String destinationProcessPointId;

	protected List<String> products = null;

	protected PartSpec currentPartSpec;
	
	private DataCollectionUtil util;
	
	List<String> errorMesList;

	public abstract void saveBuildResults();
	public abstract void loadLotControlRules();
	public abstract void collectData();
	
	public abstract void validate();

	protected static Long start;
	
	@Autowired 
	public HeadlessNaqService headlessNaqService; 


	public Device execute(Device device) {
		try {
			start = System.currentTimeMillis();	
			init(device);

			generateWorkFlow();

			if(context.isMultiProduct())
				collectDataMultiProduct();
			else
				collectDataSingleProduct();

		} catch (TaskException te) {
			handleException(te);
		} catch (Throwable e){
			handleException(e);
		}

		getLogger().debug("context:", context.toString());

		context.prepareReply(device);
		getLogger().info("replyDeviceData:", device.toReplyString());
		getLogger().debug("total process time:" + (System.currentTimeMillis() - start) + " ms.");
		return device;
	}



	/**
	 * Check if the headless client is defined for the process point
	 * @return
	 */
	public boolean isHeadlessClient() {
		Application application = ServiceFactory.getDao(ApplicationDao.class).findByKey(context.getProcessPointId());
		return application != null && !StringUtils.isEmpty(application.getScreenClass()) && !StringUtils.isEmpty(application.getScreenId());
	}

	protected void collectDataMultiProduct() {

		getLogger().info("start collect data for multiple product:", context.getProducts());
		for(String prodName : getProducts()){

			context.setProductName(prodName);

			collectDataSingleProduct();

		}

	}

	protected void collectDataSingleProduct() {
		try {
			getLogger().info("start collect data for:", StringUtils.isEmpty(context.getProductName())? "Single Product" : context.getProductName());
			context.dataCollectionComplete( LineSideContainerValue.COMPLETE);

			collectorWork.execute();

			if(StringUtils.isEmpty(context.getProductName()))
				getLogger().info("finish collect data for:", context.getProductName());

		} catch (TaskException te) {
			context.dataCollectionComplete(LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(te, "Exception when collect data for ", this.getClass().getSimpleName());
		} catch (Throwable e){
			context.dataCollectionComplete(LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(e, "Exception when collect data for ", this.getClass().getSimpleName());
		}

		if(context.containsKey(TagNames.EXCEPTION.name())) context.dataCollectionComplete( LineSideContainerValue.NOT_COMPLETE);


	}
	
	protected void generateWorkFlow() throws Exception {

		// - pre-processing
		addwork(new PreProcess(context, this));
		if(context.hasPreScript()) addwork(createScriptWork(getProperty().getPreScripts()));

		// - data collection 
		addwork(new DataCollection(context, this)); 

		// - custom processing functions
		if(context.hasChecker()) addwork(new ProductStateCheck(context, this));
		if(!StringUtils.isEmpty(getProperty().getTask())) addwork(new CustomTask(context, this));
		if(context.hasScript())  addwork(createScriptWork(getProperty().getScripts()));

		// - persistence work
		addPersistenceWork();

		// - after processing
		addwork(new AfterProcess(context, this));

	}
	
	protected void addPersistenceWork() {
		addwork(new PersistenceWork(context, this));
	}

	protected void addwork(CollectorWork work){
		if(collectorWork == null) {
			collectorWork = work;
			return;
		} else {
			CollectorWork currentWork = collectorWork;
			while(currentWork.getNext() != null)
				currentWork = currentWork.getNext();
			currentWork.setNext(work);
		}
	}

	private Script createScriptWork(Map<String, String> scripts) {
		return new Script(context, this, scripts);

	}

	protected void handleException(Throwable e) {
		if(e instanceof TaskException){
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(e, "Task Exception when collect data for ", this.getClass().getSimpleName());
		} else {
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			context.put(TagNames.EXCEPTION.name(), e.getCause() + ":" + e.getMessage());
			getLogger().error(e, "Exception when collect data for ", this.getClass().getSimpleName());
		}
	}

	public DataContainer execute(DataContainer data){
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		device.populate(data);
		return execute(device).toReplyDataContainer(getProperty().isStringInDataContainer());
	}

	public DataContainer execute(DefaultDataContainer data) {
		DataContainer dt = data;
		return execute(dt);
	}


	public void track() {
		if (context.getProduct() == null) {
			getLogger().info(this.getClass().getSimpleName(), " tracking product is null.");
			if (StringUtils.isNotBlank(context.getTagForCurrentProduct(TagNames.DEVICE_ID.name()))) {
				getTrackingService().track(context.getProductType(), context.getProductId(),
						context.getProcessPointId(), (String) context.getTagValue(TagNames.DEVICE_ID.name()));
			} else {
				getTrackingService().track(context.getProductType(), context.getProductId(),
						context.getProcessPointId());
			}
			performAdditionalTracking(context.getProductType(), context.getProductId(), context.getProcessPointId());
		} else {
			ProductHistory productHistory = null;
			ProductType productType = ProductTypeCatalog.getProductType(context.getProperty().getProductType());

			productHistory = ProductTypeUtil.createProductHistory(context.getProduct().getProductId(),
					context.getTrackingProcessPointId(), productType);

			if (productHistory != null) {
				productHistory.setAssociateNo(context.getAssociateNo());
				if (StringUtils.isNotBlank(context.getTagForCurrentProduct(TagNames.DEVICE_ID.name()))) {
					productHistory.setDeviceId((String) context.getTagValue(TagNames.DEVICE_ID.name()));
				}
				getTrackingService().track(productType, productHistory);
				performAdditionalTracking(context.getProduct().getProductType(), context.getProduct().getProductId(),
						context.getTrackingProcessPointId());
			} else { 
				Logger.getLogger().warn("WARN:", " skipped tracking for unknown product Id.");
			}
			
			logPerformance("after tracking:");
		}
	}

	protected void performAdditionalTracking(ProductType productType, String productId, String trackingPP) {
		// additional tracking 
		// RGALCDEV-7008
		// In validator, forward tracking based on checker result otherwise it works the same as before;
		String forwardToProcessPointId = getForwardToProcessPointId(trackingPP);
		if (StringUtils.isNotBlank(forwardToProcessPointId)) {
			getTrackingService().track(productType, productId, forwardToProcessPointId);
		}	
	}
	
	private boolean getForwardTrackingStatus(String statusName) {
		
		return TagNames.OVERALL_CHECK_RESULT.name().equals(statusName) ? isOverAllStatusOk() : (boolean)context.getBoolean(statusName, false);
	}

	protected String getForwardToProcessPointId(String processPointId) {
		TrackingPropertyBean property = PropertyService.getPropertyBean(TrackingPropertyBean.class, processPointId);
		boolean forwardTrackingStatus = getForwardTrackingStatus(property.getForwardTrackingStatus());
		if (forwardTrackingStatus) {
			return property.getTrackingProcessPointIdOnSuccess();
		} else {
			return property.getTrackingProcessPointIdOnFailure();
		}
	}

	protected String getForwardToProcessPointId(String processPointId, boolean success) {
		TrackingPropertyBean property = PropertyService.getPropertyBean(TrackingPropertyBean.class, processPointId);
		if (success) {
			return property.getTrackingProcessPointIdOnSuccess();
		} else {
			return property.getTrackingProcessPointIdOnFailure();
		}
	}

	protected boolean isOverAllStatusOk() {
		String overAllStatusName = getProperty().getOverallStatusPartName();
		if (!StringUtils.isBlank(overAllStatusName)) {
			ProductBuildResult overAllBuildResult = context.getBuildResult(overAllStatusName.trim());
			if (overAllBuildResult != null) {
				return InstalledPartStatus.OK.equals(overAllBuildResult.getInstalledPartStatus());
			} else {
				String msg = String.format("Overall Status Name property %s is defined but not received from PLC.", overAllStatusName);
				getLogger().error(msg);
				return false;
			}
		}
		for (ProductBuildResult br : context.getBuildResults()) {
			if (!InstalledPartStatus.OK.equals(br.getInstalledPartStatus())) {
				return false;
			}
		}
		return true;
	}

	public void updateQics() {
		long start = System.currentTimeMillis();
		boolean qicsUpdateStatus = doQicsUpdate();

		//add QICS Update status - the same way as data collection complete
		context.put(LineSideContainerTag.QICS_UPDATE_STATUS, 
				qicsUpdateStatus ? LineSideContainerValue.COMPLETE : LineSideContainerValue.NOT_COMPLETE);

		getLogger().info("Completed updateQics. time(ms):" + (System.currentTimeMillis() - start));
		logPerformance("after saveResult:");
	}


	public boolean doQicsUpdate()
	{
		return qicsService.update(context.getProcessPointId(), context.getProductType(), getBuildResults()); 
	}

	protected void logPerformance(String tag) {
		if(getProperty().isLogPerformance()){
			getLogger().info("performance - ", tag + (start - System.currentTimeMillis()) );
			start = System.currentTimeMillis();
		}
	}

	public void updateMeasurements() {
		List<? extends ProductBuildResult> buildResults = getBuildResults();
		for(ProductBuildResult result : buildResults){
			if(result.getInstalledPartStatus() == InstalledPartStatus.OK){
				List<Measurement> allMeasurements = measurementDao.findAll(context.getProductId(), result.getPartName());
				List<Measurement> repaired = new ArrayList<Measurement>();
				if(allMeasurements != null && allMeasurements.size() > 0 ){
					for(Measurement measurement : allMeasurements){
						if(measurement.getMeasurementStatus() != MeasurementStatus.OK)
						{
							measurement.setMeasurementStatus(MeasurementStatus.OK);
							measurement.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
							measurement.setMeasurementAngle(0);
							measurement.setMeasurementValue(0);
							
							repaired.add(measurement);
						}
					}
				}
				
				if(repaired.size() > 0)
					measurementDao.saveAll(repaired);
			}
		}
		
	}
	
	protected void init(Device device) {
		context.setDevice(device);	
		//@KM set associate No to store in GAL185TBX
		if(context.containsKey("ASSOCIATE_NO") && context.get("ASSOCIATE_NO") != null)  {
			context.setAssociateNo(context.get("ASSOCIATE_NO").toString());
		}
		else context.setAssociateNo("");
		if(getProperty().isInlineRepair() && getProperty().isPdaRecipeDownload()){
			if(null!=context.get("PRODUCT_TYPE"))
				context.setProductType(ProductType.getType(context.get("PRODUCT_TYPE").toString()));
		}
	}

	public String getTag(String tag) {
		return StringUtils.isEmpty(getProductName()) ? tag : 
			getProductName() + "." + tag;
	}

	public Object getValue(String tag){
		return context.get(getTag(tag));
	}


	//-----Utility Functions -----------
	public boolean isMissingRequiredPart() {
		return !(Boolean)context.get(TagNames.REQUIRED_PART.name());
	}

	public void setMissingRequiredPart(boolean missingRequiredPart) {
		context.put(TagNames.REQUIRED_PART.name(), !missingRequiredPart);
	}

	public Map<String,Object> checkProduct(String[] productCheckTypes) {
		return ProductCheckUtil.check(context.getProduct(), getProcesPointDao().findByKey(context.getProcessPointId()),
				productCheckTypes);

	}

	public Map<String,Object> checkProduct(String[] productCheckTypes, String[] installedParts) {
		return ProductCheckUtil.check(context.getProduct(), getProcesPointDao().findByKey(context.getProcessPointId()),
				productCheckTypes, Arrays.asList(installedParts));

	}

	public Object executeSqlQuery(String sql) {

		Object result = null;
		String sqlStr = DataContainerUtil.makeSQL(sql, context);
		getLogger().debug("excuteSql:" + sqlStr);
		result = getDeviceFormatDao().executeSqlQuery(sqlStr);
		if(result == null){
			getLogger().warn("WARN: null value from tag value sql query.");
			result = "null"; //can't insert null into data container, so set to null
		}

		return result;
	}

	public void executeSqlUpdate(String sql) {
		String sqlStr = DataContainerUtil.makeSQL(sql, context);
		getLogger().debug("excuteSql:" + sqlStr);
		getDeviceFormatDao().executeSqlUpdate(sqlStr);
	}

	public void refreshLotControlRuleCache(String processPointId) {
		LotControlRuleCache.removeLotCtrolRules(processPointId);
		getLogger().info("Lot Control Rule for process point:", processPointId, 
				" was removed from cache. url:", HttpServiceProvider.url);

	}

	public HeadLessPropertyBean getProperty() {
		return context.getProperty();
	}

	public String getProductName() {
		return context.getProductName();
	}


	protected String toString(Collection<String> collection) {
		StringBuilder sb = new StringBuilder();
		for(String str : collection){
			if(sb.length() > 0) sb.append(",");
			sb.append(str);
		}

		return sb.toString();

	}

	protected void checkContext(Map<Object, Object> context) {
		if(context.containsKey(TagNames.EXCEPTION.name()))
			throw new TaskException((String)context.get(TagNames.EXCEPTION.name()));

		if(context.containsKey(TagNames.MESSAGE.name()))
			getLogger().info((String)context.get(TagNames.MESSAGE.name()));
	}

	public void logContext(){
		if(context.containsKey(TagNames.MESSAGE.name()))
			getLogger().info((String)context.get(TagNames.MESSAGE.name()));

		if(context.containsKey(TagNames.EXCEPTION.name()))
			getLogger().info((String)context.get(TagNames.EXCEPTION.name()));
	}


	//--- functions to maintain product sequence by headless data collection
	private List<String> getProductList() {
		return CommonUtil.splitStringList(getProperty().getProducts());
	}

	public List<? extends ProductBuildResult> getBuildResults(String product) {
		return context.getBuildResults(product);
	}

	public ProductBuildResult getBuildResult(String partName){
		for(ProductBuildResult result : getBuildResults())
		{
			if(partName.equals(result.getPartName().trim()))
				return result;
		}

		return null;
	}


	// ------------ Getters && Setters -------------------
	public BaseProduct getProduct() {
		return context.getProduct();
	}

	public BaseProductSpec getProductSpec() {
		return (BaseProductSpec)context.getProductSpec();
	}

	public void setProductSpec(ProductSpec productSpec) {
		context.setProductSpec(productSpec);
	}

	public List<LotControlRule> getRules() {
		return rules;
	}

	public void setRules(List<LotControlRule> rules) {
		this.rules = rules;
		context.setLotControlRules(rules);
	}

	public LotControlRuleDao getLotControlRuleDao() {
		if(lotControlRuleDao == null)
			lotControlRuleDao = getDao(LotControlRuleDao.class);

		return lotControlRuleDao;
	}


	public InstalledPartDao getInstalledPartDao() {
		if(installedPartDao == null)
			installedPartDao = getDao(InstalledPartDao.class);

		return installedPartDao;
	}

	public RequiredPartDao getRequiredPartDao() {
		if(requiredPartDao == null)
			requiredPartDao = getDao(RequiredPartDao.class);

		return requiredPartDao;
	}

	public ProcessPointDao getProcesPointDao() {
		if(procesPointDao == null)
			procesPointDao = getDao(ProcessPointDao.class);

		return procesPointDao;
	}

	public DeviceFormatDao getDeviceFormatDao() {
		if(deviceFormatDao == null)
			deviceFormatDao = getDao(DeviceFormatDao.class);

		return deviceFormatDao;
	}

	public MbpnProductDao getMbpnProductDao() {
		if(mbpnProductDao == null)
			mbpnProductDao = getDao(MbpnProductDao.class);
		return mbpnProductDao;
	}
	
	public Logger getLogger() {
		return context.getLogger();

	}

	public Map<Object, Object> getContext() {
		return context;
	}

	public String getShortServerName() {
		return ServiceUtil.getShortServerName();
	}



	public List<String> getProducts() {
		if(products == null)
			products = getProductList();

		return products;
	}

	public List<? extends ProductBuildResult> getBuildResults() {
		return context.getBuildResults();
	}

	public String getValidProudctId() {
		return context.getValidProductId();
	}

	public String getProcessPointId() {
		return context.getProcessPointId();
	}

	public String getDestinationProcessPointId() {
		return destinationProcessPointId;
	}

	public void setDestinationProcessPointId(String destinationProcessPointid) {
		this.destinationProcessPointId = destinationProcessPointid;
	}

	public List<CollectorTask> getTasks() {
		return getWork(CustomTask.class).getTasks();
	}

	@SuppressWarnings("unchecked")
	public <T extends CollectorWork> T getWork(Class<T> clazz){
		CollectorWork work = collectorWork;
		do{
			if(work == null) return null;
			if(work.getClass() == clazz)
				return (T)work;

			work = work.getNext();
		} while (work != null);

		return  null;
	}

	public CollectorWork getWork(String workName){
		CollectorWork work = collectorWork;
		do{
			if(work == null) return null;
			if(workName.equals(work.getName()))
				return work;

			work = work.getNext();
		} while (work != null);

		return  null;
	}

	public void setBuildResults(List<ProductBuildResult> buildResults) {
		context.setBuildResults(buildResults);
	}

	public IDeviceHelper getDeviceHelper() {
		return context.getDeviceHelper();
	}

	public QicsService getQicsService() {
		if(qicsService == null)
			qicsService = ServiceFactory.getService(QicsService.class);
		return qicsService;
	}

	public TrackingService getTrackingService() {
		if(trackingService == null)
			trackingService = getService(TrackingService.class);

		return trackingService;
	}

	@SuppressWarnings("unchecked")
	protected List<InstalledPart> getInstalledParts() {
		return (List<InstalledPart>)getBuildResults();
	}

	protected String getBulidResultLogString(List<? extends ProductBuildResult> prodBuidResults) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < prodBuidResults.size(); i++){
			sb.append(System.getProperty("line.separator"));
			sb.append(prodBuidResults.get(i).toString());
		}

		return sb.toString();
	}

	public InstalledPart getInstalledPart(String partName) {
		for(ProductBuildResult part : getBuildResults()){
			if(partName.equals(part.getPartName()))
				return (InstalledPart)part;
		}
		return null;
	}

	public boolean updateInstalledPartStatus(InstalledPart part) {
		for(Measurement measurement : part.getMeasurements()){
			if(measurement.getMeasurementStatus() != MeasurementStatus.OK){
				part.setInstalledPartStatus(InstalledPartStatus.NG);
			}
		}

		//this is the case there is no part Sn and all the measurement are OK
		if(part.getInstalledPartStatus() == InstalledPartStatus.BLANK)
			part.setInstalledPartStatus(InstalledPartStatus.OK);

		context.put(part.getPartName() + Delimiter.DOT + TagNames.STATUS.name(), part.getInstalledPartStatusId());

		return part.getInstalledPartStatus() == InstalledPartStatus.OK;
	}

	public List<LotControlRule> getLotControlRules() {
		if(rules == null) rules = context.getLotControlRules();
		return rules;
	}

	public boolean updateStatusForInstalledParts(){
		boolean overallStatus = true;
		for(InstalledPart p : getInstalledParts()){

			overallStatus &= updateInstalledPartStatus(p);
		}

		return overallStatus;
	}
	
	protected List<LotControlRule> getLotControlRuleFromCache(String processPointId) {
		List<LotControlRule> allRules = null;
		
		try {
			allRules = LotControlRuleCache.getOrLoadLotControlRule(
				getProperty().isDeviceDriven() ? null: (ProductSpec) context.getProductSpec(), processPointId);
		} catch (Exception e) {
			getLogger().error(e, " Excetpion to get Lot Control Rule on ", processPointId, " Product Spec:",context.getProductSpec().getProductSpecCode());
			context.put(TagNames.DATA_COLLECTION_COMPLETE, DataCollectionComplete.NG);
			return null;
		}
		
		return allRules;
	}
	
	protected List<InstalledPart> saveAllInstalledParts(List<InstalledPart> installedParts) {
		return ProductResultUtil.saveAll(context.getProcessPointId(), installedParts);
	}
	
	public DataCollectionUtil getUtil() {
		if(util == null)
			util = new DataCollectionUtil(context);
		return util;
	}
	
	public boolean checkRequiredPart() {
		try{
			List<String> missingRequiredParts = context.getProductCheckUtil().outstandingRequiredPartsCheck();
			if(missingRequiredParts == null || missingRequiredParts.size() == 0){
				getLogger().info("passed missing required part check!");
				return true;
			}

			getLogger().info("Missing Required Parts:", toString(missingRequiredParts), " for product:", 
					context.getProductId(), " at process point:", context.getProcessPointId());
			
		} catch (Exception e){
			Logger.getLogger().warn(e, "Exception was thrown when checking required part.");
		}
		
		return false;
	}
	
	/**
	 * Check if the QC Hold Flag defined and enabled
	 * @return
	 */
	public boolean isQCHold() {
		return context.getQCHold();
	}
	public void setQCHold(boolean qcHold) {
		context.setQCHold(qcHold);		
	}
	
	public int getHoldSource() {
		return context.getHoldSource();
	}
	
	public void filterUpdatePart() {
		String repairPartName = "";
		try{
			repairPartName = context.get(TagNames.PART_NAME.name()).toString();
		}catch(Exception e){
			putErrorMessage("PART_NAME tag not found.");
		}
		Iterator<LotControlRule> iterator = rules.iterator();
			
		while(iterator.hasNext()){
			if(!repairPartName.equalsIgnoreCase(iterator.next().getPartNameString()))
				iterator.remove();
		}
	}
	
	public void putErrorMessage(String message){
		if(errorMesList==null){
			errorMesList = new ArrayList<String>();
		}
		errorMesList.add(message);
		context.put(TagNames.ERROR_MESSAGE.name(), errorMesList);
	}

}
