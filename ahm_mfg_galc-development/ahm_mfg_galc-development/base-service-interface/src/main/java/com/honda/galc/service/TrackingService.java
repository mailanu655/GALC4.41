package com.honda.galc.service;

import java.sql.Timestamp;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.IService;

/**
 * 
 * <h3>TrackingService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TrackingService description </p>
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
 * @author Paul Chou
 * Sep 8, 2010
 *
 */

public interface TrackingService extends IService{
	
	/**
	 * track the product in a process point
	 * based on the setting for process point, this service will update status in engine or frame table and the product sequence in "in process product" table
	 * and update count , count by production lot , count by product spec table etc
	 * 
	 * @param product - the product to be tracked. product object can be an engine or frame or other types of product
	 * @param processPointId - process point id to be tracked
	 */
	public void track(BaseProduct product, String processPointId);
	
	/**
	 * track the product with product type and product id in a process point
	 * based on the setting for process point, this service will update status in engine or frame table and the product sequence in "in process product" table
	 * and update count , count by production lot , count by product spec table etc
	 *
	 * @param productType - product type
	 * @param productId   - product id
	 * @param processPointId - process point Id
	 */
	public void track(ProductType productType, String productId, String processPointId);
	
	/**
	 * track the product with product type and product id in a process point
	 * based on the setting for process point, this service will update status in engine or frame table and the product sequence in "in process product" table
	 * and update count , count by production lot , count by product spec table etc
	 *
	 * @param productType - product type
	 * @param productId   - product id
	 * @param processPointId - process point Id
	 * @param deviceId - tag Value
	 */
	public void track(ProductType productType, String productId, String processPointId,String deviceId);
	/**
	 * track the product using productHistory
	 * 
	 * @param productType
	 * @param productHistory
	 */
	public void track(ProductType productType, ProductHistory productHistory);

	/**
	 * track the product in a process point
	 * based on the setting for process point, this service will update status in engine or frame table and the product sequence in "in process product" table
	 * and update count , count by production lot , count by product spec table etc
	 * 
	 * @param product - the product to be tracked. product object can be an engine or frame or other types of product
	 * @param processPointId - process point id to be tracked
	 * @param deviceId - tag Value to be tracked
	 */
	public void track(BaseProduct product, String trackingProcessPointId,
			String deviceId);

			
}

