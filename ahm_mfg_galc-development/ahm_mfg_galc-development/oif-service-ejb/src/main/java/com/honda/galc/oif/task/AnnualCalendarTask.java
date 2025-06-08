package com.honda.galc.oif.task;

import java.text.ParseException;
import java.util.List;

import com.honda.galc.common.OIFParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.AnnualCalendarDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.AnnualCalendar;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>AnnualCalendarTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AnnualCalendarTask.java description </p>
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
 * <TD>Feb 11, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Kamlesh Maharjan
 * @created Feb 11, 2014
 */
public class AnnualCalendarTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
//	The list of file names that are received from GPCS(MQ).
	protected String[] aReceivedFileList;
	
	public AnnualCalendarTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			processAnnualCalendar();
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current line 
	 * <p>
	 * @throws ParseException 
	 */
	private void processAnnualCalendar() throws ParseException {
		logger.info("start to process AnnualCalendar");
		refreshProperties();
//		Receive file from GPCS(MQ);
		
		aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),
				getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if (aReceivedFileList == null) {
			return;
		}

//		Get configured parsing data 
		String annualCal = getProperty(OIFConstants.PARSE_LINE_DEFS);
		OIFParsingHelper<AnnualCalendar> parseHelper = new OIFParsingHelper<AnnualCalendar>(AnnualCalendar.class, annualCal, logger);
		parseHelper.getParsingInfo();

//		Process file(s) and update Annual Calendar data  
		for (int count=0; count<aReceivedFileList.length; count++) {
			String receivedFile = aReceivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
			if (receivedRecords.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: " + receivedFile);
				setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
				continue;
			}
		
			for(String receivedRecord : receivedRecords) {
				AnnualCalendar annualCalendar = new AnnualCalendar();
				parseHelper.parseData(annualCalendar, receivedRecord);
//				Update or insert data
				ServiceFactory.getDao(AnnualCalendarDao.class).save(annualCalendar);
			}
			setIncomingJobCount(receivedRecords.size(), 0, aReceivedFileList);
			logger.info(" Annual Calendar saved; file processed: " + receivedFile);
		}
	}

}