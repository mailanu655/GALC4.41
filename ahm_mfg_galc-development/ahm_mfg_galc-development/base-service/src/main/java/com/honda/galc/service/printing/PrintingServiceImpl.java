package com.honda.galc.service.printing;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.JasperReportPageOrientation;
import com.honda.galc.data.JasperReportPageType;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.PrintingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.Base64Coder;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

public class PrintingServiceImpl implements PrintingService {

	private static final long serialVersionUID = 1L;
	public static final char STX = '\u0002';
	public static final char ETX = '\u0003';

	private static final String LOGGER_ID = "PrintingService";
	
	public void print(String queueName, String formId, DataContainer dataContainer) {
		dataContainer.put(DataContainerTag.QUEUE_NAME, queueName);
		dataContainer.put(DataContainerTag.FORM_ID, formId);

		PrintQueueSender.getInstance().enqueue(dataContainer);
	}

	private String getTemplateData(String templateName) {
		String templateData = null;
		Template template = ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
		if (template != null && template.getTemplateDataBytes() != null) {
			templateData = template.getTemplateDataString();
		}
		return templateData;
	}

	private boolean checkattribute(String attribute, String attributeValue) {
		boolean isExist = false;
		if (StringUtils.isEmpty(attributeValue))
			return isExist;
		if (attributeValue.length() >= attribute.length() + 4) {
			String str = attributeValue.substring(attribute.length(), attribute.length() + 4);
			if (".jpg".equalsIgnoreCase(str)) {
				isExist = true;
			}
		}
		return isExist;
	}

	private Map<String, Object> checkMap(DataContainer dc) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Iterator it = ((Map<String,String>)dc.get(DataContainerTag.KEY_VALUE_PAIR)).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				getLogger().info("DataContainer attributes List:" + pairs.getKey() + pairs.getValue());
				if (checkattribute(pairs.getKey().toString(), pairs.getValue().toString())) {
					String str = pairs.getValue().toString().substring(pairs.getKey().toString().length() + 4);
			
					byte[] bytearray = Base64Coder.decode(str);
					BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(imag, "jpg", baos);
					map.put(pairs.getKey().toString(), new ByteArrayInputStream(baos.toByteArray()));
				} else {
					map.put(pairs.getKey().toString(), pairs.getValue());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}
	
	public byte[] generateJasperReport(DataContainer dc) {
		try {
			Map<String, Object> map =new HashMap<String, Object>();
			map = checkMap(dc);
			String templateData = getTemplateData(dc.get(DataContainerTag.TEMPLATE_NAME).toString());
			if (templateData == null) {
				return null;
			}
			InputStream bis = new ByteArrayInputStream(templateData.getBytes());
			JasperReport jasperReport = JasperCompileManager.compileReport(bis);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
			
			boolean tumbleFlag = DataContainerUtil.getBoolean(dc,DataContainerTag.JASPER_REPORT_DUPLEX_TUMBLE, false);
			
			JasperReportPageType pageType = JasperPrintUtil.getPageType(dc);
			JasperReportPageOrientation orientation = JasperPrintUtil.getPageOrientation(dc);

			jasperPrint.setPageWidth(pageType.getPageWidth(orientation));
			jasperPrint.setPageHeight(pageType.getPageWidth(orientation));
			String psData = exportPostScriptData(jasperPrint, pageType, dc.get(DataContainerTag.TEMPLATE_NAME).toString());
			
			String trayValue =	map.containsKey(JasperExternalPrintAttributes.TRAY_VALUE.toString()) ? 
					(StringUtils.isEmpty(map.get(JasperExternalPrintAttributes.TRAY_VALUE.toString()).toString())? "0": 
						map.get(JasperExternalPrintAttributes.TRAY_VALUE.toString()).toString()) : "0";
			boolean jasperDuplexFlag =	map.containsKey(JasperExternalPrintAttributes.JASPER_DUPLEX_FLAG.toString()) ? 
					(StringUtils.isEmpty(map.get(JasperExternalPrintAttributes.JASPER_DUPLEX_FLAG.toString()).toString()) ? false:
						Boolean.valueOf(map.get(JasperExternalPrintAttributes.JASPER_DUPLEX_FLAG.toString()).toString())) : false;
			
			String baos = modifyPostScriptFile(psData, trayValue, jasperDuplexFlag, tumbleFlag, pageType, orientation);
			return baos.getBytes();
		} catch (JRException e) {
			getLogger().error("Exception occured at generatingJasperReport:"+ e.getMessage());
			e.printStackTrace();
			throw new SystemException("Could not generate Jasper report", e);
		}
	}

	// set pagesize and orientation
	private PrintRequestAttributeSet setPageSizeAndOrientation(JasperPrint jasperPrint, JasperReportPageType pageType, String templateName) {
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

		printRequestAttributeSet.add(pageType.getMediaSizeName());
		return printRequestAttributeSet;
	}

	private String exportPostScriptData(JasperPrint jasperPrint, JasperReportPageType pageType, String templateName) throws JRException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JRExporter exporter = new JRPrintServiceExporter();

		String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
		StreamPrintServiceFactory[] spsFactories = PrinterJob.lookupStreamPrintServices(psMimeType);
		StreamPrintService psPrinter = spsFactories[0].getPrintService(bos);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psPrinter);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, psPrinter.getAttributes());

		PrintRequestAttributeSet printRequestAttributeSet = setPageSizeAndOrientation(jasperPrint, pageType, templateName);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);

		try {
			exporter.exportReport();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bos.toString();
	}

	private String modifyPostScriptFile(String printData, String trayValue, boolean duplexFlag,
			boolean tumbleFlag, JasperReportPageType pageType, JasperReportPageOrientation orientation) {

		BufferedReader br = new BufferedReader(new StringReader(printData));
		StringBuilder pr = new StringBuilder();
		String line = null;
		
		try {
			while ((line = br.readLine()) != null) {
				if(line.contains("%%BeginSetup")){
					pr.append(line+"\n");
					String a = "<< /Duplex "
									+ duplexFlag
									+ " /Tumble "
									+ tumbleFlag
									+ " /NumCopies 1 /MediaPosition "
									+ trayValue 
									+ " /PageSize ["
									+ pageType.getPageWidth() + " "
									+ pageType.getPageHeight() + "]"
									+ " /Orientation " + orientation.getPostScriptValue()
									+ " >> setpagedevice";
					pr.append(a+"\n");
					a = "%%EndSetup";
					pr.append(a+"\n");
				}
				else if (line.contains("%%Page:")
						|| line.contains("<< /PageSize")
						|| line.contains("/pgSave")
						|| line.contains("pgSave restore")
						||line.contains("setpagedevice")
						||line.contains("%%EndSetup")) {
					// Do nothing
				} else
					pr.append(line);
				pr.append("\n");
			}

		} catch (Exception ex) {
			throw new SystemException("could not modify post script data", ex);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Logger.getLogger().error("Unable to close BufferedReader.");
			}
		}
		
		Logger.getLogger().debug("PostScript File:" + new String(pr));
		return new String(pr);
	}

	private Logger getLogger() {
		return Logger.getLogger(LOGGER_ID);
	}
}
