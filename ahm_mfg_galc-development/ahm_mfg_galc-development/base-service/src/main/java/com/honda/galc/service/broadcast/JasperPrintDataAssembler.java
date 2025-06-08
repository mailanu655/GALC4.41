package com.honda.galc.service.broadcast;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.JasperReportPageOrientation;
import com.honda.galc.data.JasperReportPageType;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.printing.JasperExternalPrintAttributes;
import com.honda.galc.service.printing.JasperPrintUtil;
import com.honda.galc.util.Base64Coder;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

/**
 * 
 * <h3>JasperPrintDataAssembler Class description</h3>
 * <p>
 * JasperPrintDataAssembler description
 * </p>
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
 *         Aug 26, 2013
 * 
 * 
 */
public class JasperPrintDataAssembler extends TemplatePrintDataAssembler {

	public static final String DATA_SOURCE = "DATA_SOURCE";
		
	public JasperPrintDataAssembler(Template template) {
		super(template);
	}

	public byte[] assembleData(DataContainer dc) {
		byte[] templateData = super.assembleData(dc);
		return generateJasperReport(templateData, dc);
	}
	
	protected JasperPrint fillReport(InputStream bis, JRDataSource jrDataSource, Map<String,Object> map) {

		try {
			// compile Jasper template, since its non-compiled
			JasperReport jasperReport = JasperCompileManager.compileReport(bis);
			return JasperFillManager.fillReport(jasperReport, map, jrDataSource);
		}
		catch (Exception e) {
			throw new SystemException("Failed to fill Jasper report", e);
		}
	}

	protected byte[] generateJasperReport(byte[] templateData, DataContainer dc) {
		InputStream bis = new ByteArrayInputStream(templateData);

		JRDataSource jrDataSource = createJrDataSource(dc.get(DATA_SOURCE));
		JasperPrint jasperPrint = fillReport(bis, jrDataSource, checkMap(dc));
		JasperReportPageOrientation orientation = JasperPrintUtil.getPageOrientation(dc);
		JasperReportPageType pageType = JasperPrintUtil.getPageType(dc);
		jasperPrint.setPageWidth(pageType.getPageWidth(orientation));
		jasperPrint.setPageHeight(pageType.getPageHeight(orientation));

		String psData;
		try {
			psData = exportPostScriptData(jasperPrint, dc);
			String printData = modifyPostScriptFile(psData, dc);
			return printData.getBytes();
		} 
		catch (JRException e) {
			throw new SystemException("Failed to print Jasper report", e);
		}
	}

	private boolean checkattribute(String attribute, String attributeValue){
		boolean isExist =false;
		if(StringUtils.isEmpty(attributeValue))
			return isExist;
		if(attributeValue.length()>= attribute.length()+4){
			String str = attributeValue.substring(attribute.length(),attribute.length()+4);
			if(".jpg".equalsIgnoreCase(str)){
				isExist = true;
			}
		}
		return isExist;
	}
	
	private Map<String, Object> checkMap(DataContainer dc) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Iterator it = dc.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				if (checkattribute(pairs.getKey().toString(),pairs.getValue().toString())) {
					int length = pairs.getKey().toString().length() + 4;
					Logger.getLogger().info("DataContainer attributes List:"+ pairs.getKey() + " " + pairs.getValue().toString().substring(0, length));
					String str = pairs.getValue().toString().substring(length);

					byte[] bytearray = Base64Coder.decode(str);
					BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(imag, "jpg", baos);
					map.put(pairs.getKey().toString(), new ByteArrayInputStream(baos.toByteArray()));
				} else {
					Logger.getLogger().info("DataContainer attributes List:"+ pairs.getKey() + pairs.getValue());
					map.put(pairs.getKey().toString(), pairs.getValue());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;

	}
	
	//set pagesize and orientation
	private PrintRequestAttributeSet setPageSizeAndOrientation(JasperPrint jasperPrint, DataContainer dc) {
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		JasperReportPageType pageType = JasperPrintUtil.getPageType(dc);
		printRequestAttributeSet.add(pageType.getMediaSizeName());
		return printRequestAttributeSet;
	}

	private String exportPostScriptData(JasperPrint jasperPrint, DataContainer dc) throws JRException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JRExporter exporter = new JRPrintServiceExporter();

		String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
		StreamPrintServiceFactory[] spsFactories = PrinterJob
				.lookupStreamPrintServices(psMimeType);
		StreamPrintService psPrinter = spsFactories[0].getPrintService(bos);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE,
				psPrinter);
		exporter.setParameter(
				JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
				psPrinter.getAttributes());

		PrintRequestAttributeSet printRequestAttributeSet = setPageSizeAndOrientation(
				jasperPrint, dc);
		exporter.setParameter(
				JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
				printRequestAttributeSet);

		exporter.exportReport();
		return bos.toString();
	}

	private String modifyPostScriptFile(String printData, DataContainer dc) {
		
		JasperReportPageType pageType = JasperPrintUtil.getPageType(dc);
		JasperReportPageOrientation orientation = JasperPrintUtil.getPageOrientation(dc);
		BufferedReader br = new BufferedReader(new StringReader(printData));
		StringBuilder pr = new StringBuilder();

		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				if (line.contains("%%BeginSetup")) {
					pr.append(line+"\n");
					String a = "<< /Duplex "
									+ DataContainerUtil.getBoolean(dc, JasperExternalPrintAttributes.JASPER_DUPLEX_FLAG.toString(), false)
								    + " /Tumble "
									+ JasperPrintUtil.isTumble(dc)
									+ " /NumCopies 1 /MediaPosition "
									+ DataContainerUtil.getString(dc, JasperExternalPrintAttributes.TRAY_VALUE.toString(), "0") 
									+ " /PageSize ["
									+ pageType.getPageWidth() + " " 
									+ pageType.getPageHeight() + "]"
									+ " /Orientation " + orientation.getPostScriptValue()
									+ " >> setpagedevice";
					pr.append(a + "\n");
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
				}
				else
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

	protected JRDataSource createJrDataSource(Object dataSource) {
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
}
