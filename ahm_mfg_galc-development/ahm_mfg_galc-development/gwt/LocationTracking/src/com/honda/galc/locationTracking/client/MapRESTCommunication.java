package com.honda.galc.locationTracking.client;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.honda.galc.locationTracking.shared.Feature;
import com.honda.galc.locationTracking.shared.FeatureCodec;
import com.honda.galc.locationTracking.shared.LayerFeatures;
import com.honda.galc.locationTracking.shared.LayerFeaturesCodec;
import com.honda.galc.locationTracking.shared.LayerFeaturesId;


public class MapRESTCommunication {

	private final String WEB_SERVICE_LOCATION_TRACKING = "http://10.203.33.248:8888/proxy/";
		
	private DatabaseService databaseService;
	
	public MapRESTCommunication()
	{		
		databaseService = new DatabaseService();
	}
	
	public void getCbuLocations()
	{
		try
		{
	    	Resource resource = new Resource(WEB_SERVICE_LOCATION_TRACKING + "FeatureDao/getFeaturesByLayerId?" + "CBU");
	    	resource.get().send(new JsonCallback() {
	
				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert("Failed to retrieve feature data");
					
				}
	
				@Override
				public void onSuccess(Method method, JSONValue response) {
					FeatureCodec codec = GWT.create(FeatureCodec.class);
					JSONArray result = response.isArray();
					List<Feature> features = new ArrayList<Feature>( result.size() );
					
					
					for ( int i = 0; i < result.size(); ++i ) {
					   features.add( codec.decode( result.get( i ) ) );
					}
					
					databaseService.clearAllVINs();
					databaseService.saveVINLocations(features);
							
			        
					
				}
	    		
	    	});
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
		/*RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, WEB_SERVICE_LOCATION_TRACKING + "getCbuLocations.ws");

	    try {
	      Request request = builder.sendRequest(null, new RequestCallback() {
	        public void onError(Request request, Throwable exception) {
	        }

	        public void onResponseReceived(Request request, Response response) {
	          if (200 == response.getStatusCode()) {
	        	  List<Feature> prodLocs = new ArrayList();
	        	  prodLocs.addAll(deserializeFromJson(response.getText()).getFeatures());
	        	  databaseService.clearAllVINs();
	        	  databaseService.saveVINLocations(prodLocs);
	          } else {
	          }
	        }
	      });
	    } 
	    catch (RequestException e) {
	    }*/
	}
	
	/*private void doPost(String url, final String postData) {
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	    try {
	    	Request response = builder.sendRequest(postData, new RequestCallback() {
	        public void onError(Request request, Throwable exception) {
	        	FeatureLocation fLoc = AutoBeanCodex.decode(factory, FeatureLocation.class, postData).as();
	        	fLoc.setFeatureType("2");
	        	databaseService.saveVINLocation(fLoc);

	        }
	
	        public void onResponseReceived(Request request, Response response) {
	        	if (201 == response.getStatusCode()) {
		        	FeatureLocation fLoc = AutoBeanCodex.decode(factory, FeatureLocation.class, postData).as();
		        	databaseService.clearVIN(fLoc);
	        	}
	        	else
	        	{
		        	FeatureLocation fLoc = AutoBeanCodex.decode(factory, FeatureLocation.class, postData).as();
		        	fLoc.setFeatureType("2");
	        		databaseService.saveVINLocation(fLoc);
	        	}
	        }
	      });
	    } catch (RequestException e) {
	      Window.alert("Failed to send the request: " + e.getMessage());
	    }
	  }*/

	public void saveCbuLocation(final Feature featLoc) {
		Resource featureResource = new Resource(WEB_SERVICE_LOCATION_TRACKING + "FeatureDao/update");
		FeatureCodec featureCodec = GWT.create(FeatureCodec.class);
		featureResource.post().text("{\"com.honda.galc.entity.product.Feature\": " + featureCodec.encode(featLoc) + "}").send(new JsonCallback() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage() + " Feature Update");
				featLoc.getFeaturePoints().get(0).getId().setFeatureSeq(1);
	        	databaseService.saveVINLocation(featLoc);
				
			}

			@Override
			public void onSuccess(Method method, JSONValue response) {
				LayerFeatures layerFeatures = new LayerFeatures();
				LayerFeaturesId layerFeaturesId = new LayerFeaturesId();
				layerFeaturesId.setFeatureId(featLoc.getFeatureId());
				layerFeaturesId.setLayerId("CBU");
				layerFeatures.setId(layerFeaturesId);
				LayerFeaturesCodec layerFeaturesCodec = GWT.create(LayerFeaturesCodec.class);
				Resource layerFeaturesResource = new Resource(WEB_SERVICE_LOCATION_TRACKING + "LayerFeaturesDao/update");
				layerFeaturesResource.post().text("{\"com.honda.galc.entity.product.LayerFeatures\": " + layerFeaturesCodec.encode(layerFeatures) + "}").send(new JsonCallback() {

					@Override
					public void onFailure(Method method,
							Throwable exception) {
						Window.alert(exception.getMessage() + "saveLayer");
						featLoc.getFeaturePoints().get(0).getId().setFeatureSeq(1);
			        	databaseService.saveVINLocation(featLoc);			
					}

					@Override
					public void onSuccess(Method method,
							JSONValue response) {
						databaseService.clearVIN(featLoc);
					}
					
				});
			}
			
		});
	}
	
	
	
}
