package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.service.Parameters;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 10, 2015
 * Recipe Download Service
 */
public class MeasurementSpecDaoImpl extends BaseDaoImpl<MeasurementSpec,MeasurementSpecId> implements MeasurementSpecDao {

    private static final long serialVersionUID = 1L;

 
    public List<MeasurementSpec> findAllByPartNamePartId(String partName,String partId) {
    	Parameters param=Parameters.with("id.partName",partName);
		param.put("id.partId", partId);
		return findAll(param);
	}
}
