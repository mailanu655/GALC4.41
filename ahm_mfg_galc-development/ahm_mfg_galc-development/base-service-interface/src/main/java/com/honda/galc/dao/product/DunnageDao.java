package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.service.IDaoService;

public interface DunnageDao extends IDaoService<Dunnage, String>{

	public List<Dunnage> findAllByPartialDunnage(String dunnage);
	public List<Dunnage> findAllByPartialProductId(String productId);
	public List<Dunnage> findAllByPartialMtoc(String productSpecCode);


}
