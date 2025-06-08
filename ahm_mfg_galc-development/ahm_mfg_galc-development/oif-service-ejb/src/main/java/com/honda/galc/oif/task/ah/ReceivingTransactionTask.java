package com.honda.galc.oif.task.ah;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.util.OpenJPAException;

import com.honda.galc.common.OIFParsingHelper;
import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.ParkChangeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.oif.ParkChange;
import com.honda.galc.oif.dto.ReceivingTransactionCommonDTO;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.oif.IReceivingTransactionService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.RegularMQClient;
import com.ibm.mq.MQException;

/**
 * American Honda Shipping System / Yard Management System (YMS)
 *     American Honda has a system to receive the units and it becomes Care and Custody and assigned to dealer by AH IDC AS400.
 *     AH Purpose: Estabish the reception, care, custody and assignment the units to the Dealer.
 * 
 * General Comments: 
 *     This Task launch the process to get message over the AH - GALC preset communication channels.
 *                                                                                                         
 *     Gets MQ Message process, classify the type message using the message length and the type transaction data in the message.
 *     If something goes wrong WITH the MQ getting process then send the message to back up queue.
 *     
 * Shipment Status: 0 - Ready to
 *                  1 - GALC send to AHM
 *                  2 - AHM Receive from GALC
 *                  3 - IDC Release
 *                  4 - AHM  Shipment Confirm
 *                 -1 - Factory Return
 * 
 * Business Name:    Receiving from AH (60A, 65A, 70A, 75A or 90A).
 * Business Purpose: Update the process point and status into GALC with the actions applied by AH Shipping System.
 * Interface ID:     None
 * Information Flow: From YMS to GALC
 * Timing:           Real-time Communication (Check for new messages to transmit every 60 seconds)
 * Description:      Receive transaction(60A, 65A, 70A, 75A or 90A) every each shipped was VIN.
 * Impact:           Vehicle cannot be shipped.
 *     
 * @version 1.0
 *
 */
public class ReceivingTransactionTask extends OifTask<Object> implements IEventTaskExecutable {

	//constants with the ASCII encoding values
	private final static int CHARACTER_SET	=	819;
	private final static int ENCODING		=	273;
	
	private final static int RECEIVING_60A_LENGTH	=	75;

	private final static String TRAN_TYPE_DEF = "TRAN_TYPE_DEF";
	
	private final static String TARGET_QUEUES_GET		=	"TARGET_QUEUES_GET";
	private final static String BACKUP_QUEUE_60A		=	"BACKUP_QUEUE_60A";
	private final static String BACKUP_QUEUE_65A_90A	=	"BACKUP_QUEUE_65A_90A";
	
	private final static String INPUT_DATE_FORMAT			=	"yyMMddHHmmss";
	private final static String ACTUAL_TIMESTAMP_FORMAT	=	"yyyy-MM-dd HH:mm:ss";
	
	private final static String PROCESS_POINT_60A			=	"PROCESS_POINT_60A";
	private final static String PROCESS_POINT_70A			=	"PROCESS_POINT_70A";
	private final static String PROCESS_POINT_75A			=	"PROCESS_POINT_75A";
	private final static String PROCESS_POINT_90A			=	"PROCESS_POINT_90A";
	
	private final static String	LAYOUT_60A				=	"PARSE_LINE_DEFS_60A";
	private final static String LAYOUT_65A				=	"PARSE_LINE_DEFS_65A";
	private final static String	LAYOUT_70A				=	"PARSE_LINE_DEFS_70A";
	private final static String LAYOUT_75A			 	=	"PARSE_LINE_DEFS_75A";
	private final static String LAYOUT_90A				=	"PARSE_LINE_DEFS_90A";
	private int totalRecords =0, failedRecords=0;
	
	private RegularMQClient mqClient = null;
	
	public ReceivingTransactionTask(String name) {
		super(name);
	}

	public void execute(Object[] args)
	{
		try 
		{
			refreshProperties();
			processingRecevingTransaction();
		} catch ( TaskException tx)
		{
			logger.error( "Error proccessing the receving transactions" );
		}
		finally
		{
			setIncomingJobCount(totalRecords-failedRecords, failedRecords, null);
			errorsCollector.sendEmail();
		}
	}
	
