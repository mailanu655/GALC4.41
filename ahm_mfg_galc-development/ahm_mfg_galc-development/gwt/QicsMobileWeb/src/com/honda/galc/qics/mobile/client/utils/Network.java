package com.honda.galc.qics.mobile.client.utils;

public final class Network {

    public static native String getNetworkStatus() /*-{
        if ($wnd.navigator.network == null) throw @java.lang.RuntimeException::new(Ljava/lang/String;)("The Cordova Network API is not installed.");
       
        var networkState = navigator.network.connection.type;

	    var states = {};
	    states[Connection.UNKNOWN]  = 'Unknown connection';
	    states[Connection.ETHERNET] = 'Ethernet connection';
	    states[Connection.WIFI]     = 'WiFi connection';
	    states[Connection.CELL_2G]  = 'Cell 2G connection';
	    states[Connection.CELL_3G]  = 'Cell 3G connection';
	    states[Connection.CELL_4G]  = 'Cell 4G connection';
	    states[Connection.NONE]     = 'No network connection';

    	alert('Connection type: ' + states[networkState]);
    	return states[networkState];
    	

    }-*/;

    private Network() {}
}
