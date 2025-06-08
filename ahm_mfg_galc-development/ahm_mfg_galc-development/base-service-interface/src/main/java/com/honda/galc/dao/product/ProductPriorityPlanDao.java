package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.entity.product.ProductPriorityPlanId;

import com.honda.galc.service.IDaoService;

public interface ProductPriorityPlanDao extends IDaoService<ProductPriorityPlan, ProductPriorityPlanId> {
	
	public ProductPriorityPlan getNextProduct(String orderNumber, PlanStatus planStatus);
	
	public List<ProductPriorityPlan> getProductsByOrderNumber(String orderNumber);

	public List<ProductPriorityPlan> getProductsByOrderNumber(String orderNumber, PlanStatus planStatus);

	public List<ProductPriorityPlan> getProductsByPlanStatus(PlanStatus planStatus, int maxProducts);
		
	public List<Object[]> getAvailableProductsToLoad(int maxProducts);
			
	public List<ProductPriorityPlan> getLastLoadedProducts(String containerId);
	
	public int doLoad(String containerId, String containerPos, Double sequenceNo);

	public int doUnload(String containerId);
	
	public List<Object[]> findOrderNoAndActualByDivisionIdAndPlanStatus(String divisionId, PlanStatus planStatus);

	public List<ProductPriorityPlan> getProductsByPlanStatus(String code, PlanStatus status, int maxResults);

	public Double findMaxSequence(String trackingStaus);
}
