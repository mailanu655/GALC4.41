/**
 * 
 */
package com.honda.galc.client.schedule;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Jan 17, 2013
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
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
	CHANGE_TO_UNSENT            (8,"Unsend the Lot"),
	GO_WELD_ON                  (9,"Go WeldOn"),
	
	
	PROCESSED_ORDER_CHANGED		(10),
	PROCESSED_PRODUCT_CHANGED	(11),
	UPCOMING_ORDER_CHANGED		(12),
	ON_HOLD_ORDER_CHANGED 		(13),
	CURRENT_ORDER_CHANGED		(14),
	EXPECTED_PRODUCT_CHANGED	(15),
	UPCOMING_PRODUCT_STAMPING_SEQ_CHANGED	(16),
	PROCESSED_PRODUCT_STAMPING_SEQ_CHANGED	(17),
	
	ORDER_COMPLETED				(20),
	PROCESS_PRODUCT				(21),
	GENERATE_SN					(22),
	
	UPDATE_VIEW					(30),
	
	COMPLETE_LOT                (40, "Complete"),
	SET_CURRENT_LOT             (41, "Set Current"),
	SET_NEXT                    (42,"Set Next"),
	
	DELETE_LOT                  (50,"Delete Lot"),
	UPDATE_LOT_SIZE             (51,"Update Lot Size"),
	UPDATE_COMMENT              (52,"Update Comment"),
	FINISH_LOT              	(53,"Finish Lot");
	
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
