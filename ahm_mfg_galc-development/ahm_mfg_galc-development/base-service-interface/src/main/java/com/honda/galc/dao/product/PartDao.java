package com.honda.galc.dao.product;

import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Part;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.service.IDaoService;

import java.util.List;

public interface PartDao extends IDaoService<Part, PartId> {

    public List<Part> findAllByPartName(String partName);

	public List<Part> findPartByLotCtrRule(LotControlRule rule);
	
	public void updateAllowDuplicate(String partName, int uniqueFlag);

}
