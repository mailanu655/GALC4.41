package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldAccessTypeId;
import com.honda.galc.service.IDaoService;

public interface HoldAccessTypeDao extends IDaoService<HoldAccessType, HoldAccessTypeId> {

	
	public List<HoldAccessType> findAllByMatchingSecurityGroups(List<String> groups);
	
	public List<HoldAccessType> findAllByMatchingProductType(String productType);
}