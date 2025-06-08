package com.honda.galc.service.msip.handler.inbound;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.oif.FrameShipConfirmationDao;
import com.honda.galc.dao.oif.ParkChangeDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.InRepairArea;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.oif.FrameShipConfirmation;
import com.honda.galc.entity.oif.FrameShipConfirmationId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.service.msip.dto.inbound.IAdcDto;
import com.honda.galc.service.msip.handler.inbound.BaseMsipInboundHandler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public abstract class ReceivingTransactionMsipHandler<T extends BaseMsipPropertyBean, D extends IAdcDto> extends BaseMsipInboundHandler<T, D> {
	private String errorMessage =  "";
	
	private static final Character SENDED_FLAG			= 'R';
	private static final String FACTORY_RETURN_LABEL	= "FACTORY RETURN";
	private static final String RESPONSIBLE_DEPARTMENT	= "VQ";
	private static final String PARTS_TO_BACKOUT        = "PARTS_TO_BACKOUT";
	private static final String INPUT_DATE_FORMAT			=	"yyMMddHHmmss";
	private static final String ACTUAL_TIMESTAMP_FORMAT	=	"yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Method to process the 60A message.
	 * Make the tracking for the product and update the status and actual time stamp at ShippingStatus
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 */
	@Transactional ( propagation = Propagation.REQUIRED )
	public void shippingConfirm60A( final String productId, final Timestamp actualTimestamp, final String processPointId ) throws TaskException
	{
		getLogger().info( "Starting the process for 60A transaction with productId = " + productId );
		getLogger().info( "Get the PRODUCT_ID = " + productId + " from shipping status from gal263tbx " );
		ShippingStatusDao shippingStatusDao	=	ServiceFactory.getDao( ShippingStatusDao.class );
		ShippingStatus 	shippingStatus		=	shippingStatusDao.findByKey( productId );
		if ( shippingStatus == null )
		{
			throw new TaskException( "Unable to find the VIN record in gal263tbx for the product_id = " + productId );
		}
		ShippingStatusEnum vinCurrentStatus	=	ShippingStatusEnum.getShippingStatusByStatus( shippingStatus.getStatus() );
		if( ShippingStatusEnum.S60A.equals( vinCurrentStatus ) )
		{
			errorMessage = "Product_Id = " + shippingStatus.getId() + "with status = " + shippingStatus.getStatus()
					+ " indicates that AHM receiving has already been procesed. No update applied";
			getLogger().error(errorMessage);
			throw new TaskException (errorMessage);
		}
		else 
		{
			if ( !ShippingStatusEnum.S50A.equals( vinCurrentStatus ) )
			{
				errorMessage = "Product_Id = " + shippingStatus.getId() + "with status = " + shippingStatus.getStatus()
						+ " indicate this VIN need to be process by 50A before";
				getLogger().error(errorMessage);
				throw new TaskException (errorMessage);
			}
		}
		try
		{
			getLogger().info( "Executing the 60A transaction tracking for the product = " + productId );
			TrackingService trackingService	= ServiceFactory.getService( TrackingService.class );
			trackingService.track( ProductType.FRAME, productId, processPointId );
			getLogger().info("Update the VIN status in the Shipping Status - gal263tbx ");
			shippingStatus.setStatus			(	ShippingStatusEnum.S60A.getStatus() );
			shippingStatus.setActualTimestamp	(	actualTimestamp	);
			shippingStatusDao.update			(	shippingStatus	);
		} catch (Exception e)
		{
			getLogger().error( "Error processing the 60A message" + e.getMessage() );
			throw new TaskException ( "Error processing the 60A message", e.getCause() );
		}
	}
	
	/**
	 * Process the Dealer Assign 70A.
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 * @param messageDate
	 * @param messageTime
	 * @throws TaskException
	 */
	@Transactional (propagation = Propagation.REQUIRED)
	public void dealerAssigned70A( final String productId, final Timestamp actualTimestamp, final String processPointId, final String messageDate, final String messageTime, final String dealerNo ) throws TaskException
	{
		getLogger().info( "Starting the process for 70A transaction with with productId = " + productId );
		getLogger().info( "Loading the layouts properties..." );
		
		getLogger().info( "Get the PRODUCT_ID = " + productId + " from shipping status from gal263tbx " );
		ShippingStatusDao shippingStatusDao	=	ServiceFactory.getDao( ShippingStatusDao.class );
		ShippingStatus 	shippingStatus			=	shippingStatusDao.findByKey( productId );

		if ( shippingStatus == null )
		{
			throw new TaskException( "Unable to find the VIN record in gal263tbx for the product_id = " + productId );
		}
		ShippingStatusEnum vinCurrentStatus	=	ShippingStatusEnum.getShippingStatusByStatus( shippingStatus.getStatus() );
		if( vinCurrentStatus.getStatus() >= ShippingStatusEnum.S70A.getStatus() )
		{
			errorMessage = "Product_Id = " + shippingStatus.getId() + "with status = " + shippingStatus.getStatus()
					+ " indicates that AHM IDC Release has already been procesed. No update applied";
			getLogger().error(errorMessage);
			throw new TaskException (errorMessage);
		}
		
		if( vinCurrentStatus.getStatus() == ShippingStatusEnum.INIT.getStatus() )
		{
			errorMessage = "Product_Id = " + shippingStatus.getId() + " with status = " + shippingStatus.getStatus()
					+ " indicates that AHM Shipping Transaction (50A) must be performed before. No update applied";
			getLogger().error(errorMessage);
			throw new TaskException (errorMessage);
		}	
				
		try
		{
			getLogger().info( "Executing the tracking for the product = " + productId );
			//save information in FrameShipConfirmation (AEP_STAT_OUT_TBX) 
			this.processFrameShipConfirmation( productId, processPointId, messageDate, messageTime, ShippingStatusEnum.S70A );
			TrackingService trakingProduct	=	ServiceFactory.getService( TrackingService.class );
			trakingProduct.track(ProductType.FRAME, productId, processPointId);
		
			//update the status into the gal263tbx
			getLogger().info("Update the VIN status in the Shipping Status - gal263tbx ");
			shippingStatus.setStatus	(	ShippingStatusEnum.S70A.getStatus() );
			shippingStatus.setDealerNo	(	dealerNo );
			shippingStatusDao.update	(	shippingStatus	);
			getLogger().info("End process Dealer Assing 70A transaction");
		} catch (Exception e)
		{
			errorMessage = "Error processing the 70A message" + e.getMessage();
			getLogger().error(errorMessage);
			throw new TaskException (errorMessage);
		}
	}
	
	/**
	 * Process the Shipment Confirm 75A transaction
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 * @param messageDate
	 * @param messageTime
	 * @throws TaskException
	 */
	@Transactional (propagation = Propagation.REQUIRED)
	public void shipmentConfirm75A( final String productId, final Timestamp actualTimestamp, final String processPointId, final String messageDate, final String messageTime, final String dealerNo ) throws TaskException
	{
		getLogger().info("Start to process Shipment Confirm 75A");
		/*
		the next block code evaluate status from gal263tbx for current vin. 
		 -If current Vin has status major that the actual interface. It doesn't do nothing 
		 -If current Vin doesn't exist into gal263, interface will continue with the next vin
		*/
		ShippingStatusDao shippingStatusDao = ServiceFactory.getDao( ShippingStatusDao.class );
		ShippingStatus shippingStatus = shippingStatusDao.findByKey( productId );
		
		if ( shippingStatus == null) {
			errorMessage = "Unable to find the VIN record in gal263tbx for the product_id: " + productId;
			getLogger().error(errorMessage);
			throw new TaskException(errorMessage);
		}
		if ( shippingStatus.getStatus() >= ShippingStatusEnum.S75A.getStatus() ) {//4
			errorMessage = "Status = " + shippingStatus.getStatus() + " indicates that AHM Shipment Confirm has already been processed. No update applied.";
			getLogger().error(errorMessage);
			throw new TaskException (errorMessage);			
		} else {
			if (ShippingStatusEnum.INIT.equals( ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus() ))) {
				errorMessage = "Status = " + shippingStatus.getStatus() + " this VIN need to be process by 50A before. ProductId: \"" + productId + "\"";
				getLogger().error(errorMessage);
				throw new TaskException (errorMessage);				
			} 
		}

		try
		{	
			// The next code update table 263, this set status as '4' and
			// Actual_Timestamp as date from current message
			getLogger().info("Step - Executing updating shipping status into Gal263tbx with VIN: " + productId);
			shippingStatus = shippingStatusDao.findByKey(productId);
			shippingStatus.setStatus(ShippingStatusEnum.S75A.getStatus());
			shippingStatus.setDealerNo(dealerNo);
			shippingStatus.setActualTimestamp(actualTimestamp);
			shippingStatusDao.save(shippingStatus);
			
			// Delete 50A records from ShippingTransaction (gal148tbx)
			getLogger().debug("Executig deleting 50A with VIN = " + productId);
			ShippingTransactionDao shippingTransactionDao = ServiceFactory.getDao(ShippingTransactionDao.class);
			shippingTransactionDao.remove(shippingTransactionDao.findByKey(productId));
			
			// Delete 65A records from  (gal163tbx)
			ParkChangeDao parkChangeDao = ServiceFactory.getDao( ParkChangeDao.class );
			parkChangeDao.remove( parkChangeDao.findByKey(productId) );		
			
			//save information in FrameShipConfirmation (AEP_STAT_OUT_TBX) 
			this.processFrameShipConfirmation( productId, processPointId, messageDate, messageTime, ShippingStatusEnum.S75A );
			
			// The next code get from table 176 the next product id of current VIN
			getLogger().info( "Executing the tracking for the product = " + productId );
			TrackingService trakingProduct	=	ServiceFactory.getService( TrackingService.class );
			trakingProduct.track(ProductType.FRAME, productId, processPointId);				
			//Delete InProcessProduct (Gal176tbx) records
			InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
			getLogger().debug("Executig deleting Gal176tbx with VIN = " + productId);				
			inProcessProductDao.remove(inProcessProductDao.findByKey(productId));			
			getLogger().info("End process Shipment Confirm");
		} catch (Exception e)
		{
			getLogger().error( "Error processing the 75A message" + e.getMessage() );
			throw new TaskException ( "Error processing the 75A message", e.getCause() );
		}
	}
	
	/**
	 * Process the Factory Return Process 90A
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 * @param listProcessPoint
	 */
	@Transactional (propagation = Propagation.REQUIRED)
	public void factoryReturn90A (final String productId, final Timestamp actualTimestamp, final String processPointId, final String listProcessPoint ) throws TaskException
	{
		getLogger().info("Start to process Factory Return 90A");		

		// reset installed part status to BLANK for listed parts
		List<String> partsToBackOut = PropertyService.getPropertyList(processPointId, PARTS_TO_BACKOUT);
		if (partsToBackOut != null && !partsToBackOut.isEmpty()) {
			InstalledPartDao installedPartDao  =  ServiceFactory.getDao(InstalledPartDao.class);
			installedPartDao.updateInstalledPartStatus(productId, partsToBackOut, com.honda.galc.entity.enumtype.InstalledPartStatus.BLANK.getId());
		}

		//the next block code evaluate status from gal263tbx for current vin. 
		//If current Vin has status major that the actual interface. It doesn't do nothing 
		//If current Vin has status less so this update the tables 215 and 240 with PP before of 75A
		//If current Vin doesn't exist into gal263, interface will continue with the next vin
		ShippingStatusDao shippingStatusDao = ServiceFactory.getDao(ShippingStatusDao.class);
		ShippingStatus shippingStatus = shippingStatusDao.findByKey(productId);
		if (shippingStatus == null) {
			errorMessage = "Unable to find the VIN record in gal263tbx for the product_id = " + productId;
			getLogger().error(errorMessage);
			throw new TaskException(errorMessage);
		}
		else 
		{
			if( ShippingStatusEnum.S90A.equals(ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus())) )
			{
				errorMessage = "Status = " + shippingStatus.getStatus() + " indicates that Factory Return Confirm has already been processed. No update applied.";
				getLogger().error(errorMessage);
				throw new TaskException (errorMessage);
			}
			else
			{
				if (shippingStatus.getStatus() > ShippingStatusEnum.S75A.getStatus()) {
					errorMessage = "VIN = " + shippingStatus.getVin() + " Cannot be processed because Status = " + shippingStatus.getStatus() + " is incorrect";
					getLogger().error(errorMessage);
					throw new TaskException(errorMessage);
				} else {
					if ( ShippingStatusEnum.INIT.equals(ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus()))) {
						errorMessage = "Status = " + shippingStatus.getStatus() 
								+ " indicates that ADCShipping has not already been processed by 50A. No update applied.";
						getLogger().error(errorMessage);
						throw new TaskException(errorMessage);
					}
				}
			}
		}
		
		try
		{
			ProductResultDao productResultDao = ServiceFactory.getDao(ProductResultDao.class);
			// This method delete history that exist by 60A and 70A into Gal215tbx
			getLogger().info("Step - Executing Deleting history for 60A, 70A and 75A into Production Result (gal215tbx)");
			int recordsDeleted = productResultDao.deleteHistoryByProcessPoint(listProcessPoint, productId);			
			getLogger().info("Records deleted: " + recordsDeleted);
			
			getLogger().info( "Executing the tracking for the product = " + productId );
			TrackingService trackingService	= ServiceFactory.getService( TrackingService.class );
			trackingService.track( ProductType.FRAME, productId, processPointId );
			
			// The next code update table 263, this set status as '-1' and
			// Actual_Timestamp as date from current message
			getLogger().info("Step - Executing updating shipping status into Gal263tbx with VIN: " + productId);
			shippingStatus = shippingStatusDao.findByKey(productId);
			shippingStatus.setStatus			( ShippingStatusEnum.S90A.getStatus() );
			shippingStatus.setActualTimestamp	( actualTimestamp );
			shippingStatusDao.save				( shippingStatus );	
			
			// Updating ShippingTransaction (gal148tbx) with sendedFlg as N
			getLogger().debug("Updating ShippingTransaction (gal148tbx) with sendedFlg as N for VIN = " + productId);
			ShippingTransactionDao shippingTransactionDao = ServiceFactory.getDao(ShippingTransactionDao.class);
			ShippingTransaction shippingTransaction = shippingTransactionDao.findByKey(productId);
			//validate if the shipping transaction still exist, it means that 75A has not been processed
			if (shippingTransaction != null)
			{
				shippingTransaction.setSendFlag ( SENDED_FLAG );
				shippingTransactionDao.update ( shippingTransaction );
			}
			//Delete the Park Change 65A records
			//Delete 65A records from  (gal163tbx)
			ParkChangeDao parkChangeDao = ServiceFactory.getDao(ParkChangeDao.class);
			parkChangeDao.remove(parkChangeDao.findByKey(productId));		
			
			getLogger().info("Step - Executing insert into gal177tbx");
			InRepairAreaDao inRepairAreaDao = ServiceFactory.getDao(InRepairAreaDao.class);
			InRepairArea inRepairArea 		= new InRepairArea();
			inRepairArea.setProductId		(	productId	);
			inRepairArea.setRepairAreaName	(	FACTORY_RETURN_LABEL	);
			inRepairArea.setActualTimestamp	(	actualTimestamp	);
			inRepairArea.setResponsibleDept	(	RESPONSIBLE_DEPARTMENT	);
			inRepairAreaDao.save			(	inRepairArea	);
			getLogger().info("Finishing the proccess Factory Return 90A");
		} catch (Exception e)
		{
			errorMessage = "Error processing the 90A message" + e.getMessage();
			getLogger().error(errorMessage);
			throw new TaskException (errorMessage);
		}
	}
	
	/**
	 * Method to save the Ship Confirmation data.
	 * Save the VIN was sended with the Engine Number Identification
	 * @param productId
	 * @param processPointId
	 * @param messageDate
	 * @param messageTime
	 */
	public void processFrameShipConfirmation ( final String productId, final String processPointId, final String messageDate, final String messageTime, ShippingStatusEnum status )
	{
		FrameDao frameDao			=	ServiceFactory.getDao( FrameDao.class );
		FrameSpecDao frameSpecDao	=	ServiceFactory.getDao( FrameSpecDao.class );
		FrameShipConfirmationDao frameShipConfirmationDao = ServiceFactory.getDao(FrameShipConfirmationDao.class);
		
		Frame 				frame	=	frameDao.findByKey( productId );
		FrameSpec		frameSpec	=	frameSpecDao.findByKey( frame.getProductSpecCode() );
		
		
		FrameShipConfirmationId shipConfirmationId = new FrameShipConfirmationId();
		shipConfirmationId.setEngineId			( frame.getEngineSerialNo() );
		shipConfirmationId.setProcessPointId	( processPointId );
		shipConfirmationId.setProductId			( productId		 );
		
		FrameShipConfirmation frameShipConfirmation = new FrameShipConfirmation();
		frameShipConfirmation.setId				( shipConfirmationId );				
		frameShipConfirmation.setEventDate		( messageDate );
		frameShipConfirmation.setEventTime		( messageTime );
		frameShipConfirmation.setFrameModel		( frameSpec.getModelYearCode()+ frame.getModelCode());
		frameShipConfirmation.setFrameType		( frameSpec.getModelTypeCode()	);
		frameShipConfirmation.setExtColor		( frameSpec.getExtColorCode()	);
		frameShipConfirmation.setIntColor		( frameSpec.getIntColorCode()	);
		frameShipConfirmation.setFrameOption	( frameSpec.getModelOptionCode());
		frameShipConfirmation.setSentFlag		( "I"	);				
		frameShipConfirmation.setRecordType		( String.valueOf( status.getStatus())	);
		frameShipConfirmationDao.save(frameShipConfirmation);			
	}
	
	/**
	 * Method to parse the commons attributes from the message for the receiving transactions
	 * @param componentId			-- the current transaction
	 * @param layoutPropertyDef		-- the property value with the parse line def property 
	 * @param message				-- Message to process
	 * @param productId				-- product id to send back
	 * @param actualTimestamp		-- the actual time to send bakc
	 */
	public D parseCommonAttriburesFromMessage ( D receivingTransactionDTO )
	{
		//create the product id
		final String productId	=	StringUtils.trim( receivingTransactionDTO.getVin() ) + StringUtils.trim( receivingTransactionDTO.getFrame() );
		receivingTransactionDTO.setProductId( productId );		
		//get the date from the message
		String messageDateTime	=	receivingTransactionDTO.getMessageDate() + receivingTransactionDTO.getMessageTime();
		String dateTime		=	null;
		try {
			Date inputDate	=	new SimpleDateFormat( INPUT_DATE_FORMAT	).parse( messageDateTime );
			dateTime		=	new SimpleDateFormat( ACTUAL_TIMESTAMP_FORMAT ).format( inputDate );
		} catch (ParseException e) {
			throw new TaskException ( "The input date and time are in wrong format" + messageDateTime, e.getCause() );
		}
		receivingTransactionDTO.setActualTimestamp( Timestamp.valueOf( dateTime ) );
		return receivingTransactionDTO;
	}

} 
