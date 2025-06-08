package com.honda.galc.dto;

import java.util.ArrayList;

/**
 * @author Subu Kathiresan
 * @date Mar 25, 2013 
 * 
 */
public class FeatureLocations {

	private ArrayList<FeatureLocation> features = new ArrayList<FeatureLocation>();
	
	public FeatureLocations() {
	}
	
	public ArrayList<FeatureLocation>getFeatureLocations() {
		return features;
	}
	
	public void setFeatureLocations(ArrayList<FeatureLocation> features) {
		this.features = features;
	}
}
