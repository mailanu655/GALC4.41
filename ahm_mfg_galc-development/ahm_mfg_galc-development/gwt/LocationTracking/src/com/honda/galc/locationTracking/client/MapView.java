package com.honda.galc.locationTracking.client;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.Navigation;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureUnselectedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener.FeatureSelectedEvent;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureUnselectedListener.FeatureUnselectedEvent;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.EmptyLayer;
import org.gwtopenmaps.openlayers.client.layer.Image;
import org.gwtopenmaps.openlayers.client.layer.ImageOptions;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.popup.Popup;
import org.gwtopenmaps.openlayers.client.util.JObjectArray;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.honda.galc.locationTracking.shared.Feature;
import com.honda.galc.locationTracking.shared.FeatureCodec;
import com.honda.galc.locationTracking.shared.FeaturePoint;
import com.honda.galc.locationTracking.shared.ImageCodec;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.NavStack;

public class MapView extends ScrollablePanel {
	
	private Map map;
	private MapWidget mapWidget;
	
	
	private Vector userLayer;
	private Vector carLayer;
	
	private LonLat initLonLat = new LonLat(-85.539, 39.3674);
	private LonLat userLonLat;
	
	private static final Projection DEFAULT_PROJECTION = new Projection(             "EPSG:4326");

	public MapView()
	{
		super("Map");
		
		initializeMap();
		
		initializeGeoLocation();
		
	}
	
