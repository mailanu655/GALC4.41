package com.honda.galc.locationTracking.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;


public interface LocationFactory extends AutoBeanFactory {

	AutoBean<LocationsResult> locationsResult();
	AutoBean<FeatureLocation> featureLocation();
}
