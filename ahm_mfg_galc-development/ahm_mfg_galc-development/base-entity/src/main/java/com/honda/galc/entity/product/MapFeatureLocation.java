package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Mar 25, 2013 
 * 
 */
@Entity
@Table(name="FEATURE_LOCATION_TBX")
public class MapFeatureLocation extends AuditEntry {
	
	private static final long serialVersionUID = 8694782258410534109L;

	@EmbeddedId
	private MapFeatureLocationId id;

	@Column(name="LATITUDE")
	private double latitude;

	@Column(name="LONGITUDE")
	private double longitude;

	public MapFeatureLocation() {
		super();
	}
	
	public MapFeatureLocation(String featureId, String featureType, String featureLayer) {
		super();
		id = new MapFeatureLocationId(featureId, featureType, featureLayer);
	}
	
	public MapFeatureLocation(MapFeatureLocationId id) {
		super();
		this.id = id;
	}

	public MapFeatureLocationId getId() {
		return this.id;
	}

	public void setId(MapFeatureLocationId id) {
		this.id = id;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return toString(id.getFeatureId(), id.getFeatureType(), id.getFeatureLayer(), getLongitude(), getLatitude());
	} 
}
