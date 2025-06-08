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

public class ImageFeature extends Feature {

	@Override
	public void draw(
			final Map<String, List<PrintAttributeFormat>> attributeMap, Layer refLayer, final HandlerManager eventBus, final Vector layer) {


		final VectorFeature vectorFeature = null;

		final FeatureStyle style;
		if(!attributeMap.isEmpty())
    		style = new FeatureStyle(this, attributeMap.get(getFeatureId()));
		else
			style = new FeatureStyle(this, new ArrayList<PrintAttributeFormat>());
		
		for(FeaturePoints featurePoint : getFeaturePoints())
		{	
			LonLat lonLat = new LonLat(featurePoint.getXCoordinate(), featurePoint.getYCoordinate());
			final Point point = new Point(lonLat.lon(), lonLat.lat());


		    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "ImageDao/getBase64ByImageName?");
		    	resource.get().send(new JsonCallback() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Method method, JSONValue response) {
						
				        String image = "data:image/jpeg;base64,"+ response.toString().replace("\\n", "");
				        image = image.replace("\"", "");
				        image = image.replace("\\r", "");
				        image = image.replace("=", "");
				        style.setExternalGraphic(image);
				        style.setGraphicSize(500, 500);
						style.setGraphicOpacity(1);
						VectorFeature pointFeature = new VectorFeature(point, style);
						pointFeature.setFeatureId(getFeatureId());
						Attributes attributes = new Attributes();
						attributes.setAttribute("featureSeq", 0);
						pointFeature.setAttributes(attributes);
						eventBus.fireEvent(new DrawFeatureEvent(layer, vectorFeature)); 
					}
		    		
		    	});		
			}

	}

}
