package com.honda.galc.locationTracking.client;


import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.honda.galc.locationTracking.shared.Feature;
import com.honda.galc.locationTracking.shared.FeaturePoint;
import com.honda.galc.locationTracking.shared.FeaturePointId;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.tab.Tab;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.smartgwt.mobile.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.mobile.client.widgets.tab.events.TabSelectedHandler;

 

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LocationTracking implements EntryPoint {
	
	
	private MapGeoLocation geoLocation;
	
	private DatabaseService databaseService;
	
	private MapRESTCommunication restCommunication;
	
    private Tab saveTab, mapTab, searchTab;
    private TabSet tabSet;
    private SavePanel savePanel;
    private SearchPanel searchPanel;
    private MapView mapView;
	
	/*
	 * Start a "thread" get the latest VIN locations
	 * Save any unsaved VIN locations.
	 */
	private void startUpdateThread() {
		Timer t = new Timer() {
			public void run() {
				restCommunication.getCbuLocations();
				
				
				//Try persisting
				try{
					databaseService.getMapClientDataService().getUnsavedVINs(new ListCallback<GenericRow>(){
						public void onFailure(DataServiceException e) { 
						  } 
						  public void onSuccess(List<GenericRow> resultSet) { 
						     for(GenericRow gRow : resultSet)
						     {
								Feature prodLoc = new Feature();
								prodLoc.setFeatureId(gRow.getString("FEATURE_ID"));
								prodLoc.setFeatureType("CBU");
								FeaturePoint point = new FeaturePoint();
								point.setId(new FeaturePointId(gRow.getString("FEATURE_ID"), 0));
								point.setXCoordinate(gRow.getDouble("X_COORDINATE"));
								point.setYCoordinate(gRow.getDouble("Y_COORDINATE"));
								List<FeaturePoint> points = new ArrayList<FeaturePoint>();
								points.add(point);
								prodLoc.setFeaturePoints(points);
								prodLoc.setFeatureType("CBU");
								restCommunication.saveCbuLocation(prodLoc);
								databaseService.clearVIN(prodLoc);
						     }
						  } 
					});
					
				}
				catch(Exception ex)
				{
					
				}
				

			}
		};

		// Schedule the timer to run once every seconds.
		searchPanel.loadList();
		t.scheduleRepeating(20000);
	}

	@Override
	public void onModuleLoad() {
        tabSet = new TabSet();

        saveTab = new Tab("Save", IconResources.INSTANCE.downloads());
        savePanel = new SavePanel();
        saveTab.setPane(savePanel);
        tabSet.addTab(saveTab);
        
        mapTab = new Tab("Map", IconResources.INSTANCE.bullseye());
        mapView = new MapView();
        mapTab.setPane(mapView);
        tabSet.addTab(mapTab);
        
        searchTab = new Tab("Search", IconResources.INSTANCE.search());
        searchPanel = new SearchPanel(mapView);
        searchTab.setPane(searchPanel);
        tabSet.addTab(searchTab);
        
        RootLayoutPanel.get().add(tabSet);
        
        tabSet.selectTab(mapTab);
        
        tabSet.addTabSelectedHandler(new TabSelectedHandler(){

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(event.getTabIndex() == 0)
				{
	            	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	            		
	            	    @Override
	            	    public void execute() {
	            	    	
	            	    	savePanel.vinText.setText("");
	            	        savePanel.vinText.setFocus(true);
	            	    }
	            	});
				}
			}
        	
        });
        restCommunication = new MapRESTCommunication();
        databaseService = new DatabaseService();
        startUpdateThread();
        
	}
	
	
	
	

	


	
	

}
