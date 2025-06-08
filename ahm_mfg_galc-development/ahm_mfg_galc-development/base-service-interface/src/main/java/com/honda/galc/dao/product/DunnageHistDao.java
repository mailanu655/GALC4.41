package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.DunnageHist;
import com.honda.galc.entity.product.DunnageHistId;
import com.honda.galc.service.IDaoService;

public interface DunnageHistDao extends IDaoService<DunnageHist, DunnageHistId>{	
	
	public List<DunnageHist> findAll(String productId, String dunnageId);

	public DunnageHist findfirstProduct(String productId);

	public DunnageHist findByDunnageId(String dunnageId);
	
	public DunnageHist findByProductId(String productId);
	
	public List<DunnageHist> findAllByProductId(String productId);
	
}
