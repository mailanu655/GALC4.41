package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.Feature;
import com.honda.galc.service.IDaoService;

/**
 * @author Cody Getz
 * @date Jul 16, 2013 
 * 
 */
public interface FeatureDao extends IDaoService<Feature, String> {

	public List<Feature> getFeaturesById(String featureId);
	
	public List<Feature> getFeaturesByLayerId(String layerId);
		
	public List<Object[]> getLineData(String lineId, String trackingLayer);
	
	public List<Feature> getLineLocations(String featurePrefix);
	
	public List<Feature> findAllFeatures(String featureType);
}
