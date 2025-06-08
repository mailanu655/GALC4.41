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
import com.honda.galc.visualoverview.client.event.DrawLayerEvent;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;
import com.honda.galc.visualoverview.shared.codec.DefectResultCodec;
import com.honda.galc.visualoverview.shared.codec.FeatureCodec;
import com.honda.galc.visualoverview.shared.codec.PrintAttributeFormatCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.honda.galc.visualoverview.shared.feature.PointFeature;

public class FeatureLayer extends Layer{

	@Override
	public void fetchLayerFeatures(final Layer layer, final boolean editableLayer, final HandlerManager eventBus) {
		
    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "FeatureDao/getFeaturesByLayerId?" + layer.getLayerId().trim());
    	resource.get().send(new JsonCallback() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(VisualizationConstants.WEB_SERVICE_LOCATION + "FeatureDao/getFeaturesByLayerId?" + layer.getLayerId().trim());
				Window.alert("Failed to retrieve feature data - FeatureLayer.fetchLayerFeatures");
				
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
				
				FeatureCodec codec = GWT.create(FeatureCodec.class);
				for ( int i = 0; i < result.size(); ++i ) {
					Feature feature = codec.decode( result.get( i ) );
					features.add(feature);
				}

		    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "PrintAttributeFormatDao/findAllByLayerId?" + layer.getLayerId().trim());
		    	resource.get().send(new JsonCallback() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert("PAF failure");
						
					}

					@Override
					public void onSuccess(Method method, JSONValue response) {
						final List<PrintAttributeFormat> attributes = new ArrayList<PrintAttributeFormat>();
						final Map<String, List<PrintAttributeFormat>> attributesMap = new HashMap<String, List<PrintAttributeFormat>>();
						
						PrintAttributeFormatCodec pafCodec = GWT.create(PrintAttributeFormatCodec.class);
						JSONArray result = response.isArray();
						for ( int i = 0; i < result.size(); ++i ) {
							attributes.add( pafCodec.decode( result.get( i ) ) );
						}
						
						
						for(Feature feature : features)
						{
							if(feature.getFeatureId() == "NEW")
								continue;
							for(PrintAttributeFormat attribute : attributes)
							{
								if(!attributesMap.containsKey(feature.getFeatureId()))
								{
									attributesMap.put(feature.getFeatureId(), new ArrayList<PrintAttributeFormat>());
								}
								if(feature.getFeatureType().trim() + "." + feature.getReferenceType().trim() == attribute.getId().getFormId().trim())
								{
									List<PrintAttributeFormat> tempList = attributesMap.get(feature.getFeatureId());
									tempList.add(attribute);
									attributesMap.put(feature.getFeatureId(), tempList);
								}
									
							}
						}
						
						eventBus.fireEvent(new DrawLayerEvent(layer, features, attributesMap, editableLayer));
					}		
		    		
		    	});
		        
				
			}
    		
    	});
		
	}

}
