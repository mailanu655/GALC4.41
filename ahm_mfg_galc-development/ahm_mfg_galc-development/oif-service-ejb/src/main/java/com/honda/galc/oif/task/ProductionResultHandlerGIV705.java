package com.honda.galc.oif.task;

/**
 * 
 * <h3>ProductionResultHandlerGIV705</h3>
 * <p> ProductionResultHandlerGIV705 is for GIV705 </p>
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
 * June 20, 2014
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.ProductionResultGIV705DTO;
import com.honda.galc.service.ProductionResultService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.StringUtil;
import com.honda.galc.data.ProductNumberDef; 

public class ProductionResultHandlerGIV705 extends OifTask<ProductionResultGIV705DTO> implements
		IEventTaskExecutable {

	private static String serviceId = null;
	private boolean isDebug = false;
	//private OifErrorsCollector errorsCollector;
	
	/** Production progress constants*/
	private static final Character			CANCEL_FLAG_OK       		= 'Y'; //cancel data
	private static final Character			CANCEL_FLAG_NOK      		= 'N'; //normal data
	private static final Character			RESULT_FLAG_NOK_SCRAP  		= '2'; //2 = Remake, indicate the cancel code reason
	private static final Character			RESULT_FLAG_NOK_EXCEPTIONAL	= '1'; //1 = Cut, indicate the cancel code reason
	private static final CharSequence		EXCEPTIONAL_OUT_COMMENT		= "EXCEPTIONAL";
	
	//local constants
	private static final String	RESULT_FLAG_OK       	= 	"RESULT_FLAG";
	private static final String AM_PROCESS_POINT		=	"AM_OFF";
	private static final String AM_TRACKING_STATUS		=	"AM_TRAKING_STATUS";
	private static final String LAST_EXECUTION_DATE		=	"LAST_GIV705_EXECUTION";
	private static final String IS_TRANSMISSION_PLANT	=	"IS_TRANSMISSION_PLANT";
	private static final String TRANSMISSION_DEPARTMENT	=	"AM";

	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);
	
	/**
	 * The timestamp to indicate if its date falls in a working production day.
	 * This is mainly used to determine if the interface file needs to be sent
	 * on Saturday. If the Saturday is a production day, the file will be sent.
	 * Otherwise, it will not be sent to MQ.
	 */
	private java.sql.Timestamp productionStartTimestamp;
	private java.sql.Timestamp productionEndTimestamp;
	private Boolean useProdDate = false;

	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";

	public ProductionResultHandlerGIV705(String componentId) {
		super(componentId);
		serviceId = componentId;
	}

	public void execute(Object[] args) {
		try {
			logger.info( "Starting Production Restult interface");
			refreshProperties();
			//Validate if the production result is for Transmission.
			//If not execute the normal process for Frame and Engine plant.
			Boolean isTransmissionPlant	=	getPropertyBoolean( IS_TRANSMISSION_PLANT, false );
			if (!isTransmissionPlant )
			{
				initialize();
				exportRecord();
			}
			else
			{
				processProductionResultAM();
			}
		}
		catch ( TaskException tx )
		{
			logger.error( "Error in the Production Result Task" );
			errorsCollector.error( "Error in the Production Result Task" + tx.getCause() );
		}
		finally
		{
			if (!errorsCollector.getErrorList().isEmpty())
			{
				errorsCollector.sendEmail();
			}
		}
	}

	protected void initialize() {
		logger.info("initializing ProductionResultHandlerGIV705Task.");
		isDebug = getPropertyBoolean("DEBUG_PRODUCTION_RESULT", true);
		String strProdDate = PropertyService.getProperty(componentId, "CUSTOM_PRODUCTION_DATE");
		String strEndProdDate = PropertyService.getProperty(componentId, "CUSTOM_END_TIMESTAMP");
		useProdDate = PropertyService.getPropertyBoolean(componentId, "USE_CUSTOM_PRODUCTION_DATE", false);

		if (isDebug) {
			logger.info("RES is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.SEND));
			logger.info("GIV705_MQ_INTERFACE_ID is set to "
					+ getProperty("GIV705_MQ_INTERFACE_ID"));
			logger.info("CUSTOM_PRODUCTION_DATE is set to " + strProdDate);
			logger.info("USE_CUSTOM_PRODUCTION_DATE is set to " + useProdDate);
			logger.info("ACTIVE_LINES is set to " + getProperty("ACTIVE_LINES"));
			logger.info("ACTIVE_LINES_URLS is set to "
					+ getProperty("ACTIVE_LINES_URLS"));

			logger.info("MQ_CONFIG is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.MQ_CONFIG));
			logger.info("WE_OFF_RES_CNT_PPID is set to "
					+ getProperty("WE_OFF_RES_CNT_PPID"));
			logger.info("PA_OFF_RES_CNT_PPID is set to "
					+ getProperty("PA_OFF_RES_CNT_PPID"));
			logger.info("AF_OFF_RES_CNT_PPID is set to "
					+ getProperty("AF_OFF_RES_CNT_PPID"));
		}
		errorsCollector = new OifErrorsCollector(serviceId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		// If custom production date is present, which means it is an occasion
		// where data
		// is not to be generated with current date, but with a past date which
		// should be done
		// manually by putting in the property with a past date in yyyy-MM-dd
		// format.
		// This property must be removed after the file has been generated so
		// that the scheduled
		// task can continue as normal.
		if (useProdDate && strProdDate != null) {
			try {
				java.util.Date customStartProdDate = sdf.parse(strProdDate);
				productionStartTimestamp = new java.sql.Timestamp(
						customStartProdDate.getTime());
				//get custom end timestamp
				java.util.Date customEndProdDate = sdf.parse(strEndProdDate);
				productionEndTimestamp = new java.sql.Timestamp(
						customEndProdDate.getTime());
			} catch (ParseException e) {
				logger.info("Cannot parse custom production date value, defaulting to current date.");
				productionStartTimestamp = getLastProcessTimestamp();
				productionEndTimestamp = new java.sql.Timestamp(
						(new java.util.Date()).getTime());
			}
		} else {
			//If no custom date range setup, startTime = last_run_time and endTimestamp= current time
			productionStartTimestamp = getLastProcessTimestamp();
			productionEndTimestamp = new java.sql.Timestamp(
					(new java.util.Date()).getTime());
		}
	}

	private void exportRecord() {

		String mqInterfaceID = getProperty("GIV705_MQ_INTERFACE_ID");
		String productionTimestampStartStr = String.valueOf(productionStartTimestamp.getTime());
		String productionTimestampEndStr = String.valueOf(productionEndTimestamp.getTime());
		String strTimestampPart = new SimpleDateFormat("yyyyMMddHHmmss").format(productionStartTimestamp);
		String exportFileName = mqInterfaceID + strTimestampPart + ".oif";
		String resultPath = PropertyService.getProperty(
				OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		// call the web services on the active lines and get production progress
		// data from each line
		String[] activeLinesURLsArr = getProperty("ACTIVE_LINES_URLS").split(",");
		String[] activeLinesArr = getProperty("ACTIVE_LINES").split(",");
		String activeLineURL = null;
		List<String> errorMesages = null;
		int recordCount = 0;
		try {
			BufferedWriter bufWrt = new BufferedWriter(new FileWriter(new File(
					resultPath + exportFileName)));
			
			for (int i = 0; i < activeLinesArr.length; i++) {
				activeLineURL = activeLinesURLsArr[i];
				ProductionResultService prodResultService = HttpServiceProvider
						.getService(activeLineURL + HTTP_SERVICE_URL_PART,
								ProductionResultService.class);

				Map<String, List<String>> finalReturnData = prodResultService
						.getProductionResults(productionTimestampStartStr,productionTimestampEndStr, serviceId, JapanVINLeftJustified.booleanValue());

				errorMesages = finalReturnData.get("GIV705_ERRORS");

				// write the records to a file in the file system
				if (errorMesages == null) {
					List<String> resultList = finalReturnData.get("GIV705");
					if (resultList != null && resultList.size() > 0) {
						for (String singleRec : resultList) {
							recordCount++;
							bufWrt.write(singleRec);
							bufWrt.newLine();
						}
					}
				} else {
					throw new Exception();
				}
			}

			// if there are are no records on a production day then create a no
			// records file to send.
			if (recordCount == 0) {
				logger.info("Creating an export file that contains NoRecords message for interface ID = "
						+ mqInterfaceID + "; file name = " + exportFileName);
				bufWrt.write(MQUtility.createNoDataString(170));
				bufWrt.newLine();
			}
			bufWrt.flush();
			bufWrt.close();

			Boolean sendFileToGPCS = PropertyService.getPropertyBoolean(
					componentId, "SEND_FILE_TO_GPCS", true);
			if (sendFileToGPCS) {
				// Sending file to MQ
					MQUtility mqu = new MQUtility(this);
					logger.info("Calling executeMQSendAPI with send file name  = "
							+ resultPath
							+ exportFileName
							+ " with Interface ID = " + mqInterfaceID);
					String mqConfig = PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.MQ_CONFIG);
					mqu.executeMQSendAPI(mqInterfaceID, mqConfig, resultPath
							+ exportFileName);
					setOutgoingJobStatus(recordCount, exportFileName);
			} else {
				logger.info("Flag SEND_FILE_TO_GPCS is set to false, file was not sent = "
						+ resultPath
						+ exportFileName
						+ " with Interface ID = "
						+ mqInterfaceID);
			}
			if(!useProdDate)
				updateLastProcessTimestamp(productionEndTimestamp);

		} catch (IOException ioe) {
			String errorStr = "OIException raised,URL: " + activeLineURL
					+ " MQ interface " + mqInterfaceID + ". Exception : "
					+ ioe.getMessage();
			logger.error(errorStr);
			errorsCollector.emergency(errorStr);
			setOutgoingJobStatusAndFailedCount(recordCount, OifRunStatus.MQ_ERROR, exportFileName);
		} catch (MQUtilityException mqe) {
			String errorStr = "MQUtilityException raised when sending interface file for "
					+ resultPath
					+ exportFileName
					+ " for the MQ interface "
					+ mqInterfaceID
					+ " URL: "
					+ activeLineURL
					+ "  Exception : " + mqe.getMessage();
			logger.error(errorStr);
			errorsCollector.emergency(errorStr);
			setOutgoingJobStatusAndFailedCount(recordCount, OifRunStatus.MQ_ERROR, exportFileName);
		} catch (Exception e) {
			String errorStr = "Exception raised when sending interface file for "
					+ resultPath
					+ exportFileName
					+ " for the MQ interface "
					+ mqInterfaceID
					+ " URL: "
					+ activeLineURL
					+ "  Exception : " + e.getMessage();
			if (errorMesages != null) {
				for (String string : errorMesages) {
					errorStr += System.getProperty("line.separator") + string;
				}
			}
			logger.error(errorStr);
			errorsCollector.emergency(errorStr);
			setOutgoingJobStatusAndFailedCount(recordCount, OifRunStatus.MQ_ERROR_WHILE_TRYING_TO_POST_MESSAGE, exportFileName);
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	/**
	 * Implementation to get the production progress for transmission
	 */
	public void processProductionResultAM () throws TaskException
	{
		try {
			logger.info( "Starting the Production Result for Transmission" );
			final String[]	activeLineUrl 		=	getProperty( OIFConstants.ACTIVE_LINES ).split(",");
			final String 	amOffProcessPoint	=	getProperty( AM_PROCESS_POINT );
			//gets the tracking status line for Scrap and Exceptional
			final String	trackingStatus		=	getProperty( AM_TRACKING_STATUS );
			//get the date time for the last execution
			final String	startDate			=	getProperty( LAST_EXECUTION_DATE );
			//the current date is the last date for the range to get the data from DB
			final String	endDate				=	new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date () );
			final int		recordSize			=	getPropertyInt( OIFConstants.MESSAGE_LINE_LENGTH );
			
			List<ProductionResultGIV705DTO> result = new ArrayList<ProductionResultGIV705DTO>();
			
			for (String activeLine : activeLineUrl)
			{
				logger.info( "Process the line " + activeLine );
				//get the production result service 
				ProductionResultService productionResultService = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ProductionResultService.class);
				
				logger.info( "Get the Production Result... " );
				String resultFlagValue	=  getProperty( RESULT_FLAG_OK ) ;
				//get the production progress for AM-OFF
				List<Object []> missionProductionResult = productionResultService.getProductionResult( amOffProcessPoint, startDate, endDate);
				for (Object[] missionResult : missionProductionResult) 
				{
					ProductionResultGIV705DTO productionResult = new ProductionResultGIV705DTO ();
					productionResult.setPlanCode				( missionResult [0].toString() );
					productionResult.setLineNumber				( missionResult [1].toString() );
					productionResult.setInHouseProcessLocation	( missionResult [2].toString() );
					productionResult.setVinNumber				( StringUtil.padRight(missionResult [3].toString(),ProductNumberDef.VIN.getLength(),' ', true) );
					productionResult.setProductionSequenceNumber( missionResult [4].toString() );
					productionResult.setAlcActualTimestamp		( missionResult [5].toString() );
					productionResult.setProductSpecCode			( missionResult [6].toString().substring(0, 22) );
					productionResult.setBandNumber				( "" );
					productionResult.setKdLotNumber				( missionResult [7].toString() );
					productionResult.setPartNumber				( missionResult [9] == null ? "" : missionResult [9].toString() );
					productionResult.setPartColorCode			( missionResult [10]== null ? "" : missionResult [10].toString());
					productionResult.setBosSerialNumber			( missionResult [8].toString() );
					productionResult.setFiller					( "" );
					productionResult.setCancelReasonCode		( ' ');//there are not a cancel reason, only apply for scrap
					productionResult.setResultFlag				( resultFlagValue == null ? '0' : resultFlagValue.charAt( 0 ) );
					productionResult.setCancelFlag				( CANCEL_FLAG_NOK );
					//add the result to the list
					result.add( productionResult );
				}
				
				logger.info( "Get the Production Result Scrap... " );
				//get the production progress for asembly mission scrap
				List<Object []> scrapMissionProductionResult = productionResultService.getProductionResult( TRANSMISSION_DEPARTMENT
																									, amOffProcessPoint
																									, startDate
																									, endDate
																									, trackingStatus );
				for (Object[] scrapMissionResult : scrapMissionProductionResult)
				{
					ProductionResultGIV705DTO productionResult = new ProductionResultGIV705DTO ();
					productionResult.setPlanCode					( scrapMissionResult [0].toString() );
					productionResult.setLineNumber					( scrapMissionResult [1].toString() );
					productionResult.setInHouseProcessLocation		( scrapMissionResult [2].toString() );
					productionResult.setVinNumber					( scrapMissionResult [3].toString() );
					productionResult.setProductionSequenceNumber	( scrapMissionResult [4].toString() );
					productionResult.setAlcActualTimestamp			( scrapMissionResult [5].toString() );
					productionResult.setProductSpecCode				( scrapMissionResult [6].toString().substring(0, 22) );
					productionResult.setBandNumber					( "" );
					productionResult.setKdLotNumber					( scrapMissionResult [7].toString() );
					productionResult.setPartNumber					( scrapMissionResult [10] == null ? "" : scrapMissionResult [10].toString()); 
					productionResult.setPartColorCode				( scrapMissionResult [11] == null ? "" : scrapMissionResult [11].toString());
					productionResult.setBosSerialNumber				( scrapMissionResult [8].toString() );
					productionResult.setFiller						( "" );
					//validation of the scrap comment to get the correct cancel reason code
					//Production control define that one product is sent to Exceptional the cancel reason code is 1 =  Cut
					//if the product is sent to Scrap the cancel reason code is 2 = Re make 
					if ( scrapMissionResult [9].toString().contains( EXCEPTIONAL_OUT_COMMENT ))
					{
						productionResult.setCancelReasonCode		( RESULT_FLAG_NOK_EXCEPTIONAL );//there are not a cancel reason, only apply for scrap
					}
					else
					{
						productionResult.setCancelReasonCode		( RESULT_FLAG_NOK_SCRAP );
					}
					productionResult.setResultFlag				( resultFlagValue == null ? '0' : resultFlagValue.charAt( 0 ) );
					productionResult.setCancelFlag				( CANCEL_FLAG_OK );
					//add the result to the list
					result.add( productionResult );
				}
				
				//Send to MQ
				logger.info( "Sending to MQ... " );
				final String path				=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT );
				final String mqConfig			=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG );
				final String layoutComponentId	=	getProperty					( OIFConstants.PARSE_LINE_DEFS );
				final String fileName			=	new StringBuilder( getProperty( OIFConstants.INTERFACE_ID ) )
													.append("_")
													.append( OIFConstants.stsf1.format( new Date() ))
													.toString();
				if ( result != null && result.size() > 0 )
				{
					this.exportDataByOutputFormatHelper( ProductionResultGIV705DTO.class, result, path, fileName, mqConfig, layoutComponentId);
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
				
				logger.info( "Updating the last execution date " + LAST_EXECUTION_DATE + " whit date " + endDate );
				//Update the date with the current date of last execution.
				ComponentPropertyDao propertyDao = ServiceFactory.getDao ( ComponentPropertyDao.class );
				ComponentProperty lastDateProperty = new ComponentProperty ( getName(), LAST_EXECUTION_DATE, endDate );
				propertyDao.save(lastDateProperty);
				
				logger.info( "Finish Production Result Task for Transmission." );
			}
		}
		catch ( PersistenceException px )
		{
			logger.error( "Error in the Transmission Production Progress Interface: " + px.getMessage() );
			errorsCollector.error( "Error in the Transmission Production Progress Interface: " + px.getMessage() );
			throw new TaskException( "Error in the Transmission Production Progress Interface", px.getCause() );
		}
	}
}