	private void processingRecevingTransaction () {
		//RegularMQClient mqClient	=	null;
		//List with the messages from the AH shipping system
		List<String> messages60A	=	null;
		List<String> messages65A	=	null;
		List<String> messages70A	=	null;
		List<String> messages75A	=	null;
		List<String> messages90A	=	null;
		//creating the mq manager to the client
		try {
			try 
			{
				mqClient	=	this.getMQClient ();
			} catch ( MQException mqx )
			{
				logger.error( "Error to create the MQ Client... " );
				errorsCollector.error( mqx.getCause(), "Error to create the MQ Client... " );
				throw new TaskException ( "Error to create the MQ Client... ", mqx.getCause() );
			}
			final String backUpQueue60A		=	getProperty( BACKUP_QUEUE_60A );
			final String backUpQueue65to90	=	getProperty( BACKUP_QUEUE_65A_90A );
			final String [] queues = getProperty( TARGET_QUEUES_GET ).split( "," );
			for (String queueTarget : queues) 
			{
				//get the message from the MQ
				Boolean existMessage	=	Boolean.TRUE;
				String message			=	null;
				do {
					
					try 
					{
						message	=	mqClient.getMessage( queueTarget, CHARACTER_SET, ENCODING );
						
					//apply validations
						if ( message.length() == RECEIVING_60A_LENGTH )
						{
							if ( messages60A == null ) {	messages60A	=	new ArrayList<String>();	}
							messages60A.add( message );
							totalRecords++;
						}
						else 
						{
							final String tranType	=	quickParse(message, TRAN_TYPE_DEF, "14,2");
							ShippingStatusEnum typeTransaction	=	ShippingStatusEnum.getShippingStatusByTransactionType( tranType );
							if ( ShippingStatusEnum.S65A.equals( typeTransaction ) )
							{
								if ( messages65A == null ) { messages65A	=	new ArrayList<String>();	}
								messages65A.add( message );
								totalRecords++;
							}
							if ( ShippingStatusEnum.S70A.equals( typeTransaction ) )
							{
								if ( messages70A == null ) { messages70A	=	new ArrayList<String>();	}
								messages70A.add( message );
								totalRecords++;
							}
							if ( ShippingStatusEnum.S75A.equals( typeTransaction ) )
							{
								if ( messages75A == null ) { messages75A	=	new ArrayList<String>();	}
								messages75A.add( message );
								totalRecords++;
							}
							if ( ShippingStatusEnum.S90A.equals( typeTransaction ) )
							{
								if ( messages90A == null ) { messages90A	=	new ArrayList<String>();	}
								messages90A.add( message );
								totalRecords++;
								existMessage	=	Boolean.FALSE;
							}
						}
					}catch ( MQException mqx )
					{
						if(mqx.completionCode == MQException.MQCC_FAILED && mqx.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE) {
							logger.info("Message Queue is empty.");
						} else {
							logger.error ( "Error accesing to the Queue..." );
							errorsCollector.error ( mqx.getCause(), "Error accesing to the Queue..." );
						}
						existMessage	=	Boolean.FALSE;
					}
					catch ( IOException iox )
					{
						logger.error ( "Error reading the message content..." );
						errorsCollector.error (iox.getCause(), "Error reading the message content..." );
						existMessage	=	Boolean.FALSE;
						
					}
				}while ( existMessage );
				
			}
			logger.info( "Processing the messages transactions received" );
			//process the message in order by transaction type
			this.processShippingConfirm60A	(	messages60A, backUpQueue60A		);
			this.processParkChange65A		(	messages65A, backUpQueue65to90	);
			this.processDealerAssign70A		(	messages70A, backUpQueue65to90	);
			this.processShipmentConfirm75A	(	messages75A, backUpQueue65to90	);
			this.processFactoryReturn90A	(	messages90A, backUpQueue65to90	);
			logger.info( "Finish the receiving transaction." );
		
		} finally {
			try {
				if (mqClient != null) {
					mqClient.finalize();					
				}
			} catch (Exception e) {
				logger.error("Exception in closing MQ connection: " + e.getMessage());
				e.printStackTrace();
				errorsCollector.error( "Exception in closing MQ connection " + e.getMessage() );
			}
		}
	}
	
