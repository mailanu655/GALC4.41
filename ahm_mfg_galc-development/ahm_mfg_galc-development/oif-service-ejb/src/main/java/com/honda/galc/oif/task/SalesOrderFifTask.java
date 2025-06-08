package com.honda.galc.oif.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.fif.SalesOrderFif;
import com.honda.galc.oif.dto.SalesOrderFifDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>SalesOrderFifTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SalesOrderFifTask.java description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>Feb 16, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */

public class SalesOrderFifTask extends OifTask<Object> implements
		IEventTaskExecutable {

	// The list of file names that are received from GPCS(MQ).
	protected String[] aReceivedFileList;
	private int noOfRecordsReceived = 0, noOfFailRecords = 0;

	public SalesOrderFifTask(String name) {
		super(name);
	}

	public void execute(Object[] args) {
		try {
			processSalesOrderFif();
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
	private void processSalesOrderFif() throws ParseException {
		logger.info("start to process SalesOrderFif");

		// Refresh properties
		refreshProperties();

		// Receive file from GPCS(MQ);
		
		aReceivedFileList = getFilesFromMQ(
				getProperty(OIFConstants.INTERFACE_ID),
				getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if (aReceivedFileList == null) {
			return;
		}

		// Get configured parsing data
		String parseLineDefs = getProperty(OIFConstants.PARSE_LINE_DEFS);
		OIFSimpleParsingHelper<SalesOrderFifDTO> parseHelper = new OIFSimpleParsingHelper<SalesOrderFifDTO>(
				SalesOrderFifDTO.class, parseLineDefs, logger);
		parseHelper.getParsingInfo();

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
			List<SalesOrderFifDTO> salesOrderFifDTOList = new ArrayList<SalesOrderFifDTO>();
			for (String receivedRecord : receivedRecords) {
				SalesOrderFifDTO salesOrderFifDTO = new SalesOrderFifDTO();
				parseHelper.parseData(salesOrderFifDTO, receivedRecord);
				if (salesOrderFifDTO.getUniqueId() == null
						|| salesOrderFifDTO.getSalesModelCd() == null
						|| salesOrderFifDTO.getSalesVehDestCd() == null
						|| salesOrderFifDTO.getSalesOptionCd() == null
						|| salesOrderFifDTO.getSalesExtClrCd() == null
						|| salesOrderFifDTO.getSalesIntClrCd() == null
						|| salesOrderFifDTO.getOrderType() == null
						|| salesOrderFifDTO.getFrmPlantCd() == null
						|| salesOrderFifDTO.getFrmModelYearCd() == null
						|| salesOrderFifDTO.getFrmModelCd() == null
						|| salesOrderFifDTO.getFrmDevSeqCd() == null
						|| salesOrderFifDTO.getFrmTypeCd() == null
						|| salesOrderFifDTO.getFrmOptionCd() == null
						|| salesOrderFifDTO.getFrmExtClrCd() == null
						|| salesOrderFifDTO.getFrmIntClrCd() == null) {
					logger.emergency("The primary key is missing for this record: "
							+ receivedRecord);
					errorsCollector
							.emergency("The primary key is missing for this record: "
									+ receivedRecord);
					noOfFailRecords++;
				} else {
					salesOrderFifDTOList.add(salesOrderFifDTO);
				}
			}

			// Update or insert data
			for (SalesOrderFifDTO salesOrderFifDTO : salesOrderFifDTOList) {
				SalesOrderFif salesOrderFif = salesOrderFifDTO
						.deriveSalesOrderFif();
				SalesOrderFifDao salesDao = ServiceFactory.getDao(SalesOrderFifDao.class);
				//get existing sales_order_fif by spec code only, ignoring order_seq_no
				List<SalesOrderFif> existing = salesDao.getSalesOrderFifBySpecCode(salesOrderFif.getId());
				if(existing != null && !existing.isEmpty())  {
					salesDao.removeAll(existing);
				}
				salesDao.save(salesOrderFif);					
				logger.debug("Sales Order FIF record saved" + salesOrderFif);
			}
			logger.info("Sales Order FIF record saved; file processed: "
					+ receivedFile);
		}
	}
}