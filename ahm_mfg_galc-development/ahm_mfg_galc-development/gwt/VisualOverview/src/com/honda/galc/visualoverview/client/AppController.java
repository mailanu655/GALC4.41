package com.honda.galc.visualoverview.client;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.honda.galc.visualoverview.client.event.ConfigEvent;
import com.honda.galc.visualoverview.client.event.DrawFeatureEvent;
import com.honda.galc.visualoverview.client.event.DrawImageLayerEvent;
import com.honda.galc.visualoverview.client.event.DrawLayerEvent;
import com.honda.galc.visualoverview.client.event.DrawNewFeatureEvent;
import com.honda.galc.visualoverview.client.event.FetchLayerFeaturesEvent;
import com.honda.galc.visualoverview.client.event.EditFeatureEvent;
import com.honda.galc.visualoverview.client.event.EditLayerEvent;
import com.honda.galc.visualoverview.client.event.EditViewEvent;
import com.honda.galc.visualoverview.client.event.SaveFeaturesEvent;
import com.honda.galc.visualoverview.client.event.ShowZoomEvent;
import com.honda.galc.visualoverview.client.event.WaitEvent;
import com.honda.galc.visualoverview.client.eventhandler.ConfigEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.DrawFeatureEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.DrawImageLayerEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.DrawLayerEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.DrawNewFeatureEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.EditFeatureEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.EditLayerEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.EditViewEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.FetchLayerFeaturesEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.SaveFeaturesEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.ShowZoomEventHandler;
import com.honda.galc.visualoverview.client.eventhandler.WaitEventHandler;
import com.honda.galc.visualoverview.client.presenter.ConfigPresenter;
import com.honda.galc.visualoverview.client.presenter.EditFeaturePresenter;
import com.honda.galc.visualoverview.client.presenter.EditLayerPresenter;
import com.honda.galc.visualoverview.client.presenter.EditViewPresenter;
import com.honda.galc.visualoverview.client.presenter.MapPresenter;
import com.honda.galc.visualoverview.client.presenter.Presenter;
import com.honda.galc.visualoverview.client.presenter.WaitPresenter;
import com.honda.galc.visualoverview.client.view.ConfigView;
import com.honda.galc.visualoverview.client.view.EditFeatureView;
import com.honda.galc.visualoverview.client.view.EditLayerView;
import com.honda.galc.visualoverview.client.view.EditViewView;
import com.honda.galc.visualoverview.client.view.MapView;
import com.honda.galc.visualoverview.client.view.WaitView;
import com.honda.galc.visualoverview.client.widgets.WaitDialog;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.codec.LayerCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.rpc.RPCResponse;

public class AppController implements Presenter, ValueChangeHandler<String> {
	  private final HandlerManager eventBus; 
	  private HasWidgets container;
	  private MapPresenter mapPresenter;
	  private MapView mapView = new MapView();
	  private String editLayer = "";
	  private Vector layer;
	  private Timer t;

