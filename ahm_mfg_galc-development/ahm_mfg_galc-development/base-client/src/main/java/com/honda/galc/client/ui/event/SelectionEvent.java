package com.honda.galc.client.ui.event;

/**
 * 
 * <h3>SelectionEvent Class description</h3>
 * <p> SelectionEvent description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 10, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class SelectionEvent {
	private Object source;
	private int eventType;
	
	public static int PLANT_SELECTED = 1;
	public static int DEPARTMENT_SELECTED = 2;
	public static int PROCESSPOINT_SELECTED = 3;
	public static int PARENT_PART_SELECTED =4;
	
	public static int POPULATED = 10;
	public static int SELECTING = 11;
	public static int SELECTED = 12;
	public static int UNSELECTED = 13;
	
	public static int MODEL_YEAR_SELECTED = 20;
	public static int MODEL_SELECTED = 21;
	public static int MODEL_TYPE_SELECTED = 22;
	public static int MODEL_OPTION_SELECTED = 23;
	public static int EXT_COLOR_SELECTED = 24;
	public static int INT_COLOR_SELECTED = 25;
	public static int PRODUCT_TYPE_SELECTED = 30;
	
	
	
	public SelectionEvent(Object source,int eventType) {
		this.source = source;
		this.eventType = eventType;
	}
	
	public Object getSource() {
		return source;
	}
	
	public boolean isSource(Object object) {
		return source.equals(object);
	}
	
	public int getEventType() {
		return eventType;
	}
	
	public boolean isEvent(int eventType) {
		return this.eventType == eventType;
	}
	
	public boolean isEventFromSource(int eventType,Object object) {
		return isEvent(eventType) && isSource(object);
	}
	
}