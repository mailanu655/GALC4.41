package com.honda.galc.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.oif.EntitySequenceInterface;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>EntityBuilder</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuilder description </p>
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
 * Jun. 9, 2014
 *
 */
public class EntityBuilder {
	static final String productPackage = "com.honda.galc.entity.product.";
	static final String srvIntPackage = "com.honda.galc.dao.product.";
	private static final String SUBSTR_PATTERN="([a-zA-Z0-9_\\-]*)(\\()(\\d+)(\\,)(\\d+)(\\))";
	
	protected Map<String, String> fieldsMap = new HashMap<String, String>();
	protected Map<String, String> constMap = new HashMap<String, String>();
	protected Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	protected EntityBuildPropertyBean propertyBean;
	Class<?> clzz; //Current entity class
	protected Logger logger;
	protected List<FileFieldDef> defList = new ArrayList<FileFieldDef>();
	private BuildAttributeCache baCache;
	private EntityBuilderSequenceManager seqManager;
	
	
	@SuppressWarnings("unchecked")
	private Map<Class, Map<String, Method>> entitySetterMap = new HashMap<Class, Map<String,Method>>();
	private Map<String, Set<Object>> uniqSpace = new HashMap<String, Set<Object>>();
	@SuppressWarnings("unchecked")
	Map<Class<?>, IDaoService> daoMap = new HashMap<Class<?>, IDaoService>();
	
	public EntityBuilder(EntityBuildPropertyBean propertyBean, Logger logger) {
		super();
		this.propertyBean = propertyBean;
		this.logger = logger;
		
		init();
	}

	private void init() {
		for(Entry<String, String> e : propertyBean.getFieldDefs().entrySet()){
			try {
				FileFieldDef def = new FileFieldDef(e.getKey(), e.getValue());
				defList.add(def);
			} catch(Exception ex){
				logger.error(ex, "Invalid OIF field configuration:", e.getKey(), ":", e.getValue());
			}
		}
		
		//extract properties
		initConstantFields();
		baCache = new BuildAttributeCache();
		
	}

	private void initConstantFields() {
		//put some global constant data
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		Date currentDate = new Date(System.currentTimeMillis());
		constMap.put(TagNames.CURRENT_TIMESTAMP.name(), CommonUtil.format(timeStamp));
		constMap.put(TagNames.CURRENT_DATE.name(), CommonUtil.format(currentDate));
		
		
		for(FileFieldDef def : defList){
			if(def.isProperty()){
				for(Method m : propertyBean.getClass().getMethods())
					if(def.isProperty(m.getName())) {
						try {
							constMap.put(def.getName(), (String) m.invoke(propertyBean, new Object[] {}));
						} catch (Exception e) {
							logger.warn(e, " Failed to get property:" + def.getName());
						}
					}
			}else if(def.isConstant()){
				constMap.put(def.getName(), def.getExpression());
				
			}
			
		}
	}

	public void emitFields(String currentLine) {
		
		for(FileFieldDef def : defList){
			try {
				if(def.isField()){
					fieldsMap.put(def.getName(), currentLine.substring(def.getStart(), def.getEnd()));
					
					if(def.isKey() && StringUtils.isEmpty(fieldsMap.get(def.getName())))
						throw new Exception("Error: Empty Key field!");
				}
			} catch (Exception e) {
				logger.error(e, "Exception to emit field:", def.toString(), " for line:" + currentLine);
			}
		}
		
		
	}
	
	private Class<?> getClzz(String pacakage, String entityName) {
		if(classMap.containsKey(entityName))
			return classMap.get(entityName);
		else{
			try {
				Class<?> entityClzz = Class.forName(pacakage + entityName);
				classMap.put(entityName, entityClzz);
				return entityClzz;
			} catch (Exception e) {
				logger.error(e, "Invalid entity name:", pacakage, entityName);
				return null;
			}
		}
	}
	
