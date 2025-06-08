package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.DunnageContent;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.service.IDaoService;

public interface DunnageContentDao extends IDaoService<DunnageContent, DunnageContentId>{	
	
	public List<DunnageContent> findAllProductsInDunnage(String dunnageId);
	public DunnageContent findById(String dunnageNumber);
	public List<String> findAllProductIdsInDunnage(String dunnageId);
 
}
