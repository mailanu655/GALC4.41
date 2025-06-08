package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.MapFeatureLocationDao;
import com.honda.galc.dto.FeatureLocation;
import com.honda.galc.dto.FeatureLocations;
import com.honda.galc.entity.product.MapFeatureLocation;
import com.honda.galc.entity.product.MapFeatureLocationId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Mar 25, 2013 
 * 
 */
public class MapFeatureLocationDaoImpl extends BaseDaoImpl<MapFeatureLocation, MapFeatureLocationId> 
	implements MapFeatureLocationDao {
	
	private static final String FIND_TYPE_SQL = "select mfl from MapFeatureLocation mfl where mfl.id.featureType = :featureType";

	public List<MapFeatureLocation> getFeatureLocations() {
		return findAll();
	}
	
	public List<MapFeatureLocation> getFeaturesByType(String featureType) {
		Parameters params = Parameters.with("featureType", featureType);
		List<MapFeatureLocation> objects = findAllByQuery(FIND_TYPE_SQL, params);
		return objects == null ? null : objects;

	}
	
	@Transactional
	public void deleteFeature(String featureId, String featureType, String featureLayer) {
		removeByKey(new MapFeatureLocationId(featureId, featureType, featureLayer));
	}
	
	public void saveFeatureLocation(String featureId, String featureType, String featureLayer, double longitude, double latitude) {
		MapFeatureLocation location = new MapFeatureLocation(featureId, featureType, featureLayer);
		location.setLongitude(longitude);
		location.setLatitude(latitude);
		update(location);
	}

	public FeatureLocations getCbuLocations() {
		FeatureLocations locations = new FeatureLocations();
		for (MapFeatureLocation loc: getFeatureLocations()) {
			FeatureLocation fLoc = new FeatureLocation();
			fLoc.setFeatureId(loc.getId().getFeatureId());
			fLoc.setFeatureType(loc.getId().getFeatureType());
			fLoc.setFeatureLayer(loc.getId().getFeatureLayer());
			fLoc.setLongitude(loc.getLongitude());
			fLoc.setLatitude(loc.getLatitude());
			fLoc.setCreateTimestamp(loc.getCreateTimestamp() == null? "": loc.getCreateTimestamp().toString());
			locations.getFeatureLocations().add(fLoc);
		}
		return locations;
	}
}
