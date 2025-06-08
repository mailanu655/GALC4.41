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
import com.honda.galc.visualoverview.client.widgets.WaitDialog;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;
import com.honda.galc.visualoverview.shared.codec.DefectResultCodec;
import com.honda.galc.visualoverview.shared.codec.FeatureCodec;
import com.honda.galc.visualoverview.shared.codec.PrintAttributeFormatCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.honda.galc.visualoverview.shared.feature.PointFeature;

public class DefectLayer extends Layer{

	@Override
	public void fetchLayerFeatures(final Layer layer, final boolean editableLayer, final HandlerManager eventBus) {
		final Map<String, List<PrintAttributeFormat>> attributesMap = new HashMap<String, List<PrintAttributeFormat>>();
    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "PrintAttributeFormatDao/findDefectPrintAttributes");
    	resource.get().send(new JsonCallback() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert("PAF failure");
				
			}

			@Override
			public void onSuccess(Method method, JSONValue response) {
				Window.alert("Drawing Layer");
				final List<PrintAttributeFormat> attributes = new ArrayList<PrintAttributeFormat>();
				final Map<String, List<PrintAttributeFormat>> attributesMap = new HashMap<String, List<PrintAttributeFormat>>();
				
				PrintAttributeFormatCodec pafCodec = GWT.create(PrintAttributeFormatCodec.class);
				JSONArray result = response.isArray();
				for ( int i = 0; i < result.size(); ++i ) {
					attributes.add( pafCodec.decode( result.get( i ) ) );
				}
				
				
		    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + layer.getLayerDao().trim());
		    	resource.get().send(new JsonCallback() {
		
					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert("Failed to retrieve feature data - DefectLayer.fetchLayerFeatures");
						
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
						
						DefectResultCodec codec = GWT.create(DefectResultCodec.class);
						for ( int i = 0; i < result.size(); ++i ) {
							Feature feature = codec.decode( result.get( i ) ).toFeature();
							feature.setAttributes(attributes);
							features.add(feature);
						}
						
						eventBus.fireEvent(new DrawLayerEvent(layer, features, attributesMap, editableLayer));
								        
					}
		    		
		    	});
    	
			}		
    		
    	});
    	
	}

}

//for(Feature feature : features)
//{
//	if(feature.getFeatureId() == "NEW")
//		continue;
//	for(PrintAttributeFormat attribute : attributes)
//	{
//		if(!attributesMap.containsKey(feature.getReferenceType()))
//		{
//			attributesMap.put(feature.getReferenceType(), new ArrayList<PrintAttributeFormat>());
//		}
//		if(feature.getFeatureType().trim() + "." + feature.getReferenceId().trim() == attribute.getId().getFormId().trim())
//		{
//			List<PrintAttributeFormat> tempList = attributesMap.get(feature.getReferenceType());
//			tempList.add(attribute);
//			attributesMap.put(feature.getReferenceType(), tempList);
//		}
//			
//	}
//}
