package com.honda.galc.visualoverview.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.event.FetchLayerFeaturesEvent;
import com.honda.galc.visualoverview.client.event.ShowZoomEvent;
import com.honda.galc.visualoverview.client.widgets.DisplayedLayersListGrid;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.View;
import com.honda.galc.visualoverview.shared.ViewId;
import com.honda.galc.visualoverview.shared.codec.LayerCodec;
import com.honda.galc.visualoverview.shared.codec.ViewCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;


public class EditViewPresenter implements Presenter {
	public interface Display {
	    HasClickHandlers getSaveButton();
	    HasClickHandlers getCancelButton();
	    HasClickHandlers getShowButton();
	    ListBox getViewIdListBox();
	    HasValue<String> getViewId();
	    HasValue<String> getNewViewId();
	    List<String> getDisplayedLayers();
	    DisplayedLayersListGrid getDisplayedLayersListGrid();
	    void setViews(List<View> views, String currentView);
	    void setLayers(List<Layer> layers);
	    void setDisplayed(List<View> views);
	    Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	
	public EditViewPresenter(HandlerManager eventBus, Display display)
	{
		this.eventBus = eventBus;
		this.display = display;
		fetchViews("");
		bind();
	}
	
	public void bind() {
		
		display.getViewIdListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				fetchDisplayed();
				fetchLayers();
			}
			
		});
		
		display.getDisplayedLayersListGrid().removeField.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				display.getDisplayedLayersListGrid().removeRecordClick(event.getRecordNum());
				deleteDisplayedLayer(event.getRecord().getAttribute("layerId"));
			}
			
		});
		
		display.getDisplayedLayersListGrid().addRecordDropHandler(new RecordDropHandler() {

			@Override
			public void onRecordDrop(RecordDropEvent event) {				
				for(Record record : display.getDisplayedLayersListGrid().getDataAsRecordList().toArray())
				{
					if(record.getAttribute("layerId").equals(event.getDropRecords()[0].getAttribute("layerId")))
					{
						event.cancel();
					}
				}
				
				saveDisplayedLayer(event.getDropRecords()[0].getAttribute("layerId"));
			}
			
		});
		
		
		display.getShowButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ShowZoomEvent());
				for(String layerId : display.getDisplayedLayers())
				{
					Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "LayerDao/getLayerById?" + layerId);
			    	resource.get().send(new JsonCallback() {

						@Override
						public void onFailure(Method method, Throwable exception) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Method method, JSONValue response) {
							LayerCodec codec = GWT.create(LayerCodec.class);
							Layer layer = codec.decode(response);
							eventBus.fireEvent(new FetchLayerFeaturesEvent(layer, false));
							
						}
			    	
			    	});
				}
				
			}
			
		});
		
		
	    this.display.getSaveButton().addClickHandler(new ClickHandler() {   
	      public void onClick(ClickEvent event) {
	      }
	    });
	}
	
	private void deleteDisplayedLayer(String layerId)
	{
		try
		{
	    	ViewCodec codec = GWT.create(ViewCodec.class);
    		View view = new View();
    		ViewId viewId = new ViewId();
    		viewId.setLayerId(layerId);
    		viewId.setViewId(display.getViewId().getValue());
    		view.setId(viewId);
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "ViewDao/remove");
	    	resource.post().text("[{\"com.honda.galc.entity.product.View\":" + codec.encode(view) + "}]").send(new JsonCallback() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert(exception.getMessage() + "saveView");
					
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
	
	private void saveDisplayedLayer(String layerId)
	{
		try
		{
	    	ViewCodec codec = GWT.create(ViewCodec.class);

    		View view = new View();
    		ViewId viewId = new ViewId();
    		viewId.setLayerId(layerId);
    		if(display.getViewId().getValue().equals("NEW"))
    			viewId.setViewId(display.getNewViewId().getValue());
    		else
    			viewId.setViewId(display.getViewId().getValue());
    		view.setId(viewId);
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "ViewDao/update");
	    	resource.post().text("[{\"com.honda.galc.entity.product.View\":" + codec.encode(view) + "}]").send(new JsonCallback() {

				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert(exception.getMessage() + "saveView");
					
				}

				@Override
				public void onSuccess(Method method, JSONValue response) {
					//if(display.getViewId().getValue().equals("NEW"))
					//{	
					//	fetchViews(display.getNewViewId().getValue());
					//}
					
				}
	    		
	    	});

		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
	}
	
	private void fetchLayers()
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
								
					for ( int i = 0; i < result.size(); ++i ) {
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
	
	private void fetchDisplayed()
	{
		try
		{
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "ViewDao/getViewsById?" + display.getViewId().getValue());
	    	resource.get().send(new JsonCallback() {
	
				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert("Failed to retrieve displayed view data");
					
				}
	
				@Override
				public void onSuccess(Method method, JSONValue response) {
					ViewCodec codec = GWT.create(ViewCodec.class);
					JSONArray result = response.isArray();
					List<View> views = new ArrayList<View>( result.size() );
								
					for ( int i = 0; i < result.size(); ++i ) {
					   views.add( codec.decode( result.get( i ) ) );
					}
							
			        display.setDisplayed(views);
					
				}
	    		
	    	});
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
    }
	
	private void fetchViews(final String currentView)
	{
		try
		{
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "ViewDao/getAllViews");
	    	resource.get().send(new JsonCallback() {
	
				@Override
				public void onFailure(Method method, Throwable exception) {
					Window.alert("Failed to retrieve view data");
					
				}
	
				@Override
				public void onSuccess(Method method, JSONValue response) {
					ViewCodec codec = GWT.create(ViewCodec.class);
					JSONArray result = response.isArray();
					List<View> views = new ArrayList<View>( result.size() );

					View view = new View();
					ViewId viewId = new ViewId();
					viewId.setLayerId("");
					viewId.setViewId("NEW");
					view.setId(viewId);
					views.add(view);
					
					for ( int i = 0; i < result.size(); ++i ) {
					   views.add( codec.decode( result.get( i ) ) );
					}
							
			        display.setViews(views, currentView);
			        
			        fetchDisplayed();
			        fetchLayers();
					
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
	    container.add(display.asWidget());
	}
	

}
