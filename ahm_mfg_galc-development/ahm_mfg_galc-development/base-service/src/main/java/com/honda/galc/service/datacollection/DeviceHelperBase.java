package com.honda.galc.service.datacollection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.HeadlessDataMapping;
import com.honda.galc.device.Tag;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.DataMappingPropertyBean;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CarrierUtil;
import com.honda.galc.util.LotControlRuleUtil;

/**
 * 
 * <h3>DeviceHelperBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceHelperBase description </p>
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
 * <TD>Mar 12, 2012</TD>
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
 * @since Mar 12, 2012
 */

public abstract class DeviceHelperBase {
	public static final String SEPERATOR = ".";
	enum StatusString{OK, NG};
	boolean hasUpdate = false;
	Boolean error = null;
	
	protected Device device;
	protected HeadlessDataMapping plcDataMappingStyle; 
	protected int partIndex;
	public static final String ONE = "1";
	
	protected String processPointId;
	protected String associateNo;
	protected String clientId;
	protected String productId;
	private Logger logger;
	protected List<String> products;
	protected String productName;
	protected DataMappingPropertyBean property;
	List<String> partList = null;
	protected  Integer nextPartIndex;
	protected Integer nextTorqueIndex;
	private String currentProductId;
	protected String productType;
	protected Integer itemIndex = null;
	protected LotControlRule currentRule;
	protected String mappingPartName;
	
	abstract Class<?> getBuildResultIdClass();
	abstract String getPartNamesProperty();
	
	public DeviceHelperBase(Device device, DataMappingPropertyBean property,
			String processPointId, Logger logger,String associateNo) {
		super();
		this.device = device;
		this.property = property;
		this.processPointId = processPointId;
		this.logger = logger;
		if(PropertyService.getPropertyBean(HeadLessPropertyBean.class,processPointId).isInlineRepair() 
				&& PropertyService.getPropertyBean(HeadLessPropertyBean.class,processPointId).isPdaRecipeDownload()){
			try{
				this.productType = device.getDeviceFormat(DataContainerTag.PRODUCT_TYPE).getValue().toString();
			}catch(Exception e){getLogger().info("No Product Type setup in device, using default Type");}
		}
		if(this.productType == null || StringUtils.isEmpty(productType)) this.productType = property.getProductType();
		this.associateNo=associateNo;
		init();
	}

	private void init() {
		resetState();
		currentRule = null;
		processInputDevice();
		
		// if device driven, No need to check property.getPlcDataMapping() 
		// Only MAP_BY_PART_NAME is supported for device driven
		if(property.isDeviceDriven()) {	
			plcDataMappingStyle = HeadlessDataMapping.MAP_BY_PART_NAME;
		} else {
			plcDataMappingStyle = HeadlessDataMapping.valueOf((property.getPlcDataMapping()));
		}
		
		getLogger().info("PlcDataMapping:", plcDataMappingStyle.toString());
	}

	private void processInputDevice() {
		for(DeviceFormat format : device.getDeviceDataFormats()){
			if(format.getDeviceTagType() == DeviceTagType.SQL){
				hasUpdate = true;
				String sql = format.getTagValue();
				if(StringUtils.isEmpty(sql))
					getLogger().error("Error: data container sql tag value is empty.");
				else {
					format.setValue(executeSqlQuery(sql, device.getDeviceDataFormats()));
					getLogger().info("SQL tag:", format.getTag(), " tag value:", format.getTagValue(),
							" result:" + format.getValue());
				}
			} else if(format.getDeviceTagType() == DeviceTagType.STATIC){
				format.setStaticValue();
			}
		}
		clientId = device.getClientId();
	}
		
