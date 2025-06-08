package com.honda.galc.constant;
/**
 * 
 * <h3>RepairMethod</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RepairMethod description </p>
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
 * <TD>Feb 7, 2018</TD>
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
 * @since Feb 7, 2018
 */
public enum RepairMethod {
	Method0(0, "No Data"), 
	Method100(100, "Primary Install Equipment - Complete with GALC Build Data"), 
	Method101(101, "Primary Install Equipment - Complete with Local Build Data"), 
	Method102(102, "Primary Install Equipment - Complete with One Retry"), 
	Method103(103, "Primary Install Equipment - Complete with Two Retrys"), 
	Method104(104, "Primary Install Equipment - Complete with Three Retrys"), 
	Method105(105, "Primary Install Equipment - Complete with Manual Repair"), 
	Method106(106, "Primary Install Equipment - Complete with PPA"), 
	Method107(107, "Primary Install Equipment - Not Complete Repair Required"), 
	Method200(200, "In-line Repair Equipment - Complete with GALC Build Data"), 
	Method201(201, "In-line Repair Equipment - Complete with Local Build Data"), 
	Method202(202, "In-line Repair Equipment - Complete with One Retry"), 
	Method203(203, "In-line Repair Equipment - Complete with Two Retrys"), 
	Method204(204, "In-line Repair Equipment - Complete with Three Retrys"), 
	Method205(205, "In-line Repair Equipment - Complete with Manual Repair"), 
	Method206(206, "In-line Repair Equipment - Complete with PPA"), 
	Method207(207, "In-line Repair Equipment - Not Complete Repair Required"), 
	Method300(300, "In-line Repair - Manual Complete"), 
	Method400(400, "Off-line Repair - Complete"), 
	Method500(500, "Quality Confirmation Check - Complete"), 
	Method501(501, "Quality Confirmation Check - Failed");
	
	
	private final int methodId;
    private final String description;

    private RepairMethod(int id, String description) {
    	this.methodId = id;
        this.description = description;
    }

	public int getMethodId() {
		return methodId;
	}

	public String getDescription() {
		return description;
	}
	
	public static RepairMethod fromId(int id){
		for(RepairMethod rm : RepairMethod.values())
			if(id == rm.getMethodId())
				return rm;
		
		return null;
	}

	public static RepairMethod fromId(String mid) {
		return fromId(Integer.parseInt(mid));
	}
    
    
}
