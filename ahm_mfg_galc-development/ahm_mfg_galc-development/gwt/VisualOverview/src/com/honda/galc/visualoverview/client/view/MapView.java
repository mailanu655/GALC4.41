package com.honda.galc.visualoverview.client.view;

import java.util.HashMap;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.ZIndexBase;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.control.DragFeature.DragFeatureListener;
import org.gwtopenmaps.openlayers.client.control.DragFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature.OnModificationListener;
import org.gwtopenmaps.openlayers.client.control.ModifyFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.Navigation;
import org.gwtopenmaps.openlayers.client.control.PanZoom;
import org.gwtopenmaps.openlayers.client.control.PanZoomBar;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.Snapping;
import org.gwtopenmaps.openlayers.client.control.Zoom;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.handler.PathHandler;
import org.gwtopenmaps.openlayers.client.handler.PointHandler;
import org.gwtopenmaps.openlayers.client.handler.PolygonHandler;
import org.gwtopenmaps.openlayers.client.layer.EmptyLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.RendererOptions;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.presenter.MapPresenter;
import com.honda.galc.visualoverview.client.widgets.MapViewMenu;
import com.honda.galc.visualoverview.client.widgets.WaitDialog;
import com.honda.galc.visualoverview.shared.Feature;

public class MapView extends HTMLPanel implements MapPresenter.Display {

