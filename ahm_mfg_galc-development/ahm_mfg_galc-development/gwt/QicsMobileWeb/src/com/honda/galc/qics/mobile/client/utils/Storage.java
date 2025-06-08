package com.honda.galc.qics.mobile.client.utils;

public final class Storage {

	private Storage() {}
	
	public static native boolean isStorageApiSupported() /*-{
		if( window.File && window.FileReader && window.FileList && window.Blob ) {
			return true;
		} else {
			return false;
		}
	}-*/;
}
