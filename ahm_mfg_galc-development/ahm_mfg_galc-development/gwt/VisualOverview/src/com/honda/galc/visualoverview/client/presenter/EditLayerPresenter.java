package com.honda.galc.visualoverview.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.event.FetchLayerFeaturesEvent;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.codec.LayerCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.honda.galc.visualoverview.shared.layer.FeatureLayer;


public class EditLayerPresenter implements Presenter {
	public interface Display {
	    HasClickHandlers getSaveButton();
	    HasClickHandlers getCancelButton();
	    HasClickHandlers getSaveLayerButton();
	    HasClickHandlers getEditLayerButton();
	    HasValue<String> getLayerId();
	    HasValue<String> getNewLayerId();
	    HasValue<String> getLayerType();
	    HasValue<String> getLayerDao();
	    HasValue<String> getDefaultZoom();
	    HasValue<String> getBoundsLeft();
	    HasValue<String> getBoundsRight();
	    HasValue<String> getBoundsUpper();
	    HasValue<String> getBoundsLower();
	    void setLayers(List<Layer> layers);
	    Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	
	public EditLayerPresenter(HandlerManager eventBus, Display display)
	{
		this.eventBus = eventBus;
		this.display = display;
		fetchLayerDetails();
		bind();
	}
	
	public EditLayerPresenter(HandlerManager eventBus, Display display, String id)
	{
		this.eventBus = eventBus;
		this.display = display;
		fetchLayerDetails();
		bind();
	}
	
	public void bind() {
		display.getSaveLayerButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSave();
			}
			
		});
		
		display.getEditLayerButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "LayerDao/getLayerById?" + display.getLayerId().getValue());
		    	resource.get().send(new JsonCallback() {

					@Override
					public void onFailure(Method method, Throwable exception) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Method method, JSONValue response) {
						LayerCodec codec = GWT.create(LayerCodec.class);
						Layer layer = codec.decode(response);
						eventBus.fireEvent(new FetchLayerFeaturesEvent(layer, true));
					}
		    	
		    	});
				
				
			}
			
		});
		
	    this.display.getSaveButton().addClickHandler(new ClickHandler() {   
	      public void onClick(ClickEvent event) {
	      }
	    });
	}
	
	public void fetchLayerDetails()
	{
		try
		{
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "LayerDao/getAllLayers");
	    	resource.get().send(new JsonCallback() {
	
				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert("Failed to retrieve layer data");
					
				}
	
				@Override
				public void onSuccess(Method method, JSONValue response) {
					LayerCodec codec = GWT.create(LayerCodec.class);
					JSONArray result = response.isArray();
					List<Layer> layers = new ArrayList<Layer>( result.size() );
					
					Layer newLayer = new FeatureLayer();
					newLayer.setLayerId("NEW");
					layers.add(newLayer);
					

					for ( int i = 0; i < result.size(); i++ ) {
					   layers.add( codec.decode( result.get( i ) ) );
					}
							
			        display.setLayers(layers);
					
				}
	    		
	    	});
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
    }
	
	@Override
	public void go(HasWidgets container) {
	    bind();
	    fetchLayerDetails();
	    container.add(display.asWidget());
	}
	
	private void doSave()
	{
		try
		{
	    	Layer layer = new FeatureLayer();
	    	if(display.getLayerId().getValue() == "NEW")
	    		layer.setLayerId(display.getNewLayerId().getValue());
	    	else
	    		layer.setLayerId(display.getLayerId().getValue());
	    	
	    	layer.setLayerType(display.getLayerType().getValue());
	    	layer.setLayerDao(display.getLayerDao().getValue());
	    	layer.setDefaultZoom(Integer.parseInt(display.getDefaultZoom().getValue()));
	    	layer.setBoundaryLeft(Double.parseDouble(display.getBoundsLeft().getValue()));
	    	layer.setBoundaryRight(Double.parseDouble(display.getBoundsRight().getValue()));
	    	layer.setBoundaryUpper(Double.parseDouble(display.getBoundsUpper().getValue()));
	    	layer.setBoundaryLower(Double.parseDouble(display.getBoundsLower().getValue()));
	    	LayerCodec codec = GWT.create(LayerCodec.class);
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "LayerDao/update");
	    	resource.post().text("[{\"com.honda.galc.entity.product.Layer\":" + codec.encode(layer) + "}]").send(new JsonCallback() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert("Failed to save to database");
					
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

}