	private IEntity createEntity() throws Exception {
        
		Constructor<?> constructor = clzz.getConstructor(new Class[]{});
		Object newObj = constructor.newInstance(new Object[]{});
		
		Map<String, Method> nameMethodMap = getClassSetterMap(clzz);
		
		for(Entry<String, Method> e : nameMethodMap.entrySet()) {

			try {
				Object args = extractArgs(e.getKey(), e.getValue().getParameterTypes()[0], null);
				if(args != null) e.getValue().invoke(newObj, args);
			} catch (Exception ex) {
				logger.warn(ex, " Exception to invoke:" + e.getKey() + " method:" + e.getValue().getName() + " class:" + clzz.getSimpleName());
			}
		}
		return  (IEntity)newObj;
	}


	@SuppressWarnings("unchecked")
	private Map<String, Method> getClassSetterMap(Class<?> clzz) {
		if(entitySetterMap.keySet().contains(clzz)) return entitySetterMap.get(clzz);
		else{
			Map<String, Method> setterMap = new  HashMap<String, Method>();
			
			Class cl = clzz;
	        while (cl != AuditEntry.class){
	        	getSetters(cl, setterMap, null);
	        	cl = cl.getSuperclass();
	        }
			
			entitySetterMap.put(clzz, setterMap);
			
			return setterMap;
		}

	}
	
	@SuppressWarnings("unchecked")
	private void getSetters(Class cl, Map<String, Method> map, Class rcl) {
		Field[] fields = rcl == null ? cl.getDeclaredFields() : rcl.getDeclaredFields();
		for(Field f : fields){
			Column clmn = f.getAnnotation(Column.class);
			if(clmn != null)
				try{
					Method m = getMethod(cl, getSetMethod(clmn.name()));
					if(m != null)
						map.put(clmn.name(), m);
					
				} catch(Exception e){
					logger.warn(e, "Failed to get Method:" + getSetMethod(f.getName()));
				}
			else if(f.getAnnotation(Id.class) != null || f.getAnnotation(EmbeddedId.class) != null)
				getSetters(cl, map, f.getType());
		}
	}

	private Method getMethod(Class<?> clzz, String methodName) {
		for(Method m : clzz.getMethods()) // the method name must be uniq
			if(m.getName().equals(methodName)) return m;
		return null;
	}

	public String getSetMethod(String name) {

		return getMethodName(name, EntityBuildMethodSpec.Method_Prefix.set.name());
	}

	public String getGetMethod(String name) {
		return getMethodName(name, EntityBuildMethodSpec.Method_Prefix.get.name());
	}

