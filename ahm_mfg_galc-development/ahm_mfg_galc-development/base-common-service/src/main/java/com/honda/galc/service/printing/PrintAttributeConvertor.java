package com.honda.galc.service.printing;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.EntityCache;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.PrintAttributeFormatRequiredType;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.Template;
import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductSpecUtil;



/**
 * 
 * <h3>PrintAttributeConvertor Class description</h3>
 * <p> PrintAttributeConvertor description </p>
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
 * Nov 26, 2010
 *
 *
 */
/**
 * @version 2.0
 * @author Gangadhararao Gadde
 * @date Sept 20, 2016
 */
public class PrintAttributeConvertor  {

	private String formId;
	private List<PrintAttributeFormat> printAttributes = new ArrayList<PrintAttributeFormat>();
	private BuildAttributeCache buildAttributeList;
	private EntityCache<EngineSpec,String> engineSpecCache;
	private EntityCache<FrameSpec,String> frameSpecCache;
	private static final String REQUIRED_TYPE_EXCLUSIONS = "REQUIRED_TYPE_EXCLUSIONS";
	private static final String ERROR_MSG = "ERROR";
	private static final String TARGET_PROCESS_POINT = "TARGET_PROCESS_POINT";
	private static final String GENERATE_MATRIX = "GENERATE_MATRIX";


	private List<Object> values = new ArrayList<Object>();
	public Logger logger;
	/**
	 * Constructor
	 */
	public PrintAttributeConvertor(String formId) {
		super();
		this.formId = formId;

		initCache();
		load();
	}

	public PrintAttributeConvertor(List<PrintAttributeFormat> printAttributes) {

		this.printAttributes  = printAttributes;
		initCache();
	}

	public PrintAttributeConvertor(List<PrintAttributeFormat> printAttributes, Logger logger) {
		this.printAttributes = printAttributes;
		this.logger = logger;
		initCache();
	}

	public PrintAttributeConvertor(Logger logger) {
		this.logger = logger;
		initCache();
	}

	private void initCache() {

		this.buildAttributeList = new BuildAttributeCache();
		engineSpecCache = new EntityCache<EngineSpec,String>(getDao(EngineSpecDao.class));
		frameSpecCache = new EntityCache<FrameSpec,String>(getDao(FrameSpecDao.class));

	}

	/**
	 * Now creating PrintAttributeByProductSpecCodeManager only once, outside the for loop.
	 * Create DataContainer for printer.
	 * <p>
	 * @return DataContainer for printer
	 * @param aFormID FormID
	 * @param aData DataContainer
	 * @exception DataConvertException When create printer data, exception occured
	 */
	public DataContainer make(DataContainer aData) { 

		List<String> attrList = new ArrayList<String>();

		values.clear();
		String templateName=aData.get(DataContainerTag.TEMPLATE_NAME)==null?null:aData.get(DataContainerTag.TEMPLATE_NAME).toString();
		boolean replaceAllWithError=false;
		List<String> requiredTypeExclList=new ArrayList<String>();
		if(templateName!=null)
		{	
			requiredTypeExclList = PropertyService.getPropertyList(templateName, REQUIRED_TYPE_EXCLUSIONS);
		}
		// makes DataContainer
		DataContainer dc = new DefaultDataContainer();

		for(PrintAttributeFormat printAttribute : printAttributes) {

			// @RL011 - store attribute in the list
			String key = printAttribute.getAttribute();
			attrList.add(key);
			// convert attribute value
			Object attributeValue = basicConvert(printAttribute,aData);
			String valueStr = (attributeValue == null) ? "" : attributeValue.toString();
			if(templateName!=null&&!requiredTypeExclList.contains(key)&& printAttribute.getRequiredTypeId()!=null && printAttribute.getRequiredTypeId()==PrintAttributeFormatRequiredType.REQUIRED.getId() && attributeValue==null)
			{
				replaceAllWithError=true;
			}

			values.add(attributeValue);

			dc.put(key,printAttribute.getLength() == 0 ?  valueStr : StringUtils.rightPad(valueStr, printAttribute.getLength()));

		}

		if(templateName!=null && replaceAllWithError)
		{
			for(int i=0;i<attrList.size();i++)
			{
				if(!requiredTypeExclList.contains(attrList.get(i)))
				{
					values.set(i,ERROR_MSG);
					dc.put(attrList.get(i), ERROR_MSG);
				}
			}
		}


		dc.put(DataContainerTag.TAG_LIST, attrList);

		return dc;

	}

