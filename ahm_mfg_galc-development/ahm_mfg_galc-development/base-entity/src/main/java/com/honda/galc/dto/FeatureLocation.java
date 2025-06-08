package com.honda.galc.dto;

/**
 * @author Subu Kathiresan
 * @date Mar 25, 2013 
 * 
 */
public class FeatureLocation {

	private String featureId;
	private Double longitude;
	private Double latitude;
	private String featureType;
	private String featureLayer;
	private String createTimestamp;
	
	public FeatureLocation() {
	}
	
	public String getFeatureId() {
		return featureId;
	}
	
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public String getFeatureType() {
		return featureType;
	}
	
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureLayer() {
		return featureLayer;
	}
	
	public void setFeatureLayer(String featureLayer) {
		this.featureLayer = featureLayer;
	}
	
	public String getCreateTimestamp() {
		return createTimestamp;
	}
	
	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
}
