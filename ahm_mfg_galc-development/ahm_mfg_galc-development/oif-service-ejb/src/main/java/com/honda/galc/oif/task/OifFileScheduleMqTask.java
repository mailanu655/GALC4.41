package com.honda.galc.oif.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.oif.property.OifTaskPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;

public class OifFileScheduleMqTask extends OifFileScheduleBase implements
		IEventTaskExecutable {

	protected OifTaskPropertyBean oifPropertyBean;

	public OifFileScheduleMqTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try {

			String[] fileNames = getFileNamesFromMq();

			for (String filename : fileNames) {
				logger.info("Start to process file:", filename);
				processFile(StringUtils.trimToEmpty(filename));
			}

			logger.info("Oif schedule completed successful " + componentId);
		} catch (Exception e) {
			logger.error(e, " Exception to process schedule " + componentId);
		} 
	}

	private String[] getFileNamesFromMq() {

		String[] aReceivedFileList;
		aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID));

		if (aReceivedFileList == null) {
			logger.info("No file received for interface "

			+ getProperty(OIFConstants.INTERFACE_ID) + ".\nExiting... ");
			logger.info("No file received for interface "
					+ getProperty(OIFConstants.INTERFACE_ID));
			return aReceivedFileList;
		}

		return aReceivedFileList;
	}

	protected String[] getFilesFromMQ(String pInterfaceID) {
		String[] rcvdFileList = null;
		try {
			MQUtility mqutil = new MQUtility(this);
			String mqConfig = PropertyService.getProperty(
					OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
			String resultPath = PropertyService.getProperty(
					OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
			if (mqConfig == null || resultPath == null) {
				logger.info("Could not find properties for MQ config or/and output path.");
				throw new MQUtilityException("");
			}
			try {
				rcvdFileList = mqutil.getReceiveFile(pInterfaceID, mqConfig,
						resultPath);
			} catch (Exception ex) {
				logger.error("exception occured in receive file"
						+ ex.getMessage());
			}
			if (rcvdFileList == null || rcvdFileList.length <= 0) {
				logger.info("No file in the incoming MQ queue for interface = "
						+ pInterfaceID);
				return null;

			}
			
		} catch (MQUtilityException e) {
			logger.info("exp:" + e.getMessage());
			logger.error(e,
					"MQUtilityException raised when executing getFilesFromMQ.");
		} catch (Exception e) {
			logger.error(e, "Exception raised when executing getFilesFromMQ.");
		}
		return rcvdFileList;
	}
	
	private void processFile(String filename) {

		if (StringUtils.isEmpty(filename)) {
			logger.info("Invalid file name:" + filename);
			return;
		}

		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(filename));

			new OifFileScheduleHandler(getPropertyBean(), filename, reader,
					logger).process();

		} catch (SystemException se) {
			logger.error(se.getMessage() + filename);
		} catch (IOException e) {
			logger.error(e, " Exception to read file " + filename);
		} catch (Exception e) {
			logger.error(e, e.getMessage() + filename);
		} finally {
			if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	protected OifTaskPropertyBean getPropertyBean() {
		if (oifPropertyBean == null)
			oifPropertyBean = PropertyService.getPropertyBean(
					OifTaskPropertyBean.class, componentId);

		return oifPropertyBean;
	}

	public Logger getLogger() {
		return logger;
	}

	@Override
	protected BufferedReader getBufferedReader(String fileName) {
		return null;
	}

	public static void main(String[] args) {
		OifFileScheduleMqTask job = new OifFileScheduleMqTask("OifFileScheduleMqTask");
		job.execute(args);
		System.out.println("Executed");
	}
}
