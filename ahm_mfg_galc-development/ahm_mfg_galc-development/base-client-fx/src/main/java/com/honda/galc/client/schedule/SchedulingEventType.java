package com.honda.galc.client.schedule;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * <h3>Class description</h3>
 * Scheduling Event Type Class Description
 * Event Types used by the client. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 */

public enum SchedulingEventType implements IdEnum<SchedulingEventType> {
	
	NONE						(-1,"None"),
	MOVE_UP 					(0,"Move Up"),
	MOVE_DOWN 					(1,"Move Down"),
	HOLD 						(2,"Hold"),
	RELEASE 					(3,"Release"),
	SELECT_LAST_ORDER 			(4,"Select Last Lot"),
	SELECT_LAST_PRODUCT     	(5,"Select Last Product"),
	ADD_LOT						(6,"Add Lot"),
	CREATE_SHIPPING_LOT 	 	(7,"Create Shipping Lot"),
	CHANGE_TO_UNSENT			(8,"Change to Unsent"),
	
	
	PROCESSED_ORDER_CHANGED		(10),
	PROCESSED_PRODUCT_CHANGED	(11),
	UPCOMING_ORDER_CHANGED		(12),
	ON_HOLD_ORDER_CHANGED 		(13),
	CURRENT_ORDER_CHANGED		(14),
	EXPECTED_PRODUCT_CHANGED	(15),
	PRODUCT_ID_INPUT			(16),
	SCHEDULE_CLIENT_PROCESSED	(17),
	
	ORDER_COMPLETED				(20,"Set Complete"),
	PROCESS_PRODUCT				(21),
	GENERATE_SN					(22,"Generate SN"),
	
	REFRESH_SCHEDULE_CLIENT		(30),
	
	COMPLETE_LOT                (40, "Complete"),
	SET_CURRENT_LOT             (41, "Set Current"),
	PASTE                       (43, "Paste"),
	CUT                         (42, "Cut"),
	CANCEL                      (44, "Cancel"),
	REFRESH_SCHEDULE_CLIENT_ON_TIMEOUT   (45),
	PROCESS_PRODUCT_WITH_PLATFORM   (46),
	EDIT                      (47, "Edit");
	
    private final int _id;
    private final String _message;

    private SchedulingEventType(int id){
    	this(id,null);
    }
    
    private SchedulingEventType(int id,String message) {
        _id = id;
        _message = message;
    }

    public int getId() {
        return _id;
    }
    
    public String getMessage() {
    	return _message;
    }
   
     public static SchedulingEventType getType(int id) {
        return EnumUtil.getType(SchedulingEventType.class, id);
    }
     
     public static SchedulingEventType getType(String message) {
    	 for (SchedulingEventType type : values()) {
    		 if(!StringUtils.isEmpty(type.getMessage()) && type.getMessage().equals(message)) return type;
    	 }
         return SchedulingEventType.NONE;
     }
}
