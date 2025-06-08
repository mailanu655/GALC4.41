package com.honda.galc.service.printing;

import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.print.DocFlavor;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.service.JasperReportService;

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

/**
 * 
 * <h3>JasperReportServiceImpl Class description</h3>
 * <p> JasperReportServiceImpl description </p>
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
 * Aug 27, 2013
 *
 *
 */
public class JasperReportServiceImpl implements JasperReportService{

	public byte[] generateJasperReport(byte[] templateData, DataContainer dc) {
		try {
			InputStream bis = new ByteArrayInputStream(templateData);
			JasperReport jasperReport =  JasperCompileManager.compileReport(bis);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,DataContainerUtil.getAttributeMap(dc),
					new JREmptyDataSource());
			exportPostScriptData(jasperPrint);
			String psData = exportPostScriptData(jasperPrint);
			String printData = modifyPostScriptFile(psData, dc);
			return printData.getBytes();
		} catch (JRException e) {
			throw new SystemException("Could not generate Jasper report",e);
		}
	}
	
	private String exportPostScriptData(JasperPrint jasperPrint) throws JRException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JRExporter exporter = new JRPrintServiceExporter();
		String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
		StreamPrintServiceFactory[] spsFactories = PrinterJob
				.lookupStreamPrintServices(psMimeType);
		StreamPrintService psPrinter = spsFactories[0].getPrintService(bos);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE,
				psPrinter);
		exporter.exportReport();
		return bos.toString();
	}
	
	private String modifyPostScriptFile(String printData,DataContainer dc) {
			
	    if(!dc.containsKey(JasperExternalPrintAttributes.TRAY_VALUE.toString())) return printData;
	    
		BufferedReader br= new BufferedReader(new StringReader(printData));
	    StringBuilder pr = new StringBuilder(); 
	   
	    String line = null;
		
		try{
		    while ((line = br.readLine()) != null) {
				if (line.contains("<< /NumCopies 1 >> setpagedevice")) {
					String a = line.replace(
							"<< /NumCopies 1 >> setpagedevice",
							"<< /Duplex " + DataContainerUtil.getBoolean(dc, "DUPLEX", false)
									+ " /Tumble "
									+ JasperPrintUtil.isTumble(dc)
									+ "/NumCopies 1 /MediaPosition "
									+ DataContainerUtil.getString(dc, JasperExternalPrintAttributes.TRAY_VALUE.toString(), "0")
									+ ">> setpagedevice");
					pr.append(a + "\n");
					if (line.contains("%%Page:")
							|| line.contains("<< /PageSize")
							|| line.contains("/pgSave")
							|| line.contains("pgSave restore")) {
		
					} else
						pr.append(line);
					pr.append("\n");
				}
			}
		}catch(Exception ex){
			throw new SystemException("could not modify post script data",ex);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Logger.getLogger().error("Unable to close BufferedReader.");
			}
		}

	    return pr.toString();
	}
	
}
