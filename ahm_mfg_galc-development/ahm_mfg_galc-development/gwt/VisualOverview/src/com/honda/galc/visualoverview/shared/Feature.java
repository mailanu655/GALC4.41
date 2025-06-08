package com.honda.galc.visualoverview.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.honda.galc.visualoverview.client.view.MapView;
import com.honda.galc.visualoverview.shared.feature.AreaFeature;
import com.honda.galc.visualoverview.shared.feature.DefectFeature;
import com.honda.galc.visualoverview.shared.feature.ImageFeature;
import com.honda.galc.visualoverview.shared.feature.LabelFeature;
import com.honda.galc.visualoverview.shared.feature.LineFeature;
import com.honda.galc.visualoverview.shared.feature.PointFeature;


/**
 * @author Cody Getz
 * @date Jul 15, 2013 
 * 
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="featureType")
@JsonSubTypes({@JsonSubTypes.Type(value=LabelFeature.class, name="LABEL"),
	@JsonSubTypes.Type(value=PointFeature.class, name="POINT"),
	@JsonSubTypes.Type(value=AreaFeature.class, name="AREA"),
	@JsonSubTypes.Type(value=LineFeature.class, name="LINE"),
	@JsonSubTypes.Type(value=ImageFeature.class, name="IMAGE"),
	@JsonSubTypes.Type(value=DefectFeature.class, name="DEFECT")})
public abstract class Feature {
	
	private String featureId;
	private String featureType;
	private String referenceType;
	private String referenceId;
	private int enableHistory;
	List<FeaturePoints> featurePoints = new ArrayList<FeaturePoints>();
	List<PrintAttributeFormat> attributes = new ArrayList<PrintAttributeFormat>();
	
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
	
	public List<FeaturePoints> getFeaturePoints()
	{
		return this.featurePoints;
	}
	
	public void setFeaturePoints(List<FeaturePoints> featurePoints)
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

	public abstract void draw(final Map<String, List<PrintAttributeFormat>> attributeMap, Layer refLayer, HandlerManager eventBus, Vector layer);

	
}