package com.honda.galc.locationTracking.shared;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface FeatureLocation {

	@PropertyName("featureId")
	String getFeatureID();
	@PropertyName("featureType")
	String getFeatureType();
	@PropertyName("featureLayer")
	String getFeatureLayer();
	@PropertyName("longitude")
	double getLongitude();
	@PropertyName("latitude")
	double getLatitude();
	@PropertyName("createTimestamp")
	String getTimestamp();
	@PropertyName("featureId")
	void setFeatureID(String feature);
	@PropertyName("featureType")
	void setFeatureType(String featureType);
	@PropertyName("featureLayer")
	void setFeatureLayer(String featureLayer);
	@PropertyName("longitude")
	void setLongitude(double longitude);
	@PropertyName("latitude")
	void setLatitude(double latitude);
	@PropertyName("createTimestamp")
	void setTimeStamp(String createTimestamp);
}
