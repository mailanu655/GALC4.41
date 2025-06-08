package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.service.IDaoService;

public interface ExpectedProductDao extends IDaoService<ExpectedProduct, String> {
	/**
	 * Finds all expected product records ordered by PROCESS_POINT_ID
	 */
	public List<ExpectedProduct> findAllOrderByProcessPoint();
	public List<ExpectedProduct> findAllForProcessPoint(String processPoint);
	public ExpectedProduct findForProcessPointAndProduct(String processPointId, String productId);
	public List<ExpectedProduct> findAllByProductId(String productId);
	public ExpectedProduct findForProcessPoint(String processPointId);
}
