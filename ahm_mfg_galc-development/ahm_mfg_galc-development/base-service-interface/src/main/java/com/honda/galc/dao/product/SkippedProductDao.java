package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.service.IDaoService;

public interface SkippedProductDao extends IDaoService<SkippedProduct, SkippedProductId>
{
	/** delete all skipped products matching array of product ids
	 * 
	 * @param prodIds
	 */
	public int deleteProdIds(List <String> prodIds);
    /**
     * Find all 
     * @param processPointId
     * @param status
     * @return
     */
    List<String> findProductByProcessPointAndStatus(String processPointId, int status);
    
    /**
     * find all by process point id
     * @param processPointId
     * @return
     */
    List<SkippedProduct> findAllByProcessPointId(String processPointId);

}
