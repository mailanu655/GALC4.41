package com.honda.galc.service.datacollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.DeviceUtil;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.HeadlessDCInfoCode;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.IErrorCode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.ProductCheckUtil;
/**
 * 
 * <h3>HeadlessDataCollectionContext</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HeadlessDataCollectionContext description </p>
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
 * <TD>Mar 7, 2014</TD>
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
 * @since Mar 7, 2014
 */
public class HeadlessDataCollectionContext extends HashMap<Object, Object>{

	private static final long serialVersionUID = 1L;
	private static final String PRODUCT = "product";
		
	private Logger logger;
	private HeadLessPropertyBean property;
	private String productName;
	protected String processPointId;
	protected ProductType productType;
	protected BaseProduct product;
	protected BaseProductSpec productSpec;
	private IDeviceHelper deviceHelper;
	private IErrorCode errorCode;
	private String productNamePrefix;
	
	protected List<ProductBuildResult> buildResults = null;

	private String productSpecCode;

	private ProcessPoint processPoint;
	private GpcsDivision gpcsDivision;

	private List<LotControlRule> lotControlRules;
	private Device device;

	private String partId;
	private String associateNo;
	
	private boolean qcHold = false;
	private int holdSource = 0; // "0" if terminal and "1" for Equipment
	
	public void setDevice(Device device){
		// reset logger etc.
		logger = null; 
		productName = null;
		deviceHelper = null;
		this.device = device;
		this.processPointId = StringUtils.trim(device.getIoProcessPointId());
		
		getLogger().info("collector received device data:", device.toInputString());
		property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);
				
