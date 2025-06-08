package com.honda.galc.service.printing;

import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.print.DocFlavor;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.JasperReportPageOrientation;
import com.honda.galc.data.JasperReportPageType;
import com.honda.galc.property.PrinterQueueSenderPropertyBean;
import com.honda.galc.service.property.PropertyService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

/**
 * @author Subu Kathiresan
 * @date Oct 10, 2016
 */
public class JasperPostScriptExporter extends AbstractJasperExporter {

	private JasperReportPageType pageType = JasperReportPageType.NA_LETTER;
	
	public JasperPostScriptExporter(String loggerName, String messageId) {
		super(loggerName, messageId);
	}
	
	public File exportToFile(JasperPrint jasperPrint, DataContainer dc) throws IOException {
		File psFile = null;
		try {
			long startTime = System.currentTimeMillis();
			psFile = File.createTempFile(getTempFileName(dc), ".ps");
			getLogger().info(messageId + " " + psFile.getAbsolutePath() + " created");
			
			String psData = getPostScriptData(jasperPrint, dc);
			psFile = writeToFile(psFile, psData);
			getLogger().info((System.currentTimeMillis() - startTime), messageId + " Exporting to postscript file completed");
		} catch (Exception ex) {
			logError(ex, "Exception encountered while exporting to postscript file");
		}
		return psFile;
	}
	
	public byte[] export(JasperPrint jasperPrint, DataContainer dc) {
		String psData = "";
		try {
			long startTime = System.currentTimeMillis();
			psData = getPostScriptData(jasperPrint, dc);
			getLogger().info((System.currentTimeMillis() - startTime), messageId + " Exporting to postscript completed");
		} catch (Exception ex) {
			logError(ex, "Exception encountered while exporting to postscript");
		}
		return psData.getBytes();
	}
	
	public String getPostScriptData(JasperPrint jasperPrint, DataContainer dc) throws JRException {
		
		Long startTime = System.currentTimeMillis();
		getLogger().info(messageId + "Entering getPostScriptData()");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JRExporter exporter = getExporter(jasperPrint, dc, bos);
		getLogger().info(messageId + "Attempting to invoke exporter.exportReport()");
		exporter.exportReport();
		getLogger().info(messageId + "Completed exporter.exportReport()");
		String psData =  bos.toString();
		getLogger().info((System.currentTimeMillis() - startTime), messageId + " Postscript generation completed");
		
		// replace post script headers
		startTime = System.currentTimeMillis();
		psData = modifyPostScriptHeader(psData, dc);
		getLogger().info((System.currentTimeMillis() - startTime), messageId + " Replace postscript headers completed");
		return psData;
	}

	private JRExporter getExporter(JasperPrint jasperPrint, DataContainer dc, ByteArrayOutputStream bos) {
		getLogger().info(messageId + "Entering getExporter()");
		JRExporter exporter = new JRPrintServiceExporter();
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		
		setPageType(JasperPrintUtil.getPageType(dc));
		pras.add(getPageType().getMediaSizeName());
		JasperReportPageOrientation orientation = JasperPrintUtil.getPageOrientation(dc);
		jasperPrint.setPageWidth(getPageType().getPageWidth(orientation));
		jasperPrint.setPageHeight(getPageType().getPageHeight(orientation));
		
		String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
		getLogger().info(messageId + "Looking up print services for " + psMimeType);
		StreamPrintServiceFactory[] spsFactories = PrinterJob.lookupStreamPrintServices(psMimeType);
		StreamPrintService psPrinter = spsFactories[0].getPrintService(bos);
		getLogger().info(messageId + "Found print service " + psPrinter.getName());
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psPrinter);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, pras);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, psPrinter.getAttributes());
		getLogger().info(messageId + "Exiting getExporter()");
		return exporter;
	}
	
	public String modifyPostScriptHeader(String printData, DataContainer dc) {

		int startIndex = printData.indexOf("%%BeginSetup");
		int endIndex = printData.indexOf("%%EndSetup");
		String toBeReplaced = printData.substring(startIndex + 1, endIndex);
		printData = printData.replace(toBeReplaced, getReplacementString(dc));

		getLogger().debug("PostScript data:" + printData);
		return printData;
	}

	public StringBuilder getReplacementString(DataContainer dc) {
		JasperReportPageOrientation orientation = JasperPrintUtil.getPageOrientation(dc);
		StringBuilder replacement = new StringBuilder();
		replacement.append("%%BeginSetup" + "\n");
		replacement.append("<< /Duplex " + JasperPrintUtil.isDuplex(dc));
		replacement.append(" /Tumble " + JasperPrintUtil.isTumble(dc));
		replacement.append(" /NumCopies 1 ");
		replacement.append(" /MediaPosition " + JasperPrintUtil.getTrayValue(dc));
		replacement.append(" /PageSize [" + getPageType().getPageWidth() + " " 
										  + getPageType().getPageHeight() + "]");
		replacement.append(" /Orientation " + orientation.getPostScriptValue());
		replacement.append(" >> setpagedevice");
		replacement.append("\n");
		replacement.append("%%EndSetup");
		return replacement;
	}
	
	public File writeToFile(File psFile, String psData) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(psFile);
			fw.write(psData);
			return psFile;
		} catch (Exception ex) {
			logError(ex, "Error encountered while writing to postscript file");
			return null;
		} finally {
			try {
				fw.close();
			} catch(Exception ex) {}
		}
	}
	
	public JasperReportPageType getPageType() {
		return pageType;
	}

	public void setPageType(JasperReportPageType pageType) {
		this.pageType = pageType;
	}

	public String getPrintCommand(DataContainer dc, File fileToPrint) {
		
		String cmd = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class).getJasperPrintCommand();
		if(StringUtils.isEmpty(cmd)) { 
			cmd = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class).getPrintCommand();
		}
		
		cmd += " " + JasperPrintUtil.getPrinterName(dc); 	// qprt -r -P<printerName> 
		cmd += " " + fileToPrint;							// qprt -r -P<printerName> <fileName>
		return cmd;
	}
}
