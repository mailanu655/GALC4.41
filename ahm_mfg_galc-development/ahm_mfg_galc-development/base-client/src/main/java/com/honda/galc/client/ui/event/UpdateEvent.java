package com.honda.galc.client.ui.event;

/**
 * 
 * <h3>UpdateEvent</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> UpdateEvent description </p>
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
 * @author Paul Chou
 * Dec 22, 2010
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class UpdateEvent {
	
	private Object source;
	private int eventType;
	
	public static final int CREATE_PART_NAME = 0;
	public static final int DELETE_PART_NAME = 1;
	public static final int UPDATE_PART = 2;
	
	public UpdateEvent(Object source,int eventType) {
		this.source = source;
		this.eventType = eventType;
	}

	public boolean isUpdatePartName(){
		return eventType == CREATE_PART_NAME || eventType == DELETE_PART_NAME ||
		eventType == UPDATE_PART;
	}
	
	//Getters & Setters
	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
	
	
}
