package com.honda.galc.dao.jpa.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetInspectionParamDao;
import com.honda.galc.entity.product.LetInspectionParam;
import com.honda.galc.service.Parameters;

public class LetInspectionParamDaoImpl extends BaseDaoImpl<LetInspectionParam, Integer> implements
		LetInspectionParamDao {

	public Map<String, Integer> loadAllLetInspectionParam() {
		Map<String, Integer> letInspectionParamMap = new HashMap<String, Integer>();
		List<LetInspectionParam> letInspectionParamList = findAll();
		for(LetInspectionParam letInspectionParam : letInspectionParamList){
			letInspectionParamMap.put(letInspectionParam.getInspectionParamName().trim(), letInspectionParam.getInspectionParamId());
		}
		return letInspectionParamMap;
	}

	public LetInspectionParam findParamIdByName(String paramName) {
		return findFirst(Parameters.with("inspectionParamName", paramName));
	}
}
