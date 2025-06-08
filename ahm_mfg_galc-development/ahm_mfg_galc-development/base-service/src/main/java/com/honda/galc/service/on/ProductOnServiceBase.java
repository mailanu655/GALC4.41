package com.honda.galc.service.on;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.task.IHlServiceTask;
import com.honda.galc.service.datacollection.work.ProductStateCheck;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.CommonUtil;
/**
 * 
 * <h3>ProductOnBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductOnBase description </p>
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
 * <TD>Jan 24, 2013</TD>
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
 * @since Jan 24, 2013
 */
public abstract class ProductOnServiceBase {
	protected static final String NO_ERROR = "01";
	protected String processPointId = null;
	protected ProductOnHlPropertyBean propertyBean;
	protected Logger logger;
	protected ProductType productType;
	protected Map<Object, Object> context = new HeadlessDataCollectionContext();
	protected ProductDao<BaseProduct> productDao;
	protected String productName;
	protected BaseProduct product;
	protected PreProductionLot preProductionLot;
	protected String productId;
	
	protected TrackingService trackingService;
	private List<ProductNumberDef> productNumberDefs;
	protected BuildAttributeCache buildAttributeCache;
	protected Device device;
	
	@Autowired
	PreProductionLotDao preProductionLotDao;
	
	
	public ProductOnServiceBase() {
		super();
	}

	/**
	 * initialize recipe down load process
	 * 1. find out process point Id
	 * 2. validate request product Id
	 * @param device
	 */
	protected void init(Device device) {
		this.device = device;
		this.processPointId = StringUtils.trim(device.getIoProcessPointId());
		getLogger().info("collector received device data:", device.toInputString());
		
		context.clear();
		context.put(TagNames.PROCESS_POINT_ID.name(), processPointId);
		context.put(TagNames.ERROR_CODE.name(), NO_ERROR);//set default error code
		
	}
	
	protected void populateReply(Device device) {
		device.resetReplyFormats();

		for(DeviceFormat format: device.getReplyDeviceDataFormats()){
			
			Object value = getValueFromContext(getDataTagName(device, format));
			
			if(value != null) {
				if(format.getClass().isAssignableFrom(String.class)){
					
					//truncate to the required length
					// example would be Product Spec Code:BRNWAA900 * * will have Length:7 and truncate to BRNWAA9 
					if(value instanceof String){
						if(format.getLength() < value.toString().length())
							format.setValue(value.toString().substring(0, format.getLength()));
						else
							format.setValue(value.toString());
					} else if(value instanceof Float || value instanceof Double){
						//to compatible for the original Recipe definition e.g. 2 register for 4 chars to hold MIN/MAX
						//so for instance: 200.23 will truncate to 200
						int intValue = (int)Math.round((Double)value);
						format.setValue(String.valueOf(intValue));
					} else {
						format.setValue(value.toString());
					}
				} else 
					format.setValue(value);
				
			} else {
				//fill in default value if the data is not supplied from production spec 
				format.setValue(format.getDefaultValue());
			}
			
		}

	}

