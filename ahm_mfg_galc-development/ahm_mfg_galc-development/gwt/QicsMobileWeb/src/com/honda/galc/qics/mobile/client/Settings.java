package com.honda.galc.qics.mobile.client;

import com.google.gwt.core.client.GWT;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.events.SettingsChangedEvent;
import com.honda.galc.qics.mobile.client.events.SettingsChangedEventData;
import com.honda.galc.qics.mobile.client.utils.BaseSettings;
import com.smartgwt.mobile.client.data.Storage;

/**
 * The settings class is where application configuration is done.  Functionality
 * is provided to make these values configurable at run time.  Value changes fire
 * SettingsChangedEvent.
 * 
 * @author vfc01346
 *
 */
public class Settings extends BaseSettings {
	
	public static final String DEFECT_FILTER_VALUE_ALL = "ALL";
	public static final String DEFECT_FILTER_VALUE_OPEN = "OUTSTANDING";
	public static final String DEFECT_FILTER_VALUE_CLOSED = "REPAIRED";
	
	public static final String DEFECT_LIST_FILTER_KEY = "defect filter";
	public static final String DEFECT_LIST_FILTER_DEFAULT_VALUE = DEFECT_FILTER_VALUE_OPEN;

	public static final String USER_KEY = "user";
	public static final String USER_DEFAULT_VALUE = "";

	
	// This is the base path to REST services.  
	private static String DEFAULT_REST_PATH = "RestWeb";
	
	// This is the server that responds to REST service requests.
	// This is the host and port serving the application
	// e.g., "10.142.252.68:8081"
	private static String REST_HOST_DEFAULT_VALUE = getBasePageHost();
	
	public static String PROCESS_POINT_KEY = "process point";
	public static String PROCESS_POINT_DEFAULT_VALUE = "PP10079";
	
	public static String REPAIR_DEPARTMENT_KEY = "repair department";
	public static String REPAIR_DEPARTMENT_DEFAULT_VALUE = "AF";
	

	public static String getRestWeb() {
		return "http://" + REST_HOST_DEFAULT_VALUE + "/" + DEFAULT_REST_PATH;
	}
	
	

	public static String getRepairDepartment() {
		return getProperty( REPAIR_DEPARTMENT_KEY, REPAIR_DEPARTMENT_DEFAULT_VALUE);
	}
	
	static public String getProcessPoint() {
		return getProperty( PROCESS_POINT_KEY, PROCESS_POINT_DEFAULT_VALUE );
	}
	
	static public void setProcessPoint( String processPoint ) {
		setProperty( PROCESS_POINT_KEY, processPoint );
	}
	
	static public String getUser() {
		return getProperty( USER_KEY, USER_DEFAULT_VALUE );
	}
	
	static public void setUser( String user ) {
		if ( user == null ) {
			user = "";
		}
		setProperty( USER_KEY, user );
	}
	
	
	static public String getDefectListFilter() {
		return getProperty( DEFECT_LIST_FILTER_KEY, DEFECT_LIST_FILTER_DEFAULT_VALUE );
	}
	
	static public void setDefectListFilter( String filter ) {
		assert DEFECT_FILTER_VALUE_ALL.equals( filter ) ||
			DEFECT_FILTER_VALUE_OPEN.equals( filter ) ||
			DEFECT_FILTER_VALUE_CLOSED.equals( filter );
		 
		setProperty( DEFECT_LIST_FILTER_KEY, filter );
	}
	
	/**
	 * Returns the base page host , e.g., the string 
	 * between http:// up to but excluding the first /.
	 * example: 127.0.0.1:8888
	 * @return
	 */
	private static String getBasePageHost() {
		String s = GWT.getHostPageBaseURL();
		int i = s.indexOf('/', 8 );
		return s.substring("http://".length(), i );
	}
}
