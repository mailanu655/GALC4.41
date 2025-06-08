package com.honda.galc.client.datacollection.device;

/**
 * 
 * <h3>Side</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Side description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jan 31, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 31, 2012
 */
public enum Side {
	Left_Side("L"), Right_Side("R"), ERROR("E"), EMPTY("");

    private final String id;

    private Side(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Side getSide(String id) {
    	Side[] values = Side.values();
        for(int i= 0; i < values.length; i++){
        	if(values[i].getId().equals(id))
        		return values[i];
        }
        
        return null;
    }
}