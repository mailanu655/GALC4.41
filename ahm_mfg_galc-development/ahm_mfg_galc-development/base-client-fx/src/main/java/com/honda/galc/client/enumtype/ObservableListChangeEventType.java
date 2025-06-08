package com.honda.galc.client.enumtype;

import com.honda.galc.enumtype.IdEnum;

/**
 * <h3>Class description</h3>
 * ObservableListChangeEventType Class Description
 * Event Types used by ObservableListChangeEvent. 
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
 * <TD>Prakash Dalvi</TD>
 * <TD>Feb 04 2017</TD>
 * <TD>1.0</TD>
 * </TD>
 * <TD>Initial Release</TD>
 * </TR>
 */
public enum ObservableListChangeEventType implements IdEnum<ObservableListChangeEventType>{
	CHANGE_SELECTION		(0, "CHANGE_SELECTION"),
	WARNING 	(1, "WARNING"),
	ERROR		(2, "ERROR"),
	CLEAR		(3, "CLEAR"),
	ADD			(4, "ADD");
	
	private int id = 0;
	private String name = "";

	private ObservableListChangeEventType(int id, String name){
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

}
