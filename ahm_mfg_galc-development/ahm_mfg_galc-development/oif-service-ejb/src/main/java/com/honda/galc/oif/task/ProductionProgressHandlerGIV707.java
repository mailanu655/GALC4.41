package com.honda.galc.oif.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.GpcsProductionProgressDTO;
import com.honda.galc.service.productionlot.ProductionLotService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFConstants.DEPARTMENT_CODE;

/**
 * 
 * <h3>ProductionProgressHandlerGIV707</h3>
 * <p> ProductionProgressHandlerGIV707 is for GIV707 </p>
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
 * @author Ratul Chakravarty<br>
 * April 18, 2014
 *
 */


public class ProductionProgressHandlerGIV707 extends OifTask<GpcsProductionProgressDTO> implements IEventTaskExecutable {

	private File giv707File;
	private File updatedRecsGIV707LogFile;
	private static final String RUN_GIV707 = "GIV707";
	private ProductionProgressCommon ppc;
	private boolean isDebug = false;
	private String createTimestamp;
	private String sendPath;
	
	private static final String IS_TRANSMISSION_PLANT 	=	"IS_TRANSMISSION_PLANT";	
	private static final String PROCESS_POINT_AM_ON		=	"PROCESS_POINT_AM_ON";
	private static final String PROCESS_POINT_AM_OFF	=	"PROCESS_POINT_AM_OFF";
	private static final String GIV707_ID				=	"GIV707_ID";
	private static final String DEPARTMENTS				=	"DEPARTMENTS";	
	private static final String INITIAL_DATE			=	"INITIAL_DATE"; 
	private final static String DATE_FORMAT 			=	"yyyyMMdd";
	private final static String TIME_FORMAT 			=	"HHmmss";
	private final static Integer LOT_STATUS 			=	4;
	

	public ProductionProgressHandlerGIV707(String componentId) throws IOException {
		super(componentId);
	}

	protected void initialize() {
		logger.info("initializing ProductionProgressHandlerGIV707.");
		this.isDebug = getPropertyBoolean("DEBUG_PRODUCTION_PROGRESS", false);
		if (isDebug) {
			logger.info("RES is set to " + getProperty("RES"));
//			logger.info("GIV707_RECORD_LENGTH is set to " + getProperty("GIV707_RECORD_LENGTH"));
			logger.info("GIV707_ID is set to " + getProperty("GIV707_ID"));
			logger.info("Updated records log file name for GIV707 is :- " + getProperty("FILE_NAME"));
		}
		errorsCollector = new OifErrorsCollector(this.componentId);
		ppc = new ProductionProgressCommon();
		createTimestamp = ppc.getCurrentTimestamp();		
	}

	public void execute(Object[] args) {		
		try {
			//Validate if the production result is for Transmission.
			//If not execute the normal process for Frame and Engine plant.
			Boolean isTransmissionPlant	=	getPropertyBoolean( IS_TRANSMISSION_PLANT, false );
			if(!isTransmissionPlant)
			{
				initialize();
				exportRecord();
			}
			else
			{
				productionProgressAM();								
			}
		} finally {
			errorsCollector.sendEmail();
		}
	}

	private void exportRecord() {
		String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		String logFilePath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "LOGGING");
		boolean allowDBUpdate = getPropertyBoolean("ALLOW_DB_UPDATE", true);
		String giv707FileName = getProperty(GIV707_ID) + this.createTimestamp + ".oif";
		String updatedRecsGIV707LogFileName = getProperty("FILE_NAME") + "_" + this.createTimestamp + ".log";

		// Create the output file objects
		giv707File = new File(resultPath + giv707FileName);
		updatedRecsGIV707LogFile = new File(logFilePath + updatedRecsGIV707LogFileName);

