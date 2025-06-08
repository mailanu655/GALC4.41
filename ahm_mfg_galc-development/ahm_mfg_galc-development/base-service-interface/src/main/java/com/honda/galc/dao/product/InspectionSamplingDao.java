package com.honda.galc.dao.product;


import java.util.List;
import com.honda.galc.entity.product.InspectionSampling;
import com.honda.galc.entity.product.InspectionSamplingId;
import com.honda.galc.service.IDaoService;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface InspectionSamplingDao extends IDaoService<InspectionSampling, InspectionSamplingId> {
	
	public List<Object[]> findAllMissingInspSamplingModelTypeCodes();
	
	

}