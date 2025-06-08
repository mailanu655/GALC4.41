package com.honda.galc.oif.task.ah;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.persistence.PersistenceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.oif.dto.ShippingTransactionDTO;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.DailyDepartmentScheduleUtil;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.RegularMQClient;
import com.ibm.mq.MQException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.util.DailyDepartmentScheduleUtil;

/**
 * <b>ShippingTransaction50ATask</b> 
 * This class sends the interface file 50A - PMCGAL#AHLGAS#ADC010,HCLGAL#AHLGAS#ADC010 to YMS, loads records from the GALC tables
 * 																											 GALADM.Gal143tbx (Frame),
 *                                                                                                           GALADM.Gal144tbx (FrameSpec),
 *                                                                                                           GALADM.Gal148tbx (ShippingTransaction),
 *                                                                                                           GALADM.Gal158tbx (FrameMTOCMasterSpec),
 *                                                                                                           GALADM.Gal185tbx (InstalledPart),
 *                                                                                                           GALADM.Gal217tbx (ProductionLot),
 *                                                                                                           GALADM.Gal263tbx (ShippingStatus),
 *                                                                                                           GALADM.Gal268tbx (FrameMTOCPriceMasterSpec).
 * Shipment Status: 0 - Ready to
 *                  1 - GALC send to AHM
 *                  2 - AHM Receive from GALC
 *                  3 - IDC Release
 *                  4 - AHM  Shipment Confirm
 *                 -1 - Factory Return
 * 
 * Business Name:    Shipped to AH (50A).
 * Business Purpose: Unit then can be received and assigned to a dealer by AHM AS400.  Unit becomes AHM "care & custody".
 * Interface ID:     HCLGAL#AHLGAS#ADC010,PMCGAL#AHLGAS#ADC010
 * Information Flow: From GALC to YMS
 * Timing:           Real-time Communication (Check for new messages to transmit every 30 seconds)
 * Description:      Send 50A transaction(VIN,MTOC, Engine S/N, key#, etc) every each shipped VIN.
 * Impact:           Vehicle cannot be shipped.
 * Countermeasure:   GALC prints a card called Vehicle ID card (MAG Card) instead of 50A transaction. AHM associate scans this card using 
 *                   their terminal to receive the unit and generate a 60A.
 * 
 * @author Anuar Vasquez Gomez
 * 
 * @version 1.0
 */
public class ShippingTransactionTask extends OifTask<Object> implements IEventTaskExecutable {
	
	//constants with the ASCII encoding values
	private final static int CHARACTER_SET	=	819;
	private final static int ENCODING		=	273;
	
