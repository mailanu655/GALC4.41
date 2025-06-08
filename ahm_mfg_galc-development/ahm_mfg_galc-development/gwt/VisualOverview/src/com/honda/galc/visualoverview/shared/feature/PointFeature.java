package com.honda.galc.visualoverview.shared.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.util.Attributes;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONValue;
import com.honda.galc.visualoverview.client.event.DrawFeatureEvent;
import com.honda.galc.visualoverview.client.widgets.FeatureStyle;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.FeatureInterface;
import com.honda.galc.visualoverview.shared.FeaturePoints;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;

public class PointFeature extends Feature {

	@Override
	public void draw(
			Map<String, List<PrintAttributeFormat>> attributeMap, Layer refLayer, HandlerManager eventBus, Vector layer) {


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
