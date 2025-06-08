package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dao.oif.EntitySequenceInterface;
import com.honda.galc.entity.product.MbpnProduct;

/** * * 
* 
* @author Gangadhararao Gadde 
* @since May 16, 2016
*/

public interface MbpnProductDao extends  ProductDao<MbpnProduct>, EntitySequenceInterface {
	
	List<MbpnProduct> findSubAssemblyInventoryByDivisonId(String divisionId);
	
	List<MbpnProduct> findSubAssemblyInventoryDetailByZoneNameAndProductSpecCode(String zone, String productSpecCode, String orderNo);
	
	List<MbpnProduct> findProductProgressByDivisonId(String divisionId);
	
	List<MbpnProduct> findAllByContainer(String containerId);

	Double findMaxSequence(String trackingStatus);

	String findLastProductId(String productIdPrefix);
	
	List<MbpnProduct> findAllByOrderNo(String orderNo);

	/**
	 * Returns a page of MBPN products for the given order number.
	 * @param productionLot - order number to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	List<MbpnProduct> findPageByOrderNo(String orderNo, int pageNumber, int pageSize);

	List<MbpnProduct> findAllByCreateTimeStamp(String yearMonthDay, String createDate);
	
	int deleteProductionLot(String productionLot);
	
	List<MbpnProduct> findAllByInProcessProduct(String currentProductId, int processedSize, int upcomingSize);
	
	List<MbpnProduct> findAllByProductSequence(String processPointId, String currentProductId, int processedSize, int upcomingSize);

	String findCurrentProductSpecCode(String productId);
}
