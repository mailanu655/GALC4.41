package com.honda.galc.oif.task;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.ValidEinDTO;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;


public class NseValidEinsTask extends OifTask<Object> implements IEventTaskExecutable {
	
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	private OifErrorsCollector errorsCollector;

	public NseValidEinsTask(String pObjectName) throws IOException {
		super(pObjectName);
		errorsCollector = new OifErrorsCollector(pObjectName);
	}


	public void execute(Object[] args) {
		try {
			processNseValidEins();
		} catch(TaskException e) {
			logger.emergency(e);
			errorsCollector.emergency(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	private void processNseValidEins() {
		logger.info("Started NseValidEinsTask.");
		initialize();
		OutputFormatHelper<ValidEinDTO> ofHelper = 
			new OutputFormatHelper<ValidEinDTO>(getProperty(OIFConstants.OUTPUT_FORMAT_DEFS), this.logger, this.errorsCollector);
		ofHelper.initialize(ValidEinDTO.class);
		List<String> validEinList = new ArrayList<String>();
		List<Object[]> tempList = new ArrayList<Object[]>();
		String interfaceID = getProperty(OIFConstants.INTERFACE_ID);
		String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
		SimpleDateFormat stsf1 = new SimpleDateFormat(
				PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "TIMESTAMP_FORMAT"));
		Timestamp ts = new Timestamp((new Date()).getTime());
		String exportFilePath = new StringBuffer(resultPath).append(interfaceID).append(stsf1.format(ts)).append(".oif").toString();
		List<ComponentProperty> activeLineURLs = PropertyService.getProperties(
				componentId, "ACTIVE_LINE_URL(?:\\{(.*)})");
		for (ComponentProperty activeLineURL : activeLineURLs) {
			if(activeLineURL == null) {
				logger.error("ACTIVE_LINE_URLs not set.");
				continue;
			}
			String activeLine = activeLineURL.getPropertyValue();
			logger.info("Requesting data from " + activeLine);
			EngineDao engineDao = HttpServiceProvider.getService(activeLine + HTTP_SERVICE_URL_PART, EngineDao.class);
			tempList.clear();
			if(engineDao != null) {
				tempList = engineDao.findValidEins();
			}
			if(tempList.size() == 0) {
				logger.error("No data from " + activeLine);
				continue;
			} else {
				logger.info("Found " + tempList.size() + " records from " + activeLine);
			}
			int n = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
			char[] charArray = new char[n];
			Arrays.fill(charArray, ' ');
			String prefix=getProperty("PREFIX");
			for(Object[] objs : tempList) {
				ValidEinDTO engineData = new ValidEinDTO();
				engineData.setEngineId(objs[0].toString());
				engineData.setModelYear(objs[1].toString());
				engineData.setModelCode(objs[2].toString());
				engineData.setModelTypeCode(objs[3].toString());
				engineData.setModelOptionCode(objs[4].toString());
				engineData.setTimestamp((Timestamp)objs[5]);
				engineData.setPrefix(prefix);
				validEinList.add(ofHelper.formatOutput(engineData, charArray));
			}
		}
		
		if (validEinList.size() != 0) {
			try {
				OIFFileUtility.createFile(exportFilePath);	// create empty file
			} catch(IOException e) {
				String errorResult = "Failed to create file: " + exportFilePath + " for interface: " + interfaceID;
				logger.error(e, errorResult);
				errorsCollector.emergency(e, errorResult);
				return;
			}
			try {
				OIFFileUtility.writeToFile(validEinList, exportFilePath);
			} catch (IOException e) {
				String errorResult = "Failed to write data for: " + interfaceID + " to file: " + exportFilePath;
				logger.error(e, errorResult);
				errorsCollector.emergency(e, errorResult);
			}
		}
		logger.info(new StringBuffer("Processed ").append(interfaceID).append(", File: ").append(exportFilePath).toString());
		Boolean sendFileToNSE = PropertyService.getPropertyBoolean(componentId, "SEND_FILE_TO_NSE", true);
		if (sendFileToNSE) {
			if(validEinList.size() > 0) {
				String interfaceId = getProperty(OIFConstants.INTERFACE_ID);
				MQUtility mqutil = new MQUtility(this);
				try {
					mqutil.executeMQSendAPI(interfaceId, PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG), exportFilePath);
				} catch (MQUtilityException e) {
					StringBuffer emergencyMessage = new StringBuffer();
					emergencyMessage.append("Failed to send interface. ").append(interfaceId)
						.append(", File: ").append(exportFilePath);
					logger.emergency(emergencyMessage.toString());
					errorsCollector.emergency(emergencyMessage.toString());
				}
				logger.info(new StringBuffer("Sent ").append(interfaceID).append(", File: ").append(exportFilePath).toString());
			} else {
				logger.info("No EINs found "+ interfaceID);
			}
		} else {
			logger.info("Flag SEND_FILE_TO_NSE is set to false, file was not sent = "
					+ exportFilePath + " with Interface ID = " + interfaceID);
		}
	}
	
}