	// ----------------- Device data extract functions ---------------------
	protected <T> T extract(Class<T> clazz) {
		Map<Tag, Field> tagMap = getMappedFields(clazz);
		boolean hasDeviceInput = false;
		T obj = createObject(clazz);
		for(Tag tag : tagMap.keySet()){
			
			Object inputValue= getTagValueFromDevice(tag, clazz);
			if(inputValue != null){
				hasDeviceInput = true;
				if(isStatus(tag))
				    setStatusInputData(obj, tagMap.get(tag), inputValue);
				else
				setInputValue(obj, tagMap.get(tag), inputValue);
			}
		}
		return hasDeviceInput ? obj : null;
	}

	private boolean isStatus(Tag tag) {
		return  (TagNames.INSTALLED_PART_STATUS.name().equals(tag.name()) ||
				TagNames.MEASUREMENT_STATUS.name().equals(tag.name()));
	}
	
	protected void setStatusInputData( Object obj,Field field, Object inputValue){
		InstalledPartStatus convertInstalledPartStatus = convertInstalledPartStatus(inputValue.toString());
		setInputValue(obj, field, Integer.valueOf(convertInstalledPartStatus.getId()));
		
		if(convertInstalledPartStatus != InstalledPartStatus.OK)
			setErrorCodeField(obj, inputValue);
	}
				
	private void setErrorCodeField(Object obj, Object inputValue) {
		String setMethodName = "setErrorCode";
		setValue(obj, inputValue, setMethodName);
	}
	
	protected void setValue(Object obj, Object inputValue, String setMethodName) {
		try {
			Method method = obj.getClass().getMethod(setMethodName,	new Class[] { String.class });

			String value = inputValue.toString();
			method.invoke(obj, value);
		} catch (Exception e) {
			getLogger().warn(e, " Exception to set errorCode for ", obj.getClass().getSimpleName(), " value:" + inputValue);
		}
	}

	private Object getTagValueFromDevice(Tag tag, Class<?> clazz) {
		 DeviceFormat inputDeviceFormat = device.getInputDeviceFormat(getFullTagName(tag.name(), getPrefix(), getSuffix(clazz)));
		 if(inputDeviceFormat == null)
			 inputDeviceFormat = device.getInputDeviceFormat(getFullTagName(tag.alt(), getPrefix(), getSuffix(clazz)));
		 
		 if(inputDeviceFormat == null) return null;	 
		 
		 Object inputValue = inputDeviceFormat.convert(inputDeviceFormat.getValue());
		
		 if(!StringUtils.isEmpty(inputDeviceFormat.getException()))
			 error = true;
		
		return inputValue;
	}

	protected Object getFromDevice(String name) {
		Object value = device.getInputObjectValue(name);
		
		getLogger().debug("Client Id:", clientId, " getFromDevice for tag:", name, " value:", value == null ? "null" : value.toString() );
		return value;
	}
	
	private String getFullTagName(String name, String prefix, Object suffix) {
		StringBuilder sb = new StringBuilder();
		
		if(!StringUtils.isEmpty(getProductName()))	sb.append(getProductName()).append(SEPERATOR);
		if(!StringUtils.isEmpty(prefix)) sb.append(prefix).append(SEPERATOR);
		sb.append(name);
		if(suffix != null) sb.append(suffix.toString());
		return sb.toString();
	}

	private <T> T createObject(Class<T> clazz) {
		final Constructor<T> constructor;
		try {
			constructor = clazz.getDeclaredConstructor(new Class[]{});
	        return constructor.newInstance();

		} catch (Exception e) {
			getLogger().error(e, "Exception to create Object for clazz:", clazz.getName(), " for device:", device.getClientId());
		}
		return null;
	}

	protected void setInputValue(Object obj, Field field, Object inputValue) {
		try {
			String setMethodName = getSetter(field);
			Method method = obj.getClass().getMethod(setMethodName,	new Class[] { field.getType() });
		
			Class<?> paramType = method.getParameterTypes()[0];
			if (paramType == String.class) {
				inputValue = inputValue.toString();
			} else if (!paramType.isAssignableFrom(inputValue.getClass())) {
				inputValue = convert(inputValue, boxPrimitive(paramType));
			}
			
			method.invoke(obj, inputValue);
		} catch (Exception e) {
			getLogger().error(e, " Exception to set ", field.getName(), " for ", obj.getClass().getSimpleName(), " value:" + inputValue);
		}
	}
	