	private final MapViewMenu mapViewMenu;
    private Map map;
	private MapWidget mapWidget;
	private Zoom zoom;
	private Navigation nav;
	private DrawFeature drawPointFeatureControl = null;
	private DrawFeature drawLineFeatureControl = null;
	private DrawFeature drawPolygonFeatureControl = null;
	private DragFeature dragFeature = null;
	private Snapping snapControl = null;
	private ModifyFeature modifyFeature = null;
	private SelectFeature deleteFeature = null;
	private java.util.Map<String, Vector> layers = new HashMap<String, Vector>();
	
	
	public MapView()
	{
		super("");
		mapViewMenu = new MapViewMenu();
		try
		{
			Window.enableScrolling(false);
			Window.setMargin("0px");
			setStylePrimaryName("custom-Panel");
			setSize(Integer.toString(Window.getClientWidth()) + "px", Integer.toString(Window.getClientHeight()) + "px");
			initializeMap();
			add(mapViewMenu);
			add(mapWidget);
			mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0);
				
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage() + "MapView");
		}
	}
	
	public void initializeMap()
	{
		try
		{

	        //create some MapOptions
			MapOptions defaultMapOptions = new MapOptions();  
			defaultMapOptions.setNumZoomLevels(16);
			defaultMapOptions.setMaxResolutionToAuto();
			defaultMapOptions.removeDefaultControls();
			zoom = new Zoom();
			nav = new Navigation();
			
		
			
			//Create a MapWidget and add an empty layer...we don't want to pull from an external website.   
			//Set to the width of the window.
			mapWidget = new MapWidget(Integer.toString(Window.getClientWidth()) + "px", Integer.toString(Window.getClientHeight()) + "px", defaultMapOptions);          
			map = mapWidget.getMap(); 
			
			//Set the lower left and upper right coordinates
			//of our overlay image and transform those to our CRS
	        map.setOptions(defaultMapOptions);	        
	        
			EmptyLayer.Options emptyLayerOptions = new EmptyLayer.Options();         
			emptyLayerOptions.setAttribution("");
			emptyLayerOptions.setIsBaseLayer(true); //make it a baselayer.
			EmptyLayer emptyLayer = new EmptyLayer("Empty layer", emptyLayerOptions);
			map.addLayer(emptyLayer);
			map.setCenter(new LonLat(0,0));	
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
	}
	
	public void drawLayer(Vector layer, String layerId, int index)
	{	
        
        Layer removeLayer =  map.getLayerByName(layerId);
		// Create the draw controls
        drawPointFeatureControl = new DrawFeature(layer,
                                                  new PointHandler());
        map.addControl(drawPointFeatureControl);
        drawLineFeatureControl = new DrawFeature(layer, new PathHandler());
        map.addControl(drawLineFeatureControl);
        drawPolygonFeatureControl = new DrawFeature(layer,
                                                    new PolygonHandler());
        
        map.addControl(drawPolygonFeatureControl);
        
        if(removeLayer != null)
        {
        	map.removeLayer(removeLayer);
        }
        
		map.addLayer(layer);
		layers.put(layerId, layer);
		map.setCenter(new LonLat(0, 0));
		map.setLayerIndex(layer, index);
	}
	
	public void drawFeature(final String featureId, String layerId, final Feature feature)
	{
		Vector layer = layers.get(layerId);
		layer.addVectorFeatureAddedListener(new VectorFeatureAddedListener() {

			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject) {
				eventObject.getVectorFeature().setFeatureId(featureId);
				eventObject.getVectorFeature().getAttributes().setAttribute("NEW", true);
				eventObject.getVectorFeature().getAttributes().setAttribute("REFERENCE_ID", feature.getReferenceId());
				eventObject.getVectorFeature().getAttributes().setAttribute("REFERENCE_TYPE", feature.getReferenceType());
				eventObject.getVectorFeature().getAttributes().setAttribute("ENABLE_HISTORY", feature.getEnableHistory());
				eventObject.getVectorFeature().getAttributes().setAttribute("FEATURE_TYPE", feature.getFeatureType());
				drawPointFeatureControl.deactivate();
				drawLineFeatureControl.deactivate();
				drawPolygonFeatureControl.deactivate();
				mapViewMenu.getSaveButton().setVisible(true);
			}
        	
        });
		if(feature.getFeatureType().equals("POINT"))
			drawPointFeatureControl.activate();
		if(feature.getFeatureType().equals("LINE"))
			drawLineFeatureControl.activate();
		if(feature.getFeatureType().equals("AREA"))
			drawPolygonFeatureControl.activate();
		if(feature.getFeatureType().equals("LABEL"))
			drawPointFeatureControl.activate();
	}
	
	private OnModificationListener createOnModificationListener(final String type) {
		return new OnModificationListener() {
			@Override
			public void onModification(VectorFeature vectorFeature) {
				mapViewMenu.getSaveButton().setVisible(true);
				
			}
		};
	}
	
	private DragFeatureListener createDragFeatureListener(final String type) {
        return new DragFeatureListener() {
            public void onDragEvent(VectorFeature vectorFeature,
                    Pixel pixel) {
            	mapViewMenu.getSaveButton().setVisible(true);
            }
        };
    }
	
	public void clearMap()
	{
		if(dragFeature != null)
			dragFeature.deactivate();
		map.removeOverlayLayers();
		getSaveButton().setVisible(false);
		getAddButton().setVisible(false);
		getMoveButton().setVisible(false);
		getPointsButton().setVisible(false);
		getResizeButton().setVisible(false);
		getRotateButton().setVisible(false);
		getDeleteButton().setVisible(false);
		removeZoom();
		
		 
	}
	
	public Image getConfigButton()
	{
		return mapViewMenu.getConfigButton();
	}
	
	public Image getSaveButton()
	{
		return mapViewMenu.getSaveButton();
	}
	
	public Image getAddButton()
	{
		return mapViewMenu.getAddButton();
	}

	
	public Widget asWidget() {
	    return this;
	}

	@Override
	public VectorFeature[] getLayerFeatures(String layerId) {
	
			Vector layer = layers.get(layerId);
			VectorFeature[] features = layer.getFeatures();
			return features;
	}

	@Override
	public Image getMoveButton() {
		return mapViewMenu.getMoveButton();
	}

	@Override
	public Image getPointsButton() {
		return mapViewMenu.getPointsButton();
	}

	@Override
	public Image getResizeButton() {
		return mapViewMenu.getResizeButton();
	}

	@Override
	public Image getRotateButton() {
		return mapViewMenu.getRotateButton();
	}

	@Override
	public DragFeature getDragFeature() {
		return dragFeature;
	}

	@Override
	public ModifyFeature getModifyFeature() {
		return modifyFeature;
	}

	@Override
	public Image getDeleteButton() {
		return mapViewMenu.getDeleteImage();
	}

	@Override
	public SelectFeature getDeleteFeature() {
		return deleteFeature;
	}

	@Override
	public void setEditable(Vector layer) {
		
		getAddButton().setVisible(true);
		getMoveButton().setVisible(true);
		getPointsButton().setVisible(true);
		getResizeButton().setVisible(true);
		getRotateButton().setVisible(true);
		getDeleteButton().setVisible(true);
		
		try
		{
	        ModifyFeatureOptions modifyFeatureOptions = new ModifyFeatureOptions();
	        modifyFeatureOptions.setMode(ModifyFeature.RESHAPE);
	        modifyFeatureOptions.onModification(createOnModificationListener("onModification"));
	        modifyFeature = new ModifyFeature(layer, modifyFeatureOptions);
	        map.addControl(modifyFeature);
	        
	        deleteFeature = new SelectFeature(layer);
	        map.addControl(deleteFeature);
	        
	        DragFeatureOptions dragFeatureOptions = new DragFeatureOptions();
	        dragFeatureOptions.onDrag(createDragFeatureListener("onDrag"));
	        dragFeatureOptions.onStart(createDragFeatureListener("onStart"));
	        dragFeatureOptions.onComplete(createDragFeatureListener("onComplete"));
	        dragFeature = new DragFeature(layer, dragFeatureOptions);
	        map.addControl(dragFeature);
	        dragFeature.activate();
	        
	        snapControl = new Snapping();
	        snapControl.setLayer(layer); //The editable layer.  Features from this layer that are digitized or modified may have vertices snapped to features from the target layer
	        snapControl.setTargetLayer(layer); //Editing will snap to features from this layer.
	        map.addControl(snapControl);
	        snapControl.activate();
	        

		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
		
	}

	@Override
	public String getMapProjection() {
		return map.getProjection();
	}

	@Override
	public LonLat getLonLatFromPixel(Pixel pix) {
		return map.getLonLatFromPixel(pix);
	}

	@Override
	public void addImageLayer(
			org.gwtopenmaps.openlayers.client.layer.Image imageLayer) {
		map.addLayer(imageLayer);
		
	}

	@Override
	public void removeZoom() {
		map.removeControl(zoom);
		map.removeControl(nav);
		
	}

	@Override
	public void showZoom() {
		zoom = new Zoom();
		map.addControl(zoom);
		map.addControl(nav);
		
	}

	@Override
	public void removeLayer(String layerId) {
		// TODO Auto-generated method stub
		
	}

}
