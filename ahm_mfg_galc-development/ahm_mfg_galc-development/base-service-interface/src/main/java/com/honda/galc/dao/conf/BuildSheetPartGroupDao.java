/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.BuildSheetPartGroup;
import com.honda.galc.entity.conf.BuildSheetPartGroupId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.IDaoService;

public interface BuildSheetPartGroupDao extends IDaoService<BuildSheetPartGroup, BuildSheetPartGroupId> {
	
	public List<BuildSheetPartGroup> findAllByFormId(String formId, String modelGroup);
	
	public List<Object[]> generatePartMatrix(ProductSpec productSpec, String name);
	
}
