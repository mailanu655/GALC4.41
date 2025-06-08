package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.FeatureLocations;
import com.honda.galc.entity.product.MapFeatureLocation;
import com.honda.galc.entity.product.MapFeatureLocationId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Mar 25, 2013 
 * 
 */
public interface MapFeatureLocationDao extends IDaoService<MapFeatureLocation, MapFeatureLocationId> {
	
	public List<MapFeatureLocation> getFeatureLocations();
	public List<MapFeatureLocation> getFeaturesByType(String featureType);
	public void deleteFeature(String featureId, String featureType, String featureLayer);
	public void saveFeatureLocation(String featureId, String featureType, String featureLayer, double longitude, double latitude);
	public FeatureLocations getCbuLocations();
}
