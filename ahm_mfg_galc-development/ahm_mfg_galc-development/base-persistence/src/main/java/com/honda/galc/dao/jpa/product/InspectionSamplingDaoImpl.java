package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.InspectionSamplingDao;
import com.honda.galc.entity.product.InspectionSampling;
import com.honda.galc.entity.product.InspectionSamplingId;


/*
* 
* @author Gangadhararao Gadde 
* @since Feb 06, 2014
*/
public class InspectionSamplingDaoImpl extends BaseDaoImpl<InspectionSampling, InspectionSamplingId> implements InspectionSamplingDao {
	
	
	private static final String SELECT_MISSING_MODEL_TYPE_CODES = "SELECT DISTINCT MODEL_CODE, MODEL_TYPE_CODE FROM GAL144TBX EXCEPT SELECT DISTINCT MODEL_CODE, MODEL_TYPE_CODE FROM GAL183TBX ORDER BY MODEL_CODE";

	public List<Object[]> findAllMissingInspSamplingModelTypeCodes() {
		return executeNative(SELECT_MISSING_MODEL_TYPE_CODES);
	}	

}
