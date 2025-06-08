package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
 */
public interface ProductStampingSequenceDao extends IDaoService<ProductStampingSequence, ProductStampingSequenceId> {
	
	public List<ProductStampingSequence> findAllByProductId(String productId);
	
	public List<ProductStampingSequence> findAllByProductionLot(String productionLot);
	
	public ProductStampingSequence findById(String productionLot, String productId);
	
	public List<ProductStampingSequence> findAllNext(String productionLot, String productId);
	
	public List<ProductStampingSequence> findAllNext(String productionLot, int seq);
	
	public int findStampCount(String productionLot);
	
	public ProductStampingSequence findNextProduct(String productionLot, PlanStatus planStatus);
	
	public ProductStampingSequence findNextProduct(String productionLot, int seq);
	
	public int getFilledStampCount(String productionLot);
	
	public List<ProductStampingSequence> getLinkedProducts(String processLocation, int lotCount);
	
	public List<Object[]> findAllProductIdByProductionLotList(List<String> productionLotList);
	
	public int deleteProductionLot(String productionLot);
	
}
