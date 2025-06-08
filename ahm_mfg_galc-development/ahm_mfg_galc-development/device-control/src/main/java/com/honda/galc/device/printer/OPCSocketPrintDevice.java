package com.honda.galc.device.printer;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.TemplateType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Template;
import com.honda.galc.net.DataContainerSocketSender;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.printing.CompiledJasperPrintAttributeConvertor;
import com.honda.galc.service.printing.IPrintAttributeConvertor;
import com.honda.galc.service.printing.JasperPrintAttributeConvertor;

/**
 * @author Subu Kathiresan
 * Feb 24, 2014
 */
public class OPCSocketPrintDevice extends AbstractPrintDevice {

	private String formId = "";
	private String productSpecCode = "";

	public boolean print(String textToPrint, int printQty, String vin) {
		try {
			DataContainer dc= new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, vin);
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE, productSpecCode);
			dc.put(DataContainerTag.FORM_ID, formId);
			send(calculateAttributes(dc));
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not print to device: " + getDestinationPrinter());
			return false;
		}
		return true;
	}

	private DataContainer send(DataContainer dc) {		
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(getDestinationPrinter());
		if(device == null) {
			getLogger().error("Could not find the device id " + getDestinationPrinter());
			return null;
		}
		dc.setClientID(device.getClientId());
		DataContainer outDc = createOutputDataContainer(dc);
		DataContainerSocketSender dcSender = new DataContainerSocketSender(device.getEifIpAddress(),device.getEifPort());
		if(StringUtils.isEmpty(device.getReplyClientId())) {
			dcSender.send(outDc);
			return null;
		} else {
			return dcSender.syncSend(outDc);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected DataContainer createOutputDataContainer(DataContainer dc){
		DataContainer outDc = new DefaultDataContainer();
		outDc.setClientID(dc.getClientID());
		List<String> tags = (List<String>) dc.get(DataContainerTag.TAG_LIST);
		for(String tag : tags){
			outDc.put(tag, dc.get(tag));
		}
		return outDc;
	}
	
	protected DataContainer calculateAttributes(DataContainer dc) {
		return getDataAssembler(getTemplate(getTemplateName(dc))).convertFromPrintAttribute(getName(), dc);
	}
	
	private IPrintAttributeConvertor getDataAssembler(Template template) {
		if(TemplateType.JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new JasperPrintAttributeConvertor(getLogger());
		else if(TemplateType.COMPILED_JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new CompiledJasperPrintAttributeConvertor(getLogger());
		else 
			return new AttributeConvertor(getLogger());
	}
	
	private String getTemplateName(DataContainer dc) {
		String templateID = mapAttribute(dc.getString(DataContainerTag.FORM_ID),dc.getString(DataContainerTag.PRODUCT_SPEC_CODE));
		if(StringUtils.isEmpty(templateID)) return dc.getString(DataContainerTag.FORM_ID);
		return templateID;
	}
	
	private String mapAttribute(String attribute, String productSpecCode) {
		BuildAttribute buildAttribute = new BuildAttributeCache().findById(productSpecCode,attribute);
		return buildAttribute == null ? attribute : buildAttribute.getAttributeValue();
	}
	
	private Template getTemplate(String templateName){
		return ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
	}

	public HashMap<String, IPrintDeviceListener> getListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean registerListener(String applicationId,
			IPrintDeviceListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean requestControl(String applicationId,
			IPrintDeviceListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unregisterListener(String applicationId,
			IPrintDeviceListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getprintData(DataContainer dc) {
		productSpecCode = dc.get(DataContainerTag.PRODUCT_SPEC_CODE).toString();
		formId = dc.get(DataContainerTag.FORM_ID).toString();
		return "";
	}

}
