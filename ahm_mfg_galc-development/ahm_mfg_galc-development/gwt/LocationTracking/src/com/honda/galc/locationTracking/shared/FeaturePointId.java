package com.honda.galc.locationTracking.shared;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public class FeaturePointId {
	
	private String featureId;
	private int featureSeq;
	
	public FeaturePointId(String featureId, int featureSeq)
	{
		this.featureId = featureId;
		this.featureSeq = featureSeq;
	}
	
	public FeaturePointId()
	{
		
	}
	
	public String getFeatureId()
	{
		return featureId;
	}

	public void setFeatureId(String featureId)
	{
		this.featureId = featureId;
	}

	public int getFeatureSeq()
	{
		return featureSeq;
	}

	public void setFeatureSeq(int featureSeq)
	{
		this.featureSeq = featureSeq;
	}
	
}
