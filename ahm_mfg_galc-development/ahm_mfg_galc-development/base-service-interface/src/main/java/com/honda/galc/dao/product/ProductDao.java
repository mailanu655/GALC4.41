package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.honda.galc.data.ProductType;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ProductDao Class description</h3>
 * <p> ProductDao description </p>
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
 * @author Jeffray Huang<br>
 * Feb 17, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface ProductDao<E extends BaseProduct> extends IDaoService<E, String> {
	
	public E findBySn(String sn);
	
	public E findBySn(String sn, ProductType productType);
	
	public void updateTrackingAttributes(E product);
	
	public void updateProductOffPPTrackingAttributes(E product);
	
	public void updateDefectStatus(String productId, DefectStatus status);
		
	public List<E> findAllBySN(String SN);
	
	/**
	 * Returns a page of products for the given serial number.
	 * @param SN - serial number to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	public List<E> findPageBySN(String SN, int pageNumber, int pageSize);
	
	/**
	 * Given a serial number and process point id, returns all products that match the serial number<br>
	 * and have a TRACKING_STATUS that is in the process point's line's previous line configuration.
	 */
	public List<E> findAllBySNPP(String SN, String PP);

	public void updateHoldStatus(String productId, int status);

	/**
     * find last "count" products at the processpoint processPointId
     * order by actualtimestamp desc
     * @param processPointId
     * @param count
     * @return
     */
    public List<E> findAllLastProcessed(String processPointId, int count);
    public Integer getScrapedExceptionalCount(String productionLot, String scrapLineID, String exceptionalLineID);
	public List<InventoryCount> findAllInventoryCounts();
	public List<E> findAllByProductionLot(String productionLot);
	public List<E> findAllByLikeProductionLot(String productionLot);
	public List<E> findAllByProcessPoint(String processPointId, Timestamp startTime, Timestamp endTime);
	public List<E> findAllByProcessPoint(String processPointId);
	public List<E> findAllByProcessPointAndModel(String processPointId, Timestamp startTime, Timestamp endTime, String modelCode,ProductType productType);
	public List<E> findAllByQsrId(int qsrId);
	public List<E> findByTrackingStatus(String trackingStatus);
	public List<E> findByTrackingStatus(List<String> trackingStatusList);
	
	public long countByMatchingSN(String sn);
	public long countByProductionLot(String prodLot);
	public long countByTrackingStatus(String trackingStatus);
	
	/**
	 * Returns a page of products for the given production lot.
	 * @param productionLot - production lot to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	public List<E> findPageByProductionLot(String productionLot, int pageNumber, int pageSize);
	
	/**
	 * Returns a page of products for the given tracking status.
	 * @param trackingStatus - tracking status to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	public List<E> findPageByTrackingStatus(String trackingStatus, int pageNumber, int pageSize);
	
	/**
	 * create all products of a production lot - also save ProductionLot and PreProductionLot
	 * @param productionLot
	 * @return the number of products created
	 */
	public int createProducts(ProductionLot preProdLot,String productType,String lineId,String ppId);
	
	public List<Object[]> getProductsWithinRange(String startProductId,String stopProductId);

	public List<E> findAllByDunnage(String dunnageNumber);

	public int updateDunnage(String productId, String dunnageNumber, int dunnageCapacity);
	
	public int updateDunnage(List<String>productIds, String dunnageNumber, int dunnageCapacity);

	public int removeDunnage(String productId);

	public long countByDunnage(String dunnage);
	
	public List<Map<String, Object>> selectDunnageInfo(String criteria, int resultsetSize);

	public int releaseHoldWithCheck(String productId);

	/**
	 * Given a lot prefix and production date, returns true iff there is an active<br>
	 * product for any PRODUCTION_LOT like the given lot prefix production date.
	 */
	public boolean isProductActiveForProductionDate(String lotPrefix, String productionDate, List<String> initialProcessPointIds);
	
	/**
	 * Given a product id and process point id, returns true iff the product's TRACKING_STATUS<br>
	 * is one of the process point's valid previous line ids.
	 */
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId);
	
	public List<String> findAllTrackingStatus();
	public List<String> findAllTrackingStatusByPlant(String plantName);
	
	/**
	 * Find the next product after this product in the in process product list
	 * @param productId
	 * @param processPoint
	 * @return
	 */
	public E findNextInprocessProduct(String productId);
	/**
	 * find the previous product in product seq before the current product
	 * @param productId
	 * @return
	 */
	public E findPreviousInprocessProduct(String productId);
	
	public List<E> findProducts(List<String> productIds, int startPos, int pageSize);
	
	public void updateNextTracking(String productId, String trackingStatus);
	
	public List<E> findAllByProductIdRange(String start, String end, int count);
	public List<E> findAllByPlantProductIdRange(String plantName, String start, String end, int count);
	public List<E> findByTrackingStatus(String trackingStatus, int count);
	public List<E> findAllByProductionLot(String productionLot, int count);
	
	public List<String> findProductionLotNumbersBySubstring(String prodLotSubstring);

	public List<E> findAllProcessedProductsForProcessPointForTimeRange(String onPP, String startTimeStamp,
			String endTimeStamp, List<String> shippedTrackingStatuses);
	
	public List<E> findAllProcessedProductsForProcessPointBeforeTime(String onPP, String startTimeStamp, List<String>shippedTrackingStatuses);

}