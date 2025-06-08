package com.honda.galc.service.broadcast;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.entity.enumtype.TemplateType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.IPrinterDevice;
import com.honda.galc.service.printing.MQPrintDevice;
import com.honda.galc.service.printing.MqJasperPrintDevice;
import com.honda.galc.service.printing.PrintQueuePrinterDevice;
import com.honda.galc.service.printing.SocketPrinterDevice;

/**
 * 
 * <h3>PrinterBroadcast Class description</h3>
 * <p> PrinterBroadcast description </p>
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
 * Aug 20, 2013
 *
 *
 */
public class PrinterBroadcast extends AbstractBroadcast{

	private BuildAttributeCache buildAttributeList;
	
	
	public PrinterBroadcast(BroadcastDestination destination, String processPointId,DataContainer dc) {
		super(destination, processPointId, dc);
		buildAttributeList = new BuildAttributeCache();
	}

	@Override
	public DataContainer send(DataContainer dc) {
		logger.debug("send in PrinterBroadcast");
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(destination.getDestinationId());
		if(device == null) {
			DataContainerUtil.error(logger, dc, "Could not find device for id: " + destination.getDestinationId());
			return dc;
		}
		
		String queueName = mapQueueName(device.getClientId(), dc.getString(DataContainerTag.PRODUCT_SPEC_CODE));
		
		String formId = getFormId();
		logger.debug("formId:"+ formId);
		if(StringUtils.isEmpty(formId)) {
			DataContainerUtil.error(logger, dc, "Form Id is not configured");
			return dc;  
		}
		
		String templateName = getTemplateName(formId);
		logger.debug("templateName:"+ templateName);
		dc.put(DataContainerTag.FORM_ID, formId);
		dc.put(DataContainerTag.TEMPLATE_NAME, templateName);
		dc.put(DataContainerTag.QUEUE_NAME, queueName);
		
		Template template = null;
		if(!StringUtils.isEmpty(templateName)) {
			template = ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
		}
		
		logger.info("Sending print");
		byte[] printData = generatePrintData(template,dc);
		if(printData == null) {
			logger.debug("printData is null");
		} else {
			logger.debug("printData: "+ new String(printData));
		}
		IPrinterDevice printerDevice = getPrinterDevice(device);
		printerDevice.print(printData, device, dc);

		logger.info("Print sent");
		DataContainer retDc = DataContainerUtil.copyErrors(dc);
		return retDc;
	}

	private byte[] generatePrintData(Template template, DataContainer dc) {
		
		IPrintDataAssembler dataAssembler = getDataAssembler(template);

		try{
			return dataAssembler.assembleData(dc);
		}catch(Exception ex) {
			DataContainerUtil.error(logger, dc, ex, "Could not create print date from template"); 
		}
		return null;
	}
	
	private IPrintDataAssembler getDataAssembler(Template template) {
		if(template == null) 
			return new ListPrintDataAssembler();
		else if(TemplateType.JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new JasperPrintDataAssembler(template);
		else if(TemplateType.COMPILED_JASPER.toString().equalsIgnoreCase(template.getTemplateTypeString()))
			return new CompiledJasperPrintDataAssembler(template);
		else 
			return new TemplatePrintDataAssembler(template);
	}
	
	private IPrinterDevice getPrinterDevice(Device device) {
		if (DestinationType.JASPER_MQ.toString().equalsIgnoreCase(device.getEifIpAddress())) {
			return new MqJasperPrintDevice(logger); 
		} else if (DestinationType.MQ.toString().equalsIgnoreCase(device.getEifIpAddress())) {
			return new MQPrintDevice(logger); 
		} else if(StringUtils.isEmpty(device.getEifIpAddress()) ||
				device.getEifIpAddress().equalsIgnoreCase(Device.PRINTER_PP) ||
				device.getEifIpAddress().equalsIgnoreCase(Device.PRINTER_BSX)) {
			return new PrintQueuePrinterDevice(logger);
		} else {
			return new SocketPrinterDevice(logger);
		}
	}
	
	private String getFormId() {
		String formId = destination.getRequestId();
		if(StringUtils.isEmpty(formId)) return null;
		return formId;
	}
	
	private String getTemplateName(String formId) {
		String templateID = mapAttribute(formId,dc.getString(DataContainerTag.PRODUCT_SPEC_CODE));
		if(StringUtils.isEmpty(templateID)) return formId;
		return templateID;
	}
	
	/**
	 * map the original queue name to a new queue name based on product spec code
	 * @param queueName
	 * @param productSpecCode
	 * @return
	 */
	private String mapQueueName(String queueName,String productSpecCode ) {
		return mapAttribute(queueName,productSpecCode);
	}
	
	private String mapAttribute(String attribute, String productSpecCode) {
		BuildAttribute buildAttribute = StringUtils.isEmpty(productSpecCode)? null : buildAttributeList.findById(productSpecCode,attribute);
		return buildAttribute == null ? attribute : buildAttribute.getAttributeValue();
	}
	
}
