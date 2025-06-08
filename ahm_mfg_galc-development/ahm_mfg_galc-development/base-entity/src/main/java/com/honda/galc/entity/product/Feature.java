package com.honda.galc.entity.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.conf.PrintAttributeFormat;

/**
 * @author Cody Getz
 * @date Jul 15, 2013 
 * 
 */
@Entity
@Table(name="FEATURE_TBX")
public class Feature extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FEATURE_ID")
	private String featureId;
	
	@Column(name="FEATURE_TYPE")
	private String featureType;

	@Column(name="REFERENCE_TYPE")
	private String referenceType;
	
	@Column(name="REFERENCE_ID")
	private String referenceId;
	
	@Column(name="ENABLE_HISTORY")
	private int enableHistory;
	
	@OneToMany(targetEntity = FeaturePoint.class,mappedBy = "feature",cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    @OrderBy
    private List<FeaturePoint> featurePoints = new ArrayList<FeaturePoint>();
	
	private List<PrintAttributeFormat> attributes = new ArrayList<PrintAttributeFormat>();

	public Feature() {
		super();
	}
	
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
	
	public List<PrintAttributeFormat> getAttributes()
	{
		return this.attributes;
	}
	
	public void setAttributes(List<PrintAttributeFormat> attributes)
	{
		this.attributes = attributes;
	}

	public Object getId() {
		return getFeatureId();
	}
	
}