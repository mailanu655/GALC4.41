package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.FeaturePoint;
import com.honda.galc.entity.product.FeaturePointId;
import com.honda.galc.service.IDaoService;

/**
 * @author Cody Getz
 * @date Jul 16, 2013 
 * 
 */
public interface FeaturePointDao extends IDaoService<FeaturePoint, FeaturePointId> {
	
	public List<FeaturePoint> getFeaturePointsById(String featureId);
	
}

