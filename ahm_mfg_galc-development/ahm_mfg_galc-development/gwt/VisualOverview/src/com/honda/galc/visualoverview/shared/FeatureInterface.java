package com.honda.galc.visualoverview.shared;

import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

public interface FeatureInterface {
	
	public String getFeatureId();
	
	public void setFeatureId(String featureId);
	
	public String getFeatureType();
	
	public void setFeatureType(String featureType);
	
	public String getReferenceType();
	
	public void setReferenceType(String referenceType);
	
	public String getReferenceId();
	
	public void setReferenceId(String referenceId);
	
	public int getEnableHistory();
	
	public void setEnableHistory(int enableHistory);
	
	public List<FeaturePoints> getFeaturePoints();
	
	public void setFeaturePoints(List<FeaturePoints> featurePoints);

	public VectorFeature draw();

}
