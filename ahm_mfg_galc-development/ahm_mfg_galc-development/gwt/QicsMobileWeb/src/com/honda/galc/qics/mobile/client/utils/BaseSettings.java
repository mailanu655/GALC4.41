package com.honda.galc.qics.mobile.client.utils;

import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.events.SettingsChangedEvent;
import com.honda.galc.qics.mobile.client.events.SettingsChangedEventData;
import com.smartgwt.mobile.client.data.Storage;

/**
 * The settings class is where configuration is done.  Functionality
 * is provided to make these values configurable at run time.  Value changes fire
 * SettingsChangedEvent.
 * 
 * @author vfc01346
 *
 */
public class BaseSettings  {
	

	
	private static Storage getStorage() {
		if ( isStorageApiSupported() ) {
			return Storage.LOCAL_STORAGE;
		} else {
			return Storage.SESSION_STORAGE;
		}
	}

	public static boolean isStorageApiSupported() {
		return com.honda.galc.qics.mobile.client.utils.Storage.isStorageApiSupported();
	}
		

	
	public  static String getProperty( String key ) {
		return getStorage().get(key);
	}
	
	public static String getProperty( String key, String defaultValue ) {
		String value = getProperty( key );
		if ( value == null ) {
			value = defaultValue;
		}
		return value;
	}
	
	public static void setProperty( String key, String newValue ) {
		String oldValue = getProperty( key );
		boolean change = true;
		if ( oldValue == null && newValue == null ) {
			change = false;
		} else if ( oldValue != null && oldValue.equals( newValue)) {
			change = false;
		} 
		if ( change ) {
			getStorage().put( key, newValue);
			SettingsChangedEventData eventData = new SettingsChangedEventData( key, oldValue, newValue);
			SettingsChangedEvent event = new SettingsChangedEvent( eventData );
			PubSubBus.EVENT_BUS.fireEvent(event);		
		}
	}
	

	
	
	

}
