package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Cody Getz
 * @date Jul 15, 2013 
 * 
 */
@Entity
@Table(name="LAYER_TBX")
public class Layer extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LAYER_ID")
	private String layerId;
	
	@Column(name="LAYER_TYPE")
	private String layerType;

	@Column(name="LAYER_DAO")
	private String layerDao;
	
	@Column(name="DEFAULT_ZOOM")
	private int defaultZoom;
	
	@Column(name="BOUNDARY_LEFT")
	private double boundaryLeft;
	
	@Column(name="BOUNDARY_RIGHT")
	private double boundaryRight;
	
	@Column(name="BOUNDARY_UPPER")
	private double boundaryUpper;
	
	@Column(name="BOUNDARY_LOWER")
	private double boundaryLower;

	public Layer() {
		super();
	}
	
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

	public Object getId() {
		return getLayerId();
	}
	

}