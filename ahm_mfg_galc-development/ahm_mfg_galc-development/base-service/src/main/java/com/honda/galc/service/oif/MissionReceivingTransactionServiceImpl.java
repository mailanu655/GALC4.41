package com.honda.galc.service.oif;

import java.sql.Timestamp;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

public class MissionReceivingTransactionServiceImpl extends
		ReceivingTransactionServiceImpl implements IReceivingTransactionService{
	private Logger logger	=	Logger.getLogger("MissionReceivingTransactionServiceImpl");
	private String errorMessage =  "";
	
	
	
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
	public void dealerAssigned70A( final String productId, final Timestamp actualTimestamp, final String processPointId, final String messageDate, final String messageTime, final String dealerNo, ProductType productType ) throws TaskException
	{
		logger.info( "Starting the process for 70A transaction with with productId = " + productId );
		logger.info( "Loading the layouts properties..." );

		//parseCommonAttriburesFromMessage( componentId, LAYOUT, message );
		
		logger.info( "Get the PRODUCT_ID = " + productId + " from shipping status from gal263tbx " );
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
			logger.error(errorMessage);
			throw new TaskException (errorMessage);
		}
		
		if( vinCurrentStatus.getStatus() == ShippingStatusEnum.INIT.getStatus() )
		{
			errorMessage = "Product_Id = " + shippingStatus.getId() + " with status = " + shippingStatus.getStatus()
					+ " indicates that AHM Shipping Transaction (50A) must be performed before. No update applied";
			logger.error(errorMessage);
			throw new TaskException (errorMessage);
		}	
				
		try
		{
			logger.info( "Executing the tracking for the product = " + productId );
			TrackingService trakingProduct	=	ServiceFactory.getService( TrackingService.class );
			trakingProduct.track(productType, productId, processPointId);
		
			//update the status into the gal263tbx
			logger.info("Update the VIN status in the Shipping Status - gal263tbx ");
			shippingStatus.setStatus	(	ShippingStatusEnum.S70A.getStatus() );
			shippingStatus.setDealerNo	(	dealerNo );
			shippingStatusDao.update	(	shippingStatus	);
			logger.info("End process Dealer Assing 70A transaction");
		} catch (Exception e)
		{
			errorMessage = "Error processing the 70A message" + e.getMessage();
			logger.error(errorMessage);
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
	public void shipmentConfirm75A( final String productId, final Timestamp actualTimestamp, final String processPointId, final String messageDate, final String messageTime, final String dealerNo, ProductType productType ) throws TaskException
	{
		logger.info("Start to process Shipment Confirm 75A");
		/*
		the next block code evaluate status from gal263tbx for current vin. 
		 -If current Vin has status major that the actual interface. It doesn't do nothing 
		 -If current Vin doesn't exist into gal263, interface will continue with the next vin
		*/
		ShippingStatusDao shippingStatusDao = ServiceFactory.getDao( ShippingStatusDao.class );
		ShippingStatus shippingStatus = shippingStatusDao.findByKey( productId );
		
		if ( shippingStatus == null) {
			errorMessage = "Unable to find the VIN record in gal263tbx for the product_id: " + productId;
			logger.error(errorMessage);
			throw new TaskException(errorMessage);
		}
		if ( shippingStatus.getStatus() >= ShippingStatusEnum.S75A.getStatus() ) {//4
			errorMessage = "Status = " + shippingStatus.getStatus() + " indicates that AHM Shipment Confirm has already been processed. No update applied.";
			logger.error(errorMessage);
			throw new TaskException (errorMessage);			
		} else {
			if (ShippingStatusEnum.INIT.equals( ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus() ))) {
				errorMessage = "Status = " + shippingStatus.getStatus() + " this VIN need to be process by 50A before. ProductId: \"" + productId + "\"";
				logger.error(errorMessage);
				throw new TaskException (errorMessage);				
			} 
		}

		try
		{	
			// The next code update table 263, this set status as '4' and
			// Actual_Timestamp as date from current message
			logger.info("Step - Executing updating shipping status into Gal263tbx with VIN: " + productId);
			shippingStatus = shippingStatusDao.findByKey(productId);
			shippingStatus.setStatus(ShippingStatusEnum.S75A.getStatus());
			shippingStatus.setDealerNo(dealerNo);
			shippingStatus.setActualTimestamp(actualTimestamp);
			shippingStatusDao.save(shippingStatus);		
			
			// The next code get from table 176 the next product id of current VIN
			logger.info( "Executing the tracking for the product = " + productId );
			TrackingService trakingProduct	=	ServiceFactory.getService( TrackingService.class );
			trakingProduct.track(productType, productId, processPointId);				
			//Delete InProcessProduct (Gal176tbx) records
			InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
			logger.debug("Executig deleting Gal176tbx with VIN = " + productId);				
			inProcessProductDao.remove(inProcessProductDao.findByKey(productId));			
			logger.info("End process Shipment Confirm");
		} catch (Exception e)
		{
			logger.error( "Error processing the 75A message" + e.getMessage() );
			throw new TaskException ( "Error processing the 75A message", e.getCause() );
		}
	}
}