	/**
	 * Parses a single portion of the given message based on a csv property
	 * defining the beginning index and length of the substring.
	 * @param message - the message to be parsed
	 * @param parsePropertyDef - the key for the property which contains a csv value with the beginning index and then the length of the substring
	 * @param defaultParseProperty - the default csv value consisting of the beginning index and then the length of the substring iff parsePropertyDef is not found
	 * @return - a substring of message parsed using the value corresponding to parsePropertyDef's key, or defaultParseProperty iff parsePropertyDef is not found
	 */
	private String quickParse(String message, String parsePropertyDef, String defaultParseProperty) {
		StringTokenizer vRecordDef = new StringTokenizer(getProperty(parsePropertyDef, defaultParseProperty), ",");
		int startingPosition = 0;
		int length = 0;
		try {
			startingPosition = Integer.parseInt(vRecordDef.nextToken());
			length = Integer.parseInt(vRecordDef.nextToken());
		} catch (NumberFormatException nfe) {
			logger.error(nfe, "Can't get data position for " + parsePropertyDef);
			throw nfe;
		}
		return message.substring(startingPosition, startingPosition + length);
	}
	
	/**
	 * Method to process the 60a message
	 */
	private void processShippingConfirm60A( final List<String > messages60A, final String backUpQueue  )
	{
		logger.info( "Processing the 60A transactions" );
		//process the message in order by transaction type
		if ( messages60A != null )
		{
			IReceivingTransactionService receivingTransaction	=	ServiceFactory.getService( IReceivingTransactionService.class );
			for (String data60A : messages60A)
			{
				try
				{
					logger.info( "Processing the 60A message = " + data60A );
					ReceivingTransactionCommonDTO parsedMessage	= this.parseCommonAttriburesFromMessage( LAYOUT_60A , data60A );
					receivingTransaction.shippingConfirm60A( parsedMessage.getProductId(), parsedMessage.getActualTimestamp(),  getProperty( PROCESS_POINT_60A ), ProductType.FRAME );
				}
				catch ( TaskException tx)
				{
					failedRecords++;
					logger.error( "Error processing the message = " + data60A );
					logger.error( tx.getMessage() );
					errorsCollector.error(tx.getCause(), tx.getMessage() );
					try {
						mqClient.putMessage( backUpQueue , data60A, CHARACTER_SET, ENCODING );
					} catch (IOException e) {
						logger.error( "Error sending the message = " + data60A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data60A + "the back up message to the queue= " + backUpQueue );
					} catch (MQException e) {
						logger.error( "Error sending the message = " + data60A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data60A + "the back up message to the queue= " + backUpQueue );
					}
				}
			}
		}
	}
	
	private void processParkChange65A( final List<String> messages65A, final String backUpQueue )
	{
		//the park change transaction only create or update the records in the table GAL163TBX
		//filtering by VIN
		if ( messages65A != null )
		{
			//initialize the parser helper
			OIFParsingHelper<ParkChange> parser65A = new OIFParsingHelper<ParkChange> ( ParkChange.class, getProperty( LAYOUT_65A ), logger );
			parser65A.getParsingInfo();
			ParkChangeDao parkChangeDao	=	ServiceFactory.getDao( ParkChangeDao.class );
			for (String data65A : messages65A)
			{
				try
				{
					logger.info( "Processing the 65A message = " + data65A );
					ParkChange parkChange	= new ParkChange ();
					//parse the message to a Park control entity
					parser65A.parseData( parkChange, data65A );
					//Merge the entity (save or update)
					parkChangeDao.save( parkChange );
				}
				catch (OpenJPAException opjx)
				{
					failedRecords++;
					logger.error ( "Error processing the Park Change Message = " + data65A );
					errorsCollector.error( opjx.getCause(), "Error processing the Park Change Message = " + data65A );
					try
					{
						mqClient.putMessage( backUpQueue, data65A, CHARACTER_SET, ENCODING );
					} catch (IOException e) {
						logger.error( "Error sending the message = " + data65A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data65A + "the back up message to the queue= " + backUpQueue );
					} catch (MQException e) {
						logger.error( "Error sending the message = " + data65A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data65A + "the back up message to the queue= " + backUpQueue );
					}
				}
			}
		}
	}
	