	private String getMethodName(String name, String method) {
		StringBuilder sb  = new StringBuilder(method);
		for(String s : name.split(Delimiter.UNDERSCORE))
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(s)));
		
		return sb.toString();
	}

	private Object extractArgs(String key, Class<?> parmType, String defaultValue) throws Exception {
		
		String fieldValueStr = getFieldValueStr(clzz.getSimpleName() + Delimiter.DOT + key);
		if(fieldValueStr == null) fieldValueStr = getFieldValueStr(key);
		if(fieldValueStr == null) fieldValueStr = defaultValue;
		
		return EntityBuilderHelper.convert(StringUtils.isBlank(fieldValueStr)? fieldValueStr : StringUtils.trim(fieldValueStr), parmType);
	}

	

	private String getFieldValueStr(String key) {
		
		if(constMap.containsKey(key)) 
			return constMap.get(key);
		else
			return fieldsMap.get(key);
		
	}

	protected void extractGalcProperties() {
		for(FileFieldDef def : defList){
			if(def.isBuildAttribute())
				fieldsMap.put(def.getName(), getBuildAttribute(def.getExpression()));
		}
		
	}

	private String getBuildAttribute(String attrName) {
		BuildAttribute ba = baCache.findById(getFieldValueStr(TagNames.PRODUCT_SPEC_CODE.name()),attrName);
		if(ba == null) {
			logger.warn("WARN: Failed to find build attribute:", attrName, "product_spec_code:", getFieldValueStr(TagNames.PRODUCT_SPEC_CODE.name()));
			return  null;
		} else 
			return baCache.findById(getFieldValueStr(TagNames.PRODUCT_SPEC_CODE.name()), attrName).getAttributeValue();
	}

	protected String getFieldsLogInfo() {
		StringBuilder sb = new StringBuilder().append("fields[");
		for(Entry<String, String> e :   fieldsMap.entrySet()){
			if(sb.length() > 0) sb.append(Delimiter.COMMA);
			sb.append(e.getKey()).append(":").append(e.getValue());
		}
		
		sb.append("]; constants[");
		for(Entry<String, String> ec : constMap.entrySet()){
			if(sb.length() > 0) sb.append(Delimiter.COMMA);
			sb.append(ec.getKey()).append(":").append(ec.getValue());
		}
			
		sb.append("]");
		return sb.toString();
	}

	protected void processExpression() {
		processExpression(defList, false);			
	}
	
	private void processExpression(List<FileFieldDef> expList, boolean override){
		for(FileFieldDef def : expList){
			try {
				if(def.isExpression() && !(!override && fieldsMap.containsKey(def.getName()))){
					fieldsMap.put(def.getName(), evaluateExpression(def.getExpression()));
					logger.debug("name:", def.getName(), " expression:", def.getExpression(), " result:", fieldsMap.get(def.getName()));
				} else if(def.isMethodInvocation()){
					Object returnObj = invokeMethod(def.getMethodSpec());
					if(returnObj != null)
						fieldsMap.put(def.getName(), returnObj.toString());
					
					logger.debug("name:", def.getName(), " expression:", def.getMethodSpec().getMethodName(), " result:", fieldsMap.get(def.getName()));
				}
				    
			} catch (Exception e2) {
				logger.error(e2, "Invalid OIF File Field definition:", def.toString());
			}
		}
	}

	private Object invokeMethod(EntityBuildMethodSpec methodSpec) {
		try {
			Object obj = "Dao".equalsIgnoreCase(methodSpec.getClassName()) ? getDao() : getObject(methodSpec.getClazz()) ;
			Method method = getMethod(obj.getClass(), methodSpec.getMethodName());
			Object[] params = getParams(methodSpec.getParamsArray(), method.getParameterTypes());
			Object returnObj = method.invoke(obj, params);
			
			if(method.getReturnType() != null)
				fieldsMap.put(methodSpec.getNameToken(), returnObj.toString());
			return returnObj;
			
		} catch (Exception e) {
			Logger.getLogger().error(e, " Exception to invoke method:",methodSpec.getMethodName()," Class:",methodSpec.getClazz().getName());
		}
		return null;
	}

	private Object getObject(Class<?> clazz) {
		return clazz == null ?  this : ReflectionUtils.createInstance(clazz);
	}

	private Object[] getParams(String[] argStr, Class<?>[] classes) throws Exception {
		List<Object> list = new ArrayList<Object>();
		if(argStr.length != classes.length) throw new TaskException("Invalid method specified.");
		
		for(int i = 0; i < classes.length; i++){
			list.add(extractArgs(argStr[i], classes[i], argStr[i]));
		}
		
		return list.toArray();
	}

	private String evaluateExpression(String exp) {
		StringBuilder sb = new StringBuilder();
		for (String s : exp.split("\\+")){
			
			if(StringUtils.isBlank(s)){
				sb.append(s);
				continue;
			}
			
			s = StringUtils.trimToEmpty(s);
			
			Pattern mpatern = Pattern.compile(SUBSTR_PATTERN);
			Matcher matcher = mpatern.matcher(s);
			if(matcher.matches()){
				int start = Integer.valueOf(matcher.group(3));
				String value = getFieldValueStr(matcher.group(1)).substring(start, start+Integer.valueOf(matcher.group(5)));
				sb.append(value);
			}else{
				if(fieldsMap.containsKey(s) || constMap.containsKey(s))
					sb.append(getFieldValueStr(s));
				else {
					FileFieldDef sDef = getDefinition(s);
					if(sDef == null) 
						logger.warn("WARN: Invalid definition:" + s);
					else if(sDef.isExpression()){
						fieldsMap.put(sDef.getName(), evaluateExpression(sDef.getExpression()));
						sb.append(getFieldValueStr(s));
					}
				}

			}
		}
		
		return sb.toString();
	}
	
	private FileFieldDef getDefinition(String s) {
		for(FileFieldDef def : defList)
			if(def.getName().equalsIgnoreCase(s))
				return def;
		
		return null;
	}
	
	// ----------builder functions -----------
	public Object buildEntity(String entityName) {
		try {
			clzz = getClzz(productPackage, entityName);
			EntityBuildSpec spec = getEntityBuildSpec(entityName);
			
			switch(spec.getType()){
			case ONE_TO_ONE:
				return buildSingleEntity(spec);
			case ONE_TO_MANY:
				return buildEntities(spec);
			}

		} catch (SystemException se){
			throw se;
		} catch (Exception e) {
			logger.error(e, " Exception to create entity:", entityName);
		}
		
		return null;
	}

	private List<IEntity> buildEntities(EntityBuildSpec spec) {
		
		int size = Integer.parseInt(getFieldValueStr(spec.getSize()));
		List<IEntity> productList = new ArrayList<IEntity>();
		
		if(spec.isUseSubIds()){
			String subIdsAttr = getBuildAttribute(BuildAttributeTag.SUB_IDS);
			String[] subIds = subIdsAttr.split(Delimiter.COMMA);
			for(String subId : subIds){
				fieldsMap.put(TagNames.SUB_ID.name(), subId);
				productList.addAll(buildInitalEntities(size, spec));
			}
			
		} else {
			productList.addAll(buildInitalEntities(size, spec));
		}
		
		return productList;
	}


	private List<IEntity> buildInitalEntities(int size, EntityBuildSpec spec) {
		List<IEntity> list = new ArrayList<IEntity>();
		for(int i = 0; i <size ; i++){
			fieldsMap.put(TagNames.ENTITY_CREATION_COUNT.name(), "" + (i +1));
			if(spec.getFieldDefs().size() > 0) processExpression(spec.getFieldDefs(), true);
			
			try {
				IEntity newEntity = createEntity();
				if (newEntity != null)	list.add(newEntity);
			} catch (Exception e) {
				logger.error(e, "Exception to build entity:" + clzz.getSimpleName());
			}
		}
		return list;
	}

	private EntityBuildSpec getEntityBuildSpec(String entityName) {
		
		return new EntityBuildSpec(propertyBean.getEntityBuildSpecs() == null ? "" : propertyBean.getEntityBuildSpecs().get(entityName));
	}


	private IEntity createPreProductionLotEntity() throws Exception {
		//This is the agreement: production control will not guarantee not generating the same production lot. 
		//Once find a lot already exist, then log error and exit OIF.
		if(propertyBean.isCheckProcessedLot()){
			PreProductionLot existingLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(fieldsMap.get(TagNames.PRODUCTION_LOT.name()));
			if(existingLot != null)
			   throw new SystemException("Production Lot:" + existingLot.getProductionLot() + " already exist!");
		}
		
		PreProductionLot preproductionLot = (PreProductionLot)createEntity();
		if(preproductionLot == null) return preproductionLot;
		
		updateSequence(preproductionLot);
		
		return preproductionLot;
	}

	private void updateSequence(PreProductionLot preproductionLot) {
		// scenario 1. sequence from input: for example, 1000, 2000, 3000,...
		// scenario 2. entity builder generate sequence by configuration property

		if(!getSequenceMap().keySet().contains(preproductionLot.getPlanCode())){
			Double maxSequence = getMaxPreProductionLotSequence();
			getSequenceMap().put(preproductionLot.getPlanCode(), maxSequence != null ? maxSequence : 0.0);
		}
		
		if(!fieldsMap.keySet().contains(TagNames.SEQUENCE.name())){//scenario 2
			preproductionLot.setSequence(getSequenceMap().get(preproductionLot.getPlanCode()) + propertyBean.getSequenceInterval());
			getSequenceMap().put(preproductionLot.getPlanCode(), preproductionLot.getSequence());
		} else 
			preproductionLot.setSequence(getSequenceMap().get(preproductionLot.getPlanCode()) + preproductionLot.getSequence());
		
		
	}

	private Map<String, Double> getSequenceMap() {
		if(seqManager == null) 
			seqManager = new EntityBuilderSequenceManager();
		
		return seqManager.getMap(clzz);
	}

	public Double getSequenceNumber(){
		if(!getSequenceMap().keySet().contains(getFieldValueStr(TagNames.PLAN_CODE.name()))){
			Double maxSequence = getMaxPreProductionLotSequence();
			getSequenceMap().put(getFieldValueStr(TagNames.PLAN_CODE.name()), maxSequence != null ? maxSequence : 0.0);
		} 
		
		return getSequenceMap().get(getFieldValueStr(TagNames.PLAN_CODE.name()));
	}
	
	public Double generateSequenceNumber(String number){
		if(!StringUtils.isEmpty(number)){
			if(!StringUtils.isEmpty(getFieldValueStr(number))) //passed in a token, already increased by out side source
				return getSequenceNumber() + Double.parseDouble(number);
			else {//passed in a decimal string for example "100"
				Double seq = getSequenceNumber() + Double.parseDouble(number);
				getSequenceMap().put(getFieldValueStr(TagNames.PLAN_CODE.name()), seq);
				return seq;
			}
		} else {
			Double seq = getSequenceNumber() + propertyBean.getSequenceInterval(); //increase by each round by interval
			getSequenceMap().put(getFieldValueStr(TagNames.PLAN_CODE.name()), seq);
			return seq;
		}
	}
	
	public Double manageSequence(String baseNumber, String addUp){
	
		//passed in Token, Number or actual value
		Double baseNum = 0.0;
		if(!StringUtils.isEmpty(getFieldValueStr(baseNumber)))
			baseNum = Double.parseDouble(getFieldValueStr(baseNumber));
		
		return baseNum + Double.parseDouble(addUp);
	}

	private Double getMaxPreProductionLotSequence() {
		//findMaxSequence method must be defined in the Dao.
		String planCode = (clzz.getSimpleName().equals("SubProductShipping")) ?
				getFieldValueStr(TagNames.PRODUCT_TYPE.name()): getFieldValueStr(TagNames.PLAN_CODE.name());
		return ((EntitySequenceInterface)getDao()).findMaxSequence(planCode);
	}

	private IEntity buildSingleEntity(EntityBuildSpec spec) throws Exception {
		
		if(spec.getFieldDefs().size() > 0) processExpression(spec.getFieldDefs(), true);
		if (PreProductionLot.class.isAssignableFrom(clzz))
			return createPreProductionLotEntity();
		
		IEntity entityObj = createEntity();	
		if(!spec.isUniq()) return entityObj;
		
		//same Product Sepc Code may be included in multiple lines, so only need to insert once into database
		Set<Object> uniqSet = getUniqSet(entityObj.getClass().getSimpleName());
		if(!uniqSet.contains(entityObj.getId())){
			uniqSet.add(entityObj.getId());
			return entityObj;
		} else
			logger.debug(entityObj.getClass().getSimpleName(), " Id:" + entityObj.getId(), " is already processed skipped.");
			
		return null;
	}

	private Set<Object> getUniqSet(String key) {
		if(uniqSpace.get(key) == null){
			Set<Object> set = new HashSet<Object>();
			uniqSpace.put(key, set);
		}
		return uniqSpace.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public IDaoService getDao() {
		if(daoMap.containsKey(clzz)) return daoMap.get(clzz);
		else {
		    IDaoService dao = ServiceFactory.getDao((Class<? extends IDaoService>)getClzz(srvIntPackage, clzz.getSimpleName() + "Dao"));
		    daoMap.put(clzz, dao);
		    return dao;
		}
	}
	
}