	public AppController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		Logger logger = Logger.getLogger("VisualOverview");
		logger.log(Level.INFO, "test");
		mapPresenter = new MapPresenter(eventBus, mapView);
		bind();
	}
	
	public void go(final HasWidgets container) {
	    this.container = container;
	    mapView.getConfigButton().setVisible(false);
	    if (History.getToken().trim() == "") {
	      History.newItem("map");
	    }
	    else
	    {
	    	container.clear();
	    	History.fireCurrentHistoryState();
	    }
	}
	
	private void bind() {
	    History.addValueChangeHandler(this);
	    
	    eventBus.addHandler(FetchLayerFeaturesEvent.TYPE, new FetchLayerFeaturesEventHandler() {

			@Override
			public void onFetchLayerFeatures(FetchLayerFeaturesEvent event) {
				editLayer = event.getLayer().getLayerId();
				final FetchLayerFeaturesEvent tempEvent = event;
				if(event.getLayer().getLayerType().trim().equals("FEATURE"))
				{
					Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + event.getLayer().getLayerDao().trim());
			    	resource.get().send(new JsonCallback() {
	
						@Override
						public void onFailure(Method method, Throwable exception) {
							Window.alert("Layer configured improperly");
							Window.alert(exception.getMessage());
						}
	
						@Override
						public void onSuccess(Method method, JSONValue response) {
							fetchLayerFeatures(tempEvent.getLayer(), tempEvent.isEditable());
						}
			    		
			    	});
				}
				else
				{
					fetchLayerFeatures(event.getLayer(), event.isEditable());
				}
				
			}
	    	
	    });
	    eventBus.addHandler(ConfigEvent.TYPE, new ConfigEventHandler() {

			@Override
			public void onConfig(ConfigEvent event) {
				doConfig();
			}
	    
	    });
	    eventBus.addHandler(EditLayerEvent.TYPE, new EditLayerEventHandler() {
	    	
	        public void onEditLayer(EditLayerEvent event) {
	        	 doEditLayer(event.getId());
	          }
	    });
	    eventBus.addHandler(EditFeatureEvent.TYPE, new EditFeatureEventHandler() {

			@Override
			public void onEditFeature(EditFeatureEvent event) {
				doEditFeature();
			}
	    	
	    });
	    eventBus.addHandler(EditViewEvent.TYPE, new EditViewEventHandler() {

			@Override
			public void onEditView(EditViewEvent event) {
				doEditView();
				
			}
	    	
	    });
	    eventBus.addHandler(SaveFeaturesEvent.TYPE, new SaveFeaturesEventHandler() {

			@Override
			public void onSaveFeatures(SaveFeaturesEvent event) {
				saveLayer(editLayer);
				
			}
	    	
	    });
	    eventBus.addHandler(DrawNewFeatureEvent.TYPE, new DrawNewFeatureEventHandler() {

			@Override
			public void onDrawNewFeature(DrawNewFeatureEvent event) {
				drawFeature(event.getId(), editLayer, event.getFeature());		
			}
	    	
	    });
	    eventBus.addHandler(DrawLayerEvent.TYPE,  new DrawLayerEventHandler() {

			@Override
			public void onDrawLayer(DrawLayerEvent event) {	

			layer = new Vector(event.getLayer().getLayerId());
				
			int index = 10000;
				for(final Feature feature : event.getFeatures())
				{
					if(feature.getFeatureId() == "NEW" || feature.getFeatureId().substring(0, 10) == "OpenLayers")
						continue;
					
					feature.draw(event.getAttributeMap(), event.getLayer(), eventBus, layer);
					
					if(feature.getFeaturePoints().get(0).getZCoordinate() < index)
						index = (int)feature.getFeaturePoints().get(0).getZCoordinate();
				}

				mapPresenter.drawLayer(layer, event.getLayer().getLayerId(), event.isEditable(), index);
			}
	    	
	    });
	    eventBus.addHandler(DrawFeatureEvent.TYPE, new DrawFeatureEventHandler() {

			@Override
			public void onDrawFeature(DrawFeatureEvent event) {
				
					layer.addFeature(event.getVectorFeature());			

			}
	    	
	    });
	    
	    eventBus.addHandler(ShowZoomEvent.TYPE, new ShowZoomEventHandler() {

			@Override
			public void onShowZoom(ShowZoomEvent event) {
				mapView.showZoom();
				
			}
	    	
	    });
	    
	    eventBus.addHandler(DrawImageLayerEvent.TYPE, new DrawImageLayerEventHandler () {

			@Override
			public void onDrawImage(DrawImageLayerEvent event) {
				mapView.addImageLayer(event.getImage());
				
			}
	    	
	    });
	    
	}
	
	private void saveLayer(String layerId)
	{
		mapPresenter.saveLayer(layerId);
	}
	
	private void fetchLayerFeatures(Layer layer, boolean editable)
	{
		mapPresenter.fetchLayerFeatures(layer, editable);
	}
	
	private void drawFeature(String featureId, String layerId, Feature feature)
	{
		mapView.drawFeature(featureId, layerId, feature);
	}
	
	private void doConfig() {
		mapView.clearMap();
		mapView.getAddButton().setVisible(false);
		Presenter presenter = new ConfigPresenter(eventBus, new ConfigView());
		presenter.go(container);
	}
	
	private void doWait() {
		Presenter presenter = new WaitPresenter(eventBus, new WaitView());
		presenter.go(container);
	}
	
    private void doEditLayer(String id) {
	    Presenter presenter = new EditLayerPresenter(eventBus, new EditLayerView());
	    presenter.go(container);
	}
    
    private void doEditFeature()
    {
	    Presenter presenter = new EditFeaturePresenter(eventBus, new EditFeatureView(), editLayer);
	    presenter.go(container);
    }
    
    private void doEditView()
    {
    	Presenter presenter = new EditViewPresenter(eventBus, new EditViewView());
    	presenter.go(container);
    }

	public void onValueChange(ValueChangeEvent<String> event) {
	    String token = event.getValue();
	    mapView.getConfigButton().setVisible(false);
	    
	    if(t != null)
	    	t.cancel();
	    
	    if (token != null) {
	        Presenter presenter = null;

	        if (token.equals("map")) {
	          mapPresenter.go(container);
	        }
	        else if (token.equals("config"))
	        {
				mapView.getConfigButton().setVisible(true);
				mapView.clearMap();
				mapPresenter.go(container);
	        }
    	    else
    	    {
    	    	getLayerByViewId(mapPresenter, token);
    	    	
    	    	//TODO: Move this to the layer level
    	    	startViewTimer(mapPresenter, token);
	        }

	    }
	}
	
	public void startViewTimer(final Presenter presenter, final String viewId)
	{
		t = new Timer() {
			public void run() {
				getLayerByViewId(presenter, viewId);
			}
		};

		// Schedule the timer to run.
		//TODO Put this in the database when it gets moved to the layer level.
		t.scheduleRepeating(VisualizationConstants.PLOT_REFRESH);
	}
	
	public void getLayerByViewId(final Presenter presenter, String viewId)
	{
		Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "LayerDao/getLayerByViewId?" + viewId);
    	resource.get().send(new JsonCallback() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				History.newItem("map");
			}

			@Override
			public void onSuccess(Method method, JSONValue response) {
				LayerCodec codec = GWT.create(LayerCodec.class);
				JSONArray result = response.isArray();
				
				for ( int i = 0; i < result.size(); ++i ) {
					eventBus.fireEvent(new FetchLayerFeaturesEvent(codec.decode( result.get( i ) ), false));
				}
				mapPresenter.go(container);				
			}
    	
    	});
	}
}
