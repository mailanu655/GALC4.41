package com.honda.galc.locationTracking.shared;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Cody Getz
 * @date Jul 15, 2013 
 * 
 */

public class Feature {
	

	private String featureId;
	private String featureType;
	private String referenceType;
	private String referenceId;
	private int enableHistory;
	List<FeaturePoint> featurePoints = new ArrayList<FeaturePoint>();
	
	public String getFeatureId()
	{
		return this.featureId;
	}
	
	public void setFeatureId(String featureId)
	{
		this.featureId = featureId;
	}
	
	public String getFeatureType()
	{
		return this.featureType;
	}
	
	public void setFeatureType(String featureType) 
	{
		this.featureType = featureType; 
	}
	
	public String getReferenceType()
	{
		return this.referenceType;
	}
	
	public void setReferenceType(String referenceType)
	{
		this.referenceType = referenceType;
	}
	
	public String getReferenceId()
	{
		return this.referenceId;
	}
	
	public void setReferenceId(String referenceId)
	{
		this.referenceId = referenceId;
	}
	
	public int getEnableHistory()
	{
		return this.enableHistory;
	}
	
	public void setEnableHistory(int enableHistory)
	{
		this.enableHistory = enableHistory;
	}
	
	public List<FeaturePoint> getFeaturePoints()
	{
		return this.featurePoints;
	}
	
	public void setFeaturePoints(List<FeaturePoint> featurePoints)
	{
		this.featurePoints = featurePoints;
	}

	
}