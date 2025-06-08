package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Cody Getz
 * @date Jul 15, 2013 
 * 
 */
@Entity
@Table(name="FEATURE_POINTS_TBX")
public class FeaturePoint extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FeaturePointId id;
	
	@Column(name="X_COORDINATE")
	private double xCoordinate;

	@Column(name="Y_COORDINATE")
	private double yCoordinate;
	
	@Column(name="Z_COORDINATE")
	private double zCoordinate;
	
	@Column(name="CHILD_FEATURE_ID")
	private String childFeatureId;
	
	@Column(name="SPATIAL_REFERENCE_SYSTEM")
	private String spatialReferenceSystem;
	
	@SuppressWarnings("unused")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "FEATURE_ID", referencedColumnName = "FEATURE_ID",
                    unique = true, nullable = false, insertable = false, updatable = false)
    })
	private Feature feature;

	public FeaturePoint() {
		super();
	}
	
	public FeaturePoint(String featureId, int featureSeq)
	{
		super();
		id = new FeaturePointId(featureId, featureSeq);
	}
	
	public FeaturePoint(FeaturePointId id)
	{
		super();
		this.id = id;
	}
	
	public FeaturePointId getId()
	{
		return this.id;
	}
	
	public void setId(FeaturePointId id)
	{
		this.id = id;
	}
	
	public double getXCoordinate()
	{
		return this.xCoordinate;
	}
	
	public void setXCoordinate(double xCoordinate)
	{
		this.xCoordinate = xCoordinate;
	}
	
	public double getYCoordinate()
	{
		return this.yCoordinate;
	}
	
	public void setYCoordinate(double yCoordinate)
	{
		this.yCoordinate = yCoordinate;
	}
	
	public double getZCoordinate()
	{
		return this.zCoordinate;
	}
	
	public void setZCoordinate(double zCoordinate)
	{
		this.zCoordinate = zCoordinate;
	}
	
	public String getChildFeatureId()
	{
		return this.childFeatureId;
	}
	
	public void setChildFeatureId(String childFeatureId)
	{
		this.childFeatureId = childFeatureId;
	}
	
	public String getSpatialReferenceSystem()
	{
		return this.spatialReferenceSystem;
	}
	
	public void setSpatialReferenceSystem(String spatialReferenceSystem)
	{
		this.spatialReferenceSystem = spatialReferenceSystem;
	}
	

}