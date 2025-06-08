package com.honda.galc.visualoverview.shared.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.Style;

import com.google.gwt.event.shared.HandlerManager;
import com.honda.galc.visualoverview.client.event.DrawFeatureEvent;
import com.honda.galc.visualoverview.client.widgets.FeatureStyle;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.FeaturePoints;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;

public class LabelFeature extends Feature {
	
	public LabelFeature()
	{
		
	}
	public LabelFeature(Feature feature)
	{
		super();
		setFeatureId(feature.getFeatureId());
		setEnableHistory(feature.getEnableHistory());
		setFeaturePoints(feature.getFeaturePoints());
		setFeatureType(feature.getFeatureType());
		setReferenceId(feature.getReferenceId());
		setReferenceType(feature.getReferenceType());
	}
	
	@Override
	public void draw(Map<String, List<PrintAttributeFormat>> attributeMap, Layer refLayer, HandlerManager eventBus, Vector layer) {

		VectorFeature vectorFeature = null;
		
		FeatureStyle style;
		if(!attributeMap.isEmpty())
    		style = new FeatureStyle(this, attributeMap.get(getFeatureId()));
		else
			style = new FeatureStyle(this, new ArrayList<PrintAttributeFormat>()); 
		for(FeaturePoints featurePoint : getFeaturePoints())
		{
			LonLat lonLat = new LonLat(featurePoint.getXCoordinate(), featurePoint.getYCoordinate());
			final Point point = new Point(lonLat.lon(), lonLat.lat());
			vectorFeature = new VectorFeature(point, style);
			vectorFeature.setFeatureId(getFeatureId());
		}
		
		eventBus.fireEvent(new DrawFeatureEvent(layer, vectorFeature)); 
	}
	

}