	public Class<?> boxPrimitive(Class<?> primitiveType) {
		if (primitiveType == boolean.class) {
			return Boolean.class;
		} else if (primitiveType == byte.class) {
			return Byte.class;
		} else if (primitiveType == char.class) {
			return Character.class;
		} else if (primitiveType == float.class) {
			return Float.class;
		} else if (primitiveType == int.class) {
			return Integer.class;
		} else if (primitiveType == long.class) {
			return Long.class;
		} else if (primitiveType == short.class) {
			return Short.class;
		} else if (primitiveType == double.class) {
			return Double.class;
		} else {
			return primitiveType;
		}
	}
	
    public Object convert(Object inputValue, Class<?> paramType) throws Exception, NoSuchMethodException {
		Method method = paramType.getMethod("valueOf", String.class);
		return method.invoke(null, inputValue.toString());		// invoke static method valueOf: Integer.valueOf, Timestamp.valueOf
    }
    
	private String getSetter(Field field) {
		String fieldName = StringUtils.strip(field.getName(), "_");
		return "set" + StringUtils.capitalize(fieldName);
	}

	protected static Map<Tag, Field> getMappedFields(Class<?> clazz) {
		List<Class<?>> classes = getClassList(clazz);
		Map<Tag, Field> map = new HashMap<Tag, Field>();
		for(Class<?> claz : classes){
			for(Field field : claz.getDeclaredFields()){
				if(field.getAnnotation(Tag.class) != null){
					map.put(field.getAnnotation(Tag.class), field);
				}
			}
		}
		return map;
	}

