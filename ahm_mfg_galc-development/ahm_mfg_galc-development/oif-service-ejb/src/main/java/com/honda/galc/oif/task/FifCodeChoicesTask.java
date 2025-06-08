package com.honda.galc.oif.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.FifCodeChoicesDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.fif.FifCodeChoices;
import com.honda.galc.entity.fif.FifCodeChoicesId;
import com.honda.galc.oif.dto.FifCodeChoicesDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>FifCodeChoicesTask.java</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * FifCodeChoicesTask.java description
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
 * <TR>
 * <TD>KM</TD>
 * <TD>Feb 19, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @version 0.1
 * @author Xiaomei Ma
 * @created Feb 19, 2015
 */

public class FifCodeChoicesTask extends OifTask<Object> implements
		IEventTaskExecutable {

	// The list of file names that are received from GPCS(MQ).
	protected String[] aReceivedFileList;
	private int noOfRecordsReceived = 0, noOfFailRecords = 0;

	public FifCodeChoicesTask(String name) {
		super(name);
	}

	public void execute(Object[] args) {
		try {
			processFifCodeChoices();
		} catch (TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			setIncomingJobCount(noOfRecordsReceived-noOfFailRecords, noOfFailRecords, aReceivedFileList);
			errorsCollector.sendEmail();
		}
	}

	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current line
	 * <p>
	 * 
	 * @throws ParseException
	 */
	private void processFifCodeChoices() throws ParseException {
		logger.info("start to process CodeChoices");
		refreshProperties();
		// Receive file from GPCS(MQ);
		
		aReceivedFileList = getFilesFromMQ(
				getProperty(OIFConstants.INTERFACE_ID),
				getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if (aReceivedFileList == null) {
			return;
		}

		// Get configured parsing data
		String fifCodeCho = getProperty(OIFConstants.PARSE_LINE_DEFS);
		OIFSimpleParsingHelper<FifCodeChoicesDTO> parseHelper = new OIFSimpleParsingHelper<FifCodeChoicesDTO>(
				FifCodeChoicesDTO.class, fifCodeCho, logger);
		parseHelper.getParsingInfo();

		// Process file(s) and update FIF Code Choices data
		for (int count = 0; count < aReceivedFileList.length; count++) {
			String receivedFile = aReceivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(
					OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(
					resultPath + receivedFile, logger);
			if (receivedRecords.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: "
						+ receivedFile);
				continue;
			}
			noOfRecordsReceived +=receivedRecords.size();
		

			// Process files(s)
			List<FifCodeChoicesDTO> fifCodeChoicesDTOList = new ArrayList<FifCodeChoicesDTO>();
			for (String receivedRecord : receivedRecords) {
				FifCodeChoicesDTO fifCodeChoicesDTO = new FifCodeChoicesDTO();
				parseHelper.parseData(fifCodeChoicesDTO, receivedRecord);
				if (fifCodeChoicesDTO.getDevSeqCd() == null
						|| fifCodeChoicesDTO.getEfctBegDt() == null
						|| fifCodeChoicesDTO.getFifCode() == null
						|| fifCodeChoicesDTO.getFifType() == null
						|| fifCodeChoicesDTO.getGroupCd() == null
						|| fifCodeChoicesDTO.getModelCd() == null
						|| fifCodeChoicesDTO.getModelYear() == null
						|| fifCodeChoicesDTO.getPlantCd() == null) {
					logger.emergency("The primary key is missing for this record: "
							+ receivedRecord);
					errorsCollector
							.emergency("The primary key is missing for this record: "
									+ receivedRecord);
					noOfFailRecords++;
				} else {
					fifCodeChoicesDTOList.add(fifCodeChoicesDTO);
				}
			}

			// Insert data
			for (FifCodeChoicesDTO fifCodeChoicesDTO : fifCodeChoicesDTOList) {
					FifCodeChoices fifCodeChoices = fifCodeChoicesDTO.deriveFifCodeChoices();
					FifCodeChoicesDao fifCodeChoicesDao = ServiceFactory.getDao(FifCodeChoicesDao.class);
					//Delete any existing FifCodeChoice because GPCS is not sending new end date for expired parts
					FifCodeChoicesId fifCodeChoicesId = fifCodeChoices.getId();
					List<FifCodeChoices> existingFifChoices = 
							fifCodeChoicesDao.findFifCodeChoices(fifCodeChoicesId.getPlantCd(), fifCodeChoicesId.getModelYear(), fifCodeChoicesId.getModelCd(),
									                    		fifCodeChoicesId.getDevSeqCd(), fifCodeChoicesId.getFifCode(),fifCodeChoicesId.getFifType(),
									                    		fifCodeChoicesId.getGroupCd());
					if(existingFifChoices != null && !existingFifChoices.isEmpty()) {
						fifCodeChoicesDao.removeAll(existingFifChoices);
					}
					fifCodeChoicesDao.save(fifCodeChoices);
			}
			logger.info(" FIF Code Choices saved; file processed: "
					+ receivedFile);

		}
	}
}