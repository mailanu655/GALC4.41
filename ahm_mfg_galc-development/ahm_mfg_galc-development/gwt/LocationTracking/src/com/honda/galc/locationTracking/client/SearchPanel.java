package com.honda.galc.locationTracking.client;

import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.honda.galc.locationTracking.shared.Feature;
import com.honda.galc.locationTracking.shared.FeaturePoint;
import com.honda.galc.locationTracking.shared.FeaturePointId;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.watopi.chosen.client.event.ChosenChangeEvent;
import com.watopi.chosen.client.event.ChosenChangeEvent.ChosenChangeHandler;
import com.watopi.chosen.client.gwt.ChosenListBox;


public class SearchPanel extends ScrollablePanel{

	public ChosenListBox chzn;
	private DatabaseService databaseService;
	
	public SearchPanel(final MapView mapView) {	
		super("");
		HorizontalPanel topHP = new HorizontalPanel();
		
		setSize(Integer.toString(Window.getClientWidth()) + "px", Integer.toString(Window.getClientHeight()) + "px");

		topHP.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topHP.setWidth("100%");
		
		chzn = new ChosenListBox();
		chzn.setPlaceholderTextSingle("Enter VIN");
		chzn.setWidth("90%");
		chzn.setHeight("50px");
		chzn.setStyleName("gwt-Big-Text");
		chzn.setSearchContains(true);
		topHP.add(chzn);
		
				
		databaseService =  new DatabaseService();
		loadList();
		chzn.addChosenChangeHandler(new ChosenChangeHandler() {

			@Override
			public void onChange(ChosenChangeEvent event) {
				if(event.getValue().trim() == "ALL")
				{
					databaseService.getMapClientDataService().getAllVINs(new ListCallback<GenericRow>(){
						public void onFailure(DataServiceException e) { 
							Window.alert("No VINs found");
						  } 
						  public void onSuccess(List<GenericRow> resultSet) {
							List<Feature> prodLocs = new ArrayList();
							  for(GenericRow gRow : resultSet)
						     {
									Feature prodLoc = new Feature();
									prodLoc.setFeatureId(gRow.getString("FEATURE_ID"));
									FeaturePoint point = new FeaturePoint();
									point.setId(new FeaturePointId(gRow.getString("FEATURE_ID"), 0));
									point.setXCoordinate(gRow.getDouble("X_COORDINATE"));
									point.setYCoordinate(gRow.getDouble("Y_COORDINATE"));
									List<FeaturePoint> points = new ArrayList<FeaturePoint>();
									points.add(point);
									prodLoc.setFeaturePoints(points);
									prodLocs.add(prodLoc);
						     }
						     mapView.displayAllVINs(prodLocs);
						     TabSet tabSet = (TabSet) RootLayoutPanel.get().getWidget(0);
						     tabSet.selectTab(1);
						  } 
					});
					
				}
				else
				{
					List<String> filterValues = new ArrayList();
					filterValues.add(event.getValue().trim());
					databaseService.getMapClientDataService().getVIN(filterValues, new ListCallback<GenericRow>(){
						public void onFailure(DataServiceException e) { 
							Window.alert(e.getMessage());
						  } 
						  public void onSuccess(List<GenericRow> resultSet) {
							List<Feature> prodLocs = new ArrayList();
							  for(GenericRow gRow : resultSet)
						     {
									Feature prodLoc = new Feature();
									prodLoc.setFeatureId(gRow.getString("FEATURE_ID"));
									FeaturePoint point = new FeaturePoint();
									point.setId(new FeaturePointId(gRow.getString("FEATURE_ID"), 0));
									point.setXCoordinate(gRow.getDouble("X_COORDINATE"));
									point.setYCoordinate(gRow.getDouble("Y_COORDINATE"));
									List<FeaturePoint> points = new ArrayList<FeaturePoint>();
									points.add(point);
									prodLoc.setFeaturePoints(points);
									prodLocs.add(prodLoc);
						     }
						     mapView.displayAllVINs(prodLocs);
						     TabSet tabSet = (TabSet) RootLayoutPanel.get().getWidget(0);
						     tabSet.selectTab(1);
						  } 
					});
				}
				
			}
			
		});
		
		
		setStylePrimaryName("custom-Panel");
		addMember(topHP);
		
	}
	
	public void loadList()
	{
		try
		{
		databaseService.getMapClientDataService().getAllVINs(new ListCallback<GenericRow>() {
	
			@Override
			public void onFailure(DataServiceException error) {
				// TODO Auto-generated method stub
				Window.alert(error.getMessage());
				
			}
	
			@Override
			public void onSuccess(List<GenericRow> result) {
				chzn.clear();
				chzn.addItem("ALL");
				for(GenericRow gRow : result)
				{					
					chzn.addItem(gRow.getString("FEATURE_ID"));
				}
				chzn.forceRedraw();
				
			}
			
		});
		}
		catch(Exception ex)
		{
			Window.alert(ex.getMessage());
		}
	}
}
