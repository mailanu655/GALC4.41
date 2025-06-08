package com.honda.galc.visualoverview.shared;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.google.gwt.event.shared.HandlerManager;
import com.honda.galc.visualoverview.shared.layer.DefectLayer;
import com.honda.galc.visualoverview.shared.layer.DynamicLayer;
import com.honda.galc.visualoverview.shared.layer.FeatureLayer;
import com.honda.galc.visualoverview.shared.layer.ImageLayer;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="layerType")
@JsonSubTypes({@JsonSubTypes.Type(value=DefectLayer.class, name="DEFECT"),
	@JsonSubTypes.Type(value=ImageLayer.class, name="IMAGE"),
	@JsonSubTypes.Type(value=FeatureLayer.class, name="FEATURE"),
	@JsonSubTypes.Type(value=DynamicLayer.class, name="DYNAMIC")})
public abstract class Layer {
	
	private String layerId;
	private String layerType;
	private String layerDao;
	private int defaultZoom;
	private double boundaryLeft;
	private double boundaryRight;
	private double boundaryUpper;
	private double boundaryLower;
	
	public String getLayerId()
	{
		return this.layerId;
	}
	
	public void setLayerId(String layerId)
	{
		this.layerId = layerId;
	}
	
	public String getLayerType()
	{
		return this.layerType;
	}
	
	public void setLayerType(String layerType) 
	{
		this.layerType = layerType; 
	}
	
	public String getLayerDao()
	{
		return this.layerDao;
	}
	
	public void setLayerDao(String layerDao)
	{
		this.layerDao = layerDao;
	}
	
	public int getDefaultZoom()
	{
		return this.defaultZoom;
	}
	
	public void setDefaultZoom(int defaultZoom)
	{
		this.defaultZoom = defaultZoom;
	}
	
	public double getBoundaryLeft()
	{
		return this.boundaryLeft;
	}
	
	public void setBoundaryLeft(double boundaryLeft)
	{
		this.boundaryLeft = boundaryLeft;
	}
	
	public double getBoundaryRight()
	{
		return this.boundaryRight;
	}
	
	public void setBoundaryRight(double boundaryRight)
	{
		this.boundaryRight = boundaryRight;
	}
	
	public double getBoundaryUpper()
	{
		return this.boundaryUpper;
	}
	
	public void setBoundaryUpper(double boundaryUpper)
	{
		this.boundaryUpper = boundaryUpper;
	}
	
	public double getBoundaryLower()
	{
		return this.boundaryLower;
	}
	
	public void setBoundaryLower(double boundaryLower)
	{
		this.boundaryLower = boundaryLower;
	}
	
	public abstract void fetchLayerFeatures(final Layer layer, final boolean editableLayer, HandlerManager eventBus);
}
