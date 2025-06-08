package com.honda.galc.oif.task;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.openjpa.util.OpenJPAException;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.ProcessingBodyDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.productionlot.ProductionLotService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>ProcessingBodyHandlerGIV706</h3>
 * <p>
 * ProcessingBodyHandlerGIV706 is for GIV706
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
 * @author Larry Karpov<br>
 *         June 18, 2014
 * 
 */

public class ProcessingBodyHandlerGIV706 extends OifTask<ProcessingBodyDTO> implements
		IEventTaskExecutable {

	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	
	//local constants
	private static final String PROCESS_POINT			=	"PROCESS_POINT";
	private static final String LINE_TRACKING_STATUS	=	"TRACKING_STATUS";
	private static final String IS_TRANSMISSION_PLANT	=	"IS_TRANSMISSION_PLANT";

	//private OifErrorsCollector errorsCollector;
	private boolean isDebug = false;
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);
	String sendFileName;

	public ProcessingBodyHandlerGIV706(String componentId) throws IOException {
		super(componentId);
	}

	public void execute(Object[] args){
		
		logger.info( "Starting Production Restult interface");
		refreshProperties();
		//Validate if the production result is for Transmission.
		//If not execute the normal process for Frame and Engine plant.
		Boolean isTransmissionPlant	=	getPropertyBoolean( IS_TRANSMISSION_PLANT, false );
		this.initialize();
		try {
			if (!isTransmissionPlant )
			{
				exportRecords();
			}
			else
			{
				executeProcessingBody();
			}
		} finally {
			errorsCollector.sendEmail();
		}
	}

	private void exportRecords() {
		String interfaceID = getProperty(OIFConstants.INTERFACE_ID);
		String sendPath = PropertyService.getProperty(
				OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		Timestamp ts = new Timestamp((new Date()).getTime());
		Boolean useProdDate = PropertyService.getPropertyBoolean(componentId, "USE_CUSTOM_PRODUCTION_DATE", false);
		if(useProdDate) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "PROD_DATETIME_FORMAT"));
			try {
				Date customProdDate = sdf.parse(getProperty("CUSTOM_PRODUCTION_DATE"));
				ts = new Timestamp(customProdDate.getTime());
			} catch (ParseException e) {
				;
			}
		}
		SimpleDateFormat stsf1 = new SimpleDateFormat(
				PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "TIMESTAMP_FORMAT"));
		sendFileName = new StringBuffer(interfaceID)
				.append(stsf1.format(ts)).append(".oif")
				.toString();
		String exportFilePath = new StringBuffer(sendPath).append(sendFileName)
				.toString();

		// Track the number of records being written to the interface file
		int totalRecordCount = 0;
		// get processing body data from each line other than current
		List<ComponentProperty> activeLineURLs = PropertyService.getProperties(
				componentId, "ACTIVE_LINE_URL(?:\\{(.*)})");
		List<String> outputRecords = new ArrayList<String>();
		try {
			OIFFileUtility.createFile(exportFilePath);	// create empty file
		} catch(IOException e) {
			String errorStr = "IOException raised when creating file for "
				+ sendFileName + " for the interface " + interfaceID;
			logger.error(e, errorStr);
			errorsCollector.emergency(e, errorStr);
			return;
		}
		String[] activeLinesURLsArr = getProperty("ACTIVE_LINES_URLS").split(",");
		for (int i = 0; i < activeLinesURLsArr.length; i++) {
			ProductionLotService productionLotService = HttpServiceProvider
					.getService(activeLinesURLsArr
							+ HTTP_SERVICE_URL_PART, ProductionLotService.class);
			outputRecords.clear();
			List<Object[]> processingBodyList = productionLotService.getProcessingBody(componentId);
			convertToString(outputRecords, processingBodyList);
			
			try {
				OIFFileUtility.addToFile(outputRecords, exportFilePath);
			} catch (IOException e) {
				String errorStr = "IOException raised when saving data to file for "
						+ sendFileName
						+ " for the interface "
						+ interfaceID
						+ ". Exception : " + e.getMessage();
				logger.error(e, errorStr);
				errorsCollector.emergency(e, errorStr);
			}
			if(outputRecords != null) {
				totalRecordCount += outputRecords.size();
			}
	 	}
		// get data from the current line
		if (getPropertyBoolean("INCLUDE_ACTIVE_LINE", true)) {
			ProductionLotService service = ServiceFactory
					.getService(ProductionLotService.class);
			outputRecords.clear();
			List<Object[]> processingBodyList = service.getProcessingBody(getComponentId());
			convertToString(outputRecords, processingBodyList);
			
			try {
				OIFFileUtility.addToFile(outputRecords, exportFilePath);
			} catch (IOException e) {
				String errorStr = "IOException raised when saving data to file for "
						+ sendFileName
						+ " for the interface "
						+ interfaceID
						+ ". Exception : " + e.getMessage();
				logger.error(e, errorStr);
				errorsCollector.emergency(e, errorStr);
			}
			if(outputRecords != null) {
				totalRecordCount += outputRecords.size();
			}
		}
		Boolean sendFileToGPCS = PropertyService.getPropertyBoolean(componentId, "SEND_FILE_TO_GPCS", true);
		if (totalRecordCount == 0) {
			outputRecords.clear();
			Integer lineLength = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
			if(lineLength == null) {
				String errorStr = OIFConstants.MESSAGE_LINE_LENGTH + " not set; noData message not created. "
						+ sendFileName
						+ " for the interface "
						+ interfaceID;
				logger.error(errorStr);
				errorsCollector.error(errorStr);
				sendFileToGPCS = false;
				lineLength = 0;
			}
			String noDataMessage = MQUtility.createNoDataMessage(lineLength);
			outputRecords.add(noDataMessage);
			try {
				OIFFileUtility.addToFile(outputRecords, exportFilePath);
			} catch (IOException e) {
				String errorStr = "IOException raised when saving data to file for "
						+ sendFileName
						+ " for the interface "
						+ interfaceID
						+ ". Exception : " + e.getMessage();
				logger.error(e, errorStr);
				errorsCollector.error(e, errorStr);
				sendFileToGPCS = false;
			}
		}
		logger.info("Sending " + exportFilePath
				+ " to MQ with total record count = " + totalRecordCount);
		if(sendFileToGPCS) {
			MQUtility mqu = new MQUtility(this);
			logger.info("Calling executeMQSendAPI with send file name  = "
					+ sendFileName + " with Interface ID = " + interfaceID);
			try {
				mqu.executeMQSendAPI(interfaceID,
						PropertyService.getProperty(
								OIFConstants.OIF_SYSTEM_PROPERTIES,
								OIFConstants.MQ_CONFIG), exportFilePath);
				setOutgoingJobStatus(totalRecordCount, sendFileName);
			} catch (MQUtilityException e) {
				String errorStr = "MQUtilityException raised when sending interface file for "
						+ sendFileName
						+ " for the interface "
						+ interfaceID
						+ ". Exception : " + e.getMessage();
				logger.error(errorStr);
				errorsCollector.emergency(errorStr);
				setOutgoingJobStatusAndFailedCount(totalRecordCount, OifRunStatus.MQ_ERROR, sendFileName);
			}
		} else {
			logger.info("Flag SEND_FILE_TO_GPCS is set to false, file was not sent = "
					+ sendFileName + " with Interface ID = " + interfaceID);
		}
	}
	
	protected void initialize() {
		//this.errorsCollector = new OifErrorsCollector(componentId);
		this.logger.info("initializing ProcessingBodyHandlerGIV706.");
		this.isDebug = getPropertyBoolean("DEBUG", false);
		if (this.isDebug) {
			this.logger.info("SEND is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.SEND));
			this.logger.info("GIV706_ID is set to "
					+ getProperty(OIFConstants.INTERFACE_ID));
		}
	}
	
	/**
	 * Method to get the processing body result
	 */
	private void executeProcessingBody()
	{
		try {
			logger.info( "Starting the Processing Body Task" );
			final String[]	activeLineUrl 		=	getProperty( OIFConstants.ACTIVE_LINES ).split(",");
			final String 	processPoint		=	getProperty( PROCESS_POINT );
			//gets the tracking status line for Scrap and Exceptional
			final String	trackingStatus		=	getProperty( LINE_TRACKING_STATUS );
			//final List<String> departments		=	PropertyService.getPropertyList( getName() , "DEPARTMENTS" );
			final int		recordSize			=	getPropertyInt( OIFConstants.MESSAGE_LINE_LENGTH );
			
			List<ProcessingBodyDTO> result			=	new ArrayList<ProcessingBodyDTO>();
			for (String activeLine : activeLineUrl)
			{
				logger.info( "Process the line " + activeLine );
				//get the production result service 
				ProductionLotService productionLotService = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ProductionLotService.class);
				
				logger.info( "Get the Processing Body data... " );
				List<Object []> processingBodyResult	=	productionLotService.getProcessingBody( processPoint, trackingStatus );
				convertToDTO(result, processingBodyResult);
			}
			
			logger.info( "Sending to MQ... " );
			final String path				=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT );
			final String mqConfig			=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG );
			final String layoutComponentId	=	getProperty					( OIFConstants.PARSE_LINE_DEFS );
			final String fileName			=	new StringBuilder( getProperty( OIFConstants.INTERFACE_ID ) )
												.append("_")
												.append( OIFConstants.stsf1.format( new Date() ))
												.toString();
			if ( result != null && result.size() > 0)
			{
				this.exportDataByOutputFormatHelper( ProcessingBodyDTO.class, result, path, fileName, mqConfig, layoutComponentId);
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
			logger.info( "Finish Processing Body Task" );
		} catch ( OpenJPAException px )
		{
			logger.error( "Error whe executing the Processing Body Task: " + px.getMessage() );
			errorsCollector.error( "Error whe executing the Processing Body Task: " + px.getMessage() );
		}
	}
	
	private void convertToDTO(List<ProcessingBodyDTO> result, List<Object[]> processingBodyResult) {
		int count = 1;
		for (Object[] body : processingBodyResult)
		{
			ProcessingBodyDTO processingBody	=	new ProcessingBodyDTO();
			processingBody.setPlanCode					(	body[0].toString()	);
			processingBody.setLineNumber				(	body[1].toString()	);
			processingBody.setInHouseProcessLocation	(	body[2].toString()	);
			processingBody.setVinNumber					(	ProductNumberDef.justifyJapaneseVIN(body[3].toString(), JapanVINLeftJustified.booleanValue()));
			processingBody.setProductionSequenceNumber	(	body[4].toString()	);
			processingBody.setAlcActualTimestamp		(	body[5].toString()	);
			processingBody.setProductSpecCode			(	body[6].toString().substring(0, 22)	);
			processingBody.setKdLotNumber				(	body[7].toString()	);
			processingBody.setPartNumber				(	body[8] == null ? "" : body[8].toString());
			processingBody.setPartColorCode				(	body[9] == null ? "" : body[9].toString());
			processingBody.setOnSequenceNumber			(	String.format( "%05d", count)	);
			count++;
			result.add( processingBody );
		}
	}
	
	private void convertToString(List<String> result, List<Object[]> processingBodyList) {
		StringBuffer vRecord = new StringBuffer();
		// Result set returns the following fields :
		// 0 - Plan_Code
		// 1 - Line_No
		// 2 - Process_Location
		// 3 - Product_ID
		// 4 - Production_Lot
		// 5 - Actual_Timestamp
		// 6 - Product_Spec_Code
		// 7 - KD_Lot_Number
		if(processingBodyList != null && !processingBodyList.isEmpty()) {
			int recordCount = 0;
			SimpleDateFormat stsf1 = new SimpleDateFormat("yyyyMMddHHmmss");
			for (Object[] objArr : processingBodyList) {
				recordCount++;
				// Clean up the string buffer
				vRecord.delete(0, vRecord.length());
				vRecord.append(objArr[0].toString());
				vRecord.append(objArr[1].toString());
				vRecord.append(objArr[2].toString());
				vRecord.append(ProductNumberDef.justifyJapaneseVIN(objArr[3].toString(), JapanVINLeftJustified.booleanValue()));
				vRecord.append(objArr[4].toString());
				Timestamp ts = (Timestamp) objArr[5];
				vRecord.append(stsf1.format(ts));
				vRecord.append(objArr[6].toString());
				vRecord.append("                    ");		//Band-No.(20)
				vRecord.append(objArr[7].toString());		//KD_Lot_No. (18)
				vRecord.append("                  ");		//Part-No.(18)
				vRecord.append("           ");				//Part-Clr-No. (11)
				vRecord.append("                                                ");	//Filler (48)
				vRecord.append(String.format("%05d", recordCount));	//On-Seq-No. (5)
				result.add(vRecord.toString());
				vRecord.delete(0,vRecord.length());
			}
		}
	}
}