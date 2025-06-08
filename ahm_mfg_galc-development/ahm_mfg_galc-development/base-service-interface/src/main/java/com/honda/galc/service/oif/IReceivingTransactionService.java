package com.honda.galc.service.oif;

import java.sql.Timestamp;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.ProductType;
import com.honda.galc.service.IService;

public interface IReceivingTransactionService extends IService
{
	/**
	 * Method to process the 60A message.
	 * Make the tracking for the product and update the status and actual time stamp at ShippingStatus
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 */
	public void shippingConfirm60A( final String productId, final Timestamp actualTimestamp, final String processPointId, ProductType productType ) throws TaskException;
	
	/**
	 * Process the Dealer Assign 70A.
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 * @param messageDate
	 * @param messageTime
	 * @throws TaskException
	 */
	public void dealerAssigned70A( final String productId, final Timestamp actualTimestamp, final String processPointId, final String messageDate, final String messageTime, final String dealerNo, ProductType productType ) throws TaskException;
	
	/**
	 * Process the Shipment Confirm 75A transaction
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 * @param messageDate
	 * @param messageTime
	 * @throws TaskException
	 */
	public void shipmentConfirm75A( final String productId, final Timestamp actualTimestamp, final String processPointId, final String messageDate, final String messageTime, final String dealerNo, ProductType productType ) throws TaskException;
	
	/**
	 * Process the Factory Return Process 90A
	 * @param productId
	 * @param actualTimestamp
	 * @param processPointId
	 * @param listProcessPoint
	 */
	public void factoryReturn90A (final String productId, final Timestamp actualTimestamp, final String processPointId,  ProductType productType ) throws TaskException;
	
	public void createNaqDefectAndParking(String productId, String naqDefectName, String processPoint, String repairArea);
}
