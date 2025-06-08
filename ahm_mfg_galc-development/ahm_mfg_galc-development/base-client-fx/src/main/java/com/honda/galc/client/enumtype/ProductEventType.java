package com.honda.galc.client.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * <h3>Class description</h3>
 * ProductEventType Class Description
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
 * <TD>Shweta Kadav</TD>
 * <TD>Oct 06 2016</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 */

public enum ProductEventType implements IdEnum<ProductEventType> {
	
	PRODUCT_INPUT_NG   		    (0),
	PRODUCT_INPUT_OK    		(1),
	PRODUCT_DEFECT_ACCEPT    	(2),
	PRODUCT_DEFECT_DONE     	(3),
	PRODUCT_DIRECT_PASSED     	(4),
	PRODUCT_DEFECT_VOID_ALL    	(5),
	PRODUCT_APPLY_RECENT_DEFECT (6),
	PRODUCT_REPAIR_DEFECT       (7),
	PRODUCT_REPAIR_DEFECT_DONE  (8),
	PRODUCT_INPUT_RECIEVED 		(9),
	PRODUCT_DIRECTPASS_READY	(10),
	PRODUCT_CHECK_DONE			(11),
	PRODUCT_SEND_TO_FINAL   	(12),
	PRODUCT_UPDATE_REPAIR_AREA	(13),
	PRODUCT_SHIPPED             (14),
	PRODUCT_PREVIOUS_LINE_INVALID (15),
	PRODUCT_INPUT_PROCESS		(16),
	PRODUCT_SCANNED				(17),
	PRODUCT_RESET				(18),
	PRODUCT_UPDATE_TRACKING		(19),
	TRANSACTION_ID_INPUT_RECIEVED (20);
	
    private final int _id;

    
    private ProductEventType(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public static ProductEventType getType(int id) {
        return EnumUtil.getType(ProductEventType.class, id);
    }
     
    
}
