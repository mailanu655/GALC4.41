package com.honda.galc.service.printing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.DataContainer;
import com.honda.galc.property.PrinterQueueSenderPropertyBean;
import com.honda.galc.service.property.PropertyService;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * @author Subu Kathiresan
 * @date Oct 10, 2016
 */
public class JasperPdfExporter extends AbstractJasperExporter {
	
	public JasperPdfExporter(String loggerName, String messageId) {
		super(loggerName, messageId);
	}
	
	public File exportToFile(JasperPrint jasperPrint, DataContainer dc) throws IOException {
		
		File pdfFile = null;
		try {
			long startTime = System.currentTimeMillis();

			pdfFile = File.createTempFile(getTempFileName(dc), ".pdf");
			getLogger().info(messageId + " " + pdfFile.getAbsolutePath() + " created");
		
			JRPdfExporter pdfExporter = getExporter(jasperPrint, dc, pdfFile);
			pdfExporter.exportReport();
			getLogger().info((System.currentTimeMillis() - startTime), messageId + " Exporting to pdf file completed");
		} catch (Exception ex) {
			logError(ex, "Exception encountered while exporting to pdf file");
		}
		return pdfFile;
	}
	
	public byte[] export(JasperPrint jasperPrint, DataContainer dc) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			long startTime = System.currentTimeMillis();
			JRPdfExporter pdfExporter = getExporter(jasperPrint, dc, bos);
			pdfExporter.exportReport();
			getLogger().info((System.currentTimeMillis() - startTime), messageId + " Exporting to pdf completed");
			return bos.toByteArray();
		} catch (Exception ex) {
			logError(ex, "Exception encountered while exporting to pdf file");
			return null;
		}
	}
	
	public JRPdfExporter getExporter(JasperPrint jasperPrint, DataContainer dc, File file) {
		JRPdfExporter pdfExporter = new JRPdfExporter();
		pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
		return pdfExporter;
	}
	
	public JRPdfExporter getExporter(JasperPrint jasperPrint, DataContainer dc, ByteArrayOutputStream bos) {
		JRPdfExporter pdfExporter = new JRPdfExporter();
		pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
		return pdfExporter;
	}

	public String getPrintCommand(DataContainer dc, File fileToPrint) {
		String cmd = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class).getJasperPrintCommand();
		if(StringUtils.isEmpty(cmd)) { 
			cmd = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class).getPrintCommand();
		}

		cmd += " " + JasperPrintUtil.getPrinterName(dc); 	// qprt -r -P<printerName> 
		if (JasperPrintUtil.isDuplex(dc)) {
			cmd += " " + "-Y1";								// add duplex flag
		}
		if (JasperPrintUtil.isTumble(dc)) {
			cmd += " " + "-Y2";								// add tumble flag
		}
		int trayValue = JasperPrintUtil.getTrayValue(dc);
		if (trayValue > 0) {
			cmd += " " + "-u" + trayValue;					// add tray value
		}
		cmd += " " + fileToPrint;							// qprt -r -P<printerName> <fileName>
		return cmd;
	}
}
