package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 10, 2015
 * Recipe Download Service
 */
public interface MeasurementSpecDao extends IDaoService<MeasurementSpec, MeasurementSpecId> {
	
	public List<MeasurementSpec> findAllByPartNamePartId(String partName,String partId); 

}
