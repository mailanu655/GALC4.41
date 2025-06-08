package com.honda.galc.locationTracking.shared;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface LocationsResult{

	@PropertyName("features")
	void setLocations(List<FeatureLocation> features);
	@PropertyName("features")
	List<FeatureLocation> getFeatures();
	
}