	private Object getValueFromContext(String contextKey) {
		if(contextKey == null){
			getLogger().info("WARN: context key is null!");
			return null;
		}
		
		Object value = context.get(contextKey);
		
		if(value != null){
			//do substring only if configured to do so
			String subStringDetail = getSubString(contextKey);
			if(!StringUtils.isEmpty(subStringDetail)){
				String[] split = subStringDetail.split(Delimiter.COMMA);
				
				//If sub-string is configured, then the return object is always a String
				//Here, we depend on the user to for the right usage of the substring functions.
				value = value.toString().substring(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			} 
		}
		
		
		getLogger().info("data from context - " + contextKey + " : " + value);
		return value;
	}
	
	protected void processTask() {
		String tasks = getPropertyBean().getTasks();
		IHlServiceTask task = null;
		if(StringUtils.isEmpty(tasks)){
			
			getLogger().info("No data collector task.");
			return;
		} else {
			for(String taskName : CommonUtil.splitStringList(tasks)){
				task = ServiceUtil.createTask(taskName, context, processPointId);
				task.execute();
			}
		}
		
		getLogger().info("after process-task:");
		
	}
	

	private String getSubString(String dataTagName) {
		String tagKey = dataTagName.contains(Delimiter.DOT)? dataTagName.substring(dataTagName.indexOf(Delimiter.DOT)):dataTagName;
		Map<String, String> subString = getPropertyBean().getSubString();
		return subString == null ? null : subString.get(tagKey);
	}

	private String getDataTagName(Device device, DeviceFormat format) {
		return device.isDataFromTagValue() ? format.getTagValue() : format.getTag();
	}
	
	protected void updateLastPassingProcessPoint(BaseProduct product) {
		// Need to update the last passing process point anyway
		product.setLastPassingProcessPointId(getProcessPointId());
		getProductDao().updateTrackingAttributes(product);
	}
	
	protected void invokeNotification(PreProductionLot preProdLot) {
		ServiceFactory.getNotificationService(IProductOnNotification.class, processPointId).
			execute(preProdLot.getProductionLot(), getPropertyBean().getProcessLocation(), preProdLot.getStampedCount());
		getLogger().info("IProductOnNotification initiated for process point: " + processPointId);
	}
	
	protected PreProductionLot getPreProductionLot() {
		if(preProductionLot == null || !preProductionLot.getProductionLot().equals(product.getProductionLot()))
			if(product != null && !StringUtils.isEmpty(product.getProductionLot())) 
				preProductionLot = getPreProductionLotDao().findByKey(product.getProductionLot());
		
		return preProductionLot;
	}
	
	protected boolean isMultipleProduct() {
		return !StringUtils.isEmpty(getPropertyBean().getProducts());
	}

		
	//  ----- getter & setter ---------

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}


	public ProductOnHlPropertyBean getPropertyBean() {
		if(propertyBean == null)
			propertyBean = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class, getProcessPointId());
		
