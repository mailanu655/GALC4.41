package com.honda.galc.visualoverview.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.event.FeatureHighlightedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.ImageOptions;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.event.ConfigEvent;
import com.honda.galc.visualoverview.client.event.DrawLayerEvent;
import com.honda.galc.visualoverview.client.event.EditFeatureEvent;
import com.honda.galc.visualoverview.client.event.SaveFeaturesEvent;
import com.honda.galc.visualoverview.client.view.MapView;
import com.honda.galc.visualoverview.client.widgets.WaitDialog;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.FeaturePoints;
import com.honda.galc.visualoverview.shared.FeaturePointsId;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.LayerFeatures;
import com.honda.galc.visualoverview.shared.LayerFeaturesId;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;
import com.honda.galc.visualoverview.shared.codec.DefectResultCodec;
import com.honda.galc.visualoverview.shared.codec.FeatureCodec;
import com.honda.galc.visualoverview.shared.codec.FeaturePointsCodec;
import com.honda.galc.visualoverview.shared.codec.LayerFeaturesCodec;
import com.honda.galc.visualoverview.shared.codec.PrintAttributeFormatCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.honda.galc.visualoverview.shared.feature.*;

public class MapPresenter implements Presenter {

	private String editLayer;
	private boolean handlerBound = false;

    public interface Display {
	    Image getConfigButton();
	    Image getSaveButton();
	    Image getAddButton();
	    Image getMoveButton();
	    Image getPointsButton();
	    Image getResizeButton();
	    Image getRotateButton();
	    Image getDeleteButton();
	    String getMapProjection();
	    DragFeature getDragFeature();
	    ModifyFeature getModifyFeature();
	    SelectFeature getDeleteFeature();
	    LonLat getLonLatFromPixel(Pixel pix);
	    VectorFeature[] getLayerFeatures(String layerId);
	    public void drawLayer(Vector layer, String layerId, int index);
	    void clearMap();
	    void removeLayer(String layerId);
	    void setEditable(Vector layer);
	    void removeZoom();
	    void showZoom();
	    void addImageLayer(org.gwtopenmaps.openlayers.client.layer.Image imageLayer);
	    Widget asWidget();
	}
    
    private final HandlerManager eventBus;
    private final Display display;
    
    public MapPresenter(HandlerManager eventBus, Display view)
    {
    	this.eventBus = eventBus;
    	this.display = view;
    }
    