	public void initializeMap()
	{
		//create some MapOptions
		MapOptions defaultMapOptions = new MapOptions();  
		defaultMapOptions.setNumZoomLevels(18);
		defaultMapOptions.removeDefaultControls();
		defaultMapOptions.setMaxResolutionToAuto();
		Navigation nv = new Navigation();
		defaultMapOptions.setControls(new JObjectArray(new JSObject[]{nv.getJSObject()}));
	
		
		//Create a MapWidget and add an empty layer...we don't want to pull from an external website.   
		//Set to the width of the window.
		mapWidget = new MapWidget(Integer.toString(Window.getClientWidth()) + "px", Integer.toString(Window.getClientHeight()) + "px", defaultMapOptions);          
		map = mapWidget.getMap(); 
		map.addControl(new Navigation());

        
		
		//Set the lower left and upper right coordinates
		//of our overlay image and transform those to our CRS
		final Bounds extent = new Bounds(-85.55415, 39.3607, -85.52248764038544, 39.37381);
        extent.transform(DEFAULT_PROJECTION, new Projection(map.getProjection()));         
        
		
        //Create an empty layer as our base layer.
		EmptyLayer.Options emptyLayerOptions = new EmptyLayer.Options();               
		emptyLayerOptions.setIsBaseLayer(true);          
		EmptyLayer emptyLayer = new EmptyLayer("Empty layer", emptyLayerOptions);         
				
		//Create and add the image overlay.
        ImageOptions imageOptions = new ImageOptions();         
        imageOptions.setNumZoomLevels(20);   
        imageOptions.setLayerOpacity(1);
        imageOptions.setDisplayOutsideMaxExtent(true);
        imageOptions.setAlwaysInRange(true);
        Image imageLayer = new Image("Image Layer", GWT.getModuleBaseURL() + "binghonda.JPG", extent, new Size(500,500), imageOptions);         
        imageLayer.setIsBaseLayer(false);  
        
        //Initialize our layers.
		carLayer = new Vector("Car Layer");
		userLayer = new Vector("User layer");
		
        map.addLayer(userLayer);
        map.addLayer(emptyLayer);
        map.addLayer(imageLayer);
        
        
             
		//Center and zoom to a location in case we don't have our location.               
		initLonLat.transform(DEFAULT_PROJECTION.getProjectionCode(), map.getProjection());         
		map.setCenter(initLonLat, 15);  
		userLayer.setZIndex(500);
		
        //Prevents a small white margin from appearing
        mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0); 
        //RootPanel.get().add(mapWidget);	
        addMember(mapWidget);
	}
	
	private void initializeGeoLocation()
	{
	     // Start GeoLocation stuff (note that the GeoLocation is just plain GWT stuff)                 
		Geolocation geoLocation = Geolocation.getIfSupported();                 
		if (geoLocation == null) {                     
		}
		else {                     
			final Geolocation.PositionOptions geoOptions = new Geolocation.PositionOptions();                     
			geoOptions.setHighAccuracyEnabled(true);
			geoLocation.watchPosition(new Callback<Position, PositionError>() {                         
				public void onFailure(final PositionError reason) {                             
					}                           
				public void onSuccess(final Position result) {                             
					// put the received result in an openlayers LonLat                             
					// object                             
					userLonLat = new LonLat(                                     
							result.getCoordinates().getLongitude(),                                     
							result.getCoordinates().getLatitude());                             
					userLonLat.transform(                                     
							DEFAULT_PROJECTION.getProjectionCode(),                                     
							map.getProjection()); 
					// transform lonlat to OSM coordinate system                             
					// Center the map on the received location
					map.setCenter(userLonLat);                               
					// lets create a vector point on the location                            
					Style pointStyle = new Style();                             
					pointStyle.setFillColor("red");                             
					pointStyle.setStrokeColor("green");                             
					pointStyle.setStrokeWidth(2);                             
					pointStyle.setFillOpacity(0.9);
			                        
					final Point point = new Point(                                     
							result.getCoordinates().getLongitude(),                                     
							result.getCoordinates().getLatitude());   
					point.transform(DEFAULT_PROJECTION,                                             
							new Projection(map.getProjection())); 
					// transform point to OSM coordinate system                             
					final VectorFeature pointFeature = new VectorFeature(                                     
							point, pointStyle); 
					userLayer.destroyFeatures();                             
					userLayer.addFeature(pointFeature);                         
					}
				}, geoOptions);}
	}
	
	/*
	 * Grab all the VINs from the client-side database and display them on a layer.
	 */
	public void displayAllVINs(List<Feature> features)
	{	
		if(carLayer != null)
			carLayer.destroyFeatures();
		
		carLayer = new Vector("Car Layer");
		for(Feature feature : features)
		{
			Style pointStyle = new Style();                             
			pointStyle.setFillColor("white");                             
			pointStyle.setStrokeColor("black");                             
			pointStyle.setStrokeWidth(2);                             
			pointStyle.setFillOpacity(0.9);
			final Point point = new Point(                                     
					feature.getFeaturePoints().get(0).getXCoordinate(), feature.getFeaturePoints().get(0).getYCoordinate());   
			point.transform(DEFAULT_PROJECTION,                                             
					new Projection(map.getProjection())); 
			// transform point to OSM coordinate system     
			VectorFeature pointFeature = new VectorFeature(                                     
					point, pointStyle);
			pointFeature.setFeatureId(feature.getFeatureId());
			//Attach a popup to the point, we use null as size cause we set autoSize to true                 
			//Note that we use FramedCloud... This extends a normal popup and creates is styled as a balloon
			Popup popup = new Popup(feature.getFeatureId(), new LonLat(point.getX(), point.getY()), null, "VIN: " + feature.getFeatureId() + "<br/>And more text", false);
			popup.setBackgroundColor("#FFFFFF");
			popup.setOpacity(1);
			popup.setPanMapIfOutOfView(true); //this set the popup in a strategic way, and pans the map if needed.                 
			popup.setAutoSize(true);
			pointFeature.setPopup(popup);
			
	       	carLayer.addFeature(pointFeature);
		}
		SelectFeature selectFeature = new SelectFeature(carLayer);  
		selectFeature.setAutoActivate(true);
		map.addControl(selectFeature);
		//Secondly add a VectorFeatureSelectedListener to the feature         
		carLayer.addVectorFeatureSelectedListener(new VectorFeatureSelectedListener() 
		{             
			public void onFeatureSelected(FeatureSelectedEvent eventObject) {                                  

				//And attach the popup to the map                  
				map.addPopup(eventObject.getVectorFeature().getPopup());
				            
				}         
		});           
		
		//And add a VectorFeatureUnselectedListener which removes the popup.         
		carLayer.addVectorFeatureUnselectedListener(new VectorFeatureUnselectedListener()         
		{             
			public void onFeatureUnselected(FeatureUnselectedEvent eventObject)             
			{                                 
				map.removePopup(eventObject.getVectorFeature().getPopup());                            
				}         
		}); 
		map.addLayer(carLayer);
	}
          
}
