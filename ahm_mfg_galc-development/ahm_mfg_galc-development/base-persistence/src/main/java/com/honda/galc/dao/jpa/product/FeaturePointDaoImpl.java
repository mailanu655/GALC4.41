package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.FeaturePointDao;
import com.honda.galc.entity.product.FeaturePoint;
import com.honda.galc.entity.product.FeaturePointId;
import com.honda.galc.service.Parameters;


public class FeaturePointDaoImpl extends BaseDaoImpl<FeaturePoint, FeaturePointId>
	implements FeaturePointDao {
	
	private static final String FIND_BY_ID_SQL = "select fp from FeaturePoints fp where fp.id.featureId = :featureId";


	public List<FeaturePoint> getFeaturePointsById(String featureId)
	{
		Parameters params = Parameters.with("featureId", featureId);
		List<FeaturePoint> objects = findAllByQuery(FIND_BY_ID_SQL, params);
		return objects == null ? null : objects;
	}
}