	private void load() {
		printAttributes = getDao(PrintAttributeFormatDao.class).findAllByFormId(formId);
	}
	
	@SuppressWarnings("unchecked")
	private Object getAttributeValue(PrintAttributeType attrType, Class klass, String methodName,DataContainer data) {

		Object obj = data.get(klass);
		if(obj == null) obj = deriveObject(attrType, data);

		if(obj == null) {
			throw new DataConversionException(attrType.toString() + ": there is no input data for " + klass.getSimpleName());
		}else {
			if(! klass.isAssignableFrom(obj.getClass()))
				throw new DataConversionException(attrType.toString()+ ": invalid object type : " + obj.getClass().getSimpleName() +
						" in the data container. Should be " + klass.getSimpleName());
		}

		Method method = null;
		try {
			method = klass.getMethod(methodName, new Class[]{});
		} catch (Exception e) {
			throw new DataConversionException(klass.getSimpleName() + "does not have method " + methodName + "with 0 argument");
		}

		try {
			return method.invoke(obj, new Object[]{});
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

		return "";


	}

	private Object deriveObject(PrintAttributeType attrType,DataContainer dc) {

		String productSpecCode = dc.getString(DataContainerTag.PRODUCT_SPEC_CODE);
		if(StringUtils.isEmpty(productSpecCode)) return null;
		if(attrType.equals(PrintAttributeType.AttributeByEngineSpec)) {
			return engineSpecCache.findByKey(productSpecCode);
		}else if(attrType.equals(PrintAttributeType.AttributeByFrameSpec)) {
			return frameSpecCache.findByKey(productSpecCode);
		}else if(attrType.equals(PrintAttributeType.AttributeByFrame)){		// used to match the type of AttributeByFrame
			Product product = (Product) dc.get(DataContainerTag.PRODUCT);
			if (product == null)
				return null;
			return product;
		}else if(attrType.equals(PrintAttributeType.AttributeByService)) {
			return new PrintAttributeServiceUtil(dc,logger);
		}
		return null;
	}

	public List<Object> getValues() {
		return values;
	}

	private String findSQLAttribute(String sqlSkelton, DataContainer data) {

		String sql = DataContainerUtil.makeSQL(sqlSkelton, data);

		return getDao(PrintAttributeFormatDao.class).findByNativeQuery(sql);

	}

	protected List<Map<String, Object>> findSqlCollection(String sqlSkelton, DataContainer data) {
		String sql = DataContainerUtil.makeSQL(sqlSkelton, data);
		PrintAttributeFormatDao dao = getDao(PrintAttributeFormatDao.class);
		return dao.findMapCollection(sql);
	}

	protected Object findJpql(String sqlSkelton, DataContainer data) {
		List<?> list = findJpqlCollection(sqlSkelton, data);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	protected List<?> findJpqlCollection(String sqlSkelton, DataContainer data) {
		String sql = DataContainerUtil.makeSQL(sqlSkelton, data);
		GenericDaoService dao = ServiceFactory.getService(GenericDaoService.class);
		return dao.executeGenericSqlSelectQuery(sql);
	}

	private String findAttributeByProductSpecCode(String attribute, String specCode) {

		BuildAttribute buildAttribute = buildAttributeList.findByKey(new BuildAttributeId(attribute,specCode));
		return buildAttribute == null ? "" : buildAttribute.getAttributeValue();

	}

	private String getProductSpecCode(DataContainer dc) {

		if(dc.containsKey(DataContainerTag.ProductSpecCode))
			return (String)dc.get(DataContainerTag.ProductSpecCode);
		else return "";

	}

	private String getCurrentDate(String attribute, String format) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(new Date());
		}catch(RuntimeException ex) {
			throw new DataConversionException("incorrect format defined for attribute : " + attribute + " format : " + format);
		}
	}


