package com.honda.galc.service.printing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PrintingException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.property.PrinterQueueSenderPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.QueueProcessor;
import com.honda.galc.util.SimpleVarSubstitutor;

/**
 * 
 * <h3>PrintQueueSender Class description</h3>
 * <p> PrintQueueSender description </p>
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
 * Oct 26, 2010
 *
 *
 */
public class PrintQueueSender extends QueueProcessor<DataContainer> {
    
	
	private static PrintQueueSender instance = null;
	
	public static String PRINTER_PP = "PP";
	public static String PRINTER_BSX = "BSX";
	
	private Logger logger;
	// only use singleton
	private PrintQueueSender() {
		logger = Logger.getLogger("PrintService");
	}
	
	public static PrintQueueSender getInstance() {
		
		if(instance == null){ 
			instance = new PrintQueueSender();
		    instance.start();
		}
		return instance;
	}
	
	@Override
	public void processItem(DataContainer item) {
		
		PrinterQueueSenderPropertyBean propertyBean = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class);
		
		try{
			String outputFileName = makeOutputFileName(item);
			logger.debug(outputFileName);
			
	//		 Open file
			OutputStream output = this.openOutputFile(outputFileName);
			String printerType = item.getString(DataContainerTag.PRINTER_TYPE);
			
			if(StringUtils.isEmpty(printerType))
				
				throw new PrintingException("Printer Type is not configured");
			
			IPrinterDataAssembler printerAssembler = createPrinterSender(printerType);
			
			printerAssembler.writeOutputFile(output, item);	
			
			execPrintCmd(item.getString(DataContainerTag.QUEUE_NAME), outputFileName);
			
			if(propertyBean.isCleanUp()){
				cleanupFile(outputFileName);
				logger.info("output file " + outputFileName + " is deleted");
			}
		}catch(Exception ex) {
			
			if(ex.getCause() == null)
				logger.error(ex.getMessage());
			else logger.error(ex,"Failed to print Form " + item.getString(DataContainerTag.FORM_ID));
			
		}
	}

	/**
	 * Makes up output file name
	 * @param dataContainer input DataContainer
	 * 
	 * @return output file name
	 * @throws BroadcastException
	 */
	private String makeOutputFileName(DataContainer dc) {
		
		StringBuilder sb = new StringBuilder();
		
		
		try {
			
			PrinterQueueSenderPropertyBean propertyBean = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class);
			sb.append(propertyBean.getTempDir());
			sb.append(dc.get(DataContainerTag.QUEUE_NAME)).append("-");
			sb.append(dc.get(DataContainerTag.FORM_ID)).append("-");
			Thread currentThread = Thread.currentThread();
			
			sb.append(currentThread.getId()).append("-");
			
			sb.append(new Date().getTime()).append(".out");
			
		} catch (Exception e) {			
			throw new PrintingException("Failed to make a out file name due to ", e);
		}
		
		return sb.toString();
	}
	
	/**
	 * Opens output with a given file name
	 * 
	 * @param outputFileName - output file name
	 * @return open writer
	 * @throws BroadcastException
	 */
	protected OutputStream openOutputFile(String outputFileName) {
		// Open output file
		OutputStream out;
		try {
			out = new FileOutputStream(outputFileName);
		} catch (IOException e) {
			throw new PrintingException("Could not open output", e);
		}
		return out;
	}

	private IPrinterDataAssembler createPrinterSender(String printerType) {
		if(printerType.equalsIgnoreCase(PRINTER_PP))
			return new ListPrinterDataAssembler();
		else if(printerType.equalsIgnoreCase(PRINTER_BSX))
			return new TemplatePrinterDataAssembler(false);
		else return null;
	}
	
	/**
	 * Executes print command
	 * 
	 * @param outputFileName - output file name
	 * @throws BroadcastException
	 */
	private void execPrintCmd(String queueName,String outputFileName) {

		try {
		
			PrinterQueueSenderPropertyBean propertyBean = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class);
			
			// Print Command
			String execCommand = propertyBean.getPrintCommand();
			
			SimpleVarSubstitutor subst = new SimpleVarSubstitutor();
			
			// Print Queue
			subst.add("queue", queueName);
			
			// File name
			subst.add("file", outputFileName);		
		
			// Obtain command
			execCommand = subst.substitute(execCommand);
			
			// Execute command
			logger.info("Executing Printer command: " + execCommand);
			Process process = Runtime.getRuntime().exec(execCommand);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			// stdout
			String line;
			while((line = br.readLine()) != null) {
				logger.info("Print command stdout: " + line);;
			}
			br.close();
			
			// stderr
			br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			while((line = br.readLine()) != null) {
				logger.error("Print Command stderr: " + line);
			}
			br.close();
			
			int status;
			for (;;) {
				try {
					status = process.waitFor();
					break;
				} catch (InterruptedException e) {
					// do nothing
				}
			}	
			logger.info("Print command status: " + status);
			
		} catch (Exception e) {
			throw new PrintingException("Failed to execute printer command", e);
		}
	}
	
	/**
	 * Cleans up printer output file
	 * 
	 * @param outputFileName - output file name
	 */
	protected void cleanupFile(String outputFileName) {
		// Delete the file
		boolean deleted = new File(outputFileName).delete();
		if(!deleted) {
			throw new PrintingException("Could not delete file: " + outputFileName);
		}
	}
}
