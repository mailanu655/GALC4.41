package com.honda.galc.visualoverview.shared.layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.layer.ImageOptions;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.honda.galc.visualoverview.client.event.DrawImageLayerEvent;
import com.honda.galc.visualoverview.client.event.DrawLayerEvent;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;
import com.honda.galc.visualoverview.shared.codec.DefectResultCodec;
import com.honda.galc.visualoverview.shared.codec.FeatureCodec;
import com.honda.galc.visualoverview.shared.codec.PrintAttributeFormatCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.honda.galc.visualoverview.shared.feature.PointFeature;

public class ImageLayer extends Layer{

	@Override
	public void fetchLayerFeatures(final Layer layer, final boolean editableLayer, final HandlerManager eventBus) {		

    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + layer.getLayerDao().trim());
    	resource.get().send(new JsonCallback() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert("Failed to retrieve feature data");
				
			}

			@Override
			public void onSuccess(Method method, JSONValue response) {
				JSONArray result = response.isArray();
				List<Feature> initFeatures = new ArrayList<Feature>(1);
				if(result != null)
					initFeatures = new ArrayList<Feature>(result.size());
				
				final List<Feature> features = initFeatures;
				
				
				Feature newFeature = new PointFeature();
				newFeature.setFeatureId("NEW");
				features.add(newFeature);
				
		        ImageOptions imageOptions = new ImageOptions();         
		        imageOptions.setNumZoomLevels(16);   
		        imageOptions.setLayerOpacity(1);
		        imageOptions.setDisplayOutsideMaxExtent(true);
		        imageOptions.setAlwaysInRange(true);
		        String image = "data:image/jpeg;base64,"+ response.toString().replace("\\n", "");
		        image = image.replace("\"", "");
		        image = image.replace("\\r", "");
		        image = image.replace("=", "");
		        Bounds extent = new Bounds(layer.getBoundaryLeft(), layer.getBoundaryLower(), layer.getBoundaryRight(), layer.getBoundaryUpper()) ;
		        org.gwtopenmaps.openlayers.client.layer.Image imageLayer = new org.gwtopenmaps.openlayers.client.layer.Image(layer.getLayerId(), image, extent, new Size(500,500), imageOptions);         
		        imageLayer.setIsBaseLayer(false);
		        eventBus.fireEvent(new DrawImageLayerEvent(imageLayer));				
			}
    		
    	});
		
	}

}
