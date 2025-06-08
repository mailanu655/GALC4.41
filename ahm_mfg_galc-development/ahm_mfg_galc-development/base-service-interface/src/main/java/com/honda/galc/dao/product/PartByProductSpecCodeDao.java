package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.IDaoService;

public interface PartByProductSpecCodeDao extends IDaoService<PartByProductSpecCode, PartByProductSpecCodeId> {
	
	public List<PartByProductSpecCode> findByProductSpec(ProductSpec productSpec);	
	
	public PartByProductSpecCode getPartId(String productSpecCode, String partName);
	
	public List<PartByProductSpecCode> findAllByPartName(String partName);
	
    public List<PartByProductSpecCode> findAllByPartNameAndProductSpec(String partName, String productSpec, String productType);
	
}