		putAll(device.getInputMap());  
	}
	
	@Override
	public Object put(Object arg0, Object arg1) {
		if(!StringUtils.isEmpty(productName))
			super.put(getTagForCurrentProduct(arg0.toString()), arg1);
		
		return super.put(arg0, arg1);
	}
	
	// ==================== utility functions ===================
	
	public String getTagForCurrentProduct(String tagName) {
		return getTagForProduct(productName, tagName); 
	}

	
	public String getTagForProduct(String prodName, String tagName){
		return StringUtils.isEmpty(prodName) ?  tagName:	prodName + Delimiter.DOT + tagName;
	}

	public Object getTagValue(String tagName){
		return get(getTagForCurrentProduct(tagName));
	}
	
	public void prepareReply(Device device) {
		if(product != null) put(TagNames.MODEL_CODE.name(), product.getModelCode());
		device.populateReply(this);
	}
	
	private IDeviceHelper createDeviceHelper() {
		ProductTypeUtil typeUtil = ProductTypeUtil.getTypeUtil(getProductType());
		if(typeUtil.getProductBuildResultClass().equals(InstalledPart.class)) {
	    	 return new InstalledPartHelper(device, getProperty(), getProcessPointId(), getLogger(),associateNo);
		} else {
	    	 return new DiecastBuildResultHelper(device, getProperty(), processPointId, getLogger(),associateNo);
		}
	}

	public void prepareInfoCode(HeadlessDCInfoCode infoCode) {
		put(TagNames.INFO_CODE.name(), infoCode.getInfoCode());
		put(TagNames.INFO_MSG.name(), infoCode.getInfoMsg(isMultiProduct() ? "" : getDeviceHelper().getProductId()));
		
	}
	
	public boolean isMultiProduct() {
		return !StringUtils.isEmpty(getProperty().getProducts());
	}
	
	public String getProductInfoString() {
		StringBuilder sb = new  StringBuilder();
		sb.append("Product:").append(getProduct() == null ? "null" : getProduct().getProductId());
		sb.append(" ProductSpecCode:").append(getProductSpecCode()).append(" missingRequiredPart:");
		sb.append(get(TagNames.REQUIRED_PART.name(), true).toString());
		return sb.toString();
	}
	
	public Object get(String key, Object defaultValue) {
		Object value = get(key);
		return value == null ? defaultValue : value;
	}
	
	public Boolean getBoolean(String key, Boolean defaultValue) {
		Object value = get(key);
		return value == null ? defaultValue : DeviceUtil.convertToBoolean(value.toString());
	}

	public void dataCollectionComplete( String status){
		put(getTagForCurrentProduct(TagNames.DATA_COLLECTION_COMPLETE.name()), status);
		merge(TagNames.DATA_COLLECTION_COMPLETE.name(), DeviceUtil.convertToBoolean(status));
	}
	
	public Object getDataCollectionComplete(){
		return get(getTagForCurrentProduct(TagNames.DATA_COLLECTION_COMPLETE.name()));
	}
	
	public Object getDataCollectionCompleteForProduct(String prodName){
		return get(getTagForProduct(prodName, TagNames.DATA_COLLECTION_COMPLETE.name()));
	}

	public String getTrackingProcessPointId() {
		return StringUtils.isEmpty(getProperty().getDestinationProcessPointId()) ? getProcessPointId() : getProperty().getDestinationProcessPointId();
	}
	
	public boolean isHomeProduct(String productId) {
		if (ServiceUtil.isDiecast(getProductType().name())){
			List<ProductNumberDef> productNumberDefs = ProductNumberDef.getProductNumberDefs(getProperty().getHomeProductNumberDef());
			for (ProductNumberDef def : productNumberDefs) {
				if (def.getPlant(getProductId()).equals(getProperty().getPlantCode())) return true;
			}
			return false;
		}
		return true;
	}
	
	public ProductTypeUtil getProductTypeUtil() {
		return ProductTypeUtil.getTypeUtil(getProductType());
	}
	

    public ProductCheckUtil getProductCheckUtil() {
			return  new ProductCheckUtil(getProductToCheck(), getProcessPoint());
	}
    
    public BaseProduct getProductToCheck() {
		BaseProduct checkingProduct = (product != null) ? product : ProductTypeUtil.createProduct( getProductType().name(),getDeviceHelper().getProductId());
		return checkingProduct;
	}

	
	
	
	// ==================== getter & setter ===================
	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
		this.productNamePrefix = this.productName + Delimiter.DOT;
		getDeviceHelper().setProductName(productName);
		super.put(TagNames.PRODUCT_NAME.name(), productName);
		
		updateContext();
	}


	private void updateContext() {
		Map<Object, Object> tmpMap = new HashMap<Object, Object>();
		for(Object k : keySet()){
			if(null != k && k.toString().startsWith(productNamePrefix))
				tmpMap.put(k.toString().replaceFirst("^"+productNamePrefix, ""), this.get(k));
		}
		
		this.putAll(tmpMap);
		
		if(this.getDeviceHelper() != null && this.getDeviceHelper().hasUpdate())
			this.updateContext(this.getDevice().getDeviceDataFormats());
		
	}

	public Logger getLogger() {
		if(logger == null)
			logger = ServiceUtil.getLogger(processPointId); 

		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public HeadLessPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, getProcessPointId());
		return property;
	}


	public void setProperty(HeadLessPropertyBean property) {
		this.property = property;
	}

	public IDeviceHelper getDeviceHelper() {
		if(deviceHelper == null)
			deviceHelper = createDeviceHelper();
		
		return deviceHelper;
	}


	public void setDeviceHelper(IDeviceHelper deviceHelper) {
		this.deviceHelper = deviceHelper;
	}

	public ProductType getProductType() {
		if(productType == null)
			productType = ProductTypeCatalog.getProductType(property.getProductType());
		
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public BaseProduct getProduct() {
		return product;
	}

	public String getProcessPointId() {
		if(StringUtils.isEmpty(processPointId))
			processPointId = (String)get(TagNames.PROCESS_POINT_ID.name());
		return processPointId;
	}

	public String getValidProductId() {
		return  product == null ? null : product.getProductId();
	}

	public BaseProductSpec getProductSpec() {
		return productSpec;
	}

	public String getProducts() {
		return property.getProducts();
	}



	public String getProductId() {
		return getDeviceHelper().getProductId();
	}

	public String getProductSpecCode() {
		
		return productSpecCode;
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}


	public List<ProductBuildResult> getBuildResults() {
		if(buildResults == null)  buildResults = new ArrayList<ProductBuildResult>();
		return buildResults;
	}

	public void setBuildResults(List<ProductBuildResult> buildResults) {
		this.buildResults = buildResults;
		if(!StringUtils.isEmpty(productName))  put(productName, buildResults);
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends ProductBuildResult> getBuildResults(String productName) {
		return get(productName) == null ? new ArrayList() : (List<? extends ProductBuildResult>) get(productName);
	}

	public ProductBuildResult getBuildResult(String partName) {
		for(ProductBuildResult result: getBuildResults()){
			if(partName.equals(result.getPartName().trim()))
				return result;
		}
		return null;
	}

	public void setProductSpec(BaseProductSpec prodpec) {
		this.productSpec = prodpec;
		
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
		if(product != null) put(PRODUCT, product);
		
	}

	public ProcessPoint getProcessPoint() {
		if(processPoint == null)
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		return processPoint;
	}
	
	

	public GpcsDivision getGpcsDivision() {
		if(gpcsDivision == null)
			gpcsDivision = ServiceFactory.getDao(GpcsDivisionDao.class).findByKey(getProcessPoint().getDivisionId());
		return gpcsDivision;
	}

	public void setGpcsDivision(GpcsDivision gpcsDivision) {
		this.gpcsDivision = gpcsDivision;
	}

	public List<LotControlRule> getLotControlRules() {
		
		return lotControlRules;
	}

	public void setLotControlRules(List<LotControlRule> lotControlRules) {
		this.lotControlRules = lotControlRules;
	}

	public void setCurrentProductId(String productId) {
		getDeviceHelper().setCurrentProductId(productId);
		
	}

	public void extractResults(List<LotControlRule> rules) {
		buildResults = getDeviceHelper().getBuildResults(rules);
	}
	
	public void extractResultsForMbpnProduct(List<String> mbpnInstalledParts) {
		buildResults = getDeviceHelper().getBuildResultsMbpnProduct(mbpnInstalledParts);
	}

	public void updateDevice() {
		if(hasPreScript())
			getDeviceHelper().getDevice().populate(this);
		
	}
	
	public boolean hasScript() {
		try {
			return getProperty().getScripts() != null && getProperty().getScripts().size() > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean hasPreScript() {
		try {
			return getProperty().getPreScripts() != null && getProperty().getPreScripts().size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasChecker() {
		return getProperty().getProductStateChecks().length > 0 || getProperty().getProductCheckTypes().length > 0;
	}

	public void merge(String name, Boolean result) {
		Boolean existing = (Boolean)getBoolean(name, true);
		Boolean finalResult = result & existing;
		if (name.equals(TagNames.DATA_COLLECTION_COMPLETE.name()))
			put(name, (finalResult ? DataCollectionComplete.OK : DataCollectionComplete.NG));
		else 
			put(name, finalResult);
		
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getAssociateNo() {
		return associateNo;
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public Device getDevice() {
		return device;
		
	}
	
	public DataContainer asDataContainer(){
		DataContainer dc = new DefaultDataContainer();
		
		for(Map.Entry<Object, Object> e : entrySet()){
			dc.put(e.getKey().toString(), e.getValue());
		}
		
		//some client need the device input
		if(device.isDataFromTagValue()){
			for(DeviceFormat df : device.getDeviceDataFormats())
				dc.put(df.getTagValue(), df.getValue());
		}
		
		return dc;
	}

	public String getCurrentProductId() {
		if(!StringUtils.isEmpty(getValidProductId()))
			return getValidProductId();
		
		return getProductId();
	}

	public String getAssociateNoValue() {
		if(!associateNo.equals(""))
			return associateNo;
		else if(!getProperty().isInstalledPartAssociateUsingUserId())
			return this.processPointId;
		else
			return null;
	}

	public void updateContext(List<DeviceFormat> deviceDataFormats) {
		for(DeviceFormat df : deviceDataFormats){
			if(df.getDeviceTagType() == DeviceTagType.SQL)
				this.put(df.getTag(), df.getValue());
		}
	}
	
	public boolean getQCHold() { 
		if (StringUtils.isNotBlank((String)getTagValue(TagNames.QC_HOLD.name())) && 
				(getTagValue(TagNames.QC_HOLD.name()).equals("TRUE"))){ 
			qcHold = true;
		}
			
		return qcHold;
	}

	public void setQCHold(boolean qcHold) {
		this.qcHold = qcHold;
	}
	
	public int getHoldSource() { 
		return holdSource;
	}

	public void setHoldSource(int holdSource) {
		this.holdSource= holdSource;

	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
		
	}

	public boolean isProductStateCheckOk() {
		return this.getBoolean(TagNames.CHECK_RESULT.name(), true);
	}
	
	public IErrorCode getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(IErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
}