	private final static String CCC_PART			= "CCC_PART_NAME";
	private final static String SEND_LOCATION		= "SEND_LOCATION";
	private final static String PART_INSTALLED		= "PART_INSTALLED";
	private final static String ADC_PROCESS_CODE	= "ADC_PROCESS_CODE";
	private final static String TRAN_TYPE			= "TRAN_TYPE";
	private final static String ADC_50B_PROCESS		= "50B";
	private final static String VQ_SHIP_PROCESS_POINT_ID = "VQ_SHIP_PROCESS_POINT_ID";
	private final static String KEY_NO_PART			= "KEY_NO_PART_NAME";
	private final static String LOOKUP_VEHICLE_PRICE= "LOOKUP_VEHICLE_PRICE";
	private final static String LINE_BY_PROCESSPOINT = "LINE_BY_PROCESSPOINT";
	private final static String SEND_LOCATION_BY_PROCESSPOINT = "SEND_LOCATION_BY_PROCESSPOINT";
	private final static String PROCESSPOINT_LIST ="50A_PROCESSPOINT_LIST"; 
	private final static String MODELCODE_LIST ="MODELCODE_LIST";
	
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);
	
	public ShippingTransactionTask ( final String name )
	{
		super ( name );
	}

	public void execute(Object[] args)
	{
		logger.info( "Starting shipping to AH" );
		try {
			processingVin();
		} catch ( TaskException e )
		{
			logger.error( "Error while executing the Shipping Transaction Task" );
			errorsCollector.error( "Error while executing the Shipping Transaction Task" );
		} catch ( Exception ex )
		{
			ex.printStackTrace();
			logger.error( "Error processing Shipping Transaction" );
			errorsCollector.error( ex.getCause(), "Error processing Shipping Transaction" );
		} finally 
		{
			errorsCollector.sendEmail();
		}
 		
	}
	
	private void processingVin() throws TaskException
	{
		//get the property name for CCCLabels part
		final String cccPartName		=	getProperty( CCC_PART );
		final Integer recordSize		=	getPropertyInt( OIFConstants.MESSAGE_LINE_LENGTH );
		final String sendLocation		=	getProperty( SEND_LOCATION );
		final String partInstalled		=	getProperty( PART_INSTALLED ) == null || getProperty( PART_INSTALLED ) == "" ? " " : getProperty( PART_INSTALLED );
		final String adc50AProccessCode	=	getProperty( ADC_PROCESS_CODE );
		final String tranType			=	getProperty( TRAN_TYPE );
		final String vqShipProcessPointId = getProperty(VQ_SHIP_PROCESS_POINT_ID);
		final Map<String,String> lineIdByShipProcessPointId = PropertyService.getPropertyMap(this.componentId, LINE_BY_PROCESSPOINT);
		final Map<String,String> sendLocationByShipProcessPointId = PropertyService.getPropertyMap(this.componentId, SEND_LOCATION_BY_PROCESSPOINT);
		final List<String> keyNoPartName = getPropertyWrappedInList(KEY_NO_PART);
		
		//MQ client
		RegularMQClient mqClient = null;
		
		try {
			//get Shipping transaction (gal148tbx) DAO instance
			ShippingTransactionDao	shippingTransactionDao 	= ServiceFactory.getDao( ShippingTransactionDao.class	);
			//get Shipping status (gal263tbx) DAO instance 
			ShippingStatusDao		shippingStatusDao		= ServiceFactory.getDao( ShippingStatusDao.class 		);
			//get Installed Part (gal185tbx) DAO instance 
			InstalledPartDao		installedPartDao		= ServiceFactory.getDao( InstalledPartDao.class 		);
			ProductResultDao		pHistDao		= ServiceFactory.getDao( ProductResultDao.class 		);
			
			//Define parameters values
			final Integer status 		= 0;
			final Integer effectiveDate = getEffectivePriceDate(vqShipProcessPointId);
			final Character sendFlag 	= 'Y';
			
			logger.info( "Initialize OutputFormaterHelper for Shipping Transaction Task" );
			//Initialize the output format helper
			final String layoutDefinition = getProperty( OIFConstants.PARSE_LINE_DEFS );
			final OutputFormatHelper<ShippingTransactionDTO> outputFormatHelper = new OutputFormatHelper<ShippingTransactionDTO>( layoutDefinition, this.logger, this.errorsCollector );
			outputFormatHelper.initialize(ShippingTransactionDTO.class);
			//initialize the array with empty characters
			final char[] lenghtArray = new char[recordSize]; 
			Arrays.fill( lenghtArray, ' ');
			
			//Initialize the MQ Client
			mqClient = getRegularMQClient();
			
			//get the vins to process
			List<ShippingTransaction> vins = null;
			String operationName = ServiceFactory.getDao(MCOperationRevisionDao.class).findOperationNameByCommonName(cccPartName);
			vins = shippingTransactionDao.get50ATransactionVin( status, effectiveDate, sendFlag, operationName );
			
			final List<String> modelExclusionList = PropertyService.getPropertyList(this.componentId, MODELCODE_LIST);
			for (ShippingTransaction shippingTransaction : vins)
			{
				String salesModelCode = shippingTransaction.getSalesModelCode();
				if(!modelExclusionList.isEmpty() && modelExclusionList.contains(salesModelCode)) {
					logger.info("VIN: " + shippingTransaction.getId() + ", Sales Model Code = " + salesModelCode + ", is exclusion model code");
					continue;
				}
				ShippingTransactionDTO transactionDTO = new ShippingTransactionDTO();
				String productId = shippingTransaction.getVin();
				String afOffProcessPt = getProperty("AF_OFF_PROCESS_POINT", "");
				
				logger.info ( "VIN: " + shippingTransaction.getId() + " = " + shippingTransaction.getPriceString() );
				if ( StringUtils.isBlank(shippingTransaction.getPriceString()))
				{
					if(getPropertyBoolean(LOOKUP_VEHICLE_PRICE, true)) {
						errorsCollector.error( "Error in the VIN: " +  shippingTransaction.getId() + " doesn't have PRICE, it is not possible to send.");
						setOutgoingJobStatusAndFailedCount(1,OifRunStatus.VIN_DOES_NOT_HAVE_PRICE,null);
						continue;
					} else {
						shippingTransaction.setPriceString(String.format("%09d", 0));
					}
				}

				if (StringUtils.isBlank(shippingTransaction.getEngineNumber())) {
					//NALC-1423: For EV Vehicle no engine number present, so setting engine number to six zeros
                    shippingTransaction.setEngineNumber(String.format("%06d", 0));
         		}

				String afOffDate = shippingTransaction.getAfOffDate();
				if (StringUtils.isEmpty(afOffDate))  {
					Timestamp afOffTs = pHistDao.getMaxActualTs(productId, afOffProcessPt);
					if(afOffTs != null)  {
						afOffDate = new SimpleDateFormat("yyMMdd").format(afOffTs);
						shippingTransaction.setAfOffDate(afOffDate);
					}
				}
				if (StringUtils.isBlank(afOffDate)) {
					String msg = String.format("Error : Shipping Transaction VIN:%s, has missing AF OFF Date and it will not be processed.", shippingTransaction.getId());
					logger.error(msg);
					errorsCollector.error(msg);
					setOutgoingJobStatusAndFailedCount(1,OifRunStatus.MISSING_AF_OFF_DATE,null);
					continue;
				}
				String keyNo = shippingTransaction.getKeyNumber();				
				if (keyNoPartName != null && !keyNoPartName.isEmpty()) {
					List<InstalledPart> installedPart = installedPartDao.findAllByProductIdAndPartNames(shippingTransaction.getId(), keyNoPartName);
					if (installedPart != null && !installedPart.isEmpty()) {
						String keyVal = installedPart.get(0).getPartSerialNumber();
						if(!StringUtils.isEmpty(keyVal))  {
							keyNo = StringUtils.leftPad(keyVal, 7, '0');
							shippingTransaction.setKeyNumber(keyNo);
						}
					}
				}
				if (StringUtils.isBlank(shippingTransaction.getKeyNumber())) {
					String msg = String.format("Error : Shipping Transaction VIN:%s, has missing key number and it will not be processed.", shippingTransaction.getId());
					logger.error(msg);
					errorsCollector.error(msg);
					setOutgoingJobStatusAndFailedCount(1,OifRunStatus.MISSING_KEY_NUMBER,null);
					continue;
				}
				
				shippingTransaction.setAdcProcessCode	(	adc50AProccessCode	);
				shippingTransaction.setTranType			(	tranType		);
				shippingTransaction.setSendLocation(getSendLocation(shippingTransaction));
				
				List<String> processPointList = PropertyService.getPropertyList(this.componentId, PROCESSPOINT_LIST);
				if(processPointList != null && processPointList.size() > 0) {
					List<ProductResult> productResultList = ServiceFactory.getDao(ProductResultDao.class).findHistoryByProcessPointList(productId, processPointList);
					String shipProcessPoint = productResultList.size()>0?productResultList.get(0).getProcessPointId():"";
				
					if(lineIdByShipProcessPointId != null && lineIdByShipProcessPointId.size() > 0) {
						String lineId= lineIdByShipProcessPointId.get(shipProcessPoint);
						if(StringUtils.isNotBlank(lineId))shippingTransaction.setLineNumber(lineId);
					}else {
						logger.info("LINE_BY_PROCESSPOINT property not Populated");
					}
					if(sendLocationByShipProcessPointId != null && sendLocationByShipProcessPointId.size()>0 ) {
						String sLocation = sendLocationByShipProcessPointId.get(shipProcessPoint);
						if(StringUtils.isNotBlank(sLocation))shippingTransaction.setSendLocation(sLocation);
					}else {
						logger.info("SEND_LOCATION_BY_PROCESSPOINT property not Populated");
					}
				}
				
				
				
				//copy entity properties to DTO
				BeanUtils.copyProperties( shippingTransaction,transactionDTO );
				//format japan vin, right justified
				//the vin should send in this way VIN(japanese number) = JAC-0123456 use '      JAC-0123456'.
				transactionDTO.setVin(  ProductNumberDef.justifyJapaneseVIN(shippingTransaction.getVin(), JapanVINLeftJustified.booleanValue() ) );
				transactionDTO.setPartInstalled( partInstalled );
				transactionDTO.setPurchaseContractNumber(getPurchaseContractNumber(shippingTransaction));
				
				
				//Validate if a 50B transacction (Only PMC)
				if ( ADC_50B_PROCESS.equals( adc50AProccessCode ) )
				{
					//put the FIF codes and fillers into the transactionDTO.
					String fifCode	=	this.getFIFCodeBySpecCode( shippingTransaction.getVin() );
					if ( fifCode == null )
					{
						errorsCollector.error( "Error: Unable to get the FIF CODE to the VIN = " + shippingTransaction.getVin() + ", check the SALES ORDER FIF CODES Data" );
					}
					else  {
						transactionDTO.setFifCode( fifCode );
					}
				}
				//parse the DTO
				String message = outputFormatHelper.formatOutput( transactionDTO, lenghtArray );
				logger.info( "Sending the 50A message = " + message );
				
				try {
					shippingTransaction.setSendFlag		( 'Y' );
					//Retrieve the shipping status for the VIN
					ShippingStatus shippingStatus = shippingStatusDao.findByKey( shippingTransaction.getVin() );
					//Change the status value 0 to 1 (already sent to AH)
					shippingStatus.setStatus( ShippingStatusEnum.S50A.getStatus() );
					//Update the shipping status
					shippingStatusDao.update( shippingStatus );
					//send message to YMS
					int attemptCounter = 0;
					while(true) {
					    try {
					    	mqClient.putMessage( message, CHARACTER_SET, ENCODING );
					    	setSuccessCount(1);
					    	break;
					    } catch (MQException mqx) {
					    	TimeUnit.SECONDS.sleep(3);
					        if (++attemptCounter == 3) throw mqx;
					    }
					}
					
				}catch ( MQException mqx )
				{
					logger.error( "Error with MQ " + mqx.getMessage() );
					errorsCollector.error( mqx.getCause(), "Error while sendind the message to MQ " );
					//if there is a error to send the message change the send flag to status = N
					shippingTransaction.setSendFlag		( 'N' );
				} catch ( IOException iox )
				{
					logger.error("Error while sending the Shipping transaccion message " + iox.getMessage() );
					errorsCollector.error( iox.getCause(), "Error while sending the Shipping transaccion message " );
					//if there is a error to send the message change the send flag to status = N
					shippingTransaction.setSendFlag		( 'N' );
				} catch ( PersistenceException prx)
				{
					logger.error("Error while updating Shipping Transaction status " + prx.getMessage() );
					errorsCollector.error( prx.getCause(), "Error while updating Shipping Transaction status " );
					//if there is a error to send the message change the send flag to status = N
					shippingTransaction.setSendFlag		( 'N' );
				}
				
				//the printed flag always is N, unknown broadcast service to print.
				shippingTransaction.setPrintedFlag	( 'N' );
				
				//save the shipping transaction (gal148tbx)
				ShippingTransaction newTransaction = new ShippingTransaction();
				//create a new instance to avoid the open jpa issue, when the entity is created
				//from the native query and try to save it, jpa looking for this entity in the cache
				//when it is not found, query the DB to get the entity and make the merge, then the 
				//exception org.apache.openjpa.persistence.EntityNotFoundException raise when jpa doesn't find the entity in the DB.
				BeanUtils.copyProperties( shippingTransaction, newTransaction );
				shippingTransactionDao.save( newTransaction );
			}
		} catch ( MQException mqx )
		{
			logger.error( "Error with MQ " + mqx.getMessage() );
			errorsCollector.error( mqx.getCause(), "Error while connect with MQ " );
			setJobStatus(OifRunStatus.MQ_ERROR);
			throw new TaskException( mqx.getMessage(), mqx.getCause() );		
		} catch ( Exception ex )
		{
			logger.error( "Error performing shipping transaction " + ex.getMessage() );
			errorsCollector.error( ex.getCause(), "Error performing shipping transaction " );
			throw new TaskException( ex.getMessage(), ex.getCause() );		
		}finally
		{
			try {
				if(mqClient != null)	mqClient.finalize(); // clean up
			} catch (MQException e) {
				logger.error("Exception in closing MQ connection: " + e.getMessage());
				errorsCollector.error( "Exception in closing MQ connection " + e.getMessage() );
				setJobStatus(OifRunStatus.MQ_ERROR_CLOSING_CONNECTION);
				e.printStackTrace();
			}
			mqClient = null;
		}
	}
	
	private Integer getEffectivePriceDate(String processPointId) {
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		DailyDepartmentScheduleUtil util = new DailyDepartmentScheduleUtil(processPoint);
		Date productionDate = util.getProductionDate();
		return Integer.valueOf(new SimpleDateFormat( "yyyyMMdd" ).format(productionDate));
	}
	
	private String getPurchaseContractNumber(ShippingTransaction shippingTransaction) {
		Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(shippingTransaction.getVin());
		return (frame == null || frame.getPurchaseContractNumber() == null)  ? "" : frame.getPurchaseContractNumber();
	}
	
	/**
	 * Method to initialize the Regular MQ client
	 * @return
	 * @throws MQException
	 */
	private RegularMQClient getRegularMQClient() throws MQException 
	{
		
		final String hostName		=	getProperty		( OIFConstants.RMQ_HOST_NAME		);
		final Integer port			=	getPropertyInt	( OIFConstants.RMQ_PORT				);
		final String queueManager	=	getProperty		( OIFConstants.RMQ_QUEUE_MANAGER	);
		final String channel		=	getProperty		( OIFConstants.RMQ_CHANNEL			);
		final String queueName		= 	getProperty		( OIFConstants.RMQ_QUEUE_NAME		);
		final String userMq			=	getProperty		( OIFConstants.RMQ_USER				);
		final String passwordMQ		=	getProperty		( OIFConstants.RMQ_PASSWORD			);
		
		RegularMQClient mqClient = new RegularMQClient(	hostName
														,port
														,queueManager
														,channel
														,queueName
														,userMq
														,passwordMQ
														);
		return mqClient;
	}
	
	/**
	 * Get the FIF code for the product spec code
	 * @param productSpecCode
	 * @return
	 */
	private String getFIFCodeBySpecCode ( final String productId )
	{
		FrameDao		frameDao		=	ServiceFactory.getDao( FrameDao.class		);
		FrameSpecDao	frameSpecDao	=	ServiceFactory.getDao( FrameSpecDao.class	);
		SalesOrderFifDao 	fifDao		=	ServiceFactory.getDao( SalesOrderFifDao.class);
		//get the Product for the productId
		Frame currentVin	=	frameDao.findByKey( productId );
		//get the product spec entity
		FrameSpec frameSpec	=	frameSpecDao.findByKey(currentVin.getProductSpecCode());
		//get the fif codes by product spec
		return fifDao.getFIFCodeByProductSpec( frameSpec );
	}
	
	private List<String> getPropertyWrappedInList(String propertyName) {
		String prop = getProperty(propertyName);
		if (StringUtils.isBlank(prop)) return null;
		return java.util.Collections.singletonList(prop);
	}
	
	protected String getSendLocation(ShippingTransaction shippingTransaction) {
		return getProperty( SEND_LOCATION );
	}
}
