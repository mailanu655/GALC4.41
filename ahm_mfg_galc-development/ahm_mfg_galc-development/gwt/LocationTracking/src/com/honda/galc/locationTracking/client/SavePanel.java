package com.honda.galc.locationTracking.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.honda.galc.locationTracking.shared.Feature;
import com.honda.galc.locationTracking.shared.FeaturePoint;
import com.honda.galc.locationTracking.shared.FeaturePointId;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;

public class SavePanel extends ScrollablePanel {

	public TextBox vinText;
	private MapRESTCommunication restCommunication;
	
	public SavePanel() {

		super("Save");
		HorizontalPanel topHP = new HorizontalPanel();
		HTMLPanel middleHP = new HTMLPanel("");
		
		vinText = new TextBox();
		vinText.setWidth("90%");
		vinText.setHeight("50px");
		vinText.setMaxLength(17);
		vinText.setStyleName("gwt-Big-Text");
		vinText.setAlignment(TextAlignment.CENTER);
		topHP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topHP.add(vinText);
		topHP.setWidth("100%");
			
		setSize(Integer.toString(Window.getClientWidth()) + "px", Integer.toString(Window.getClientHeight()) + "px");
		addMember(topHP);
		addMember(middleHP);
		
		final MapGeoLocation geoLocation = new MapGeoLocation();
		
		//Initialize REST communication.
		restCommunication = new MapRESTCommunication();
		restCommunication.getCbuLocations();
		
		vinText.addValueChangeHandler(new ValueChangeHandler<String>(){
			public void onValueChange(ValueChangeEvent<String> event){
				if(event.getValue().length() != 17)
				{
					Window.alert("Invalid barcode");
					return;
				}
				if(geoLocation.getLonLat() == null)
				{
					Window.alert("User location not found");
					return;
				}
				Date now = new Date();
				
				
				Feature prodLoc = new Feature();
				prodLoc.setFeatureId(event.getValue());
				prodLoc.setFeatureType("CBU");
				FeaturePoint point = new FeaturePoint();
				point.setId(new FeaturePointId(event.getValue(), 0));
				point.setXCoordinate(geoLocation.getLonLat().lon());
				point.setYCoordinate(geoLocation.getLonLat().lat());
				List<FeaturePoint> points = new ArrayList<FeaturePoint>();
				points.add(point);
				prodLoc.setFeaturePoints(points);
				restCommunication.saveCbuLocation(prodLoc);
				
                SC.showPrompt("VIN Saved!", "VIN: " + event.getValue() + " saved at Location(" + Double.toString(geoLocation.getLonLat().lat()) + "," + Double.toString(geoLocation.getLonLat().lon()) + ")");
                new Timer() {

                    @Override
                    public void run() {
                        SC.clearPrompt();
                    }
                }.schedule(2 * 1000);
								
			}
		});
				
		setStylePrimaryName("custom-Panel");		
	}

	
}
