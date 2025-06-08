package com.honda.galc.service.printing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import com.honda.galc.common.exception.PrintingException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.PrinterQueueSenderPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SimpleVarSubstitutor;

/**
 * 
 * <h3>PrintQueuePrinterDevice Class description</h3>
 * <p>
 * PrintQueuePrinterDevice description
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
public class PrintQueuePrinterDevice implements IPrinterDevice {

	private Logger logger;

	public PrintQueuePrinterDevice(Logger logger) {
		this.logger = logger;
	}

	public void print(byte[] printData, Device device, DataContainer dc) {

		try {
			PrinterQueueSenderPropertyBean propertyBean = PropertyService
					.getPropertyBean(PrinterQueueSenderPropertyBean.class);
			String outputFileName = makeOutputFileName(dc);
			outputFile(outputFileName, printData);
			String printQuantity = DataContainerUtil.getString(dc,
					DataContainerTag.PRINT_QUANTITY, "1");
			if (Integer.parseInt(printQuantity) == 0) {
				logger.info("print quantity is zero.so no need to print this.");
			} else {
				for (int i = 1; i <= Integer.parseInt(printQuantity); i++) {

					execPrintCmd(dc.getString(DataContainerTag.QUEUE_NAME),
							outputFileName);
					logger.info("Sent print data to print queue device " + i
							+ device);
				}

			}
			if (propertyBean.isCleanUp()) {
				cleanupFile(outputFileName);
				logger.info("output file " + outputFileName + " is deleted");
			}
		} catch (Exception ex) {
			DataContainerUtil.error(logger, dc, ex, "Could not send print data to print queue device " + device);
		}
	}

	/**
	 * Makes up output file name
	 * 
	 * @param dataContainer
	 *            input DataContainer
	 * 
	 * @return output file name
	 * @throws BroadcastException
	 */
	private String makeOutputFileName(DataContainer dc) {

		StringBuilder sb = new StringBuilder();

		try {

			PrinterQueueSenderPropertyBean propertyBean = PropertyService
					.getPropertyBean(PrinterQueueSenderPropertyBean.class);
			sb.append(propertyBean.getTempDir());
			sb.append(dc.get(DataContainerTag.QUEUE_NAME)).append("-");
			sb.append(dc.get(DataContainerTag.FORM_ID)).append("-");
			Thread currentThread = Thread.currentThread();

			sb.append(currentThread.getId()).append("-");

			sb.append(new Date().getTime()).append(".out");

		} catch (Exception e) {
			throw new PrintingException(
					"Failed to make a out file name due to ", e);
		}

		return sb.toString();
	}

	private void outputFile(String outputFileName, byte[] printData) {
		// Open output file
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputFileName);
			out.write(printData);
		} catch (IOException e) {
			throw new PrintingException("Could not open output", e);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Executes print command
	 * 
	 * @param outputFileName
	 *            - output file name
	 * @throws BroadcastException
	 */
	private void execPrintCmd(String queueName, String outputFileName) {

		try {

			PrinterQueueSenderPropertyBean propertyBean = PropertyService
					.getPropertyBean(PrinterQueueSenderPropertyBean.class);

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

			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			// stdout
			String line;
			while ((line = br.readLine()) != null) {
				logger.info("Print command stdout: " + line);
				;
			}
			br.close();

			// stderr
			br = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));

			while ((line = br.readLine()) != null) {
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
	 * @param outputFileName
	 *            - output file name
	 */
	protected void cleanupFile(String outputFileName) {
		// Delete the file
		boolean deleted = new File(outputFileName).delete();
		if (!deleted) {
			throw new PrintingException("Could not delete file: "
					+ outputFileName);
		}
	}
}
