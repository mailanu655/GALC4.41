package com.honda.galc.qics.mobile.client.utils;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import com.googlecode.gwtphonegap.client.connection.Connection;

public class PhGap {
	
    private static final PhoneGap phoneGap = GWT.create(PhoneGap.class);

    private static PhGap phGap = null;
    private boolean isAvailable = false;

    private PhGap() {
    	   phoneGap.addHandler(new PhoneGapAvailableHandler() {

               @Override
               public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
            	   isAvailable = true;
            	   Log.info("PhoneGap is ready");
            	   //phoneGap.getLog().setRemoteLogServiceUrl(url);
               }
       });

       phoneGap.addHandler(new PhoneGapTimeoutHandler() {

               @Override
               public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
                   //can not start phonegap - something is for with your setup
            	   Log.warn("PhoneGap timed out");

               }
       });

       phoneGap.initializePhoneGap();
    }
    
    public static void initialize() {
    	getInstance();
    }
    
    public static PhGap getInstance() {
    	if ( phGap == null ) {
    		phGap = new PhGap();
    	}
    	return phGap;
    }
    
	public PhoneGap getPhoneGap() {
		return phoneGap;
	}
	
	public boolean isAvailable() {
		return this.isAvailable;
	}

	public static void doTactalFeedback() {
		getInstance().getPhoneGap().getNotification().vibrate(200);
	}
	
	public static String getDeviceOsVersion() {
		return getInstance().getPhoneGap().getDevice().getVersion();
	}
	
	public static String getPhoneGapVersion() {
		return getInstance().getPhoneGap().getDevice().getPhoneGapVersion(); 
	}
	
	public static String getDeviceModel() {
		return getInstance().getPhoneGap().getDevice().getModel();
	}
	
	public static String getDevicePlatform() {
		return getInstance().getPhoneGap().getDevice().getPlatform();
	}
	
	public static String getDeviceUuid() {
		return getInstance().getPhoneGap().getDevice().getUuid();
	}

	/**
	 * Returns string describing connection type
	 * @return String value like "none", "unknown",  "ethernet", "wifi", "2g", "3g", "4g"
	 */
	public static String getConnectionType() {
		return getInstance().getPhoneGap().getConnection().getType();
	}
	
	/**
	 * Is this device connected to something.
	 * @return true if connected, false otherwise
	 */
	public static boolean isConnected() {
		return ! Connection.NONE.equals(getConnectionType());
	}

}
