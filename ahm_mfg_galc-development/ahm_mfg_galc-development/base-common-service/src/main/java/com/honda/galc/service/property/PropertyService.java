package com.honda.galc.service.property;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.qics.QicsDefectInfoManager;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>PropertyService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PropertyService description </p>
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
 * @author Jeffray Huang
 * Oct 5, 2009
 *

*/
/**
 * 
 * Regional-HMIN Merged Version 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
*/

public class PropertyService extends PersistentCache{

	public static PropertyService propertyService;

	private static final String PROP_BEAN_SUFFIX = "PropertyBean";
	
	private static final String SYSTEM_INFO_COMPONENT_ID = "System_Info";
	private static final String ASSEMBLY_LINE_ID = "ASSEMBLY_LINE_ID";
	private static final String SITE_NAME = "SITE_NAME";
	private static final String IS_CHANGE_PASSWORD_ENABLED="IS_CHANGE_PASSWORD_ENABLED";
	
	private static final String WILD_CARD ="*";
	private static final String MAP_KEY_REGEX = "\\s*(?:(?:\\{\\s*(\\S+([\\s]\\S+)?)\\s*\\})|(?:\\[\\s*(\\S+)\\s*\\]))\\s*";

	private static Map<String,List<IProperty>> beans = new ConcurrentHashMap<String,List<IProperty>>();
	private static Set<String> componentIds = Collections.synchronizedSet(new HashSet<String>());
	
	/**
	 * return property bean based on the supplied interface class 
	 * componentId is derived from interface class 
	*/
	public static <T extends IProperty> T getPropertyBean(Class<T> intfClass) {
		return getPropertyBean(intfClass,resolveComponentId(intfClass));
	}
	
	/**
	 * return property bean based on the supplied interface class 
	 * componentId is derived from interface class 
	*/
	public static <T extends IProperty> T getRefreshedPropertyBean(Class<T> intfClass) {
		String componentId = resolveComponentId(intfClass);
		refreshComponentProperties(componentId);
		return getPropertyBean(intfClass,resolveComponentId(intfClass));
	}
	
