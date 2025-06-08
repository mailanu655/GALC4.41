package com.honda.galc.visualoverview.shared;

public enum FeatureType {
	POINT("POINT"),
	LINE("LINE"),
	AREA("AREA"),
	IMAGE("IMAGE"),
	DEFECT("DEFECT");
	
	private String featureName;
	
	private FeatureType(String featureName)
	{
		this.featureName = featureName;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

}
