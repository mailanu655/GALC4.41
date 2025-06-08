package com.honda.galc.client.ui.event;

/**
 * 
 * <h3>EventType Class description</h3>
 * <p> EventType description </p>
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
 * May 9, 2011
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
public class EventType {
	
	public static int CHANGED = 1;
	public static int INSERTED = 2;
	public static int DELETED = 3;
	
	public static int SELECTED = 100;
	public static int SELECTING = 101;
	public static int POPULATED = 102;
	public static int UNSELECTED = 103;
	
	public static int SUCCEEDED = 200;
	public static int FAILED = 201;
	public static int PLC_READY = 200;
	
	public static int WARNING_OVERRIDDEN = 300;
	
	// torque related events 
	public static int TORQUE_DISABLE_ALL = 400;
	public static int TORQUE_ENABLE = 401;
}