	public Object basicConvert(PrintAttributeFormat printAttribute,
			DataContainer data) {

		PrintAttributeType type = printAttribute.getAttributeType();
		String attribute = printAttribute.getAttribute();
		String attributeValue = printAttribute.getAttributeValue();
		// Added to get formId and set it to DC, so that it will be used to find printer config properties.
		data.put(DataContainerTag.FORM_ID, printAttribute.getId().getFormId());
		
		switch (type) {
		case None:
			return "";
		case Static:
			return attributeValue;
		case Tag:
			return StringUtils.isEmpty(attributeValue) ? data.get(attribute)
					: data.get(attributeValue);
		case SQL:
			return findSQLAttribute(attributeValue, data);
		case AttributeByMTOC:
			return findAttributeByProductSpecCode(StringUtils
					.isEmpty(attributeValue) ? attribute : attributeValue,
							getProductSpecCode(data));
		case Date:
			return getCurrentDate(attribute, attributeValue);
		case List:
			return "";
		case Class:
			Class<?> ref = null;
			try {
				ref = Class.forName(printAttribute.getAttributeValue());
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
			PrintAttributeFormatter formatter = null;
			try {
				formatter = (PrintAttributeFormatter) ref.newInstance();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			attributeValue = formatter.execute(data);

			return attributeValue;
		case AttributeByEngineSpec:
			return getAttributeValue(type, EngineSpec.class, attributeValue,
					data);

		case AttributeByFrameSpec:
			return getAttributeValue(type, FrameSpec.class, attributeValue,
					data);

		case AttributeByProduct:
			return getAttributeValue(type, Product.class, attributeValue, data);

		case AttributeByEngine:
			return getAttributeValue(type, Engine.class, attributeValue, data);

		case AttributeByFrame:
			return getAttributeValue(type, Frame.class, attributeValue, data);

		case AttributeByProductionLot:
			return getAttributeValue(type, ProductionLot.class, attributeValue,
					data);
		case SQL_COLLECTION:
			return findSqlCollection(attributeValue, data);

		case JPQL:
			return findJpql(attributeValue, data);

		case JPQL_COLLECTION:
			return findJpqlCollection(attributeValue, data);

		case PartSpecByMTOC:		
			return getPartMarkByPartSpec(type, attributeValue, data);
			
		case AttributeByPartSpec:
			List<LotControlRule> lcrList = ServiceFactory.getDao(LotControlRuleDao.class).findAllByPartName(attribute);
			
			ProductSpecCode productSpec = new ProductSpecCode();
			productSpec.setProductSpecCode(getProductSpecCode(data));
			LotControlRule matchedRule = LotControlPartUtil.findBestMatchedRulesOfSameRuleSequenceByPartNameAndSpec(productSpec, lcrList);

			if(matchedRule == null){
				return StringUtils.EMPTY;
			}
			String partName = matchedRule.getPartNameString();			
			PartByProductSpecCodeDao dao = ServiceFactory.getDao(PartByProductSpecCodeDao.class);
			String partID = dao.getPartId(matchedRule.getProductSpecCode(), partName).getPartSpec().getId().getPartId();
			
			return getPartSpecByPartNameAndPartID(attributeValue, partName, partID);  
			
		case AttributeByService:
			if(data.containsKey(attribute) && !(StringUtils.equalsIgnoreCase(JasperExternalPrintAttributes.TRAY_VALUE.toString(), attribute)
					|| StringUtils.equalsIgnoreCase(GENERATE_MATRIX, attribute))) return data.get(attribute);
			return getAttributeValue(type, PrintAttributeServiceUtil.class, attributeValue, data);
			
		default:
		}
		return "";

	}
	
	private String getPartSpecByPartNameAndPartID(String attributeValue, String partName, String partID) {
		PartSpecDao dao = ServiceFactory.getDao(PartSpecDao.class);
		PartSpec partSpec = dao.findValueWithPartNameAndPartID(partName, partID);
		
		if(attributeValue.equals("getPartMark")) {
			return partSpec.getPartMark();
		}else if(attributeValue.equals("getPartNumber")) {
			return partSpec.getPartNumber();
		}else if(attributeValue.equals("getPartSerialNumberMask")) {
			String parsedMaskString = partSpec.getPartSerialNumberMask().replaceAll("<", "");
			return parsedMaskString.replaceAll(">", "");
		}	
		return "";
	}

	private String getPartMarkByPartSpec(PrintAttributeType type, String partName, DataContainer dc) {
		String processPointId = PropertyService.getProperty("Default_DataMapping", "PART_MARK_VIRTUAL_PROCESS_POINT");		
		Object prd = dc.get(DataContainerTag.PRODUCT);
		
		BaseProduct product = null;
		if(prd instanceof String){
			ProductType productType = (ProductType)dc.get(DataContainerTag.PRODUCT_TYPE);
			ProductDao productDao = ProductTypeUtil.getProductDao(productType==null?ProductType.FRAME:productType);
			product =  (BaseProduct)productDao.findBySn((String)prd);
		}else{
			product = (BaseProduct) prd;
		}
		
		if (product == null)
			return null;
		else {
			BaseProductSpecDao productSpecDao = (BaseProductSpecDao) ProductTypeUtil.getProductSpecDao(product.getProductType());
			BaseProductSpec productSpec = (BaseProductSpec) productSpecDao.findByKey(product.getProductSpecCode());
			boolean isMbpn = ProductTypeUtil.isMbpnProduct(product.getProductType().toString());
			List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPointIdAndPartName(processPointId, partName);
			LotControlRule rule = ProductSpecUtil.getMatchedItem(productSpec.getProductSpecCode(), rules, LotControlRule.class, isMbpn);
			if(rule != null){
			List<PartByProductSpecCode> partByProductSpecCodes = rule.getPartByProductSpecs();
				if (!partByProductSpecCodes.isEmpty()) {
					PartSpec partSpec = partByProductSpecCodes.get(0).getPartSpec();
	
					if (partSpec != null)
						return partSpec.getPartMark();
				}	
			}
		}

		return null;
	}

	private Object getAttributeValueFromObject(Class<?> klass,
			String methodName, DataContainer dc) {
		Object obj = dc.get(klass);
		if (obj == null)
			return null;
		if (!klass.isAssignableFrom(obj.getClass())) {
			return null;
		}
		return getValue(obj, methodName);
	}

	private Object getAttributeValueFromProduct(String methodName,
			DataContainer dc) {
		Product product = (Product) dc.get(DataContainerTag.PRODUCT);
		if (product == null)
			return null;
		return getValue(product, methodName);
	}

	private Object getValue(Object object, String methodName) {
		Method method = null;
		try {
			method = object.getClass().getMethod(methodName, new Class[] {});
		} catch (Exception e) {
			return null;
		}

		try {
			return method.invoke(object, new Object[] {});
		} catch (Exception e) {
			return null;
		}
	}

	private Object getAttributeValueFromProductSpec(String methodName,
			DataContainer dc) {
		ProductSpec productSpec = (ProductSpec) dc
				.get(DataContainerTag.PRODUCT_SPEC);
		if (productSpec == null)
			return null;
		return getValue(productSpec, methodName);
	}

	public Object convertDeviceFormat(DeviceFormat deviceFormat,
			DataContainer data) {

		DeviceTagType type = deviceFormat.getDeviceTagType();
		String attribute = deviceFormat.getTag();
		String attributeValue = deviceFormat.getTagValue();

		switch (type) {
		case NONE:
			return "";
		case STATIC:
			return attributeValue;
		case TAG:
			return StringUtils.isEmpty(attributeValue) ? data.get(attribute)
					: data.get(attributeValue);
		case SQL:
			return findSQLAttribute(attributeValue, data);
		case ATTR_BY_MTOC:
			return findAttributeByProductSpecCode(StringUtils
					.isEmpty(attributeValue) ? attribute : attributeValue,
							getProductSpecCode(data));
		case DATE:
			return getCurrentDate(attribute, attributeValue);
		case LIST:
			return "";
		case CLASS:
			return "";
		case OBJECT:
			return StringUtils.isEmpty(attributeValue) ? data.get(attribute)
					: data.get(attributeValue);
		case DEVICE:
			return data.get(attribute);
		case ATTR_BY_ENGINE_SPEC:
		case ATTR_BY_FRAME_SPEC:
			return getAttributeValueFromProductSpec(attributeValue, data);
		case ATTR_BY_ENGINE:
		case ATTR_BY_FRAME:
			return getAttributeValueFromProduct(attributeValue, data);
		case ATTR_BY_PROD_LOT:
			return getAttributeValueFromObject(ProductionLot.class,
					attributeValue, data);
		default:
		}
		return "";
	}

	public void convertAttributeDeviceFormat(List<DeviceFormat> deviceLst,BaseProduct product,BaseProductSpec productSpec) {

		for (DeviceFormat deviceFormat:deviceLst) { 
			DeviceTagType type = deviceFormat.getDeviceTagType();
			String attribute = deviceFormat.getTag();
			String attributeValue = deviceFormat.getTagValue();
	
			switch (type) {
			case ATTR_BY_MTOC:
				deviceFormat.setTagValue(findAttributeByProductSpecCode(StringUtils.isEmpty(attributeValue) ? attribute : attributeValue,
								productSpec.getProductSpecCode()));
				break;
			case ATTR_BY_ENGINE_SPEC:
			case ATTR_BY_FRAME_SPEC:
				if (productSpec!=null) deviceFormat.setTagValue(""+getValue(productSpec, attributeValue));
				break;
			case ATTR_BY_ENGINE:
			case ATTR_BY_FRAME:
				if (product!=null) deviceFormat.setTagValue(""+getValue(product, attributeValue));
				break;
			case ATTR_BY_PROD_LOT:
				if (product != null ) deviceFormat.setTagValue(""+getValue(getDao(ProductionLotDao.class).findByKey(product.getProductionLot()), attributeValue));
				break;
			case ATTR_BY_TRACK:
				if (product != null&&attributeValue.equals("getNextLineSeq")) {
					int seq = getDao(LineDao.class).findNextLineSeq(product.getTrackingStatus());
					if (seq != -1) deviceFormat.setTagValue(""+seq);
				}else if (product != null&&attributeValue.equals("getLastLineSeq")) {
					int seq = getDao(LineDao.class).findByKey(product.getTrackingStatus()).getLineSequenceNumber();
					if (seq != -1) deviceFormat.setTagValue(""+seq);
				} else deviceFormat.setTagValue(""+getValue(product, attributeValue));
				break;
			default:
			}
		}
	}

	
	public static String getPrintData(DataContainer dc) {
		String printData = null;
		Map<String, String> map = new HashMap<String, String>();
		map.putAll((Map<String, String>) dc.get("KEY_VALUE_PAIR"));
		if (map.isEmpty())
			return printData;
		List<String> att = new ArrayList<String>(map.size());
		List<String> attValue = new ArrayList<String>(map.size());
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String attr = ("@" + pairs.getKey().toString() + "@").toString();
			String attrval = pairs.getValue().toString();
			att.add(attr);
			attValue.add(attrval);
		}
		String[] attArr = att.toArray(new String[map.size()]);
		String[] attValueArr = attValue.toArray(new String[map.size()]);
		String templateData = getTemplateData(dc.get(
				DataContainerTag.TEMPLATE_NAME).toString());
		return StringUtils.isEmpty(templateData) ? templateData : StringUtils
				.replaceEach(templateData, attArr, attValueArr);
	}

	private static String getTemplateData(String template) {
		String templateData = null;
		Template templateObj = null;
		TemplateDao templateDao = ServiceFactory.getDao(TemplateDao.class);
		templateObj = templateDao.findTemplateByTemplateName(template.trim());
		if (templateObj != null && templateObj.getTemplateDataBytes() != null) {
			templateData = templateObj.getTemplateDataString();
		}
		return templateData;
	}

	public List<PrintAttributeFormat> checkAttributes(
			Map<String, PrintAttributeFormat> printAttributeFormats,
			List<String> templateAttributesList) {

		List<PrintAttributeFormat> printAttributeFormatList = new ArrayList<PrintAttributeFormat>();
		Iterator<Entry<String, PrintAttributeFormat>> it = printAttributeFormats.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, PrintAttributeFormat> pairs = it.next();
			Logger.getLogger().info(pairs.getKey() + " = " + pairs.getValue());
			if (templateAttributesList != null) {
				for (String st : templateAttributesList) {
					if (st.equalsIgnoreCase(pairs.getKey().toString())) {
						printAttributeFormatList
						.add((PrintAttributeFormat) pairs.getValue());
					}
				}
			}
		}
		return printAttributeFormatList;
	}

	public List<PrintAttributeFormat> checkExternalAttributes(Map<String, PrintAttributeFormat> printAttributeFormats, DataContainer dc) {
		List<PrintAttributeFormat> printAttributeFormatList = new ArrayList<PrintAttributeFormat>();
		Iterator<Entry<String, PrintAttributeFormat>> it = printAttributeFormats.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, PrintAttributeFormat> pairs = it.next();
			JasperExternalPrintAttributes attribType = EnumUtil.getType(JasperExternalPrintAttributes.class, pairs.getKey().toString());
			if (attribType != null) {
				printAttributeFormatList.add(pairs.getValue());
			}
		}
		return printAttributeFormatList;
	}

}
