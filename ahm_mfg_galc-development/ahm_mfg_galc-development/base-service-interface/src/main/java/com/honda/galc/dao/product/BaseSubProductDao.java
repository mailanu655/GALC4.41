package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductLot;
import com.honda.galc.util.KeyValue;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BaseSubProductDao</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jun 29, 2015
 */

public interface BaseSubProductDao<E extends SubProduct> extends ProductDao<E> {

	// Knuckle specific Minimum Serial Number for remake
	public final int MIN_REMAKE_SN_LIMIT = 950000;
	public final int MAX_REMAKE_SN_LIMIT = 999999;

	/**
	 * find max serial number of the product with the product id prefixed with
	 * prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public String findMaxProductId(String prefix);

	/**
	 *find min product id in a production lot, with product id prefixed with
	 * "prefix"
	 * 
	 * @return productId - null if not found
	 */
	public String findMinProductId(String productionLot, String prefix);

	/**
	 * Find production lot summary from sub_product
	 * 
	 * @return
	 */
	public List<SubProductLot> findProductionLots();

	public List<E> findAllByProductionLot(String productionLot);

	/**
	 * find all the shipped subproducts older than passed date
	 * 
	 * @param productionDate
	 * @return
	 */
	public List<E> findAllShipped(String productionDate);

	/**
	 * Find the minimum remake sub product id
	 */
	public String findMinRemake(String productId);

	/**
	 * find the subproducts whose last a few digits matches the serial number
	 * 
	 * @param serialNumber
	 * @return
	 */
	public List<E> findAllMatchSerialNumber(String serialNumber);

	/**
	 * Find passing product count for left and right side knuckles
	 * 
	 * @param productionLot
	 * @param processPoint
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<KeyValue> findPassingSubProduct(String productionLot, String processPoint);

	/**
	 * check if the created knuckle serial numbers matches the part number build
	 * attributes check both left and right side
	 * 
	 * @param productionLot
	 *            - selected production lot
	 * @return all knuckles which do not match
	 */
	public List<E> findAllNotMatchingPartNumbers(String productionLot);

	/**
	 * delete all sub products for a production lot and a sub id
	 * 
	 * @param productionLot
	 * @param subId
	 */
	public void deleteAll(String productionLot, String subId);

	/**
	 * delete all sub products matching array of kdLots
	 * 
	 * @param kdLots
	 */
	public int deleteKdLots(String productType, List<String> kdLots);

	public List<E> findByTrackingStatus(String trackingStatus);

	/**
	 * find the first product for the given production lot and sub id
	 * 
	 * @param productionLot
	 * @param subId
	 * @return
	 */
	public E findFistProduct(String productionLot, String subId);

	public List<E> findByPartName(String lineId, int prePrintQty, int maxPrintCycle, String ppid, String partName);

	/**
	 * find the last product for the given production lot and sub id
	 * 
	 * @param productionLot
	 * @param subId
	 * @return
	 */
	public E findLastProduct(String productionLot, String subId);

	/**
	 * find the total number of subproduct of the same sub id within a
	 * production lot
	 * 
	 * @param productionLot
	 * @param partCode
	 * @return
	 */
	public int count(String productionLot, String partCode);

	public int countProductionLotByKdLot(String productionLot, String kdLot);
}