		return propertyBean;
	}


	public void setPropertyBean(ProductOnHlPropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}


	public Logger getLogger() {
		if(logger == null)
			logger = ServiceUtil.getLogger(getProcessPointId()); 
		
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public ProductType getProductType() {
		if(productType == null)
			productType = ProductTypeCatalog.getProductType(getPropertyBean().getProductType());
		
		return productType;
	}


	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	@SuppressWarnings("unchecked")
	public ProductDao<BaseProduct> getProductDao() {
		if(productDao == null)
			productDao = (ProductDao<BaseProduct>)ProductTypeUtil.getProductDao(getPropertyBean().getProductType());
		
		return productDao;
	}
	

	protected String getFullTag(String tag) {
		return StringUtils.isEmpty(productName) ? tag : productName + Delimiter.DOT + tag;
	}
	

	protected String getTrackingProcessPointId() {
		return (StringUtils.isEmpty(getPropertyBean().getTrackingProcessPointId()) ? 
				getProcessPointId() : getPropertyBean().getTrackingProcessPointId());
	}
	
	public PreProductionLotDao getPreProductionLotDao() {
		if(preProductionLotDao == null)
			preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		
		return preProductionLotDao;
	}
	
	protected String getProductionLot() {
		return product.getProductionLot();
	}
	
	protected void contextPut(String tag, Object value) {
		context.put(getFullTag(tag), value);
		
	}
	
	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		
		if(device.getDeviceType() == DeviceType.VENDER_APP)
			return executeCumstom(data, device);
		else {
			device.populate(data);
			return execute(device).toReplyDataContainer(true);
		}
	}
	
	public Device execute(Device device) {
		init(device);
		
		doExecute();
		
		populateReply(device);
		return device;
		
	}

	private DataContainer executeCumstom(DataContainer data, Device device) {
		this.init(device);
		init(data);
		
		doExecute();
		
		populateReply(device);
		return device.toReplyDataContainer(true);
	}

	private void init(DataContainer data) {
		context.putAll(data);
	}

	
	private void doExecute() {
		try{
			if(isMultipleProduct())
				processMultipleProduct();
			else
				processSingleProduct();
			
		} catch(Throwable te){
			getLogger().error(te, " Exception to collect data for", this.getClass().getSimpleName());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}

		if(getPropertyBean().isCollectData()) {
			DataContainer dc = new DefaultDataContainer();
			dc.putAll(context);
			ServiceFactory.getService(DataCollectionService.class).execute(dc);
		}
	}
	
	

	protected void processMultipleProduct() {
		getLogger().info("start to process multiple product: ", getPropertyBean().getProducts());
		for(String prodName : CommonUtil.splitStringList(getPropertyBean().getProducts())){
			productName = prodName;
			productId = null;			
			processSingleProduct();
		}
		
	}
	
	protected void track() {
		if(product == null)
			getLogger().warn(this.getClass().getSimpleName(), " product is null and skipped tracking.");
		else 
			getTrackingService().track(product, getTrackingProcessPointId());
		
	}

	protected TrackingService getTrackingService() {
		if(trackingService == null)
			trackingService = getService(TrackingService.class);

		return trackingService;
	}
	
	protected void processSingleProduct(){};

	protected void invokeNotification(String productId, String msg, MessageType mtype) {
		ServiceFactory.getNotificationService(IProductOnNotification.class, processPointId).execute(productId, msg, getPlanCode(), mtype);
		
	}

	private String getPlanCode() {
		return getPreProductionLot() != null ? getPreProductionLot().getPlanCode() : getPropertyBean().getPlanCode();
	}
	
	protected String getContextString(String key){
		String value = (String)context.get(key);
		return  StringUtils.isEmpty(value) ? value : StringUtils.trim(value);
	}

	protected void logAndNotify(boolean notifyClient, MessageType mtype, String ...msg) {
		StringBuilder sb = new StringBuilder();
		for(String s : msg)
			sb.append(s);

		if(mtype.ordinal() >= MessageType.ERROR.ordinal()) 	
			getLogger().error(sb.toString());
		else if (mtype.ordinal() == MessageType.INFO.ordinal())
		    getLogger().info(sb.toString());
		else getLogger().warn(sb.toString());
		
		if(notifyClient)
			invokeNotification(productId, sb.toString(), mtype);
		
	}

	
	protected void logAndNotify(MessageType mtype, String ...msg ) {

		logAndNotify(true, mtype, msg );

	}
	
	public List<ProductNumberDef> getProductNumberDefs() {
		if(productNumberDefs == null)
			productNumberDefs = ServiceUtil.getProductNumberDefs(getProductType().name(), productId);
		return productNumberDefs;
	}
	
	protected BuildAttributeCache getBuildAttributesCache() {
		if(buildAttributeCache == null){
			buildAttributeCache = new BuildAttributeCache();
			buildAttributeCache.loadAttribute(BuildAttributeTag.SUB_IDS);
		}
		return buildAttributeCache;
	}
	
	protected String[] getSubIds(PreProductionLot preProductionLot) {
		 String subIdStr = getBuildAttributesCache().findAttributeValue(preProductionLot.getProductSpecCode(),BuildAttributeTag.SUB_IDS, "", getProductType());
		 String[] subIds = new String[]{};
		 if(StringUtils.isEmpty(subIdStr)){
			 getLogger().info("Build Attribute SUB_IDS is not defined.");
		 } else 
			 subIds = subIdStr.split(Delimiter.COMMA);
		
		return subIds;
	}
	
	protected HeadlessDataCollectionContext getHeadlessDataCollectionContext() {
		HeadlessDataCollectionContext hlContext = (HeadlessDataCollectionContext)context;
		if(hlContext.getDevice() == null )
			hlContext.setDevice(device);
		
		if(StringUtils.isEmpty(hlContext.getProcessPointId()))
			hlContext.setProcessPointId(getProcessPointId());
		
		if(hlContext.getLogger() == null)
			hlContext.setLogger(getLogger());
		
		if(hlContext.getProduct() == null)
			hlContext.setProduct(product);
		
		if(hlContext.getProductType() == null)
			hlContext.setProductType(productType);
		
		return hlContext;
	}
	
	protected void updateContextError(String errorCode, String msg) {
		contextPut(TagNames.ERROR_CODE.name(), errorCode);
		context.put(TagNames.ERROR_MESSAGE.name(), msg);
		
		Logger.getLogger().warn(msg);
	}
	
	protected void updateContextError(OnErrorCode errCode) {
		updateContextError(errCode.getCode(), errCode.getMessage(getProductId()));
	}
	
	protected String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	protected boolean doProductStateCheck() throws Exception {
		ProductStateCheck stateCheck = new ProductStateCheck(getHeadlessDataCollectionContext(), null);
		stateCheck.doWork();
		return getHeadlessDataCollectionContext().getBoolean(TagNames.CHECK_RESULT.name(), true) == true;
	}

	protected void updateStampingInfoCode(FloorStampInfoCodes stampInfo, String productId) {
		contextPut(TagNames.ERROR_CODE.name(), stampInfo.getInfoCode());
		contextPut(TagNames.ERROR_MESSAGE.name(), stampInfo.getInfoMessage(productId));
	};


}
