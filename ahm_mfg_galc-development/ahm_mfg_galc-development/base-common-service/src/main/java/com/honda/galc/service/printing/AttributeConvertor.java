package com.honda.galc.service.printing;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.script.DataUtil;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.PrintAttributeFormatRequiredType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Template;
import com.honda.galc.enumtype.EnumUtil;


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
public class AttributeConvertor implements IPrintAttributeConvertor {

	private BuildAttributeCache buildAttributeList;
	public Logger logger;
	private static final String REQUIRED_TYPE_EXCLUSIONS = "REQUIRED_TYPE_EXCLUSIONS";
	private static final String ERROR_MSG = "ERROR";
	
/**
 * Constructor
 */
public AttributeConvertor(Logger logger) {
	this.logger = logger;
	initCache();
}

public AttributeConvertor(List<PrintAttributeFormat> printAttributes) {
	initCache();
}

private void initCache() {
	this.buildAttributeList = new BuildAttributeCache();
}

public List<String> parseTemplateData(String templateData) {
	String[] tds = null;
	try {
		if (templateData != null && templateData.contains("@")) {
			tds = StringUtils.substringsBetween(templateData, "@", "@");
			Set set = new HashSet(Arrays.asList(tds));
			tds = (String[]) (set.toArray(new String[set.size()]));
		}
					
	} catch (Exception exception) {
		exception.printStackTrace();
	}
	if (tds != null)
		return Arrays.asList(tds);
	else
		return null;
}

public DataContainer convertFromPrintAttribute(String clientId,DataContainer dc) {
	logger.debug("convertFromPrintAttribute in attribute convertor");
	List<String> attrList = new ArrayList<String>();
	List<Object> values = new ArrayList<Object>();
	PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(logger);
	Map<String, PrintAttributeFormat> formPrintAttributes = new HashMap<String, PrintAttributeFormat>();
	formPrintAttributes = getAttributes(clientId);
	String templateName = getTemplateName(clientId, dc);
	List<String> requiredTypeExclList =new ArrayList<String>();
	if(templateName!=null)
	{
		requiredTypeExclList = PropertyService.getPropertyList(templateName, REQUIRED_TYPE_EXCLUSIONS);
	}
	boolean replaceAllWithError=false;

	Map<String, PrintAttributeFormat> templatePrintAttributes = new HashMap<String, PrintAttributeFormat>();
	templatePrintAttributes = getAttributes(templateName);
	formPrintAttributes.putAll(templatePrintAttributes);
	Template template = getTemplate(templateName);
	List<PrintAttributeFormat> printAttributeFormatList;
	if(template != null ){
		List<String> templateAttributes = parseTemplateData(template.getTemplateDataString());
		printAttributeFormatList = printAttributeConvertor.checkAttributes(formPrintAttributes, templateAttributes);
		printAttributeFormatList = checkExternalAttributes(formPrintAttributes,dc,printAttributeFormatList);
	}else {
		printAttributeFormatList = new ArrayList<PrintAttributeFormat>();
		for(Map.Entry<String, PrintAttributeFormat> entry : formPrintAttributes.entrySet()) {
			printAttributeFormatList.add(entry.getValue());
		}
	}
	
	for(PrintAttributeFormat printAttribute : printAttributeFormatList) {
		
		// @RL011 - store attribute in the list
		String key = printAttribute.getAttribute();
		attrList.add(key);
		// convert attribute value
		Object attributeValue = convert(printAttribute,dc);
		if(templateName!=null&&!requiredTypeExclList.contains(key)&& printAttribute.getRequiredTypeId()!=null && printAttribute.getRequiredTypeId()==PrintAttributeFormatRequiredType.REQUIRED.getId() && attributeValue==null)
		{
			replaceAllWithError=true;
		}
		
		String valueStr = (attributeValue == null) ? "" : attributeValue.toString();
		
		values.add(attributeValue);
 		
		dc.put(key,printAttribute.getLength() == 0 ?  valueStr : StringUtils.rightPad(valueStr, printAttribute.getLength()));
		logger.info("convert attribute: " + key + " type: " + printAttribute.getAttributeType() + " calculated value : " + attributeValue);
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

public static List<PrintAttributeFormat> checkExternalAttributes(
		Map<String, PrintAttributeFormat> printAttributeFormats,
		DataContainer dc,List<PrintAttributeFormat> printAttributeFormatList) {
	//List<PrintAttributeFormat> printAttributeFormatList = new ArrayList<PrintAttributeFormat>();
	Iterator it = printAttributeFormats.entrySet().iterator();
	while (it.hasNext()) {
		Map.Entry pairs = (Map.Entry) it.next();

		if (DataUtil.isInEnum(pairs.getKey().toString(),
				JasperExternalPrintAttributes.class)) {
			switch (EnumUtil.getType(JasperExternalPrintAttributes.class,
					pairs.getKey().toString())) {
			case PRINT_QUANTITY:
				printAttributeFormatList.add((PrintAttributeFormat) pairs
						.getValue());
				break;
			default:
				break;
			}
		}
	}
	return printAttributeFormatList;
}
protected Template getTemplate(String templateName){
	logger.debug("templateName:"+ templateName);
	return ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
}

protected Map<String, PrintAttributeFormat> getAttributes(String form) {
	List<PrintAttributeFormat> printAttributeFormatList = null;
	Map<String, PrintAttributeFormat> printAttributes = new LinkedHashMap<String, PrintAttributeFormat>();
	try{
		PrintAttributeFormatDao printAttributeFormatDao = ServiceFactory.getDao(PrintAttributeFormatDao.class);
		printAttributeFormatList = printAttributeFormatDao.findAllByFormId(form);
		for(PrintAttributeFormat pf: printAttributeFormatList) {
			// Skip print attributes with negative length.
			// It allows to skip conditional attributes for printouts that do not have templates.
			if (pf.getLength() < 0) {
				continue;
			}
			printAttributes.put(pf.getAttribute(), pf);
		}
	} catch(Exception ex){
		logger.debug("exception occured at getAttributes");
		ex.printStackTrace();
	}
	return printAttributes;
}

protected String getTemplateName(String formId, DataContainer dc) {
	String templateID = mapAttribute(formId,dc.getString(DataContainerTag.PRODUCT_SPEC_CODE));
	if(StringUtils.isEmpty(templateID)) return formId;
	return templateID;
}

protected String mapAttribute(String attribute, String productSpecCode) {
	BuildAttribute buildAttribute = StringUtils.isEmpty(productSpecCode)? null : buildAttributeList.findById(productSpecCode,attribute);
	return buildAttribute == null ? attribute : buildAttribute.getAttributeValue();
}

public DataContainer convertFromDeviceDataFormat(String clientId,DataContainer dc) {
	
	logger.info("Converting device data format for device id : " + clientId);
	try{
	PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(logger);
	List<DeviceFormat> deviceFormats = getDao(DeviceFormatDao.class).findAllByDeviceId(clientId);
	
	List<String> attrList = new ArrayList<String>();
	List<Object> values = new ArrayList<Object>();
	
	for(DeviceFormat deviceFormat : deviceFormats) {
		
		// @RL011 - store attribute in the list
		String key = deviceFormat.getTag();
		attrList.add(key);
		// convert attribute value
		Object attributeValue = printAttributeConvertor.convertDeviceFormat(deviceFormat,dc);
		String valueStr = (attributeValue == null) ? "" : attributeValue.toString();
		
		// if length < 0 , we want to ignore. This is for the case we want to add some config 
		//  in device format 

		if(deviceFormat.getLength() < 0) continue;
		
		values.add(attributeValue);
		if(deviceFormat.getDeviceTagType().toString().equalsIgnoreCase("OBJECT")){
			if(attributeValue!=null)
				dc.put(key,attributeValue);
		}else{
			if(deviceFormat.getLength() == 0) {
				dc.put(key,valueStr);
			}else if(deviceFormat.getDeviceDataType().equals(DeviceDataType.INTEGER)) {
					dc.put(key, StringUtils.leftPad(valueStr, deviceFormat.getLength(),"0"));
			}else  {
				dc.put(key, StringUtils.rightPad(valueStr, deviceFormat.getLength()));
			}
		}
		logger.info("convert attribute: " + key + " type: " + deviceFormat.getDeviceTagType() + " calculated value : " + attributeValue);
	}
	
	dc.put(DataContainerTag.TAG_LIST, attrList);
	}catch(Exception ex){
		ex.printStackTrace();
		logger.error("exception occured at convert print attributes"+ ex.getMessage());
	}
	return dc;
}

protected Object convert(PrintAttributeFormat printAttribute, DataContainer data) {
	try {
		PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(logger);
		return printAttributeConvertor.basicConvert(printAttribute,data);
	}catch(Exception e) {
		logger.error("Failed to convert " + printAttribute + " due to : " + e.getMessage());
	}
	return null;
}

}