		if (isDebug) {
			logger.info("After creating file " + giv707File.getAbsolutePath() + ". File exists " + giv707File.exists());
			logger.info("ALLOW_DB_UPDATE :- " + allowDBUpdate);
		}
		// Track the number of records being written to the interface file
		int totalRecordCount = 0;
		// call the web services on the active lines and get production progress data from each line
		String[] activeLinesURLsArr = getProperty("ACTIVE_LINES_URLS").split(",");
		for (int i = 0; i < activeLinesURLsArr.length; i++) {
			int recordCount = 0;
			int totalLineRecordCount = 0;
			for(DEPARTMENT_CODE div : DEPARTMENT_CODE.values()) {
				try {
					recordCount = ppc.exportProductionProgress(giv707File, activeLinesURLsArr[i], this.componentId, RUN_GIV707, div, errorsCollector, updatedRecsGIV707LogFile, allowDBUpdate);
				} catch (Exception e) {
					logger.error(e, "Exception occured while executing task with interface ID : " + this.componentId + " and department: " + div.name());
					errorsCollector.emergency(e, "Exception occured while executing task with interface ID : " + this.componentId + " and department: " + div.name());
				}
				totalLineRecordCount += recordCount;
				if (isDebug) {
					logger.info("Active line: " + activeLinesURLsArr[i] + ", Department: " + div + "; Record count: " + recordCount + ", Total Line record count: " + totalLineRecordCount);
				}
			}
			totalRecordCount += totalLineRecordCount;
			if (isDebug) {
				logger.info("Active line: " + activeLinesURLsArr[i] + "; Total Line record count: " + totalLineRecordCount + ", Total record count: " + totalRecordCount);
			}
		}

		// Create a record with a message of
		// "NO RECORD" if it does not find any production lot that has
		// remaining bodies
		if (isDebug) {
			logger.info("total count = " + totalRecordCount);
		}
		if (totalRecordCount == 0) {
			try {
				logger.info("Creating an export file that contains NoRecords message for interface ID = " + this.componentId + "; file name = " + giv707FileName);
				BufferedWriter giv707Writer = new BufferedWriter(new FileWriter(giv707File));
				giv707Writer.write(MQUtility.createNoDataString(120));
				giv707Writer.newLine();
				giv707Writer.close();
			} catch (IOException e) {
				logger.error(e, "IOException occured writing to file while executing task with interface ID : " + this.componentId);
				errorsCollector.emergency(e, "IOException occured writing to file while executing task with interface ID : " + this.componentId);
			}
		}

