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
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.event.DrawFeatureEvent;
import com.honda.galc.visualoverview.client.event.DrawNewFeatureEvent;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.codec.FeatureCodec;
import com.honda.galc.visualoverview.shared.constants.VisualizationConstants;
import com.honda.galc.visualoverview.shared.feature.PointFeature;


public class EditFeaturePresenter implements Presenter {
	public interface Display {
	    HasClickHandlers getSaveButton();
	    HasClickHandlers getCancelButton();
	    HasClickHandlers getSaveLayerButton();
	    HasChangeHandlers getLayerIdListBox();
	    Label getLayerId();
	    HasValue<String> getFeatureId();
	    HasValue<String> getNewFeatureId();
	    HasValue<String> getFeatureType();
	    HasValue<String> getReferenceId();
	    HasValue<String> getReferenceType();
	    void setFeatures(List<Feature> features);
	    ListBox getLayerListBox();
	    Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	private String editLayer;
	
	
	public EditFeaturePresenter(HandlerManager eventBus, Display display, String editLayer)
	{
		this.eventBus = eventBus;
		this.display = display;
		this.editLayer = editLayer;
		fetchLayerDetails();
		bind();
	}
	
	public void bind() {
		display.getSaveLayerButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				Feature feature = new PointFeature();
				feature.setFeatureId(display.getNewFeatureId().getValue());
				feature.setFeatureType(display.getFeatureType().getValue().toUpperCase());
				feature.setReferenceId(display.getReferenceId().getValue());
				feature.setReferenceType(display.getReferenceType().getValue());
				feature.setEnableHistory(0);
				eventBus.fireEvent(new DrawNewFeatureEvent(display.getNewFeatureId().getValue(), feature));
			}
			
		});
	
		
		display.getLayerIdListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				fetchFeatureDetails();
			}
			
		});
		
	    this.display.getSaveButton().addClickHandler(new ClickHandler() {   
	      public void onClick(ClickEvent event) {
	      }
	    });
	}
	
	public void fetchFeatureDetails()
	{
		try
		{
	    	Resource resource = new Resource(VisualizationConstants.WEB_SERVICE_LOCATION + "FeatureDao/getFeaturesByLayerId?" + display.getLayerId().getText());
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
					
					Feature newFeature = new PointFeature();
					newFeature.setFeatureId("NEW");
					features.add(newFeature);
					
					for ( int i = 0; i < result.size(); ++i ) {
					   features.add( codec.decode( result.get( i ) ) );
					}
							
			        display.setFeatures(features);
			        
					
				}
	    		
	    	});
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
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
					
							
			        display.getLayerId().setText(editLayer);
			        			        
			        fetchFeatureDetails();
					
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
	

}
