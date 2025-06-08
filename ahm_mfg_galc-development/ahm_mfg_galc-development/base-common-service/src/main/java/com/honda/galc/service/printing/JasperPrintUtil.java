package com.honda.galc.service.printing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;
import javax.swing.table.TableModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.CharSets;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.JasperReportPageOrientation;
import com.honda.galc.data.JasperReportPageType;
import com.honda.galc.entity.enumtype.TemplateType;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2016
 */
public class JasperPrintUtil {

	public static final String JPG = "jpg";
	public static final String IMAGE = "IMAGE";
	public static final String DATA_SOURCE = "DATA_SOURCE";
	public static final String DEFAULT_TRAY_VALUE = "0";
	
	private static ConcurrentHashMap<String, Template> templateMap = new ConcurrentHashMap<String, Template>();
	
	private String loggerName = "";
	private String messageId = "";
	
	public JasperPrintUtil(String loggerName, String messageId) {
		this.loggerName = loggerName;
		this.messageId = messageId;
	}
	
	public static boolean isImage(String attribute, String attributeValue){
		if(StringUtils.endsWithIgnoreCase(attribute, IMAGE))
			return true;
		else
			return false;
	}

	public Map<String, Object> fillImages(DataContainer dc, boolean reloadIfUpdated) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Iterator<Entry<Object, Object>> it = dc.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Object, Object> pair = it.next();
				String attrib = pair.getKey().toString();
				String attribVal = null;
				if(pair.getValue() != null) attribVal = pair.getValue().toString();
				else {
					getLogger().error("Ignoring print Attribute Value is null. Attribute  " + attrib);
				}
				if (isImage(attrib, attribVal)) {
					ByteArrayOutputStream baos = retrieveImage(attribVal, reloadIfUpdated);
					map.put(attrib,	new ByteArrayInputStream(baos.toByteArray()));
				} else {
					if (attribVal != null)map.put(attrib, pair.getValue());
				}
			}
		} catch (Exception ex) {
			logError(ex, "Exception occurred while checking DataContainer");
		}
		return map;
	}

	public ByteArrayOutputStream retrieveImage(String attribVal, boolean reloadIfUpdated) throws IOException {	
		Template template = getTemplate(attribVal, reloadIfUpdated);
		byte[] byteArray = template.getTemplateDataBytes();
		BufferedImage imag = ImageIO.read(new ByteArrayInputStream(byteArray));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(imag, JPG, baos);
		return baos;
	}

	public JasperReport compileTemplate(Template template) {
		try {
			InputStream bis = new ByteArrayInputStream(template.getTemplateDataBytes());
			return JasperCompileManager.compileReport(bis);
		}
		catch (Exception ex) {
			logError(ex, "Failed to compile Jasper report");
			throw new SystemException(getMessageId() + ": Failed to compile Jasper report", ex);
		}
	}
	
	public static TemplateDao getTemplateDao() {
		return ServiceFactory.getDao(TemplateDao.class);
	}
	
	public JasperPrint fillReport(DataContainer dc, JasperReport jasperReport, boolean reloadIfUpdated) {
		getLogger().info(getMessageId() + " Attempting to fill report");
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = createJasperPrint(dc, jasperReport, reloadIfUpdated);
		} catch (ConcurrentModificationException cmex) {
			cmex.printStackTrace();
			logError(cmex, "Fill report failed. Retrying..");
			try {
				jasperPrint = createJasperPrint(dc, jasperReport, reloadIfUpdated);
			} catch (JRException e) {
				logFillReportException(e);
			}
		} catch (Exception ex) {
			logFillReportException(ex);
		}
		return jasperPrint;
	}

	private void logFillReportException(Exception ex) {
		ex.printStackTrace();
		logError(ex, "Failed to fill Jasper report");
	}

	private JasperPrint createJasperPrint(DataContainer dc, JasperReport jasperReport, boolean reloadIfUpdated) throws JRException {
		JRDataSource jrDataSource = createJrDataSource(dc.get(DATA_SOURCE));
		long startTime = System.currentTimeMillis();
		Map<String, Object> imageMap = fillImages(dc, reloadIfUpdated);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, imageMap, jrDataSource);
		getLogger().info((System.currentTimeMillis() - startTime), getMessageId() + " Fill report completed");
		return jasperPrint;
	}
	
	public Template getTemplate(String templateName, boolean reloadIfUpdated) {
		Template template = getTemplateMap().get(templateName);
		if (template == null) {
			template = addToTemplateCache(templateName);
			return template;
		}
		if (reloadIfUpdated) {
			Date cachedUpdateTimestamp = template.getUpdateTimestamp();
			Date latestUpdateTimestamp = getTemplateDao().findUpdateTimestamp(templateName);
			if (latestUpdateTimestamp == null) {
				return template;
			}
			if (cachedUpdateTimestamp == null || cachedUpdateTimestamp.before(latestUpdateTimestamp)) {
				template = addToTemplateCache(templateName);
			}
		}
		return template;
	}
	
	public Template addToTemplateCache(String templateName) {
		if (StringUtils.isEmpty(templateName)) {
			throw new SystemException(getMessageId() + ": Unable to add empty template name to cache");
		}
		Template template = getTemplateDao().findByKey(templateName);
		if (template == null) {
			throw new SystemException(getMessageId() + ": Failed to add non-existent template " + templateName + " to cache");
		}
		getTemplateMap().put(templateName, template);
		getLogger().info(getMessageId() + " Adding template " + templateName + " to cache");
		return template;
	}

	public JasperReport getJasperReport(Template template) {
		JasperReport jasperReport = null;
		if (!template.getTemplateTypeString().equals(TemplateType.COMPILED_JASPER.toString())) {
			jasperReport = compileTemplate(template);
		} else {
			long startTime = System.currentTimeMillis();
			try {
				jasperReport = (JasperReport) JRLoader.loadObject(new ByteArrayInputStream(template.getTemplateDataBytes()));				
			} catch (JRException ex) {
				logError(ex, "Unable to create JasperReport from template");
			}
			getLogger().info((System.currentTimeMillis() - startTime), getMessageId() + " Fetch report completed");
		}
		return jasperReport;
	}
	
	public void print(JasperPrint jasperPrint, DataContainer dc)  {
		long startTime = System.currentTimeMillis();
		try {
			IJasperExporter exporter = getExporter(dc);
			File fileToPrint = exporter.exportToFile(jasperPrint, dc);
						
			String command = exporter.getPrintCommand(dc, fileToPrint);
			Runtime.getRuntime().exec(command);
			getLogger().info((System.currentTimeMillis() - startTime), getMessageId() + " Generated print file and print command issued: " + command);
		} catch (Exception ex) {
			logError(ex, "Exception encountered while attempting to print");
		}
	}

	public IJasperExporter getExporter(DataContainer dc) {
		
		if (dc.containsKey(DataContainerTag.PRINT_FORMAT)) {
			if (dc.get(DataContainerTag.PRINT_FORMAT).equals(DataContainerTag.PRINT_PDF)) {
				return new JasperPdfExporter(loggerName, getMessageId());
			}
		}
		return new JasperPostScriptExporter(loggerName, getMessageId());
	}
	
	public static boolean isDuplex(DataContainer dc) {
		return DataContainerUtil.getBoolean(dc, JasperExternalPrintAttributes.JASPER_DUPLEX_FLAG.toString(), false);
	}
	
	public static boolean isTumble(DataContainer dc) {
		return DataContainerUtil.getBoolean(dc, DataContainerTag.JASPER_REPORT_DUPLEX_TUMBLE, false);
	}
	
	public static int getTrayValue(DataContainer dc) {
		String trayValue = DataContainerUtil.getString(dc, JasperExternalPrintAttributes.TRAY_VALUE.toString(), DEFAULT_TRAY_VALUE);
		if (!StringUtils.isNumeric(trayValue)){
			return Integer.parseInt(DEFAULT_TRAY_VALUE);
		} else {
			return Integer.parseInt(trayValue);
		}
	}

	public static JasperReportPageType getPageType(DataContainer dc) {
		try {
			String pageType = DataContainerUtil.getString(
								dc, 
								DataContainerTag.JASPER_REPORT_PAGE_TYPE.toString(), 
								JasperReportPageType.NA_LETTER.toString());
			
			return Enum.valueOf(JasperReportPageType.class, pageType);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return JasperReportPageType.NA_LETTER;
	}
	
	public static JasperReportPageOrientation getPageOrientation(DataContainer dc) {
		try {
			String pageOrientation = DataContainerUtil.getString(
										dc, 
										DataContainerTag.JASPER_REPORT_PAGE_ORIENTATION.toString(), 
										JasperReportPageOrientation.PORTRAIT.toString());
			
			return Enum.valueOf(JasperReportPageOrientation.class, pageOrientation);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return JasperReportPageOrientation.PORTRAIT;
	}
	
	public static String getPrinterName(DataContainer dc) {
		return dc.getString(DataContainerTag.QUEUE_NAME).toString();
	}

	public JRDataSource createJrDataSource(Object dataSource) {
		if (dataSource == null) {
			return new JREmptyDataSource();
		}
		if (dataSource instanceof TableModel) {
			return new JRTableModelDataSource((TableModel) dataSource);
		}
		if (dataSource instanceof Collection) {
			return new JRBeanCollectionDataSource((Collection<?>) dataSource);
		}
		return new JREmptyDataSource();
	}

	public void logError(Exception ex, String logMessage) {
		ex.printStackTrace();
		getLogger().error(ex, getMessageId() + " " + logMessage + " - " + StringUtils.trimToEmpty(ex.getMessage()));
	}
	
	public String getLoggerName() {
		return loggerName;
	}
	
	public String getMessageId() {
		return messageId;
	}
	
	public Logger getLogger(){
		return Logger.getLogger(loggerName);
	}

	public static ConcurrentHashMap<String, Template> getTemplateMap() {
		return templateMap;
	}
	
	public static byte[] compileJrxml(byte[] jrxmlData) throws JRException {
		if (jrxmlData == null || jrxmlData.length == 0) {
			return null;
		}
		InputStream bis = new ByteArrayInputStream(jrxmlData);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JasperCompileManager.compileReportToStream(bis, bos);
		byte[] jasperData = bos.toByteArray();
		try {
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jasperData;
	}
	
	public static byte[] decompileJasper(byte[] jasperData) throws JRException {
		return decompileJasper(jasperData,  null);
	}
	
	public static byte[] decompileJasper(byte[] jasperData, String encoding) throws JRException {
		if (StringUtils.isBlank(encoding)) {
			encoding = CharSets.UTF_8;
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new ByteArrayInputStream(jasperData));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JRXmlWriter.writeReport(jasperReport, bos, encoding);
		byte[] jrxmlData = bos.toByteArray();
		return jrxmlData;
	}
}
