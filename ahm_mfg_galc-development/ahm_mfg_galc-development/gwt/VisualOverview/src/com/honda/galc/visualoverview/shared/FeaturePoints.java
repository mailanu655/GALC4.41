package com.honda.galc.visualoverview.shared;

public class FeaturePoints {

	private FeaturePointsId id;
	private double xCoordinate;
	private double yCoordinate;
	private double zCoordinate;
	private String childFeatureId;
	private String spatialReferenceSystem;
	
	public FeaturePointsId getId()
	{
		return id;
	}

	public void setId(FeaturePointsId id)
	{
		this.id = id;
	}

	public double getXCoordinate()
	{
		return xCoordinate;
	}

	public void setXCoordinate(double xCoordinate)
	{
		this.xCoordinate = xCoordinate;
	}

	public double getYCoordinate()
	{
		return yCoordinate;
	}

	public void setYCoordinate(double yCoordinate)
	{
		this.yCoordinate = yCoordinate;
	}

	public double getZCoordinate()
	{
		return zCoordinate;
	}

	public void setZCoordinate(double zCoordinate)
	{
		this.zCoordinate = zCoordinate;
	}

	public String getChildFeatureId()
	{
		return childFeatureId;
	}

	public void setChildFeatureId(String childFeatureId)
	{
		this.childFeatureId = childFeatureId;
	}

	public String getSpatialReferenceSystem()
	{
		return spatialReferenceSystem;
	}

	public void setSpatialReferenceSystem(String spatialReferenceSystem)
	{
		this.spatialReferenceSystem = spatialReferenceSystem;
	}
	
}