	public void processDealerAssign70A ( final List<String> messages70A, final String backUpQueue )
	{
		if ( messages70A != null )
		{
			//DealerAssigned70AService receivingTransaction = ServiceFactory.getService( DealerAssigned70AService.class );
			IReceivingTransactionService receivingTransaction = ServiceFactory.getService( IReceivingTransactionService.class );
			for (String data70A : messages70A)
			{
				try
				{
					logger.info( "Processing the 70A message = " + data70A );
					ReceivingTransactionCommonDTO parsedMessage	= this.parseCommonAttriburesFromMessage( LAYOUT_70A , data70A );
					receivingTransaction.dealerAssigned70A( parsedMessage.getProductId()
															, parsedMessage.getActualTimestamp()
															, getProperty( PROCESS_POINT_70A )
															, parsedMessage.getMessageDate()
															, parsedMessage.getMessageTime()
															, parsedMessage.getDealerNo()
															, ProductType.FRAME
														   );
				}
				catch ( TaskException tx)
				{
					failedRecords++;
					logger.error( "Error processing the message = " + data70A );
					logger.error( tx.getMessage() );
					errorsCollector.error(tx.getCause(), tx.getMessage() );
					try
					{
						mqClient.putMessage( backUpQueue, data70A, CHARACTER_SET, ENCODING );
					} catch (IOException e) {
						logger.error( "Error sending the message = " + data70A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data70A + "the back up message to the queue= " + backUpQueue );
					} catch (MQException e) {
						logger.error( "Error sending the message = " + data70A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data70A + "the back up message to the queue= " + backUpQueue );
					}
				}
			}
		}
	}
	
	private void processShipmentConfirm75A ( final List<String> messages75A, final String backUpQueue )
	{
		if ( messages75A != null )
		{
			IReceivingTransactionService receivingTransaction = ServiceFactory.getService( IReceivingTransactionService.class );
			for (String data75A : messages75A)
			{
				try
				{
					logger.info( "Processing the 75A message = " + data75A );
					ReceivingTransactionCommonDTO parsedMessage	= this.parseCommonAttriburesFromMessage( LAYOUT_75A , data75A );
					receivingTransaction.shipmentConfirm75A(parsedMessage.getProductId()
															, parsedMessage.getActualTimestamp()
															, getProperty( PROCESS_POINT_75A )
															, parsedMessage.getMessageDate()
															, parsedMessage.getMessageTime()
															, parsedMessage.getDealerNo()
															, ProductType.FRAME
															);
				}
				catch ( TaskException tx)
				{
					failedRecords++;
					logger.error( "Error processing the message = " + data75A );
					logger.error( tx.getMessage() );
					errorsCollector.error(tx.getCause(), tx.getMessage() );
					try
					{
						mqClient.putMessage( backUpQueue, data75A, CHARACTER_SET, ENCODING );
					} catch (IOException e) {
						logger.error( "Error sending the message = " + data75A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data75A + "the back up message to the queue= " + backUpQueue );
					} catch (MQException e) {
						logger.error( "Error sending the message = " + data75A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data75A + "the back up message to the queue= " + backUpQueue );
					}
				}
			}
		}
	}
	
	private void processFactoryReturn90A ( final List<String> messages90A, final String backUpQueue )
	{
		if ( messages90A != null )
		{
			IReceivingTransactionService receivingTransaction	=	ServiceFactory.getService( IReceivingTransactionService.class );
			
			for (String data90A : messages90A)
			{
				try
				{
					logger.info( "Processing the 90A message = " + data90A );
					ReceivingTransactionCommonDTO parsedMessage	= this.parseCommonAttriburesFromMessage( LAYOUT_90A , data90A );
					
					receivingTransaction.factoryReturn90A( parsedMessage.getProductId()
															, parsedMessage.getActualTimestamp()
															, getProperty( PROCESS_POINT_90A )
															, ProductType.FRAME
														);
					boolean updateNaq = PropertyService.getPropertyBoolean(componentId, "FACTORY_RETRUN_NAQ_UPDATE", false);
					String defectName = PropertyService.getProperty(componentId, "FACTORY_RETRUN_NAQ_DEFECT_NAME", "AH RETURN");
					String repairArea = PropertyService.getProperty(componentId,"FACTORY_RETRUN_NAQ_REPAIR_AREA","VQ FACTORY RETURN");
					if(updateNaq) {
						receivingTransaction.createNaqDefectAndParking(parsedMessage.getProductId(), defectName,getProperty( PROCESS_POINT_90A ),repairArea );
					}
				}
				catch ( TaskException tx)
				{
					failedRecords++;
					logger.error( "Error processing the message = " + data90A );
					logger.error( tx.getMessage() );
					errorsCollector.error(tx.getCause(), tx.getMessage() );
					try
					{
						mqClient.putMessage( backUpQueue, data90A, CHARACTER_SET, ENCODING );
					} catch (IOException e) {
						logger.error( "Error sending the message = " + data90A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data90A + "the back up message to the queue= " + backUpQueue );
					} catch (MQException e) {
						logger.error( "Error sending the message = " + data90A + "the back up message to the queue= " + backUpQueue );
						errorsCollector.error( e.getCause(), "Error sending the message = " + data90A + "the back up message to the queue= " + backUpQueue );
					}
				}
			}
		}
	}
	
