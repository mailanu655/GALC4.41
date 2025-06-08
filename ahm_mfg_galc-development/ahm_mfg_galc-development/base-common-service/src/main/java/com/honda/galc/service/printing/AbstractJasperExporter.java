package com.honda.galc.service.printing;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Subu Kathiresan
 * @date Oct 17, 2016
 */
public abstract class AbstractJasperExporter implements IJasperExporter {

	private String loggerName = "";
	protected String messageId = "";
	
	public AbstractJasperExporter(String loggerName, String messageId) {
		this.loggerName = loggerName;
		this.messageId = messageId;
	}
	
	abstract public File exportToFile(JasperPrint jasperPrint, DataContainer dc) throws IOException;
	
	abstract public byte[] export(JasperPrint jasperPrint, DataContainer dc);
	
	abstract public String getPrintCommand(DataContainer dc, File file);
	
	public void logError(Exception ex, String logMessage) {
		ex.printStackTrace();
		getLogger().error(ex, messageId + " " + logMessage + " - " + StringUtils.trimToEmpty(ex.getMessage()));
	}
	
	public String getTempFileName(DataContainer dc) {
		String fileName = "";
		if (dc.containsKey(DataContainerTag.PRODUCT_ID)) {
			fileName += dc.get(DataContainerTag.PRODUCT_ID).toString();
		}
		if (dc.containsKey(DataContainerTag.QUEUE_NAME)) {
			fileName += "_" + dc.get(DataContainerTag.QUEUE_NAME).toString();
		}
		if (fileName.equals("")) {
			fileName += getTimestamp();
		} else {
			fileName += "_" + getTimestamp();
		}
		getLogger().info(messageId + " " + fileName);
		return fileName;
	}
	
	public String getTimestamp() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmss_");
			return dateFormat.format(new Date());
		} catch (Exception ex) {
			logError(ex, "Problem creating timestamp");
		}
		return "";
	}
	
	public Logger getLogger(){
		return Logger.getLogger(loggerName);
	}
}
