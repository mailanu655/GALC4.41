package com.honda.galc.dao.product;

import java.util.Map;

import com.honda.galc.entity.product.LetInspectionParam;
import com.honda.galc.service.IDaoService;

public interface LetInspectionParamDao extends IDaoService<LetInspectionParam, Integer> {
	
	Map<String, Integer> loadAllLetInspectionParam();
	
	LetInspectionParam findParamIdByName(String paramName);

}
