package com.honda.galc.locationTracking.client;

import java.util.Collection;
import java.util.List;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.honda.galc.locationTracking.shared.Feature;

public class DatabaseService {
	
	private MapClientDataService dbService;
	public List<GenericRow> returnSet;

	public DatabaseService()
	{
		if(Database.isSupported())
		{
			dbService = (MapClientDataService)GWT.create(MapClientDataService.class);
			dbService.initFeaturePoints(new VoidCallback() { 
				  public void onFailure(DataServiceException e) { 
				    Window.alert("Failed to initialize database");
				  } 
				  public void onSuccess() { 
				    // Transaction completed! Continue... 
				  } 
			});
			
			dbService.initFeature(new VoidCallback() { 
				  public void onFailure(DataServiceException e) { 
				    Window.alert("Failed to initialize database");
				  } 
				  public void onSuccess() { 
				    // Transaction completed! Continue... 
				  } 
			});
		}
	}
	
	public void clearVIN(Feature fLoc)
	{
		if(Database.isSupported())
		{
			dbService.clearVIN(fLoc, new VoidCallback() { 
				  public void onFailure(DataServiceException e) { 
				    Window.alert("Clear database.");
				  } 
				  public void onSuccess() { 
				  } 
			});
		}
	}
	
	public void clearAllVINs()
	{
		if(Database.isSupported())
		{
			dbService.clearVINs(new VoidCallback() { 
				  public void onFailure(DataServiceException e) { 
				    Window.alert("Clear database.");
				  } 
				  public void onSuccess() { 
				  } 
			});
		}
	}
	
	public void saveVINLocations(Collection<Feature> prodLocs)
	{
		if (Database.isSupported())
		{
			for(Feature feature : prodLocs)
			{
	
				dbService.insertProductLocations(feature.getFeaturePoints(), new RowIdListCallback() {
					public void onFailure(DataServiceException e) { 
					     Window.alert("Failed to save VIN"); 
					     Window.alert(e.getMessage());
					  } 
					public void onSuccess(List<Integer> resultSet) {
					}
				});
			}
		}

	}

	/*public void insertMapImage(String imageID, String image)
	{
		if(Database.isSupported())
		{
			dbService.insertMapImage(imageID, image, new RowIdListCallback() {
				public void onFailure(DataServiceException e) { 
				     Window.alert(e.getMessage()); 
				  } 
				public void onSuccess(List<Integer> resultSet) {
					//Window.alert("success saving");
				}
			});
		}
	}*/
	
	/*public void clearMap()
	{
		if(Database.isSupported())
		{
			dbService.clearMap(new VoidCallback() { 
				  public void onFailure(DataServiceException e) { 
				    Window.alert("Clear database.");
				  } 
				  public void onSuccess() { 
				  } 
			});
		}
	}*/
	
	public void saveVINLocation(Feature prodLoc)
	{
		
		if (Database.isSupported())
		{
			dbService.insertProductLocations(prodLoc.getFeaturePoints(), new RowIdListCallback() {
				public void onFailure(DataServiceException e) { 
				     Window.alert("Failed to save VIN"); 
				     Window.alert(e.getMessage());
				  } 
				public void onSuccess(List<Integer> resultSet) {
				}
			});
		}

	}
	
	public MapClientDataService getMapClientDataService()
	{
		return dbService;
	}
	
}