	/**
	 * Method to get the MQ client instance
	 * @return
	 * @throws MQException
	 */
	private RegularMQClient getMQClient () throws MQException
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
	 * Method to parse the commons attributes from the message for the receiving transactions
	 * @param componentId			-- the current transaction
	 * @param layoutPropertyDef		-- the property value with the parse line def property 
	 * @param message				-- Message to process
	 * @param productId				-- product id to send back
	 * @param actualTimestamp		-- the actual time to send bakc
	 */
	private ReceivingTransactionCommonDTO parseCommonAttriburesFromMessage ( final String layoutPropertyDef, final String message )
	{
		//layout properties
		final String parseCompoment		=	getProperty( layoutPropertyDef );
		//final String [] datePosition	=	PropertyService.getProperty( parseCompoment, "DATE" ).split( "," );
		//final String [] timePosition	=	PropertyService.getProperty( parseCompoment, "TIME" ).split( "," );
		//final String [] vinPosition		=	PropertyService.getProperty( parseCompoment, "VIN" ).split( "," );
		//final String [] framePosition	=	PropertyService.getProperty( parseCompoment, "FRAME" ).split( "," );
		
		final OIFSimpleParsingHelper<ReceivingTransactionCommonDTO> parserHelper =
					new OIFSimpleParsingHelper<ReceivingTransactionCommonDTO>( ReceivingTransactionCommonDTO.class, parseCompoment, logger );
		parserHelper.getParsingInfo();
		ReceivingTransactionCommonDTO receivingTransactionDTO	=	new ReceivingTransactionCommonDTO();
		parserHelper.parseData(receivingTransactionDTO, message);
		
		//get the vin data from the message
		/*int startPosition	=	Integer.valueOf( vinPosition [0] );
		int endPosition		=	Integer.valueOf( vinPosition [1] ) + startPosition;
		final String inputVin	=	message.substring( startPosition, endPosition );*/
		//get the frame data from the message
		/*startPosition	=	Integer.valueOf( framePosition [0] );
		endPosition		=	Integer.valueOf( framePosition [1] ) + startPosition;
		final String inputFrame	=	message.substring( startPosition, endPosition );*/
		//create the product id
		final String productId	=	StringUtils.trim( receivingTransactionDTO.getVin() ) + StringUtils.trim( receivingTransactionDTO.getFrame() );
		receivingTransactionDTO.setProductId( productId );		
		//get the date from the message
		/*startPosition	=	Integer.valueOf( datePosition [0] );
		endPosition		=	Integer.valueOf( datePosition [1] ) + startPosition;
		this.messageDate=	message.substring( startPosition, endPosition );
		
		startPosition	=	Integer.valueOf( timePosition [0] );
		endPosition		=	Integer.valueOf( timePosition [1] ) + startPosition;
		this.messageTime=	message.substring( startPosition, endPosition );*/
		
		//String messageDateTime	= this.messageDate+this.messageTime;
		String messageDateTime	=	receivingTransactionDTO.getMessageDate() + receivingTransactionDTO.getMessageTime();
		String dateTime		=	null;
		try {
			Date inputDate	=	new SimpleDateFormat( INPUT_DATE_FORMAT	).parse( messageDateTime );
			dateTime		=	new SimpleDateFormat( ACTUAL_TIMESTAMP_FORMAT ).format( inputDate );
		} catch (ParseException e) {
			throw new TaskException ( "The input date and time are in wrong format" + messageDateTime, e.getCause() );
		}
		//actualTimestamp	=	Timestamp.valueOf( dateTime );
		receivingTransactionDTO.setActualTimestamp( Timestamp.valueOf( dateTime ) );
		return receivingTransactionDTO;
	}
}