	public void bind()
	{
		handlerBound = true;
		display.getConfigButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ConfigEvent());
			}
			
		});
		
		display.getAddButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditFeatureEvent(editLayer));
			}
			
		});
		
		display.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SaveFeaturesEvent(""));
				
			}
			
		});
		
		display.getMoveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.getDeleteFeature().deactivate();
				display.getModifyFeature().deactivate();
				display.getDragFeature().activate();
			}
			
		});
		
		
		display.getPointsButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				display.getDeleteFeature().deactivate();
				display.getDragFeature().deactivate();
				display.getModifyFeature().deactivate();
				display.getModifyFeature().setMode(ModifyFeature.RESHAPE);
				display.getModifyFeature().activate();
				}
				
			
			});
		
		
		display.getResizeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.getDeleteFeature().deactivate();
				display.getDragFeature().deactivate();
				display.getModifyFeature().deactivate();
				display.getModifyFeature().setMode(ModifyFeature.RESIZE);
				display.getModifyFeature().activate();
				
			}
			
		});
		
		display.getRotateButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.getDeleteFeature().deactivate();
				display.getDragFeature().deactivate();
				display.getModifyFeature().deactivate();
				display.getModifyFeature().setMode(ModifyFeature.ROTATE);
				display.getModifyFeature().activate();
				
			}
			
		});
		
		display.getDeleteButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				display.getDragFeature().deactivate();
				display.getModifyFeature().deactivate();
				display.getDeleteFeature().activate();
				
				display.getDeleteFeature().addFeatureHighlightedListener(new FeatureHighlightedListener() {
					@Override
					public void onFeatureHighlighted(VectorFeature vectorFeature) {
						deleteFeature(vectorFeature);
						vectorFeature.destroy();
						
					}
				});
				
			}
			
		});
		

		
	}
	
	public void deleteFeature(final VectorFeature feature)
	{
		try
		{
			Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "FeatureDao/remove");
			final Feature newFeature = new PointFeature();
			newFeature.setEnableHistory(0);
			newFeature.setFeatureId(feature.getFeatureId());
			newFeature.setFeatureType(feature.getAttributes().getAttributeAsString("FEATURE_TYPE"));
			newFeature.setReferenceId(feature.getAttributes().getAttributeAsString("REFERENCE_ID"));
			newFeature.setReferenceType("");
			List<FeaturePoints> featurePoints = new ArrayList<FeaturePoints>();
    		Point[] points;
    		int index = 0;
    		if(feature.getAttributes().getAttributeAsString("FEATURE_TYPE").equals("LINE"))
    			points = getLinePoints(feature.getGeometry());
    		else
    			points = feature.getGeometry().getVertices(false);
     		for(Point fp : points)
    		{
				FeaturePointsId pointsId = new FeaturePointsId();
				pointsId.setFeatureId(feature.getFeatureId());
				pointsId.setFeatureSeq(index);
				index++;
				FeaturePoints point = new FeaturePoints();
				point.setId(pointsId);
				point.setXCoordinate(fp.getX());
				point.setYCoordinate(fp.getY());
				point.setChildFeatureId("");
				point.setSpatialReferenceSystem("CUSTOM_SRS");
				
				featurePoints.add(point);
    		}
			newFeature.setFeaturePoints(featurePoints);
			FeatureCodec featureCodec = GWT.create(FeatureCodec.class);
	    	resource.post().text("[{\"com.honda.galc.entity.product.Feature\": " + featureCodec.encode(newFeature) + "}]").send(new JsonCallback() {
	
				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert("Failed to remove feature data");
					Window.alert(exception.getMessage());
					
				}
	
				@Override
				public void onSuccess(Method method, JSONValue response) {
			        
					
				}
	    		
	    	});
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
	}
	
	public void saveLayer(final String layerId)
	{
		Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "FeaturePointDao/update");
    	for(VectorFeature feature : display.getLayerFeatures(layerId))
    	{
    		int index = 0;
    		if(feature.getAttributes().getAttributeAsBoolean("NEW"))
    		{
    			Resource featureResource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "FeatureDao/update");
    			final Feature newFeature = new PointFeature();
    			newFeature.setEnableHistory(0);
    			newFeature.setFeatureId(feature.getFeatureId());
    			newFeature.setFeatureType(feature.getAttributes().getAttributeAsString("FEATURE_TYPE"));
    			newFeature.setReferenceId(feature.getAttributes().getAttributeAsString("REFERENCE_ID"));
    			newFeature.setReferenceType("");
    			newFeature.setFeaturePoints(new ArrayList<FeaturePoints>());
    			FeatureCodec featureCodec = GWT.create(FeatureCodec.class);
    			featureResource.post().text("[{\"com.honda.galc.entity.product.Feature\": " + featureCodec.encode(newFeature) + "}]").send(new JsonCallback() {

    				@Override
    				public void onFailure(Method method, Throwable exception) {
    					Window.alert(exception.getMessage() + " Feature Update");
    					
    				}

    				@Override
    				public void onSuccess(Method method, JSONValue response) {
    					LayerFeatures layerFeatures = new LayerFeatures();
    					LayerFeaturesId layerFeaturesId = new LayerFeaturesId();
    					layerFeaturesId.setFeatureId(newFeature.getFeatureId());
    					layerFeaturesId.setLayerId(layerId);
    					layerFeatures.setId(layerFeaturesId);
    					LayerFeaturesCodec layerFeaturesCodec = GWT.create(LayerFeaturesCodec.class);
    					Resource layerFeaturesResource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "LayerFeaturesDao/update");
    					layerFeaturesResource.post().text("[{\"com.honda.galc.entity.product.LayerFeatures\": " + layerFeaturesCodec.encode(layerFeatures) + "}]").send(new JsonCallback() {

    						@Override
    						public void onFailure(Method method,
    								Throwable exception) {
    							Window.alert(exception.getMessage() + "saveLayer");
    							
    						}

    						@Override
    						public void onSuccess(Method method,
    								JSONValue response) {
    							
    						}
    						
    					});
    				}
        			
        		});
    		}
    		Point[] points;
    		if(feature.getAttributes().getAttributeAsString("FEATURE_TYPE").equals("LINE"))
    			points = getLinePoints(feature.getGeometry());
    		else
    			points = feature.getGeometry().getVertices(false);
     		for(Point point : points)
    		{
     			if(feature.getFeatureId().contains("OpenLayers.Feature"))
     				continue;
     			
	    		FeaturePoints featurePoint = new FeaturePoints();
	    		FeaturePointsId id = new FeaturePointsId();
	    		featurePoint.setId(id);
	    		featurePoint.getId().setFeatureId(feature.getFeatureId());
	    		//Window.alert(Integer.toString(feature.getAttributes().getAttributeAsInt("featureSeq")));
	    		featurePoint.getId().setFeatureSeq(index);
	    		index++;
	    		featurePoint.setXCoordinate(point.getX());
	    		featurePoint.setYCoordinate(point.getY());
	    		featurePoint.setZCoordinate(0);
	    		featurePoint.setSpatialReferenceSystem("CUSTOM_SRS");
	    		featurePoint.setChildFeatureId("");
	    		FeaturePointsCodec codec = GWT.create(FeaturePointsCodec.class);
	    		resource.post().text("[{\"com.honda.galc.entity.product.FeaturePoint\": " + codec.encode(featurePoint) + "}]").send(new JsonCallback() {
	
					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert(exception.getMessage() + "save points");
						
					}
	
					@Override
					public void onSuccess(Method method, JSONValue response) {
						
					}
	    			
	    		});
    		}
     		display.getSaveButton().setVisible(false);
    	}

	}
	
	public Point[] getLinePoints(Geometry geometry)
	{
   		Point[] endPoints = geometry.getVertices(true);
		Point[] nonEndPoints = geometry.getVertices(false);
		Point[] linePoints = new Point[endPoints.length + nonEndPoints.length];
		System.arraycopy(endPoints, 0, linePoints, 0, 1);
		System.arraycopy(nonEndPoints, 0, linePoints, 1, nonEndPoints.length);
		System.arraycopy(endPoints, 1, linePoints, nonEndPoints.length+1, 1);
		return linePoints;
	}
	
	public void fetchLayerFeatures(final Layer layer, final boolean editableLayer)
	{
			if(editableLayer)
				editLayer = layer.getLayerId();
			else
				editLayer = "";
			
			layer.fetchLayerFeatures(layer, editableLayer, eventBus);
	}
	
	public void drawLayer(Vector layer, String layerId, boolean editable, int index)
	{	
		display.drawLayer(layer, layerId, index);
		
		if(editable)
		{
			display.setEditable(layer);
		}
     
	}
    
	public void go(HasWidgets container) {
		if(!handlerBound)
			bind();
		
	    container.clear();
	    container.add(display.asWidget());
	}
	

}
