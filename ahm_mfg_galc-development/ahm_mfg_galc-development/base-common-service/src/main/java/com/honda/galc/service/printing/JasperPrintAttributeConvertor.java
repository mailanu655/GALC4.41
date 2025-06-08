package com.honda.galc.service.printing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.Base64Coder;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;

public class JasperPrintAttributeConvertor extends AttributeConvertor {
	
	private static final long serialVersionUID = 1L;
	
	public static final char STX = '\u0002';
	public static final char ETX = '\u0003';
	
	public JasperPrintAttributeConvertor(Logger logger) {
		super(logger);
		logger.debug("inside the JasperPrintAttributeConvertor");
	}
	
	public JasperPrintAttributeConvertor(
			List<PrintAttributeFormat> printAttributes) {
		super(printAttributes);
		
	}
	
	public List<String> parseTemplateData(Template template){
		String[] tds = null;
		
		String templateData = template.getTemplateDataString();
		
		try {
			if (templateData != null && (templateData.contains("$P")|| templateData.contains("$F"))) {
				tds = DataContainerXMLUtil.parseJasperXMLString(templateData);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (tds != null)
			return Arrays.asList(tds);
		else
			return null;
		
	}
	
	@Override
	public DataContainer convertFromPrintAttribute(String formId,DataContainer dc) {
		
		logger.debug("JasperconvertFromPrintAttribute method called");
		try{
		List<String> attrList = new ArrayList<String>();
		PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(logger);
		Map<String, PrintAttributeFormat> formPrintAttributes = new HashMap<String, PrintAttributeFormat>();
		formPrintAttributes = getAttributes(formId);
		String templateName = getTemplateName(formId, dc);
		Map<String, PrintAttributeFormat> templatePrintAttributes = new HashMap<String, PrintAttributeFormat>();
		templatePrintAttributes = getAttributes(templateName);
		formPrintAttributes.putAll(templatePrintAttributes);
		Template template = getTemplate(templateName);
		List<String> templateAttributes = parseTemplateData(template);
		List<PrintAttributeFormat> printAttributeFormatList = printAttributeConvertor.checkAttributes(formPrintAttributes, templateAttributes);
		printAttributeFormatList.addAll(printAttributeConvertor.checkExternalAttributes(formPrintAttributes, dc));
		
		for(PrintAttributeFormat printAttribute : printAttributeFormatList) {
			
			// @RL011 - store attribute in the list
			String key = printAttribute.getAttribute();
			attrList.add(key);
			// convert attribute value
			Object attributeValue = convert(printAttribute,dc);
			int attId = printAttribute.getAttributeTypeId();
			if (attId == PrintAttributeType.SQL_COLLECTION.getId() || attId == PrintAttributeType.JPQL_COLLECTION.getId() || attId == PrintAttributeType.JPQL.getId() ) {
				dc.put(key, attributeValue);
				logger.info("convert attribute: " + key + " type: " + printAttribute.getAttributeType() + " value : " + attributeValue);
			} else {
				
			if(attributeValue!= null && String.valueOf(attributeValue).contains(getETX()))
				parseClassAttributeValue(attributeValue.toString(), dc);
			
			attributeValue = checkImageAttributeValue(key, attributeValue == null ? "" : attributeValue.toString());
			String valueStr = (attributeValue == null) ? "" : attributeValue.toString();
			
			dc.put(key,printAttribute.getLength() == 0 ?  valueStr : StringUtils.rightPad(valueStr, printAttribute.getLength()));
			
			logger.info("convert attribute: " + key + " type: " + printAttribute.getAttributeType() + " calculated value : " + valueStr);
			}
		}
		
		dc.put(DataContainerTag.TAG_LIST, attrList);
		} catch(Exception ex){
			ex.printStackTrace();
			logger.error("exception happened at convert print attributes"+ex.getMessage());
		}
		return dc;
	}

	protected void parseClassAttributeValue(String attributeValue,
			DataContainer data) {
		String[] attributeArrayString = attributeValue.split(getETX());
		for (int i = 0; i < attributeArrayString.length; i++) {
			String[] keyvalue = attributeArrayString[i].split(getSTX());
			data.put(keyvalue[0], keyvalue[1]);
		}
	}
	
	public Object checkImageAttributeValue(String attribute, String attributeValue){
		logger.debug("checkImageAttributeValue called with args " + attribute +"," +attributeValue);
		int dotIndex=attributeValue.lastIndexOf('.');
		if(dotIndex>=0) { // to prevent exception if there is no dot
			String extension =attributeValue.substring(dotIndex);
			if(extension.equalsIgnoreCase(".png")|| extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg")
					||extension.equalsIgnoreCase(".gif")||extension.equalsIgnoreCase(".bmp")){

				return attribute+".jpg"+getBase64ByImageName(attributeValue);
			}
		}
		return attributeValue;
	}
	
	public String getBase64ByImageName(String imageName) {
		Template template = ServiceFactory.getDao(TemplateDao.class).findByKey(imageName);
		if(template!= null && template.getTemplateDataBytes()!=null){
			return new String(Base64Coder.encode(template.getTemplateDataBytes()));
		}
		return "";
	}

	public static String getETX() {
		return Character.toString(ETX);
	}

	public static String getSTX() {
		return Character.toString(STX);
	}
	
	
}
