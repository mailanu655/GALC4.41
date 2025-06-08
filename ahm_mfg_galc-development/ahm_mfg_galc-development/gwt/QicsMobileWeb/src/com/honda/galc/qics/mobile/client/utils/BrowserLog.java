package com.honda.galc.qics.mobile.client.utils;

/**
 * This logs a message to the browser's console.  
 * 
 * @author vfc01346
 *
 */
public final class BrowserLog {
	
	/**
	 * Log a message to the browser.
	 *
	 * @param message the message
	 */
	native static void log( String message) /*-{
    	console.log( "me:" + message );
	}-*/;
	
	private BrowserLog() {}
}