	/**
	 * Return Property Bean based on supplied Property Bean interface<br/>
	 * Differs from PropertyServices#getPropertyBean(Class) by supplying explicit componenet ID<br/>
	 * 
	 * @see PropertyServices#getPropertyBean(Class) for complete details
	 * @param <T> - Property Bean interface 
	 * @param intfClass - Property Bean interface class
	 * @param componentId - component ID used to retrieve the properties
	 * @return - Property Bean instance
	 * @throws PropertyException
	*/
	public static <T extends IProperty> T getPropertyBean(Class<T> intfClass, String componentId) {
		
		return getPropertyBean(intfClass,componentId,null);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T extends IProperty> T getPropertyBean(Class<T> intfClass, String componentId,String suffix) {
		if(StringUtils.isEmpty(suffix)) {
			T bean = getCannedPropertyBean(intfClass, componentId);
			if(bean != null) return bean;
		}

		loadComponentProperty(componentId);

		InvocationHandler handler = new PropertyBeanInvocationHandler(componentId, intfClass, getInstance(),suffix);
		
		T intf = (T) Proxy.newProxyInstance(intfClass.getClassLoader(),new Class[] { intfClass },handler);
		
		// cache bean instance
		if(StringUtils.isEmpty(suffix))	getBeanList(componentId).add(intf);
		
		return intf;
	}	
	
	public static PropertyService getInstance() {
		
		if(propertyService == null ) propertyService = new PropertyService();
		
		return propertyService;
		
	}
	
	public PropertyService() {
	    super();
	}
	
	@SuppressWarnings("unchecked")
    public static <T extends IProperty> T getCannedPropertyBean(Class<T> intfClass, String componentId) {
		
		if(!beans.containsKey(componentId)) return null;
		
		for	(IProperty prop: Collections.unmodifiableList(beans.get(componentId))) {
			if(isInstance(prop,intfClass)) return (T) prop;
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
    public static <T extends IProperty> T getCannedPropertyBean(Class<T> intfClass) {
		
		for(Map.Entry<String, List<IProperty>> e:beans.entrySet()) {
			
			for(IProperty prop : Collections.unmodifiableList(e.getValue())) {
				if(isInstance(prop,intfClass)) return (T) prop;
			}
		}
		
		return null;
		
	}
	
	public static <T extends IProperty> String getCannedComponentId(Class<T> intfClass) {
		
		for(Map.Entry<String, List<IProperty>> e:beans.entrySet()) {
			
			for(IProperty prop : Collections.unmodifiableList(e.getValue())) {
				if(isInstance(prop,intfClass)) return e.getKey();
			}
		}
		
		return null;
		
	}
	
	private static boolean isInstance(IProperty aClass, Class<?> intfClass) {
		Class<?>[] interfaces = aClass.getClass().getInterfaces();
		return interfaces.length > 0 && intfClass.equals(interfaces[0]);
	}
	
	/**
	 * Use an existing property bean or create new property bean implementing IProperty. Use this only when the overhead of a PropertyBean is an overKill.
	 * @param componentId
	 * @param propertyName 
	 * @return
	 */
	@Deprecated
	public static String getProperty(String componentId, String propertyName) {
		
		loadComponentProperty(componentId);
		
		for(ComponentProperty prop :getComponentProperty(componentId)) {
			if(prop.getId().getPropertyKey().equals(propertyName)) return prop.getPropertyValue();
		}
		
		return null;
		
	}
	
	public static List<String> getPropertyList(String componentId,String propertyName) {
		return getPropertyList(componentId,propertyName,",");
	}
	
	public static List<String> getPropertyList(String componentId,String propertyName,String separaters) {
		
		List<String> items = new ArrayList<String>();
		String propertyStr = getProperty(componentId,propertyName);
		if(StringUtils.isEmpty(propertyStr)) return items; 
		String[] properties =  propertyStr.split(separaters);
		for(String item : properties) {
			items.add(StringUtils.trim(item));
		}
		return items;
		
	}
	
	public static Map<String,String> getPropertyMap(String componentId,String propertyName) {
		List<ComponentProperty> properties = getComponentProperty(componentId);
		Map<String,String> propertyMap = new LinkedHashMap<String, String>();
		Pattern pattern = getPropertyKeyPattern(propertyName);
		for(ComponentProperty property : properties) {
			String mapKey = null;
			if(property.getId().getPropertyKey().equals(StringUtils.trim(propertyName))) mapKey = WILD_CARD;
			else {
				Matcher m = pattern.matcher(property.getId().getPropertyKey());
				if(m.matches()) mapKey = m.group(1);
			}
			if(mapKey != null) propertyMap.put(mapKey, property.getPropertyValue());
		}
		return propertyMap;
	}
	
	private static Pattern getPropertyKeyPattern(String propertyKey) {
		String regex = new StringBuilder(propertyKey).append(MAP_KEY_REGEX).toString();
		Pattern p = Pattern.compile(regex);
		return p;
	}

	
	/**
	 * wild card supports *#
	 * @param componentId
	 * @param wildCard
	 * @return
	 */
	public static List<ComponentProperty> getProperties(String componentId, String regex) {
		loadComponentProperty(componentId);
		List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
		for(ComponentProperty prop :getComponentProperty(componentId)) {
			if(prop.getId().getPropertyKey().matches(regex)) properties.add(prop);
		}
		
		return properties;
	}
	
	private static boolean isPropertyLoaded(String componentId) {
		for(String id : new CopyOnWriteArrayList<String>(componentIds)) {
			if(id.equals(componentId)) return true;
		}
		return false;
	}
	
	public static Integer getPropertyInt(String componentId, String propertyName) {
		String propValue = getProperty(componentId,propertyName);
		if(propValue == null) return null;
		Integer num = Integer.parseInt(propValue);
		return num;
	}
	
	public static int getPropertyInt(String componentId, String propertyName, int defaultValue) {
		Integer  property= getPropertyInt(componentId, propertyName);
		return (property == null) ? defaultValue : property;
	}
	
	public static boolean getPropertyBoolean(String componentId, String propertyName, boolean defaultValue) {
		String  property = getProperty(componentId, propertyName);
		if(property == null) return defaultValue;
		property = property.trim();
		return property.equalsIgnoreCase("Y") || property.equalsIgnoreCase("TRUE") 
		       || property.equalsIgnoreCase("YES") || property.equalsIgnoreCase("1");
	}
	
	public static boolean hasProperty(String componentId,String propertyName) {
		return getProperty(componentId, propertyName) != null;
	}
	
	public static List<IProperty> getBeanList(String componentId) {
		if(!beans.containsKey(componentId)) {
			beans.put(componentId, new CopyOnWriteArrayList<IProperty>());
		}
		return beans.get(componentId);
	}
	
	/**
	 * load component property if not loaded
	 * @param componentId - component Id
	 */
	private static void loadComponentProperty(String componentId) {
		if(!isPropertyLoaded(componentId))
			basicLoadComponentProperty(componentId);
	}
	
	private static synchronized void basicLoadComponentProperty(String componentId) {
		if(ServiceFactory.isServerAvailable()){
		    try{
	            ComponentPropertyDao dao = ServiceFactory.getDao(ComponentPropertyDao.class);
				List<ComponentProperty> properties = dao.findAllByComponentId(componentId);
				getInstance().put(componentId, properties);
				if(componentId != null) //a null in the component list will hurt the 
					componentIds.add(componentId);
			}catch(Exception e){
			    e.printStackTrace();
			    getLogger().error(e,"Could not load component property ", componentId," due to " + e.getMessage());
    
			}
		}
	}
	
	public static List<ComponentProperty> getComponentProperty(String componentId) {
		loadComponentProperty(componentId);
	    return getInstance().getList(componentId,ComponentProperty.class);
	    
	}

	public static String getProperty(String componentId, String propertyName, String defaultValue) {
		String property = getProperty(componentId, propertyName);
		return StringUtils.isEmpty(property) ? defaultValue : property;
	}
	
	public static SystemPropertyBean getSystemPropertyBean(){
		return getPropertyBean(SystemPropertyBean.class);
	}
	
	public static void updateProperty(String componentId, String propertyKey, String propertyValue) {
		
		if(!isPropertyLoaded(componentId)) return;
		List<ComponentProperty> properties = getComponentProperty(componentId);
		if(properties == null){
			properties = new ArrayList<ComponentProperty>();
			properties.add(new ComponentProperty(componentId,propertyKey,propertyValue));
		}

		for(ComponentProperty property : properties) {
			if(property.getId().getPropertyKey().equals(propertyKey)){
				property.setPropertyValue(propertyValue);
				break;
			}
		}
		getInstance().put(componentId,properties);
		
		
	}
	
	/**
	 * refresh component properties only when the properties were loaded before 
	 * @param componentId
	 */
	public static void refreshComponentProperties(String componentId) {
		
		if(!isPropertyLoaded(componentId)) return;
		basicLoadComponentProperty(componentId);
		
		QicsDefectInfoManager.getInstance().refresh(componentId);
		
		getLogger().info("refresh properties for component: ", componentId);
		
	}
	
	public static LogLevel getLogLevel(String componentId) {
	        String property = getProperty(componentId, "LOG_LEVEL", LogRecord.defaultLevel.toString());
	        return LogLevel.valueOf(property);
	}
	
	private static <T extends IProperty> String resolveComponentId(Class<T> intfClass) {
		
		//		 Resolve component id
		String componentId = null;
		Annotation annotation = intfClass.getAnnotation(PropertyBean.class);
		if(annotation != null) {
			// Since ConfigObject annotation exists, take component ID from the annotation
			PropertyBean configObject = (PropertyBean) annotation;
			if (!configObject.componentId().equals(PropertyBean.DEF_UNNASSIGNED)) {
				componentId = configObject.componentId();
			}			
		};
		
		if(componentId == null) {
			// Derive implicit component ID 
			componentId 
				= getComponentByBeanName(intfClass.getSimpleName());
		}
		
		return componentId;
	}
	
	/**
	 * Helper method to make up componenet ID based on Property Bean class name
	 * 
	 * @param beanName - Property Bean class name
	 * @return componenet ID based on Property Bean class name 
	 */
	public static String getComponentByBeanName(String beanName) {
		// PropBeanAaaBbbCcc -> AAA_BBB_CCC
		StringBuilder res = new StringBuilder();
		int endAt = 0;
		boolean lastIsLowerCase = false;
		// check for "PropertyBean" suffix
		if(beanName.endsWith(PROP_BEAN_SUFFIX)) {
			endAt = PROP_BEAN_SUFFIX.length();
		}
		
		// In the loop
		for (int i = 0; i < beanName.length() - endAt; i++) {
			char letter = beanName.charAt(i);
			if(Character.isLowerCase(letter)) {
				//    if source letter is not capital
				//          convert to capital and add to result
				res.append(Character.toUpperCase(letter));
				lastIsLowerCase = true;
			} else {
				//    if source letter is capital (and not the 1st)
				//          add "_" and source letter	
				if(lastIsLowerCase) {
					res.append("_");
				}
				res.append(letter);
			}
		}
		
		return res.toString();
	}
	
	/**
	 * get Assembly Line Id
	 * @return
	 */
	public static String getAssemblyLineId() {
		
		return getProperty(SYSTEM_INFO_COMPONENT_ID,ASSEMBLY_LINE_ID);
		
	}
	
	public static String getSiteName() {
		return getProperty(SYSTEM_INFO_COMPONENT_ID,SITE_NAME);
	}
	
	public static Map<String, String> getServerUrls() {
		return getSystemPropertyBean().getServerUrls();
	}
	
	public static Properties getComponentProperties(String processPointId) {
		Properties properties = new Properties();
		List<ComponentProperty> propertyList = PropertyService.getComponentProperty(processPointId);
		if(propertyList != null){
			for(ComponentProperty prop : propertyList){
				properties.put(prop.getPropertyKey(), prop.getPropertyValue());
			}
		}
		return properties;
	}
	
	public static boolean isPasswordChangeEnabled() {
		String value = getProperty(SYSTEM_INFO_COMPONENT_ID,IS_CHANGE_PASSWORD_ENABLED);
		if (value != null) {
    		return Boolean.parseBoolean(getProperty(SYSTEM_INFO_COMPONENT_ID,IS_CHANGE_PASSWORD_ENABLED));
		} else {
			return true;  
		}
	}
	
	
	public static String getProductType(){
		return getSystemPropertyBean().getProductType();
	}
	
	public static String getProductType(String department) {
		Map<String,String> productTypeMap = getPropertyMap(SYSTEM_INFO_COMPONENT_ID, "PRODUCT_TYPE");
		if(productTypeMap.containsKey(department)) return productTypeMap.get(department);
		else if(productTypeMap.containsKey(WILD_CARD)) return productTypeMap.get(WILD_CARD);
		return "";
	}
	
	public static String getPartMaskWildcardFormat(){
		return getSystemPropertyBean().getPartMaskWildcardFormat();
	}
	
	public static String getLoggerName(String componentId) {
		return componentId + getPropertyBean(ApplicationPropertyBean.class, componentId).getNewLogSuffix();
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getComponentIds() {
		return new SortedArrayList<String>(getInstance().getKeys());
	}

	public static boolean isComponentDefined(String componentId) {
        ComponentPropertyDao dao = ServiceFactory.getDao(ComponentPropertyDao.class);
		return dao.findAllByComponentId(componentId) == null ? false : true;
	}
	
	public static String getExtColorExceptionChars(){
		return getSystemPropertyBean().getExtColorExceptionChars();
	}
	
	public static void reset() {
		beans.clear();
		componentIds.clear();
		propertyService = null;
	}
}