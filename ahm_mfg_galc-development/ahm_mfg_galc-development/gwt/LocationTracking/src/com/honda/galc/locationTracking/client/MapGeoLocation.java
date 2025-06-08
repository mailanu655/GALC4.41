package com.honda.galc.locationTracking.client;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.Callback;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;

public class MapGeoLocation extends Geolocation {

	private static final Projection DEFAULT_PROJECTION = new Projection(             "EPSG:4326");
	private LonLat userLonLat;
	
	/**
	 * Starts the Geolocation, and handles the updates...per the documentation for watchPosition
	 * Repeatedly calls the given callback with the user's position, as it changes, with additional options. 
	 * The frequency of these updates is entirely up to the browser. 
	 * There is no guarantee that updates will be received at any set interval, 
	 * but are instead designed to be sent when the user's position changes. 
	 * This method should be used instead of polling the user's current position.
	 */
	public MapGeoLocation()
	{
	     // Start GeoLocation stuff (note that the GeoLocation is just plain GWT stuff)                 
		Geolocation geoLocation = Geolocation.getIfSupported();                 
		if (geoLocation == null) {                     
		}
		else {                     
			final Geolocation.PositionOptions geoOptions = new Geolocation.PositionOptions();                     
			geoOptions.setHighAccuracyEnabled(true);
			geoLocation.watchPosition(new Callback<Position, PositionError>() {                         
				public void onFailure(final PositionError reason) {                             
					}                           
				public void onSuccess(final Position result) {                             
					// put the received result in an openlayers LonLat                             
					// object                             
					userLonLat = new LonLat(                                     
							result.getCoordinates().getLongitude(),                                     
							result.getCoordinates().getLatitude());                             
            
					}
				}, geoOptions);
			}
	}
	
	public LonLat getLonLat()
	{
		return userLonLat;
	}
}