	private static List<Class<?>> getClassList(Class<?> clazz) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Class<?> supperClazz = clazz;
		while(supperClazz != Object.class){
			list.add(supperClazz);
			supperClazz = supperClazz.getSuperclass();
		}
		return list;
	}

	private Object getSuffix(Class<?> clazz) {
		if(isDynamicDataMapping()){
			return (clazz == Measurement.class) ? nextTorqueIndex : nextPartIndex;
				
		} else {
			if(clazz ==  Measurement.class) return itemIndex;
		}
		return null;
	}

	private String getPrefix() {
		if(plcDataMappingStyle == HeadlessDataMapping.MAP_BY_TAG_NAME || 
			(plcDataMappingStyle == HeadlessDataMapping.MAP_BY_PART_NAME  && StringUtils.isEmpty(mappingPartName)))
			return null;

		if(plcDataMappingStyle == HeadlessDataMapping.MAP_BY_PART_NAME)
			return mappingPartName;
		else if(plcDataMappingStyle == HeadlessDataMapping.MAP_BY_RULE_INDEX)
			return Integer.toString(partIndex +1);

		return null;
	}
	
	protected void populateProductResultOverallStatus(ProductBuildResult result) {
		mappingPartName = null;
		Object status = getFromDevice(getFullTagName(TagNames.OVERALL_STATUS.name(), null, null));
		if(status != null){
			result.setInstalledPartStatus(convertInstalledPartStatus(status.toString()));
		}
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		result.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		result.setProcessPointId(this.processPointId);
		if(!associateNo.equals(""))
			result.setAssociateNo(associateNo);
		else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
				PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))
			result.setAssociateNo(this.processPointId);
	}
	
	// ----------------- getters & setters -----------------
	public String getProcessPointId() {
		return processPointId;
	}

	public String getProductId() {
		if(StringUtils.isEmpty(productId)) {
			if(!StringUtils.isEmpty(property.getTrackingArea())) {
				Object carrierValue = getFromDevice(getFullTagName(TagNames.CARRIER_ID.name(),null, null));
				if(carrierValue != null) {
					productId = CarrierUtil.findProductIdByCarrier(property.getTrackingArea(), carrierValue.toString());
					if(StringUtils.isEmpty(productId))
						productId = carrierValue.toString();
				}
			} else
				productId = getProductIdFromeDevice();

		}
		return productId;
	}
	
	public String getProductIdFromeDevice() {
		    Tag productIdTag = findTag(TagNames.PRODUCT_ID.name());
			
			Object productId = getFromDevice(getFullTagName(productIdTag.name(),null, null));
			if(productId != null) return productId.toString();
			
			productId =	getFromDevice(getFullTagName(productIdTag.alt(), null, null));
			
			return productId == null ? null : productId.toString();
	}
	
	private Tag findTag(String tagName) {
		Class<?> buildResultClass = getBuildResultIdClass();
		Map<Tag, Field> mappedFields = getMappedFields(buildResultClass);
		for(Tag tag : mappedFields.keySet()){
			if(tagName.equals(tag.name())) 
					return tag;
		}
		
		getLogger().error("Error: Can't find product id tag.");
		return null;
	}

	public String getClientId() {
		return clientId;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
		resetState();
	}

	private void resetState() {
		this.productId = null; //reset product id 
		this.itemIndex = null;
		this.nextPartIndex = 0;
		this.nextTorqueIndex = 0;
		this.setCurrentProductId(null);
	}
	public String getProductName() {
		return productName;
	}

	public List<String> getPartList(List<LotControlRule> rules) {
		if(partList == null){
			partList = new ArrayList<String>();
			
			if(property.isLotControl() || property.isDeviceDriven()){
				
				if(rules == null){
					getLogger().error("No Lot Control Rule configured for process point configured to use Lot Control Rule!");
					return partList;
				}
				
				for(LotControlRule rule : rules){
					getLogger().info("LotControl rule:", rule.toString());
					partList.add(rule.getPartNameString());
				}
			} else{
				partList = initPartList(getPartNamesProperty());
			}
			
		}
		return partList;
	}
	
	public List<LotControlRule> deduceLotControlRules() {
		return LotControlRuleUtil.deduceLotControlRules(device, productType, getProductName(), getLogger());
	}

	protected List<String> initPartList(String partNames) {
		
		if(StringUtils.isEmpty(partNames)) return new ArrayList<String>();
		
		String[] split = partNames.split(",");
		for(String loc : split){
			loc = loc.trim();
		}
		
		getLogger().info("Locations from property:", partNames);
		return Arrays.asList(split);
	}
	
	protected String getValidProductId() {
		return StringUtils.isEmpty(getCurrentProductId()) ? getProductId() : getCurrentProductId();
	}
	
	// ----------------- utilities functions -------------------
	public InstalledPartStatus convertInstalledPartStatus(String value) {
		InstalledPartStatus status = InstalledPartStatus.NG;
		if(StatusString.OK.toString().equals(value) || 
				ONE .equals(value) ||
				Boolean.TRUE.equals(Boolean.valueOf(value)))
			 status = InstalledPartStatus.OK;
		return status;
	}
	
	protected String getTagPrefix(String productName, String partName) {
		StringBuilder prefix = new StringBuilder();
		if(!StringUtils.isEmpty(productName)) prefix.append(productName);
		if(!StringUtils.isEmpty(partName)){
			if(prefix.length() > 0) prefix.append(".");
			prefix.append(partName);
		}
		
		return prefix.toString();
	}
	
	protected String getTagName(String tagName) {
		return StringUtils.isEmpty(getProductName()) ? tagName : getProductName() + "." + tagName;
	}
	
	protected Timestamp getActualTimestampFromDevice() {
		Timestamp actualTimestamp = null;
		try {
			actualTimestamp = (Timestamp)getFromDevice(getFullTagName(TagNames.ACTUAL_TIMESTAMP.name(), null, null));
		} catch(Exception e) {
			getLogger().info("Ignoring exception occurred while fetching actual timestamp from the device");
		}
		return actualTimestamp;
	}

	protected boolean isMapByPartName() {
		return plcDataMappingStyle == HeadlessDataMapping.MAP_BY_PART_NAME;
	}

	protected boolean isDynamicDataMapping() {
		return plcDataMappingStyle == HeadlessDataMapping.MAP_BY_RULE;
	}
	
	public Object executeSqlQuery(String sql, List<DeviceFormat> deviceDataFormats) {
		Object result = null;
		String sqlStr = DataContainerUtil.makeSQL(sql, device.getInputMap());
		getLogger().debug("excuteSql:" + sqlStr);
		result = ServiceFactory.getDao(DeviceFormatDao.class).executeSqlQuery(sqlStr);
		if(result == null){
			getLogger().warn("WARN: null value from tag value sql query.");
			result = "null"; //can't insert null into data container, so set to null
		}
		return result;
	}
	
	public void executeSqlUpdate(String sql, List<DeviceFormat> deviceDataFormats) {
		String sqlStr = DataContainerUtil.makeSQL(sql, device.getInputMap());
		getLogger().info("excuteSql:" + sqlStr);
		ServiceFactory.getDao(DeviceFormatDao.class).executeSqlUpdate(sqlStr);
	}
	
	protected Logger getLogger(){
		if(logger == null)
		    logger = getLogger(processPointId);
		
		return logger;
	}
	
	protected String logInfo(String partName, String token) {
		StringBuilder sb = new StringBuilder();
		sb.append(getProcessPointId()).append(":").append(getProductId()).append(":").append(partName).append(":").append(token);
		return sb.toString();
	}

	public void setCurrentProductId(String currentProductId) {
		this.currentProductId = currentProductId;
	}
	
	public String getCurrentProductId(){
		return this.currentProductId;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public List<DeviceFormat> getCurrentProductDeviceDataFormats() {
		return StringUtils.isEmpty(getProductName()) ? getDevice().getDeviceDataFormats() :  getDeviceFormatsForName(getProductName());
	}

	public List<DeviceFormat> getDeviceFormatsForName(String tagNamePrefix) {
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		
		for(DeviceFormat df : getDevice().getDeviceDataFormats())
			if(df.getTag().startsWith(tagNamePrefix))
				list.add(df);
		
		return list;
	}

	public List<DeviceFormat> getCurrentProductReplyDeviceDataFormats() {
		return StringUtils.isEmpty(getProductName())? getDevice().getReplyDeviceDataFormats() : getReplyDeviceFormatsForName(getProductName());
	}

	private List<DeviceFormat> getReplyDeviceFormatsForName(String tagNamePrefix) {
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		for(DeviceFormat df : getDevice().getReplyDeviceDataFormats())
			if(df.getTag().startsWith(tagNamePrefix))
				list.add(df);
		
		return list;
	}
	
	protected void emit(String tag, Object obj, Field field, Map<Object, Object> context) {
		try {
			String getMethodName = getGetter(field);
			Method method = obj.getClass().getMethod(getMethodName, new Class[] {});
			context.put(getFullTagName(tag, getPrefix(), getSuffix(obj.getClass())), method.invoke(obj, new Object[] {}));
			getLogger().info("populated build result tag: ", tag, ":", getFullTagName(tag, getPrefix(), getSuffix(obj.getClass())));
		} catch (Exception e) {
			getLogger().error(e, " Exception to get ", field.getName(), " for ", obj.getClass().getSimpleName());
		}
		
	}
	
	private static String getGetter(Field field) {
		String fieldName = StringUtils.strip(field.getName(), "_");
		return "get" + StringUtils.capitalize(fieldName);
	}
	
	public static Logger getLogger(String loggerName){
		Logger logger;
		if(StringUtils.isEmpty(loggerName))
			logger = Logger.getLogger();
		else{
			logger = Logger.getLogger(PropertyService.getLoggerName(loggerName));
		}
		
		String level =PropertyService.getProperty(loggerName, LogLevel.LOG_LEVEL, LogRecord.defaultLevel.toString());
		logger.setLogLevel(LogLevel.valueOf(level));
		
		return logger;
	}	
	
	public boolean hasUpdate() {
		return hasUpdate;
	}
}