		Boolean sendFileToGPCS = PropertyService.getPropertyBoolean(componentId, "SEND_FILE_TO_GPCS", true);
		sendPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		if(sendFileToGPCS) {
			try {
				logger.info("Sending " + giv707FileName + " to MQ with total record count = " + totalRecordCount);
				MQUtility mqu = new MQUtility(this);
				logger.info("Calling executeMQSendAPI with send file name  = " + sendPath + giv707FileName + " with Interface ID = " + getProperty(GIV707_ID));
				String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
				mqu.executeMQSendAPI(getProperty(GIV707_ID), mqConfig, sendPath + giv707FileName);
				setOutgoingJobStatus(totalRecordCount, giv707FileName);
			} catch (MQUtilityException e) {
				String errorStr = "MQUtilityException raised when sending interface file for " + sendPath + giv707FileName + " for the interface " + this.componentId + ". Exception : " + e.getMessage();
				logger.error(errorStr);
				errorsCollector.emergency(errorStr);
				setOutgoingJobStatusAndFailedCount(totalRecordCount, OifRunStatus.MQ_ERROR, giv707FileName);
			}
		} else {
			logger.info("Flag SEND_FILE_TO_GPCS is set to false, file was not sent = " + sendPath + giv707FileName + " with Interface ID = " + getProperty(GIV707_ID));
		}
	}
	
	
	private void productionProgressAM()
	{
		
		
		final Integer recordSize 			=	getPropertyInt(	OIFConstants.MESSAGE_LINE_LENGTH		);		
		final String[] listProcessLocation 	=	getProperty(	DEPARTMENTS			).split(",");
		final String propertyDate 			=	getProperty(	INITIAL_DATE		);
		final String processPointAmOn 		=	getProperty(	PROCESS_POINT_AM_ON	);
		final String processPointAmOff 		=	getProperty(	PROCESS_POINT_AM_OFF);
		final String plantCode 				=	PropertyService.getSiteName();
		final String interfaceId 			=	getProperty(	OIFConstants.INTERFACE_ID			); // here is the id for interface
		final String[]	activeLineUrl		=	getPropertyArray( OIFConstants.ACTIVE_LINES );
		
		Date createDate;
		try {
			createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(propertyDate);		
			List<Object> productionProgressList = new ArrayList<Object>();
			final List<GpcsProductionProgressDTO> result = new ArrayList<GpcsProductionProgressDTO>();
			// Initialize the output format helper
			final String layoutDefinition = getProperty(OIFConstants.PARSE_LINE_DEFS);		
			
			for (String activeLine : activeLineUrl) {
				
				logger.info( "Process the line " + activeLine );
				//get the production result service 
				ProductionLotService productionLotService = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ProductionLotService.class);
				
				
				for (String processLocation : listProcessLocation) {
					productionProgressList.addAll(productionLotService.getProductionProgress(processLocation, plantCode, createDate, processPointAmOn, processPointAmOff));							
				}
										
					
				// copy properties from entity to a DTO
				Date actualDate = new Date();
				
	
				for (Object object : productionProgressList) {
					
					Integer remainingLot = 0;
					final Integer lotSize = (Integer) ((Object[]) object)[8];
					final Integer productionCountOn = (Integer) ((Object[]) object)[11];
					final Integer productionCountOff = (Integer) ((Object[]) object)[12];
					final Integer scrapCount = (Integer) ((Object[]) object)[13];
					final Integer exceptionalCountOn = (Integer) ((Object[]) object)[14];
					final Integer exceptionalCountOff = (Integer) ((Object[]) object)[15];
	
					
					for (int i = 1; i <= 2; i++) {
						if (i == 1) {
							remainingLot = lotSize - productionCountOn - scrapCount + exceptionalCountOn;
						} else {
							remainingLot = lotSize - productionCountOff - scrapCount + exceptionalCountOff;
						}
						ProductionLot productionLot = productionLotService.getProductionLot((String) ((Object[]) object)[16]);  
							//productionLotDao.findByKey((String) ((Object[]) object)[16]);
						if (remainingLot.intValue() <= 0) {
							if (i == 2) {
								// This block only apply in off of process location
								productionLot.setLotStatus(LOT_STATUS);
								productionLotService.updateProductionLot(productionLot);								
							}
							continue;
						}
	
						final GpcsProductionProgressDTO productionProgressDTO = new GpcsProductionProgressDTO();
						productionProgressDTO.setPlanCode(productionLot.getPlanCode());
						productionProgressDTO.setLineNo(productionLot.getLineNo());
						productionProgressDTO.setProcessLocation(productionLot.getProcessLocation());
						productionProgressDTO.setOnOffFlag(i + "");
						productionProgressDTO.setKdLotNo(productionLot.getKdLotNumber());
						productionProgressDTO.setProdSeqNo(productionLot.getLotNumber());
						productionProgressDTO.setMbpn((String) ((Object[]) object)[5]);
						productionProgressDTO.setHesColor((String) ((Object[]) object)[6]);
						productionProgressDTO.setMtoc(productionLot.getProductSpecCode());	//productSpectCode
						productionProgressDTO.setProductionQty(String.format("%5s", productionLot.getLotSize()).replace(' ', '0'));
						productionProgressDTO.setResultQty(String.format("%5s", remainingLot.toString()).replace(' ', '0'));
						productionProgressDTO.setCreatedDate(new SimpleDateFormat(DATE_FORMAT).format(actualDate));
						productionProgressDTO.setCreatedTime(new SimpleDateFormat(TIME_FORMAT).format(actualDate));
						productionProgressDTO.setMinusFlag("");
						productionProgressDTO.setFiller("");
						result.add(productionProgressDTO);
					}
				}
			}
			
			logger.info( "Sending to MQ... " );
			final String path				=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT );
			final String mqConfig			=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG );
			
			final String fileName			=	new StringBuilder( interfaceId )
												.append("_")
												.append( OIFConstants.stsf1.format( new Date() ))
												.toString();
			
			if ( result != null && result.size() > 0)
			{
				this.exportDataByOutputFormatHelper(GpcsProductionProgressDTO.class, result, path, fileName, mqConfig, layoutDefinition);
			}
			else
			{
				//send the message with GPCS NORECORD YYYYMMDDHHMMSS + 37 bytes(spaces)
				logger.info( "Sending the GPCS NORECORD message" );
				List<String> noDataMessage	=	new ArrayList<String>();
				final String message	=	MQUtility.createNoDataMessage( recordSize );
				noDataMessage.add( message );
				this.sendNoDataMessage(noDataMessage, path, fileName, mqConfig);
			}	
		} catch (ParseException parseException) {
			logger.error("Error when try to convert date for Transmission Production Progress GIV707 Interface " + parseException);
			errorsCollector.error("Error when try to convert date for Transmission Production Progress GIV707 Interface " + parseException);
			
		}
		
	}

	

}